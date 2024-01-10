package neuefische.vanessabocok.todoapp.controller;

import lombok.RequiredArgsConstructor;
import neuefische.vanessabocok.todoapp.models.ToDo;
import neuefische.vanessabocok.todoapp.models.ToDoCreate;
import neuefische.vanessabocok.todoapp.service.ToDoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    //GET
    @GetMapping
    public List<ToDo> getAllToDos(){
        return toDoService.getAllToDos();
    }

    @GetMapping("/{id}")
    public ToDo getToDoById(@PathVariable String id){
        return toDoService.getToDoById(id);
    }

    //POST
    @PostMapping
    public ToDo post(@RequestBody ToDoCreate toDoCreate){
        return toDoService.createToDo(toDoCreate);
    }

    //PUT
    @PutMapping("/{id}")
    public ToDo updateToDo(@RequestBody ToDo toDo){
        return toDoService.updateToDo(toDo);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ToDo deleteToDo(@PathVariable String id){
        return toDoService.deleteToDoById(id);
    }

}
