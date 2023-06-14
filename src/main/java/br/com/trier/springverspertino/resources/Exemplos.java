package br.com.trier.springverspertino.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exemplos")
public class Exemplos {
    
    @GetMapping
    public String bemVindo() {
        return "Bem vindo(a)";
    }
    
    @GetMapping("/ex2")
    public String helloWorld() {
        return "Hello World";
    }
    @GetMapping("/ex3")
    public String exemplo3(@RequestParam String sexo) {
        if(sexo.equalsIgnoreCase("M")) {
            return "Bem Vindo";
        }
        return "Bem VindA";
    }
    @GetMapping("/ex4")
    public String exemplo4(@RequestParam String sexo, @RequestParam String nome) {
        if(sexo.equalsIgnoreCase("M")) {
            return "Bem Vindo" + " " + nome;
        }
        return "Bem VindA" + " " + nome;
    }
    @GetMapping("/somar/{n1}/{n2}")
    public int somar(@PathVariable int n1, @PathVariable int n2) {
        return n1 + n2;
    }
    
    
    

}
