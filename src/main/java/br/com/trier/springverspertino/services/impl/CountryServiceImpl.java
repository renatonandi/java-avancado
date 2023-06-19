package br.com.trier.springverspertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.models.User;
import br.com.trier.springverspertino.repositories.CountryRepository;
import br.com.trier.springverspertino.services.CountryService;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository repository;

    @Override
    public Country findById(Integer id) {
        Optional<Country> country = repository.findById(id);
        return country.orElseThrow(() -> new ObjectNotFound("O país %s não existe".formatted(id)));
    }

    @Override
    public Country insert(Country country) {
        findByExistName(country);
        return repository.save(country);
    }

    @Override
    public List<Country> listAll() {
        List<Country> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem um país cadastrado");
        }
        return list;
    }

    @Override
    public Country update(Country country) {
        findById(country.getId());
        findByExistName(country);
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
        List<Country> list = repository.findByNameContainingIgnoreCase(name);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem um país encontrado");
        }
        return list;
    }

    private void findByExistName(Country country) {
        Country busca = repository.findByName(country.getName());
        if (busca != null && busca.getId() != country.getId()) {
            throw new IntegrityViolation("Esse país já está cadastrado");
        }
    }
}
