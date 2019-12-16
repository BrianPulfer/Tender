package ch.usi.tender.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ch.usi.tender.MainActivity;
import ch.usi.tender.R;
import ch.usi.tender.places.PlacesAPI;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Sets the view to the main activity and starts the location tracking.
        Activity activity = getActivity();

        WebView view = (WebView) root.findViewById(R.id.webview);
        view.setWebViewClient(new WebViewClient());
        view.setInitialScale(100);

        ((MainActivity)activity).setPicture(view);

        return root;
    }
}