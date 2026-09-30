package com.example.bookmyhotels.api;

import com.example.bookmyhotels.model.Guest;
import com.example.bookmyhotels.model.Reservation;
import com.example.bookmyhotels.model.ReservationRequest;
import com.example.bookmyhotels.model.ReservationResponse;
import com.example.bookmyhotels.model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/guests")
    Call<List<Guest>> getGuests();

    @GET("api/rooms")
    Call<List<Room>> getRooms();

    @POST("api/reservations")
    Call<ReservationResponse> createReservation(
            @Body ReservationRequest request
    );

    @GET("api/reservations")
    Call<List<Reservation>> getReservations();

    @DELETE("api/reservations/{reservation_id}")
    Call<Void> cancelReservation(
            @Path("reservation_id") int reservationId
    );
}