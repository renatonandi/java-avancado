package br.com.trier.springverspertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.Team;
import br.com.trier.springverspertino.repositories.PilotRepository;
import br.com.trier.springverspertino.services.PilotService;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@Service
public class PilotServiceImpl implements PilotService{
    
    @Autowired
    private PilotRepository repository;

    @Override
    public Pilot findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFound("O piloto %s não existe".formatted(id)));
    }

    @Override
    public Pilot insert(Pilot pilot) {
        return repository.save(pilot);
    }

    @Override
    public List<Pilot> listAll() {
        List<Pilot> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto cadastrado");
        }
        return list;
    }

    @Override
    public Pilot update(Pilot pilot) {
        findById(pilot.getId());
        return repository.save(pilot);
    }

    @Override
    public void delete(Integer id) {
        Pilot pilot = findById(id);
        repository.delete(pilot);
        
    }

    @Override
    public List<Pilot> findByNameContainingIgonreCase(String name) {
        List<Pilot> list = repository.findByNameContainingIgnoreCase(name);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto encontrado");
        }
        return list;
    }

    @Override
    public List<Pilot> findByNameContainingAndCountryOrderByName(String name, Country country) {
        List<Pilot> list = repository.findByNameContainingAndCountryOrderByName(name, country);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto encontrado para esse país: %s".formatted(country.getName()));
        }
        return list;
    }

    @Override
    public List<Pilot> findByTeamOrderByName(Team team) {
        List<Pilot> list = repository.findByTeamOrderByName(team);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto encontrado para essa equipe: %s".formatted(team.getName()));
        }
        return list;
    }

    

}
