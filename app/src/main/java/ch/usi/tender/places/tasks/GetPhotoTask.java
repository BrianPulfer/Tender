package ch.usi.tender.places.tasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Unused class
 *
 *
 * This class had the purpose of obtaining a drawable given an URL.
 * The class isn't used anymore because most of the Google Places Photos API references re-direct
 * to other URLs, and most of the times the returned Drawables were null.
 *
 * For this reason, a WebView is used instead of a ImageView (see ui/home/HomeFragment).
 *
 * */

public class GetPhotoTask extends AsyncTask<String, Void, Drawable> {

    private static final String BASE_NAME = "Drawable";
    private static int RESOURCE_COUNT = 1;

    @Override
    protected Drawable doInBackground(String... strings) {
        try {
            return loadImageFromUrl(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final int BUFFER_IO_SIZE = 8000;

    private Drawable loadImageFromUrl(final String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        conn.connect();

        return Drawable.createFromStream(conn.getInputStream(), BASE_NAME+(RESOURCE_COUNT++));
    }

    /*
    private Bitmap loadImageFromUrl(final String url) {
        try {
            // Addresses bug in SDK :
            // http://groups.google.com/group/android-developers/browse_thread/thread/4ed17d7e48899b26/
            URLConnection connection = new URL(url).openConnection();
            connection.connect();

            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream(), BUFFER_IO_SIZE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos, BUFFER_IO_SIZE);
            copy(bis, bos);
            bos.flush();

            return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
        } catch (IOException e) {
            // handle it properly
        }
        return null;
    }
    */

    private void copy(final InputStream bis, final OutputStream baos) throws IOException {
        byte[] buf = new byte[256];
        int l;
        while ((l = bis.read(buf)) >= 0) baos.write(buf, 0, l);
    }


}
