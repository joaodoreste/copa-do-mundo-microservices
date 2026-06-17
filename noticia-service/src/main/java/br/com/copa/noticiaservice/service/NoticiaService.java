package br.com.copa.noticiaservice.service;

import br.com.copa.noticiaservice.entity.Noticia;
import br.com.copa.noticiaservice.repository.NoticiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticiaService {

    private final NoticiaRepository repository;

    public List<Noticia> listar() {
        return repository.findAll();
    }

    public Flux<Noticia> listarReativo() {
        return Flux.fromIterable(repository.findAll())
                .delayElements(Duration.ofMillis(300));
    }

    public Noticia salvar(Noticia noticia) {
        return repository.save(noticia);
    }
}