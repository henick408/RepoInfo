package org.henick.repoinfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class GithubApiConfig {

    @Bean
    RestClient githubApiRestClient(
            RestClient.Builder builder,
            @Value("${github-api.base_url}") String baseUrl,
            @Value("${github-api.token}") String token
    ) {
        RestClient.Builder clientBuilder = builder
                .baseUrl(baseUrl);
        if (token != null && !token.isBlank()) {
            clientBuilder.defaultHeader("Authorization", "Bearer " + token);
        }

        return clientBuilder.build();
    }

}
