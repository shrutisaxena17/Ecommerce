package com.example.Ecommerce.payment;

import com.example.Ecommerce.entity.Order;
import com.example.Ecommerce.entity.OrderItem;
import com.example.Ecommerce.entity.Product;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    public StripeResponse checkoutProducts(Order order, String customerEmail) {
        Stripe.apiKey = secretKey;

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(product.getName())
                            .setDescription(product.getDescription())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("INR")
                            .setUnitAmount(orderItem.getPrice().multiply(new BigDecimal(100)).longValue())
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) orderItem.getQuantity())
                            .setPriceData(priceData)
                            .build();

            lineItems.add(lineItem);
        }

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/success")
                        .setCancelUrl("http://localhost:8080/cancel")
                        .addAllLineItem(lineItems)
                        .setCustomerEmail(customerEmail)
                        .build();


        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            e.printStackTrace();
            return new StripeResponse("ERROR", "Error creating payment session: " + e.getMessage(), null, null);
        }

        return new StripeResponse("SUCCESS", "Payment session created", session.getId(), session.getUrl());
    }
}
