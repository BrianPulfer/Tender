package ch.usi.tender;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.webkit.WebView;
import android.widget.Toast;

import android.database.sqlite.*;

import ch.usi.tender.places.PlacesAPI;

public class MainActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "Tender Database";
    private AppBarConfiguration mAppBarConfiguration;

    private static final int PERMISSION_REQUESTS_CODE = 27;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private static PlacesAPI placesAPI;

    private WebView currentPicture;

    private SQLiteDatabase db;

    private boolean pictureInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        initDatabase();
    }

    public PlacesAPI getPlacesAPI(){
        return placesAPI;
    }

                            /** Database related methods */
    private void initDatabase() {
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Visited(Name VARCHAR, Reference VARCHAR, LatLon VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Liked(Name VARCHAR, Reference VARCHAR, LatLon VARCHAR);");
    }

    private void insertVisit(String name, String reference, String latlon){
        String command = "INSERT INTO Visited VALUES('"+name+"','"+reference+"','"+latlon+"');";
        db.execSQL(command);
    }

    private void insertLike(String name, String reference, String latlon){
        String command = "INSERT INTO Liked VALUES('"+name+"','"+reference+"','"+latlon+"');";
        db.execSQL(command);
    }

    private void deleteAll(){
        if (db != null) {
            db.delete("Visited", null, null);
            db.delete("Liked", null, null);
        }
    }

    public Cursor selectAllVisits(){
        return db.rawQuery("SELECT * FROM Visited", null);
    }
    public Cursor selectAllLikes(){ return db.rawQuery("SELECT * FROM Liked", null);}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setPicture(WebView view){
        /**
         * Initializes the PlacesAPI object with the passed parameter 'view'.
         *
         * After this initialization, the tracking of the location starts and image can be put
         * in the passed view.
         * */

        if (!pictureInitialized) {
            this.currentPicture = view;
            this.placesAPI = new PlacesAPI(currentPicture);

            this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            this.locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    placesAPI.getPhotos(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };
            checkPermission();

            pictureInitialized = true;
        }
    }

    @SuppressLint("MissingPermission")
    private void startUpdatingLocation(){
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this.locationListener);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, PERMISSION_REQUESTS_CODE);
                return;
            }
        }
        startUpdatingLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUESTS_CODE:
                if(grantResults.length > 0 && permissions[0].equals(PackageManager.PERMISSION_GRANTED)){
                    startUpdatingLocation();
                }
        }
    }

    public void dislike(View view) {
        /** Method called when users press on the dislike (cross) button
         *
         *
         * The disliked picture's reference, as well as the restaurant name and string containing
         * latitude and longitude are stored in the database in the 'Visited' table.
         *
         * The next picture in the list is then showed.
         */


        placesAPI.showNext();

        if(placesAPI.getCurrentPhotoName() != null && placesAPI.getCurrentPhotoReference() != null && placesAPI.getCurrentLatLon() != null)
            insertVisit(placesAPI.getCurrentPhotoName(), placesAPI.getCurrentPhotoReference(), placesAPI.getCurrentLatLon());
    }

    public void like(View view){
        /** Method called when users press on the like (heart) button
         *
         *
         * The liked picture's reference, as well as the restaurant name and string containing
         * latitude and longitude are stored in the database in both tables 'Visited' and 'Liked'.
         *
         * An improvement would be to just store in the 'Liked' table.
         */

        Toast.makeText(this, placesAPI.getCurrentPhotoName(), Toast.LENGTH_SHORT).show();

        if(placesAPI.getCurrentPhotoName() != null && placesAPI.getCurrentPhotoReference() != null && placesAPI.getCurrentLatLon() != null) {
            insertVisit(placesAPI.getCurrentPhotoName(), placesAPI.getCurrentPhotoReference(), placesAPI.getCurrentLatLon());
            insertLike(placesAPI.getCurrentPhotoName(), placesAPI.getCurrentPhotoReference(), placesAPI.getCurrentLatLon());
        }
    }

    public void goToRestaurant(View view){
        /** Method called when users press on the like dish (or restaurant) picture
         *
         *
         * An activity to deal with location is started using an implicit intent.
         * The location that is going to be displayed is the one relative to the restaurant position
         */

        placesAPI.startLocationIntent(this);
    }
}
