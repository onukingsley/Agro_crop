package com.example.agrocrop;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
String tempmin,tempmax,pressure,humidity,desc,date;
String tempmin1,tempmax1,pressure1,humidity1,desc1,date1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  stringrequest();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"Good day user\n Repost for "+ date + " Seems like its going to be " + desc + " \n " +
                        "tempmin : "+ tempmin + " \n tempmax : " + tempmax + " \n pressure : " + pressure + " \n humidity : " + humidity+
                        " \n Repost for "+ date1 + " Seems like its going to be " + desc1 + " \n " +
                        "tempmin : "+ tempmin1 + " \n tempmax : " + tempmax1 + " \n pressure : " + pressure1 + " \n humidity : " + humidity1 , Toast.LENGTH_LONG).show();
                issuenotification();
            }
        },3000);*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        },5000);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void makenotificationchannel(String id, String name, int importance){
        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setShowBadge(true);
        channel.setDescription("Good day user\n Repost for "+ date + " Seems like its going to be " + desc + " \n " +
                "tempmin : "+ tempmin + " \n tempmax : " + tempmax + " \n pressure : " + pressure + " \n humidity : " + humidity+
                " \n Repost for "+ date1 + " Seems like its going to be " + desc1 + " \n " +
        "tempmin : "+ tempmin1 + " \n tempmax : " + tempmax1 + " \n pressure : " + pressure1 + " \n humidity : " + humidity1);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void issuenotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            makenotificationchannel("channel_1","weather report",NotificationManager.IMPORTANCE_DEFAULT);
        }


            NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this,"channel_1");

            notification.setContentTitle("weather report")
                    .setContentText("Good day user\n Repost for "+ date + " Seems like its going to be " + desc + " \n " +
                            "tempmin : "+ tempmin + "oC \n tempmax : " + tempmax + " oC \n pressure : " + pressure + "KPa \n humidity : " + humidity+
                            " \n\n Repost for "+ date1 + " Seems like its going to be " + desc1 + " \n " +
                            "tempmin : "+ tempmin1 + " \n tempmax : " + tempmax1 + " \n pressure : " + pressure1 + " \n humidity : " + humidity1)
                    .setNumber(1)
                    .setSmallIcon(R.drawable.weather)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Good day user\n Repost for "+ date + " Seems like its going to be " + desc + " \n " +
        "tempmin : "+ tempmin + " oC \n tempmax : " + tempmax + " oC \n pressure : " + pressure + "KPa \n humidity : " + humidity+
                " \n\n Repost for "+ date1 + " Seems like its going to be " + desc1 + " \n " +
                "tempmin : "+ tempmin1 + " \n tempmax : " + tempmax1 + " \n pressure : " + pressure1 + " \n humidity : " + humidity1));

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1,notification.build());
        }





    public void stringrequest(){
        StringRequest request = new StringRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/forecast?id=524901&appid=1b54162d1c3a078e735e6335abd96b3c", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("response", response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray list = jsonObject.getJSONArray("list");

                    JSONObject verbose = list.getJSONObject(0);

                    JSONObject main = verbose.getJSONObject("main");
                    date = verbose.getString("dt_txt");
                    double t1 = Double.valueOf(main.getString("temp_min"))-273;
                    double t2 = Double.valueOf(main.getString("temp_max"))-273;
                    String r1 = String.format("%.2f", t1);
                    String r2 = String.format("%.2f", t2);
                    tempmin = r1;
                    tempmax = r2;

                    pressure = main.getString("pressure");
                    /*tempmin = main.getString("temp_min");
                    tempmax = main.getString("temp_max");*/
                    humidity = main.getString("humidity");
                    /*date = main.getString("dt_txt");*/

                    JSONArray weather = verbose.getJSONArray("weather");
                    JSONObject wea = weather.getJSONObject(0);
                    desc = wea.getString("description");




                    JSONObject verbose1 = list.getJSONObject(1);

                    JSONObject main2 = verbose1.getJSONObject("main");
                    date1 = verbose1.getString("dt_txt");
                    double d1 = Double.valueOf(main.getString("temp_min"))-273;
                    double d2 = Double.valueOf(main.getString("temp_max"))-273;
                    String a1 = String.format("%.2f", d1);
                    String a2 = String.format("%.2f", d2);
                    tempmin = a1;
                    tempmax = a2;
                   /* tempmin1 = main.getString("temp_min");
                    tempmax1 = main.getString("temp_max");*/
                    pressure1 = main2.getString("pressure");
                    humidity1 = main2.getString("humidity");
                    /*date1 = main2.getString("dt_txt");*/

                    JSONArray weather1 = verbose1.getJSONArray("weather");
                    JSONObject wea1 = weather1.getJSONObject(0);
                    desc1 = wea1.getString("description");





                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "error in request " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

}