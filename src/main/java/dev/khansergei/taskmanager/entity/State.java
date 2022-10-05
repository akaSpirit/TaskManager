package dev.khansergei.taskmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum State {
    NEW("new"),
    IN_WORK("in work"),
    COMPLETE("complete");

    private final String name;
}
