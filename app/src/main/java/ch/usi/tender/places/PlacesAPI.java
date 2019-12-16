package ch.usi.tender.places;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import ch.usi.tender.places.tasks.GetReferencesTask;

public class PlacesAPI {

    //TODO: Put your own Google places API Key
    public static final String API_KEY = YOUR_API_KEY;

    private ArrayList<String> currentReferences = new ArrayList<>();
    private ArrayList<String> currentReferencesNames = new ArrayList<>();
    private ArrayList<String> currentLatLons = new ArrayList<>();
    private WebView view;

    private String currentPhotoName;
    private String currentPhotoReference;
    private String currentLatLon;

    public PlacesAPI(WebView view){
        this.view = view;
    }

    public void getPhotos(Location location){
        /**
         * Given a location, this method fills the internal list of references links to pictures
         * close to the given location. Also names and coordinates are retrieved.
         * */
        try {
            boolean notDisplayingDishes = (currentReferences.size() == 0);
            ArrayList<String>[] refs = new GetReferencesTask().execute(location).get();

            currentReferences = refs[0];
            currentReferencesNames = refs[1];
            currentLatLons = refs[2];

            if (notDisplayingDishes)
                showNext();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNext(){
        /**
         * Updates the view to display the next image in the references list
         * */
        try {

            // TODO: Remove
            Log.d("Brian", "Left references: "+currentReferences.size());

            if (currentReferences.size() > 0) {
                String urlString = "https://maps.googleapis.com/maps/api/place/photo?"
                                    + "key="+ PlacesAPI.API_KEY
                                    + "&maxwidth=1000"
                                    + "&photoreference="+currentReferences.get(0);

                currentPhotoName = currentReferencesNames.get(0);
                currentPhotoReference = currentReferences.get(0);
                currentLatLon = currentLatLons.get(0);

                currentReferencesNames.remove(currentPhotoName);
                currentReferences.remove(currentPhotoReference);
                currentLatLons.remove(currentLatLon);

                view.loadUrl(urlString);
                view.setInitialScale(100);

                // TODO: Remove
                Log.d("Brian", "Getting photo at link: "+urlString);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getCurrentPhotoName(){
        return this.currentPhotoName;
    }

    public String getCurrentPhotoReference(){
        return this.currentPhotoReference;
    }

    public String getCurrentLatLon(){return this.currentLatLon;}

    public void startLocationIntent(Context context) {
        /**
         * Given a context, uses the context to start an implicit intent to access the location of
         * the current picture
         *
         */

        String lat = currentLatLon.split(" ")[0];
        String lon = currentLatLon.split(" ")[1];

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:"+lat+","+lon+"?z=20"));

        if (intent.resolveActivity(context.getPackageManager()) != null){
            context.startActivity(intent);
        }
    }
}
