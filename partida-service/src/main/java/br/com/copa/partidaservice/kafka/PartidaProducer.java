package br.com.copa.partidaservice.kafka;

import br.com.copa.partidaservice.event.PartidaCriadaEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartidaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publicar(PartidaCriadaEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);

            kafkaTemplate.send("partidas.criadas", json)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            System.out.println(
                                    "[CORRELATION-ID: " + event.getCorrelationId() + "] "
                                            + "Erro ao publicar evento no Kafka: "
                                            + ex.getMessage()
                            );
                        } else {
                            System.out.println(
                                    "[CORRELATION-ID: " + event.getCorrelationId() + "] "
                                            + "Evento enviado com sucesso para o Kafka: "
                                            + json
                            );
                        }
                    });

        } catch (Exception e) {
            throw new RuntimeException("Erro ao publicar evento no Kafka", e);
        }
    }
}