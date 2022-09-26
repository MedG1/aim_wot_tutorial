package tn.supcom.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.jnosql.artemis.Database;
import org.jnosql.artemis.DatabaseType;
import org.jnosql.diana.mongodb.document.MongoDBDocumentCollectionManager;
import tn.supcom.controllers.repositories.Repository;
import tn.supcom.entities.Todo;
import tn.supcom.controllers.repositories.TodoRepository;

import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
@Repository
public class EchoController {
    @Inject
    private MongoDBDocumentCollectionManager manager;


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
