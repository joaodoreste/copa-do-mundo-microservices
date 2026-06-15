package br.com.copa.noticiaservice.client;

import br.com.copa.noticiaservice.dto.PartidaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "partida-service")
public interface PartidaClient {

    @GetMapping("/partidas/{id}")
    PartidaResponse buscarPorId(@PathVariable Long id);
}