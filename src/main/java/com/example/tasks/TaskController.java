package com.example.tasks;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    @GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) Boolean done) {
        if (done == null) {
            return new ArrayList<>(tasks.values());
        }

        return tasks.values().stream()
                .filter(task -> task.done() == done)
                .toList();
    }

    @GetMapping("/count")
    public TaskCountResponse getTaskCount() {
        return TaskCountResponse.from(tasks.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable long id) {
        Task task = tasks.get(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        Task updated = tasks.computeIfPresent(
                id,
                (taskId, oldTask) -> new Task(taskId, request.title(), request.done())
        );

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request) {
        long id = counter.incrementAndGet();

        Task newTask = new Task(
                id,
                request.title(),
                request.done()
        );

        tasks.put(id, newTask);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        tasks.remove(id);

        return ResponseEntity.noContent().build();
    }
}