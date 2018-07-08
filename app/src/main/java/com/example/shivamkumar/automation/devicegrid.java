package com.example.shivamkumar.automation;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;

public class devicegrid extends AppCompatActivity {

    //bluetooth
    private BluetoothAdapter btAdapter = null;
    Handler bluetoothIn;
    final int handlerState = 0;                        //used to identify handler message
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    BluetoothConnectionService mBluetoothConnection;
    String Input;
    //bluetooth

    GridLayout mainGrid;
    private static String address;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicegrid);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        TextView txt = (TextView) findViewById(R.id.textGrid);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/Glasoor.ttf");
        txt.setTypeface(custom_font);
        Toast.makeText(getBaseContext(), "Click on the Icons to Turn Device On or OFF!",
                Toast.LENGTH_LONG).show();

        onclick();
        //Starting Bluetooth Sending Proc
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //Ending Bluetooth Sending Proc

    }


    public void onclick() {
        final CardView cardview1 = (CardView) findViewById(R.id.cardview1);
        final CardView cardview2 = (CardView) findViewById(R.id.cardview2);
        final CardView cardview3 = (CardView) findViewById(R.id.cardview3);
        final CardView cardview4 = (CardView) findViewById(R.id.cardview4);
        final CardView cardview5 = (CardView) findViewById(R.id.cardview5);
        final CardView cardview6 = (CardView) findViewById(R.id.cardview6);
        final CardView cardview7 = (CardView) findViewById(R.id.cardview7);
        final CardView cardview8 = (CardView) findViewById(R.id.cardview8);
        final CardView cardview9 = (CardView) findViewById(R.id.cardview9);
        final CardView cardview10 = (CardView) findViewById(R.id.cardview10);
        final CardView cardview11 = (CardView) findViewById(R.id.cardview11);
        final CardView cardview12 = (CardView) findViewById(R.id.cardview12);
        cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview2.setVisibility(View.VISIBLE);
                cardview1.setVisibility(View.INVISIBLE);
                sendforon();

            }
        });
        cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview1.setVisibility(View.VISIBLE);
                cardview2.setVisibility(View.INVISIBLE);
                sendforoff();

            }
        });
        cardview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview4.setVisibility(View.VISIBLE);
                cardview3.setVisibility(View.INVISIBLE);
                sendforon();

            }
        });
        cardview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview3.setVisibility(View.VISIBLE);
                cardview4.setVisibility(View.INVISIBLE);
                sendforoff();

            }
        });
        cardview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview6.setVisibility(View.VISIBLE);
                cardview5.setVisibility(View.INVISIBLE);
                sendforon();

            }
        });
        cardview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview5.setVisibility(View.VISIBLE);
                cardview6.setVisibility(View.INVISIBLE);
                sendforoff();

            }
        });
        cardview7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview8.setVisibility(View.VISIBLE);
                cardview7.setVisibility(View.INVISIBLE);
                sendforon();

            }
        });
        cardview8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview7.setVisibility(View.VISIBLE);
                cardview8.setVisibility(View.INVISIBLE);
                sendforoff();

            }
        });
        cardview9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview10.setVisibility(View.VISIBLE);
                cardview9.setVisibility(View.INVISIBLE);
                sendforon();

            }
        });
        cardview10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview9.setVisibility(View.VISIBLE);
                cardview10.setVisibility(View.INVISIBLE);
                sendforoff();

            }
        });
        cardview11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview12.setVisibility(View.VISIBLE);
                cardview11.setVisibility(View.INVISIBLE);
                sendforon();

            }
        });
        cardview12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview11.setVisibility(View.VISIBLE);
                cardview12.setVisibility(View.INVISIBLE);
                sendforoff();
            }
        });


    }

    public void sendforon() {
        String on = "A";
        try {
            byte[] bytes = on.getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "An Error Occured!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void sendforoff() {
        String off = "a";
        try {
            byte[] bytes = off.getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "An Error Occured!",
                    Toast.LENGTH_LONG).show();
        }
    }


}