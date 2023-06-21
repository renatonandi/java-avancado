package br.com.trier.springverspertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.models.Speedway;

@Repository
public interface SpeedwayRepository extends JpaRepository<Speedway, Integer>{
    
    List<Speedway> findByNameStartsWithIgnoreCase(String name);
    
    List<Speedway> findBySizeBetween(Integer sizeIn, Integer sizeFin);
    
    List<Speedway> findByCountryOrderBySize(Country country);

}
