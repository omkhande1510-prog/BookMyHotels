package com.example.bookmyhotels.model;

public class ReservationResponse {

    private String message;
    private int reservation_id;
    private double amount;
    private int nights;
    private String payment_method;
    private String payment_status;

    public String getMessage() {
        return message;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public double getAmount() {
        return amount;
    }

    public int getNights() {
        return nights;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getPayment_status() {
        return payment_status;
    }
}
