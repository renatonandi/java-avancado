package br.com.trier.springverspertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springverspertino.models.RacePilot;
import br.com.trier.springverspertino.models.dto.RacePilotDTO;
import br.com.trier.springverspertino.services.PilotService;
import br.com.trier.springverspertino.services.RacePilotService;
import br.com.trier.springverspertino.services.RaceService;

@RestController
@RequestMapping("/races/pilots")
public class RacePilotResource {
    
    @Autowired
    private RacePilotService service;
    
    @Autowired
    private RaceService raceService;
    
    @Autowired
    private PilotService pilotService;
    
    @Secured({"ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<RacePilotDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id).toDTO());
    }
    
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<RacePilotDTO> insert(@RequestBody RacePilotDTO racePilotDTO){
        RacePilot racePilot = new RacePilot(racePilotDTO, pilotService.findById(racePilotDTO.getPilotId()), raceService.findById(racePilotDTO.getRaceId())); 
        return ResponseEntity.ok(service.insert(racePilot).toDTO());
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping
    public ResponseEntity<List<RacePilotDTO>> listAll(){
        return ResponseEntity.ok(service.listAll()
                .stream()
                .map(racePilot -> racePilot.toDTO())
                .toList());
    }
    
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/{id}")
    public ResponseEntity<RacePilotDTO> update (@RequestBody RacePilotDTO racePilotDTO, @PathVariable Integer id){
        RacePilot racePilot = new RacePilot(racePilotDTO, pilotService.findById(racePilotDTO.getPilotId()), raceService.findById(racePilotDTO.getRaceId())); 
        racePilot.setId(id);
        return ResponseEntity.ok(service.update(racePilot).toDTO());
        
    }
    
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/placing/{placing}")
    public ResponseEntity<List<RacePilotDTO>> findByPlacement(@PathVariable Integer placing){
        return ResponseEntity.ok(service.findByPlacing(placing)
                .stream()
                .map(pilot -> pilot.toDTO())
                .toList());
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/pilot/{id}")
    public ResponseEntity<List<RacePilotDTO>> findByPilotOrderByPlacement(@PathVariable Integer id){
        return ResponseEntity.ok(service.findByPilotOrderByPlacing(pilotService.findById(id))
                .stream()
                .map(pilot -> pilot.toDTO())
                .toList());
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/race/{id}")
    public ResponseEntity<List<RacePilotDTO>> findByRaceOrderByPlacement(@PathVariable Integer id){
        return ResponseEntity.ok(service.findByRaceOrderByPlacing(raceService.findById(id))
                .stream()
                .map(pilot -> pilot.toDTO())
                .toList());
    }

}
