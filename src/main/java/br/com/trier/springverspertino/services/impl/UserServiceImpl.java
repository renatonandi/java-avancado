package br.com.trier.springverspertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springverspertino.models.User;
import br.com.trier.springverspertino.repositories.UserRepository;
import br.com.trier.springverspertino.services.UserService;
import br.com.trier.springverspertino.services.exceptions.IntegrityViolation;
import br.com.trier.springverspertino.services.exceptions.ObjectNotFound;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository repository;
    
    private void findByEmail(User user) {
        Optional<User> busca = repository.findByEmail(user.getEmail());
        if (busca.isPresent() && busca.get().getId() != user.getId()) {
            throw new IntegrityViolation("Esse email já está cadastrado");
        }
    }
    
    @Override
    public User findById(Integer id) {
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFound("O usuario %s não existe".formatted(id)));
    }

    @Override
    public User insert(User user) {
        findByEmail(user);
        return repository.save(user);
    }

    @Override
    public List<User> listAll() {
        List<User> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nenhum usuário cadastrado");
        }
        return list;
    }

    @Override
    public User update(User user) {
        findById(user.getId());
        findByEmail(user);
        return repository.save(user);
    }

    @Override
    public void delete(Integer id) {
        User user = findById(id);
        repository.delete(user); 
      
    }

    @Override
    public List<User> findByNameStarting(String name) {
        List<User> list = repository.findByNameStartingWithIgnoreCase(name);
        if (list.isEmpty()) {
            throw new ObjectNotFound("Nem um nome de usuário encontrado. Não existe nome iniciado com %s".formatted(name));
        }
        return list;
    }
    
}
