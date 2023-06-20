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
import br.com.trier.springverspertino.models.Team;
import br.com.trier.springverspertino.resources.exceptions.StandardError;


@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVerspertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamResourceTest {
    
    
    @Autowired
    protected TestRestTemplate rest;
    
    private ResponseEntity<Team> getTeam(String url) {
        return rest.getForEntity(url, Team.class);
    }

    private ResponseEntity<List<Team>> getTeams(String url) {
        return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Team>>() {
        });
    }

    @Test
    @DisplayName("Listar todos")
    @Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    public void listAllTest() {
        ResponseEntity<List<Team>> response = getTeams("/equipe");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(3, response.getBody().size());
    }
    
    @Test
    @DisplayName("Teste de lista sem nem um registro")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    public void testListAllEmptyTest() {
        ResponseEntity<StandardError> response = rest.getForEntity("/equipe", StandardError.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }
    
    @Test
    @DisplayName("Buscar por id")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    public void testGetOk() {
        ResponseEntity<Team> response = getTeam("/equipe/1");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Team team = response.getBody();
        assertEquals("Ferrari", team.getName());
    }
    
    @Test
    @DisplayName("Buscar por id inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    public void testGetNotFound() {
        ResponseEntity<Team> response = getTeam("/equipe/100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Cadastrar equipe")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    public void testCreateCountry() {
        Team team = new Team(null, "equipe");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Team> requestEntity = new HttpEntity<>(team, headers);
        ResponseEntity<Team> responseEntity = rest.exchange(
                "/equipe", 
                HttpMethod.POST,  
                requestEntity,    
                Team.class   
        );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Team ret = responseEntity.getBody();
        assertEquals("equipe", ret.getName());
    }
    
    @Test
    @DisplayName("Alterar equipe")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    public void testUpdateCountry() {
        Team team = new Team(1, "equipeNova");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Team> requestEntity = new HttpEntity<>(team, headers);
        ResponseEntity<Team> responseEntity = rest.exchange(
                "/equipe/1", 
                HttpMethod.PUT,  
                requestEntity,    
                Team.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Team ret = responseEntity.getBody();
        assertEquals("equipeNova", ret.getName());
    }
    
    @Test
    @DisplayName("Alterar equipe inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    public void testUpdateUserNonExistTest() {
        Team team = new Team(1, "equipeNova");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Team> requestEntity = new HttpEntity<>(team, headers);
        ResponseEntity<Team> responseEntity = rest.exchange(
                "/equipe/100", 
                HttpMethod.PUT,  
                requestEntity,    
                Team.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }
    
    @Test
    @DisplayName("Deletar equipe")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    public void testDeleteUser() {
        ResponseEntity<Team> responseEntity = rest.exchange(
                "/equipe/1", 
                HttpMethod.DELETE,  
                new HttpEntity<>(""),    
                Team.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
       
    }
    
    @Test
    @DisplayName("Deletar equipe inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    public void testDeleteNonExistsUser() {
        ResponseEntity<Team> responseEntity = rest.exchange(
                "/equipe/10", 
                HttpMethod.DELETE,  
                new HttpEntity<>(""),    
                Team.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }
    
    @Test
    @DisplayName("Buscar equipe que o nome tenha")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    public void testFindByNameUser() {
        ResponseEntity<List<Team>> response = getTeams("/equipe/name/fe");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, response.getBody().size());
    }
    
    @Test
    @DisplayName("Buscar equipe que o nome não exista")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    public void testFindByNameNonExistUser() {
        ResponseEntity<StandardError> response = rest.getForEntity("/equipe/name/z", StandardError.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }

}
