package com.example.lena.test1;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private final int REQUEST_ENABLE_BT = 1;

    private TextView Steps;
    private TextView Lvl;
    private Button buttonReset;
    private Button buttonConnect;

    private Button buttonSave;
    private BluetoothAdapter bluetoothAdapterOfDevice;
    ViewGroup container;
    ImageView image;
    SharedPreferences SaveData;
    private static final String SAVED_DATA = "Saved Game";
    private static final String SAVED_STEPS = "Saved Steps";
    private static final String SAVED_LVL = "Saved LVL";
    ArrayList<TextView> STEPSnLEVELS;
    ArrayList<ImageView> PokeImage;
    String SavedSteps, SavedLvl;
    int LvlInt;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Steps = (TextView) findViewById(R.id.StepsView);
        Lvl = (TextView) findViewById(R.id.LvlValueView);

        buttonReset = (Button) findViewById(R.id.ResetButton);
        buttonConnect = (Button) findViewById(R.id.ConnectButton);

        buttonSave = (Button) findViewById(R.id.SaveButton);


        SaveData = getApplicationContext().getSharedPreferences(SAVED_DATA, 0);

        SavedSteps = SaveData.getString(SAVED_STEPS, "0");
        Steps.setText(SavedSteps);
        SavedLvl = SaveData.getString(SAVED_LVL, "0");
        Lvl.setText(SavedLvl);
        image  = (ImageView) findViewById(R.id.PokeIcon);

        LvlInt=Integer.parseInt(SavedLvl);
        switch (LvlInt) {
            case 1:   image.setImageResource(R.drawable.magikarp);
                break;
            case 2:   image.setImageResource(R.drawable.jigglypuff);
                break;
            case 3:   image.setImageResource(R.drawable.meowth);
                break;
            case 4:   image.setImageResource(R.drawable.pikachu);
                break;
            case 5:   image.setImageResource(R.drawable.butterfree);
                break;
            case 6:   image.setImageResource(R.drawable.raticate);
                break;
            case 7:   image.setImageResource(R.drawable.charizard);
                break;
            case 8:   image.setImageResource(R.drawable.mewtwo);
                break;
        }


        setStorage();


        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Jestes pewien?");
        alertDialogBuilder.setMessage("Czy naprawdę chcesz usunąć statystyki i zacząć od zera?");
        alertDialogBuilder.setCancelable(false);


        alertDialogBuilder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Steps.setText("0");
                Lvl.setText("1");
                image  = (ImageView) findViewById(R.id.PokeIcon);
                image.setImageResource(R.drawable.magikarp);
                setButtonSave();
            }
        });


        alertDialogBuilder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ///Toast.makeText(MainActivity.this,"Uff",Toast.LENGTH_SHORT).show();
            }
        });

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setButtonConnect();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setButtonReset();
            }
        });



        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setButtonSave();
            }
        });



    }



    private void setStorage() {
        STEPSnLEVELS = new ArrayList<>();
        STEPSnLEVELS.add(Steps);
        STEPSnLEVELS.add(Lvl);

        PokeImage = new ArrayList<>();
        PokeImage.add(image);
    }



    private void setButtonConnect() {
        setAndCheckIfSupportedBluetoothAdapter();
        if (!isBluetoothEnabled()) {
            intentToEnableBluetooth();
        }
        connectToDevice();
    }



    private void setButtonSave() {

        SharedPreferences.Editor preferencesEditor = SaveData.edit();


        String newSteps = Steps.getText().toString();
        preferencesEditor.putString(SAVED_STEPS, newSteps);

        String newLvl = Lvl.getText().toString();
        preferencesEditor.putString(SAVED_LVL, newLvl);

        preferencesEditor.apply();
        Toast.makeText(getApplicationContext(), "Zapisano", Toast.LENGTH_LONG).show();

    }


    private void setAndCheckIfSupportedBluetoothAdapter() {
        bluetoothAdapterOfDevice = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapterOfDevice == null) {
            Toast.makeText(getApplicationContext(), "Urządzenie nie obsługuje Bluetooth", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isBluetoothEnabled() {
        if (!bluetoothAdapterOfDevice.isEnabled()) {
            return false;
        }
        Toast.makeText(getApplicationContext(), "Bluetooth już uruchomione", Toast.LENGTH_LONG).show();
        return true;
    }

    private void intentToEnableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connectToDevice() {
        BluetoothDevice btDevice = getDevice();
        if (btDevice == null) {
            Toast.makeText(getApplicationContext(), "Nie można połączyć", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "Połączono z " + btDevice.getName(), Toast.LENGTH_LONG).show();
        (new ThreadConnectBTdevice(btDevice, STEPSnLEVELS, PokeImage)).start();
    }

    private BluetoothDevice getDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapterOfDevice.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("HC-06")) {
                    return device;
                }
            }
        }
        return null;
    }

    private void setButtonReset() {

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }






}