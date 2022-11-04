package org.ae.account.management.util;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Data
public class AppProperties {
  private final Mail mail = new Mail();
  private final Security security = new Security();
  private final CorsConfiguration cors = new CorsConfiguration();

  @Data
  public static class Mail {
    private String baseUrl;

  }

  @Data
  public static class Security {

    private final Authentication authentication = new Authentication();

    @Data
    public static class Authentication {

      private final Jwt jwt = new Jwt();

      @Data
      public static class Jwt {

        private String secret;

        private String base64Secret;

        private long tokenValidityInSeconds;

        private long tokenValidityInSecondsForRememberMe;

      }
    }

  }
}
