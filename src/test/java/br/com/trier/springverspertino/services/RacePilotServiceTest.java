package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("Buscar por id")
    void findById() {
        RacePilot racePilot = service.findById(1);
        assertNotNull(racePilot);
        assertEquals(1, racePilot.getId());
        assertEquals(1, racePilot.getPlacing());
        assertEquals(1, racePilot.getPilot().getId());
        assertEquals(1, racePilot.getRace().getId());
    }

    @Test
    @DisplayName("Buscar por id inválido")
    void findIdInvalid() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(4));
        assertEquals("O relacionamento piloto/corrida 4 não existe", exception.getMessage());
    }
//
//    @Test
//    @DisplayName("Buscar todos")
//    void findAll() {
//        assertEquals(3, service.listAll().size());
//    }
//
//    @Test
//    @DisplayName("Buscar todos com nenhum cadastro")
//    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
//    void findAllWithNoPilotRace() {
//        var ex = assertThrows(ObjectNotFound.class, () -> service.listAll());
//        assertEquals("Nenhum piloto/corrida cadastrado", ex.getMessage());
//    }
//
//    @Test
//    @DisplayName("Insert novo piloto/corrida")
//    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
//    @Sql({"classpath:/resources/sqls/pais.sql"})
//    @Sql({"classpath:/resources/sqls/time.sql"})
//    @Sql({"classpath:/resources/sqls/piloto.sql"})
//    @Sql({"classpath:/resources/sqls/pista.sql"})
//    @Sql({"classpath:/resources/sqls/campeonato.sql"})
//    @Sql({"classpath:/resources/sqls/corrida.sql"})
//    void insert() {
//        RacePilot pilot = new RacePilot(null, pilotService.findById(1), raceService.findById(1), 1);
//        service.insert(pilot);
//        assertEquals(1, service.listAll().size());
//        assertEquals(1, pilot.getId());
//        assertEquals(1, pilot.getPlacing());
//    }
//
//
//    @Test
//    @DisplayName("Update piloto_corrida")
//    void update() {
//        RacePilot pilot = service.findById(1);
//        assertNotNull(pilot);
//        assertEquals(1, pilot.getId());
//        assertEquals(1, service.findById(1).getPlacing());
//        pilot = new RacePilot(1, pilotService.findById(1), raceService.findById(1), 2);
//        service.update(pilot);
//        assertEquals(3, service.listAll().size());
//        assertEquals(1, pilot.getId());
//        assertEquals(2, service.findById(1).getPlacing());
//    }
//
//    @Test
//    @DisplayName("Update piloto/corrida não existente")
//    void updateNonExists() {
//        RacePilot pilot = new RacePilot(10, pilotService.findById(1), raceService.findById(1), 1);
//        var exception = assertThrows(ObjectNotFound.class, () -> service.update(pilot));
//        assertEquals("O relacionamento piloto/corrida 10 não existe", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Delete piloto/corrida")
//    void delete() {
//        RacePilot pilot = service.findById(1);
//        assertNotNull(pilot);
//        assertEquals(1, pilot.getId());
//        assertEquals(1, pilot.getPlacing());
//        service.delete(1);
//        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(1));
//        assertEquals("O relacionamento piloto/corrida 1 não existe", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Delete piloto não existente")
//    void deleteNonExists() {
//        var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
//        assertEquals("O relacionamento piloto/corrida 10 não existe", exception.getMessage());
//    }
//    
//    @Test
//    @DisplayName("Encontra piloto/corrida por colocação")
//    void findByPlacement() {
//        List<RacePilot> list = service.findByPlacing(1);
//        assertEquals(2, list.size());
//    }
//    
//    @Test
//    @DisplayName("Encontra piloto/corrida por colocação sem colocação igual")
//    void findByPlacementNonExist() {
//        var exception = assertThrows(ObjectNotFound.class, () -> service.findByPlacing(3));
//        assertEquals("Nenhum piloto cadastrado com colocação: 3", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Encontra piloto/corrida por piloto")
//    void findByPilot() {
//        List<RacePilot> list = service.findByPilotOrderByPlacing(pilotService.findById(1));
//        assertEquals(2, list.size());
//    }
//    
//    @Test
//    @DisplayName("Encontra piloto/corrida por piloto sem nenhum com este piloto")
//    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
//    @Sql({"classpath:/resources/sqls/pais.sql"})
//    @Sql({"classpath:/resources/sqls/time.sql"})
//    @Sql({"classpath:/resources/sqls/piloto.sql"})
//    @Sql({"classpath:/resources/sqls/campeonato.sql"})
//    @Sql({"classpath:/resources/sqls/pista.sql"})
//    @Sql({"classpath:/resources/sqls/corrida.sql"})
//    void findByPilotNonExist() {
//        var exception = assertThrows(ObjectNotFound.class, () -> service.findByPilotOrderByPlacing(pilotService.findById(1)));
//        assertEquals("Piloto não cadastrado : %s".formatted(pilotService.findById(1).getName()), exception.getMessage());
//    }
//
//
//    @Test
//    @DisplayName("Encontra piloto/corrida por corrida")
//    void findByRace() {
//        List<RacePilot> list = service.findByRaceOrderByPlacing(raceService.findById(1));
//        assertEquals(2, list.size());
//    }
//    
//    @Test
//    @DisplayName("Encontra piloto/corrida por corrida sem corrida encontrados")
//    @Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
//    @Sql({"classpath:/resources/sqls/pais.sql"})
//    @Sql({"classpath:/resources/sqls/time.sql"})
//    @Sql({"classpath:/resources/sqls/piloto.sql"})
//    @Sql({"classpath:/resources/sqls/campeonato.sql"})
//    @Sql({"classpath:/resources/sqls/pista.sql"})
//    @Sql({"classpath:/resources/sqls/corrida.sql"})
//    void findByRaceNonExist() {
//        var exception = assertThrows(ObjectNotFound.class, () -> service.findByRaceOrderByPlacing(raceService.findById(1)));
//        assertEquals("Nenhum piloto associado a corrida de id: 1", exception.getMessage());
//    }

}
