package br.com.trier.springverspertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springverspertino.models.Team;


@Repository
public interface TeamRepository extends JpaRepository<Team, Integer>{
    List<Team> findByNameContainingIgnoreCase(String name);
    
    Team findByName(String name);

}
