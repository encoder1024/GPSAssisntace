package com.domo.zoom.gpsassisntace;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.view.View.GONE;

public class ScrollingActivitySitio extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL = 7;
    private static final String KEY_USER_FAVORITOS = "key_user_favoritos";

    private boolean isStarOn = false;
    private FloatingActionButton fab;
    private List<String> favoritosList;
    private Set<String> myFavorits;

    private String[] infoSiteParts;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressBar progressBar;

    static public List<Sitio> sitesList;
    static public List<User> usersList;

    ImageView myLogo;

    String htmlAsString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitio_activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            String infoSite= intent.getStringExtra("key"); //if it's a string you stored.
            infoSiteParts = infoSite.split("@");
        }

        sitesList = new ArrayList<>();
        usersList = new ArrayList<>();

        favoritosList = new ArrayList<>();
        fab = findViewById(R.id.fabScroll);

        myFavorits = new HashSet<String>(favoritosList);

        if (MainActivity.pref.getStringSet(KEY_USER_FAVORITOS, null)!=null){
            favoritosList = Arrays.asList(MainActivity.pref.getStringSet(KEY_USER_FAVORITOS,
                    null).toArray(new String[MainActivity.pref.getStringSet(KEY_USER_FAVORITOS,
                    null).size()]));

            myFavorits =  new HashSet<String>(favoritosList);

            if (myFavorits.contains(String.valueOf(infoSiteParts[1]))){//TODO cambiar el index para que tome el del sitio dinamicamente.
                fab.setImageResource(android.R.drawable.btn_star_big_on); //android.R.drawable.btn_star_big_on
                fab.hide();
                fab.show();
                isStarOn = true;
            } else {
                fab.setImageResource(android.R.drawable.btn_star_big_off); //android.R.drawable.btn_star_big_off
                fab.hide();
                fab.show();
                isStarOn = false;
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStarOn) {
                    fab.setImageResource(android.R.drawable.btn_star_big_on); //android.R.drawable.btn_star_big_on
                    fab.hide();
                    fab.show();
                    isStarOn = true;
                    myFavorits.add(String.valueOf(infoSiteParts[1]));//TODO cambiar el index para que tome el del sitio dinamicamente.

                    SharedPreferences.Editor editor = MainActivity.pref.edit();
                    editor.putStringSet(KEY_USER_FAVORITOS, myFavorits);
                    editor.commit(); // commit changes

                    Log.i("Favoritos: ",myFavorits.toString());
                    Snackbar.make(view, "Ahora es un favorito!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    fab.setImageResource(android.R.drawable.btn_star_big_off); //android.R.drawable.btn_star_big_off
                    fab.hide();
                    fab.show();
                    isStarOn = false;
                    myFavorits.remove(String.valueOf(infoSiteParts[1]));//TODO cambiar el index para que tome el del sitio dinamicamente.

                    SharedPreferences.Editor editor = MainActivity.pref.edit();
                    editor.putStringSet(KEY_USER_FAVORITOS, myFavorits);
                    editor.commit(); // commit changes

                    Log.i("Favoritos: ",myFavorits.toString());
                    Snackbar.make(view, "Ahora no es un favorito.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myLogo = findViewById(R.id.imLogoSite);

        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_READ_SITIOS_FULL + String.valueOf(infoSiteParts[1]),
                null,
                CODE_GET_REQUEST);
        request.execute();




    }

    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }




        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }

        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);
            String [] urlmix = url.split("=");
            try {
                switch (urlmix[0]+"="+urlmix[1]+"="){
                    case Api.URL_READ_SITIOS_FULL:
                        JSONObject objectSitio = new JSONObject(s);
                        if (!objectSitio.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectSitio.getString("message") + ": sitio!", Toast.LENGTH_SHORT).show();
                            //refreshing the herolist after every operation
                            //so we get an updated list
                            //we will create this method right now it is commented
                            //because we haven't created it yet

                            refreshSitesList(objectSitio.getJSONArray("sitio"));
                        }
                        break;
                    case Api.URL_READ_SITIOS_ESPECIAL:
                        JSONObject objectEspecial = new JSONObject(s);
                        if (!objectEspecial.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectEspecial.getString("message") + ": espec", Toast.LENGTH_SHORT).show();
                            //refreshing the herolist after every operation
                            //so we get an updated list
                            //we will create this method right now it is commented
                            //because we haven't created it yet

                            refreshEspecialList(objectEspecial.getJSONArray("especial"));
                        }
                        break;
                    case Api.URL_READ_SITIO_CERTIF:
                        JSONObject objectUser = new JSONObject(s);
                        if (!objectUser.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectUser.getString("message") + ": certificados!", Toast.LENGTH_SHORT).show();
                            refreshCertifList(objectUser.getJSONArray("certif"));
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshCertifList(JSONArray certif) throws JSONException {
        htmlAsString = new StringBuilder().append(htmlAsString).append("<p>Certificado: ").toString();

        for (int i = 0; i < certif.length(); i++) {
            //getting each hero object
            JSONObject obj = certif.getJSONObject(i);
            htmlAsString = new StringBuilder().append(htmlAsString).append(obj.getString("nombre")).append(", ").toString();
        }

        htmlAsString =  new StringBuilder().append(htmlAsString).append("</p><br></body></html>").toString();

        WebView wvDisplay = findViewById(R.id.wvDisplay);
        wvDisplay.loadDataWithBaseURL( null, htmlAsString,  "text/html", "utf-8", null);
    }

    private void refreshEspecialList(JSONArray especial) throws JSONException {
        htmlAsString = new StringBuilder().append(htmlAsString).append("<p>Especialidad: ").toString();

        for (int i = 0; i < especial.length(); i++) {
            //getting each hero object
            JSONObject obj = especial.getJSONObject(i);
            htmlAsString = new StringBuilder().append(htmlAsString).append(obj.getString("nombre")).append(", ").toString();
        }

        htmlAsString =  new StringBuilder().append(htmlAsString).append("</p><br></body></html>").toString();

        WebView wvDisplay = findViewById(R.id.wvDisplay);
        wvDisplay.loadDataWithBaseURL(null, htmlAsString,  "text/html", "utf-8", null);

        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_READ_SITIO_CERTIF + String.valueOf(infoSiteParts[1]),
                null,
                CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshSitesList(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        sitesList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);

            //adding the hero to the list
            sitesList.add(new Sitio(
                    obj.getInt("id"),
                    obj.getString("nombre"),
                    obj.getString("direccion"),
                    obj.getString("descripcion"),
                    obj.getString("telefono"),
                    obj.getInt("provincia_id"),
                    obj.getString("latitud"),
                    obj.getString("longitud"),
                    obj.getString("notas"),
                    obj.getString("created_at"),
                    obj.getString("updated_at"),
                    obj.getInt("tipoSitio"),
                    obj.getString("email"),
                    obj.getString("web"),
                    obj.getString("red_social"),
                    obj.getString("imagen_file"),
                    obj.getString("appu_pre_uno"),
                    obj.getString("appu_pre_dos"),
                    obj.getString("appu_pre_tres"),
                    obj.getInt("contrato_id"),
                    obj.getInt("contratoHabilitado"),
                    obj.getInt("zona_id"),
                    obj.getString("certificadoSite"),
                    obj.getString("foto_nombre"),
                    obj.getString("ruta")
            ));
        }

        Button btCall = findViewById(R.id.btLlamar);
        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScrollingActivitySitio.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScrollingActivitySitio.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL);
                } else {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:"+sitesList.get(0).getTeleSite()));
                    startActivity(i);
                }
            }
        });

        TextView tvDisplay = findViewById(R.id.tvDescrip);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvDisplay.setText(Html.fromHtml("<h1>Title</h1><br><h2>Description here</h2><br><p>Bienvenido: " + MainActivity.usersList.get(0).getApellidoUser() + "</p>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvDisplay.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
        }

        WebView wvDisplay = findViewById(R.id.wvDisplay);

        WebSettings myWebSettings = wvDisplay.getSettings();
        myWebSettings.setDefaultTextEncodingName("utf-8");

        htmlAsString =  "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>"
                +"<h1>"+sitesList.get(0).getNombreSite()+"</h1><br>"
                +"<h2>"+sitesList.get(0).getDescSite()+"</h2><br>"
                +"<p>Bienvenido "+MainActivity.usersList.get(0).getNombreUser()+", este es el mejor lugar para encontrar la información de tu proveedor de confianza.</p><br>"
                +"<p>"
                +"Dirección: "+sitesList.get(0).getDireSite()+"<br>"
                +"Telefono: "+sitesList.get(0).getTeleSite()+"<br>"
                +"email: "+sitesList.get(0).getEmailSite()+"<br>"
                +"web: "+"<a href=\""+sitesList.get(0).getWebSite()+"\">"+sitesList.get(0).getWebSite()+"</a><br>"
                +"redes sociales: "+sitesList.get(0).getRedSocialSite()
                +"</p><br>";

        //wvDisplay.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
        wvDisplay.loadData(htmlAsString, "text/html; charset=utf-8", "utf-8");


        if (!sitesList.get(0).getFotoNombre().isEmpty()){
            switch (sitesList.get(0).getRuta()){
                case "web_domozoom":
                    updateFoto(sitesList.get(0).getFotoNombre());
                    break;
                case "app":
                    break;
                case "web_vieja":
                    break;
                default:

            }
        }

        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_READ_SITIOS_ESPECIAL + String.valueOf(infoSiteParts[1]),
                null,
                CODE_GET_REQUEST);
        request.execute();

    }

    private void updateFoto(String fotoNombre) {
        String URL = Api.ROOT_URL_IMAGES+fotoNombre;
        CargaImagenes nuevaTarea = new CargaImagenes();
        nuevaTarea.execute(URL);
    }

    private class CargaImagenes extends AsyncTask<String, Void, Bitmap>{

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ScrollingActivitySitio.this);
            pDialog.setMessage("Cargando Imagen");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground" , "Entra en doInBackground");
            String url = params[0];
            return descargarImagen(url);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            myLogo.setImageBitmap(result);
            pDialog.dismiss();
        }

    }

    private Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }
}
