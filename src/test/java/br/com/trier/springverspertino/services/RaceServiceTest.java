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
import br.com.trier.springverspertino.models.Race;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class RaceServiceTest extends BaseTest{
    
    @Autowired
    RaceService service;
    
    @Autowired
    SpeedwayService speedwayService;
    
    @Autowired
    ChampionshipService championshipService;
    
    @Test
    @DisplayName("Teste buscar piloto por ID")
    @Sql({ "classpath:/resources/sqls/campeonato.sql" })
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    @Sql({ "classpath:/resources/sqls/corrida.sql" })
    void findByIdTest() {
        var race = service.findById(1);
        assertNotNull(race);
        assertEquals(1, race.getId());
        assertEquals("Formula 1", race.getChampionship().getDescription());
    }
    
    @Test
    @DisplayName("Teste buscar pista por id inexistente")
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
        var corrida = service.listAll();
        assertNotNull(corrida);
        assertEquals(3, corrida.size());
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
        Race race = new Race(null, "2000-01-01 10:00:00",championshipService.findById(1), speedwayService.findById(1));
        service.insert(race);
        race = service.findById(1);
        assertEquals(1, race.getId());
        assertEquals("Formula 1", race.getChampionship().getDescription());
     
    }

    @Test
    @DisplayName("Teste remover corrida")
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
    @DisplayName("Teste alterar corrida")
    @Sql({ "classpath:/resources/sqls/campeonato.sql" })
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    @Sql({ "classpath:/resources/sqls/corrida.sql" })
    void updateUsersTest() {
        var race = service.findById(1);
        assertEquals("Formula 1", race.getChampionship().getDescription());
        var raceNew = new Race(1, championshipService.findById(2), speedwayService.findById(1));
        service.update(raceNew);
        assertEquals("Formula Ind", service.findById(1).getChampionship().getDescription());
    }

}
