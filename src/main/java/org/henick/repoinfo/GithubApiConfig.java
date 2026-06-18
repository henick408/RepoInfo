package org.henick.repoinfo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class GithubApiConfig {

    @Bean
    RestClient githubApiRestClient(
            RestClient.Builder builder
    ) {
        return builder
                .baseUrl("https://api.github.com")
                .build();
    }

}
