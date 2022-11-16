package com.apgroup.pms.thread;

import com.apgroup.pms.error.ErrorCode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.apgroup.pms.data.entity.Order;
import com.apgroup.pms.data.entity.RawMaterial;
import com.apgroup.pms.data.repository.OrderRepository;
import com.apgroup.pms.data.repository.RawMaterialRepository;
import com.apgroup.pms.dto.OrderSheet;
import com.apgroup.pms.type.OrderStatusCode;
import com.apgroup.pms.utils.OrderManagementUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 생산 설비 쓰레드
 */
@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class ProductionLine implements Runnable {
	
	private final RawMaterialRepository rawMaterialRepository;
	
	private final OrderRepository orderRepository;
	
	private final int MAX_REMAIN_AMOUNT = 200;				// 원료당 최대 잔량

	private static final int MAX_PRODUCTION_PER_DAY = 30;	// 하루 총 생산 개수

	private static int TODAY_PRODUCTION = 0;				// 금일 생산 개수
	
	private static boolean isForward = false;				// 일괄 발송 여부
	
	private boolean isWorkTime = true;

	/**
	 * 초기 원료 세팅
	 */
	@PostConstruct
	public void init() {
		rawMaterialRepository.save(new RawMaterial("A", 200, 0));
		rawMaterialRepository.save(new RawMaterial("B", 200, 0));
		rawMaterialRepository.save(new RawMaterial("C", 200, 0));
		rawMaterialRepository.save(new RawMaterial("D", 200, 0));	
	}

	@Scheduled(fixedDelay = 20000, initialDelay = 1000)
	public void logStatus() {
		StringBuilder sb = new StringBuilder("\n");
		
		sb.append("=====================================").append("\n");
		sb.append(">> ").append(WorkTimer.getCurrentTime()).append("\n");
		sb.append("1. 주문 목록").append("\n");
		// 생산/대기 중인 주문 목록
		List<Order> orders = orderRepository.findAll();
		
		List<Order> progressList = new ArrayList<Order>();
		
		List<Order> receiptList = new ArrayList<Order>();
		
		orders.stream().forEach(order -> {
			if (OrderStatusCode.PRODUCT_IN_PRODUCTION.equals(OrderStatusCode.getByCode(order.getOrderStatus()))) {
				progressList.add(order);
				
			} else if (OrderStatusCode.ORDER_RECEPTION.equals(OrderStatusCode.getByCode(order.getOrderStatus()))) {
				receiptList.add(order);
			}
		});
		
		sb.append(">> 주문 상태 : ").append(OrderStatusCode.PRODUCT_IN_PRODUCTION.getDescription()).append(" / ").append(progressList.size()).append("건").append("\n");
		
		if (progressList.size() > 0) {
			progressList.stream().forEach(order -> {
				sb.append("[주문번호 : ").append(order.getOrderNumber());
				sb.append(" / 주문코드 : ").append(order.getOrderCode()).append("]");
				sb.append("\n");
			});
		}
		
		sb.append(">> 주문 상태 : ").append(OrderStatusCode.ORDER_RECEPTION.getDescription()).append(" / ").append(receiptList.size()).append("건").append("\n");
		
		if (receiptList.size() > 0) {
			receiptList.stream().forEach(order -> {
				sb.append("[주문번호 : ").append(order.getOrderNumber());
				sb.append(" / 주문코드 : ").append(order.getOrderCode()).append("]");
				sb.append("\n");
			});
		}
		
		// 원료 잔량
		List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
		
		sb.append("\n").append("2. 원료 잔량").append("\n");
		rawMaterials.stream().forEach(rawMaterial -> {
			sb.append(">> 원료명 : ").append(rawMaterial.getId())
			.append(" / 잔량 : ").append(rawMaterial.getRemainAmount())
			.append(" / 재고량 : ").append(rawMaterial.getStockAmount()).append("\n");
		});
		
		
		sb.append("=====================================");
		
		log.info("{}", sb.toString());
	}
	
	@Override
	public void run() {
		while (true) {
			if (WorkTimer.isWorkTime()) {
				if (isWorkTime)  {
					TODAY_PRODUCTION = 0;
					log.info("[{}] 근무 시작", WorkTimer.getCurrentTime());
					isWorkTime = false;
				}
				isForward = false;
				
				if (!OrderManagementUtils.isOrderQueueEmpty() && MAX_PRODUCTION_PER_DAY - TODAY_PRODUCTION > 0) { // 하루 최대 생산량을 못채운 경우
					OrderSheet orderSheet = OrderManagementUtils.getOrderSheet();
					log.info("[{}][주문번호 : {}] 주문 확인", WorkTimer.getCurrentTime(), orderSheet.getOrderNumber());
					
					String orderNumber = orderSheet.getOrderNumber();					
					
					Order order = orderRepository.findById(orderNumber).get();

					
					if (isRequiredOneRawMaterial(orderSheet)) { // 효능이 하나이고, 해당 원료가 있는 경우
						RawMaterial rawMaterial = rawMaterialRepository.findById(orderSheet.getEffect1()).get();
						
						if (isAvailableProduction(rawMaterial.getRemainAmount(), rawMaterial.getStockAmount(), orderSheet.getRate1())) {
							orderRepository.updateStatus(orderNumber, OrderStatusCode.PRODUCT_IN_PRODUCTION.getCode());
							production(rawMaterial, orderSheet.getRate1());
							TODAY_PRODUCTION++;	
							
							orderRepository.updateStatus(orderNumber, OrderStatusCode.PRODUCT_PRODUCTION_COMPLETED.getCode());
							
							log.info("[{}][주문번호 : {}] {} 제품 생산 완료. 금일 생산량 : {}", WorkTimer.getCurrentTime(), orderSheet.getOrderNumber(), orderSheet.getOrder(), TODAY_PRODUCTION);
							orderRepository.updateStatus(orderNumber, OrderStatusCode.READY_TO_SHIP.getCode());
							
							OrderManagementUtils.addForwadingQueue(order);
							
							if (rawMaterial.getRemainAmount() == 0 && rawMaterial.getStockAmount() > 0) {
								supplementMaterial(rawMaterial);
							}
						} else {
							log.info("[{}][주문번호 : {}] 생산 불가 - 원료 및 재고 부족", WorkTimer.getCurrentTime(),  orderSheet.getOrderNumber());
							order.setOrderStatus(OrderStatusCode.ORDER_CANCEL.getCode());
							order.setError(ErrorCode.RAW_MATERIAL_SHORT_ON_STOCK.getMessage());
							orderRepository.update(order);
						}
					} else if (isRequiredTwoRawMaterials(orderSheet)) { // 효능이 두개이고, 해당 원료가 있는 경우
						RawMaterial rawMaterial1 = rawMaterialRepository.findById(orderSheet.getEffect1()).get();
						RawMaterial rawMaterial2 = rawMaterialRepository.findById(orderSheet.getEffect2()).get();
						if (isAvailableProduction(rawMaterial1.getRemainAmount(), rawMaterial1.getStockAmount(), orderSheet.getRate1())
								&& isAvailableProduction(rawMaterial2.getRemainAmount(), rawMaterial2.getStockAmount(), orderSheet.getRate2())) {
							
							orderRepository.updateStatus(orderNumber, OrderStatusCode.PRODUCT_IN_PRODUCTION.getCode());
							production(rawMaterial1, orderSheet.getRate1());
							production(rawMaterial2, orderSheet.getRate2());
							orderRepository.updateStatus(orderNumber, OrderStatusCode.PRODUCT_PRODUCTION_COMPLETED.getCode());
							TODAY_PRODUCTION++;
							
							log.info("[{}][주문번호 : {}] {} 제품 생산 완료. 금일 생산량 : {}", WorkTimer.getCurrentTime(),orderSheet.getOrderNumber(), orderSheet.getOrder(), TODAY_PRODUCTION);
							orderRepository.updateStatus(orderNumber, OrderStatusCode.READY_TO_SHIP.getCode());
							OrderManagementUtils.addForwadingQueue(order);
							
							if (rawMaterial1.getRemainAmount() == 0 && rawMaterial1.getStockAmount() > 0) {
								supplementMaterial(rawMaterial1);
							}
							if (rawMaterial2.getRemainAmount() == 0 && rawMaterial2.getStockAmount() > 0) {
								supplementMaterial(rawMaterial2);
							}
							
						} else {
							log.info("[{}][주문번호 : {}] 생산 불가 - 원료 및 재고 부족", WorkTimer.getCurrentTime(),  orderSheet.getOrderNumber());
							order.setOrderStatus(OrderStatusCode.ORDER_CANCEL.getCode());
							order.setError(ErrorCode.RAW_MATERIAL_SHORT_ON_STOCK.getMessage());
							orderRepository.update(order);
						}
					} else {
						log.info("[{}][주문번호 : {}] 생산 불가 - 효능 단종", WorkTimer.getCurrentTime(), orderSheet.getOrderNumber());
						order.setOrderStatus(OrderStatusCode.ORDER_CANCEL.getCode());
						order.setError(ErrorCode.RAW_MATERIAL_DISCONTINUED.getMessage());
						orderRepository.update(order);
					}				
				}
			} else {
				if (!isWorkTime) {
					log.info("[{}] 근무 종료", WorkTimer.getCurrentTime());
					isWorkTime = true;
				}
				if (!isForward) {
					isForward = forwarding();
				}	
			}
		}
	}

	private boolean isRequiredOneRawMaterial(OrderSheet orderSheet) {
		return (orderSheet.getOrder().length() == 3 && rawMaterialRepository.existsById(orderSheet.getEffect1()));
	}
	
	private boolean isRequiredTwoRawMaterials(OrderSheet orderSheet) {
		return rawMaterialRepository.existsById(orderSheet.getEffect1()) && rawMaterialRepository.existsById(orderSheet.getEffect2());
	}

	
	/**
	 * 제품 일괄 발송
	 */
	public boolean forwarding() {
		if (!OrderManagementUtils.isFowardingQueueEmpty()) {
			List<Order> orders = OrderManagementUtils.getForwardingList();
			
			orders.stream().forEach(order -> { 
				order.setOrderStatus(OrderStatusCode.SHIPMENT_COMPLETED.getCode());
				orderRepository.update(order);
			});
			
			log.info("[{}] 제품 일괄 발송 완료 : {}", WorkTimer.getCurrentTime(), orders.size());
		}
		
		return true;
	}
	
	/**
	 * 제품 생산
	 */
	public void production(RawMaterial rawMaterial, int requirement) {
		int remains = rawMaterial.getRemainAmount();
		
		if (remains - requirement >= 0) {	// 원료 잔량이 필요량보다 많음. 생산가능
			rawMaterial.setRemainAmount(remains - requirement);
			rawMaterialRepository.update(rawMaterial);
		}  else {
			requirement = requirement - remains;
			
			int stockAmount = rawMaterial.getStockAmount();
			
			rawMaterial.setRemainAmount(0);
			
			rawMaterialRepository.update(rawMaterial);
			
			if (stockAmount >= MAX_REMAIN_AMOUNT) { // 재고있음. 원료 최대로 보충
				supplementMaterial(rawMaterial);
				rawMaterial.setRemainAmount(MAX_REMAIN_AMOUNT);
				rawMaterial.setStockAmount(stockAmount - requirement);
				rawMaterialRepository.update(rawMaterial);
			} else { // 재고 있음. 재고량만큼 보충
				supplementMaterial(rawMaterial);
				rawMaterial.setStockAmount(0);
				rawMaterial.setRemainAmount(stockAmount - requirement);
				rawMaterialRepository.update(rawMaterial);
			} 
		}
		
	}
	
	/**
	 * 해당 원료 잔량 및 재고량을 통해서 제품 생산 가능한지 여부
	 */
	private boolean isAvailableProduction(int remains, int stockAmount, int requirement) {
		if (remains - requirement >= 0) {	// 원료 잔량이 필요량보다 많음. 생산가능
			return true;
		} else if (stockAmount == 0) { // 재고 없음. 생산 불가
			return false;
		} else if (stockAmount < (requirement - remains)) { // 재고 부족. 생산 불가
			return false;
		} else { // 재고 있음
			return true;
		}	
	}

	public void supplementMaterial(RawMaterial rawMaterial) {
		log.info("[{}][{} 원료] 보충 시작", WorkTimer.getCurrentTime(), rawMaterial.getId());
		try {
			if (WorkTimer.isAbleCharge()) {
				Thread.sleep(40000);
			} else {
				long remainWorkMinute = WorkTimer.remainWorkMinute();
				Thread.sleep(remainWorkMinute * 1000);

				while (!WorkTimer.isWorkTime()) {}

				Thread.sleep((40 - remainWorkMinute) * 1000);
			}
			
			log.info("[{}][{} 원료] 보충 완료", WorkTimer.getCurrentTime(), rawMaterial.getId());
		} catch (InterruptedException e) {
			log.error("ProductLine is dead", e);
		}
	}
	
	public static boolean isProductionPossible() {
		return MAX_PRODUCTION_PER_DAY - TODAY_PRODUCTION > 0;
	}

	public static int remainTodayProduction() {
		return MAX_PRODUCTION_PER_DAY - TODAY_PRODUCTION;
	}

	public static int getMaxProductionCount() {
		return MAX_PRODUCTION_PER_DAY;
	}

	public static void initTodayProduction() {
		TODAY_PRODUCTION = 0;
	}
	
}
