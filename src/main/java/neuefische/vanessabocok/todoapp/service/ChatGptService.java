package neuefische.vanessabocok.todoapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import neuefische.vanessabocok.todoapp.models.chatGpt.*;

import java.util.List;


@Service

public class ChatGptService {

    private final RestClient restClient;


    public ChatGptService(@Value("${app.chatGpt.api.url}") String url,
                          @Value("${app.chatGpt.api.key}") String apiKey,
                          @Value("${app.chatGpt.api.org}") String org) {

        restClient = RestClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("OpenAI-Organization", org)
                .build();
    }

    public String chatGpt(String message) {

        ChatGptRequest requestBody = new ChatGptRequest(List.of(new ChatGptMessage("user", message)), "gpt-3.5-turbo");

        ChatGptResponse response = restClient.post()
                .uri("/completions")
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ChatGptResponse.class);

        assert response != null;
        return response.choices().get(0).message().content();
    }

}