package io.github.rafaeljpc.server.services.got.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.inject.Qualifier;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return dataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("spring.oauth-datasource")
    public DataSourceProperties oauthDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.oauth-datasource")
    public DataSource oauthDataSource() {
        return oauthDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Value("${auth-server.url}")
    private String authEndpoint;

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(oauthDataSource());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().anyRequest().permitAll()
            .and().cors().disable().csrf().disable().httpBasic().disable()
            .exceptionHandling()
            .authenticationEntryPoint(
                    (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            )
            .accessDeniedHandler(
                    (request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            );
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("mw/adminapp").tokenStore(tokenStore());
    }

    @Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId("adminapp");
        tokenServices.setClientSecret("password");
        tokenServices.setCheckTokenEndpointUrl(authEndpoint + "/uaa/oauth/check_token");
        return tokenServices;
    }
}
