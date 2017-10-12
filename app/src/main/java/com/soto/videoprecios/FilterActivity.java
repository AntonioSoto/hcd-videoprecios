package com.soto.videoprecios;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    String videogameName;
    List<Videogame> originalList;

    Spinner spinnerOrder;
    Spinner spinnerPlatform;
    Spinner spinnerSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        setTitle( "Filtros de búsqueda" );

        videogameName = getIntent().getStringExtra("intent_VideogameName");
        originalList = VideogameListManager.callManager().getVideogameList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_filterView);
        toolbar.setBackgroundColor(Color.parseColor("#22b14c"));
        setSupportActionBar(toolbar);

        findViewById(R.id.textView_Platform).setVisibility(View.GONE);
        findViewById(R.id.spinner_Platform).setVisibility(View.GONE);
        findViewById(R.id.textView_Seller).setVisibility(View.GONE);
        findViewById(R.id.spinner_Seller).setVisibility(View.GONE);

        this.setupSpinners();
    }

    @Override
    public void onBackPressed() {
        ;
    }

    private void setupSpinners(){

        spinnerOrder = (Spinner) findViewById(R.id.spinner_Order);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_Order,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapter);

        spinnerPlatform = (Spinner) findViewById(R.id.spinner_Platform);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.array_Platforms,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlatform.setAdapter(adapter2);

        spinnerSeller = (Spinner) findViewById(R.id.spinner_Seller);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this,
                R.array.array_Sellers,
                android.R.layout.simple_spinner_item
        );
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeller.setAdapter(adapter3);
    }

    public void clickCancelFilter(View v){

        Intent intent = new Intent( this, SearchActivity.class );
        intent.putExtra( "intent_VideogameName", this.videogameName );
        VideogameListManager.callManager().setVideogameList(null);
        startActivity(intent);
    }

    public void clickApplyFilter(View v){

        EditText lowerPriceText = (EditText) findViewById(R.id.editText_Price1);
        EditText higherPriceText = (EditText) findViewById(R.id.editText_Price2);

        if(lowerPriceText.getText().toString().compareTo("") != 0 &&
                higherPriceText.getText().toString().compareTo("") != 0){

            this.applyOrder();
            this.applyPrices();
        }else{
            this.applyOrder();
        }

        Intent intent = new Intent( this, SearchActivity.class );
        intent.putExtra( "intent_VideogameName", this.videogameName );
        startActivity(intent);
    }

    private void applyOrder(){

        switch(spinnerOrder.getSelectedItem().toString()){
            case "(Ninguno)":
                VideogameListManager.callManager().setVideogameList(originalList);
                break;
            case "Precio (de menor a mayor)":
                orderMinToMax();
                break;
            case "Precio (de mayor a menor)":
                orderMaxToMin();
                break;
            default:
                break;
        }
    }

    private void orderMinToMax(){

        List<Videogame> outputList = VideogameListManager.callManager().getVideogameList();

        Collections.sort( outputList, new Comparator<Videogame>() {
            @Override
            public int compare(Videogame v1, Videogame v2) {

                String nv1 = v1.getPrice().replace("$","");
                String nv2 = v2.getPrice().replace("$","");
                nv1 = nv1.replace(",","");
                nv2 = nv2.replace(",","");

                return Double.compare(Double.parseDouble(nv1), Double.parseDouble(nv2));
            }
        });

        VideogameListManager.callManager().setVideogameList( outputList );
    }

    private void orderMaxToMin(){

        List<Videogame> outputList = VideogameListManager.callManager().getVideogameList();

        Collections.sort( outputList, new Comparator<Videogame>() {
            @Override
            public int compare(Videogame v1, Videogame v2) {

                String nv1 = v1.getPrice().replace("$","");
                String nv2 = v2.getPrice().replace("$","");
                nv1 = nv1.replace(",","");
                nv2 = nv2.replace(",","");

                return Double.compare(Double.parseDouble(nv2), Double.parseDouble(nv1));
            }
        });

        VideogameListManager.callManager().setVideogameList( outputList );
    }

    private void applyPrices(){

        List<Videogame> inputList = VideogameListManager.callManager().getVideogameList();
        List<Videogame> outputList = new ArrayList<>();
        EditText lowerPriceText = (EditText) findViewById(R.id.editText_Price1);
        double lowerPrice = Double.parseDouble(lowerPriceText.getText().toString());
        EditText higherPriceText = (EditText) findViewById(R.id.editText_Price2);
        double higherPrice = Double.parseDouble(higherPriceText.getText().toString());

        for(int i=0; i < inputList.size(); i++){

            String nv1 = inputList.get(i).getPrice().replace("$","");
            nv1 = nv1.replace(",","");

            if(lowerPrice < Double.parseDouble(nv1) && Double.parseDouble(nv1) < higherPrice){
                outputList.add(inputList.get(i));
            }
        }

        VideogameListManager.callManager().setVideogameList(outputList);
    }

}
