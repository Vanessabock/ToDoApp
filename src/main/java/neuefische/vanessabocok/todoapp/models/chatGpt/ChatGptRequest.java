package neuefische.vanessabocok.todoapp.models.chatGpt;

import java.util.List;

public record ChatGptRequest(
        List<ChatGptMessage> messages,
        String model
) {
}