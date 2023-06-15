package br.com.trier.springverspertino.services;

import java.util.List;

import br.com.trier.springverspertino.models.Country;

public interface CountryService {
    
    Country findById(Integer id);
    
    Country insert(Country country);
    
    List<Country> listAll();
    
    Country update(Country country);
    
    void delete(Integer id);

}
