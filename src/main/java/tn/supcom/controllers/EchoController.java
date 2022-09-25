package tn.supcom.controllers;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.Database;
import org.jnosql.artemis.DatabaseType;
import org.jnosql.diana.api.document.DocumentCollectionManagerFactory;
import org.jnosql.diana.mongodb.document.MongoDBDocumentCollectionManager;
import tn.supcom.entities.Todo;
import tn.supcom.repositories.TodoRepository;

import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
public class EchoController {
    @Inject
    @Database(DatabaseType.DOCUMENT)
    private TodoRepository repository;

    public Todo add(Todo todo) {
        return repository.save(todo);
    }

    public Todo get(String id) {
        Optional<Todo> todo = repository.findById(id);
        return todo.get();
    }

    public List<Todo> getAll() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }


}
