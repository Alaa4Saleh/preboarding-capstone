
package com.example.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TaskController())
                .build();
    }

    @Test
    void getAllTasksReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createTaskReturns201() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Learn Java",
                                    "done": false
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Java"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void getTaskByIdReturns200() throws Exception {
        createSampleTask();

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Java"));
    }

    @Test
    void getMissingTaskReturns404() throws Exception {
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTaskWithBlankTitleReturns400() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "   ",
                                    "done": false
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTasksReturnsCreatedTask() throws Exception {
        createSampleTask();

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Learn Java"));
    }

    @Test
    void deleteTaskReturns204() throws Exception {
        createSampleTask();

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTaskReturns200AndPreservesId() throws Exception {
        createSampleTask();

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Learn Spring Boot",
                                    "done": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
                .andExpect(jsonPath("$.done").value(true));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void updateMissingTaskReturns404() throws Exception {
        mockMvc.perform(put("/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Learn Spring Boot",
                                    "done": true
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTaskWithBlankTitleReturns400() throws Exception {
        createSampleTask();

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "   ",
                                    "done": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTasksFilterByDoneTrue() throws Exception {
        createTask("Task 1", false);
        createTask("Task 2", true);

        mockMvc.perform(get("/tasks").param("done", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 2"))
                .andExpect(jsonPath("$[0].done").value(true));
    }

    @Test
    void getAllTasksFilterByDoneFalse() throws Exception {
        createTask("Task 1", false);
        createTask("Task 2", true);

        mockMvc.perform(get("/tasks").param("done", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].done").value(false));
    }

    @Test
    void getAllTasksFilterWhenNoTasksMatchReturnsEmptyList() throws Exception {
        createTask("Task 1", false);

        mockMvc.perform(get("/tasks").param("done", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllTasksWithInvalidDoneFilterReturns400() throws Exception {
        mockMvc.perform(get("/tasks").param("done", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTasksWithoutFilterReturnsAll() throws Exception {
        createTask("Task 1", false);
        createTask("Task 2", true);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    private void createTask(String title, boolean done) throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "%s",
                                    "done": %s
                                }
                                """.formatted(title, done)))
                .andExpect(status().isCreated());
    }

    private void createSampleTask() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Learn Java",
                                    "done": false
                                }
                                """))
                .andExpect(status().isCreated());
    }
}
