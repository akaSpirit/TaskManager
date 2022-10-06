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
    @JsonProperty("user_email")
    private String userEmail;
    private State state;

    public TaskDto(String header, String description, LocalDateTime deadline, String userEmail) {
        this.header = header;
        this.description = description;
        this.deadline = deadline;
        this.userEmail = userEmail;
    }
}
