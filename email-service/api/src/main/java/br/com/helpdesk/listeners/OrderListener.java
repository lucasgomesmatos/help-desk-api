package br.com.helpdesk.listeners;

import br.com.helpdesk.models.enums.OperationEnum;
import br.com.helpdesk.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.dtos.OrderCreatedMessage;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class OrderListener {

    private final EmailService emailService;

    @RabbitListener(bindings =
    @QueueBinding(
            exchange = @Exchange(value = "helpdesk", type = "topic"),
            value = @Queue(value = "queue.orders"),
            key = "rk.orders.create"
    ))
    public void listen(final OrderCreatedMessage message) throws MessagingException {
        log.info("Ordem de serviço recebida: {}", message);

        log.info("Enviando e-mail para o cliente: {}", message.getCustomer().email());
        this.emailService.sendHtmlMail(message, OperationEnum.ORDER_CREATED);
    }
}
