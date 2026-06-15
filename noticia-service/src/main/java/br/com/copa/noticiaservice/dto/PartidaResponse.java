package br.com.copa.noticiaservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartidaResponse {

    private Long id;
    private String selecaoMandante;
    private String selecaoVisitante;
    private Integer golsMandante;
    private Integer golsVisitante;
    private String fase;
    private String status;
}