package com.green.greengram.config.security;

//Spring Security 세팅
import com.green.greengram.config.constants.ConstOAuth2;
import com.green.greengram.config.jwt.JwtAuthenticationEntryPoint;
import com.green.greengram.config.jwt.TokenAuthenticationFilter;
import com.green.greengram.config.jwt.TokenProvider;
import com.green.greengram.config.security.oauth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //메소드 빈등록이 있어야 의미가 있다. 메소드 빈등록이 싱글톤이 됨.
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    //OAuth2
    private final Oauth2AuthenticationCheckRedirectUriFilter oauth2AuthenticationCheckRedirectUriFilter;
    private final Oauth2AuthenticationRequestBasedOnCookieRepository repository;
    private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final MyOauth2UserService myOauth2UserService;
    private final ConstOAuth2 constOAuth2;

    //스프링 시큐리티 기능 비활성화 (스프링 시큐리티가 관여하지 않았으면 하는 부분)
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                        .requestMatchers(new AntPathRequestMatcher("/static/**"));
//    }

    @Bean //스프링이 메소드 호출을 하고 리턴한 객체의 주소값을 관리한다. (빈등록)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //시큐리티가 세션을 사용하지 않는다.
                .httpBasic(h -> h.disable()) //SSR(Server Side Rendering)이 아니다. 화면을 만들지 않을꺼기 때문에 비활성화 시킨다. 시큐리티 로그인창 나타나지 않을 것이다.
                .formLogin(form -> form.disable()) //SSR(Server Side Rendering)이 아니다. 폼로그인 기능 자체를 비활성화
                .csrf(csrf -> csrf.disable()) //SSR(Server Side Rendering)이 아니다. 보안관련 SSR 이 아니면 보안이슈가 없기 때문에 기능을 끈다.
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/feed", "/api/feed/**").authenticated() //로그인이 되어 있어야만 사용 가능
                            .requestMatchers(HttpMethod.GET,"/api/user").authenticated()
                            .requestMatchers(HttpMethod.PATCH,"/api/user/pic").authenticated()
                            .anyRequest().permitAll() //나머지 요청은 모두 허용
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login( oauth2 -> oauth2.authorizationEndpoint( auth -> auth.baseUri( constOAuth2.getBaseUri() )
                                                                                  .authorizationRequestRepository(repository) )
                                              .redirectionEndpoint( redirection -> redirection.baseUri("/*/oauth2/code/*") ) //BE가 사용하는 redirectUri이다. 플랫폼마다 설정을 할 예정
                                              .userInfoEndpoint( userInfo -> userInfo.userService(myOauth2UserService) )
                                              .successHandler(oauth2AuthenticationSuccessHandler)
                                              .failureHandler(oauth2AuthenticationFailureHandler) )
                .addFilterBefore(oauth2AuthenticationCheckRedirectUriFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
