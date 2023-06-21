package br.com.trier.springverspertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.Speedway;
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
    
    @GetMapping("/{id}")
    public ResponseEntity<Race> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<Race> insert(@RequestBody Race race){
        speedwayService.findById(race.getSpeedway().getId());
        championshipService.findById(race.getChampionship().getId());
        return ResponseEntity.ok(service.insert(race));
    }

    @GetMapping
    public ResponseEntity<List<Race>> listAll(){
        return ResponseEntity.ok(service.listAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Race> update(@PathVariable Integer id, @RequestBody Race race){
        race.setId(id);
        speedwayService.findById(race.getSpeedway().getId());
        championshipService.findById(race.getChampionship().getId());
        return ResponseEntity.ok(service.update(race));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/corrida/{idSpeedway}")
    public ResponseEntity<List<Race>> findBfindBySpeedway(Integer idSpeedway){
       return ResponseEntity.ok(service.findBySpeedwayOrderByDate(speedwayService.findById(idSpeedway)));
    }

    @GetMapping("/corrida/{idChampionship}")
    public ResponseEntity<List<Race>> findBfindByChampionship(Integer idChampionship){
        return ResponseEntity.ok(service.findByChampionshipOrderByDate(championshipService.findById(idChampionship)));
    }
    
//    public ResponseEntity<List<Race>> findByDateBetween(String firstDate, String lastDate){
//        return ResponseEntity.ok(service.findByDateBetween(service., lastDate));
//    }

}
