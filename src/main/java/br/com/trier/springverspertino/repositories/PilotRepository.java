package br.com.trier.springverspertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.Team;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Integer>{
    
    List<Pilot> findByNameContainingIgnoreCase(String name);
    
    List<Pilot> findByNameContainingAndCountryOrderByName(String name, Country country);
    
    List<Pilot> findByTeamOrderByName(Team team);

}
