package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;
import java.util.List;

import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springverspertino.BaseTest;
import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import br.com.trier.springverspertino.utils.DateUtils;
import jakarta.transaction.Transactional;

@Transactional
public class RaceServiceTest extends BaseTest {

	@Autowired
	RaceService service;

	@Autowired
	SpeedwayService speedwayService;

	@Autowired
	ChampionshipService championshipService;

	@Test
	@DisplayName("Teste buscar corrida por ID")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void findByIdTest() {
		var race = service.findById(1);
		assertNotNull(race);
		assertEquals(1, race.getId());
		assertEquals("Formula 1", race.getChampionship().getDescription());
		assertEquals(1990, race.getDate().getYear());
	}

	@Test
	@DisplayName("Teste buscar corrida por id inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void findByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> service.findById(4));
		assertEquals("A corrida 4 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste de listagem de todos os registros")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void listAllTest() {
		assertNotNull(service.listAll());
		assertEquals(3, service.listAll().size());
	}

	@Test
	@DisplayName("Teste de listagem de todos os registros sem nem um cadastrado")
	void listAllNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
		assertEquals("Nenhuma corrida cadastrada", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir corrida")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void insertPistaTest() {
		Race race = new Race(null, DateUtils.strToZonedDateTime("01/01/1990"), championshipService.findById(1),
				speedwayService.findById(1));
		service.insert(race);
		assertEquals(1, service.listAll().size());
		assertEquals("Formula 1", race.getChampionship().getDescription());
	}

	@Test
	@DisplayName("Teste alterar corrida")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void updateUsersTest() {
		var race = service.findById(1);
		assertEquals("Formula 1", race.getChampionship().getDescription());
		var raceNew = new Race(1, DateUtils.strToZonedDateTime("01/01/2000"), championshipService.findById(2),
				speedwayService.findById(1));
		service.update(raceNew);
		assertEquals("Formula Ind", service.findById(1).getChampionship().getDescription());
	}

	@Test
	@DisplayName("Alterar corrida data não condiz com ano do campeonato")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void updateInvalidDate() {
		Race race = new Race(1, DateUtils.strToZonedDateTime("01/10/2000"), championshipService.findById(1),
				speedwayService.findById(2));
		var ex = assertThrows(IntegrityViolation.class, () -> service.update(race));
		assertEquals("A data difere da data do campeonato: 1990", ex.getMessage());
	}

	@Test
	@DisplayName("Alterar corrida não existente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void updateInvalid() {
		Race race = new Race(4, ZonedDateTime.now(), championshipService.findById(1), speedwayService.findById(2));
		var ex = assertThrows(ObjectNotFound.class, () -> service.update(race));
		assertEquals("A corrida 4 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Remover corrida")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void removePilotoTest() {
		service.delete(1);
		List<Race> list = service.listAll();
		assertEquals(2, list.size());
		assertEquals(2, list.get(0).getId());
	}

	@Test
	@DisplayName("Remover corrida não existente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void deleteInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.delete(4));
		assertEquals("A corrida 4 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Buscar pista ordenado por data")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void findByRunwayOrderByDate() {
		List<Race> list = service.findBySpeedwayOrderByDate(speedwayService.findById(1));
		assertEquals(1, list.size());
	}

	@Test
	@DisplayName("Buscar pista ordenado por data não existe")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void findByRunwayOrderByDateNonExist() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findBySpeedwayOrderByDate(speedwayService.findById(3)));
		assertEquals("Nenhuma corrida encontrada para pista: Pista3", ex.getMessage());
	}
	
	@Test
	@DisplayName("Buscar campeonato ordenado por data")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void findByChampionshipOrderByDate() {
		List<Race> lista = service.findByChampionshipOrderByDate(championshipService.findById(1));
		assertEquals(1, lista.size());
	}
	
	@Test
	@DisplayName("Buscar campeonato ordenado por data não existe")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void findByChampionshipOrderByDateNonExist() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByChampionshipOrderByDate(championshipService.findById(3)));
		assertEquals("Nenhuma corrida para o campeonato Formula E foi encontrada", ex.getMessage());
	}
	
	@Test
	@DisplayName("Encontra por data entre")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void findByDateBetween() {
		List<Race> list = service.findByDateBetween("01/01/1990", "01/10/1992");
		assertEquals(1, list.size());	
	}
	
	@Test
	@DisplayName("Encontra por data entre não existe")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	void findByDateBetweenNonExist() {	
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByDateBetween("01/10/1991", "01/10/1998"));
		assertEquals("Nenhuma corrida foi encontrada para a data selecionada", ex.getMessage());
	}

}
