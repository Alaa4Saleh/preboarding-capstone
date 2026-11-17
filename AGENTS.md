
# Agent Instructions

## Project
- A REST API for managing tasks using Java 17, Spring Boot, and Maven.
- All tasks are stored in memory; no database is required.

## Build and Test
- Build: `./mvnw clean package`
- Test: `./mvnw test`
- On Windows, use `.\mvnw.cmd` instead of `./mvnw`.

## Conventions
- Keep controllers focused on HTTP handling and move complex logic to service classes.
- Use Java records for simple data models and Bean Validation for input validation.
- Use appropriate HTTP status codes and keep code simple and readable.

## Rules
- Every new endpoint must have MockMvc tests, including relevant error cases.
- Preserve existing API behavior unless explicitly requested otherwise.
- Do not add unnecessary dependencies or introduce a database.
- Run all tests before declaring a task complete.
- Explain the implementation plan before modifying files.
- Do not commit changes unless explicitly requested.