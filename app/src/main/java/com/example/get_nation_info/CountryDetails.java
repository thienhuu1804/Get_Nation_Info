package com.example.get_nation_info;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CountryDetails extends AppCompatActivity {

    ProgressDialog progressDialog;
    CountrySimpleData country;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_details);

        country = getIntent().getParcelableExtra("selectedCountry");

        TextView code = findViewById(R.id.countryCode);
        TextView name = findViewById(R.id.countryName);
        TextView population = findViewById(R.id.population);
        TextView area = findViewById(R.id.areaInSqrKm);
        img = findViewById(R.id.img);

        code.setText(country.getCountryCode());
        name.setText(country.getName());
        population.setText(country.getPopulation());
        area.setText(country.getAreaInSqKm());
        String flagURL = "https://www.countryflags.io/" + country.getCountryCode() + "/flat/64.png";

        new setFlag().execute(flagURL);
    }

    private class setFlag extends AsyncTask<String,String,String> {


        RequestCreator req;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(CountryDetails.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            req = Picasso.get().load(strings[0]);
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            super.onPostExecute(s);
            req.resize(128,128).into(img);
            Toast.makeText(getApplicationContext(), "set flag: done !", Toast.LENGTH_LONG).show();
        }
    }
}
