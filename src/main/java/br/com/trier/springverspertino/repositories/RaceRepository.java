package br.com.trier.springverspertino.repositories;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.Speedway;

@Repository
public interface RaceRepository extends JpaRepository<Race, Integer>{
    
    List<Race> findBySpeedwayOrderByDate(Speedway speedway);
    
    List<Race> findByChampionshipOrderByDate(Championship championship);
    
    List<Race> findByDateBetween(ZonedDateTime firstDate, ZonedDateTime lastDate);

}
