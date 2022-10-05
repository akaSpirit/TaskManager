package dev.khansergei.taskmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum State {
    NEW("new") {
        @Override
        public State nextState() {
            return ACTIVE;
        }
    },
    ACTIVE("active") {
        @Override
        public State nextState() {
            return COMPLETE;
        }
    },
    COMPLETE("complete") {
        @Override
        public State nextState() {
            return this;
        }
    };

    private final String name;

    public abstract State nextState();
}
