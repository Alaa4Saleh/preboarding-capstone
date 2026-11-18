package com.example.tasks;

import java.util.Collection;

public record TaskCountResponse(
        long total,
        long done
) {
    public static TaskCountResponse from(Collection<Task> tasks) {
        long total = tasks.size();
        long done = tasks.stream()
                .filter(Task::done)
                .count();
        return new TaskCountResponse(total, done);
    }
}
