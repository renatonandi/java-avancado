package br.com.trier.springverspertino.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.repositories.ChampionshipRepository;
import br.com.trier.springverspertino.services.ChampionshipService;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@Service
public class ChampionshipServiceImpl implements ChampionshipService{

    @Autowired
    private ChampionshipRepository repository;
    
    @Override
    public Championship findById(Integer id) {
        Optional<Championship> champ = repository.findById(id);
        return champ.orElseThrow(() -> new ObjectNotFound("O campeonato %s não existe".formatted(id)));
    }

    @Override
    public Championship insert(Championship championship) {
        validateYear(championship.getYear());
        return repository.save(championship);
    }

    @Override
    public List<Championship> listAll() {
        List<Championship> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem um campeonato cadastrado");
        }
        return list;
    }

    @Override
    public Championship update(Championship championship) {
        findById(championship.getId());
        validateYear(championship.getYear());
        return repository.save(championship);
    }

    @Override
    public void delete(Integer id) {
        Championship champ = findById(id);
        if (champ != null) {
            repository.delete(champ);
        }
    }

    @Override
    public List<Championship> findByYearAndDescription(Integer firstYear, Integer lastYear, String description) {
        validateYear(firstYear);
        validateYear(lastYear);
        List<Championship> list = repository.findByYearBetweenAndDescriptionContainingIgnoreCase(firstYear, lastYear, description);
        
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem um campeonato encontrado");
        }
        return list;
    }

    @Override
    public List<Championship> findByYear(Integer year) {
        validateYear(year);
        List<Championship> list = repository.findByYear(year);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem um campeonato com esse ano cadastrado");
        }
        return list;
    }

    
    private void validateYear(Integer year) {
        if (year == null) {
            throw new IntegrityViolation("Ano inválido. O ano não pode ser nulo");
        }
        if (year < 1990 || year > LocalDate.now().getYear() + 1) {
            throw new IntegrityViolation("Ano inválido. O ano não pode ser menos que 1990 e mais que o próximo ano");
        }
        
    }

   
}
