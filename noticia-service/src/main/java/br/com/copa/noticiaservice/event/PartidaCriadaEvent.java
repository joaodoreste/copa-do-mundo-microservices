package br.com.copa.noticiaservice.event;

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
}