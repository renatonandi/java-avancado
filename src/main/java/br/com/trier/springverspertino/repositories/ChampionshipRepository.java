package br.com.trier.springverspertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springverspertino.models.Championship;


@Repository
public interface ChampionshipRepository extends JpaRepository<Championship, Integer>{
    
    List<Championship> findByYearBetweenAndDescriptionContainingIgnoreCase(Integer firstYear, Integer lastYear, String description);
    
    List<Championship> findByYear(Integer year);

}
