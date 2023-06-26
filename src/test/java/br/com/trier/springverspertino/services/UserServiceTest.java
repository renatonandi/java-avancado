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
import br.com.trier.springverspertino.models.User;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class UserServiceTest extends BaseTest{
    
    @Autowired
    UserService userService;
    
    @Test
    @DisplayName("Teste de listagem de todos os registros")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void listAllTest() {
        var usuario = userService.listAll();
        assertNotNull(usuario);
        assertEquals(3, usuario.size());
    }

    @Test
    @DisplayName("Teste de listagem de todos os registros sem nem um cadastrado")
    void listAllNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> userService.listAll());
        assertEquals("Nenhum usuário cadastrado", exception.getMessage());
    }
    
    
    
    @Test
    @DisplayName("Teste buscar por usuário por ID")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByIdTest() {
        var usuario = userService.findById(4);
        assertNotNull(usuario);
        assertEquals(4, usuario.getId());
        assertEquals("User 1", usuario.getName());
        assertEquals("email1@gmail.com", usuario.getEmail());
        assertEquals("123", usuario.getPassword());
    }
    
    @Test
    @DisplayName("Teste buscar usuário por ID inexistente")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByIdInvalidTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> userService.findById(10));
        assertEquals("O usuario 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar usuario por nome")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByNameTest() {
        var usuario = userService.findByNameStarting("use");
        assertNotNull(usuario);
        assertEquals(3, usuario.size());
        var usuario1 = userService.findByNameStarting("User 1");
        assertEquals(1, usuario1.size());
    }
    
    @Test
    @DisplayName("Teste buscar usuario por nome errado")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByNameInvalidTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> userService.findByNameStarting("c"));
        assertEquals("Nem um nome de usuário encontrado. Não existe nome iniciado com c", exception.getMessage());
        
    }
    
    @Test
    @DisplayName("Teste inserir usuario")
    void insertUserTest() {
        User usuario = new User(null, "nome", "Email", "senha", "ADMIN,USER");
        userService.insert(usuario);
        usuario = userService.findById(1);
        assertEquals(1, usuario.getId());
        assertEquals("nome", usuario.getName());
        assertEquals("Email", usuario.getEmail());
        assertEquals("senha", usuario.getPassword()); 
    }
    
    @Test
    @DisplayName("Teste apagar usuario por id")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void deleteByIdTest() {
        userService.delete(4);
        List<User> list = userService.listAll();
        assertEquals(2, list.size());
    }
    
    @Test
    @DisplayName("Teste apagar usuario por id incorreto")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void deleteByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> userService.delete(10));
        assertEquals("O usuario 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar usuario")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void updateByIdTest() {
        var usuario = userService.findById(4);
        assertEquals("User 1", usuario.getName());
        User usuarioAlter = new User(4, "nomeAlterado", "emailAlterado", "senhaAlterada", "ADMIN,USER");
        userService.update(usuarioAlter);
        usuario = userService.findById(4);
        assertEquals("nomeAlterado", usuario.getName());
    }

    @Test
    @DisplayName("Teste alterar usuario inexistente")
    void updateByIdNonExistsTest() {
        
        User usuarioAlter = new User(1, "nomeAlterado", "emailAlterado", "senhaAlterada", "ADMIN");
        var exception = assertThrows(ObjectNotFound.class, () -> userService.update(usuarioAlter));
        assertEquals("O usuario 1 não existe", exception.getMessage());
        
    }
    
    @Test
    @DisplayName("Teste inserir usuario com email duplicado")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void insertUserEmailExistTest() {
        User usuario = new User(null, "User 5", "email1@gmail.com", "novo", "USER");
        var exception = assertThrows(IntegrityViolation.class, () -> userService.insert(usuario));
        assertEquals("Esse email já está cadastrado", exception.getMessage());
    }
    

}
