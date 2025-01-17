package br.com.helpdesk.services.impl;

import br.com.helpdesk.clients.UserServiceFeignClient;
import br.com.helpdesk.entities.Order;
import br.com.helpdesk.mapper.OrderMapper;
import br.com.helpdesk.repositories.OrderRepository;
import br.com.helpdesk.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.dtos.OrderCreatedMessage;
import models.enums.OrderStatusEnum;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateOrderRequest;
import models.requests.OrderResponse;
import models.requests.UpdateOrderRequest;
import models.responses.UserResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final UserServiceFeignClient userServiceFeignClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public OrderResponse findById(final Long id) {
        return mapper.fromEntity(find(id));
    }

    @Override
    public void save(CreateOrderRequest request) {
        final var requester = validateUser(request.requesterId());
        final var customer = validateUser(request.customerId());
        var entity = repository.save(
                mapper.fromRequest(request)
        );

        rabbitTemplate.convertAndSend(
                "helpdesk",
                "rk.orders.create",
                new OrderCreatedMessage(
                        mapper.fromEntity(entity),
                        requester,
                        customer
                )
        );


    }

    @Override
    public OrderResponse update(final Long id, UpdateOrderRequest request) {
        validateUsers(request);

        var entity = find(id);
        entity = mapper.fromRequest(entity, request);

        if (entity.getStatus().equals(OrderStatusEnum.CLOSED)) {
            entity.setClosedAt(LocalDateTime.now());
        }

        return mapper.fromEntity(repository.save(entity));
    }

    private void validateUsers(final UpdateOrderRequest request) {
        if (request.requesterId() != null) {
            validateUser(request.requesterId());
        }

        if (request.customerId() != null) {
            validateUser(request.customerId());
        }
    }

    @Override
    public void delete(final Long id) {
        repository.delete(find(id));
    }

    @Override
    public List<OrderResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::fromEntity)
                .toList();
    }

    @Override
    public Page<OrderResponse> findAllPaginated(Integer page, Integer size, String sortBy, String sortDirection) {

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.valueOf(sortDirection),
                sortBy
        );

        return repository.findAll(pageRequest)
                .map(mapper::fromEntity);
    }

    private Order find(final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Object not found "
                        + id + ", Type: " + Order.class.getSimpleName()
                ));
    }

    private UserResponse validateUser(final String userId) {
        final var response = userServiceFeignClient.findById(userId).getBody();
        log.info("User found: {}", response);
        return response;
    }

}
