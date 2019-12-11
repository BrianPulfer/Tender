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
        if(location == null) {
            return null;
        }

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        String requestURL =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "location=" + lat + "," + lon +
                        "&radius=500" +
                        "&types=food" +
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

        ArrayList<String> references = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        for(int i = 1; i<refs.length; i++){
            String reference = refs[i].split("\"")[0];
            String name = nms[i].split("\"")[0];

            references.add(reference);
            names.add(name);
        }

        //TODO: REMOVE
        Log.d("Brian", content);

        return new ArrayList[]{references, names};
    }
}
