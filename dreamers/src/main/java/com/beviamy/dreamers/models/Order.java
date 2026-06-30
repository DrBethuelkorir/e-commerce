package com.beviamy.dreamers.models;

import com.beviamy.dreamers.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;  // ← CHANGE IMPORT
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<OrderItems> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user-id")
    @JsonBackReference  // ← CHANGED THIS
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