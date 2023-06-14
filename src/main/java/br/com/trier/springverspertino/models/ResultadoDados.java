package br.com.trier.springverspertino.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResultadoDados {
    private List<Integer> valoresDados;
    private Integer soma;
    private Double porcentagem;

}
