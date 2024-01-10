package neuefische.vanessabocok.todoapp.models;

import lombok.With;

@With
public record ToDoCreate(
        String description,
        Status status
) {
}
