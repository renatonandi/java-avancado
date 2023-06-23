package br.com.trier.springverspertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.RacePilot;

public interface RacePilotRepository extends JpaRepository<RacePilot, Integer>{
	
	List<RacePilot> findByPilotOrderByPlacing(Pilot pilot);
	
	List<RacePilot> findByRaceOrderByPlacing(Race race);
	
	List<RacePilot> findByPlacing(Integer placing);

}
