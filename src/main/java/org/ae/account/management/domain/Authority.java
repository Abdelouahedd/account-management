package org.ae.account.management.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "T_AUTHORITY")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Authority extends AbstractAuditingEntity<Long>{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
}
