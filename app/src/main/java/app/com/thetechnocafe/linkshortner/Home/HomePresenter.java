package app.com.thetechnocafe.linkshortner.Home;

import android.widget.Toast;

import app.com.thetechnocafe.linkshortner.Networking.NetworkService;
import app.com.thetechnocafe.linkshortner.Utilities.AuthPreferences;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gurleensethi on 01/03/17.
 */

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mMainView;

    @Override
    public void attachView(HomeContract.View view) {
        mMainView = view;
        mMainView.initViews();

        getListOfLinksForAccount();
    }

    @Override
    public void detachView() {
        mMainView = null;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    private void getListOfLinksForAccount() {
        NetworkService.getInstance()
                .getLinkShortenerAPI()
                .getListOfShortenedLinks(AuthPreferences.getInstance().getAuthToken(mMainView.getAppContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shortenedLinks -> {
                    mMainView.onShortLinksReceived(shortenedLinks.getShortenedLinks());
                }, throwable -> {
                    throwable.printStackTrace();
                    Toast.makeText(mMainView.getAppContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                });
    }
}
