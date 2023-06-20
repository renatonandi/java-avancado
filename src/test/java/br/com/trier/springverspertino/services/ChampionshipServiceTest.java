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
import br.com.trier.springverspertino.models.Championship;
import br.com.trier.springverspertino.models.User;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class ChampionshipServiceTest extends BaseTest{
	
	@Autowired
	ChampionshipService champService;
	
	@Test
    @DisplayName("Teste de listagem de todos os registros")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void listAllTest() {
        assertNotNull(champService.listAll());
        assertEquals(3, champService.listAll().size());
    }
	
	@Test
    @DisplayName("Teste de listagem de todos os registros sem nem um cadastrado")
    void listAllNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> champService.listAll());
        assertEquals("Nem um campeonato cadastrado", exception.getMessage());
    }
	
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
	    var exception = assertThrows(ObjectNotFound.class, () -> champService.findById(4));
        assertEquals("O campeonato 4 não existe", exception.getMessage());
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
	@DisplayName("Insert novo campeonato com ano nulo")
	void insertNullYearTest() {	
	    Championship champ = new Championship(null, "Campeonato", null);
	    var exception = assertThrows(IntegrityViolation.class, () -> champService.insert(champ));
        assertEquals("Ano inválido. O ano não pode ser nulo", exception.getMessage());
	}
	
	@Test
	@DisplayName("Update campeonato")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void updateTest() {	
		Championship champ = champService.findById(1);
		assertNotNull(champ);
		assertEquals(1, champ.getId());
		assertEquals("Formula 1", champ.getDescription());	
		champ = new Championship(1, "CampeonatoNovo", 2024);
		champService.update(champ);
		assertEquals(3, champService.listAll().size());
		assertEquals(1, champ.getId());
		assertEquals("CampeonatoNovo", champ.getDescription());	
		assertEquals(2024, champ.getYear());	
	}
	
	@Test
    @DisplayName("Teste apagar campeonato por id")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void deleteByIdTest() {
        champService.delete(1);
        List<Championship> list = champService.listAll();
        assertEquals(2, list.size());
    }
	
	@Test
	@DisplayName("Delete campeonato que não existe")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void deleteIdNoExistTest() {	
	    var exception = assertThrows(ObjectNotFound.class, () -> champService.delete(4));
        assertEquals("O campeonato 4 não existe", exception.getMessage());
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
	@DisplayName("Procura por ano incorreto")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByYearInvalidTest() {
	    var exception = assertThrows(IntegrityViolation.class, () -> champService.findByYear(1989));
        assertEquals("Ano inválido. O ano não pode ser menos que 1990 e mais que o próximo ano", exception.getMessage());
	}

	@Test
	@DisplayName("Procura por ano incorreto")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByYearNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> champService.findByYear(1995));
	    assertEquals("Nem um campeonato com esse ano cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Procura por ano e descrição")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByYearAndDescriptionTest() {
		var champ = champService.findByYearAndDescription(1990, 2005, "for");
		assertEquals(2, champ.size());
		champ = champService.findByYearAndDescription(1990, 1995, "For");
		assertEquals(1, champ.size());
		assertEquals("Formula 1", champ.get(0).getDescription());
	}
	
	@Test
	@DisplayName("Procura por ano e descrição errada")
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void findByYearAndDescriptionWrongTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> champService.findByYearAndDescription(1990, 2005, "z"));
        assertEquals("Nem um campeonato encontrado", exception.getMessage());
	}
	
	@Test
    @DisplayName("Teste alterar campeonato inexistente")
    void updateByIdNonExistsTest() {
        Championship champAlter = new Championship(1, "descriçãoAlterado", 2000);
        var exception = assertThrows(ObjectNotFound.class, () -> champService.update(champAlter));
        assertEquals("O campeonato 1 não existe", exception.getMessage());
        
    }
	
	@Test
    @DisplayName("Update campeonato passando ano inválido")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void updateInvalidYearTest() { 
        Championship champ = champService.findById(1);
        assertNotNull(champ);
        assertEquals(1, champ.getId());
        assertEquals("Formula 1", champ.getDescription());  
        var champAlter = new Championship(1, "CampeonatoNovo", 2030);
        var exception = assertThrows(IntegrityViolation.class, () -> champService.update(champAlter));
        assertEquals("Ano inválido. O ano não pode ser menos que 1990 e mais que o próximo ano", exception.getMessage());  
    }

}
