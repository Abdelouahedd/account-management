package org.ae.account.management.services.util;

import lombok.*;
import org.thymeleaf.context.Context;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MailRequest {
  private String from;
  private String to;
  private String[] cc;
  private String subject;
  private boolean isPriority;
  private Context context;
  private String template;
}
