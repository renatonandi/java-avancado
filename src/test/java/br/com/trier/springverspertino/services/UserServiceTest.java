package br.com.trier.springverspertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springverspertino.BaseTest;
import br.com.trier.springverspertino.models.User;
import jakarta.transaction.Transactional;

@Transactional
public class UserServiceTest extends BaseTest{
    
    @Autowired
    UserService userService;
    
    @Test
    @DisplayName("Teste buscar por usuário por ID")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByIdTest() {
        var usuario = userService.findById(1);
        assertNotNull(usuario);
        assertEquals(1, usuario.getId());
        assertEquals("User 1", usuario.getName());
        assertEquals("email1@gmail.com", usuario.getEmail());
        assertEquals("123", usuario.getPassword());
    }
    
    @Test
    @DisplayName("Teste buscar usuário por ID inexistente")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByIdWrongTest() {
        var usuario = userService.findById(4);
        assertNull(usuario);
    }
    
    @Test
    @DisplayName("Teste buscar usuario por nome")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByNameTest() {
        var usuario = userService.findByName("use");
        assertNotNull(usuario);
        assertEquals(3, usuario.size());
        var usuario1 = userService.findByName("User 1");
        assertEquals(1, usuario1.size());
    }
    
    @Test
    @DisplayName("Teste buscar usuario por nome errado")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByNameWrongTest() {
        var usuario = userService.findByName("c");
        assertEquals(0, usuario.size());
    }
    
    @Test
    @DisplayName("Teste inserir usuario")
    void insertUserTes() {
        User usuario = new User(null, "nome", "Email", "senha");
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
        userService.delete(1);
        List<User> list = userService.listAll();
        assertEquals(2, list.size());
    }
    
    @Test
    @DisplayName("Teste apagar usuario por id incorreto")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void deleteByIdNonExistsTest() {
        userService.delete(10);
        List<User> list = userService.listAll();
        assertEquals(3, list.size());
    }
    
    @Test
    @DisplayName("Teste alterar usuario")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void updateByIdTest() {
        var usuario = userService.findById(1);
        assertEquals("User 1", usuario.getName());
        User usuarioAlter = new User(1, "nomeAlterado", "emailAlterado", "senhaAlterada");
        userService.update(usuarioAlter);
        usuario = userService.findById(1);
        assertEquals("nomeAlterado", usuario.getName());
    }
    

}
