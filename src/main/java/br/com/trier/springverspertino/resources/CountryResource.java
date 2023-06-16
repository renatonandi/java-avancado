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

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.services.CountryService;

@RestController
@RequestMapping("/pais")
public class CountryResource {

    @Autowired
    private CountryService service;
    
    @PostMapping
    public ResponseEntity<Country> insert(@RequestBody Country country){
        Country newCountry = service.insert(country);
        return newCountry != null ? ResponseEntity.ok(newCountry) : ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Country> findById(@PathVariable Integer id){
        Country country = service.findById(id);
        return country != null ? ResponseEntity.ok(country) : ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Country>> listAll(){
        List<Country> list = service.listAll();
        return list.size() > 0 ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Country> update(@PathVariable Integer id, @RequestBody Country country){
        country.setId(id);
        country = service.update(country);
        return country != null ? ResponseEntity.ok(country) : ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/nome/{name}")
    public ResponseEntity<List<Country>> findById(@PathVariable String name){
        List<Country> list = service.findByCountry(name);
        return list.size() > 0 ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
    }
    
}
