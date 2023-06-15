package br.com.trier.springverspertino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.springverspertino.models.Team;

public interface TeamRepository extends JpaRepository<Team, Integer>{

}
