package br.com.copa.noticiaservice.repository;

import br.com.copa.noticiaservice.entity.Noticia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoticiaRepository extends MongoRepository<Noticia, String> {
}