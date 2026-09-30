package com.example.bookmyhotels.model;

public class ReservationRequest {

    private int room_id;
    private int guest_id;
    private String check_in;
    private String check_out;
    private String payment_method;

    public ReservationRequest(
            int room_id,
            int guest_id,
            String check_in,
            String check_out,
            String payment_method) {

        this.room_id = room_id;
        this.guest_id = guest_id;
        this.check_in = check_in;
        this.check_out = check_out;
        this.payment_method = payment_method;
    }
}
