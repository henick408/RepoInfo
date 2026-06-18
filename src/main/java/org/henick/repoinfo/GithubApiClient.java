package org.henick.repoinfo;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class GithubApiClient {

    private final RestClient githubApiRestClient;

    GithubApiClient(RestClient restClient) {
        this.githubApiRestClient = restClient;
    }

    List<Repo> getRepos(String username) {
        List<RepoExternalDto> repositories = githubApiRestClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        (_, _) -> {
                            throw new RuntimeException();
                        }
                )
                .body(new ParameterizedTypeReference<>() {} );

        return repositories.stream()
                .map(externalRepo ->
                        new Repo(externalRepo.name(), externalRepo.owner().login(), externalRepo.fork())
                )
                .toList();
    }
}
