package br.com.trier.springverspertino.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RaceDTO {

    private Integer id;

    private String date;
    
    private Integer championshipId;

    private String championshipName;

    private Integer speedwayId;

    private String speedwayName;

}
