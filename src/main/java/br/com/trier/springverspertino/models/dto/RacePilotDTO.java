package br.com.trier.springverspertino.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RacePilotDTO {
	
	private Integer id;
	
	private Integer pilotId;
	
	private String pilotName;
	
	private Integer raceId;
	
	private String raceDate;
	
	private Integer placing;

}
