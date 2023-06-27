package br.com.trier.springverspertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.dto.RaceDTO;
import br.com.trier.springverspertino.services.ChampionshipService;
import br.com.trier.springverspertino.services.RaceService;
import br.com.trier.springverspertino.services.SpeedwayService;

@RestController
@RequestMapping("/corrida")
public class RaceResource {
    
    @Autowired
    private RaceService service;
    
    @Autowired
    private SpeedwayService speedwayService;
    
    @Autowired
    private ChampionshipService championshipService;
    
    @Secured({"ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<RaceDTO> findById(@PathVariable Integer id){ 
        return ResponseEntity.ok(service.findById(id).toDTO());
    }
    
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<RaceDTO> insert(@RequestBody RaceDTO raceDTO){
        return ResponseEntity.ok(service.insert(new Race(
                raceDTO,
                championshipService.findById(raceDTO.getChampionshipId()),
                speedwayService.findById(raceDTO.getSpeedwayId()))).toDTO());
    }

    @Secured({"ROLE_USER"})
    @GetMapping
    public ResponseEntity<List<RaceDTO>> listAll(){
        return ResponseEntity.ok(service.listAll().stream().map((race -> race.toDTO())).toList());
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<RaceDTO> update(@PathVariable Integer id, @RequestBody RaceDTO raceDTO){
        Race race = new Race(raceDTO, championshipService.findById(raceDTO.getChampionshipId()), speedwayService.findById(raceDTO.getSpeedwayId()));
        race.setId(id);
        return ResponseEntity.ok(service.update(race).toDTO());
    }
    
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/corrida/{idSpeedway}")
    public ResponseEntity<List<Race>> findBfindBySpeedway(@PathVariable Integer idSpeedway){
       return ResponseEntity.ok(service.findBySpeedwayOrderByDate(speedwayService.findById(idSpeedway)));
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/corrida/{idChampionship}")
    public ResponseEntity<List<Race>> findBfindByChampionship(@PathVariable Integer idChampionship){
        return ResponseEntity.ok(service.findByChampionshipOrderByDate(championshipService.findById(idChampionship)));
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/data/{firstDate}/{lastDate}")
    public ResponseEntity<List<RaceDTO>> findByDateBetween(@PathVariable String firstDate, @PathVariable String lastDate){
        return ResponseEntity.ok(service.findByDateBetween(firstDate, lastDate).stream().map(race -> race.toDTO()).toList());
    }

}
