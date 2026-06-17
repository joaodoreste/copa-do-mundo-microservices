package br.com.copa.partidaservice.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaCriadaEvent {

    private Long id;
    private String selecaoMandante;
    private String selecaoVisitante;
    private String fase;
    private String correlationId;
}