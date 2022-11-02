package org.ae.account.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Table("T_USER")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
  @Id
  private Long id;


  private String login;

  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 60)
  @Column("password_hash")
  private String password;

  @Size(max = 50)
  @Column("first_name")
  private String firstName;

  @Size(max = 50)
  @Column("last_name")
  private String lastName;

  @Email
  @Size(min = 5, max = 254)
  private String email;

  @NotNull
  private boolean activated = false;

  @Size(min = 2, max = 10)
  @Column("lang_key")
  private String langKey;


  @Size(max = 20)
  @Column("activation_key")
  @JsonIgnore
  private String activationKey;

  @Size(max = 20)
  @Column("reset_key")
  @JsonIgnore
  private String resetKey;

  @Column("reset_date")
  private Instant resetDate = null;

}
