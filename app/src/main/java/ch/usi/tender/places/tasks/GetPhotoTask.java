package ch.usi.tender.places.tasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GetPhotoTask extends AsyncTask<String, Void, Drawable> {

    private static final String BASE_NAME = "Drawable";
    private static int RESOURCE_COUNT = 1;

    @Override
    protected Drawable doInBackground(String... strings) {
        try {
            InputStream is = (InputStream) new URL(strings[0]).getContent();
            return Drawable.createFromStream(is, BASE_NAME + (RESOURCE_COUNT++));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
