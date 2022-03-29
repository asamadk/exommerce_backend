package in.alifclothing.security;

import com.sun.xml.bind.v2.TODO;
import in.alifclothing.Jwt.JwtTokenVerifier;
import in.alifclothing.Jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class securityConfiguration extends WebSecurityConfigurerAdapter {



    @Bean
    public UserDetailsService getUserDetailsService(){
        return new userDetailsServiceImplementation();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration  = new CorsConfiguration();
		//Preflight is a HTTP request that is sent before actual HTTP request sent by browser. These request is created
		//by the browser.The browser check and it doesn't allow HTTPResponse if we don't configure
		//CORS(Cross Origin Resource Sharing) for the webservices end point.
		//configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080",""));
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","https://alif-frontend.herokuapp.com/"));
		//configuration.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE","OPTIONS"));
		configuration.setAllowedMethods(Arrays.asList("*")); //We use asterik, so that all HTTP methods are allowed.
		//If we want to allow credentials for HTTPResponse and credentials, here, are cookies and Authorization header.
		//Or, it could be SSL client certificate.If we want this info to be included, then credentials set to true.
		configuration.setAllowCredentials(true);
//		configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
		configuration.setAllowedHeaders(Arrays.asList("*"));//Allowed all headers.
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		//source.registerCorsConfiguration("/authenticate", configuration);
		source.registerCorsConfiguration("/**", configuration); //Specify the path pattern.
		return (CorsConfigurationSource) source;
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()))
                .addFilterAfter(new JwtTokenVerifier(),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests().
                antMatchers("/superuser/**").hasRole("SUPERUSER").
                antMatchers("/admin/**").hasRole("ADMIN").
//        antMatchers("/admin/**").permitAll().
                antMatchers("/user/**").hasRole("USER").
                antMatchers("/**").permitAll();
    }

}
