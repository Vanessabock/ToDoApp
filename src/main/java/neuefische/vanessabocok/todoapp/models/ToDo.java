package neuefische.vanessabocok.todoapp.models;

import lombok.With;

@With
public record ToDo(
        String id,
        String description,
        Status status,
        String lastModified
) {
}
