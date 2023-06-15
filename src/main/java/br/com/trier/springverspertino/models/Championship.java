package br.com.trier.springverspertino.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id_campeonato")
@Entity(name = "campeonato")
public class Championship {
    
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campeonato")
    private Integer id;
    
    @Column(name = "descricao")
    private String description;
    
    @Column(name = "ano_campeonato")
    private Integer year;

}
