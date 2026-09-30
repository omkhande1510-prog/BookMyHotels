package com.example.bookmyhotels;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmyhotels.model.Reservation;
import com.example.bookmyhotels.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationListActivity extends AppCompatActivity {

    private LinearLayout reservationsContainer;

    private final int BLUE = Color.rgb(13, 71, 161);
    private final int LIGHT_BLUE = Color.rgb(227, 242, 253);
    private final int GREEN = Color.rgb(46, 125, 50);
    private final int RED = Color.rgb(198, 40, 40);
    private final int DARK = Color.rgb(31, 41, 55);
    private final int GRAY = Color.rgb(107, 114, 128);
    private final int LIGHT_GRAY = Color.rgb(229, 231, 235);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservation_list);

        reservationsContainer =
                findViewById(R.id.reservationsContainer);

        loadReservations();
    }

    // =========================================================
    // LOAD RESERVATIONS
    // =========================================================

    private void loadReservations() {

        RetrofitClient.getApiService()
                .getReservations()
                .enqueue(new Callback<List<Reservation>>() {

                    @Override
                    public void onResponse(
                            Call<List<Reservation>> call,
                            Response<List<Reservation>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            displayReservations(
                                    response.body()
                            );

                        } else {

                            Toast.makeText(
                                    ReservationListActivity.this,
                                    "Failed to load reservations. HTTP "
                                            + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Reservation>> call,
                            Throwable t) {

                        Toast.makeText(
                                ReservationListActivity.this,
                                "Connection failed:\n"
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    // =========================================================
    // DISPLAY RESERVATIONS
    // =========================================================

    private void displayReservations(
            List<Reservation> reservations) {

        reservationsContainer.removeAllViews();

        if (reservations.isEmpty()) {

            showEmptyReservations();

            return;
        }

        for (Reservation reservation : reservations) {

            addReservationCard(reservation);
        }
    }

    // =========================================================
    // EMPTY STATE
    // =========================================================

    private void showEmptyReservations() {

        LinearLayout emptyLayout =
                new LinearLayout(this);

        emptyLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        emptyLayout.setGravity(
                Gravity.CENTER
        );

        emptyLayout.setPadding(
                30,
                100,
                30,
                100
        );

        TextView icon =
                new TextView(this);

        icon.setText("📅");
        icon.setTextSize(55);
        icon.setGravity(Gravity.CENTER);

        emptyLayout.addView(icon);

        TextView title =
                new TextView(this);

        title.setText(
                "No Reservations Yet"
        );

        title.setTextSize(22);
        title.setTextColor(DARK);
        title.setTypeface(
                null,
                Typeface.BOLD
        );
        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        titleParams.setMargins(
                0,
                15,
                0,
                8
        );

        emptyLayout.addView(
                title,
                titleParams
        );

        TextView message =
                new TextView(this);

        message.setText(
                "Your hotel reservations will appear here."
        );

        message.setTextSize(15);
        message.setTextColor(GRAY);
        message.setGravity(Gravity.CENTER);

        emptyLayout.addView(message);

        reservationsContainer.addView(
                emptyLayout
        );
    }

    // =========================================================
    // RESERVATION CARD
    // =========================================================

    private void addReservationCard(
            Reservation reservation) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                20,
                20,
                20
        );

        GradientDrawable cardBackground =
                new GradientDrawable();

        cardBackground.setColor(
                Color.WHITE
        );

        cardBackground.setCornerRadius(
                28
        );

        card.setBackground(
                cardBackground
        );

        card.setElevation(5);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                18
        );

        // =====================================================
        // TOP ROW
        // =====================================================

        LinearLayout topRow =
                new LinearLayout(this);

        topRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        topRow.setGravity(
                Gravity.CENTER_VERTICAL
        );

        // -----------------------------------------------------
        // HOTEL ICON
        // -----------------------------------------------------

        TextView hotelIcon =
                new TextView(this);

        hotelIcon.setText("🏨");
        hotelIcon.setTextSize(34);
        hotelIcon.setGravity(
                Gravity.CENTER
        );

        GradientDrawable iconBackground =
                new GradientDrawable();

        iconBackground.setColor(
                LIGHT_BLUE
        );

        iconBackground.setCornerRadius(
                20
        );

        hotelIcon.setBackground(
                iconBackground
        );

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        55,
                        55
                );

        topRow.addView(
                hotelIcon,
                iconParams
        );

        // -----------------------------------------------------
        // ROOM INFORMATION
        // -----------------------------------------------------

        LinearLayout roomInfo =
                new LinearLayout(this);

        roomInfo.setOrientation(
                LinearLayout.VERTICAL
        );

        LinearLayout.LayoutParams roomInfoParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        roomInfoParams.setMargins(
                12,
                0,
                8,
                0
        );

        // ROOM NUMBER

        TextView roomNumber =
                new TextView(this);

        roomNumber.setText(
                "Room "
                        + String.valueOf(
                        reservation.getRoom_number()
                )
        );

        roomNumber.setTextSize(21);
        roomNumber.setTextColor(BLUE);

        roomNumber.setTypeface(
                null,
                Typeface.BOLD
        );

        roomInfo.addView(
                roomNumber
        );

        // ROOM TYPE

        TextView roomType =
                new TextView(this);

        roomType.setText(
                safe(
                        reservation.getRoom_type()
                )
                        + " Room"
        );

        roomType.setTextSize(14);
        roomType.setTextColor(GRAY);

        LinearLayout.LayoutParams typeParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        typeParams.setMargins(
                0,
                3,
                0,
                0
        );

        roomInfo.addView(
                roomType,
                typeParams
        );

        topRow.addView(
                roomInfo,
                roomInfoParams
        );

        // -----------------------------------------------------
        // STATUS BADGE
        // -----------------------------------------------------

        TextView status =
                createStatusBadge(
                        reservation.getStatus()
                );

        topRow.addView(
                status
        );

        card.addView(
                topRow
        );

        // =====================================================
        // DIVIDER
        // =====================================================

        addDivider(card);

        // =====================================================
        // DATE SECTION
        // =====================================================

        LinearLayout dateRow =
                new LinearLayout(this);

        dateRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView checkIn =
                createInfoText(
                        "CHECK-IN",
                        formatDate(
                                reservation.getCheck_in()
                        )
                );

        TextView checkOut =
                createInfoText(
                        "CHECK-OUT",
                        formatDate(
                                reservation.getCheck_out()
                        )
                );

        dateRow.addView(
                checkIn,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        dateRow.addView(
                checkOut,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        card.addView(
                dateRow
        );

        // =====================================================
        // GUEST
        // =====================================================

        addSectionTitle(
                card,
                "GUEST"
        );

        TextView guest =
                new TextView(this);

        guest.setText(
                "👤  "
                        + safe(
                        reservation.getGuest_name()
                )
        );

        guest.setTextSize(16);
        guest.setTextColor(DARK);

        guest.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                guest
        );

        // =====================================================
        // DIVIDER
        // =====================================================

        addDivider(card);

        // =====================================================
        // PAYMENT / AMOUNT
        // =====================================================

        LinearLayout paymentRow =
                new LinearLayout(this);

        paymentRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        // -----------------------------------------------------
        // PAYMENT INFORMATION
        // -----------------------------------------------------

        LinearLayout paymentInfo =
                new LinearLayout(this);

        paymentInfo.setOrientation(
                LinearLayout.VERTICAL
        );

        TextView paymentTitle =
                new TextView(this);

        paymentTitle.setText(
                "PAYMENT"
        );

        paymentTitle.setTextSize(12);
        paymentTitle.setTextColor(GRAY);

        paymentTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        paymentInfo.addView(
                paymentTitle
        );

        TextView payment =
                new TextView(this);

        payment.setText(
                "💳 "
                        + safe(
                        reservation.getPayment_method()
                )
        );

        payment.setTextSize(15);
        payment.setTextColor(DARK);

        paymentInfo.addView(
                payment
        );

        TextView paymentStatus =
                new TextView(this);

        paymentStatus.setText(
                safe(
                        reservation.getPayment_status()
                )
        );

        paymentStatus.setTextSize(13);
        paymentStatus.setTextColor(GREEN);

        paymentStatus.setTypeface(
                null,
                Typeface.BOLD
        );

        paymentInfo.addView(
                paymentStatus
        );

        paymentRow.addView(
                paymentInfo,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        // -----------------------------------------------------
        // AMOUNT
        // -----------------------------------------------------

        LinearLayout amountInfo =
                new LinearLayout(this);

        amountInfo.setOrientation(
                LinearLayout.VERTICAL
        );

        amountInfo.setGravity(
                Gravity.END
        );

        TextView amountTitle =
                new TextView(this);

        amountTitle.setText(
                "TOTAL AMOUNT"
        );

        amountTitle.setTextSize(12);
        amountTitle.setTextColor(GRAY);

        amountTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        amountInfo.addView(
                amountTitle
        );

        TextView amount =
                new TextView(this);

        amount.setText(
                "₹"
                        + String.format(
                        "%.2f",
                        reservation.getAmount()
                )
        );

        amount.setTextSize(21);
        amount.setTextColor(GREEN);

        amount.setTypeface(
                null,
                Typeface.BOLD
        );

        amountInfo.addView(
                amount
        );

        paymentRow.addView(
                amountInfo,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        card.addView(
                paymentRow
        );

        // =====================================================
        // RESERVATION ID
        // =====================================================

        TextView reservationId =
                new TextView(this);

        reservationId.setText(
                "Reservation #"
                        + reservation.getReservation_id()
        );

        reservationId.setTextSize(12);
        reservationId.setTextColor(GRAY);
        reservationId.setGravity(
                Gravity.END
        );

        LinearLayout.LayoutParams idParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        idParams.setMargins(
                0,
                15,
                0,
                5
        );

        card.addView(
                reservationId,
                idParams
        );

        // =====================================================
        // CANCEL BUTTON
        // =====================================================

        Button cancelButton =
                new Button(this);

        cancelButton.setText(
                "CANCEL RESERVATION"
        );

        cancelButton.setTextSize(14);
        cancelButton.setTextColor(Color.WHITE);

        cancelButton.setTypeface(
                null,
                Typeface.BOLD
        );

        cancelButton.setAllCaps(false);

        GradientDrawable buttonBackground =
                new GradientDrawable();

        buttonBackground.setColor(
                RED
        );

        buttonBackground.setCornerRadius(
                18
        );

        cancelButton.setBackground(
                buttonBackground
        );

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        52
                );

        buttonParams.setMargins(
                0,
                8,
                0,
                0
        );

        cancelButton.setLayoutParams(
                buttonParams
        );

        // -----------------------------------------------------
        // ALREADY CANCELLED
        // -----------------------------------------------------

        if ("Cancelled".equalsIgnoreCase(
                reservation.getStatus()
        )) {

            cancelButton.setEnabled(
                    false
            );

            cancelButton.setText(
                    "✓ RESERVATION CANCELLED"
            );

            cancelButton.setTextColor(
                    GRAY
            );

            GradientDrawable disabledBackground =
                    new GradientDrawable();

            disabledBackground.setColor(
                    LIGHT_GRAY
            );

            disabledBackground.setCornerRadius(
                    18
            );

            cancelButton.setBackground(
                    disabledBackground
            );

        } else {

            cancelButton.setOnClickListener(
                    v -> showCancelDialog(
                            reservation
                    )
            );
        }

        card.addView(
                cancelButton
        );

        // =====================================================
        // ADD CARD
        // =====================================================

        reservationsContainer.addView(
                card,
                cardParams
        );
    }

    // =========================================================
    // STATUS BADGE
    // =========================================================

    private TextView createStatusBadge(
            String reservationStatus) {

        TextView badge =
                new TextView(this);

        String status =
                safe(reservationStatus);

        GradientDrawable background =
                new GradientDrawable();

        background.setCornerRadius(
                30
        );

        if (status.equalsIgnoreCase(
                "Cancelled"
        )) {

            badge.setText(
                    "CANCELLED"
            );

            badge.setTextColor(
                    RED
            );

            background.setColor(
                    Color.rgb(
                            255,
                            235,
                            238
                    )
            );

        } else {

            badge.setText(
                    "CONFIRMED"
            );

            badge.setTextColor(
                    GREEN
            );

            background.setColor(
                    Color.rgb(
                            232,
                            245,
                            233
                    )
            );
        }

        badge.setBackground(
                background
        );

        badge.setTextSize(11);

        badge.setTypeface(
                null,
                Typeface.BOLD
        );

        badge.setGravity(
                Gravity.CENTER
        );

        badge.setPadding(
                18,
                8,
                18,
                8
        );

        return badge;
    }

    // =========================================================
    // INFO TEXT
    // =========================================================

    private TextView createInfoText(
            String title,
            String value) {

        TextView text =
                new TextView(this);

        text.setText(
                title
                        + "\n"
                        + value
        );

        text.setTextSize(14);
        text.setTextColor(DARK);

        text.setTypeface(
                null,
                Typeface.BOLD
        );

        text.setPadding(
                0,
                5,
                0,
                8
        );

        return text;
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================

    private void addSectionTitle(
            LinearLayout parent,
            String title) {

        TextView text =
                new TextView(this);

        text.setText(title);
        text.setTextSize(12);
        text.setTextColor(GRAY);

        text.setTypeface(
                null,
                Typeface.BOLD
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                14,
                0,
                5
        );

        parent.addView(
                text,
                params
        );
    }

    // =========================================================
    // DIVIDER
    // =========================================================

    private void addDivider(
            LinearLayout parent) {

        View divider =
                new View(this);

        divider.setBackgroundColor(
                Color.rgb(
                        235,
                        238,
                        242
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );

        params.setMargins(
                0,
                15,
                0,
                10
        );

        parent.addView(
                divider,
                params
        );
    }

    // =========================================================
    // FORMAT DATE
    // =========================================================

    private String formatDate(
            String date) {

        if (date == null
                || date.isEmpty()) {

            return "Not available";
        }

        /*
         * Flask response example:
         *
         * Fri, 09 Oct 2026 00:00:00 GMT
         *
         * Result:
         *
         * 09 Oct 2026
         */

        try {

            String[] parts =
                    date.split(" ");

            if (parts.length >= 4) {

                return parts[1]
                        + " "
                        + parts[2]
                        + " "
                        + parts[3];
            }

        } catch (Exception ignored) {
        }

        return date;
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(
            String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    // =========================================================
    // CANCEL CONFIRMATION
    // =========================================================

    private void showCancelDialog(
            Reservation reservation) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Cancel Reservation?"
                )

                .setMessage(
                        "Are you sure you want to cancel "
                                + "Reservation #"
                                + reservation.getReservation_id()
                                + "?"
                )

                .setNegativeButton(
                        "NO",
                        null
                )

                .setPositiveButton(
                        "YES, CANCEL",
                        (dialog, which) -> {

                            cancelReservation(
                                    reservation.getReservation_id()
                            );
                        }
                )

                .show();
    }

    // =========================================================
    // CANCEL RESERVATION
    // =========================================================

    private void cancelReservation(
            int reservationId) {

        RetrofitClient.getApiService()
                .cancelReservation(
                        reservationId
                )
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    ReservationListActivity.this,
                                    "Reservation cancelled successfully",
                                    Toast.LENGTH_LONG
                            ).show();

                            loadReservations();

                        } else {

                            Toast.makeText(
                                    ReservationListActivity.this,
                                    "Cancellation failed. HTTP "
                                            + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {

                        Toast.makeText(
                                ReservationListActivity.this,
                                "Connection failed:\n"
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    // =========================================================
    // RELOAD WHEN RETURNING TO SCREEN
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        loadReservations();
    }
}