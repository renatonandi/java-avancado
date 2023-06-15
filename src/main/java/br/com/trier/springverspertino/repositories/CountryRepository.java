package br.com.trier.springverspertino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.springverspertino.models.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>{

}
