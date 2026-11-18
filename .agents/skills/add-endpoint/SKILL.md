---
name: add-endpoint
description: Adds or modifies a REST endpoint in this Spring Boot project with tests.
---

# Add an endpoint

1. Read the existing controller and tests to match conventions.
2. Write a MockMvc test for the new endpoint first, including error cases.
3. Implement the endpoint. Keep business logic out of the controller.
4. Run ./mvnw test (on Windows, use .\mvnw.cmd test) and fix failures until all pass.
5. Summarize the change and list the files you touched.
6. Preserve existing API behavior and avoid unrelated code changes.
7. Do not commit or push without explicit user approval.
8. Run the new tests before implementing the endpoint and verify that they fail for the expected reason. After implementation, verify that all tests pass.