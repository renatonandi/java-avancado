package br.com.trier.springverspertino.services;

import java.util.List;

import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.Team;

public interface PilotService {

    Pilot findById(Integer id);

    Pilot insert(Pilot pilot);

    List<Pilot> listAll();

    Pilot update(Pilot pilot);

    void delete(Integer id);

    List<Pilot> findByNameContainingIgonreCase(String name);

    List<Pilot> findByNameContainingAndCountryOrderByName(String name, Country country);

    List<Pilot> findByTeamOrderByName(Team team);

}
