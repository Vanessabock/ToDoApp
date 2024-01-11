package neuefische.vanessabocok.todoapp.models.chatGpt;

import java.util.List;

public record DallEResponse(
        int created,
        List<Data> data
) {
}
