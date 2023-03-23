package com.mindhub.homebanking.configurations;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/index.html", "/web/style.css").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/web/account.html", "/web/cards.html", "/web/accounts.html","/web/setting.html",
                        "/web/create-cards.html/", "/web/transfers.html", "/web/loan-application.html", "/web/exchange.html" ).hasAuthority("CLIENT")
                .antMatchers("/api/logout").hasAnyAuthority("ADMIN","CLIENT")
                .antMatchers("/api/clients/current/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers("/api/clients/current").hasAuthority("CLIENT")
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/admin/manager/", "/admin/create-loan.html").hasAuthority("ADMIN")
                .antMatchers("/web/**").hasAuthority("CLIENT")
                .antMatchers("/api/clients").hasAuthority("ADMIN")
                .antMatchers( "/api/**", "/h2-console/").hasAuthority("ADMIN");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> {
            if(req.getRequestURI().contains("web") || req.getRequestURI().contains("admin")){
                res.sendRedirect("/web/index.html");
            }else{
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        });
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}