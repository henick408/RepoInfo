package org.henick.repoinfo;

import org.springframework.stereotype.Component;

@Component
class RepoMapper {
    private final BranchMapper branchMapper;

    RepoMapper(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    Repo fromExternalDto(RepoExternalDto externalDto) {
        return new Repo(
                externalDto.name(),
                externalDto.owner().login(),
                externalDto.fork()
        );
    }
    RepoResponseDto toResponseDto(Repo repo) {
        return new RepoResponseDto(
                repo.getName(),
                repo.getOwnerLogin(),
                repo.getBranches().stream().map(branchMapper::toResponseDto).toList()
        );
    }
}
