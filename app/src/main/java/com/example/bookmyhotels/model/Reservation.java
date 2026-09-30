package com.example.bookmyhotels.model;

public class Reservation {

    private int reservation_id;
    private int room_id;
    private int room_number;
    private String room_type;
    private int guest_id;
    private String guest_name;
    private String check_in;
    private String check_out;
    private String status;
    private double amount;
    private String payment_method;
    private String payment_status;

    public int getReservation_id() {
        return reservation_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public int getRoom_number() {
        return room_number;
    }

    public String getRoom_type() {
        return room_type;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public String getCheck_in() {
        return check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public String getStatus() {
        return status;
    }

    public double getAmount() {
        return amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getPayment_status() {
        return payment_status;
    }
}