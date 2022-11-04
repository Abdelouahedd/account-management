package org.ae.account.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Table(name="T_USER")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends AbstractAuditingEntity<Long>{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  private String login;

  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 60)
  @Column(name = "password_hash")
  private String password;

  @Size(max = 50)
  @Column(name="first_name")
  private String firstName;

  @Size(max = 50)
  @Column(name="last_name")
  private String lastName;

  @Email
  @Size(min = 5, max = 254)
  private String email;

  @NotNull
  private boolean activated = false;

  @Size(min = 2, max = 10)
  @Column(name="lang_key")
  private String langKey;


  @Size(max = 20)
  @Column(name="activation_key")
  @JsonIgnore
  private String activationKey;

  @Size(max = 20)
  @Column(name="reset_key")
  @JsonIgnore
  private String resetKey;

  @Column(name="reset_date")
  private Instant resetDate = null;

  @ManyToMany()
  @JoinTable(
    name = "T_USER_AUTHORITY",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id")
  )
  private Set<Authority> authorities = new HashSet<>();

}
