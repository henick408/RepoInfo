package org.henick.repoinfo;

import java.util.List;

record RepoResponseDto(
        String repositoryName,
        String ownerLogin,
        List<BranchResponseDto> branches
) {
}
