package ch.usi.tender.ui.bookmarks;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ch.usi.tender.MainActivity;
import ch.usi.tender.R;
import ch.usi.tender.places.PlacesAPI;

public class BookMarksFragment extends Fragment {


    /**
     * Class responsible for displaying all the liked images and restaurant names.
     *
     * When a user clicks on the picture, is redirected to a map displaying the position of the
     * restaurant through an implicit intent.
     */

    private BookMarksViewModel galleryViewModel;

    private LinearLayout linearLayout;

    private final int PIC_WIDTH = 800;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(BookMarksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        this.linearLayout = root.findViewById(R.id.linearlayout);
        displayVisited();

        return root;
    }

    private void displayVisited(){
        /**
         * Retrieves every liked restaurant photo reference + name + coordinates.
         * Displays names and photos to the user.
         */

        Cursor cursor = ((MainActivity)this.getActivity()).selectAllLikes();

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

            WebView photo = new WebView(getContext());
            setPreferredPhotoAttributes(photo, restaurantLat, restaurantLon);
            photo.loadUrl(URL);

            TextView text = new TextView(getContext());
            text.setText(restaurantName);
            setPreferredTextAttributes(text);

            this.linearLayout.addView(photo);
            this.linearLayout.addView(text);
        }
    }

    private void setPreferredTextAttributes(TextView text) {
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);
    }

    private void setPreferredPhotoAttributes(WebView photo, final String lat, final String lon) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PIC_WIDTH, PIC_WIDTH);
        params.gravity = Gravity.CENTER;



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