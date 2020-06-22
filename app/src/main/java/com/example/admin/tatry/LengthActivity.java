package com.example.admin.tatry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LengthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_length);
    }
    public void ButtonPressed(View view) {
        int id = view.getId();

        if(id == R.id.Regle) Coordinates.setCoordinates(49.26092, 19.96775);
        else if(id == R.id.Regle2) Coordinates.setCoordinates(49.25261, 19.81530);
        else if(id == R.id.StarorobocianskiWierch) Coordinates.setCoordinates(49.27664, 19.83148);
        else if(id == R.id.Brzeziny) Coordinates.setCoordinates(49.28731, 20.03568);
        else if(id == R.id.Palenica) Coordinates.setCoordinates(49.25459, 20.10292);
        else if(id == R.id.ZHaliOrnak) Coordinates.setCoordinates(49.22978, 19.85989);
        else if(id == R.id.Palenica) Coordinates.setCoordinates(49.25459, 20.10293);
        else if(id == R.id.Rusinowa) Coordinates.setCoordinates(49.26286, 20.09030);
        else if(id == R.id.MorskieOko) Coordinates.setCoordinates(49.19354, 20.07039);
        else if(id == R.id.MorskieOko2) Coordinates.setCoordinates(49.20972, 20.03001);
        else if(id == R.id.PolanaChocholowska2) Coordinates.setCoordinates(49.23638, 19.79376);
        else if(id == R.id.Kasprowy) Coordinates.setCoordinates(49.26951, 19.98051);
        else if(id == R.id.Cyrhla) Coordinates.setCoordinates(49.28774, 20.01208);
        else if(id == R.id.Szpiglas) Coordinates.setCoordinates(49.20972, 20.03001);
        else if(id == R.id.Rysy) Coordinates.setCoordinates(49.19354, 20.07039);
        else if(id == R.id.Zleb) Coordinates.setCoordinates(49.22077, 20.03292);
        else if(id == R.id.Trzydniowka) Coordinates.setCoordinates(49.23975, 19.80607);
        else if(id == R.id.Zazdan) Coordinates.setCoordinates(49.29031, 20.08018);
        else if(id == R.id.Poroniec) Coordinates.setCoordinates(49.28476, 20.11289);
        else if(id == R.id.Kir) Coordinates.setCoordinates(49.27549, 19.86887);
        else if(id == R.id.Kopa) Coordinates.setCoordinates(49.23649, 19.93219);
        else if(id == R.id.Zakopane) Coordinates.setCoordinates(49.29551, 19.94591);
        else if(id == R.id.Koscielec2) Coordinates.setCoordinates(49.24145, 20.00177);
        else if(id == R.id.Swinica) Coordinates.setCoordinates(49.23237, 19.98165);
        else if(id == R.id.Zawrat) Coordinates.setCoordinates(49.21909, 20.01639);
        else if(id == R.id.Granat) Coordinates.setCoordinates(49.22701, 20.03351);
        else if(id == R.id.Sarnia) Coordinates.setCoordinates(49.26282, 19.94249);

        Intent intent = new Intent(this, OfflineMapsActivity.class);
        startActivity(intent);
    }
}
