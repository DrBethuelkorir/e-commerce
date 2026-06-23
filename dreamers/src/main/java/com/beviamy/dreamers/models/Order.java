package com.beviamy.dreamers.models;

import com.beviamy.dreamers.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<OrderItems> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user-id")
    private User user;

    public Order(
            LocalDate orderDate,
            BigDecimal totalAmount,
            OrderStatus orderStatus,
            Set<OrderItems> orderItems
        ) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
    }

}
