package br.com.trier.springverspertino.services;

import java.util.List;

import br.com.trier.springverspertino.models.User;

public interface UserService {
    
    User findById(Integer id); 
        
    User insert(User user);
    
    List<User> listAll();
    
    User update(User user);
    
    void delete(Integer id);
      

}
