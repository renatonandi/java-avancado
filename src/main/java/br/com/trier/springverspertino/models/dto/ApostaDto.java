package br.com.trier.springverspertino.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApostaDto {
    
    private List<Integer> valoresDados;
    private Integer soma;
    private Double porcentagem;
    private String mensagem;

}
