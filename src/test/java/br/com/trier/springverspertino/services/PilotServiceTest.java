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
import br.com.trier.springverspertino.models.Pilot;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class PilotServiceTest extends BaseTest{
    
    @Autowired
    PilotService service;
    
    @Autowired
    CountryService countryService;
    
    @Autowired
    TeamService teamService;
    
    @Test
    @DisplayName("Teste buscar piloto por ID")
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void findByIdTest() {
        var pilot = service.findById(1);
        assertNotNull(pilot);
        assertEquals(1, pilot.getId());
        assertEquals("Alonso", pilot.getName());
    }
    
    @Test
    @DisplayName("Teste buscar pista por id inexistente")
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(4));
        assertEquals("O piloto 4 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste de listagem de todos os registros")
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void listAllTest() {
        var pilot = service.listAll();
        assertNotNull(pilot);
        assertEquals(3, pilot.size());
    }

    @Test
    @DisplayName("Teste de listagem de todos os registros sem nem um cadastrado")
    void listAllNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
        assertEquals("Nenhum piloto cadastrado", exception.getMessage());
    }

    @Test
    @DisplayName("Teste inserir piloto")
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    void insertPistaTest() {
        Pilot pilot = new Pilot(null, "Piloto", countryService.findById(1), teamService.findById(1));
        service.insert(pilot);
        pilot = service.findById(1);
        assertEquals(1, pilot.getId());
        assertEquals("Piloto", pilot.getName());
        assertEquals("Brasil", pilot.getCountry().getName());
        assertEquals("Ferrari", pilot.getTeam().getName());
    }

    @Test
    @DisplayName("Teste remover piloto")
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void removePilotoTest() {
        service.delete(1);
        List<Pilot> list = service.listAll();
        assertEquals(2, list.size());
        assertEquals(2, list.get(0).getId());
    }

    @Test
    @DisplayName("Teste alterar piloto")
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void updateUsersTest() {
        var pilot = service.findById(1);
        assertEquals("Alonso", pilot.getName());
        var pilotNew = new Pilot(1, "PilotoAlterado", countryService.findById(1), teamService.findById(1));
        service.update(pilotNew);
        assertEquals("PilotoAlterado", service.findById(1).getName());
    }
    
    @Test
    @DisplayName("Teste buscar piloto por nome")
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void findByNameTest() {
        var pilot = service.findByNameContainingIgonreCase("al");
        assertNotNull(pilot);
        assertEquals(1, pilot.size());
        var pilot2 = service.findByNameContainingIgonreCase("air");
        assertEquals(1, pilot2.size());
    }

    @Test
    @DisplayName("Teste buscar piloto por nome errado")
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void findByNameWrongTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByNameContainingIgonreCase("z"));
        assertEquals("Nenhum piloto encontrado", exception.getMessage());
    }
  
    @Test
    @DisplayName("Teste buscar piloto por nome e pais")
    @Sql({ "classpath:/resources/sqls/pais.sql"})
    @Sql({ "classpath:/resources/sqls/equipe.sql"})
    @Sql({ "classpath:/resources/sqls/piloto.sql"})
    void findByNameAndCountryTest() {
        var pilot = service.findByNameContainingAndCountryOrderByName("Air", countryService.findById(3));
        assertNotNull(pilot);
        assertEquals(1, pilot.size());
       
    }

    @Test
    @DisplayName("Teste buscar piloto por nome errado e pais")
    @Sql({ "classpath:/resources/sqls/pais.sql"})
    @Sql({ "classpath:/resources/sqls/equipe.sql"})
    @Sql({ "classpath:/resources/sqls/piloto.sql"})
    void findByNameWrongAndCountryTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByNameContainingAndCountryOrderByName("z", countryService.findById(1)));
        assertEquals("Nenhum piloto encontrado para esse país: Brasil", exception.getMessage());
        
    }

    @Test
    @DisplayName("Teste buscar piloto por nome e pais inexistente")
    @Sql({ "classpath:/resources/sqls/pais.sql"})
    @Sql({ "classpath:/resources/sqls/equipe.sql"})
    @Sql({ "classpath:/resources/sqls/piloto.sql"})
    void findByNameAndCountryNonExistTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByNameContainingAndCountryOrderByName("Alo", countryService.findById(5)));
        assertEquals("O país 5 não existe", exception.getMessage());
    }

    @Test
    @DisplayName("Teste buscar piloto por equipe")
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void findByTeamTest() {
        var pilot = service.findByTeamOrderByName(teamService.findById(1));
        assertNotNull(pilot);
        assertEquals(2, pilot.size());
       
    }

    @Test
    @DisplayName("Teste buscar piloto por equipe inexistente")
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void findByTeamNonExistTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByTeamOrderByName(teamService.findById(5)));
        assertEquals("A equipe 5 não existe", exception.getMessage());
    }

    @Test
    @DisplayName("Teste buscar piloto por equipe que não tenha piloto cadastrado")
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    @Sql({ "classpath:/resources/sqls/equipe.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    void findByTeamWithoutPilotTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByTeamOrderByName(teamService.findById(2)));
        assertEquals("Nenhum piloto encontrado para essa equipe: BMW", exception.getMessage());
    }

}
