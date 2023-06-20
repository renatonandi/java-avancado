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
import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.resources.exceptions.StandardError;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVerspertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryResouceTest {

    @Autowired
    protected TestRestTemplate rest;
    
    private ResponseEntity<Country> getCountry(String url) {
        return rest.getForEntity(url, Country.class);
    }

    private ResponseEntity<List<Country>> getCountries(String url) {
        return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Country>>() {
        });
    }

    @Test
    @DisplayName("Listar todos")
    @Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    public void listAllTest() {
        ResponseEntity<List<Country>> response = getCountries("/pais");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(3, response.getBody().size());
    }
    
    @Test
    @DisplayName("Teste de lista sem nem um registro")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    public void testListAllEmptyTest() {
        ResponseEntity<StandardError> response = rest.getForEntity("/pais", StandardError.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }
    
    @Test
    @DisplayName("Buscar por id")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    public void testGetOk() {
        ResponseEntity<Country> response = getCountry("/pais/1");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Country country = response.getBody();
        assertEquals("Brasil", country.getName());
    }
    
    @Test
    @DisplayName("Buscar por id inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    public void testGetNotFound() {
        ResponseEntity<Country> response = getCountry("/pais/100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Cadastrar país")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    public void testCreateCountry() {
        Country country = new Country(null, "país");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Country> requestEntity = new HttpEntity<>(country, headers);
        ResponseEntity<Country> responseEntity = rest.exchange(
                "/pais", 
                HttpMethod.POST,  
                requestEntity,    
                Country.class   
        );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Country coun = responseEntity.getBody();
        assertEquals("país", coun.getName());
    }
    
    @Test
    @DisplayName("Alterar país")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    public void testUpdateCountry() {
        Country country = new Country(1, "paísNovo");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Country> requestEntity = new HttpEntity<>(country, headers);
        ResponseEntity<Country> responseEntity = rest.exchange(
                "/pais/1", 
                HttpMethod.PUT,  
                requestEntity,    
                Country.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Country coun = responseEntity.getBody();
        assertEquals("paísNovo", coun.getName());
    }
    
    @Test
    @DisplayName("Alterar país inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    public void testUpdateUserNonExistTest() {
        Country country = new Country(1, "paísNovo");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Country> requestEntity = new HttpEntity<>(country, headers);
        ResponseEntity<Country> responseEntity = rest.exchange(
                "/pais/100", 
                HttpMethod.PUT,  
                requestEntity,    
                Country.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }
    
    @Test
    @DisplayName("Deletar país")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    public void testDeleteUser() {
        ResponseEntity<Country> responseEntity = rest.exchange(
                "/pais/1", 
                HttpMethod.DELETE,  
                new HttpEntity<>(""),    
                Country.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
       
    }
    
    @Test
    @DisplayName("Deletar país inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    public void testDeleteNonExistsUser() {
        ResponseEntity<Country> responseEntity = rest.exchange(
                "/pais/10", 
                HttpMethod.DELETE,  
                new HttpEntity<>(""),    
                Country.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }
    
    @Test
    @DisplayName("Buscar país que o nome tenha")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    public void testFindByNameUser() {
        ResponseEntity<List<Country>> response = getCountries("/pais/name/br");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, response.getBody().size());
    }
    
    @Test
    @DisplayName("Buscar país que o nome não exista")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    public void testFindByNameNonExistUser() {
        ResponseEntity<StandardError> response = rest.getForEntity("/pais/name/z", StandardError.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }
}
