package org.henick.repoinfo;

import org.springframework.stereotype.Component;

@Component
class BranchMapper {
    Branch fromExternalDto(BranchExternalDto externalDto) {
        return new Branch(externalDto.name(), externalDto.commit().sha());
    }
    BranchResponseDto toResponseDto(Branch branch) {
        return  new BranchResponseDto(branch.getName(), branch.getLastCommitSha());
    }
}
