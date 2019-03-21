package com.domo.zoom.gpsassisntace;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

//Comentario para generar el release 1.2

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    public static boolean isAppRunning;

    private GoogleMap mMap;
    private Boolean isFABOpen = false;
    private MarkerOptions markerOptions;
    FloatingActionButton fab, fab1, fab2, fab3, fab4, fab5, fab6;

    private static final int CODE_FAB_VARIOS = 6;
    private static final int CODE_FAB_LAVADERO = 5;
    private static final int CODE_FAB_SERVICIOS = 4;
    private static final int CODE_FAB_TALLER = 3;
    private static final int CODE_FAB_GOMERIA = 2;
    private static final int CODE_FAB_REPUESTOS = 1;
    private static final int CODE_FAB_FILTRO = 0;
    private int fabVista = CODE_FAB_FILTRO;

    private static final int CODE_DB_TALLER = 1;
    private static final int CODE_DB_REPUESTOS = 2;
    private static final int CODE_DB_GOMERIA = 3;
    private static final int CODE_DB_SERVICIOS = 4;
    private static final int CODE_DB_LAVADERO = 5;
    private static final int CODE_DB_VARIOS = 6;

    public static final String KEY_USER_EXISTE = "key_existe";
    public static final String KEY_USER_ID = "key_id";
    public static final String KEY_USER_NOMBRE = "key_nombre";
    public static final String KEY_USER_APELLIDO = "key_apellido";
    public static final String KEY_USER_EMAIL = "key_email";
    public static final String KEY_USER_CREATED_AT = "key_created_at";
    public static final String KEY_USER_CELULAR = "key_celular";
    public static final String KEY_USER_VEHICULO_ID = "key_id_vehiculo";

    private String filtroSitio = "all";
    private String filtroMarca = "all";

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressBar progressBar;

    static public List<Sitio> sitesList;
    static public List<User> usersList;
    static public List<Pro> proList;
    static public List<String> miFiltroList;

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    double[] posActGl;

    static public SharedPreferences pref;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static ImageView myLogo;
    private static WebView wvDisplay;
    private static TextView tvDisplay;
    private static Button btCall;

    private static String htmlAsString = null;

    private static final int MY_PERMISSIONS_REQUEST_CALL = 7;

    private static Context context;
    private boolean miFiltroAplicado = false;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAppRunning = true;
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        context = GlobalApplication.getAppContext();

        if (pref.getBoolean(Constants.KEY_USER_EXISTE, false) && pref.getBoolean(Constants.KEY_USER_FINAL, true)){
            appUser();
        } else {
            appPro();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pref.getBoolean(Constants.KEY_USER_EXISTE, false) && pref.getBoolean(Constants.KEY_USER_FINAL, true)){
            registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
        }

        if (pref.getBoolean(Constants.KEY_USER_EXISTE, false) && pref.getBoolean(Constants.KEY_USER_FINAL, true)){
            appUser();
        } else {
            appPro();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pref.getBoolean(Constants.KEY_USER_EXISTE, false) && pref.getBoolean(Constants.KEY_USER_FINAL, true)) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    Double latitude,longitude;
    Geocoder geocoder;
    List<Address> addresses;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final PendingResult resultReciver = goAsync();

            latitude = Double.valueOf(intent.getStringExtra("latutide"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));


            addresses = new ArrayList<>();

            try {
                geocoder = new Geocoder(MainActivity.this);
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

//                tv_area.setText(addresses.get(0).getAdminArea());
//                tv_locality.setText(stateName);
//                tv_address.setText(countryName);
                Log.i("doInBackgroundgps" , "Datos de Servicio Maps: " +cityName+" "+stateName +" "+countryName);
                if (MainActivity.isAppRunning) {
                    for (Sitio sitio : sitesList) {
                        if (!sitio.getLatSite().isEmpty() && !sitio.getLngSite().isEmpty()) {
                            if (Math.abs(Double.valueOf(sitio.getLatSite()) - latitude) < 0.00015) {
                                if (Math.abs(Double.valueOf(sitio.getLngSite()) - longitude) < 0.00015){
                                    Log.i("doInBackgroundgps", "Estamos cerca del Sitio: " + sitio.getNombreSite());
                                    //Toast.makeText(getApplicationContext(), "Estamos cerca del Sitio: " + sitio.getNombreSite(), Toast.LENGTH_SHORT).show();
                                    //TODO crear el metodo RADAR que inicia en un diametro de 1000 m luego 700 m, 400m y luego 100m o menos manda la notif con la oferta...
                                    //TODO Enviar un mensaje al appu del Sitio al que estamos cercanos a 100m o menos...
                                    //TODO guardar el evento en la DB
                                    //TODO guardar el evento de lectura de QR con fecha hora y posición.
                                    //TODO limitar en tiempo las notificacones, por ejemplo detectar cercania cada 24 hs y si es habitual, hacerlo una vez a la semana.
                                    //TODO veamos que apsa con los cambios.

                                    int notificationId = new Random().nextInt(60000);

                                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    notificationManager.notify(notificationId, getNotificationCerca(sitio.getNombreSite(), cityName+" "+stateName +" "+countryName , context));

//                                    PerformNetworkRequest request = new PerformNetworkRequest(
//                                            Api.URL_READ_USER_TEST + "'" + pref.getString("key_email", "test@test.com") +"'",
//                                            null,
//                                            CODE_GET_REQUEST);
//                                    request.execute();
//


                                }

                            }
                        }
                    }
                }

                //TODO cuando la App no está corriendo también debo notificar con la info que tenga, puedo guardar una copia de lo básico en pref en el MainActivity con un Set y sacarlo junto con el id del Usuario.
                //TODO también puedo hacer jugar el tema de los favoritos en las notificaciones, para que tengan mayor peso.
                new Thread(){
                    public void run(){
                        //Do background work
                        //TODO hacer un Api request en crudo para enviar id usuario final y id del sitio a ser notificado. Luego del lado del server con PHP lo resuelvo a la notificacion para ambos.
                        //este result.finish() debe ir en el onPostExecute en la AsyncTask que se ejecuta, para cerrar el hilo del broadcastreceiver.
                        resultReciver.finish();
                    }
                }.start();



            } catch (IOException e1) {
                e1.printStackTrace();
            }

                Log.i("doInBackground" , "Datos de Servicio Maps point: " +latitude+" "+longitude);
