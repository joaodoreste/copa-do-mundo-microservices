package br.com.copa.partidaservice.service;

import br.com.copa.partidaservice.entity.Partida;
import br.com.copa.partidaservice.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository repository;

    public List<Partida> listar() {
        return repository.findAll();
    }

    public Partida salvar(Partida partida) {
        return repository.save(partida);
    }

    public Partida buscarPorId(Long id) {return repository.findById(id).orElse(null);}
}