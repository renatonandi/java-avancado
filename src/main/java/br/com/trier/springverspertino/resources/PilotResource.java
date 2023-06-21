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

import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.services.CountryService;
import br.com.trier.springverspertino.services.PilotService;
import br.com.trier.springverspertino.services.TeamService;

@RestController
@RequestMapping("/piloto")
public class PilotResource {
    
    @Autowired
    private PilotService service;
    
    @Autowired
    private TeamService teamService;
    
    @Autowired
    private CountryService countryService;
    
    @GetMapping("/{id}")
    public ResponseEntity<Pilot> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Pilot> insert(@RequestBody Pilot pilot){
        countryService.findById(pilot.getCountry().getId());
        teamService.findById(pilot.getTeam().getId());
        return ResponseEntity.ok(service.insert(pilot));
    }

    @GetMapping
    public ResponseEntity<List<Pilot>> listAll(){
        return ResponseEntity.ok(service.listAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pilot> update(@PathVariable Integer id, @RequestBody Pilot pilot){
        pilot.setId(id);
        countryService.findById(pilot.getCountry().getId());
        teamService.findById(pilot.getTeam().getId());
        return ResponseEntity.ok(service.update(pilot));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Pilot>> findByName(@PathVariable String name){
        return ResponseEntity.ok(service.findByNameContainingIgonreCase(name));
    }

    @GetMapping("/name/pais/{name}/{idCountry}")
    public ResponseEntity<List<Pilot>> findByNameContainingAndCountryOrderByName(@PathVariable String name, @PathVariable Integer idCountry){
        return ResponseEntity.ok(service.findByNameContainingAndCountryOrderByName(name, countryService.findById(idCountry)));
    }

    @GetMapping("/equipe/{idTeam}")
    public ResponseEntity<List<Pilot>> findByTeamOrderByName(@PathVariable Integer idTeam){
        return ResponseEntity.ok(service.findByTeamOrderByName(teamService.findById(idTeam)));
    }

}
