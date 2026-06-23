package com.beviamy.dreamers.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId
    private String email;
    private String password;

    @OneToOne(mappedBy ="user" ,cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy ="user" ,cascade = CascadeType.ALL)
    private Order order;

    public User(String firstName, String lastName, String email, String password,Cart cart,Order order) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.cart = cart;
        this.order = order;
    }
}


