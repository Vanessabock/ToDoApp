package neuefische.vanessabocok.todoapp.models;

import lombok.With;

import java.time.LocalDateTime;

@With
public record ToDo(
        String id,
        String description,
        Status status,
        LocalDateTime lastModified
) {
}
