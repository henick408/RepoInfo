package org.henick.repoinfo;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;


@SpringBootTest
@AutoConfigureMockMvc
@EnableWireMock(@ConfigureWireMock(baseUrlProperties = "github-api.base_url"))
class GithubServiceTests {

    @Autowired
    MockMvc mockMvc;


    @Test
    void existingUserReturnsNonForkReposWithBranches() throws Exception {
        WireMock.stubFor(WireMock.get("/users/existingUser/repos")
                .willReturn(WireMock.okJson("""
                  [
                    {
                        "id": 1234567890,
                        "name": "RepoInfo",
                        "full_name": "existingUser/RepoInfo",
                        "private": false,
                        "owner": {
                            "login": "existingUser",
                            "id": 123456789
                        },
                        "html_url": "https://github.com/existingUser/RepoInfo",
                        "description": null,
                        "fork": false
                    },
                    {
                        "id": 1234567899,
                        "name": "ForkedRepo",
                        "full_name": "existingUser/ForkedRepo",
                        "private": false,
                        "owner": {
                            "login": "existingUser",
                            "id": 175512379
                        },
                        "html_url": "https://github.com/existingUser/ForkedRepo",
                        "description": null,
                        "fork": true
                    },
                    {
                        "id": 1234567899,
                        "name": "EmptyRepo",
                        "full_name": "existingUser/EmptyRepo",
                        "private": false,
                        "owner": {
                            "login": "existingUser",
                            "id": 175512379
                        },
                        "html_url": "https://github.com/existingUser/EmptyRepo",
                        "description": null,
                        "fork": false
                    }
                  ]
                  """
                )));
        WireMock.stubFor(WireMock.get("/repos/existingUser/RepoInfo/branches")
                .willReturn(WireMock.okJson("""
                  [
                    {
                      "name": "master",
                      "commit": {
                        "sha": "abc123"
                      }
                    },
                    {
                      "name": "feature",
                      "commit": {
                        "sha": "def456"
                      }
                    }
                  ]
                """
                )));
        WireMock.stubFor(WireMock.get("/repos/existingUser/EmptyRepo/branches")
                .willReturn(WireMock.okJson("""
                  []
                """
                )));
        WireMock.stubFor(WireMock.get("/repos/existingUser/ForkedRepo/branches")
                .willReturn(WireMock.okJson("""
                  [
                    {
                      "name": "master",
                      "commit": {
                        "sha": "ghi789"
                      }
                    }
                  ]
                """
                )));

        mockMvc.perform(MockMvcRequestBuilders.get("/existingUser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].repositoryName").value("RepoInfo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ownerLogin").value("existingUser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branches[0].name").value("master"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branches[0].lastCommitSha").value("abc123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].repositoryName").value("EmptyRepo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ownerLogin").value("existingUser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].branches.length()").value(0));

    }

    @Test
    void nonExistingUserReturnsStatus404WithErrorBody() throws Exception {
        WireMock.stubFor(WireMock.get("/users/nonExistingUser/repos")
                .willReturn(WireMock.jsonResponse("""
                        {
                          "status": 404,
                          "message": "User with name = 'nonExistingUser' does not exist"
                        }
                        """,
                        404
                )));
        mockMvc.perform(MockMvcRequestBuilders.get("/nonExistingUser"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with name = 'nonExistingUser' does not exist"));
    }

    @Test
    void existingUserWithNoRepositoriesReturnsEmptyList() throws Exception {
        WireMock.stubFor(WireMock.get("/users/userWithNoRepos/repos")
                .willReturn(WireMock.okJson("""
                  []
                """)));

        mockMvc.perform(MockMvcRequestBuilders.get("/userWithNoRepos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    void existingUserWithOnlyForkRepositoriesReturnsEmptyList() throws Exception {
        WireMock.stubFor(WireMock.get("/users/userWithForksOnly/repos")
                .willReturn(WireMock.okJson("""
                  [
                    {
                        "id": 1234567890,
                        "name": "ForkedRepo1",
                        "full_name": "userWithForksOnly/ForkedRepo1",
                        "private": false,
                        "owner": {
                            "login": "userWithForksOnly",
                            "id": 123456789
                        },
                        "html_url": "https://github.com/userWithForksOnly/ForkedRepo1",
                        "description": null,
                        "fork": true
                    },
                    {
                        "id": 1234567899,
                        "name": "ForkedRepo2",
                        "full_name": "userWithForksOnly/ForkedRepo2",
                        "private": false,
                        "owner": {
                            "login": "userWithForksOnly",
                            "id": 175512379
                        },
                        "html_url": "https://github.com/userWithForksOnly/ForkedRepo2",
                        "description": null,
                        "fork": true
                    }
                  ]
                  """
                )));

        mockMvc.perform(MockMvcRequestBuilders.get("/userWithForksOnly"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

}
