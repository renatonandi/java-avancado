package br.com.trier.springverspertino.services.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.Speedway;
import br.com.trier.springverspertino.repositories.RaceRepository;
import br.com.trier.springverspertino.services.RaceService;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@Service
public class RaceServiceImpl implements RaceService{
    
    @Autowired
    private RaceRepository repository;
    
    public void validateDate(Race race) {
        if (race.getDate() == null) {
            throw new IntegrityViolation("A data não pode ser nula");
        }
        if (race.getChampionship().getYear() != race.getDate().getYear()) {
            throw new IntegrityViolation("A data difere da data do campeonato");
        }
    }

    @Override
    public Race findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A corrida %s não existe".formatted(id)));
    }

    @Override
    public Race insert(Race race) {
        validateDate(race);
        return repository.save(race);
    }

    @Override
    public List<Race> listAll() {
        List<Race> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhuma corrida cadastrada");
        }
        return list;
    }

    @Override
    public Race update(Race race) {
        validateDate(race);
        return repository.save(race);
    }

    @Override
    public void delete(Integer id) {
        Race race = findById(id);
        repository.delete(race);
        
        
    }

    @Override
    public List<Race> findBySpeedwayOrderByDate(Speedway speedway) {
        List<Race> list = repository.findBySpeedwayOrderByDate(speedway);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem uma corrida encontrada");
        }
        return list;
    }

    @Override
    public List<Race> findByChampionshipOrderByDate(Championship championship) {
        List<Race> list = repository.findByChampionshipOrderByDate(championship);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem uma pista para o campeonato %s foi encontrada".formatted(championship.getDescription()));
        }
        return list;
    }

    @Override
    public List<Race> findByDateBetween(ZonedDateTime firstDate, ZonedDateTime lastDate) {
        List<Race> list = repository.findByDateBetween(firstDate, lastDate);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem uma corrida foi encontrada para a data selecionada");
        }
        return list;
    }

}
