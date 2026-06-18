package org.henick.repoinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Repo {
    private String name;
    private String ownerLogin;
    private boolean isFork;
    private List<Branch> branches;

    Repo(String name, String ownerLogin, boolean isFork) {
        this.name = name;
        this.ownerLogin = ownerLogin;
        this.isFork = isFork;
        this.branches = List.of();
    }

}
