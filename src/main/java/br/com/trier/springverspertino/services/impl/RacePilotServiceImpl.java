package br.com.trier.springverspertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.RacePilot;
import br.com.trier.springverspertino.repositories.RacePilotRepository;
import br.com.trier.springverspertino.services.RacePilotService;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@Service
public class RacePilotServiceImpl implements RacePilotService{

    @Autowired
    private RacePilotRepository repository;
    
    @Override
    public RacePilot findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFound("Piloto Corrida %s não existe".formatted(id)));
    }

    @Override
    public RacePilot insert(RacePilot racePilot) {
        return repository.save(racePilot);
    }

    @Override
    public List<RacePilot> listAll() {
        List<RacePilot> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto corrida encontrada");
        }
        return null;
    }

    @Override
    public RacePilot update(RacePilot racePilot) {
        findById(racePilot.getId());
        return repository.save(racePilot);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(findById(id));
        
    }

    @Override
    public List<RacePilot> findByRaceOrderByPlacing(Race race) {
        List<RacePilot> list = repository.findByRaceOrderByPlacing(race);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto foi encontrado para a corrida: %s".formatted(race.getId()));
        }
        return list;
    }

    @Override
    public List<RacePilot> findByPilotOrderByPlacing(Pilot pilot) {
        List<RacePilot> list = repository.findByPilotOrderByPlacing(pilot);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem um piloto foi encontrado");
        }
        return list;
    }

    @Override
    public List<RacePilot> findByPlacing(Integer placing) {
        List<RacePilot> list = repository.findByPlacing(placing);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem um piloto encontrado para a colocação %s".formatted(placing));
        }
        return list;
    }

}
