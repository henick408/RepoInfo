package org.henick.repoinfo;

record ExternalRepoDto(
        String name,
        ExternalOwnerDto owner,
        boolean fork
) {
}
