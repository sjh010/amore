package com.apgroup.pms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apgroup.pms.dto.request.RawMaterialRequest;
import com.apgroup.pms.dto.request.RawMaterialStockRequest;
import com.apgroup.pms.dto.response.RawMaterialResponse;
import com.apgroup.pms.dto.response.RawMaterialStockResponse;
import com.apgroup.pms.service.RawMaterialService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * 원료 및 재고 API Controller 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/v1/raw-materials")
public class RawMaterialController {
	
	private final RawMaterialService rawMaterialService;
	
	@GetMapping
	@ApiOperation(value = "원료 전체 조회", notes = "생산 설비의 모든 원료를 조회한다.")
	public List<RawMaterialResponse> getAllRawMaterials() {
		return rawMaterialService.getRawMaterials();
	}
	
	@PostMapping
	@ApiOperation(value = "원료 추가", notes = "생산 설비에 해당 원료를 추가한다.")
	public RawMaterialResponse addRawMaterial(@RequestBody @Validated RawMaterialRequest request) {
		return rawMaterialService.addRawMaterial(request);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "원료 삭제", notes = "생산 설비에 해당 원료를 삭제한다.")
	public RawMaterialResponse deleteRawMaterial(@PathVariable String id) {
		return rawMaterialService.deleteRawMaterial(id);
	}
	
	@GetMapping("/stock")
	@ApiOperation(value = "전체 재고 보유량 조회", notes = "각 원료의 재고 보유량 목록을 조회한다.")
	public List<RawMaterialStockResponse> getStocks() {
		return rawMaterialService.getStocks();
	}
	
	@GetMapping("/stock/{id}")
	@ApiOperation(value = "재고 보유량 조회", notes = "해당 원료의 재고 보유량을 조회한다.")
	public RawMaterialStockResponse getStock(@PathVariable String id) {
		return rawMaterialService.getStock(id);
	}
	
	@PostMapping("/stock")
	@ApiOperation(value = "재고 보유량 입력", notes = "해당 원료의 재고 보유량을 입력한다.")
	public RawMaterialStockResponse addStock(@RequestBody @Valid RawMaterialStockRequest request) {	
		return rawMaterialService.updateStockAmount(request);			
		
	}
	
}
