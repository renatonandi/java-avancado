package br.com.trier.springverspertino.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.repositories.ChampionshipRepository;
import br.com.trier.springverspertino.services.ChampionshipService;

@Service
public class ChampionshipServiceImpl implements ChampionshipService{

    @Autowired
    private ChampionshipRepository repository;
    
    @Override
    public Championship findById(Integer id) {
        Optional<Championship> champ = repository.findById(id);
        return champ.orElse(null);
    }

    @Override
    public Championship insert(Championship championship) {
        Integer dataAtual = LocalDate.now().getYear();
        
        if (championship.getYear() < dataAtual) {
            throw new IllegalArgumentException("A data não pode ser menor que a data atual");
        }
        return repository.save(championship);
    }

    @Override
    public List<Championship> listAll() {
        return repository.findAll();
    }

    @Override
    public Championship update(Championship championship) {
        return repository.save(championship);
    }

    @Override
    public void delete(Integer id) {
        Championship champ = findById(id);
        if (champ != null) {
            repository.delete(champ);
        }
        
    }

}
