package ch.usi.tender.ui.recently_visited;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecentlyVisitedViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecentlyVisitedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}