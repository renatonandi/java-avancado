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
import br.com.trier.springverspertino.models.Country;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class CountryServiceTest extends BaseTest{
	
	@Autowired
	CountryService countryService;
	
	@Test
    @DisplayName("Teste buscar país por id")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByIdTest() {
        var country = countryService.findById(1);
        assertNotNull(country);
        assertEquals(1, country.getId());
        assertEquals("Brasil", country.getName());
    }
    
    @Test
    @DisplayName("Teste buscar país por id inexistente")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByIdInvalidTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.findById(4));
        assertEquals("O país 4 não existe", exception.getMessage());
    }
    
	@Test
    @DisplayName("Teste de listagem de todos os registros")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void listAllTest() {
        var country = countryService.listAll();
        assertNotNull(country);
        assertEquals(3, country.size());
    }

    @Test
    @DisplayName("Teste de listagem de todos os registros sem nem um cadastrado")
    void listAllNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.listAll());
        assertEquals("Nem um país cadastrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar país por nome")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByNameTest() {
        var country = countryService.findByCountry("br");
        assertNotNull(country);
        assertEquals(1, country.size());
       
    }
    
    @Test
    @DisplayName("Teste buscar país por nome errado")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByNameInvalidTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.findByCountry("z"));
        assertEquals("Nem um país encontrado", exception.getMessage());
        
    }
    
    @Test
    @DisplayName("Teste inserir país")
    void insertCountryTest() {
        Country country = new Country(null, "PaisNovo");
        countryService.insert(country);
        country = countryService.findById(1);
        assertEquals(1, country.getId());
        assertEquals("PaisNovo", country.getName());
    }
    
    @Test
    @DisplayName("Teste apagar equipe por id")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void deleteByIdTest() {
        countryService.delete(1);
        List<Country> list = countryService.listAll();
        assertEquals(2, list.size());
    }
    
    @Test
    @DisplayName("Teste apagar país por id incorreto")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void deleteByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.delete(4));
        assertEquals("O país 4 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar país")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void updateByIdTest() {
        var country = countryService.findById(1);
        assertEquals("Brasil", country.getName());
        Country countryAlter = new Country(1, "nomeAlterado");
        countryService.update(countryAlter);
        countryAlter = countryService.findById(1);
        assertEquals("nomeAlterado", countryAlter.getName());
    }

    @Test
    @DisplayName("Teste alterar equipe inexistente")
    void updateByIdNonExistsTest() {
        Country countryAlter = new Country(1, "nomeAlterado");
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.update(countryAlter));
        assertEquals("O país 1 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste inserir país com nome duplicado")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void insertCountryWithDuplicateNameTest() {
        Country country = new Country(null, "Brasil");
        var exception = assertThrows(IntegrityViolation.class, () -> countryService.insert(country));
        assertEquals("Esse país já está cadastrado", exception.getMessage());
    }

}
