package com.example.admin.tatry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HostelsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostels);
    }

    public void ButtonPressed2(View view) {
        int id = view.getId();

        if(id == R.id.PolanaChocholowska) Coordinates.setCoordinates(49.23635, 19.78787);
        else if(id == R.id.HalaOrnak) Coordinates.setCoordinates(49.22911, 19.85905);
        else if(id == R.id.HalaKondratowa) Coordinates.setCoordinates(49.24979, 19.95171);
        else if(id == R.id.Murowaniec) Coordinates.setCoordinates(49.24336, 20.00726);
        else if(id == R.id.DolinaPieciuStawowPolskich) Coordinates.setCoordinates(49.21353, 20.04866);
        else if(id == R.id.NadMorskimOkiem) Coordinates.setCoordinates(49.20139, 20.07114);
        else if(id == R.id.DolinaRoztoki) Coordinates.setCoordinates(49.23368, 20.09568);
        else if(id == R.id.Glodowka) Coordinates.setCoordinates(49.30227, 20.11585);

        Intent intent = new Intent(this, OfflineMapsActivity.class);
        startActivity(intent);
    }
}
