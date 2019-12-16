package ch.usi.tender.ui.recently_visited;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import ch.usi.tender.MainActivity;
import ch.usi.tender.R;
import ch.usi.tender.places.PlacesAPI;

public class RecentlyVisitedFragment extends Fragment {

    /**
     * This fragment displays every liked or disliked photo with the relative name.
     *
     * When clicking on a picture, the user is sent an implicit intent to open the restaurant's
     * location
     */

    private RecentlyVisitedViewModel slideshowViewModel;

    private LinearLayout linearLayout;

    private final int PIC_WIDTH = 800;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(RecentlyVisitedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recently_visited, container, false);

        this.linearLayout = root.findViewById(R.id.linear_layout_rv);
        displayVisited();

        return root;
    }

    private void displayVisited(){
        /**
         * Retrieves all the visited photo references + restaurants names + coordinates.
         * Displays the pictures and the restaurant names.
         */
        Cursor cursor = ((MainActivity)this.getActivity()).selectAllVisits();

        while(cursor.moveToNext()){
            String restaurantName = cursor.getString(0);
            String photoReference = cursor.getString(1);
            String latLon = cursor.getString(2);

            String restaurantLat = latLon.split(" ")[0];
            String restaurantLon = latLon.split(" ")[1];

            String URL = "https://maps.googleapis.com/maps/api/place/photo?"
                    + "key="+ PlacesAPI.API_KEY
                    + "&maxwidth="+PIC_WIDTH
                    + "&photoreference="+photoReference;

            TextView text = new TextView(getContext());
            text.setText(restaurantName);
            setPreferredTextAttributes(text);

            WebView photo = new WebView(getContext());
            setPreferredPhotoAttributes(photo, restaurantLat, restaurantLon);
            photo.loadUrl(URL);

            Button button = new Button(getContext());
            button.setText("Get there!");
            setPreferredPhotoAttributes(button);

            this.linearLayout.addView(text);
            this.linearLayout.addView(photo);
            this.linearLayout.addView(button);

        }
    }

    private void setPreferredPhotoAttributes(Button button) {
        button.setTextSize(16);
        button.setGravity(Gravity.CENTER);
    }

    private void setPreferredTextAttributes(TextView text) {
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);
    }

    private void setPreferredPhotoAttributes(WebView photo, final String lat, final String lon) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PIC_WIDTH, PIC_WIDTH);
        params.gravity = Gravity.CENTER;

        photo.setInitialScale(100);
        photo.setWebViewClient(new WebViewClient());

        photo.setLayoutParams(params);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:"+lat+","+lon+"?z=20"));

                if (intent.resolveActivity(getActivity().getPackageManager()) != null){
                    getActivity().startActivity(intent);
                }
            }
        });
    }
}