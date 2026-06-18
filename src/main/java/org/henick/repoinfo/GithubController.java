package org.henick.repoinfo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class GithubController {

    private final GithubService githubService;

    GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{userLogin}")
    ResponseEntity<List<RepoResponseDto>> getUserInfo(@PathVariable String userLogin) {
        List<RepoResponseDto> repoResponseDtos = githubService.getUserInfo(userLogin);
        return ResponseEntity.ok(repoResponseDtos);
    }

}
