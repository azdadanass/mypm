package ma.azdad.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

import ma.azdad.model.Role;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Value("${applicationCode}")
	public String applicationCode;

	@Value("#{'${spring.profiles.active}'.replaceAll('-dev','')}")
	public String erp;

	@Autowired
	private LoginFailureHandler loginFailureHandler;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().cacheControl().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests() //
				.antMatchers("/test.xhtml", "/resources/**", "/login.xhtml", "/blank", "/rest/**", "/resttest/**", "/passwordReset.xhtml", "/ad.xhtml", "/javax.faces.resource/**",
						"/favicon.ico", "/.well-known/**", "/git.txt")
				.permitAll() //
				.antMatchers("/scripts.xhtml").hasRole(Role.ROLE_IT_MANAGER.getRole())//
				.antMatchers(getPages("Company")).hasRole(Role.ROLE_IADMIN_ADMIN.getRole())//
				.antMatchers(getPages("Lob")).hasRole(Role.ROLE_IADMIN_ADMIN.getRole())//
				.antMatchers(getPages("Bu")).hasRole(Role.ROLE_IADMIN_ADMIN.getRole())//
				.antMatchers(getPages("Costcenter")).hasRole(Role.ROLE_IADMIN_ADMIN.getRole())//
				.antMatchers(getPages("Currency")).hasRole(Role.ROLE_IADMIN_ADMIN.getRole())//
				.antMatchers(getPages("AccessLog")).hasRole(Role.ROLE_IADMIN_ADMIN.getRole())//
				.antMatchers("/**").hasRole(applicationCode)//
				.and().formLogin().authenticationDetailsSource(authenticationDetailsSource).loginPage("/login.xhtml")
				.failureHandler(loginFailureHandler) //
				.successHandler(loginSuccessHandler) //
				.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)//
				.and().logout().permitAll();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}
	
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}
	
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(ImmutableList.of("*"));
		configuration.setAllowedMethods(ImmutableList.of("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	private String[] getPages(String... models) {
		String[] pages = new String[3 * models.length];
		for (int i = 0; i < models.length; i++) {
			String str1 = models[i];
			String str2 = str1.substring(0, 1).toLowerCase() + str1.substring(1);
			pages[i * 3] = "/" + str2 + "List.xhtml";
			pages[i * 3 + 1] = "/addEdit" + str1 + ".xhtml";
			pages[i * 3 + 2] = "/view" + str1 + ".xhtml";
		}
		return pages;
	}

	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
}