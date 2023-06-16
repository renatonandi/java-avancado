package br.com.trier.springverspertino.services;

import java.util.List;

import br.com.trier.springverspertino.models.Team;

public interface TeamService {

    Team findById(Integer id);
    
    Team insert(Team team);
    
    List<Team> listAll();
    
    List<Team> findByTeam(String name);
    
    Team update(Team team);
    
    void delete(Integer id);
    
}
