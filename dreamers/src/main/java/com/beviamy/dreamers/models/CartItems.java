    package com.beviamy.dreamers.models;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.math.BigDecimal;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    public class CartItems {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;

        @ManyToOne
        @JoinColumn(name = "productId")
        private Product product;
        @ManyToOne()
        @JoinColumn(name = "cartId")
        private Cart cart;

        public void calculateTotalPrice() {
            this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
        }
    }
