package com.google.mlkit.vision.demo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.mlkit.vision.demo.R;

import java.util.ArrayList;
import java.util.List;

public class gyroscope extends Activity {
    TextView textX, textY;
    Button b,b2;
    SensorManager sensorManager;
    Sensor sensor;
    int flag=0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        b=findViewById(R.id.b);
        b2=findViewById(R.id.b2);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
            }
        });
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            ArrayList<Double> list = new ArrayList<Double>();
            Double[]c={-0.072090656,////////////////////////////////////////x
                    0.12855415,
                    -0.066592865,
                    0.08579358,///yyyyyyyyy
                    0.10567114,
                    -0.08280523,//////////////////y
                    0.11055806,
                    -0.013777454,
                    0.23150937,
                    -0.22330423,//////////////////////////y
                    -0.19304198,/////////////////////////////////////////////////x
                    0.008213693,
                    0.011597888,
                    0.074798,
                    0.05863451,
                    0.15787569,//yyy
                    0.47463375,//akbr xxxx
                    -0.17565674,
                    0.32741523,
                    0.3191441,//y
                    -0.05804075,
                    0.3472439,//yyyyy
                    0.23578542,
                    0.42604554,//yyyyy
                    0.116055846,
                    0.05341772,
                    0.2852655,
                    -0.38640523,///////////////////////////////y
                    -0.37691242,///////////////////////////as8r x
                    0.054639455,
                    -0.20892447,
                    -0.3131014,
                    -0.29444557,
                    -0.16099598,
                    -0.11362948,
                    -0.14083743,
                    -0.0079498,
                    -0.088303015,
                    0.045195475,
                    -0.012555724,
                    0.72386676,////akbr xxxx
                    -0.8256174,////////////////////////////////////////////y
                    0.09101037,
                    -0.22269337,
                    -0.10507738,
                    -0.11762455,
                    -0.54551125,/////////////////////////////////////////////////////////////////////7
                    -0.036379468,
                    -0.14600535,
                    -0.047375042,
                    -0.21747658,
                    -0.1677155,
                    0.16248159,
                    0.21468614,
                    0.25411138,
                    0.27394006,
                    0.5827569,
                    0.20307972,
                    0.55893314,
                    -0.650299,
                    0.50762045,
                    -1.8915772,///////as8r rkm yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                    -0.25473937,
                    -0.14755695,
                    -0.52229834,///////////////////////////////////////////////////////////////8
                    0.16215174,
                    -0.809405,///////as8rrrr rkm//////////////////////////////////////////////////////////////////////////o
                    0.32525274,
                    0.05496932,
                    0.26783141,
                    0.02931298,
                    0.013100617};


            if(flag==1) {

                double x = event.values[0];
                double y = event.values[1];

                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    x = event.values[0];
                    y = event.values[1];

                    list.add(x);
                    list.add(y);

                    Double[] item = list.toArray(new Double[list.size()]);
                    DTW dtn = new DTW(c, item);
                    if(dtn.warpingDistance<0.14 &&dtn.warpingDistance >0.129) {
                        textX.setText("Good");
                    }
                    else
                    {
                        textX.setText("Bad");

                    }
                }

            }




        }


    };
}
class DTW {


    protected Double[] seq1;
    protected Double[] seq2;
    protected int[][] warpingPath;

    protected int n;
    protected int m;
    protected int K;

    protected double warpingDistance;

    public DTW(Double[] sample, Double[] templete) {
        seq1 = sample;
        seq2 = templete;

        n = seq1.length;
        m = seq2.length;
        K = 1;

        warpingPath = new int[n + m][2];        // max(n, m) <= K < n + m
        warpingDistance = 0.0;

        this.compute();
    }

    public DTW(double list, double listtrue) {

    }

    public void compute() {
        double accumulatedDistance = 0.0;

        double[][] d = new double[n][m];        // local distances
        double[][] D = new double[n][m];        // global distances

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                d[i][j] = distanceBetween(seq1[i], seq2[j]);
            }
        }

        D[0][0] = d[0][0];

        for (int i = 1; i < n; i++) {
            D[i][0] = d[i][0] + D[i - 1][0];
        }

        for (int j = 1; j < m; j++) {
            D[0][j] = d[0][j] + D[0][j - 1];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                accumulatedDistance = Math.min(Math.min(D[i-1][j], D[i-1][j-1]), D[i][j-1]);
                accumulatedDistance += d[i][j];
                D[i][j] = accumulatedDistance;
            }
        }
        accumulatedDistance = D[n - 1][m - 1];

        int i = n - 1;
        int j = m - 1;
        int minIndex = 1;

        warpingPath[K - 1][0] = i;
        warpingPath[K - 1][1] = j;

        while ((i + j) != 0) {
            if (i == 0) {
                j -= 1;
            } else if (j == 0) {
                i -= 1;
            } else {        // i != 0 && j != 0
                double[] array = { D[i - 1][j], D[i][j - 1], D[i - 1][j - 1] };
                minIndex = this.getIndexOfMinimum(array);

                if (minIndex == 0) {
                    i -= 1;
                } else if (minIndex == 1) {
                    j -= 1;
                } else if (minIndex == 2) {
                    i -= 1;
                    j -= 1;
                }
            } // end else
            K++;
            warpingPath[K - 1][0] = i;
            warpingPath[K - 1][1] = j;
        } // end while
        warpingDistance = accumulatedDistance / K;

        this.reversePath(warpingPath);
    }


    protected void reversePath(int[][] path) {
        int[][] newPath = new int[K][2];
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < 2; j++) {
                newPath[i][j] = path[K - i - 1][j];
            }
        }
        warpingPath = newPath;
    }

    public double getDistance() {
        return warpingDistance;
    }


    protected double distanceBetween(double p1, double p2) {
        return (p1 - p2) * (p1 - p2);
    }

    protected int getIndexOfMinimum(double[] array) {
        int index = 0;
        double val = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < val) {
                val = array[i];
                index = i;
            }
        }
        return index;
    }

    public String toString() {
        String retVal = "Warping Distance: " + warpingDistance + "\n";
        retVal += "Warping Path: {";
        for (int i = 0; i < K; i++) {
            retVal += "(" + warpingPath[i][0] + ", " +warpingPath[i][1] + ")";
            retVal += (i == K - 1) ? "}" : ", ";

        }
        return retVal;
    }



}