package neuefische.vanessabocok.todoapp.models.chatGpt;

import java.util.List;

public record ChatGptResponse(
        List<ChatGptChoice> choices
) {
}