package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class OrderEntity {

    private long transactionId;
    private int cents;
    private UserEntity user;

    public OrderEntity(long transactionId, int cents, UserEntity user) {
        this.transactionId = transactionId;
        this.cents = cents;
        this.user = user;
    }
}
