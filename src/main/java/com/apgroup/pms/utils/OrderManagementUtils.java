package com.apgroup.pms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.apgroup.pms.data.entity.Order;
import com.apgroup.pms.dto.OrderSheet;
import com.apgroup.pms.dto.response.OrderResponse;

import lombok.RequiredArgsConstructor;

/**
 * 주문 및 발송 관리 유틸
 */
@RequiredArgsConstructor
public class OrderManagementUtils {
	
	// 생산 대기 queue
	private static LinkedBlockingQueue<OrderSheet> orderQueue = new LinkedBlockingQueue<OrderSheet>();
	
	// 발송 대기 queue
	private static LinkedBlockingQueue<Order> forwardingQueue = new LinkedBlockingQueue<Order>();
	
	public static OrderResponse addOrderSheet(Order order) {
		orderQueue.add(createOrderSheet(order));
		
		return getOrderResponse(order);	
	}
	
	public static void removeOrderSheet(OrderSheet orderSheet) {
		orderQueue.remove(orderSheet);
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
				.order_number(order.getOrderNumber())
				.order(order.getOrderCode())
				.send_date(order.getSendDate())
				.build();
		
		return orderResponse;
	}

}
