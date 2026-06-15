package br.com.copa.partidaservice.controller;

import br.com.copa.partidaservice.entity.Partida;
import br.com.copa.partidaservice.service.PartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService service;

    @GetMapping("/partidas")
    public List<Partida> listar() {
        return service.listar();
    }

    @PostMapping("/partidas")
    public Partida salvar(@RequestBody Partida partida) {
        return service.salvar(partida);
    }

    @GetMapping("/partidas/{id}")
    public Partida buscarPorId(@PathVariable Long id) {return service.buscarPorId(id);}
}