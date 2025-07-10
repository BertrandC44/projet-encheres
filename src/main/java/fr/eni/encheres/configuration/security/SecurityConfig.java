/*package fr.eni.encheres.configuration.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/", "/encheres", "/css/*", "/images/*", "encheres/connexion", "encheres/deconnexion", "encheres/inscription", "/encheres/encherir",
							"/encheres/profil", "/encheres/vente", "/encheres/profil/modifier").permitAll()
				.anyRequest().denyAll()
			)
			.sessionManagement((session) -> session
				    .invalidSessionUrl("/encheres/connexion")
				)
			.logout((logout) -> logout
					.logoutUrl("/encheres/deconnexion")
		            .logoutSuccessUrl("/")
		            .invalidateHttpSession(true)
		           );
	
		return http.build();
	}
	
	@Bean
	UserDetailsManager users(DataSource dataSource) {
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.setUsersByUsernameQuery("SELECT pseudo, '{noop}' + motDePasse, 1 actif FROM UTILISATEUR WHERE pseudo = ?");
		users.setAuthoritiesByUsernameQuery("SELECT pseudo, role FROM ROLES WHERE pseudo = ?");
		return users;
	}
	
	
	
}*/