//            tv_latitude.setText(latitude+"");
//            tv_longitude.setText(longitude+"");
//            tv_address.getText();


        }
    };

    private NotificationManager notificationManager;

    private Notification getNotificationCerca(String title, String msg, Context context) {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ADMIN_CHANNEL_ID")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Estás a un paso! "+title)
                .setContentText(msg);

        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel("ADMIN_CHANNEL_ID", adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";



        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
            MainActivity.PlaceholderFragment fragment = new MainActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    View rootView = inflater.inflate(R.layout.fragment_mi_local, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//                    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)) + "Mi Local");
                    myLogo = rootView.findViewById(R.id.imLogoSite);
                    wvDisplay = rootView.findViewById(R.id.wvDisplay);
                    tvDisplay = rootView.findViewById(R.id.tvDescrip);


                    btCall = rootView.findViewById(R.id.btLlamar);
                    btCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_REQUEST_CALL);
                            } else if (sitesList.size() > 0) {
                                Intent i = new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse("tel:"+sitesList.get(0).getTeleSite()));
                                startActivity(i);
                            }
                        }
                    });
                    if (MainActivity.proList.size() > 0) {
                        PerformNetworkRequestPro request = new PerformNetworkRequestPro(
                                Api.URL_READ_SITIOS_FULL + String.valueOf(pref.getInt(Constants.KEY_USER_PRO_SITIOID, 0)),
                                null,
                                CODE_GET_REQUEST);
                        request.execute();
                    }


                    return rootView;

                case 2:
                    View rootView2 = inflater.inflate(R.layout.fragment_display, container, false);
                    TextView textView2 = (TextView) rootView2.findViewById(R.id.section_label);
                    textView2.setText(getString(R.string.section_format,getArguments().getInt(ARG_SECTION_NUMBER)) + "Display");

                    return rootView2;

                default: //3
                    View rootView3 = inflater.inflate(R.layout.fragment_info, container, false);
                    TextView textView3 = (TextView) rootView3.findViewById(R.id.section_label);
                    textView3.setText(getString(R.string.section_format,getArguments().getInt(ARG_SECTION_NUMBER)) + "Info");
                    return rootView3;

            }

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return MainActivity.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    boolean layoutUserExist = false;

    private void appUser() {

        if (!layoutUserExist){
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            sitesList = new ArrayList<>();
            usersList = new ArrayList<>();
            proList = new ArrayList<>();
            miFiltroList = new ArrayList<>();

            //findViewById de todos los fab.
            fab = findViewById(R.id.fab);
            fab1 = findViewById(R.id.fab1);
            fab2 = findViewById(R.id.fab2);
            fab3 = findViewById(R.id.fab3);
            fab4 = findViewById(R.id.fab4);
            fab5 = findViewById(R.id.fab5);
            fab6 = findViewById(R.id.fab6);

            fab.setOnClickListener(new View.OnClickListener() { //BOTON INICIAL DEL FILTRO
                @Override
                public void onClick(View view) { //Filtros
                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();
                        //TODO fab Filtro: Acá debe volver a mostrar todos los puntos del mapa de las tres categorías principales.
                        if (fabVista != CODE_FAB_FILTRO){
                            mMap.clear();
                            //TODO fab Talleres: mostrar solo los talleres.
                            fabVista = CODE_FAB_FILTRO;
                            actDisplayMap(CODE_FAB_FILTRO);
                        }
                    }
                }
            });

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //Repuestos
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();
                        //TODO fab Repuestos: mostrar solo las casas de repuestos.

                        if (fabVista != CODE_FAB_REPUESTOS){
                            mMap.clear();
                            //TODO fab Talleres: mostrar solo los talleres.
                            fabVista = CODE_FAB_REPUESTOS;
                            actDisplayMap(CODE_DB_REPUESTOS);
                        }
                    }
                }
            });

            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //Gomerias
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();
                        //TODO fab Gomerías: mostrar solo las gomerías.

                        if (fabVista != CODE_FAB_GOMERIA){
                            mMap.clear();
                            //TODO fab Talleres: mostrar solo los talleres.
                            fabVista = CODE_FAB_GOMERIA;
                            actDisplayMap(CODE_DB_GOMERIA);
                        }
                    }
                }
            });

            fab3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //Talleres
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();

                        if (fabVista != CODE_FAB_TALLER){
                            mMap.clear();
                            //TODO fab Talleres: mostrar solo los talleres.
                            fabVista = CODE_FAB_TALLER;
                            actDisplayMap(CODE_DB_TALLER);
                        }

                    }
                }
            });

            fab4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //Servicios
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();

                        if (fabVista != CODE_FAB_SERVICIOS){
                            mMap.clear();
                            //TODO fab Talleres: mostrar solo los talleres.
                            fabVista = CODE_FAB_SERVICIOS;
                            actDisplayMap(CODE_DB_SERVICIOS);
                        }

                    }
                }
            });

            fab5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //Lavaderos //TODO: cambiar a lubricentros.
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();

                        if (fabVista != CODE_FAB_LAVADERO){
                            mMap.clear();
                            //TODO fab Talleres: mostrar solo los talleres.
                            fabVista = CODE_FAB_LAVADERO;
                            actDisplayMap(CODE_DB_LAVADERO);
                        }

                    }
                }
            });

            fab6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //Varios
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();

                        if (fabVista != CODE_FAB_VARIOS){
                            mMap.clear();
                            //TODO fab Talleres: mostrar solo los talleres.
                            fabVista = CODE_FAB_VARIOS;
                            actDisplayMap(CODE_DB_VARIOS);
                        }

                    }
                }
            });

            showFABMenu();
            isFABOpen = true;

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            layoutUserExist = true;
        }

    }
    private boolean layoutProExist = false;
    private void appPro() {
        sitesList = new ArrayList<>();
        usersList = new ArrayList<>();
        proList = new ArrayList<>();

        if (pref.getBoolean("key_existe", false) && !layoutProExist){
            setContentView(R.layout.activity_main_pro);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            progressBar = (ProgressBar) findViewById(R.id.progressBar);




            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            layoutProExist = true;
        }

        readUser();

    }

    private void actDisplayMap(int tipoSite ){
        if (tipoSite > 0) {
            for (Sitio s : sitesList) {
                try {
                    if (!s.getLngSite().isEmpty() && !s.getLatSite().isEmpty() && s.getTipoSite() == tipoSite) {
                        double latS = Double.parseDouble(s.getLatSite());
                        double lngS = Double.parseDouble(s.getLngSite());
                        LatLng sydney = new LatLng(latS, lngS);
                        switch (tipoSite){
                            case CODE_DB_TALLER:
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taller01e)));
                                break;

                            case CODE_DB_REPUESTOS:
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.repuestos03e)));
                                break;

                            case CODE_DB_GOMERIA:
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gomeria01e)));
                                break;

                            case CODE_DB_SERVICIOS: // Servicios
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.servicios01e)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                break;

                            case CODE_DB_LAVADERO: //Lavadero //TODO: cambiar a Lubricentros
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icono_lubricentro)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                break;

                            case CODE_DB_VARIOS: //Varios
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.varios01e)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                break;
                        }

                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }
                } catch (Exception e) {

                }
            }
        } else {
            for (Sitio s : sitesList) {
                try {
                    if (!s.getLngSite().isEmpty() && !s.getLatSite().isEmpty()){
                        double latS = Double.parseDouble(s.getLatSite());
                        double lngS = Double.parseDouble(s.getLngSite());
                        LatLng sydney = new LatLng(latS, lngS);
                        switch (s.getTipoSite()){
                            case CODE_DB_TALLER: //Taller
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taller01e)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                break;
                            case CODE_DB_REPUESTOS: //Repuestos
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.repuestos03e)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                break;
                            case CODE_DB_GOMERIA: // Gomería
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gomeria01e)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                break;
                            case CODE_DB_SERVICIOS: // Servicios
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.servicios01e)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                break;
                            case CODE_DB_LAVADERO: //Lavadero
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icono_lubricentro)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                break;
                            default:
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(s.getNombreSite())
                                        .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.varios01e)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }

                    }
                }catch (Exception e){

                }
            }
        }
    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab1.animate().translationX(-getResources().getDimension(R.dimen.standard_55)/2);
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab2.animate().translationX(-getResources().getDimension(R.dimen.standard_55)/2);
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        fab3.animate().translationX(-getResources().getDimension(R.dimen.standard_55)/2);
        fab4.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab4.animate().translationX(+getResources().getDimension(R.dimen.standard_55)/2);
        fab5.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab5.animate().translationX(+getResources().getDimension(R.dimen.standard_55)/2);
        fab6.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        fab6.animate().translationX(+getResources().getDimension(R.dimen.standard_55)/2);
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
        fab4.animate().translationY(0);
        fab5.animate().translationY(0);
        fab6.animate().translationY(0);
        fab1.animate().translationX(0);
        fab2.animate().translationX(0);
        fab3.animate().translationX(0);
        fab4.animate().translationX(0);
        fab5.animate().translationX(0);
        fab6.animate().translationX(0);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        if (pref.getBoolean(Constants.KEY_USER_FINAL, true)){
            getMenuInflater().inflate(R.menu.main, menu);

        } else {
            getMenuInflater().inflate(R.menu.main_pro, menu);
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) { //Mi Filtro
            MenuItem bedMenuItem = menu.findItem(R.id.action_settings);
            if (!miFiltroAplicado){
                bedMenuItem.setTitle("Mi filtro Aplicado");
                miFiltroAplicado = true;
                //TODO filtrar los sitios por marca de vehiculo. Leer cuantos sitios atienden esa marca en la DB y traer el lsitado y solo mostrar esos sitios por id del listado de sitios en el celular.
                PerformNetworkRequest request = new PerformNetworkRequest(
                        Api.URL_READ_SITIOS_POR_MARCATIPO +
                                String.valueOf(pref.getInt(Constants.KEY_USER_VEHICULO_MARCA_ID , 0)) +
                        "&idTipo=" + String.valueOf(pref.getInt(Constants.KEY_USER_VEHICULO_TIPO_ID , 0)),
                        null,
                        CODE_GET_REQUEST);
                request.execute();
            } else {
                bedMenuItem.setTitle("Mi filtro");
                miFiltroAplicado = false;
                //TODO mostrar todos los sitios de nuevo...
                closeFABMenu();
                mMap.clear();
                //fab Talleres: mostrar solo los talleres.
                fabVista = CODE_FAB_FILTRO;
                actDisplayMap(CODE_FAB_FILTRO);

            }

            return true;
        }
        if (id == R.id.buscar) {
            //Buscar especialidades y marcas para agregar al filtro.
            return true;
        }
        if (id == R.id.action_settings_pro){ // Opciones para Pro
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(MainActivity.this,
                            "permission was granted, :)",
                            Toast.LENGTH_LONG).show();//Permission was granted. Do the Location related task.
                    posActGl = GetCurrentLocation();

                    ActualizarMarcas(posActGl);
                }
                else
                {
                    //permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(MainActivity.this,
                            "permission denied, ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent myIntent = new Intent(MainActivity.this, LeerCodigoQR.class);
            if (usersList.size() == 0 && proList.size() == 0){
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            if (usersList.size() > 0){
                if (usersList.get(0).celularUser.isEmpty()){
                    myIntent.putExtra("key", "faltaCelular"); //Optional parameters
                } else {
                    myIntent.putExtra("key", "tieneCelular");
                }
                MainActivity.this.startActivity(myIntent);
            }
            if (proList.size() > 0){
                if (proList.get(0).celularUser.isEmpty()){
                    myIntent.putExtra("key", "faltaCelular"); //Optional parameters
                } else {
                    myIntent.putExtra("key", "tieneCelular");
                }
                MainActivity.this.startActivity(myIntent);
            }

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent myIntent = new Intent(MainActivity.this, UserDataActual.class);
            if (usersList.size() > 0){
                if (usersList.get(0).celularUser.isEmpty()){
                    myIntent.putExtra("key", "faltaCelular"); //Optional parameters
                } else {
                    myIntent.putExtra("key", "tieneCelular");
                }
                MainActivity.this.startActivity(myIntent);
            }
            if (proList.size() > 0){
                if (proList.get(0).celularUser.isEmpty()){
                    myIntent.putExtra("key", "faltaCelular"); //Optional parameters
                } else {
                    myIntent.putExtra("key", "tieneCelular");
                }
                MainActivity.this.startActivity(myIntent);
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //TODO: primero poner la poisción actual y el zoom para ver 1 km a la redonda, luego cargar
        //TODO: los sitios que estén en ese radio y mostrarlos según el filtro de marcas y tipo de Sitio.

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-31.413, -64.191);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Córdoba")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.taller01e)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


//        posActGl = GetCurrentLocation();
//
//        ActualizarMarcas(posActGl);

        // Setting a custom info window adapter for the google map
        SitioInfoWindowAdapter markerInfoWindowAdapter = new SitioInfoWindowAdapter(getApplicationContext());
        googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            posActGl = GetCurrentLocation();
            ActualizarMarcas(posActGl);
            Intent intent = new Intent(getApplicationContext(), GoogleService.class);
            startService(intent);
        }

        googleMap.setOnInfoWindowClickListener(this);

//        if (pref.getBoolean(Constants.KEY_USER_EXISTE, false) && pref.getBoolean(Constants.KEY_USER_FINAL, true)){
//            appUser();
//        } else {
//            appPro();
//        }

    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "elegiste a: " + marker.getTitle(), Toast.LENGTH_SHORT).show();

        Intent myIntent = new Intent(MainActivity.this, ScrollingActivitySitio.class);
        myIntent.putExtra("key", marker.getSnippet()); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }


    private void ActualizarMarcas(double[] posActGl){
        double latAct = posActGl[0];
        double lngAct = posActGl[1];

        readSites();
        readUser();

    }

    private double[] GetCurrentLocation() {
        Share posAct = new Share();

        double[] d = getlocation();
        posAct.setLat(d[0]);
        posAct.setLng(d[1]);

//        mMap
//                .addMarker(new MarkerOptions()
//                        .position(new LatLng(posAct.getLat(), posAct.getLng()))
//                        .title("Current Location"));

        mMap
                .animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(posAct.getLat(), posAct.getLng()), 15));
        return d;
    }

    public double[] getlocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;
        for (int i = 0; i < providers.size(); i++) {
            // verifico si el usuario dio los permisos para la camara
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // verificamos la version de Android que sea al menos la M para mostrar
                    // el dialog de la solicitud de la camara
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) ;
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return null;
            } else {
                try {
                    mMap.setMyLocationEnabled(true);
                    l = lm.getLastKnownLocation(providers.get(i));
                } catch (Exception ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            if (l != null)
                break;
        }
        double[] gps = new double[2];

        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }

    private void readSites() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_SITIOS, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void readUser() {
        if (pref.getBoolean("key_existe", false)){
            if (pref.getBoolean(Constants.KEY_USER_FINAL, true)){
                PerformNetworkRequest request = new PerformNetworkRequest(
                        Api.URL_READ_USER + "'" + pref.getString("key_email", "test@test.com") +"'",
                        null,
                        CODE_GET_REQUEST);
                request.execute();
            } else {
                PerformNetworkRequest request = new PerformNetworkRequest(
                        Api.URL_READ_PRO + "'" + pref.getString("key_email", "test@test.com") +"'",
                        null,
                        CODE_GET_REQUEST);
                request.execute();
            }

        } else {
            Intent myIntent = new Intent(MainActivity.this, UserDataActual.class);
            myIntent.putExtra("key", "registrarUser"); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        }
//        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_USER + "'test@test.com'", null, CODE_GET_REQUEST);
//        request.execute();
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
                    //obj.getString("direccion"),
                    //obj.getString("descripcion"),
                    //obj.getString("telefono"),
                    //obj.getInt("provincia_id"),
                    obj.getString("latitud"),
                    obj.getString("longitud"),
                    //obj.getString("notas"),
                    //obj.getString("created_at"),
                    //obj.getString("updated_at"),
                    obj.getInt("tipoSitio"),
                    obj.getString("token_uno"),
                    obj.getString("token_dos"),
                    obj.getString("token_tres"),
                    obj.getInt("idContrato"),
                    obj.getInt("idZona")
            ));
        }

        for (Sitio s : sitesList) {
            try {
                if (!s.getLngSite().isEmpty() && !s.getLatSite().isEmpty()){
                    double latS = Double.parseDouble(s.getLatSite());
                    double lngS = Double.parseDouble(s.getLngSite());
                    LatLng sydney = new LatLng(latS, lngS);
                    switch (s.getTipoSite()){
                        case 1: //Taller
                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title(s.getNombreSite())
                                    .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taller01e)));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            break;
                        case 2: //Repuestos
                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title(s.getNombreSite())
                                    .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.repuestos03e)));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            break;
                        case 3: // Gomería
                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title(s.getNombreSite())
                                    .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.gomeria01e)));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            break;
                        case 4: // Servicios
                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title(s.getNombreSite())
                                    .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.servicios01e)));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            break;
                        case 5: //Lavadero // TODO: Cambiar por Lubricentros.
                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title(s.getNombreSite())
                                    .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icono_lubricentro)));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            break;
                        default:
                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title(s.getNombreSite())
                                    .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taller01e)));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }

                }
            }catch (Exception e){

            }


        }
    }

    private void refreshUser(JSONArray users) throws JSONException {

        //clearing previous heroes
        usersList.clear();
        proList.clear();

        if (pref.getBoolean(Constants.KEY_USER_FINAL, true)){
            //traversing through all the items in the json array
            //the json we got from the response
            for (int i = 0; i < users.length(); i++) {
                //getting each hero object
                JSONObject obj = users.getJSONObject(i);

                //adding the hero to the list
                usersList.add(new User(
                        obj.getInt("id"),
                        obj.getString("nombre"),
                        obj.getString("apellido"),
                        obj.getString("email"),
                        obj.getString("created_at"),
                        obj.getString("celular"),
                        obj.getInt("vehiId")
                ));
            }
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean(KEY_USER_EXISTE, true); // Storing boolean - true/false
            editor.putInt(KEY_USER_ID, usersList.get(0).getIdUser()); // Storing string
            editor.putString(KEY_USER_NOMBRE, usersList.get(0).getNombreUser()); // Storing integer
            editor.putString(KEY_USER_APELLIDO, usersList.get(0).getApellidoUser()); // Storing float
            editor.putString(KEY_USER_EMAIL, usersList.get(0).getEmailUser()); // Storing long
            editor.putString(KEY_USER_CREATED_AT, usersList.get(0).getCreadoUser()); // Storing string
            editor.putString(KEY_USER_CELULAR, usersList.get(0).getCelularUser()); // Storing integer
            editor.putInt(KEY_USER_VEHICULO_ID, usersList.get(0).getVehiId()); // Storing float

            editor.commit(); // commit changes
        } else {
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
            editor.putInt(Constants.KEY_USER_ID, proList.get(0).getIdUser()); // Storing string
            editor.putString(Constants.KEY_USER_NOMBRE, proList.get(0).getNombreUser()); // Storing integer
            editor.putString(Constants.KEY_USER_APELLIDO, proList.get(0).getApellidoUser()); // Storing float
            editor.putString(Constants.KEY_USER_EMAIL, proList.get(0).getEmailUser()); // Storing long
            editor.putString(Constants.KEY_USER_CREATED_AT, proList.get(0).getCreadoUser()); // Storing string
            editor.putString(Constants.KEY_USER_CELULAR, proList.get(0).getCelularUser()); // Storing integer
            editor.putInt(Constants.KEY_USER_PRO_SITIOID, proList.get(0).getVehiId());
//        editor.putInt(Constants.KEY_USER_PRO_PASSWORD, proList.get(0).getPassword());

            editor.commit(); // commit changes
        }



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
                    case Api.URL_READ_SITIOS + "=":
                        JSONObject objectSitios = new JSONObject(s);
                        if (!objectSitios.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectSitios.getString("message") + ": sitios!", Toast.LENGTH_SHORT).show();
                            //refreshing the herolist after every operation
                            //so we get an updated list
                            //we will create this method right now it is commented
                            //because we haven't created it yet
                            //para generar el release-1.0

                            refreshSitesList(objectSitios.getJSONArray("sitios"));
                        }
                        break;
                    case Api.URL_READ_HEROES + "=":
                        JSONObject objectHeroes = new JSONObject(s);
                        if (!objectHeroes.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectHeroes.getString("message") + ": sitios", Toast.LENGTH_SHORT).show();
                            //refreshing the herolist after every operation
                            //so we get an updated list
                            //we will create this method right now it is commented
                            //because we haven't created it yet

                            refreshSitesList(objectHeroes.getJSONArray("heroes"));
                        }
                        break;
                    case Api.URL_READ_USER:
                    case Api.URL_READ_PRO:
                        JSONObject objectUser = new JSONObject(s);
                        if (!objectUser.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectUser.getString("message") + ": user", Toast.LENGTH_SHORT).show();
                            refreshUser(objectUser.getJSONArray("users"));
                        }
                        break;
                    case Api.URL_READ_SITIOS_POR_MARCATIPO:
                        JSONObject objectSitiosId = new JSONObject(s);
                        if (!objectSitiosId.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), objectSitiosId.getString("message") + ": sitios!", Toast.LENGTH_SHORT).show();
                            //refreshing the herolist after every operation
                            //so we get an updated list
                            //we will create this method right now it is commented
                            //because we haven't created it yet

                            refreshSitesMiFiltro(objectSitiosId.getJSONArray("sitiosId"));
                        }
                        break;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshSitesMiFiltro(JSONArray heroes) throws JSONException {
        //clearing previous sitios filtrados
        miFiltroList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);
            //adding the hero to the list
            //miFiltroList.add(i*3, String.valueOf(obj.getInt("id")));
            miFiltroList.add(i, String.valueOf(obj.getInt("tallerId")));
            //miFiltroList.add(i*3+2, String.valueOf(obj.getInt("marcaid")));
        }

        if (miFiltroList.size() > 0){
            mMap.clear();
            for (Sitio s : sitesList) {
                try {
                    if (!s.getLngSite().isEmpty() && !s.getLatSite().isEmpty()){
                        double latS = Double.parseDouble(s.getLatSite());
                        double lngS = Double.parseDouble(s.getLngSite());
                        LatLng sydney = new LatLng(latS, lngS);
                        if (s.getTipoSite() == pref.getInt(Constants.KEY_USER_VEHICULO_TIPO_ID, 0) &&
                                miFiltroList.contains(String.valueOf(s.getIdSite()))){
                            switch (s.getTipoSite()){
                                case 1: //Taller
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(s.getNombreSite())
                                            .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.taller01e)));
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                    break;
                                case 2: //Repuestos
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(s.getNombreSite())
                                            .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.repuestos03e)));
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                    break;
                                case 3: // Gomería
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(s.getNombreSite())
                                            .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gomeria01e)));
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                    break;
                                case 4: // Servicios
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(s.getNombreSite())
                                            .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.servicios01e)));
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                    break;
                                case 5: //Lavadero // TODO: Cambiar por Lubricentros.
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(s.getNombreSite())
                                            .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icono_lubricentro)));
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                    break;
                                default:
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(s.getNombreSite())
                                            .snippet(s.getIdContrato()+"@"+s.getIdSite()+"@"+s.getIdZona()+"@"+s.getTokenNotiUno())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.taller01e)));
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning = false;
    }


    //inner class to perform network request extending an AsyncTask
    private static class PerformNetworkRequestPro extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequestPro(String url, HashMap<String, String> params, int requestCode) {
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
                            //Toast.makeText(getApplicationContext(), objectSitio.getString("message") + ": sitio!", Toast.LENGTH_SHORT).show();
                            //refreshing the herolist after every operation
                            //so we get an updated list
                            //we will create this method right now it is commented
                            //because we haven't created it yet

                            refreshSitesListPro(objectSitio.getJSONArray("sitio"));
                        }
                        break;
                    case Api.URL_READ_SITIOS_ESPECIAL:
                        JSONObject objectEspecial = new JSONObject(s);
                        if (!objectEspecial.getBoolean("error")) {
                            //Toast.makeText(getApplicationContext(), objectEspecial.getString("message") + ": espec", Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(MainActivity.this, objectUser.getString("message") + ": certificados!", Toast.LENGTH_SHORT).show();
                            refreshCertifList(objectUser.getJSONArray("certif"));
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void refreshCertifList(JSONArray certif) throws JSONException {
        htmlAsString = new StringBuilder().append(htmlAsString).append("<p>Certificado: ").toString();

        for (int i = 0; i < certif.length(); i++) {
            //getting each hero object
            JSONObject obj = certif.getJSONObject(i);
            htmlAsString = new StringBuilder().append(htmlAsString).append(obj.getString("nombre")).append(", ").toString();
        }

        htmlAsString =  new StringBuilder().append(htmlAsString).append("</p><br></body></html>").toString();

        wvDisplay.loadData( htmlAsString, "text/html; UTF-8", null);
    }

    private static void refreshEspecialList(JSONArray especial) throws JSONException {
        htmlAsString = new StringBuilder().append(htmlAsString).append("<p>Especialidad: ").toString();

        for (int i = 0; i < especial.length(); i++) {
            //getting each hero object
            JSONObject obj = especial.getJSONObject(i);
            htmlAsString = new StringBuilder().append(htmlAsString).append(obj.getString("nombre")).append(", ").toString();
        }

        htmlAsString =  new StringBuilder().append(htmlAsString).append("</p><br></body></html>").toString();

        wvDisplay.loadData( htmlAsString, "text/html; UTF-8", null);

        PerformNetworkRequestPro request = new PerformNetworkRequestPro(
                Api.URL_READ_SITIO_CERTIF + pref.getInt(Constants.KEY_USER_PRO_SITIOID, 0),
                null,
                CODE_GET_REQUEST);
        request.execute();
    }

    private static void refreshSitesListPro(JSONArray heroes) throws JSONException {
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



//        TextView tvDisplay = findViewById(R.id.tvDescrip);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvDisplay.setText(Html.fromHtml("<h1>Title</h1><br><h2>Description here</h2><br><p>Bienvenido: " + MainActivity.proList.get(0).getApellidoUser() + "</p>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvDisplay.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
        }

       // WebView wvDisplay = findViewById(R.id.wvDisplay);

        htmlAsString =  "<html><head><meta http-equiv='Content-Type' content='text/html' charset='UTF-8' /></head><body>"
                +"<h1>"+sitesList.get(0).getNombreSite()+"</h1><br>"
                +"<h2>"+sitesList.get(0).getDescSite()+"</h2><br>"
                +"<p>Bienvenido "+MainActivity.proList.get(0).getNombreUser()+", esta es la mejor forma para mostrar tus servicios a tus Clientes.</p><br>"
                +"<p>"
                +"Dirección: "+sitesList.get(0).getDireSite()+"<br>"
                +"Telefono: "+sitesList.get(0).getTeleSite()+"<br>"
                +"email: "+sitesList.get(0).getEmailSite()+"<br>"
                +"web: "+"<a href=\""+sitesList.get(0).getWebSite()+"\">"+sitesList.get(0).getWebSite()+"</a><br>"
                +"redes sociales: "+sitesList.get(0).getRedSocialSite()
                +"</p><br>";

        wvDisplay.loadDataWithBaseURL(null, htmlAsString, "text/html", "UTF-8", null);



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

        PerformNetworkRequestPro request = new PerformNetworkRequestPro(
                Api.URL_READ_SITIOS_ESPECIAL + pref.getInt(Constants.KEY_USER_PRO_SITIOID, 0),
                null,
                CODE_GET_REQUEST);
        request.execute();

    }

    private static void updateFoto(String fotoNombre) {
        String URL = Api.ROOT_URL_IMAGES+fotoNombre;
        CargaImagenes nuevaTarea = new CargaImagenes();
        nuevaTarea.execute(URL);
    }

    private static class CargaImagenes extends AsyncTask<String, Void, Bitmap>{

//        ProgressDialog pDialog;
        Context context = GlobalApplication.getAppContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

//            pDialog = new ProgressDialog(context);
//            pDialog.setMessage("Cargando Imagen");
//            pDialog.setCancelable(true);
//            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            pDialog.show();

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
//            pDialog.dismiss();
        }

    }

    private static Bitmap descargarImagen(String imageHttpAddress){
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
