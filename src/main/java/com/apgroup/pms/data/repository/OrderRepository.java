package com.apgroup.pms.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.apgroup.pms.data.entity.Order;

/**
 * 주문 repository
 * - DB 사용 대신 Map 사용
 */
@Repository
public class OrderRepository {
	
	private final Map<String, Order> map = new HashMap<>();
		
	public List<Order> findAll() {
		List<Order> list = new ArrayList<>();
		
		for (Entry<String, Order> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		
		return list;
	}

	public Optional<Order> findById(String id) {
		Order order = map.get(id);
		return Optional.ofNullable(order);
	}

	public Order save(Order entity) {
		return map.put(entity.getOrderNumber(), entity);
	}

	public Order update(Order entity) {
		map.put(entity.getOrderNumber(), entity);
		return entity;
	}

	public boolean deleteById(String id) {
		if (ObjectUtils.isEmpty(map.remove(id))) {
			return false;
		} else {
			return true;
		}
	}

	public Order updateStatus(String orderNumber, String statusCode) {
		Order order = map.get(orderNumber);
		order.setOrderStatus(statusCode);
		
		return map.put(orderNumber, order);
	}
	
	public boolean existsById(String id) {
		return map.containsKey(id);
	}
}
