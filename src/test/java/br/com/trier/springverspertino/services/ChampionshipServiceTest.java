package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springverspertino.BaseTest;
import br.com.trier.springverspertino.models.Championship;
import jakarta.transaction.Transactional;

@Transactional
public class ChampionshipServiceTest extends BaseTest{
	
	@Autowired
	ChampionshipService champService;
	
	@Test
	@DisplayName("Buscar por id")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByIdTest() {
		Championship champ = champService.findById(1);
		assertNotNull(champ);
		assertEquals(1, champ.getId());
		assertEquals("Formula 1", champ.getDescription());
		
	}
	
	@Test
	@DisplayName("Buscar por id inválido")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByIdInvalidTest() {	
		assertNull(champService.findById(4));
	}
	
	@Test
	@DisplayName("Buscar todos")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void listAllTest() {	
		assertEquals(3, champService.listAll().size());
	}
	
	@Test
	@DisplayName("Insert novo campeonato")
	void insertTest() {	
		Championship champ = new Championship(null, "Campeonato", 2015);
		champService.insert(champ);
		assertEquals(1, champService.listAll().size());
		assertEquals(1, champ.getId());
		assertEquals("Campeonato", champ.getDescription());	
	}
	
	@Test
	@DisplayName("Update campeonato")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void updateTest() {	
		Championship champ = champService.findById(1);
		assertNotNull(champ);
		assertEquals(1, champ.getId());
		assertEquals("Formula 1", champ.getDescription());	
		champ = new Championship(1, "CampeonatoNovo", 2030);
		champService.update(champ);
		assertEquals(3, champService.listAll().size());
		assertEquals(1, champ.getId());
		assertEquals("CampeonatoNovo", champ.getDescription());	
		assertEquals(2030, champ.getYear());	
	}
	
	@Test
	@DisplayName("Delete campeonato")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void deleteTest() {	
		assertEquals(3, champService.listAll().size());
		champService.delete(1);
		assertEquals(2, champService.listAll().size());
		assertNull(champService.findById(1));
	}
	
	@Test
	@DisplayName("Delete campeonato que não existe")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void deleteIdNoExistTest() {	
		assertEquals(3, champService.listAll().size());
		champService.delete(10);
		assertEquals(3, champService.listAll().size());
	}
	
	@Test
	@DisplayName("Procura por ano")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByYearTest() {	
		assertEquals(1, champService.findByYear(1990).size());
		assertEquals(1, champService.findByYear(2000).size());
		assertEquals(1, champService.findByYear(2010).size());

	}

	@Test
	@DisplayName("Procura por ano e descrição")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByYearAndDescriptionTest() {
		var champ = champService.findByYearAndDescription(1980, 2005, "for");
		assertEquals(2, champ.size());
		champ = champService.findByYearAndDescription(1980, 1995, "For");
		assertEquals(1, champ.size());
		assertEquals("Formula 1", champ.get(0).getDescription());
	}
	
	@Test
	@DisplayName("Procura por ano e descrição errada")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByYearAndDescriptionWrongTest() {
		var champ = champService.findByYearAndDescription(1980, 2005, "z");
		assertEquals(0, champ.size());
	}

}
