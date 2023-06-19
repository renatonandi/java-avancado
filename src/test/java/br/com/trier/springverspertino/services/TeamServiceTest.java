package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springverspertino.BaseTest;
import br.com.trier.springverspertino.models.Team;
import br.com.trier.springverspertino.models.User;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class TeamServiceTest extends BaseTest{
	
	@Autowired
	TeamService teamService;
	
	@Test
    @DisplayName("Teste de listagem de todos os registros")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void listAllTest() {
        var team = teamService.listAll();
        assertNotNull(team);
        assertEquals(3, team.size());
    }

    @Test
    @DisplayName("Teste de listagem de todos os registros sem nem um cadastrado")
    void listAllNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.listAll());
        assertEquals("Nem uma equipe cadastrada", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar por equipe por ID")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByIdTest() {
        var team = teamService.findById(1);
        assertNotNull(team);
        assertEquals(1, team.getId());
        assertEquals("Ferrari", team.getName());
    }
    
    @Test
    @DisplayName("Teste buscar equipe por ID inexistente")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByIdInvalidTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.findById(4));
        assertEquals("A equipe 4 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar equipe por nome")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByNameTest() {
        var team = teamService.findByTeam("fe");
        assertNotNull(team);
        assertEquals(1, team.size());
       
    }
    
    @Test
    @DisplayName("Teste buscar equipe por nome errado")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByNameInvalidTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.findByTeam("z"));
        assertEquals("Nem uma equipe encontrada", exception.getMessage());
        
    }
    
    @Test
    @DisplayName("Teste inserir equipe")
    void insertUserTest() {
        Team team = new Team(null, "equipe");
        teamService.insert(team);
        team = teamService.findById(1);
        assertEquals(1, team.getId());
        assertEquals("equipe", team.getName());
    }
    
    @Test
    @DisplayName("Teste apagar equipe por id")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void deleteByIdTest() {
        teamService.delete(1);
        List<Team> list = teamService.listAll();
        assertEquals(2, list.size());
    }
    
    @Test
    @DisplayName("Teste apagar equipe por id incorreto")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void deleteByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.delete(4));
        assertEquals("A equipe 4 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar equipe")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void updateByIdTest() {
        var team = teamService.findById(1);
        assertEquals("Ferrari", team.getName());
        Team teamAlter = new Team(1, "nomeAlterado");
        teamService.update(teamAlter);
        teamAlter = teamService.findById(1);
        assertEquals("nomeAlterado", teamAlter.getName());
    }

    @Test
    @DisplayName("Teste alterar equipe inexistente")
    void updateByIdNonExistsTest() {
        
        Team teamAlter = new Team(1, "nomeAlterado");
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.update(teamAlter));
        assertEquals("A equipe 1 não existe", exception.getMessage());
        
    }
    
    @Test
    @DisplayName("Teste inserir equipe com nome duplicado")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void insertUserEmailExistTest() {
        Team team = new Team(null, "Ferrari");
        var exception = assertThrows(IntegrityViolation.class, () -> teamService.insert(team));
        assertEquals("Essa equipe já está cadastrada", exception.getMessage());
        
       
    }

}
