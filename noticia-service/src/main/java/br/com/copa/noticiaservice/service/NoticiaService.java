package br.com.copa.noticiaservice.service;

import br.com.copa.noticiaservice.entity.Noticia;
import br.com.copa.noticiaservice.repository.NoticiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticiaService {

    private final NoticiaRepository repository;

    public List<Noticia> listar() {
        return repository.findAll();
    }

    public Noticia salvar(Noticia noticia) {
        return repository.save(noticia);
    }
}