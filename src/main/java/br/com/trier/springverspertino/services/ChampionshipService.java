package br.com.trier.springverspertino.services;

import java.util.List;

import br.com.trier.springverspertino.models.Championship;

public interface ChampionshipService {

    Championship findById(Integer id);
    
    Championship insert(Championship championship);
    
    List<Championship> listAll();
    
    Championship update(Championship championship);
    
    void delete(Integer id);
    
}
