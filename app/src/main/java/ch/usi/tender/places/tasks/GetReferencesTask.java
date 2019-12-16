package ch.usi.tender.places.tasks;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ch.usi.tender.places.PlacesAPI;

public class GetReferencesTask extends AsyncTask<Location, Void, ArrayList<String>[]> {

    /**
     * Class responsable for retrieving references to photos given a location.
     * The photo references should point to photos that are close to the given locaiton.
     */


    @Override
    protected ArrayList<String>[] doInBackground(Location... locations) {
        try {
            return getPhotosReferences(locations[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String>[] getPhotosReferences(Location location) throws IOException {
        /**
         * Given the location, returns 3 ArrayLists of Strings:
         *      The references to the photos
         *      The names of the restaurants
         *      The latitude and longitude under form of string
         *
         * The returned arrays are sorted such that the first element of each array refers to the
         * first restaurant and so on.
         */

        if(location == null) {
            return null;
        }

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        // TODO: Consider changing 'types' to 'food'
        String requestURL =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "location=" + lat + "," + lon +
                        "&radius=500" +
                        "&types=restaurant" +
                        "&key=" + PlacesAPI.API_KEY;


        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;

        while((line = br.readLine()) != null){
            sb.append(line);
        }

        String content = sb.toString();
        String[] refs = content.split("\"photo_reference\" : \"");
        String[] nms = content.split("\"name\" : \"");
        String[] lats = content.split("\"lat\" : ");
        String[] lons = content.split("\"lng\" : ");

        // TODO: REMOVE
        Log.d("Brian", "Photo references: "+ (refs.length-1) + ". Names:: "+(nms.length-1));

        ArrayList<String> references = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> latLons = new ArrayList<>();

        for(int i = 1; i<refs.length-1; i++){
            String latitude = lats[3*(i) + 1].split(",")[0];
            String longitude = lons[3*(i) + 1].split(" ")[0];

            String reference = refs[i].split("\"")[0];
            String name = nms[i].split("\"")[0];
            String latLon = latitude+" "+longitude;

            references.add(reference);
            names.add(name);
            latLons.add(latLon);
        }

        //TODO: REMOVE
        Log.d("Brian", content);

        return new ArrayList[]{references, names, latLons};
    }
}
