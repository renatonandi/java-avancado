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
import br.com.trier.springverspertino.models.dto.UserDTO;
import br.com.trier.springverspertino.resources.exceptions.StandardError;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVerspertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {
    
    @Autowired
    protected TestRestTemplate rest;
    
    private ResponseEntity<UserDTO> getUser(String url) {
        return rest.getForEntity(url, UserDTO.class);
    }

    private ResponseEntity<List<UserDTO>> getUsers(String url) {
        return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>() {
        });
    }
    
    @Test
    @DisplayName("Listar todos")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void listAllTest() {
        ResponseEntity<List<UserDTO>> response = getUsers("/usuarios");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(3, response.getBody().size());
    }
    
    @Test
    @DisplayName("Teste de lista sem nem um registro")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    public void testListAllEmptyTest() {
        ResponseEntity<StandardError> response = rest.getForEntity("/usuarios", StandardError.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Buscar por id")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testGetOk() {
        ResponseEntity<UserDTO> response = getUser("/usuarios/1");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        UserDTO user = response.getBody();
        assertEquals("User 1", user.getName());
    }

    @Test
    @DisplayName("Buscar por id inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testGetNotFound() {
        ResponseEntity<UserDTO> response = getUser("/usuarios/100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Cadastrar usuário")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    public void testCreateUser() {
        UserDTO dto = new UserDTO(null, "nome", "email", "senha");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<UserDTO> responseEntity = rest.exchange(
                "/usuarios", 
                HttpMethod.POST,  
                requestEntity,    
                UserDTO.class   
        );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        UserDTO user = responseEntity.getBody();
        assertEquals("nome", user.getName());
    }
    
    @Test
    @DisplayName("Cadastrar usuário com email existente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testCreateUserEmailExists() {
        UserDTO dto = new UserDTO(null, "nome", "email1@gmail.com", "senha");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<UserDTO> responseEntity = rest.exchange(
                "/usuarios", 
                HttpMethod.POST,  
                requestEntity,    
                UserDTO.class   
        );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Alterar usuário")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testUpdateUser() {
        UserDTO dto = new UserDTO(1, "nomeNovo", "emailNovo", "senhaNova");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<UserDTO> responseEntity = rest.exchange(
                "/usuarios/1", 
                HttpMethod.PUT,  
                requestEntity,    
                UserDTO.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        UserDTO user = responseEntity.getBody();
        assertEquals("nomeNovo", user.getName());
    }

    @Test
    @DisplayName("Alterar usuário com email existente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testUpdateUserEmailExist() {
    	UserDTO dto = new UserDTO(1, "nomeNovo", "email2@gmail.com", "senhaNova");
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
    	ResponseEntity<UserDTO> responseEntity = rest.exchange(
    			"/usuarios/1", 
    			HttpMethod.PUT,  
    			requestEntity,    
    			UserDTO.class   
    			);
    	assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
    
    @Test
    @DisplayName("Alterar usuário inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testUpdateUserNonExistTest() {
        UserDTO dto = new UserDTO(1, "nomeNovo", "emailNovo", "senhaNova");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<UserDTO> responseEntity = rest.exchange(
                "/usuarios/100", 
                HttpMethod.PUT,  
                requestEntity,    
                UserDTO.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Deletar usuário")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testDeleteUser() {
        ResponseEntity<UserDTO> responseEntity = rest.exchange(
                "/usuarios/1", 
                HttpMethod.DELETE,  
                new HttpEntity<>(""),    
                UserDTO.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
    @DisplayName("Deletar usuário inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testDeleteNonExistsUser() {
        ResponseEntity<UserDTO> responseEntity = rest.exchange(
                "/usuarios/10", 
                HttpMethod.DELETE,  
                new HttpEntity<>(""),    
                UserDTO.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Buscar usuário que o nome comece com")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testFindByNameUser() {
        ResponseEntity<List<UserDTO>> response = getUsers("/usuarios/name/us");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(3, response.getBody().size());
    }

    @Test
    @DisplayName("Buscar usuário que o nome não exista")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    public void testFindByNameNonExistUser() {
        ResponseEntity<StandardError> response = rest.getForEntity("/usuarios/name/z", StandardError.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
