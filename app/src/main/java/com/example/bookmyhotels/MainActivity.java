package com.example.bookmyhotels;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmyhotels.model.Room;
import com.example.bookmyhotels.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerRoomType;
    private LinearLayout roomsContainer;

    private List<Room> allRooms = new ArrayList<>();

    // =========================================================
    // COLORS
    // =========================================================

    private final int BLUE =
            Color.rgb(21, 101, 192);

    private final int DARK_BLUE =
            Color.rgb(13, 71, 161);

    private final int GREEN =
            Color.rgb(46, 125, 50);

    private final int LIGHT_GREEN =
            Color.rgb(232, 245, 233);

    private final int RED =
            Color.rgb(198, 40, 40);

    private final int LIGHT_RED =
            Color.rgb(255, 235, 238);

    private final int GRAY_TEXT =
            Color.rgb(107, 114, 128);

    private final int DARK_TEXT =
            Color.rgb(31, 41, 55);

    private final int LIGHT_BLUE =
            Color.rgb(227, 242, 253);


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Find views
        spinnerRoomType =
                findViewById(R.id.spinnerRoomType);

        roomsContainer =
                findViewById(R.id.roomsContainer);

        Button btnMyReservations =
                findViewById(R.id.btnMyReservations);


        // =====================================================
        // MY RESERVATIONS
        // =====================================================

        btnMyReservations.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            ReservationListActivity.class
                    );

            startActivity(intent);
        });


        // Setup room filter
        setupSpinner();

        // Load rooms from Flask API
        loadRooms();
    }


    // =========================================================
    // ROOM TYPE FILTER
    // =========================================================

    private void setupSpinner() {

        String[] roomTypes = {
                "All",
                "Single",
                "Double",
                "Deluxe",
                "Suite"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        roomTypes
                );


        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerRoomType.setAdapter(adapter);


        spinnerRoomType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        String selectedType =
                                parent
                                        .getItemAtPosition(position)
                                        .toString();

                        displayRooms(selectedType);
                    }


                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                }
        );
    }


    // =========================================================
    // LOAD ROOMS FROM FLASK API
    // =========================================================

    private void loadRooms() {

        RetrofitClient.getApiService()
                .getRooms()
                .enqueue(new Callback<List<Room>>() {

                    @Override
                    public void onResponse(
                            Call<List<Room>> call,
                            Response<List<Room>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            allRooms =
                                    response.body();

                            displayRooms("All");

                        } else {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Failed to load rooms. HTTP "
                                            + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }


                    @Override
                    public void onFailure(
                            Call<List<Room>> call,
                            Throwable t) {

                        Toast.makeText(
                                MainActivity.this,
                                "Connection failed:\n"
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }


    // =========================================================
    // DISPLAY ROOMS
    // =========================================================

    private void displayRooms(
            String selectedType) {

        roomsContainer.removeAllViews();


        // Loop through rooms
        for (Room room : allRooms) {

            // -------------------------------------------------
            // FILTER ROOM TYPE
            // -------------------------------------------------

            if (!selectedType.equals("All")
                    && room.getRoom_type() != null
                    && !room.getRoom_type()
                    .equalsIgnoreCase(selectedType)) {

                continue;
            }


            // =================================================
            // ROOM CARD
            // =================================================

            LinearLayout card =
                    new LinearLayout(this);

            card.setOrientation(
                    LinearLayout.VERTICAL
            );


            card.setPadding(
                    22,
                    20,
                    22,
                    20
            );


            // =================================================
            // CARD BACKGROUND
            // =================================================

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


            // =================================================
            // CARD MARGINS
            // =================================================

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


            // =================================================
            // TOP ROW
            // =================================================

            LinearLayout topRow =
                    new LinearLayout(this);

            topRow.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            topRow.setGravity(
                    Gravity.CENTER_VERTICAL
            );


            // =================================================
            // HOTEL ICON
            // =================================================

            TextView hotelIcon =
                    new TextView(this);

            hotelIcon.setText("🏨");
            hotelIcon.setTextSize(30);
            hotelIcon.setGravity(
                    Gravity.CENTER
            );


            GradientDrawable iconBackground =
                    new GradientDrawable();

            iconBackground.setColor(
                    LIGHT_BLUE
            );

            iconBackground.setCornerRadius(
                    22
            );


            hotelIcon.setBackground(
                    iconBackground
            );


            LinearLayout.LayoutParams iconParams =
                    new LinearLayout.LayoutParams(
                            58,
                            58
                    );


            hotelIcon.setLayoutParams(
                    iconParams
            );


            topRow.addView(
                    hotelIcon
            );


            // =================================================
            // ROOM TITLE SECTION
            // =================================================

            LinearLayout titleSection =
                    new LinearLayout(this);

            titleSection.setOrientation(
                    LinearLayout.VERTICAL
            );


            LinearLayout.LayoutParams titleParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    );


            titleParams.setMargins(
                    14,
                    0,
                    10,
                    0
            );


            titleSection.setLayoutParams(
                    titleParams
            );


            // Room number
            TextView roomTitle =
                    new TextView(this);

            roomTitle.setText(
                    "Room "
                            + room.getRoom_number()
            );

            roomTitle.setTextSize(21);

            roomTitle.setTextColor(
                    DARK_BLUE
            );

            roomTitle.setTypeface(
                    null,
                    Typeface.BOLD
            );


            titleSection.addView(
                    roomTitle
            );


            // Room type
            String type =
                    room.getRoom_type();


            if (type == null
                    || type.isEmpty()) {

                type = "Room";
            }


            TextView roomType =
                    new TextView(this);

            roomType.setText(
                    type + " Room"
            );

            roomType.setTextSize(14);

            roomType.setTextColor(
                    GRAY_TEXT
            );

            roomType.setPadding(
                    0,
                    3,
                    0,
                    0
            );


            titleSection.addView(
                    roomType
            );


            topRow.addView(
                    titleSection
            );


            // =================================================
            // STATUS BADGE
            // =================================================

            String roomStatus =
                    room.getStatus();


            if (roomStatus == null
                    || roomStatus.isEmpty()) {

                roomStatus = "Unknown";
            }


            TextView status =
                    new TextView(this);


            status.setText(
                    "● " + roomStatus
            );


            status.setTextSize(12);

            status.setTypeface(
                    null,
                    Typeface.BOLD
            );


            status.setGravity(
                    Gravity.CENTER
            );


            status.setPadding(
                    12,
                    7,
                    12,
                    7
            );


            GradientDrawable statusBackground =
                    new GradientDrawable();


            statusBackground.setCornerRadius(
                    30
            );


            if (roomStatus.equalsIgnoreCase(
                    "Available")) {

                status.setTextColor(
                        GREEN
                );

                statusBackground.setColor(
                        LIGHT_GREEN
                );

            } else {

                status.setTextColor(
                        RED
                );

                statusBackground.setColor(
                        LIGHT_RED
                );
            }


            status.setBackground(
                    statusBackground
            );


            topRow.addView(
                    status
            );


            card.addView(
                    topRow
            );


            // =================================================
            // DIVIDER
            // =================================================

            View divider =
                    new View(this);


            divider.setBackgroundColor(
                    Color.rgb(
                            238,
                            240,
                            244
                    )
            );


            LinearLayout.LayoutParams dividerParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                    );


            dividerParams.setMargins(
                    0,
                    16,
                    0,
                    14
            );


            card.addView(
                    divider,
                    dividerParams
            );


            // =================================================
            // PRICE ROW
            // =================================================

            LinearLayout priceRow =
                    new LinearLayout(this);


            priceRow.setOrientation(
                    LinearLayout.HORIZONTAL
            );


            priceRow.setGravity(
                    Gravity.CENTER_VERTICAL
            );


            // Price label
            TextView priceLabel =
                    new TextView(this);


            priceLabel.setText(
                    "Price per night"
            );


            priceLabel.setTextSize(14);

            priceLabel.setTextColor(
                    GRAY_TEXT
            );


            LinearLayout.LayoutParams priceLabelParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    );


            priceLabel.setLayoutParams(
                    priceLabelParams
            );


            // Price
            TextView price =
                    new TextView(this);


            price.setText(
                    "₹"
                            + String.format(
                            "%.0f",
                            room.getBase_price()
                    )
            );


            price.setTextSize(21);

            price.setTextColor(
                    GREEN
            );


            price.setTypeface(
                    null,
                    Typeface.BOLD
            );


            // Night
            TextView night =
                    new TextView(this);


            night.setText(
                    " / night"
            );


            night.setTextSize(13);

            night.setTextColor(
                    GRAY_TEXT
            );


            priceRow.addView(
                    priceLabel
            );


            priceRow.addView(
                    price
            );


            priceRow.addView(
                    night
            );


            card.addView(
                    priceRow
            );


            // =================================================
            // BOOK NOW BUTTON
            // =================================================

            Button bookButton =
                    new Button(this);


            bookButton.setText(
                    "BOOK NOW"
            );


            bookButton.setTextSize(14);

            bookButton.setTextColor(
                    Color.WHITE
            );


            bookButton.setTypeface(
                    null,
                    Typeface.BOLD
            );


            bookButton.setAllCaps(
                    false
            );


            bookButton.setBackgroundTintList(
                    ColorStateList.valueOf(
                            BLUE
                    )
            );


            LinearLayout.LayoutParams buttonParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            52
                    );


            buttonParams.setMargins(
                    0,
                    16,
                    0,
                    0
            );


            bookButton.setLayoutParams(
                    buttonParams
            );


            // =================================================
            // OPEN BOOKING WITH CONFIRMATION DIALOG
            // =================================================

            View.OnClickListener openBooking =
                    v -> {

                        // -----------------------------------------
                        // Room information
                        // -----------------------------------------

                        String dialogRoomType =
                                room.getRoom_type();


                        if (dialogRoomType == null
                                || dialogRoomType.isEmpty()) {

                            dialogRoomType = "Room";
                        }


                        String dialogRoomNumber =
                                "Room "
                                        + room.getRoom_number();


                        String dialogPrice =
                                "₹"
                                        + String.format(
                                        "%.0f",
                                        room.getBase_price()
                                )
                                        + " / night";


                        // -----------------------------------------
                        // Inflate custom dialog layout
                        // -----------------------------------------

                        View dialogView =
                                LayoutInflater
                                        .from(MainActivity.this)
                                        .inflate(
                                                R.layout.dialog_room_confirmation,
                                                null
                                        );


                        // -----------------------------------------
                        // Find dialog views
                        // -----------------------------------------

                        TextView txtRoomNumber =
                                dialogView.findViewById(
                                        R.id.txtDialogRoomNumber
                                );


                        TextView txtRoomType =
                                dialogView.findViewById(
                                        R.id.txtDialogRoomType
                                );


                        TextView txtPrice =
                                dialogView.findViewById(
                                        R.id.txtDialogPrice
                                );


                        // -----------------------------------------
                        // Set dialog information
                        // -----------------------------------------

                        txtRoomNumber.setText(
                                dialogRoomNumber
                        );


                        txtRoomType.setText(
                                dialogRoomType
                                        + " Room"
                        );


                        txtPrice.setText(
                                dialogPrice
                        );


                        // -----------------------------------------
                        // Create dialog
                        // -----------------------------------------

                        AlertDialog dialog =
                                new AlertDialog.Builder(
                                        MainActivity.this
                                )
                                        .setView(
                                                dialogView
                                        )
                                        .setNegativeButton(
                                                "CANCEL",
                                                null
                                        )
                                        .setPositiveButton(
                                                "CONTINUE",
                                                null
                                        )
                                        .create();


                        // -----------------------------------------
                        // Show dialog
                        // -----------------------------------------

                        dialog.show();


                        // -----------------------------------------
                        // Style buttons
                        // -----------------------------------------

                        Button cancelButton =
                                dialog.getButton(
                                        AlertDialog.BUTTON_NEGATIVE
                                );


                        Button continueButton =
                                dialog.getButton(
                                        AlertDialog.BUTTON_POSITIVE
                                );


                        cancelButton.setTextColor(
                                GRAY_TEXT
                        );


                        cancelButton.setTextSize(
                                14
                        );


                        cancelButton.setTypeface(
                                null,
                                Typeface.BOLD
                        );


                        continueButton.setTextColor(
                                BLUE
                        );


                        continueButton.setTextSize(
                                14
                        );


                        continueButton.setTypeface(
                                null,
                                Typeface.BOLD
                        );


                        // -----------------------------------------
                        // CONTINUE
                        // -----------------------------------------

                        continueButton.setOnClickListener(
                                view -> {

                                    Intent intent =
                                            new Intent(
                                                    MainActivity.this,
                                                    ReservationActivity.class
                                            );


                                    // IMPORTANT:
                                    // Existing intent extras unchanged
                                    intent.putExtra(
                                            "room_id",
                                            room.getRoom_id()
                                    );


                                    intent.putExtra(
                                            "room_number",
                                            room.getRoom_number()
                                    );


                                    startActivity(
                                            intent
                                    );


                                    dialog.dismiss();
                                }
                        );
                    };


            // Button click
            bookButton.setOnClickListener(
                    openBooking
            );


            // =================================================
            // AVAILABLE CHECK
            // =================================================

            boolean isAvailable =
                    room.getStatus() != null
                            && room.getStatus()
                            .equalsIgnoreCase(
                                    "Available"
                            );


            if (isAvailable) {

                bookButton.setEnabled(
                        true
                );


                bookButton.setText(
                        "BOOK NOW"
                );


                card.setClickable(
                        true
                );


                card.setFocusable(
                        true
                );


                card.setOnClickListener(
                        openBooking
                );

            } else {

                bookButton.setEnabled(
                        false
                );


                bookButton.setText(
                        "NOT AVAILABLE"
                );


                card.setClickable(
                        false
                );
            }


            // Add button
            card.addView(
                    bookButton
            );


            // Add card
            roomsContainer.addView(
                    card,
                    cardParams
            );
        }


        // =====================================================
        // NO ROOMS
        // =====================================================

        if (roomsContainer.getChildCount() == 0) {

            TextView noRooms =
                    new TextView(this);


            noRooms.setText(
                    "🏨\n\nNo rooms available for this type."
            );


            noRooms.setTextSize(
                    18
            );


            noRooms.setTextColor(
                    GRAY_TEXT
            );


            noRooms.setGravity(
                    Gravity.CENTER
            );


            noRooms.setPadding(
                    20,
                    60,
                    20,
                    60
            );


            roomsContainer.addView(
                    noRooms
            );
        }
    }
}