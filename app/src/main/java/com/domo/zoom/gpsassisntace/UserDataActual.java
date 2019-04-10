package com.domo.zoom.gpsassisntace;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;

public class UserDataActual extends AppCompatActivity {

    EditText etName, etLastName, etEmail, etCelular, etDate, etPass;
    Spinner spTipoVe, spMarcaVe, spModeloVe;
    Button btActData;

    private static final int DB_TIPO_VEHI_AUTO = 1;
    private static final int DB_TIPO_VEHI_MOTO = 2;
    private static final int DB_TIPO_VEHI_BOTE = 3;
    private static final int DB_TIPO_VEHI_SERV = 4;
    private static final int DB_TIPO_VEHI_AERO = 5;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressBar progressBar;

    static public List<Vehiculo.Marca> marcasList;
    static public List<Vehiculo.Modelo> modelosList;

    private int posTipoVe, posMarcaVe, posModeloVe;
    private ArrayList<String> listadoMarcas;

    private ArrayList<String> listadoModelos;

    private boolean inicioTipoVe = true;
    private boolean inicioMarcaVe = true;
    private boolean inicioModeloVe = true;

    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_actual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            action = intent.getStringExtra("key"); //if it's a string you stored.
        } else {
            action = null;
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = (EditText) findViewById(R.id.etNombre);
        etLastName = (EditText) findViewById(R.id.etApellido);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etCelular = (EditText) findViewById(R.id.etCelular);
        spTipoVe = (Spinner) findViewById(R.id.sp_tipo_vehiculo);
        spMarcaVe = (Spinner) findViewById(R.id.sp_marca_vehiculo);
        spModeloVe = (Spinner) findViewById(R.id.sp_modelo_vehiculo);
        etDate = (EditText) findViewById(R.id.etAnoVehi);
        etPass = findViewById(R.id.etPassword);
        btActData = (Button) findViewById(R.id.btActData);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.vehiculo_tipo_array, R.layout.textview_spinner_data_user ); //android.R.layout.simple_spinner_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoVe.setAdapter(adapter);
        spTipoVe.setOnItemSelectedListener(new MyOnItemSelectedListener());

        spMarcaVe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Toast.makeText(adapterView.getContext(), "Item is " +
                        adapterView.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
                readModelos(marcasList.get(pos).getIdMarca());
                posMarcaVe = pos +1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spModeloVe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Toast.makeText(adapterView.getContext(), "Item is " +
                        adapterView.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
                posModeloVe = pos+1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        marcasList = new ArrayList<>();
        listadoMarcas = new ArrayList<>();
        modelosList = new ArrayList<>();
        listadoModelos = new ArrayList<>();



        if ((MainActivity.usersList.size() > 0 || MainActivity.proList.size() > 0) && !action.isEmpty()){

            if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true)){
                etName.setText(MainActivity.usersList.get(0).getNombreUser());
                etLastName.setText(MainActivity.usersList.get(0).getApellidoUser());
                etEmail.setText(MainActivity.usersList.get(0).getEmailUser());
                etEmail.setEnabled(false);
                etCelular.setText(MainActivity.usersList.get(0).getCelularUser());
                etDate.setText(MainActivity.pref.getString("key_edad_vehi", "1886"));
                etPass.setVisibility(View.GONE);
            } else {
                spTipoVe.setVisibility(View.GONE);
                spMarcaVe.setVisibility(View.GONE);
                spModeloVe.setVisibility(View.GONE);
                etDate.setVisibility(View.GONE);
                etPass.setVisibility(View.VISIBLE);
                etName.setText(MainActivity.proList.get(0).getNombreUser());
                etLastName.setText(MainActivity.proList.get(0).getApellidoUser());
                etEmail.setText(MainActivity.proList.get(0).getEmailUser());
                etEmail.setEnabled(false);
                etCelular.setText(MainActivity.proList.get(0).getCelularUser());
            }



//            if (MainActivity.pref.getBoolean("key_existe", false)){
//                etDate.setText(MainActivity.pref.getString("key_edad_vehi", "1886"));
//                spTipoVe.setSelection(MainActivity.pref.getInt("key_tipo_vehi", 1)-1);
//                spMarcaVe.setSelection(MainActivity.pref.getInt("key_marca_vehi", 1)-1);
//                spModeloVe.setSelection(MainActivity.pref.getInt("key_modelo_vehi", 1)-1);
//            }
            btActData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//TODO: Eliminar los espacios o cambiarlos porun %20 para que no rompan la cadena de texto del GET.
                    if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true) && TextUtils.isEmpty(etDate.getText())){
                        etDate.setError("Ingrese un año entre 1886 y 2019");
                        etDate.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(etName.getText())){
                        etName.setError("Por favor ingrese un nombre");
                        etName.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(etLastName.getText())){
                        etLastName.setError("Por favor ingrese un nombre");
                        etLastName.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(etCelular.getText())){
                        etCelular.setError("Por favor ingrese un celular");
                        etCelular.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(etEmail.getText())){
                        etEmail.setError("Por favor ingrese un email");
                        etEmail.requestFocus();
                        return;
                    }
                    if (!MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true) && TextUtils.isEmpty(etPass.getText())){
                        etPass.setError("La clave es incorrecta");
                        etPass.requestFocus();
                        return;
                    }
                    if (MainActivity.pref.getString(Constants.KEY_USER_FIREBASE_TOKEN, null)!=null){
                        if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true)){
                            writeDataUser(MainActivity.usersList.get(0).getIdUser(), posTipoVe, posMarcaVe, posModeloVe, etDate.getText().toString(),
                                    etName.getText().toString(), etLastName.getText().toString(),
                                    etEmail.getText().toString(), etCelular.getText().toString(), MainActivity.pref.getString(Constants.KEY_USER_FIREBASE_TOKEN, null) );
                        } else {
                            writeDataPro(MainActivity.proList.get(0).getIdUser(), 0, 0, 0, "1886",
                                    etName.getText().toString(), etLastName.getText().toString(),
                                    etEmail.getText().toString(), etCelular.getText().toString(), MainActivity.pref.getString(Constants.KEY_USER_FIREBASE_TOKEN, null), etPass.getText().toString());
                        }
                    } else {
                        if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true)){
                            writeDataUser(MainActivity.usersList.get(0).getIdUser(), posTipoVe, posMarcaVe, posModeloVe, etDate.getText().toString(),
                                    etName.getText().toString(), etLastName.getText().toString(),
                                    etEmail.getText().toString(), etCelular.getText().toString(), "");
                        } else {
                            writeDataPro(MainActivity.proList.get(0).getIdUser(), 0, 0, 0, "1886",
                                    etName.getText().toString(), etLastName.getText().toString(),
                                    etEmail.getText().toString(), etCelular.getText().toString(), "", etPass.getText().toString());
                        }
                    }
                }
            });
        } else if (action.equals("registrarUser")){
            btActData.setText("Crear usuario"); //TODO: Eliminar los espacios o cambiarlos porun %20 para que no rompan la cadena de texto del GET.
            btActData.setEnabled(false);
            btActData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true) && TextUtils.isEmpty(etDate.getText())){
                        etDate.setError("Ingrese un año entre 1886 y 2019");
                        etDate.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(etName.getText())){
                        etName.setError("Por favor ingrese un nombre");
                        etName.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(etLastName.getText())){
                        etLastName.setError("Por favor ingrese un apellido");
                        etLastName.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(etCelular.getText())){
                        etCelular.setError("Por favor ingrese un celular");
                        etCelular.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(etEmail.getText())){
                        etEmail.setError("Por favor ingrese un email");
                        etEmail.requestFocus();
                        return;
                    }
                    if (!MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true) && TextUtils.isEmpty(etPass.getText())){
                        etPass.setError("La clave es incorrecta");
                        etPass.requestFocus();
                        return;
                    }
                    if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true)){
                        PerformNetworkRequest request = new PerformNetworkRequest(
                                Api.URL_READ_USER + "'" + etEmail.getText().toString() +"'",
                                null,
                                CODE_GET_REQUEST);
                        request.execute();
                    } else {
                        PerformNetworkRequest request = new PerformNetworkRequest(
                                Api.URL_READ_PRO + "'" + etEmail.getText().toString() +"'",
                                null,
                                CODE_GET_REQUEST);
                        request.execute();
                    }


                }
            });
            String[] colors = {"si", "no"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ud. es un usuario final?");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                    if (which != 0) {
                        spTipoVe.setVisibility(View.GONE);
                        spMarcaVe.setVisibility(View.GONE);
                        spModeloVe.setVisibility(View.GONE);
                        etDate.setVisibility(View.GONE);
                        //etDate.setText("1886");
                        etPass.setVisibility(View.VISIBLE);
                        //btActData.setEnabled(true);
                        btActData.setText("Acceso Prestador");

                        SharedPreferences.Editor editor = MainActivity.pref.edit();
                        editor.putBoolean(Constants.KEY_USER_FINAL, false); // Storing boolean - true/false
                        editor.commit(); // commit changes

                    } else {
                        etPass.setVisibility(View.GONE);
                        SharedPreferences.Editor editor = MainActivity.pref.edit();
                        editor.putBoolean(Constants.KEY_USER_FINAL, true); // Storing boolean - true/false
                        editor.commit(); // commit changes
                    }
                    btActData.setEnabled(true);
                }
            });
            builder.show();
        }

    }

    public class Norm {
        String cadena;

        public Norm(String cadena) {
            this.cadena = cadena;
        }

        public String cleanString() {
            cadena = Normalizer.normalize(cadena, Normalizer.Form.NFD);
            cadena = cadena.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            cadena = cadena.replaceAll("ñ","n");
            cadena = cadena.replaceAll("Ñ", "N");
            cadena = cadena.replaceAll("'", "");
            cadena = cadena.replaceAll("\"", "");
            cadena = cadena.replaceAll("&", "y");
            cadena = cadena.replaceAll("=", "");
            cadena = quitaEspacios(cadena);
            cadena = cadena.replaceAll(" ", "%20");
            return cadena;
        }

        public String quitaEspacios(String texto) {
            java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
            StringBuilder buff = new StringBuilder();
            while (tokens.hasMoreTokens()) {
                buff.append(" ").append(tokens.nextToken());
            }
            return buff.toString().trim();
        }
    }

    public  class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            Toast.makeText(parent.getContext(), "Item is " +
                    parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
            if (pos == 3) pos = DB_TIPO_VEHI_AERO;
            posTipoVe = pos+1;
            readMarcas(pos+1);
//            if (MainActivity.pref.getBoolean("key_existe", false) && inicioTipoVe){
//                spTipoVe.setSelection(MainActivity.pref.getInt("key_tipo_vehi", 1)-1);
//                inicioTipoVe = false;
//            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    private void readMarcas(int tipoVe) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_MARCAS_POR_TIPO_VEHI + tipoVe, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void readModelos(int marcaVe) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_MODELOS_POR_MARCA_VEHI + marcaVe + "&idVehiTipo=" + posTipoVe, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void writeCreateUser(int idUser, int tipoVe, int marcaVe, int modeloVe, String edadVe, String nombreUser, String apellidoUser, String emailUser, String celularUser, String token) {
        //if validation passes

        String query = Api.URL_WRITE_ADD_USER +
                "&idUser="+ idUser +
                "&nombreUser="+new Norm(nombreUser).cleanString()+
                "&apellidoUser="+new Norm(apellidoUser).cleanString()+
                "&emailUser="+new Norm(emailUser).cleanString()+
                "&celularUser="+new Norm(celularUser).cleanString()+
                "&tipoVe="+tipoVe+
                "&marcaVe="+marcaVe+
                "&modeloVe="+modeloVe+
                "&edadVe="+new Norm(edadVe).cleanString()+
                "&token="+token;


        PerformNetworkRequest request = new PerformNetworkRequest(query, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void writeCreatePro(int idUser, int tipoVe, int marcaVe, int modeloVe, String edadVe, String nombreUser, String apellidoUser, String emailUser, String celularUser, String token, String pass) {
        //if validation passes

        String query = Api.URL_WRITE_ADD_PRO +
                "&idUser="+idUser+
                "&nombreUser="+new Norm(nombreUser).cleanString()+
                "&apellidoUser="+new Norm(apellidoUser).cleanString()+
                "&emailUser="+new Norm(emailUser).cleanString()+
                "&celularUser="+new Norm(celularUser).cleanString()+
                "&tipoVe="+tipoVe+
                "&marcaVe="+marcaVe+
                "&modeloVe="+modeloVe+
                "&edadVe="+new Norm(edadVe).cleanString()+
                "&token="+token+
                "&pass="+pass;


        PerformNetworkRequest request = new PerformNetworkRequest(query, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void writeDataPro(int idUser, int tipoVe, int marcaVe, int modeloVe, String edadVe, String nombreUser, String apellidoUser, String emailUser, String celularUser, String token, String pass) {
        //if validation passes

        String query = Api.URL_WRITE_ACT_PRO +
                "&idUser="+idUser+
                "&nombreUser="+new Norm(nombreUser).cleanString()+
                "&apellidoUser="+new Norm(apellidoUser).cleanString()+
                "&emailUser="+new Norm(emailUser).cleanString()+
                "&celularUser="+new Norm(celularUser).cleanString()+
                "&tipoVe="+tipoVe+
                "&marcaVe="+marcaVe+
                "&modeloVe="+modeloVe+
                "&edadVe="+new Norm(edadVe).cleanString()+
                "&token="+token+
                "&pass="+pass;

        PerformNetworkRequest request = new PerformNetworkRequest(query, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void writeDataUser(int idUser, int tipoVe, int marcaVe, int modeloVe, String edadVe, String nombreUser, String apellidoUser, String emailUser, String celularUser, String token) {
        //if validation passes

        String query = Api.URL_WRITE_ACT_USER +
                "&idUser="+idUser+
                "&nombreUser='"+new Norm(nombreUser).cleanString()+
                "'&apellidoUser='"+new Norm(apellidoUser).cleanString()+
                "'&emailUser='"+new Norm(emailUser).cleanString()+
                "'&celularUser="+new Norm(celularUser).cleanString()+
                "&tipoVe="+tipoVe+
                "&marcaVe="+marcaVe+
                "&modeloVe="+modeloVe+
                "&edadVe="+new Norm(edadVe).cleanString()+
                "&token='"+token+"'";

        PerformNetworkRequest request = new PerformNetworkRequest(query, null, CODE_GET_REQUEST);
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
//            progressBar.setVisibility(View.VISIBLE);
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
//            progressBar.setVisibility(GONE);
            String [] urlmix = url.split("=");
            try {
                switch (urlmix[0]+"="+urlmix[1]+"="){
                    case Api.URL_READ_MODELOS_POR_MARCA_VEHI:
                        JSONObject objectHeroes = new JSONObject(s);
                        if (!objectHeroes.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectHeroes.getString("message") + ": modelos", Toast.LENGTH_SHORT).show();
                            //refreshing the herolist after every operation
                            //so we get an updated list
                            //we will create this method right now it is commented
                            //because we haven't created it yet

                            refreshModelos(objectHeroes.getJSONArray("modelos"));
                        }
                        break;
                    case Api.URL_READ_MARCAS_POR_TIPO_VEHI:
//                    case Api.URL_READ_MARCAS_POR_TIPO_VEHI+2:
//                    case Api.URL_READ_MARCAS_POR_TIPO_VEHI+3:
//                    case Api.URL_READ_MARCAS_POR_TIPO_VEHI+5:
                        JSONObject objectMarcas = new JSONObject(s);
                        if (!objectMarcas.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectMarcas.getString("message") + ": marcas", Toast.LENGTH_SHORT).show();
                            refreshMarcas(objectMarcas.getJSONArray("marcas"));
                        }
                        break;
                    case Api.URL_WRITE_ACT_USER +"&idUser=":
                    case Api.URL_WRITE_ADD_USER +"&idUser=":
                    case Api.URL_WRITE_ADD_PRO +"&idUser=":
                    case Api.URL_WRITE_ACT_PRO +"&idUser=":
                        JSONObject objectUser = new JSONObject(s);
                        if (!objectUser.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectUser.getString("message") + ": user", Toast.LENGTH_SHORT).show();
                            if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true)){
                                refreshUser(objectUser.getJSONArray("users"));
                            } else {
                                refreshPro(objectUser.getJSONArray("users"));
                            }

                        }
                        break;
                    case Api.URL_READ_USER:
                    case Api.URL_READ_PRO:
                        JSONObject objectUserRead = new JSONObject(s);
                        if (!objectUserRead.getBoolean("error" ) && objectUserRead.getJSONArray("users").length() > 0) {

                            if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true)){
                                Toast.makeText(getApplicationContext(), objectUserRead.getString("message") + ": email existente.", Toast.LENGTH_SHORT).show();
                            } else {
                                writeDataPro(0, 0, 0, 0, "1886",
                                        etName.getText().toString(), etLastName.getText().toString(),
                                        etEmail.getText().toString(), etCelular.getText().toString(),
                                        MainActivity.pref.getString(Constants.KEY_USER_FIREBASE_TOKEN, null),
                                        etPass.getText().toString());
                            }
                        } else {
                            if (MainActivity.pref.getBoolean(Constants.KEY_USER_FINAL, true)){
                                writeCreateUser(0, posTipoVe, posMarcaVe, posModeloVe, etDate.getText().toString(),
                                        etName.getText().toString(), etLastName.getText().toString(),
                                        etEmail.getText().toString(), etCelular.getText().toString(), MainActivity.pref.getString(Constants.KEY_USER_FIREBASE_TOKEN, null));
                            } else {
                                Toast.makeText(getApplicationContext(), objectUserRead.getString("message") + ": Sin autorización.", Toast.LENGTH_LONG).show();
                            }

                        }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshUser(JSONArray users) throws JSONException {
        //clearing previous heroes
        MainActivity.usersList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < users.length(); i++) {
            //getting each hero object
            JSONObject obj = users.getJSONObject(i);

            //adding the hero to the list
            MainActivity.usersList.add(new User(
                    obj.getInt("id"),
                    obj.getString("nombre"),
                    obj.getString("apellido"),
                    obj.getString("email"),
                    obj.getString("created_at"),
                    obj.getString("celular"),
                    obj.getInt("vehiId")
            ));
        }

        SharedPreferences.Editor editor = MainActivity.pref.edit();

        editor.putBoolean(MainActivity.KEY_USER_EXISTE, true); // Storing boolean - true/false
        editor.putInt(MainActivity.KEY_USER_ID, MainActivity.usersList.get(0).getIdUser()); // Storing string
        editor.putString(MainActivity.KEY_USER_NOMBRE, MainActivity.usersList.get(0).getNombreUser()); // Storing integer
        editor.putString("key_apellido", MainActivity.usersList.get(0).getApellidoUser()); // Storing float
        editor.putString("key_email", MainActivity.usersList.get(0).getEmailUser()); // Storing long
        editor.putString("key_created_at", MainActivity.usersList.get(0).getCreadoUser()); // Storing string
        editor.putString("key_celular", MainActivity.usersList.get(0).getCelularUser()); // Storing integer
        editor.putInt("key_id_vehiculo", MainActivity.usersList.get(0).getVehiId()); // Storing float
        editor.putInt(Constants.KEY_USER_VEHICULO_TIPO_ID, posTipoVe); //recordad sumar uno si la posición es la 4 (aviones) por diferencias con la DB
        if ( posMarcaVe > 0) {
            editor.putInt(Constants.KEY_USER_VEHICULO_MARCA_ID, marcasList.get(posMarcaVe-1).getIdMarca());
        } else {
            editor.putInt(Constants.KEY_USER_VEHICULO_MARCA_ID, marcasList.get(posMarcaVe).getIdMarca());   
        }
        
        if ( posModeloVe > 0) {
            editor.putInt("key_modelo_vehi", modelosList.get(posModeloVe-1).getIdModelo());
        } else {
            editor.putInt("key_modelo_vehi", modelosList.get(posModeloVe).getIdModelo());   
        }

        editor.putString("key_edad_vehi", etDate.getText().toString());


        editor.commit(); // commit changes
    }

    private void refreshPro(JSONArray users) throws JSONException {
        //clearing previous heroes
        MainActivity.proList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < users.length(); i++) {
            //getting each hero object
            JSONObject obj = users.getJSONObject(i);

            //adding the hero to the list
            MainActivity.proList.add(new Pro(
                    obj.getInt("id"),
                    obj.getString("nombre"),
                    obj.getString("apellido"),
                    obj.getString("email"),
                    obj.getString("created_at"),
                    obj.getString("celular"),
                    obj.getInt("vehiId"), // es el sitioId...
                    0
            ));
        }

        SharedPreferences.Editor editor = MainActivity.pref.edit();

        editor.putBoolean(Constants.KEY_USER_EXISTE, true); // Storing boolean - true/false
        editor.putInt(Constants.KEY_USER_ID, MainActivity.proList.get(0).getIdUser()); // Storing string
        editor.putString(Constants.KEY_USER_NOMBRE, MainActivity.proList.get(0).getNombreUser()); // Storing integer
        editor.putString(Constants.KEY_USER_APELLIDO, MainActivity.proList.get(0).getApellidoUser()); // Storing float
        editor.putString(Constants.KEY_USER_EMAIL, MainActivity.proList.get(0).getEmailUser()); // Storing long
        editor.putString(Constants.KEY_USER_CREATED_AT, MainActivity.proList.get(0).getCreadoUser()); // Storing string
        editor.putString(Constants.KEY_USER_CELULAR, MainActivity.proList.get(0).getCelularUser()); // Storing integer
        editor.putInt(Constants.KEY_USER_PRO_SITIOID, MainActivity.proList.get(0).getVehiId());
//        editor.putInt(Constants.KEY_USER_PRO_PASSWORD, MainActivity.proList.get(0).getPassword());

        editor.commit(); // commit changes
    }

    private void refreshModelos(JSONArray marcas) throws JSONException {
        //clearing previous heroes
        modelosList.clear();
        listadoModelos.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < marcas.length(); i++) {
            //getting each hero object
            JSONObject obj = marcas.getJSONObject(i);

            //adding the hero to the list
            modelosList.add(new Vehiculo.Modelo(
                    obj.getInt("idModelo"),
                    obj.getString("nombre"),
                    obj.getString("slug"),
                    obj.getInt("idMarca"),
                    obj.getInt("idVehiTipo"),
                    obj.getString("imagen"),
                    obj.getString("created_at"),
                    obj.getString("updated_at")
            ));

            listadoModelos.add(modelosList.get(i).getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.textview_spinner_data_user, listadoModelos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spModeloVe.setAdapter(adapter);
        if (MainActivity.pref.getBoolean("key_existe", false) && inicioModeloVe){
            int pos=0;
            for(Vehiculo.Modelo i: modelosList){
                if(i.getIdModelo()==MainActivity.pref.getInt("key_modelo_vehi", 1)) spModeloVe.setSelection(pos);
                pos++;
            }
            inicioModeloVe = false;
        }
        //spModeloVe.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    private void refreshMarcas(JSONArray marcas) throws JSONException {
        //clearing previous heroes
        marcasList.clear();
        listadoMarcas.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < marcas.length(); i++) {
            //getting each hero object
            JSONObject obj = marcas.getJSONObject(i);

            //adding the hero to the list
            marcasList.add(new Vehiculo.Marca(
                    obj.getInt("id"),
                    obj.getString("nombre"),
                    obj.getString("imagen"),
                    obj.getString("created_at"),
                    obj.getString("updated_at")
            ));

            listadoMarcas.add(marcasList.get(i).getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.textview_spinner_data_user, listadoMarcas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarcaVe.setAdapter(adapter);

        if (MainActivity.pref.getBoolean("key_existe", false) && inicioMarcaVe){
            int pos=0;
            for(Vehiculo.Marca i: marcasList){
                if(i.getIdMarca()==MainActivity.pref.getInt("key_marca_vehi", 1)) spMarcaVe.setSelection(pos);
                pos++;
            }
            inicioMarcaVe = false;
        }
        //spModeloVe.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }
}

