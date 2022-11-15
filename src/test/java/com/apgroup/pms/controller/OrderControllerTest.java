package com.apgroup.pms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.apgroup.pms.dto.request.OrderRequest;
import com.apgroup.pms.dto.response.OrderResponse;
import com.apgroup.pms.error.ErrorCode;
import com.apgroup.pms.error.exception.EntityNotExistException;
import com.apgroup.pms.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ActiveProfiles("test")
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("주문 정보 리스트를 가져온다")
    @Test
    void get_orders_success() throws Exception {
        // Given
        OrderResponse order1 = new OrderResponse("1", "A5B5", "20221115", null);
        OrderResponse order2 = new OrderResponse("2", "A5C5", "20221116", null);
        OrderResponse order3 = new OrderResponse("3", "A5D5", "20221117", null);
        List<OrderResponse> response = new ArrayList<>();
        response.add(order1);
        response.add(order2);
        response.add(order3);
        given(orderService.getOrders()).willReturn(response);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/apis/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].order_number").value(1))
                .andExpect(jsonPath("$[0].order").value("A5B5"))
                .andExpect(jsonPath("$[0].send_date").exists())
                .andExpect(jsonPath("$[0].error").doesNotExist())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[1].order_number").value(2))
                .andExpect(jsonPath("$[1].order").value("A5C5"))
                .andExpect(jsonPath("$[1].send_date").exists())
                .andExpect(jsonPath("$[1].error").doesNotExist())
                .andExpect(jsonPath("$[2]").exists())
                .andExpect(jsonPath("$[2].order_number").value(3))
                .andExpect(jsonPath("$[2].order").value("A5D5"))
                .andExpect(jsonPath("$[2].send_date").exists())
                .andExpect(jsonPath("$[2].error").doesNotExist())
                .andExpect(jsonPath("$[3]").doesNotExist());
    }

    @DisplayName("주문번호로 특정 주문 정보를 가져온다 - 성공")
    @Test
    void get_order_success() throws Exception {
        // Given
        String orderNumber = "1";
        OrderResponse order = new OrderResponse(orderNumber, "A5B5", "20221115", null);
        given(orderService.getOrder(orderNumber)).willReturn(order);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/apis/v1/orders/{order_number}", orderNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.order_number").value(1))
                .andExpect(jsonPath("$.order").value("A5B5"))
                .andExpect(jsonPath("$.send_date").exists())
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @DisplayName("주문번호로 특정 주문 정보를 가져온다 - 실패(존재하지 않는 주문번호)")
    @Test
    void get_order_failure() throws Exception {
        // Given
        String orderNumber = "2";
        willThrow(new EntityNotExistException("not exists order : " + orderNumber))
                .given(orderService).getOrder(any(String.class));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/apis/v1/orders/{order_number}", orderNumber))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.resultCode").value("404"))
                .andExpect(jsonPath("$.resultMessage").exists());
    }

    @DisplayName("주문 요청 성공")
    @Test
    public void order_success() throws Exception {
        // Given
        String orderNumber = "1";
        String order = "A5C5";
        String sendDate = "20221120";
        OrderRequest request = new OrderRequest(orderNumber, order, "20221115");
        OrderResponse response = new OrderResponse(orderNumber, order, sendDate, null);

        given(orderService.addOrder(any(OrderRequest.class))).willReturn(response);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/apis/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.order_number").value(orderNumber))
                .andExpect(jsonPath("$.order").value(order))
                .andExpect(jsonPath("$.send_date").value(sendDate))
                .andExpect(jsonPath("$.error").doesNotExist());

    }

    @DisplayName("주문 요청 실패 - BadRequest")
    @MethodSource
    @ParameterizedTest(name = "Cause : {0}")
    public void order_failure_bad_request(String cause, OrderRequest request) throws Exception {
        // Given

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/apis/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.resultCode").value("400"))
                .andExpect(jsonPath("$.resultMessage").exists());
        ;
    }

    private static Stream<Arguments> order_failure_bad_request() {
        return Stream.of(
                Arguments.of("Empty OrderNumber", new OrderRequest("", "A5C5", "20221115")),
                Arguments.of("Empty Order", new OrderRequest("1", "", "20221115")),
                Arguments.of("Empty OrderDate", new OrderRequest("1", "A5D5", "")),
                Arguments.of("Invalid Order(Exceed Material Count)", new OrderRequest("1", "A5D7", "20221115")),
                Arguments.of("Invalid Order(Short Material Count)", new OrderRequest("1", "A3D3", "20221115")),
                Arguments.of("Invalid Order", new OrderRequest("1", "AD", "20221115"))
        );
    }

    @DisplayName("주문 취소 - 성공")
    @Test
    void cancel_order_success() throws Exception {
        // Given
        String orderNumber = "1";
        OrderResponse order = new OrderResponse(orderNumber, "A5B5", "20221115", null);
        given(orderService.cancelOrder(orderNumber)).willReturn(order);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/apis/v1/orders/{order_number}", orderNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.order_number").value(1))
                .andExpect(jsonPath("$.order").value("A5B5"))
                .andExpect(jsonPath("$.send_date").exists())
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @DisplayName("주문 취소 - 실패(존재하지 않는 주문번호)")
    @Test
    void cancel_order_failure() throws Exception {
        // Given
        String orderNumber = "2";
        willThrow(new EntityNotExistException("not exists order : " + orderNumber))
                .given(orderService).cancelOrder(any(String.class));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/apis/v1/orders/{order_number}", orderNumber))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.resultCode").value("404"))
                .andExpect(jsonPath("$.resultMessage").exists());
    }

    @DisplayName("주문 취소 - 실패(취소 할 수 없는 주문)")
    @Test
    void cancel_order_failure2() throws Exception {
        // Given
        String orderNumber = "3";
        String order = "A5B5";
        OrderResponse response = new OrderResponse(orderNumber, order, "20221115", ErrorCode.UNABLE_TO_CANCEL_ORDER.getMessage());
        given(orderService.cancelOrder(orderNumber)).willReturn(response);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/apis/v1/orders/{order_number}", orderNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.order_number").value(orderNumber))
                .andExpect(jsonPath("$.order").value(order))
                .andExpect(jsonPath("$.send_date").exists())
                .andExpect(jsonPath("$.error").exists());
    }

}