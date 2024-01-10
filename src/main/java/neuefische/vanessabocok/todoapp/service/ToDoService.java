package neuefische.vanessabocok.todoapp.service;

import lombok.RequiredArgsConstructor;
import neuefische.vanessabocok.todoapp.models.ToDo;
import neuefische.vanessabocok.todoapp.models.ToDoCreate;
import neuefische.vanessabocok.todoapp.repository.ToDoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public ToDo createToDo(ToDoCreate toDoCreate){
        ToDo toDo = new ToDo(
                UUID.randomUUID().toString(),
                toDoCreate.description(),
                toDoCreate.status(),
                LocalDateTime.now()
        );
        return toDoRepository.save(toDo);
    }

    public ToDo getToDoById(String id) {
        return toDoRepository.findToDoById(id);
    }

    public ToDo updateToDo(ToDo toDo) {
        ToDo oldToDo = toDoRepository.findToDoById(toDo.id());
        toDoRepository.delete(oldToDo);
        return toDoRepository.save(toDo.withLastModified(LocalDateTime.now()));
    }

    public ToDo deleteToDoById(String id) {
        ToDo toDoToDelete = toDoRepository.findToDoById(id);
        toDoRepository.delete(toDoToDelete);
        return toDoToDelete;
    }
}
