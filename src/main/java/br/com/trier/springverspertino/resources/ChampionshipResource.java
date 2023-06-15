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

import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.services.ChampionshipService;

@RestController
@RequestMapping("/campeonato")
public class ChampionshipResource {
    @Autowired
    private ChampionshipService service;
    
    @PostMapping
    public ResponseEntity<Championship> insert(@RequestBody Championship champ){
        Championship newChamp = service.insert(champ);
        return newChamp != null ? ResponseEntity.ok(newChamp) : ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Championship> findById(Integer id){
        Championship champ = service.findById(id);
        return champ != null ? ResponseEntity.ok(champ) : ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Championship>> listAll(){
        List<Championship> list = service.listAll();
        return list.size() > 0 ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Championship> update(@PathVariable Integer id, @RequestBody Championship champ){
        champ.setId(id);
        champ = service.update(champ);
        return champ != null ? ResponseEntity.ok(champ) : ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
