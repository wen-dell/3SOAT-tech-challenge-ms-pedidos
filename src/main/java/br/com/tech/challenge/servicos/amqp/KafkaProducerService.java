package br.com.tech.challenge.servicos.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Long> kafkaTemplate;

    public void sendMessage(String topic, Long idPedido) {
        log.info("Enviando mensagem para o topico: {}", topic.toUpperCase());
        kafkaTemplate.send(topic, idPedido);
    }
}
