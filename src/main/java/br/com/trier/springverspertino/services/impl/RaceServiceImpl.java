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
import br.com.trier.springverspertino.utils.DateUtils;

@Service
public class RaceServiceImpl implements RaceService{
    
    @Autowired
    private RaceRepository repository;
    
    
    public void validateDate(Race race) {
        if (race.getChampionship() == null) {
            throw new IntegrityViolation("O campeonato não pode ser nulo");
        }
        if (race.getChampionship().getYear() != race.getDate().getYear()) {
            throw new IntegrityViolation("A data difere da data do campeonato: %s".formatted(race.getChampionship().getYear()));
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
    	findById(race.getId());
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
            throw new ObjectNotFound("Nenhuma corrida encontrada para pista: %s".formatted(speedway.getName()));
        }
        return list;
    }

    @Override
    public List<Race> findByChampionshipOrderByDate(Championship championship) {
        List<Race> list = repository.findByChampionshipOrderByDate(championship);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhuma corrida para o campeonato %s foi encontrada".formatted(championship.getDescription()));
        }
        return list;
    }

    @Override
    public List<Race> findByDateBetween(String firstDate, String lastDate) {
        List<Race> list = repository.findByDateBetween(DateUtils.strToZonedDateTime(firstDate) , DateUtils.strToZonedDateTime(lastDate));
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhuma corrida foi encontrada para a data selecionada");
        }
        return list;
    }

}
