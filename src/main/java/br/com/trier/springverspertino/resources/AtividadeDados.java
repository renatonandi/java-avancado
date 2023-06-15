package br.com.trier.springverspertino.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springverspertino.models.dto.ApostaDto;

@RestController
@RequestMapping("/dados")
public class AtividadeDados {
    
    @GetMapping("/rolagem/{quantidade}/{aposta}")
    public ResponseEntity<ApostaDto> rolagem(@PathVariable int quantidade, @PathVariable int aposta) {
        
        ApostaDto retorno = null;
        
        if (quantidade < 1 || quantidade > 4) {
            throw new IllegalArgumentException("A quantiadade de dados não foi aceita. Valor máximo é de 4 e minimo de 1");
        }
        
        Integer valorMin = quantidade;
        Integer valorMax = quantidade * 6;
        if (aposta < valorMin || aposta > valorMax) {
            throw new IllegalArgumentException("Valor de aposta inválido.");
        }
        
        
        List<Integer> valoresDados = new ArrayList<Integer>();
        Random random = new Random();
        Integer soma = 0;
        for (int i = 0;i < quantidade; i++) {
            Integer valorDados = random.nextInt(6) + 1;
            valoresDados.add(valorDados);
            soma += valorDados;            
        }
        
        
        
//        ResultadoDados resultado = new ResultadoDados();
//        resultado.setValoresDados(valoresDados);
//        resultado.setSoma(soma);
//        resultado.setPorcentagem(porcentagem);
        
        if (soma == aposta) {
            retorno = new ApostaDto(valoresDados, soma, null, "Você acertou");
        }else {
            Double porcentagem = (double) Math.abs((aposta - quantidade) / aposta * 100);
            retorno = new ApostaDto(valoresDados, soma, porcentagem, "Não foi dessa vez");
            
        }
        
        return ResponseEntity.ok(retorno);
        
    }

}
