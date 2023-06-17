package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springverspertino.BaseTest;
import br.com.trier.springverspertino.models.Team;
import jakarta.transaction.Transactional;

@Transactional
public class TeamServiceTest extends BaseTest{
	
	@Autowired
	TeamService teamService;
	
	@Test
	@DisplayName("Buscar por id")
	@Sql({"classpath:/resources/sqls/equipe.sql"})
	void findByIdTest() {
		Team equipe = teamService.findById(1);
		assertNotNull(equipe);
		assertEquals(1, equipe.getId());
		assertEquals("Ferrari", equipe.getName());
		
	}
	
	@Test
	@DisplayName("Buscar por id inválido")
	@Sql({"classpath:/resources/sqls/equipe.sql"})
	void findByIdInvalidTest() {	
		assertNull(teamService.findById(4));
	}
	
	@Test
	@DisplayName("Buscar todos")
	@Sql({"classpath:/resources/sqls/equipe.sql"})
	void listAllTest() {	
		assertEquals(3, teamService.listAll().size());
	}
	
	@Test
	@DisplayName("Insert nova equipe")
	void insertTest() {	
		Team equipe = new Team(null, "EquipeNova");
		teamService.insert(equipe);
		assertEquals(1, teamService.listAll().size());
		assertEquals(1, equipe.getId());
		assertEquals("EquipeNova", equipe.getName());	
	}
	
	@Test
	@DisplayName("Update equipe")
	@Sql({"classpath:/resources/sqls/equipe.sql"})
	void updateTest() {	
		Team equipe = teamService.findById(1);
		assertNotNull(equipe);
		assertEquals(1, equipe.getId());
		assertEquals("Ferrari", equipe.getName());	
		equipe = new Team(1, "FerrariNova");
		teamService.update(equipe);
		assertEquals(3, teamService.listAll().size());
		assertEquals(1, equipe.getId());
		assertEquals("FerrariNova", equipe.getName());	
	}
	
	@Test
	@DisplayName("Delete equipe")
	@Sql({"classpath:/resources/sqls/equipe.sql"})
	void deleteTest() {	
		assertEquals(3, teamService.listAll().size());
		teamService.delete(1);
		assertEquals(2, teamService.listAll().size());
		assertEquals(2, teamService.listAll().get(0).getId());
	}
	
	@Test
	@DisplayName("Delete equipe que não existe")
	@Sql({"classpath:/resources/sqls/equipe.sql"})
	void deleteIdNoExistTest() {	
		assertEquals(3, teamService.listAll().size());
		teamService.delete(10);
		assertEquals(3, teamService.listAll().size());
	}
	
	@Test
	@DisplayName("Procura por nome")
	@Sql({"classpath:/resources/sqls/equipe.sql"})
	void findByTeamTest() {	
		assertEquals(1, teamService.findByTeam("f").size());
		assertEquals(1, teamService.findByTeam("m").size());
		assertEquals(2, teamService.findByTeam("r").size());
	}
	
	

}
