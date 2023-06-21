package br.com.trier.springverspertino.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springverspertino.SpringVerspertinoApplication;
import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.resources.exceptions.StandardError;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVerspertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChampionshipResourceTest {

	@Autowired
	protected TestRestTemplate rest;

	private ResponseEntity<Championship> getChamp(String url) {
		return rest.getForEntity(url, Championship.class);
	}

	private ResponseEntity<List<Championship>> getChamps(String url) {
		return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Championship>>() {
		});
	}

	@Test
	@DisplayName("Listar todos")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void listAllTest() {
		ResponseEntity<List<Championship>> response = getChamps("/campeonato");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(3, response.getBody().size());
	}

	@Test
	@DisplayName("Teste de lista sem nem um registro")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	public void testListAllEmptyTest() {
		ResponseEntity<StandardError> response = rest.getForEntity("/campeonato", StandardError.class);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

	}

	@Test
	@DisplayName("Buscar por id")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testGetOk() {
		ResponseEntity<Championship> response = getChamp("/campeonato/1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Championship champ = response.getBody();
		assertEquals("Formula 1", champ.getDescription());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testGetNotFound() {
		ResponseEntity<Championship> response = getChamp("/campeonato/100");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	@DisplayName("Cadastrar campeonato")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	public void testCreateCountry() {
		Championship champ = new Championship(null, "campeonato",2015);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Championship> requestEntity = new HttpEntity<>(champ, headers);
		ResponseEntity<Championship> responseEntity = rest.exchange("/campeonato", HttpMethod.POST, requestEntity, Championship.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		Championship cham = responseEntity.getBody();
		assertEquals("campeonato", cham.getDescription());
	}

	@Test
	@DisplayName("Alterar campeonato")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testUpdateCountry() {
		Championship champ = new Championship(1, "campeonatoNovo",2015);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Championship> requestEntity = new HttpEntity<>(champ, headers);
		ResponseEntity<Championship> responseEntity = rest.exchange("/campeonato/1", HttpMethod.PUT, requestEntity, Championship.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		Championship cham = responseEntity.getBody();
		assertEquals("campeonatoNovo", cham.getDescription());
	}

	@Test
	@DisplayName("Alterar campeonato inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testUpdateUserNonExistTest() {
		Championship champ = new Championship(1, "campeonatoNovo",2015);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Championship> requestEntity = new HttpEntity<>(champ, headers);
		ResponseEntity<Championship> responseEntity = rest.exchange("/campeonato/100", HttpMethod.PUT, requestEntity,
				Championship.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

	}

	@Test
	@DisplayName("Deletar campeonato")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testDeleteUser() {
		ResponseEntity<Championship> responseEntity = rest.exchange("/campeonato/1", HttpMethod.DELETE, new HttpEntity<>(""),
				Championship.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}

	@Test
	@DisplayName("Deletar campeonato inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testDeleteNonExistsUser() {
		ResponseEntity<Championship> responseEntity = rest.exchange("/campeonato/10", HttpMethod.DELETE, new HttpEntity<>(""),
				Championship.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

	}
	
	@Test
	@DisplayName("Buscar por ano e descrição")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testFindByYearAndDescription() {
		ResponseEntity<List<Championship>> response = getChamps("/campeonato/ano/1990/2010/for");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(3, response.getBody().size());
	}
	
	@Test
	@DisplayName("Buscar por ano e descrição errada")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testFindByYearAndDescriptionWrong() {
		ResponseEntity<StandardError> response = rest.getForEntity("/campeonato/ano/1990/2010/z", StandardError.class);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Buscar por ano")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	public void testFindByYear() {
		ResponseEntity<List<Championship>> response = getChamps("/campeonato/ano/2010");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(1, response.getBody().size());
	}

}
