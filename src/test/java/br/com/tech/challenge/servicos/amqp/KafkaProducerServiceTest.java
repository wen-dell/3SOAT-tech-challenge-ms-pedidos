package br.com.tech.challenge.servicos.amqp;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, Long> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService producer;

    @DisplayName("Deve enviar mensagem para a fila com sucesso")
    @Test
    public void shouldSuccessfullySendMessage() {
        Long mensagem = 123L;
        String topico = "meu-topico";

        producer.sendMessage(topico, mensagem);

        verify(kafkaTemplate).send(topico, mensagem);
    }
}
