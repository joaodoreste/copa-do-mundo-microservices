package br.com.copa.noticiaservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "noticias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Noticia {

    @Id
    private String id;

    private String titulo;

    private String conteudo;

    private String autor;

    private String dataPublicacao;
}