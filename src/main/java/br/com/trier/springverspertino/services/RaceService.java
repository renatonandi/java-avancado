package br.com.trier.springverspertino.services;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.Speedway;

public interface RaceService {

    Race findById(Integer id);

    Race insert(Race race);

    List<Race> listAll();

    Race update(Race race);

    void delete(Integer id);

    List<Race> findBySpeedwayOrderByDate(Speedway speedway);

    List<Race> findByChampionshipOrderByDate(Championship championship);

    List<Race> findByDateBetween(ZonedDateTime firstDate, ZonedDateTime lastDate);

}
