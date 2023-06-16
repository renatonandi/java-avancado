package br.com.trier.springverspertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.repositories.CountryRepository;
import br.com.trier.springverspertino.services.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository repository;
    
    @Override
    public Country findById(Integer id) {
        Optional<Country> country = repository.findById(id);
        return country.orElse(null);
    }

    @Override
    public Country insert(Country country) {
        return repository.save(country);
    }

    @Override
    public List<Country> listAll() {
        return repository.findAll();
    }

    @Override
    public Country update(Country country) {
        return repository.save(country);
    }

    @Override
    public void delete(Integer id) {
        Country country = findById(id);
        if (country != null) {
            repository.delete(country);
        }
    }

    @Override
    public List<Country> findByCountry(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

}
