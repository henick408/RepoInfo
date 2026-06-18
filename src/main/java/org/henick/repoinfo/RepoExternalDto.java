package org.henick.repoinfo;

record RepoExternalDto(
        String name,
        OwnerExternalDto owner,
        boolean fork
) {
}
