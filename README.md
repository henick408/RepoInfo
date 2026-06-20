# RepoInfo

A small Spring Boot service that acts as a proxy in front of the GitHub REST API.

## Overview

Given a GitHub login, the service:
- Fetches the user's repositories from the GitHub API.
- Filters out forks.
- Fetches branches (name and last commit SHA) for each remaining repository.
- Returns the aggregated result as JSON.

## Tech Stack

- Java 25
- Spring Boot 4
- Gradle (Kotlin DSL)
- WireMock for integration testing
- Backing API: [GitHub REST API v3](https://developer.github.com/v3)

## API

### `GET /{userLogin}`

Returns the non-fork repositories of the given GitHub user.

**Success — `200 OK`**

```json
[
  {
    "repositoryName": "RepoInfo",
    "ownerLogin": "henick408",
    "branches": [
      { 
        "name": "master", 
        "lastCommitSha": "abc123"
      }
    ]
  }
]
```

If the user has no repositories, or only forked ones, the response is an empty array.

**User not found — `404 Not Found`**

```json
{
  "status": 404,
  "message": "User with name = 'someUser' does not exist"
}
```

**Other upstream GitHub errors** (e.g. rate limiting) are mapped to the same shape, with GitHub's own error message and status code:

```json
{
  "status": 403,
  "message": "API rate limit exceeded for ..."
}
```

## Configuration

Set in `src/main/resources/application.properties`:

| Property              | Description                                                                                            | Required | Default                  |
|-----------------------|--------------------------------------------------------------------------------------------------------|----------|--------------------------|
| `github-api.base_url` | Base URL of the GitHub API.                                                                            | yes      | `https://api.github.com` |
| `github-api.token`    | Optional GitHub personal access token, used to raise the otherwise low (60/hour) anonymous rate limit. | no       | none                     |

## Running the Application

```
./gradlew bootRun
```

## Running Tests

```
./gradlew test
```

Tests are integration tests: they call the controller through `MockMvc`, with the GitHub API emulated by an embedded WireMock server.

## License

MIT — see [LICENSE](LICENSE).
