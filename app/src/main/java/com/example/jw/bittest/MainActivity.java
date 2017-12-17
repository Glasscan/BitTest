package com.example.jw.bittest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> bitList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetBits().execute();

        bitList = new ArrayList<>();
        lv = findViewById(R.id.list);

    }

    private class GetBits extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String url = "https://bittrex.com/api/v1.1/public/getmarketsummaries";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray Bits = jsonObj.getJSONArray("result");

                    for (int i = 0; i < Bits.length(); i++) {
                        JSONObject c = Bits.getJSONObject(i);

                        String MarketName = c.getString("MarketName");
                        String High = c.getString("High");
                        String Low = c.getString("Low");
                        String Volume = c.getString("Volume");
                        String Last = c.getString("Last");
                        String BaseVolume = c.getString("BaseVolume");
                        String TimeStamp = c.getString("TimeStamp");

                        String Bid = c.getString("Bid");
                        String Ask = c.getString("Ask");
                        String OpenBuyOrders = c.getString("OpenBuyOrders");
                        String OpenSellOrders = c.getString("OpenSellOrders");
                        String PrevDay = c.getString("PrevDay");
                        String Created = c.getString("Created");

                        // tmp hash map for single coin
                        HashMap<String, String> coin = new HashMap<>();

                        // adding each child node to HashMap key => value
                        coin.put("MarketName", "Market: "+ MarketName);
                        coin.put("High", "High: "+ High);
                        coin.put("Low", "Low: " + Low);
                        coin.put("Volume", "Volume: " + Volume);
                        coin.put("Last", "Last: " + Last);
                        coin.put("BaseVolume", "Base Volume: " + BaseVolume);
                        coin.put("TimeStamp", "Time Stamp: " + TimeStamp);

                        coin.put("Bid", "Bid: " + Bid);
                        coin.put("Ask", "Ask: " + Ask);
                        coin.put("OpenBuyOrders", "Open Buy Orders: " + OpenBuyOrders);
                        coin.put("OpenSellOrders", "Open Sell Orders: "+ OpenSellOrders);
                        coin.put("PrevDay", "Previous Day: " + PrevDay);
                        coin.put("Created", "Created: " + Created);

                        // adding contact to coin list
                        bitList.add(coin);
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, bitList,
                    R.layout.list_item, new String[]{
                        "MarketName", "High", "Low", "Volume",
                        "Last", "BaseVolume", "TimeStamp",
                        "Bid", "Ask", "OpenBuyOrders", "OpenSellOrders",
                        "PrevDay","Created"
                    },
                    new int[]{
                        R.id.MarketName, R.id.High,
                            R.id.Low,R.id.Volume,R.id.Last,R.id.BaseVolume,
                            R.id.TimeStamp,R.id.Bid, R.id.Ask,
                            R.id.OpenBuyOrders,R.id.OpenSellOrders,R.id.PrevDay,
                            R.id.Created
                    });
            lv.setAdapter(adapter);
        }
    }
}