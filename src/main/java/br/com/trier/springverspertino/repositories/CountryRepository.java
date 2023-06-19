package br.com.trier.springverspertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springverspertino.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{

    List<Country> findByNameContainingIgnoreCase(String name);
    
    Country findByName(String name);
}
