package dev.khansergei.taskmanager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Task {
    private Long id;
    private String header;
    private String description;
    private LocalDateTime deadline;
    @JsonProperty("user_id")
    private Long userId;
    private State state;
}
