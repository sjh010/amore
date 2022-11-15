package com.apgroup.pms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.apgroup.pms.data.entity.RawMaterial;
import com.apgroup.pms.data.repository.RawMaterialRepository;
import com.apgroup.pms.dto.request.RawMaterialRequest;
import com.apgroup.pms.dto.request.RawMaterialStockRequest;
import com.apgroup.pms.dto.response.RawMaterialResponse;
import com.apgroup.pms.dto.response.RawMaterialStockResponse;
import com.apgroup.pms.error.ErrorCode;

import lombok.RequiredArgsConstructor;

/**
 * 원료 및 재고 처리 서비스
 * - 제품 리뉴얼에 따라 효능 원룐는 추가/삭제 가능 -> 원료 추가, 원료 삭제
 * - 각 원료의 재고 보유량은 입력/조회 가능 -> 재고 보유량 조회, 재고 보유량 입력
 */
@Service
@RequiredArgsConstructor
public class RawMaterialService {

	private final RawMaterialRepository repository;
	
	public List<RawMaterialResponse> getRawMaterials() {
		List<RawMaterial> all = repository.findAll();
		return all.stream()
				.map(rawMaterial -> this.getRawMaterialResponse(rawMaterial))
				.collect(Collectors.toList());
	}
	
	public RawMaterialResponse addRawMaterial(RawMaterialRequest request) {
		if (repository.existsById(request.getId())) {
			Optional<RawMaterial> optionalRawMaterial = repository.findById(request.getId());
			return getRawMaterialResponse(optionalRawMaterial.get(), ErrorCode.RAW_MATERIAL_IS_EXIST);
		}
		
		RawMaterial newRawMaterial = RawMaterial.builder()
				.id(request.getId())
				.stockAmount(request.getAmount())
				.build();
		
		repository.save(newRawMaterial);
		
		return getRawMaterialResponse(newRawMaterial);
	}
	
	public RawMaterialResponse deleteRawMaterial(String id) {
		if (repository.delete(id)) {
			return RawMaterialResponse.builder().id(id).build();
		} else {
			return RawMaterialResponse.builder().id(id)
					.error(ErrorCode.RAW_MATERIAL_NOT_EXIST.getMessage()).build();
		}
	}
	
	/**
	 * 재고 목록 조회
	 * @return
	 */
	public List<RawMaterialStockResponse> getStocks() {
		List<RawMaterialStockResponse> responses = new ArrayList<RawMaterialStockResponse>();
		
		repository.findAll().stream().forEach(rawMaterial -> {
			responses.add(getStockResponse(rawMaterial));
		});
		
		return responses;
	}
	
	/**
	 * 재고량 조회
	 * @param id
	 * @return
	 */
	public RawMaterialStockResponse getStock(String id) {
		RawMaterial stock = repository.findById(id).get();
		
		if (ObjectUtils.isEmpty(stock)) {
			return null;
		} else {
			return getStockResponse(stock);
		}
	}
	
	/**
	 * 재고량 입력
	 * @param id
	 * @param stockAmount
	 * @return
	 */
	public RawMaterialStockResponse updateStockAmount(RawMaterialStockRequest request) {
		
		if (repository.existsById(request.getId())) {
			RawMaterial stock = repository.findById(request.getId()).get();
			
			
			stock.setStockAmount(request.getStock());
			
			return getStockResponse(repository.update(stock));	
		} else {
			return RawMaterialStockResponse.builder().id(request.getId())
					.error(ErrorCode.RAW_MATERIAL_NOT_EXIST.getMessage())
					.build();
		}
		
			
	}
	
	private RawMaterialStockResponse getStockResponse(RawMaterial rawMaterial) {
		RawMaterialStockResponse response = RawMaterialStockResponse.builder()
				.id(rawMaterial.getId())
				.stock(rawMaterial.getStockAmount())
				.build(); 
		
		return response;
	}
	
	private RawMaterialResponse getRawMaterialResponse(RawMaterial rawMaterial) {
		RawMaterialResponse response = RawMaterialResponse.builder()
				.id(rawMaterial.getId())
				.remainsAmount(rawMaterial.getRemainAmount())
				.stockAmount(rawMaterial.getStockAmount())
				.build(); 
		
		return response;
	}
	
	private RawMaterialResponse getRawMaterialResponse(RawMaterial rawMaterial, ErrorCode errorCode) {
		RawMaterialResponse response = getRawMaterialResponse(rawMaterial);
		
		if (!ObjectUtils.isEmpty(errorCode)) {
			response.setError(errorCode.getMessage());
		}
		
		return response;
	}


}
