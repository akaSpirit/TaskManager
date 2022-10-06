package dev.khansergei.taskmanager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String header;
    private String description;
    private LocalDateTime deadline;
    @JsonProperty("user_email")
    private String userEmail;
    private State state;
}
