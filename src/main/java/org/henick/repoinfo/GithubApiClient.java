package org.henick.repoinfo;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class GithubApiClient {

    private final RestClient githubApiRestClient;
    private final BranchMapper branchMapper;
    private final RepoMapper repoMapper;


    GithubApiClient(RestClient restClient, BranchMapper branchMapper, RepoMapper repoMapper) {
        this.githubApiRestClient = restClient;
        this.branchMapper = branchMapper;
        this.repoMapper = repoMapper;
    }

    List<Repo> getRepos(String username) {
        List<RepoExternalDto> repositories = githubApiRestClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        (_, _) -> {
                            throw new UserNotFoundException(username, "User with name = '" + username + "' does not exist");
                        }
                )
                .body(new ParameterizedTypeReference<>() {} );

        return repositories.stream()
                .map(repoMapper::fromExternalDto)
                .toList();
    }

    List<Branch> getBranches(String owner, String repo) {
        List<BranchExternalDto> branches = githubApiRestClient.get()
                .uri("/repos/{owner}/{repo}/branches", owner, repo)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return branches.stream()
                .map(branchMapper::fromExternalDto)
                .toList();
    }

}
