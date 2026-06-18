package org.henick.repoinfo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class GithubService {

    private final GithubApiClient githubApiClient;
    private final RepoMapper repoMapper;

    GithubService(GithubApiClient githubApiClient, RepoMapper repoMapper) {
        this.githubApiClient = githubApiClient;
        this.repoMapper = repoMapper;
    }

    List<RepoResponseDto> getUserInfo(String userLogin) {
        List<Repo> repos = githubApiClient.getRepos(userLogin).stream().filter(repo -> !repo.isFork()).toList();
        for (Repo repo : repos) {
            List<Branch> branches = githubApiClient.getBranches(repo.getOwnerLogin(), repo.getName());
            repo.setBranches(branches);
        }

        return repos.stream()
                .map(repoMapper::toResponseDto)
                .toList();
    }

}
