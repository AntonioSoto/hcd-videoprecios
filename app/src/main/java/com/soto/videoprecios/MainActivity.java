package com.soto.videoprecios;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }

    @Override
    public void onBackPressed() {
        ;
    }

    public void clickSearch(View v){

        EditText searchField = (EditText) findViewById( R.id.editText_Search );

        boolean isTextValid = searchField.getText().toString().compareTo("") != 0;

        if( isTextValid ){
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra( "intent_VideogameName", searchField.getText().toString() );
            startActivity(intent);
        }
        else{
            Toast.makeText(
                    getApplicationContext(),
                    "Escribe el nombre de un videojuego",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

}
