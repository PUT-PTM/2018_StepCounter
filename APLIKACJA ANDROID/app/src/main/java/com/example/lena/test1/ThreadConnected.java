package com.example.lena.test1;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import android.widget.ImageView;

public class ThreadConnected extends Thread {

    private static final String TAG = "3";

    private final InputStream inStream;
    private ArrayList STEPSnLEVEL;
    private ArrayList PokeImages;

    private int StepsInt;
    private int LvlProgress=0;
    public static int LvlValue=1;
    private TextView Steps;
    private TextView Lvl;
    private ImageView image;


    public ThreadConnected(BluetoothSocket socket, ArrayList STEPSnLEVELS, ArrayList PokeImage) {

        this.STEPSnLEVEL = STEPSnLEVELS;
        Steps = (TextView) STEPSnLEVEL.get(0);
        Lvl = (TextView) STEPSnLEVEL.get(1);

        this.PokeImages = PokeImage;
        image = (ImageView) PokeImages.get(0);

        InputStream input = null;

        try {
            input = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        inStream = input;
    }


    public void run() {


        while (true) {
            try {
                byte[] buffer = new byte[32];
                inStream.read(buffer);
                AddSteps();
            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    private void AddSteps() {

        StepsInt = Integer.parseInt(Steps.getText().toString());
        StepsInt += 1;


        CheckLvlUp();


        Steps.post(new Runnable() {
            public void run() {
                Steps.setText(Integer.toString(StepsInt));
                Lvl.setText(Integer.toString(LvlValue));
            }
        });
    }



    private void CheckLvlUp() {
        LvlValue= Integer.parseInt(Lvl.getText().toString());
        LvlProgress=LvlProgress+1;

        if (LvlValue==1 && LvlProgress>=5 || LvlValue>1 && LvlProgress>=LvlValue*LvlValue*2){ /////////jakas sensowna funkcja sprawdzajaca czy jest lvl up
            LvlValue=LvlValue+1;
            LvlProgress=0;


           image.setImageResource(R.drawable.lvl_up);


            } else {
            ///////case
            switch (LvlValue) {
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
        }
    }


}