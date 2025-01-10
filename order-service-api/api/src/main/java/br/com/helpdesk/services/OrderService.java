package br.com.helpdesk.services;

import models.requests.CreateOrderRequest;
import models.requests.OrderResponse;
import models.requests.UpdateOrderRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    OrderResponse findById(final Long id);

    void save(CreateOrderRequest request);

    OrderResponse update(final Long id, UpdateOrderRequest request);

    void delete(final Long id);

    List<OrderResponse> findAll();

    Page<OrderResponse> findAllPaginated(Integer page, Integer size, String sortBy, String sortDirection);
}
