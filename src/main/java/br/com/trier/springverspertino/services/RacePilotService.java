package br.com.trier.springverspertino.services;

import java.util.List;

import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.models.RacePilot;

public interface RacePilotService {

    RacePilot findById(Integer id);

    RacePilot insert(RacePilot racePilot);

    List<RacePilot> listAll();

    RacePilot update(RacePilot racePilot);

    void delete(Integer id);

    List<RacePilot> findByPilotOrderByPlacing(Pilot pilot);

    List<RacePilot> findByRaceOrderByPlacing(Race race);

    List<RacePilot> findByPlacing(Integer placing);

}
