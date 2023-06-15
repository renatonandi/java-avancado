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

import br.com.trier.springverspertino.models.User;
import br.com.trier.springverspertino.services.UserService;

@RestController
@RequestMapping("/usuarios")
public class UserResource {
    
    @Autowired
    private UserService service;
    
    @PostMapping
    public ResponseEntity<User> insert(@RequestBody User user) {
        User newUser = service.insert(user);
        return newUser!=null ? ResponseEntity.ok(newUser) : ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id){
        User user = service.findById(id);
        return user!=null ? ResponseEntity.ok(user) : ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<User>> listAll(){
        List<User> list = service.listAll();
        return list.size() > 0 ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user){
        user.setId(id);
        user = service.update(user);
        return user!=null ? ResponseEntity.ok(user) : ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    
    
}
