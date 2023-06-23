package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.springverspertino.BaseTest;
import br.com.trier.springverspertino.models.RacePilot;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/equipe.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/corrida.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto_corrida.sql")
public class RacePilotServiceTest extends BaseTest{
    
    @Autowired
    PilotService pilotService;
    
    @Autowired
    RaceService raceService;
    
    @Autowired
    RacePilotService service;
    
    @Test
    @DisplayName("Teste buscar por id")
    void findByIdTest() {
        RacePilot racePilot = service.findById(1);
        assertNotNull(racePilot);
        assertEquals(1, racePilot.getId());
        assertEquals(1, racePilot.getPlacing());
        assertEquals(1, racePilot.getPilot().getId());
        assertEquals(1, racePilot.getRace().getId());
    }

    @Test
    @DisplayName("Teste buscar por id inválido")
    void findIdInvalidTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(4));
        assertEquals("Piloto para a corrida 4 não existe", exception.getMessage());
    }

    @Test
    @DisplayName("Teste buscar todos")
    void findAllTest() {
        assertEquals(3, service.listAll().size());
    }

    @Test
    @DisplayName("Teste buscar todos com nenhum cadastro")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    void findAllWithNoRecordTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
        assertEquals("Nenhum piloto/corrida encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Teste inserir piloto_corrida")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    @Sql({"classpath:/resources/sqls/pista.sql"})
    @Sql({"classpath:/resources/sqls/corrida.sql"})
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void insertPilotTest() {
        RacePilot pilot = new RacePilot(null, pilotService.findById(1), raceService.findById(1), 1);
        service.insert(pilot);
        assertEquals(1, service.listAll().size());
        assertEquals(1, pilot.getId());
        assertEquals(1, pilot.getPlacing());
    }

    @Test
    @DisplayName("Teste alterar piloto_corrida")
    void updatePilotTest() {
        RacePilot pilot = service.findById(1);
        assertNotNull(pilot);
        assertEquals(1, pilot.getId());
        assertEquals(1, service.findById(1).getPlacing());
        pilot = new RacePilot(1, pilotService.findById(1), raceService.findById(1), 2);
        service.update(pilot);
        assertEquals(3, service.listAll().size());
        assertEquals(1, pilot.getId());
        assertEquals(2, service.findById(1).getPlacing());
    }

    @Test
    @DisplayName("Teste alterar piloto_corrida inexistente")
    void updatePilotNonExistsTest() {
        RacePilot pilot = new RacePilot(10, pilotService.findById(1), raceService.findById(1), 1);
        var exception = assertThrows(ObjectNotFound.class, () -> service.update(pilot));
        assertEquals("Piloto para a corrida 10 não existe", exception.getMessage());
    }

    @Test
    @DisplayName("Teste apagar piloto_corrida")
    void deletePilotTest() {
        RacePilot pilot = service.findById(1);
        assertNotNull(pilot);
        assertEquals(1, pilot.getId());
        assertEquals(1, pilot.getPlacing());
        service.delete(1);
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(1));
        assertEquals("Piloto para a corrida 1 não existe", exception.getMessage());
    }

    @Test
    @DisplayName("Teste apagar piloto inexistente")
    void deletePilotNonExistTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.delete(4));
        assertEquals("Piloto para a corrida 4 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar piloto_corrida por colocação")
    void findByPlacingTest() {
        List<RacePilot> list = service.findByPlacing(1);
        assertEquals(2, list.size());
    }
    
    @Test
    @DisplayName("Teste buscar piloto_corrida por colocação incorreta")
    void findByPlacingNonExistTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByPlacing(3));
        assertEquals("Nenhum piloto encontrado para a colocação 3", exception.getMessage());
    }

    @Test
    @DisplayName("Teste buscar piloto_corrida por piloto")
    void findByPilotTest() {
        List<RacePilot> list = service.findByPilotOrderByPlacing(pilotService.findById(1));
        assertEquals(1, list.size());
    }
    
    @Test
    @DisplayName("Teste buscar piloto_corrida por piloto sem nenhum registro com este piloto")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    @Sql({"classpath:/resources/sqls/pista.sql"})
    @Sql({"classpath:/resources/sqls/corrida.sql"})
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByPilotNonExistTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByPilotOrderByPlacing(pilotService.findById(1)));
        assertEquals("Nenhum piloto foi encontrado", exception.getMessage());
    }


    @Test
    @DisplayName("Teste buscar piloto_corrida por corrida")
    void findByRaceTest() {
        List<RacePilot> list = service.findByRaceOrderByPlacing(raceService.findById(1));
        assertEquals(1, list.size());
    }
    
    @Test
    @DisplayName("Teste buscar piloto_corrida por corrida sem corrida encontrados")
    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    @Sql({"classpath:/resources/sqls/pista.sql"})
    @Sql({"classpath:/resources/sqls/corrida.sql"})
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByRaceNonExistTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByRaceOrderByPlacing(raceService.findById(1)));
        assertEquals("Nenhum piloto foi encontrado para a corrida: 1", exception.getMessage());
    }

}
