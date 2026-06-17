package br.com.copa.partidaservice.service;

import br.com.copa.partidaservice.entity.Partida;
import br.com.copa.partidaservice.event.PartidaCriadaEvent;
import br.com.copa.partidaservice.kafka.PartidaProducer;
import br.com.copa.partidaservice.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository repository;
    private final PartidaProducer producer;

    public List<Partida> listar() {
        return repository.findAll();
    }

    public Partida salvar(Partida partida) {

        Partida salva = repository.save(partida);

        String correlationId = UUID.randomUUID().toString();

        PartidaCriadaEvent event = PartidaCriadaEvent.builder()
                .id(salva.getId())
                .selecaoMandante(salva.getSelecaoMandante())
                .selecaoVisitante(salva.getSelecaoVisitante())
                .fase(salva.getFase())
                .correlationId(correlationId)
                .build();

        producer.publicar(event);

        return salva;
    }

    public Partida buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
}