package br.com.copa.noticiaservice.kafka;

import br.com.copa.noticiaservice.entity.Noticia;
import br.com.copa.noticiaservice.event.PartidaCriadaEvent;
import br.com.copa.noticiaservice.repository.NoticiaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartidaConsumer {

    private final ObjectMapper objectMapper;
    private final NoticiaRepository repository;

    @KafkaListener(
            topics = "partidas.criadas",
            groupId = "noticia-group-9",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumir(String mensagem) {
        try {

            PartidaCriadaEvent event =
                    objectMapper.readValue(mensagem, PartidaCriadaEvent.class);

            log.info(
                    "[CORRELATION-ID: {}] Mensagem recebida do Kafka: {} x {}",
                    event.getCorrelationId(),
                    event.getSelecaoMandante(),
                    event.getSelecaoVisitante()
            );

            Noticia noticia = Noticia.builder()
                    .titulo("Nova partida cadastrada")
                    .conteudo(
                            event.getSelecaoMandante()
                                    + " x "
                                    + event.getSelecaoVisitante()
                                    + " foi cadastrada na fase "
                                    + event.getFase()
                    )
                    .autor("Kafka")
                    .dataPublicacao(LocalDate.now().toString())
                    .build();

            repository.save(noticia);

            log.info(
                    "[CORRELATION-ID: {}] Notícia criada automaticamente no MongoDB.",
                    event.getCorrelationId()
            );

        } catch (Exception e) {
            log.error("Erro ao consumir evento de partida criada", e);
        }
    }
}