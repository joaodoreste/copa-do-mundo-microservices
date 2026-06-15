package br.com.copa.noticiaservice.controller;

import br.com.copa.noticiaservice.dto.PartidaResponse;
import br.com.copa.noticiaservice.entity.Noticia;
import br.com.copa.noticiaservice.service.NoticiaService;
import br.com.copa.noticiaservice.service.PartidaConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaService service;
    private final PartidaConsultaService partidaConsultaService;

    @GetMapping("/noticias")
    public List<Noticia> listar() {
        return service.listar();
    }

    @PostMapping("/noticias")
    public Noticia salvar(@RequestBody Noticia noticia) {
        return service.salvar(noticia);
    }

    @GetMapping("/noticias/partida/{id}")
    public PartidaResponse buscarPartida(@PathVariable Long id) {return partidaConsultaService.buscarPartida(id);}
}