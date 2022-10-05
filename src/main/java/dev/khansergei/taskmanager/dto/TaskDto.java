package dev.khansergei.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.khansergei.taskmanager.entity.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String header;
    private String description;
    private LocalDateTime deadline;
    @JsonProperty("user_id")
    private Long userId;
    private State state;

    public TaskDto(String header, String description, LocalDateTime deadline, Long userId) {
        this.header = header;
        this.description = description;
        this.deadline = deadline;
        this.userId = userId;
    }
}
