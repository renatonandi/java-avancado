package br.com.trier.springverspertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.Team;
import br.com.trier.springverspertino.repositories.TeamRepository;
import br.com.trier.springverspertino.services.TeamService;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@Service
public class TeamServiceImpl implements TeamService{

    @Autowired
    private TeamRepository repository;
    
    @Override
    public Team findById(Integer id) {
        Optional<Team> team = repository.findById(id);
        return team.orElseThrow(() -> new ObjectNotFound("A equipe %s não existe".formatted(id)));
    }

    @Override
    public Team insert(Team team) {
        findByExistName(team);
        return repository.save(team);
    }

    @Override
    public List<Team> listAll() {
        List<Team> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem uma equipe cadastrada");
        }
        return list;
    }

    @Override
    public Team update(Team team) {
        findById(team.getId());
        findByExistName(team);
        return repository.save(team);
    }

    @Override
    public void delete(Integer id) {
        Team team = findById(id);
        if (team != null) {
            repository.delete(team);
        }
    }

    @Override
    public List<Team> findByTeam(String name) {
        List<Team> list = repository.findByNameContainingIgnoreCase(name);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nemn uma equipe encontrada");
        }
        return list;
    }
    
    private void findByExistName(Team team) {
        Team busca = repository.findByName(team.getName());
        if (busca != null && busca.getId() != team.getId()) {
            throw new IntegrityViolation("Essa equipe já está cadastrada");
        }
    }

}
