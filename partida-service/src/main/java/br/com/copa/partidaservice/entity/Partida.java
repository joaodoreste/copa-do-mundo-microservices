package br.com.copa.partidaservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "partidas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String selecaoMandante;

    private String selecaoVisitante;

    private Integer golsMandante;

    private Integer golsVisitante;

    private String fase;

    private String status;
}