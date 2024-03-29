package com.example.get_nation_info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextView emptyElement;
    ImageView img;
    ArrayList<CountrySimpleData> countries ;
    JSONObject jsonObject;
    CountryAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countries = new ArrayList<CountrySimpleData>();
        new jsonTask().execute("http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&username=caoth&style=full");
    }

    private class jsonTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(params[0]);
                connection =(HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                    Log.d("Response:" , "> "+ line);
                }

                return buffer.toString();

            }catch(MalformedURLException m){
                m.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }finally {
                if (connection != null)
                    connection.disconnect();
                try{
                    if( reader != null )
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

//          Get contents of json file and parse to an JSONObject
            try {
                jsonObject = new JSONObject(s);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Loi chuyen JSON", Toast.LENGTH_LONG);
                e.printStackTrace();
            }

//           Get optional values from jsonObject
            try {
                ArrayList<JSONObject> list = JSONArray_to_JSONObjectArrayList(jsonObject.getJSONArray("geonames"));
                // Pass needed values from JSONObject to countries
                for(int i=0; i<list.size(); i++){
                    JSONObject obj = list.get(i);
                    countries.add(new CountrySimpleData(
                            obj.getString("countryCode"),
                            obj.getString("countryName"),
                            obj.getString("population"),
                            obj.getString("areaInSqKm"))
                    );

                }
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"Loi get string", Toast.LENGTH_LONG);
                e.printStackTrace();
            }
            emptyElement = findViewById(R.id.emptyElement);
            if(countries.size()>0){
                emptyElement.setVisibility(View.INVISIBLE);
            } else{
                emptyElement.setVisibility((View.VISIBLE));
            }
            adapter = new CountryAdapter(MainActivity.this, countries);
            recyclerView = findViewById(R.id.countryName);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(adapter);
        }
    }

    private ArrayList<JSONObject> JSONArray_to_JSONObjectArrayList(JSONArray array) throws JSONException {
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        JSONObject obj;
        JSONArray arr = array;
        for(int i = 0; i < arr.length();i++ ){
            obj = new JSONObject(arr.get(i).toString());
            list.add(obj);
        }
        return list;
    }
}
