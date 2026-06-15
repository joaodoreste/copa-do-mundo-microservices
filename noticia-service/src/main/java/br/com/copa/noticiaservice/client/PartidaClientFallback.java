package br.com.copa.noticiaservice.client;

import br.com.copa.noticiaservice.dto.PartidaResponse;
import org.springframework.stereotype.Component;

@Component
public class PartidaClientFallback implements PartidaClient {

    @Override
    public PartidaResponse buscarPorId(Long id) {
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