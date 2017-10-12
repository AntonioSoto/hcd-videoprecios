package com.soto.videoprecios;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String videogameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        videogameName = getIntent().getStringExtra("intent_VideogameName");
        setTitle( videogameName );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#22b14c"));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
                Intent intent = new Intent( getApplicationContext(), FilterActivity.class );
                intent.putExtra( "intent_VideogameName", videogameName );
                startActivity(intent);
            }
        });

        if( VideogameListManager.callManager().getVideogameList() == null ){
            this.doScrapping(videogameName);
        }
        else{
            setupVideogameAdapter(VideogameListManager.callManager().getVideogameList());
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            //finish(); // close this activity and return to preview activity (if there is any)
            Intent intent = new Intent( getApplicationContext(), MainActivity.class );
            VideogameListManager.callManager().setVideogameList(null);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void doScrapping(String videogameName){

        //String siteUrl = "https://gameplanet.com/search/sort-by/popularidad/sort-direction/desc?q=";
        //new GameplanetAsyncTask().execute( siteUrl.concat(videogameName).replaceAll("\\s","%20") );

        String siteUrl = "https://www.google.com.mx/search?hl=es-419&dcr=0&tbm=shop&q=";
        new ShoppingAsyncTask().execute( siteUrl.concat(videogameName).replaceAll("\\s","%20") );
    }

    private void setupVideogameAdapter(List<Videogame> result){

        ListView listView = (ListView)findViewById(R.id.listView_Games);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Videogame game = (Videogame) adapterView.getItemAtPosition(i);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(game.getUrl().toString()));
                startActivity(browserIntent);
            }
        });

        VideogameAdapter adapter = new VideogameAdapter(getApplicationContext(), R.layout.listview_layout, result);
        listView.setAdapter(adapter);
    }

    private List<Videogame> setupGameList(
            ArrayList<URL> urls,
            ArrayList<String> names,
            ArrayList<String> prices,
            ArrayList<String> sellers,
            ArrayList<Bitmap> imgs
    ){
        List<Videogame> outputList = new ArrayList<>();

        for(int i=0; i < sellers.size(); i++){

            Videogame game = new Videogame(
                    imgs.get(i),
                    names.get(i),
                    prices.get(i),
                    sellers.get(i),
                    urls.get(i)
            );

            outputList.add(game);
        }

        return outputList;
    }

    private class GameplanetAsyncTask extends AsyncTask<String, Void, List<Videogame>> {

        @Override
        protected List<Videogame> doInBackground(String... strings) {

            //StringBuffer buffer = new StringBuffer();
            List<Videogame> outputList = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(strings[0]).get();

                Elements nameList = doc.select("h2[class=product-name lista-title]");
                //buffer.append("Nombres\r\n");
                ArrayList<URL> listURLs = new ArrayList<>();
                ArrayList<String> listNames = new ArrayList<>();
                for (Element link : nameList) {

                    Element hrefE = link.select("a").first();
                    URL url = new URL(hrefE.attr("href"));
                    listURLs.add(url);

                    String data = link.text();
                    listNames.add(data);

                    //buffer.append("Data ["+data+"] \r\n");
                    //buffer.append("Href ["+href+"] \r\n");
                }

                Elements priceList = doc.select("span[class=price]");
                //buffer.append("Precios\r\n");
                ArrayList<String> listPrices = new ArrayList<>();
                for (Element link : priceList) {
                    String data = link.text();
                    listPrices.add(data);
                    //buffer.append("Data ["+data+"] \r\n");
                }

                Elements imageList = doc.select("img[class=img-responsive]");
                //buffer.append("Imagenes\r\n");
                ArrayList<Bitmap> listImages = new ArrayList<>();
                for (Element link : imageList) {
                    String src = link.absUrl("src");
                    URL url = new URL(src);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    listImages.add(bmp);
                    //buffer.append("SRC ["+src+"] \r\n");
                }

                Elements sellerList = doc.select("span[class=att_publ_contenedor]");
                //buffer.append("Vendedores\r\n");
                ArrayList<String> listSellers = new ArrayList<>();
                for (Element topic : sellerList) {
                    String data = topic.text();
                    listSellers.add(data);
                    //buffer.append("Data ["+data+"] \r\n");
                }

                outputList = setupGameList(listURLs, listNames, listPrices, listSellers, listImages);
            }
            catch(Throwable t) {
                t.printStackTrace();
            }

            return outputList;
        }

        @Override
        protected void onPostExecute(List<Videogame> result) {

            super.onPostExecute(result);

            VideogameListManager.callManager().setVideogameList(result);

            setupVideogameAdapter(result);

            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
        }
    }

    private class ShoppingAsyncTask extends AsyncTask<String, Void, List<Videogame>> {

        @Override
        protected List<Videogame> doInBackground(String... strings) {

            List<Videogame> outputList = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(strings[0]).get();

                Elements nameList = doc.select("a[class=pstl]");
                ArrayList<String> listNames = new ArrayList<>();
                for (Element link : nameList) {

                    String data = link.text();
                    System.out.println(data);
                    listNames.add(data);
                }

                Elements priceList = doc.select("span[class=price]");
                ArrayList<String> listPrices = new ArrayList<>();
                for (Element link : priceList) {

                    String data = link.text();
                    data = "$"+data.substring(data.indexOf("N") + 1);
                    System.out.println(data);
                    listPrices.add(data);
                }

                Elements sellerList = doc.select("div[class=_tyb shop__secondary]");
                ArrayList<String> listSellers = new ArrayList<>();
                for (Element link : sellerList) {

                    String data = link.text();
                    data = data.substring(data.indexOf("n ") + 2);
                    System.out.println(data);
                    listSellers.add(data);
                }

                Elements imageList = doc.select("img[class=_zyj]");
                ArrayList<Bitmap> listImages = new ArrayList<>();
                for (Element link : imageList) {

                    String src = link.attr("src").replace("data:image/gif;base64,","");
                    System.out.println(src);
                    byte[] imageAsBytes = Base64.decode(src.getBytes(), 0);
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_t);
                    listImages.add(bmp);
                }

                Elements urlList = doc.select("h3[class=r]");
                ArrayList<URL> listURLs = new ArrayList<>();
                for (Element link : urlList) {

                    Element hrefE = link.select("a").first();
                    String googleurl = "https://www.google.com";
                    URL url = new URL(googleurl.concat(hrefE.attr("href")));
                    listURLs.add(url);
                }

                outputList = setupGameList(listURLs, listNames, listPrices, listSellers, listImages);
            }
            catch(Throwable t) {
                t.printStackTrace();
            }

            return outputList;
        }

        @Override
        protected void onPostExecute(List<Videogame> result) {

            super.onPostExecute(result);

            VideogameListManager.callManager().setVideogameList(result);

            setupVideogameAdapter(result);

            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
        }
    }

}
