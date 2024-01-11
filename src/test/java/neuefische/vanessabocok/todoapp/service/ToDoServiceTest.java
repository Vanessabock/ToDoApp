package neuefische.vanessabocok.todoapp.service;

import neuefische.vanessabocok.todoapp.models.Status;
import neuefische.vanessabocok.todoapp.models.ToDo;
import neuefische.vanessabocok.todoapp.models.ToDoCreate;
import neuefische.vanessabocok.todoapp.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToDoServiceTest {

    ToDoRepository toDoRepository = Mockito.mock(ToDoRepository.class);
    ChatGptService chatGptService = Mockito.mock(ChatGptService.class);

    @Test
    public void getAllToDosTest(){
        //GIVEN
        Mockito.when(toDoRepository.findAll()).thenReturn(List.of(
                new ToDo("1", "Shopping", Status.OPEN, "123"),
                new ToDo("2", "Learning", Status.IN_PROGRESS, "456")
        ));

        ToDoService toDoService = new ToDoService(toDoRepository, chatGptService);

        //WHEN
        List<ToDo> actual = toDoService.getAllToDos();

        //THEN
        assertEquals(List.of(
                new ToDo("1", "Shopping", Status.OPEN, "123"),
                new ToDo("2", "Learning", Status.IN_PROGRESS, "456")
        ), actual);

        Mockito.verify(toDoRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(toDoRepository);
    }

    @Test
    public void createToDoTest(){
        //GIVEN
        Mockito.when(toDoRepository.save(Mockito.any())).thenReturn(
                new ToDo("1", "Shopping", Status.OPEN, "123"));

        ToDoService toDoService = new ToDoService(toDoRepository, chatGptService);

        //WHEN
        ToDo actual = toDoService.createToDo(new ToDoCreate("Learning", Status.DONE));

        //THEN
        assertEquals(new ToDo("1", "Shopping", Status.OPEN, "123"), actual);

        Mockito.verify(toDoRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verifyNoMoreInteractions(toDoRepository);
        Mockito.verify(chatGptService, Mockito.times(1)).chatGpt(Mockito.any());
        Mockito.verifyNoMoreInteractions(chatGptService);
    }

    @Test
    public void chatGPTGrammarCorrectionTest(){
        //GIVEN
        Mockito.when(chatGptService.chatGpt(Mockito.any())).thenReturn(
                "Shopping");

        ToDoService toDoService = new ToDoService(toDoRepository, chatGptService);

        //WHEN
        String actual = toDoService.chatGPTGrammarCorrection("Shoping");

        //THEN
        assertEquals("Shopping", actual);

        Mockito.verify(chatGptService, Mockito.times(1)).chatGpt(Mockito.any());
        Mockito.verifyNoMoreInteractions(chatGptService);
    }

    @Test
    public void getToDoByIdTest(){
        //GIVEN
        Mockito.when(toDoRepository.findToDoById(Mockito.any())).thenReturn(
                new ToDo("1", "Shopping", Status.OPEN, "123"));

        ToDoService toDoService = new ToDoService(toDoRepository, chatGptService);

        //WHEN
        ToDo actual = toDoService.getToDoById("1");

        //THEN
        assertEquals(new ToDo("1", "Shopping", Status.OPEN, "123"), actual);

        Mockito.verify(toDoRepository, Mockito.times(1)).findToDoById(Mockito.any());
        Mockito.verifyNoMoreInteractions(toDoRepository);
    }

    @Test
    public void updateToDoTest(){
        //GIVEN
        Mockito.when(toDoRepository.findToDoById(Mockito.any())).thenReturn(
                new ToDo("1", "Shopping", Status.OPEN, "123"));
        Mockito.when(toDoRepository.save(Mockito.any())).thenReturn(
                new ToDo("1", "Shopping", Status.IN_PROGRESS, "123"));

        ToDoService toDoService = new ToDoService(toDoRepository, chatGptService);

        //WHEN
        ToDo actual = toDoService.updateToDo(new ToDo("1", "Shopping", Status.OPEN, "123"));

        //THEN
        assertEquals(new ToDo("1", "Shopping", Status.IN_PROGRESS, "123"), actual);

        Mockito.verify(toDoRepository, Mockito.times(1)).findToDoById(Mockito.any());
        Mockito.verify(toDoRepository, Mockito.times(1)).delete(Mockito.any());
        Mockito.verify(toDoRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verifyNoMoreInteractions(toDoRepository);
    }

    @Test
    public void deleteToDoTest(){
        //GIVEN
        Mockito.when(toDoRepository.findToDoById(Mockito.any())).thenReturn(
                new ToDo("1", "Shopping", Status.OPEN, "123"));

        ToDoService toDoService = new ToDoService(toDoRepository, chatGptService);

        //WHEN
        ToDo actual = toDoService.deleteToDoById("1");

        //THEN
        assertEquals(new ToDo("1", "Shopping", Status.OPEN, "123"), actual);

        Mockito.verify(toDoRepository, Mockito.times(1)).findToDoById(Mockito.any());
        Mockito.verify(toDoRepository, Mockito.times(1)).delete(Mockito.any());
        Mockito.verifyNoMoreInteractions(toDoRepository);
    }

}