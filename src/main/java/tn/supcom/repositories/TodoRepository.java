package tn.supcom.repositories;
import org.jnosql.artemis.Repository;
import tn.supcom.entities.Todo;

import java.util.List;

public interface TodoRepository extends Repository<Todo, String> {
    List<Todo> findByName(String name);
    List<Todo> findAll();
}
