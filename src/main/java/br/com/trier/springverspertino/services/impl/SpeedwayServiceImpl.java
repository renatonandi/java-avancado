package br.com.trier.springverspertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.models.Speedway;
import br.com.trier.springverspertino.repositories.SpeedwayRepository;
import br.com.trier.springverspertino.services.SpeedwayService;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@Service
public class SpeedwayServiceImpl implements SpeedwayService{
    
    @Autowired
    private SpeedwayRepository repository;
    
    private void validateSpeedway(Speedway speedway) {
        if (speedway.getSize() == null || speedway.getSize() <= 0) {
            throw new IntegrityViolation("Tamanho da pista inválido");
        }
    }

    @Override
    public Speedway findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A pista %s não existe".formatted(id)));
    }

    @Override
    public Speedway insert(Speedway speedway) {
        validateSpeedway(speedway);
        return repository.save(speedway);
    }

    @Override
    public List<Speedway> listAll() {
        List<Speedway> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhuma pista cadastrada");
        }
        return list;
    }

    @Override
    public Speedway update(Speedway speedway) {
        findById(speedway.getId());
        validateSpeedway(speedway);
        return repository.save(speedway);
    }

    @Override
    public void delete(Integer id) {
        Speedway speedway = findById(id);
        repository.delete(speedway);
    }

    @Override
    public List<Speedway> findByNameStartsWithIgnoreCase(String name) {
        List<Speedway> list = repository.findByNameStartsWithIgnoreCase(name);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhuma pista encontrada");
        }
        return list;
    }

    @Override
    public List<Speedway> findBySizeBetween(Integer sizeIn, Integer sizeFin) {
        List<Speedway> list = repository.findBySizeBetween(sizeIn, sizeFin);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhuma pista encontrada com essas medidas");
        }
        return list;
    }

    @Override
    public List<Speedway> findByCountryOrderBySize(Country country) {
        List<Speedway> list = repository.findByCountryOrderBySize(country);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhuma pista encontrada para esse país: %s".formatted(country.getName()));
        }
        return list;
    }

}
