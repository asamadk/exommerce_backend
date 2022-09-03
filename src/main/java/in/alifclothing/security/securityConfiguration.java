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
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://43.204.244.109:3000/","https://www.alifclothing.in/"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
//        configuration.addAllowedOrigin("*");
		configuration.setAllowCredentials(true);
//		configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); //Specify the path pattern.
		return (CorsConfigurationSource) source;
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and()
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
