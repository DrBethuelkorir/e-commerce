    package com.beviamy.dreamers.models;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.hibernate.annotations.NaturalId;

    import java.util.List;

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
        @Column(unique = true, nullable = false)
        private String email;
        @JsonIgnore
        private String password;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
        @JsonIgnore
        private Cart cart;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<Order> orders;  // ← Changed from "order" to "orders"

        public User(String firstName, String lastName, String email,
                    String password, Cart cart, List<Order> orders) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.cart = cart;
            this.orders = orders;
        }
    }