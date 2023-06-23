package br.com.trier.springverspertino.services;

import java.util.List;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.models.Speedway;

public interface SpeedwayService {
    
    Speedway findById(Integer id); 
    
    Speedway insert(Speedway speedway);
    
    List<Speedway> listAll();
    
    Speedway update(Speedway speedway);
    
    void delete(Integer id);
    
    List<Speedway> findByNameStartsWithIgnoreCase(String name);
    
    List<Speedway> findBySizeBetween(Integer sizeIn, Integer sizeFin);

    List<Speedway> findByCountryOrderBySize(Country country);

}
