package com.reactivespring.intg;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@AutoConfigureWireMock(port = 8084)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
    properties = {
        "restClient.movieInfoUrl=http://localhost:8084/v1/movieInfo",
        "restClient.reviewUrl=http://localhost:8084/v1/review"
    }
)
public class MovieControllerIntgTest {



}
