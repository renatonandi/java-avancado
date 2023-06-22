package br.com.trier.springverspertino.models;

import java.time.ZonedDateTime;

import br.com.trier.springverspertino.models.dto.RaceDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "corrida")
public class Race {
    
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corrida")
    private Integer id;
    
    @Column(name = "data_corrida")
    private ZonedDateTime date;
    
    @ManyToOne
    @NotNull
    private Championship championship;
    
    @ManyToOne
    @NotNull
    private Speedway speedway;
    
    public Race(RaceDTO dto, Championship championship, Speedway speedway) {
        this(dto.getId(), DateUtils.strToZonedDateTime(dto.getDate()), championship, speedway);
    }
    
    public RaceDTO toDTO() {
        return new RaceDTO(id, DateUtils.zonedDateTimeToStr(date), championship.getId(), championship.getDescription(), speedway.getId(), speedway.getName());
    }

}
