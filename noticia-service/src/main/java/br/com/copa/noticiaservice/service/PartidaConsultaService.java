package br.com.copa.noticiaservice.service;

import br.com.copa.noticiaservice.client.PartidaClient;
import br.com.copa.noticiaservice.dto.PartidaResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartidaConsultaService {

    private final PartidaClient client;

    @CircuitBreaker(name = "partidaService", fallbackMethod = "fallbackBuscarPartida")
    public PartidaResponse buscarPartida(Long id) {
        return client.buscarPorId(id);
    }

    public PartidaResponse fallbackBuscarPartida(Long id, Throwable erro) {
        PartidaResponse partida = new PartidaResponse();
        partida.setId(id);
        partida.setSelecaoMandante("Indisponível");
        partida.setSelecaoVisitante("Indisponível");
        partida.setGolsMandante(0);
        partida.setGolsVisitante(0);
        partida.setFase("Indisponível");
        partida.setStatus("PARTIDA-SERVICE INDISPONÍVEL");
        return partida;
    }
}