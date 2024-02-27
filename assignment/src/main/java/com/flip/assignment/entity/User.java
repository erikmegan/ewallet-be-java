package com.flip.assignment.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class User extends BaseEntity {
    @NotBlank
    @Size(max = 50)
    @Column
    private String username;

    @Column
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
}
