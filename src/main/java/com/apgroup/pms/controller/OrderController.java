package com.apgroup.pms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apgroup.pms.dto.request.OrderRequest;
import com.apgroup.pms.dto.response.OrderResponse;
import com.apgroup.pms.service.OrderService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * 주문 API Controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/apis/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @ApiOperation(value = "주문 목록 조회", notes = "전체 주문 목록을 조회한다.")
    public List<OrderResponse> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{orderNumber}")
    @ApiOperation(value = "주문 조회", notes = "주문번호에 해당하는 주문 내역을 조회한다.")
    public OrderResponse getOrder(@PathVariable String orderNumber) {
        return orderService.getOrder(orderNumber);
    }

    @PostMapping
    @ApiOperation(value = "주문 요청", notes = "제품 주문을 요청한다.")
    public OrderResponse order(@RequestBody @Valid OrderRequest orderRequest) {
        return orderService.addOrder(orderRequest);
    }

    @DeleteMapping("/{orderNumber}")
    @ApiOperation(value = "주문 취소", notes = "주문번호에 해당하는 주문을 취소한다.")
    public OrderResponse cancelOrder(@PathVariable String orderNumber) {
        return orderService.cancelOrder(orderNumber);
    }

}
