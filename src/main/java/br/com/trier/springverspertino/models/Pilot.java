package br.com.trier.springverspertino.models;

import br.com.trier.springverspertino.models.dto.PilotDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "piloto")
public class Pilot {
    
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_piloto")
    private Integer id;
    
    @Column(name = "nome_piloto")
    private String name;
    
    @ManyToOne
    @NotNull
    private Country country;
    
    @ManyToOne
    @NotNull
    private Team team;

    
    public Pilot(PilotDTO dto, Country country, Team team) {
        this(dto.getId(), dto.getName(), country, team);
    }
    
    public PilotDTO toDTO() {
        return new PilotDTO(id, name, country.getId(), country.getName(), team.getId(), team.getName());
    }
}
