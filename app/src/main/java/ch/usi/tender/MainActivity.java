package ch.usi.tender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ch.usi.tender.places.PlacesAPI;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUESTS_CODE = 27;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private PlacesAPI placesAPI;

    private ImageView currentPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.currentPicture = (ImageView) findViewById(R.id.dishpic);
        this.placesAPI = new PlacesAPI(currentPicture);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                placesAPI.getPhotos(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        checkPermission();
    }

    @SuppressLint("MissingPermission")
    private void startUpdatingLocation(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
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
        placesAPI.showNext();
    }

    public void like(View view){ Toast.makeText(this, placesAPI.getCurrentPhotoName(), Toast.LENGTH_SHORT).show();}
}
