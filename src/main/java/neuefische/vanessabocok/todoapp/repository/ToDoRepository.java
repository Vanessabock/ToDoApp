package neuefische.vanessabocok.todoapp.repository;

import neuefische.vanessabocok.todoapp.models.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends MongoRepository<ToDo, String> {
    ToDo findToDoById(String id);
}
