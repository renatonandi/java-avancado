package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springverspertino.BaseTest;
import br.com.trier.springverspertino.models.Country;
import jakarta.transaction.Transactional;

@Transactional
public class CountryServiceTest extends BaseTest{
	
	@Autowired
	CountryService countryService;
	
	@Test
	@DisplayName("Buscar por id")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	void findByIdTest() {
		Country country = countryService.findById(1);
		assertNotNull(country);
		assertEquals(1, country.getId());
		assertEquals("Brasil", country.getName());
		
	}
	
	@Test
	@DisplayName("Buscar por id inválido")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	void findByIdInvalidTest() {	
		assertNull(countryService.findById(4));
	}
	
	@Test
	@DisplayName("Buscar todos")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	void listAllTest() {	
		assertEquals(3, countryService.listAll().size());
	}
	
	@Test
	@DisplayName("Insert novo pais")
	void insertTest() {	
		Country pais = new Country(null, "PaisNovo");
		countryService.insert(pais);
		assertEquals(1, countryService.listAll().size());
		assertEquals(1, pais.getId());
		assertEquals("PaisNovo", pais.getName());	
	}
	
	@Test
	@DisplayName("Update país")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	void updateTest() {	
		Country pais = countryService.findById(1);
		assertNotNull(pais);
		assertEquals(1, pais.getId());
		assertEquals("Brasil", pais.getName());	
		pais = new Country(1, "BrasilNovo");
		countryService.update(pais);
		assertEquals(3, countryService.listAll().size());
		assertEquals(1, pais.getId());
		assertEquals("BrasilNovo", pais.getName());	
	}
	
	@Test
	@DisplayName("Delete país")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	void deleteTest() {	
		assertEquals(3, countryService.listAll().size());
		countryService.delete(1);
		assertEquals(2, countryService.listAll().size());
		assertNull(countryService.findById(1));
	}
	
	@Test
	@DisplayName("Delete país que não existe")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	void deleteIdNoExistTest() {	
		assertEquals(3, countryService.listAll().size());
		countryService.delete(10);
		assertEquals(3, countryService.listAll().size());
	}
	
	@Test
	@DisplayName("Procura por nome")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	void findByCountryTest() {	
		assertEquals(1, countryService.findByCountry("b").size());
		assertEquals(1, countryService.findByCountry("it").size());
		assertEquals(1, countryService.findByCountry("e").size());
	}

}
