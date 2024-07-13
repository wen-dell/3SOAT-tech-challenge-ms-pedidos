package br.com.tech.challenge.servicos;

import br.com.tech.challenge.servicos.amqp.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CozinhaTopicProducer {

    private final KafkaProducerService kafkaProducerService;

    private static final String COZINHA_KAFKA_TOPIC = "fila-pedidos";

    public void enviarPedidoParaCozinha(final Long idPedido) {
        kafkaProducerService.sendMessage(COZINHA_KAFKA_TOPIC, idPedido);
        log.info("Pedido enviado com sucesso para cozinha!");
    }
}
