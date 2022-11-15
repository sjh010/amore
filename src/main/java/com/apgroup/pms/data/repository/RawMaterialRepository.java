package com.apgroup.pms.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.apgroup.pms.data.entity.RawMaterial;

/**
 * 원료 repository
 * - DB 사용 대신 Map 사용
 */
@Repository
public class RawMaterialRepository {

	private Map<String, RawMaterial> map = new HashMap<String, RawMaterial>();
	
	public List<RawMaterial> findAll() {
		List<RawMaterial> list = new ArrayList<RawMaterial>();
		
		for (Entry<String, RawMaterial> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		
		return list;
	}

	public Optional<RawMaterial> findById(String id) {
		RawMaterial rawMaterial = map.get(id);
		return Optional.ofNullable(rawMaterial);
	}

	public RawMaterial save(RawMaterial entity) {
		return map.put(entity.getId(), entity);
	}

	public RawMaterial update(RawMaterial entity) {
		map.put(entity.getId(), entity);
		
		return entity;
	}

	public boolean delete(String id) {
		if (ObjectUtils.isEmpty(map.remove(id))) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean existsById(String id) {
		return map.containsKey(id);
	}
	
}
