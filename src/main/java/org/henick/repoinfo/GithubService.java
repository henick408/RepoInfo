package org.henick.repoinfo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class GithubService {

    private final GithubApiClient githubApiClient;

    GithubService(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    List<RepoResponseDto> getUserInfo(String userLogin) {
        List<Repo> repos = githubApiClient.getRepos(userLogin);
        return repos.stream()
                .filter(repo -> !repo.isFork())
                .map(repo -> new RepoResponseDto(repo.name(), repo.ownerLogin()))
                .toList();
    }

}
