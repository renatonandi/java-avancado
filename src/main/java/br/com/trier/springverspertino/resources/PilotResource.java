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

import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.dto.PilotDTO;
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
    
    @Secured({"ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<PilotDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id).toDTO());
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<PilotDTO> insert(@RequestBody PilotDTO pilotDTO){
        return ResponseEntity.ok(service.insert(new Pilot(
                pilotDTO, countryService.findById(pilotDTO.getCountryId()),
                teamService.findById(pilotDTO.getTeamId()))).toDTO());
    }

    @Secured({"ROLE_USER"})
    @GetMapping
    public ResponseEntity<List<PilotDTO>> listAll(){
        return ResponseEntity.ok(service.listAll().stream().map((pilot -> pilot.toDTO())).toList());
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<PilotDTO> update(@PathVariable Integer id, @RequestBody PilotDTO pilotDTO){
        
        Pilot pilot = new Pilot(pilotDTO, countryService.findById(pilotDTO.getId()), teamService.findById(pilotDTO.getId()));
        pilot.setId(id);
        return ResponseEntity.ok(service.update(pilot).toDTO());
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Pilot>> findByName(@PathVariable String name){
        return ResponseEntity.ok(service.findByNameContainingIgonreCase(name));
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/name/pais/{name}/{idCountry}")
    public ResponseEntity<List<Pilot>> findByNameContainingAndCountryOrderByName(@PathVariable String name, @PathVariable Integer idCountry){
        return ResponseEntity.ok(service.findByNameContainingAndCountryOrderByName(name, countryService.findById(idCountry)));
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/equipe/{idTeam}")
    public ResponseEntity<List<Pilot>> findByTeamOrderByName(@PathVariable Integer idTeam){
        return ResponseEntity.ok(service.findByTeamOrderByName(teamService.findById(idTeam)));
    }

}
