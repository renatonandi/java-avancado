package br.com.trier.springverspertino.resources;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.RacePilot;
import br.com.trier.springverspertino.models.dto.RaceCountryYearDTO;
import br.com.trier.springverspertino.models.dto.RaceDTO;
import br.com.trier.springverspertino.models.dto.RacePilotCountryDTO;
import br.com.trier.springverspertino.models.dto.RacePilotDTO;
import br.com.trier.springverspertino.services.CountryService;
import br.com.trier.springverspertino.services.PilotService;
import br.com.trier.springverspertino.services.RacePilotService;
import br.com.trier.springverspertino.services.RaceService;
import br.com.trier.springverspertino.services.SpeedwayService;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@RestController
@RequestMapping("/report")
public class ReportResource {
    
    @Autowired
    private CountryService countryService;
    
    @Autowired
    private PilotService pilotService;
    
    @Autowired
    private SpeedwayService speedwayService;
    
    @Autowired
    private RaceService raceService;
    
    @Autowired
    private RacePilotService racePilotService;
    
    @GetMapping("/races-by-country-year/{countryId}/{year}")
    public ResponseEntity<RaceCountryYearDTO> findRaceByCountryAndYear(@PathVariable Integer countryId, @PathVariable Integer year){
        Country country = countryService.findById(countryId);
        
        List<RaceDTO> racesDTO = speedwayService.findByCountryOrderBySize(country).stream().flatMap(speedway -> {
            try {
                return raceService.findBySpeedwayOrderByDate(speedway).stream();
            } catch (ObjectNotFound e) {
                return Stream.empty();
            }
        }).filter(race -> race.getDate().getYear() == year).map(Race::toDTO).toList();
        
        return ResponseEntity.ok(new RaceCountryYearDTO(year, country.getName(), racesDTO.size(), racesDTO));
    }
    
    
    
    @GetMapping("/races-by-pilot-and-country/{pilotId}/{countryId}")
    public ResponseEntity<RacePilotCountryDTO> findRaceByPilotAndCountry(@PathVariable Integer pilotId, @PathVariable Integer countryId){
        Pilot pilot = pilotService.findById(pilotId);
        
        Country country = countryService.findById(countryId);
        
        List<RacePilotDTO> racesDTO = racePilotService.findByPilotOrderByPlacing(pilot).stream().flatMap(racePilot -> {
            try {
                return racePilotService.findByRaceOrderByPlacing(racePilot.getRace()).stream();
            } catch (ObjectNotFound e) {
                return Stream.empty();
            }
        }).filter(racePilot -> racePilot.getPilot().getCountry().getId() == countryId).map(RacePilot::toDTO).toList();
        
        return ResponseEntity.ok(new RacePilotCountryDTO(pilot.getName(), country.getName(), racesDTO.size(), racesDTO));
    }

}
