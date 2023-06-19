package br.com.trier.springverspertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springverspertino.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    
    List<User> findByNameStartingWithIgnoreCase(String name);
    
    User findByEmail(String email);
    
}
