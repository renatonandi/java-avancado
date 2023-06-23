package br.com.trier.springverspertino.models;

import br.com.trier.springverspertino.models.dto.RacePilotDTO;
import br.com.trier.springverspertino.utils.DateUtils;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "piloto_corrida")
public class RacePilot {
    
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pais")
    private Integer id;
    
    @ManyToOne
    @NotNull
    private Pilot pilot;
    
    @ManyToOne
    @NotNull
    private Race race;
    
    @Column(name = "posicao_piloto")
    private Integer placing;
    
    public RacePilot(RacePilotDTO racePilotDTO, Pilot pilot, Race race) {
    	this(racePilotDTO.getId(), pilot, race, racePilotDTO.getPlacing());
    }
    
    public RacePilotDTO toDTO() {
    	return new RacePilotDTO(id, pilot.getId(), pilot.getName(), race.getId(), DateUtils.zonedDateTimeToStr(race.getDate()) , placing);
    }

}
