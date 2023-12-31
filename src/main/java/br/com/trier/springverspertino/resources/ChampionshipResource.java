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

import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.services.ChampionshipService;

@RestController
@RequestMapping("/campeonato")
public class ChampionshipResource {
    
    @Autowired
    private ChampionshipService service;
    
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<Championship> insert(@RequestBody Championship champ){
        Championship newChamp = service.insert(champ);
        return newChamp != null ? ResponseEntity.ok(newChamp) : ResponseEntity.noContent().build();
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<Championship> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Secured({"ROLE_USER"})
    @GetMapping
    public ResponseEntity<List<Championship>> listAll(){
        return ResponseEntity.ok(service.listAll());
    }
    
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<Championship> update(@PathVariable Integer id, @RequestBody Championship champ){
        champ.setId(id);
        return ResponseEntity.ok(service.update(champ));
    }
    
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/ano/{firstYear}/{lastYear}/{description}")
    public ResponseEntity<List<Championship>> findByYearAndDescription(@PathVariable Integer firstYear, @PathVariable Integer lastYear, @PathVariable String description){
        return ResponseEntity.ok(service.findByYearAndDescription(firstYear, lastYear, description));
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/ano/{year}")
    public ResponseEntity<List<Championship>> findByYear(@PathVariable Integer year){
        return ResponseEntity.ok(service.findByYear(year));
    }
}
