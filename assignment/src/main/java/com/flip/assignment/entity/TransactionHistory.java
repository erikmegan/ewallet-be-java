package com.flip.assignment.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "transaction_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class TransactionHistory  extends BaseEntity implements Serializable {

    @Column
    private String username;

    @Column
    private BigDecimal amount;

    @Column
    private String type;

    @Column
    private String externalId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "related_transaction_id", referencedColumnName = "id")
    private TransactionHistory relatedTransactionId;

    @OneToOne(mappedBy = "relatedTransactionId")
    private TransactionHistory transactionHistories;

    @Override
    public String toString() {
        return "TransactionHistories{" + "username='" + username + '\'' + ", amount=" + amount
                + ", type='" + type + '\'' + ", externalId='" + externalId + '\'' + '}';
    }

}
