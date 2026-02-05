# Investware Frontend

#### Table of Contents

- [Prerequisites](#prerequisites)
- [Scripts](#scripts)
- [Commit Standards](#commit-standards)
- [Commit Format](#commit-format)
- [Dependencies](#dependencies)
- [Dev Dependencies](#dev-dependencies)

## Prerequisites

Before you can start using Seeds, make sure you have the following prerequisites installed:

- Node.js >= 20.x

## Scripts

Following scripts for development and testing:

- `dev`: Start the development server with Next.js.
- `build`: Build the Next.js application.
- `start`: Start the Next.js production server.
- `prepare`: Install Husky hooks.
- `lint`: Lint TypeScript and JavaScript files using ESLint.
- `check-app-types`: Check TypeScript types for the application.
- `check-test-types`: Check TypeScript types for tests.
- `check-lint`: Run ESLint to check code style.
- `check-format`: Check code formatting using Prettier.
- `format`: Automatically format code using Prettier.
- `test`: Run Jest tests.
- `test:watch`: Run Jest tests in watch mode.

## Commit Standards

We follow a specific set of commit standards for this project. Each commit message should start with a type and include a brief description. Here are the commit types and their meanings:

- **feat**: For new features or enhancements.
- **fix**: For bug fixes.
- **chore**: Typically for maintenance tasks or things not directly related to the codebase (hidden in the changelog).
- **docs**: For documentation changes (hidden in the changelog).
- **style**: For code style changes (hidden in the changelog).
- **refactor**: For code refactoring (hidden in the changelog).
- **perf**: For performance improvements (hidden in the changelog).
- **test**: For adding or modifying tests (hidden in the changelog).

## Commit Format

Each commit message should follow this format:
<type>: <description>
For Example:

```
feat: Add user authentication
fix: Fix issue with login button
```
