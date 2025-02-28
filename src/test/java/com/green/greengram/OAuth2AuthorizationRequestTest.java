package com.green.greengram;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
public class OAuth2AuthorizationRequestTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test() throws Exception {
        String json = "{\"authorizationUri\":\"https://accounts.google.com/o/oauth2/v2/auth\",\"responseType\":{\"value\":\"code\"},\"clientId\":\"69808373650-pcogoi7rrgas1aar165k8unpfr98n9t6.apps.googleusercontent.com\",\"redirectUri\":\"http://localhost:8080/login/oauth2/code/google\",\"scopes\":[\"email\",\"profile\"],\"state\":\"hRCsX7w5beMgPJCaQc-CR7ANFx0D2tdaItxEgYBcmD4=\",\"additionalParameters\":{},\"authorizationRequestUri\":\"https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=69808373650-pcogoi7rrgas1aar165k8unpfr98n9t6.apps.googleusercontent.com&scope=email%20profile&state=hRCsX7w5beMgPJCaQc-CR7ANFx0D2tdaItxEgYBcmD4%3D&redirect_uri=http://localhost:8080/login/oauth2/code/google\",\"attributes\":{\"registration_id\":\"google\"},\"authorizationGrantType\":{\"value\":\"authorization_code\"}}";
        System.out.println(json);

        System.out.println(objectMapper);

        OAuth2AuthorizationRequest oauth2 = objectMapper.convertValue(json, OAuth2AuthorizationRequest.class);
        System.out.println(oauth2);
    }
}
