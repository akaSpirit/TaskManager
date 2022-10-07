package dev.khansergei.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.khansergei.taskmanager.entity.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String header;
    private String description;
    private LocalDate deadline;
    @JsonProperty("user_email")
    private String userEmail;
    private String state;

    public TaskDto(String header, String description, LocalDate deadline, String userEmail, String state) {
        this.header = header;
        this.description = description;
        this.deadline = deadline;
        this.userEmail = userEmail;
    }
}
