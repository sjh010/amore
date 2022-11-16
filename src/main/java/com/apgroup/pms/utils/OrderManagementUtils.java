package com.apgroup.pms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.apgroup.pms.data.entity.Order;
import com.apgroup.pms.dto.OrderSheet;
import com.apgroup.pms.dto.response.OrderResponse;
import com.apgroup.pms.thread.WorkTimer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 주문 및 발송 관리 유틸
 */
@Slf4j
@RequiredArgsConstructor
public class OrderManagementUtils {
	
	// 생산 대기 queue
	private static LinkedBlockingQueue<OrderSheet> orderQueue = new LinkedBlockingQueue<OrderSheet>();
	
	// 발송 대기 queue
	private static LinkedBlockingQueue<Order> forwardingQueue = new LinkedBlockingQueue<Order>();
	
	public static OrderResponse addOrderSheet(Order order) {
		log.info("[{}][주문번호 : {}] 주문 접수", WorkTimer.getCurrentTime(), order.getOrderNumber());
		orderQueue.add(createOrderSheet(order));
		return getOrderResponse(order);	
	}
	
	/**
	 * 주문 취소된 주문서를 생산대기 queue에서 삭제한다.
	 */
	public static void cancelOrder(String orderNumber) {
		orderQueue.stream().forEach(orderSheet -> {
			if (orderSheet.getOrderNumber().equalsIgnoreCase(orderNumber)) {
				orderQueue.remove(orderSheet);
			}
		});
	}
	
	public static OrderSheet getOrderSheet() {
		return orderQueue.poll();
	}
	
	public static boolean isOrderQueueEmpty() {
		return orderQueue.size() == 0;
	}
	
	public static boolean isFowardingQueueEmpty() {
		return forwardingQueue.size() == 0;
	}
	
	public static void addForwadingQueue(Order order) {
		forwardingQueue.add(order);
	}
	
	public static List<Order> getForwardingList() {
		List<Order> list = new ArrayList<Order>();
		
		while (!forwardingQueue.isEmpty()) {
			list.add(forwardingQueue.poll());
		}
		
		return list;
	}
	
	/**
	 * 생산 주문서 작성
	 * - 입력받은 주문코드에서 효능 및 비율을 분리한다.
	 */
	private static OrderSheet createOrderSheet(Order order) {
		OrderSheet orderSheet = OrderSheet.builder()
				.orderNumber(order.getOrderNumber())
				.order(order.getOrderCode())
				.build();
		
		String orderCode = order.getOrderCode();
		
		if (orderCode.length() == 3) {
			orderSheet.setEffect1(orderCode.substring(0, 1));
			orderSheet.setRate1(Integer.valueOf(orderCode.substring(1, 3)));
		} else if (orderCode.length() == 4) {
			orderSheet.setEffect1(orderCode.substring(0, 1));
			orderSheet.setRate1(Integer.valueOf(orderCode.substring(1, 2)));
			orderSheet.setEffect2(orderCode.substring(2, 3));
			orderSheet.setRate2(Integer.valueOf(orderCode.substring(3, 4)));
		}
		
		return orderSheet;
	}

	
	private static OrderResponse getOrderResponse(Order order) {
		OrderResponse orderResponse = OrderResponse.builder()
				.orderNumber(order.getOrderNumber())
				.orderCode(order.getOrderCode())
				.sendDate(order.getSendDate())
				.build();
		
		return orderResponse;
	}

}
