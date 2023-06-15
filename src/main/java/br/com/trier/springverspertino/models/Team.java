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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity(name = "time")
public class Team {
    
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_time")
    private Integer id;
    
    @Column(name = "nome_time", unique = true)
    private String name;

}
