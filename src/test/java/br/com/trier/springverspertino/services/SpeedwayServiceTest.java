package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springverspertino.BaseTest;
import br.com.trier.springverspertino.models.Speedway;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class SpeedwayServiceTest extends BaseTest {

	@Autowired
	SpeedwayService speedwayService;

	@Autowired
	CountryService countryService;

	@Test
	@DisplayName("Teste buscar pista por id")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void findByIdTest() {
		var speedway = speedwayService.findById(1);
		assertNotNull(speedway);
		assertEquals(1, speedway.getId());
		assertEquals("Pista1", speedway.getName());
	}

	@Test
	@DisplayName("Teste buscar pista por id inexistente")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void findByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> speedwayService.findById(4));
		assertEquals("A pista 4 não existe.", exception.getMessage());
	}

	@Test
	@DisplayName("Teste de listagem de todos os registros")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void listAllTest() {
		var speedway = speedwayService.listAll();
		assertNotNull(speedway);
		assertEquals(3, speedway.size());
	}

	@Test
	@DisplayName("Teste de listagem de todos os registros sem nem um cadastrado")
	void listAllNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> speedwayService.listAll());
		assertEquals("Nenhuma pista cadastrada.", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir pista")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void insertSpeedwayTest() {
		Speedway speedway = new Speedway(null, "Pista", 5000, countryService.findById(1));
		speedwayService.insert(speedway);
		speedway = speedwayService.findById(1);
		assertEquals(1, speedway.getId());
		assertEquals("Pista", speedway.getName());
		assertEquals(5000, speedway.getSize());
		assertEquals("Brasil", speedway.getCountry().getName());
	}

	@Test
    @DisplayName("Teste inserir pista de tamanho inválido")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void insertSpeedwayInvalidSizeTest() {
        Speedway speedway = new Speedway(null, "insert", -2000, null);
        assertThrows(IntegrityViolation.class, () -> {
            speedwayService.insert(speedway);
        });
    }
	
	@Test
	@DisplayName("Teste remover pista")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void deleteSpeedwayTest() {
		speedwayService.delete(1);
		List<Speedway> list = speedwayService.listAll();
		assertEquals(2, list.size());
		assertEquals(2, list.get(0).getId());
	}

	@Test
	@DisplayName("Teste alterar pista")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void updateSpeedwayTest() {
		var speedway = speedwayService.findById(1);
		assertEquals("Pista1", speedway.getName());
		var speedwayNew = new Speedway(1, "Pista 500", 3000, countryService.findById(1));
		speedwayService.update(speedwayNew);
		assertEquals("Pista 500", speedwayService.findById(1).getName());
	}

	@Test
	@DisplayName("Teste buscar pista por nome")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void findByNameTest() {
		var speedway = speedwayService.findByNameStartsWithIgnoreCase("Pi");
		assertNotNull(speedway);
		assertEquals(3, speedway.size());
		var speedway2 = speedwayService.findByNameStartsWithIgnoreCase("Pista3");
		assertEquals(1, speedway2.size());
	}

	@Test
	@DisplayName("Teste buscar pista por nome errado")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void findByNameInvalidTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> speedwayService.findByNameStartsWithIgnoreCase("w"));
		assertEquals("Nenhuma pista encontrada.", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar por tamanho entre")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void findBySizeBetweenTest() {
		List<Speedway> result = speedwayService.findBySizeBetween(1000, 3000);
		assertEquals(3, result.size());
		assertEquals("Pista1", result.get(0).getName());
		assertEquals("Pista2", result.get(1).getName());
		assertEquals("Pista3", result.get(2).getName());
	}

	@Test
	@DisplayName("Teste buscar por tamanho invalido")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void findBySizeBetweenInvalidTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> speedwayService.findBySizeBetween(0, 100));
		assertEquals("Nenhuma pista encontrada com essas medidas.", exception.getMessage());
	}

	@Test
	@DisplayName("Teste nenhuma pista cadastrada para o país")
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByCountryOrderBySizeWithNo() {
		assertThrows(ObjectNotFound.class, () -> speedwayService.findByCountryOrderBySize(countryService.findById(1)));
	}

	@Test
	@DisplayName("Teste buscar pistas encontradas")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	void findByCountryOrderBySizeTest() {
		speedwayService.insert(new Speedway(1, "Pista4", 1000, countryService.findById(1)));
		speedwayService.insert(new Speedway(2, "Pista5", 2000, countryService.findById(1)));
		List<Speedway> list = speedwayService.findByCountryOrderBySize(countryService.findById(1));
		assertEquals(2, list.size());
		assertEquals("Pista4", list.get(0).getName());
		assertEquals("Pista5", list.get(1).getName());
	}

}
