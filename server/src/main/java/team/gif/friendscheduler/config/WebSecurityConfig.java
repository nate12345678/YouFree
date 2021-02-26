package team.gif.friendscheduler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Disable CSRF (cross site request forgery)
//		http.csrf().disable();
		
		http.authorizeRequests()
				.anyRequest()
				.permitAll()
				.and()
				.csrf().disable();
//				.and()
//				.requiresChannel()
//				.anyRequest()
//				.requiresSecure();
	}
}
