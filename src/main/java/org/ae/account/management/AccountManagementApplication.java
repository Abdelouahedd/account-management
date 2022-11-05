package org.ae.account.management;

import org.ae.account.management.domain.Authority;
import org.ae.account.management.repository.AuthorityRepository;
import org.ae.account.management.util.AppProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class AccountManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountManagementApplication.class, args);
  }

  @Bean
  CommandLineRunner run(AuthorityRepository authorityRepository) {
    return (arg) -> {
      Authority userAuthority = new Authority("ROLE_USER");
      Authority adminAuthority = new Authority("ROLE_ADMIN");
      authorityRepository.saveAll(Arrays.asList(userAuthority, adminAuthority));
    };
  }
}

