package com.apgroup.pms.service;

import com.apgroup.pms.error.exception.EntityNotExistException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.apgroup.pms.data.entity.Order;
import com.apgroup.pms.data.repository.OrderRepository;
import com.apgroup.pms.dto.OrderSheet;
import com.apgroup.pms.dto.request.OrderRequest;
import com.apgroup.pms.dto.response.OrderResponse;
import com.apgroup.pms.thread.ProductionLine;
import com.apgroup.pms.thread.WorkTimer;
import com.apgroup.pms.error.ErrorCode;
import com.apgroup.pms.type.OrderStatusCode;
import com.apgroup.pms.utils.OrderManagementUtils;

import lombok.RequiredArgsConstructor;

/**
 * 주문 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class OrderService {
	private static LinkedBlockingQueue<OrderSheet> orderQueue = new LinkedBlockingQueue<OrderSheet>();

	private final OrderRepository repository;

	public OrderResponse getOrder(String orderNumber) {
		Optional<Order> optionalOrder = repository.findById(orderNumber);

		Order order = optionalOrder.orElseThrow(() -> {
			throw new EntityNotExistException("not exists order : " + orderNumber);
		});

		return getOrderResponse(order);
	}
	
	public List<OrderResponse> getOrders() {
		List<OrderResponse> list = new ArrayList<>();
		
		repository.findAll().forEach(order -> list.add(getOrderResponse(order)));
		
		return list;
	}
	
	/**
	 * 주문 등록 - 설명추가필요
	 */
	public OrderResponse addOrder(OrderRequest orderRequest) {
		if (repository.existsById(orderRequest.getOrderNumber())) {
			Optional<Order> optionalOrder = repository.findById(orderRequest.getOrderNumber());
			return getOrderResponse(optionalOrder.get(), ErrorCode.ORDER_IS_EXIST);
		}

		Order order = Order.builder()
				.orderNumber(orderRequest.getOrderNumber())
				.orderCode(orderRequest.getOrder())
				.orderDate(orderRequest.getOrderDate())
				.build();

		OrderStatusCode orderStatusCode = OrderStatusCode.ORDER_RECEPTION;

		String sendDate = "";
		int delayDays = 0;

		if (ProductionLine.isProductionPossible()) {
			// 작업대기량 - 오늘 작업할 수 있는양 < 0 : 오늘 작업 가능. 발송일 오늘
			// 작업대기량 - 오늘 작업할 수 있는양 >= 0 : 오늘 작업 못함. 발송일 계산
			int count = orderQueue.size() - ProductionLine.remainTodayProduction();

			if (count < 0) {
				sendDate = WorkTimer.getDate(delayDays);
			} else {
				delayDays = (count / ProductionLine.getMaxProductionCount()) + 1;
				sendDate = WorkTimer.getDate(delayDays);
			}
		} else {
			delayDays = (orderQueue.size() / ProductionLine.getMaxProductionCount()) + 1;
			sendDate = WorkTimer.getDate(delayDays);
		}

		order.setSendDate(sendDate);

		if (delayDays >= 7) { // 주문일과 발송예정일이 7일이상 차이날 경우 생산 지연으로 인한 주문취소처리
			order.setOrderStatus(OrderStatusCode.ORDER_CANCEL.getCode());

			return getOrderResponse(order, ErrorCode.ORDER_CANCEL);
		} else {
			order.setOrderStatus(orderStatusCode.getCode());
			order.setSendDate(sendDate);

			repository.save(order);

			return OrderManagementUtils.addOrderSheet(order);
		}
	}
	
	/**
	 * 주문 취소 - 설명
	 */
	public OrderResponse cancelOrder(String orderNumber) {
		Optional<Order> optionalOrder = repository.findById(orderNumber);
		Order order = optionalOrder.orElseThrow(() -> {
			throw new EntityNotExistException("not exists order : " + orderNumber);
		});

		if (OrderStatusCode.isCancellable(OrderStatusCode.getByCode(order.getOrderStatus()).getOrder())) {
			// 제품이 생산이 시작 되지 않았을 경우에만 주문 취소 가능
			order.setOrderStatus(OrderStatusCode.ORDER_CANCEL.getCode());
			repository.update(order);
			return getOrderResponse(order);
		} else { // 제품 생산이 시작된 이후 주문 취소 불가
			return getOrderResponse(order, ErrorCode.UNABLE_TO_CANCEL_ORDER);
		}
	}
	

	private OrderResponse getOrderResponse(Order order) {
		OrderResponse orderResponse = OrderResponse.builder()
				.order_number(order.getOrderNumber())
				.order(order.getOrderCode())
				.send_date(order.getSendDate())
				.build();
		
		return orderResponse;
	}
	
	private OrderResponse getOrderResponse(Order order, ErrorCode errorMessage) {
		OrderResponse orderResponse = getOrderResponse(order);
		
		if (!ObjectUtils.isEmpty(errorMessage)) {
			orderResponse.setError(errorMessage.getMessage());
		}
		
		return orderResponse;
	}

}
