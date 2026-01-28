# AGENTS.md - IridiumSkyblock Development Reference

This document provides essential development guidelines and commands for agentic coding assistants working on the IridiumSkyblock codebase.

## Research Tools

You should use the perplexity MCP to search for additional context from Google when working on unfamiliar Minecraft/Java concepts or when the codebase lacks sufficient documentation. Use `perplexity_search` for quick web searches and `perplexity_ask` for conversational research.

## Build Commands

You should use Gradle commands via the `./gradlew` wrapper to build the project:

- `./gradlew build` - Compile, run tests, and create JAR (includes shadowJar with relocated dependencies)
- `./gradlew compileJava` - Compile main source code only
- `./gradlew shadowJar` - Create the final plugin JAR with shaded dependencies

The build targets Java 8 compatibility for compilation with UTF-8 encoding.

## Test Running

Tests are executed via `./gradlew test`. No individual test file execution is configured.

## Style Guidelines

You should follow these conventions for new code:

### Import Organization
- Java standard library imports first (`java.*`, `javax.*`)
- Third-party imports second (alphabetical order)
- Project imports last (`com.iridium.*`)

### Naming Conventions
- Classes: PascalCase
- Methods: camelCase
- Fields: camelCase (with `@Getter` for public access)
- Constants: SCREAMING_SNAKE_CASE
- Parameters: camelCase

### Code Structure
- Use Lombok annotations (`@Getter`, `@Setter`, `@Data`) extensively
- Prefer method references and streams over loops when appropriate
- Include JavaDoc for public APIs (classes, methods, fields)

### Formatting
- 4-space indentation (follow existing code patterns)
- Opening braces on same line as declaration
- Consistent spacing around operators
- Line length around 120-140 characters

### Error Handling
- Catch specific exceptions rather than generic `Exception`
- Log warnings/errors using `plugin.getLogger()`
- Use try-with-resources for I/O operations
- Validate parameters at method entry points
- Fail gracefully with meaningful messages

### Minecraft Plugin Patterns
- Register listeners using `plugin.getServer().getPluginManager().registerEvents()`
- Use `Bukkit.getScheduler()` for async operations
- Handle null checks for player/disconnection scenarios
- Follow Bukkit API conventions for event handling
- Use descriptive permission node names matching the plugin's structure