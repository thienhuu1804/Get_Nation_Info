package com.example.get_nation_info;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    TextView infoField;
    ImageView img;
    List<CountrySimpleData> countries ;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.ImageView01);
        infoField = findViewById(R.id.info);
        setFlag();
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),CountryDetails.class);
//                CountrySimpleData ct1 = new CountrySimpleData("vn1","code1","1000","1111");
//                countries = new ArrayList<CountrySimpleData>();
//                countries.add(ct1);
//                countries.add(new CountrySimpleData("vn2","code2","1000","1111"));
//                countries.add(new CountrySimpleData("vn3","code3","1000","1111"));
//                intent.putExtra("test", (Serializable) countries);
//                startActivity(intent);
//            }
//        });
    }

    private boolean setFlag(){
        Picasso.get()
                .load("https://www.countryflags.io/be/flat/64.png")
                .resize(128,128)
                .into(img );
        new jsonTask().execute("http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&username=caoth&style=full");
        Toast.makeText(getApplicationContext(),"set flag: done !",Toast.LENGTH_LONG).show();
        return true;
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
//                String result = jsonObject.getJSONArray("geonames").get(1).toString();
                ArrayList<JSONObject> list = JSONArray_to_JSONObjectArrayList(jsonObject.getJSONArray("geonames"));
//                jsonObject = new JSONObject(result);
                infoField.setText("country code cua 5 la "+ list.get(5).getString("countryName"));
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"Loi get string", Toast.LENGTH_LONG);
                e.printStackTrace();
            }
//            infoField.setText(jsonObject.getString(""));
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
