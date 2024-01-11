package neuefische.vanessabocok.todoapp.controller;

import neuefische.vanessabocok.todoapp.models.Status;
import neuefische.vanessabocok.todoapp.models.ToDo;
import neuefische.vanessabocok.todoapp.repository.ToDoRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ToDoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ToDoRepository toDoRepository;

    private static MockWebServer mockWebServer;

    static {
        System.setProperty("API_KEY", "someKey");
        System.setProperty("ORG_KEY", "someOrg");
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("app.chatGpt.api.url", () -> mockWebServer.url("/").toString());
    }

    @DirtiesContext
    @Test
    void getAllToDos_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        //GIVEN
        toDoRepository.save(new ToDo("1", "Shopping", Status.OPEN, "123"));

        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/todo"))

                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                [{
                                     "id": "1",
                                     "description": "Shopping",
                                     "status": "OPEN",
                                     "lastModified": "123"
                                 }]
                                """))
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 200);
    }

    @DirtiesContext
    @Test
    void getToDoById_shouldReturnOneObject_whenThisObjectWasSavedInRepository() throws Exception {
        //GIVEN
        toDoRepository.save(new ToDo("1", "Shopping", Status.OPEN, "123"));

        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/todo/1"))

                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "id": "1",
                                     "description": "Shopping",
                                     "status": "OPEN",
                                     "lastModified": "123"
                                 }
                                """))
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 200);
    }

    @DirtiesContext
    @Test
    void createToDo_shouldReturnOneObject_whenThisObjectWasSavedInRepository() throws Exception {
        //GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                            {
                               "id": "chatcmpl-123",
                               "object": "chat.completion",
                               "created": 1677652288,
                               "model": "gpt-3.5-turbo-0613",
                               "system_fingerprint": "fp_44709d6fcb",
                               "choices": [{
                                 "index": 0,
                                 "message": {
                                   "role": "assistant",
                                   "content": "Shopping"
                                 },
                                 "logprobs": null,
                                 "finish_reason": "stop"
                               }],
                               "usage": {
                                 "prompt_tokens": 9,
                                 "completion_tokens": 12,
                                 "total_tokens": 21
                               }
                             }
                        """)
                .addHeader("Content-Type", "application/json"));

        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                   "description": "Shoping",
                                   "status": "OPEN"
                                }
                                """))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "description": "Shopping",
                                     "status": "OPEN"
                                 }
                                """))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.lastModified").isNotEmpty())
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 200);
    }

    @DirtiesContext
    @Test
    void updateToDo_shouldReturnOneObject_whenThisObjectWasUpdatedInRepository() throws Exception {
        //GIVEN
        toDoRepository.save(new ToDo("1", "Shopping", Status.OPEN, "456"));

        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/todo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                   "id": "1",
                                   "description": "Shopping",
                                   "status": "IN_PROGRESS",
                                   "lastModified": "123"
                                }
                                """))

                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "id": "1",
                                     "description": "Shopping",
                                     "status": "IN_PROGRESS"
                                 }
                                """))
                .andExpect(jsonPath("$.lastModified").isNotEmpty())
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 200);
    }

    @DirtiesContext
    @Test
    void deleteToDo_shouldReturnNoObject_whenThisObjectWasDeletedFromRepository() throws Exception {
        //GIVEN
        toDoRepository.save(new ToDo("1", "Shopping", Status.OPEN, "123"));

        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/todo/1"))

                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "id": "1",
                                     "description": "Shopping",
                                     "status": "OPEN",
                                     "lastModified": "123"
                                 }
                                """))
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 200);
    }
}
