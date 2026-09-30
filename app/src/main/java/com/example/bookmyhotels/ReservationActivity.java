package com.example.bookmyhotels;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmyhotels.model.Guest;
import com.example.bookmyhotels.model.ReservationRequest;
import com.example.bookmyhotels.model.ReservationResponse;
import com.example.bookmyhotels.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {

    private TextView txtSelectedRoom;
    private TextView txtCheckIn;
    private TextView txtCheckOut;

    private Spinner spinnerGuest;
    private Spinner spinnerPaymentMethod;

    private int roomId;
    private int roomNumber;

    private List<Guest> guests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservation);

        txtSelectedRoom = findViewById(R.id.txtSelectedRoom);
        txtCheckIn = findViewById(R.id.txtCheckIn);
        txtCheckOut = findViewById(R.id.txtCheckOut);

        spinnerGuest = findViewById(R.id.spinnerGuest);
        spinnerPaymentMethod =
                findViewById(R.id.spinnerPaymentMethod);

        Button btnCheckIn =
                findViewById(R.id.btnCheckIn);

        Button btnCheckOut =
                findViewById(R.id.btnCheckOut);

        Button btnConfirmBooking =
                findViewById(R.id.btnConfirmBooking);

        roomId =
                getIntent().getIntExtra("room_id", -1);

        roomNumber =
                getIntent().getIntExtra("room_number", -1);

        txtSelectedRoom.setText(
                "Selected Room: " + roomNumber
        );

        setupPaymentSpinner();

        loadGuests();

        btnCheckIn.setOnClickListener(
                v -> showDatePicker(true)
        );

        btnCheckOut.setOnClickListener(
                v -> showDatePicker(false)
        );

        btnConfirmBooking.setOnClickListener(
                v -> confirmBooking()
        );
    }


    // =====================================================
    // PAYMENT SPINNER
    // =====================================================

    private void setupPaymentSpinner() {

        String[] paymentMethods = {
                "Cash",
                "UPI",
                "Card"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        paymentMethods
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerPaymentMethod.setAdapter(adapter);
    }


    // =====================================================
    // LOAD GUESTS
    // =====================================================

    private void loadGuests() {

        RetrofitClient.getApiService()
                .getGuests()
                .enqueue(new Callback<List<Guest>>() {

                    @Override
                    public void onResponse(
                            Call<List<Guest>> call,
                            Response<List<Guest>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            guests = response.body();

                            List<String> guestNames =
                                    new ArrayList<>();

                            for (Guest guest : guests) {

                                guestNames.add(
                                        guest.getFirst_name()
                                                + " "
                                                + guest.getLast_name()
                                );
                            }

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(
                                            ReservationActivity.this,
                                            android.R.layout.simple_spinner_item,
                                            guestNames
                                    );

                            adapter.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item
                            );

                            spinnerGuest.setAdapter(adapter);

                        } else {

                            Toast.makeText(
                                    ReservationActivity.this,
                                    "Failed to load guests. HTTP "
                                            + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Guest>> call,
                            Throwable t) {

                        Toast.makeText(
                                ReservationActivity.this,
                                "Guest connection failed:\n"
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }


    // =====================================================
    // DATE PICKER
    // =====================================================

    private void showDatePicker(boolean isCheckIn) {

        Calendar calendar =
                Calendar.getInstance();

        DatePickerDialog dialog =
                new DatePickerDialog(
                        this,

                        (view, year, month, dayOfMonth) -> {

                            String date =
                                    year
                                            + "-"
                                            + String.format(
                                            "%02d",
                                            month + 1
                                    )
                                            + "-"
                                            + String.format(
                                            "%02d",
                                            dayOfMonth
                                    );

                            if (isCheckIn) {

                                txtCheckIn.setText(date);

                            } else {

                                txtCheckOut.setText(date);
                            }
                        },

                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

        dialog.show();
    }


    // =====================================================
    // CONFIRM BOOKING
    // =====================================================

    private void confirmBooking() {

        if (roomId == -1) {

            Toast.makeText(
                    this,
                    "Invalid room",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (guests.isEmpty()) {

            Toast.makeText(
                    this,
                    "No guests available",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String checkIn =
                txtCheckIn.getText().toString();

        String checkOut =
                txtCheckOut.getText().toString();

        if (checkIn.equals("Not selected")
                || checkOut.equals("Not selected")) {

            Toast.makeText(
                    this,
                    "Please select check-in and check-out dates",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        int selectedGuestPosition =
                spinnerGuest.getSelectedItemPosition();

        if (selectedGuestPosition < 0
                || selectedGuestPosition >= guests.size()) {

            Toast.makeText(
                    this,
                    "Please select a guest",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int guestId =
                guests.get(selectedGuestPosition)
                        .getGuest_id();

        String paymentMethod =
                spinnerPaymentMethod
                        .getSelectedItem()
                        .toString();

        ReservationRequest request =
                new ReservationRequest(
                        roomId,
                        guestId,
                        checkIn,
                        checkOut,
                        paymentMethod
                );


        // =================================================
        // SEND BOOKING REQUEST
        // =================================================

        RetrofitClient.getApiService()
                .createReservation(request)
                .enqueue(new Callback<ReservationResponse>() {

                    @Override
                    public void onResponse(
                            Call<ReservationResponse> call,
                            Response<ReservationResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            ReservationResponse result =
                                    response.body();

                            // =================================
                            // SHOW BOOKING SUCCESS
                            // =================================

                            new AlertDialog.Builder(
                                    ReservationActivity.this
                            )
                                    .setTitle(
                                            "Booking Confirmed!"
                                    )
                                    .setMessage(
                                            "Reservation ID: "
                                                    + result.getReservation_id()
                                                    + "\n\n"
                                                    + "Amount: ₹"
                                                    + result.getAmount()
                                                    + "\n\n"
                                                    + "Payment: "
                                                    + result.getPayment_status()
                                    )
                                    .setPositiveButton(
                                            "OK",
                                            (dialog, which) -> {
                                                finish();
                                            }
                                    )
                                    .setCancelable(false)
                                    .show();

                        } else {

                            String errorMessage =
                                    "Booking failed\nHTTP Code: "
                                            + response.code();

                            if (response.errorBody() != null) {

                                try {

                                    errorMessage +=
                                            "\n"
                                                    + response
                                                    .errorBody()
                                                    .string();

                                } catch (Exception e) {

                                    errorMessage +=
                                            "\nUnable to read server error";
                                }
                            }

                            Toast.makeText(
                                    ReservationActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ReservationResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                ReservationActivity.this,
                                "Booking connection failed:\n"
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}