package ch.usi.tender.places;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

import ch.usi.tender.places.tasks.GetPhotoTask;
import ch.usi.tender.places.tasks.GetReferencesTask;

public class PlacesAPI {

    // public static final String API_KEY = "AIzaSyBGMkWa7FQ_G11Iabtzn3NsM23x2CYZ3ws";
    public static final String API_KEY = "AIzaSyCTVC00X_jCbBWmW-9QFdd73caD8NnxSzA";

    private ArrayList<String> currentReferences = new ArrayList<>();
    private ImageView view;

    public PlacesAPI(ImageView view){
        this.view = view;
    }

    public void getPhotos(Location location){
        try {
            boolean notDisplayingDishes = (currentReferences.size() == 0);
            currentReferences = new GetReferencesTask().execute(location).get();

            if (notDisplayingDishes)
                    showNext();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNext(){
        try {
            if (currentReferences.size() > 0) {
                String urlString = "https://maps.googleapis.com/maps/api/place/photo?"
                                    + "key="+PlacesAPI.API_KEY
                                    + "&maxwidth=1000"
                                    + "&photoreference="+currentReferences.get(0);
                currentReferences.remove(currentReferences.get(0));


                Drawable newImage = new GetPhotoTask().execute(urlString).get();
                view.setImageDrawable(newImage);

                // TODO: Remove
                Log.d("Brian", "Displaying next");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
