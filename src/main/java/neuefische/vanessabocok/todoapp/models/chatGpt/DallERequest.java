package neuefische.vanessabocok.todoapp.models.chatGpt;

public record DallERequest(
        String model,
        String prompt,
        String size,
        String quality,
        int n

) {
}
