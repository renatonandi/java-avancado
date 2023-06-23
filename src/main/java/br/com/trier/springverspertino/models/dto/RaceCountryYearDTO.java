package br.com.trier.springverspertino.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RaceCountryYearDTO {
    
    private Integer year;
    
    private String country;
    
    private Integer raceSize;
    
    private List<RaceDTO> races;

}
