package br.com.copa.noticiaservice.service;

import br.com.copa.noticiaservice.client.PartidaClient;
import br.com.copa.noticiaservice.dto.PartidaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartidaConsultaService {

    private final PartidaClient client;

    public PartidaResponse buscarPartida(Long id) {
        return client.buscarPorId(id);
    }
}