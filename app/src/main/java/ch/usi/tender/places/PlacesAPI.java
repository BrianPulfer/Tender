package ch.usi.tender.places;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ch.usi.tender.places.tasks.GetPhotoTask;
import ch.usi.tender.places.tasks.GetReferencesTask;

public class PlacesAPI {

    public static final String API_KEY = "AIzaSyDsBrzeCuJU_rJFhGBZty_d_4WVleEK_5c";
    //public static final String API_KEY = "AIzaSyCTVC00X_jCbBWmW-9QFdd73caD8NnxSzA";
    //public static final String API_KEY = "AIzaSyA7TgHqPLPBSvF-ZBIVc8HWf-EorXcgTiA";

    private ArrayList<String> currentReferences = new ArrayList<>();
    private ArrayList<String> currentReferencesNames = new ArrayList<>();
    private ImageView view;

    public PlacesAPI(ImageView view){
        this.view = view;
    }

    public void getPhotos(Location location){
        try {
            boolean notDisplayingDishes = (currentReferences.size() == 0);
            ArrayList<String>[] refs = new GetReferencesTask().execute(location).get();

            currentReferences = refs[0];
            currentReferencesNames = refs[1];

            if (notDisplayingDishes)
                showNext();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNext(){
        try {
            //TODO REMOVE
            Log.d("Brian", "Current references: "+currentReferences.size());

            if (currentReferences.size() > 0) {
                String urlString = "https://maps.googleapis.com/maps/api/place/photo?"
                                    + "key="+ PlacesAPI.API_KEY
                                    + "&maxwidth=1000"
                                    + "&photoreference="+currentReferences.get(0);

                //TODO REMOVE
                Log.d("Brian", "URL: "+urlString);

                currentReferences.remove(currentReferences.get(0));
                currentReferencesNames.remove(currentReferencesNames.get(0));


                Drawable newImage = new GetPhotoTask().execute(urlString).get();
                view.setImageDrawable(newImage);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getCurrentPhotoName(){
        if (this.currentReferencesNames.size() > 0)
            return this.currentReferencesNames.get(0);
        return null;
    }
}
