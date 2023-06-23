package br.com.trier.springverspertino.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RacePilotCountryDTO {

    private String pilotName;
    
    private String countryName;
    
    private Integer raceSize;
    
    private List<RacePilotDTO> races;
    
}
