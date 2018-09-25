package com.example.lena.test1;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


import java.util.ArrayList;
import java.util.UUID;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;


public class ThreadConnectBTdevice extends Thread {
    private static final String TAG = "2";

    private ArrayList<TextView> STEPSnLEVEL;
    ArrayList<ImageView> PokeImage;
    private BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;

    private final UUID MY_UUID = UUID.fromString("5d33df8a-2155-4dec-8f0d-be9fdabb3b0c");

    public ThreadConnectBTdevice(BluetoothDevice device, ArrayList STEPSnLEVELS,ArrayList PokeImage) {
        BluetoothSocket temporaryBtS = null;
        bluetoothDevice = device;
        try {
            temporaryBtS = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        this.STEPSnLEVEL = STEPSnLEVELS;
        this.PokeImage = PokeImage;
        bluetoothSocket = temporaryBtS;
    }

    public void run() {
        try {
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            tryToConnectAgain();
        }

        (new ThreadConnected(bluetoothSocket, STEPSnLEVEL, PokeImage)).start();
    }

    private void tryToConnectAgain() {
        try {
            bluetoothSocket = (BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(bluetoothDevice, 1);
            bluetoothSocket.connect();
        } catch (Exception ex) {
            cancel();
        }
    }

    private void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}