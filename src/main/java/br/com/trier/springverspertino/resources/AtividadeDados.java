package br.com.trier.springverspertino.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springverspertino.models.ResultadoDados;

@RestController
@RequestMapping("/dados")
public class AtividadeDados {
    
    @GetMapping("/rolagem/{quantidade}/{aposta}")
    public ResultadoDados rolagem(@PathVariable int quantidade, @PathVariable int aposta) {
        
        
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
        
        
        Double porcentagem = ((double) soma / aposta) * 100;
        
        ResultadoDados resultado = new ResultadoDados();
        resultado.setValoresDados(valoresDados);
        resultado.setSoma(soma);
        resultado.setPorcentagem(porcentagem);
        
        
        
        
       
        return resultado;
        
    }

}
