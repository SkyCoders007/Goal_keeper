package com.mxi.goalkeeper.mudule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.mxi.goalkeeper.controller.ErrorDialogHandler;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;
import com.mxi.goalkeeper.controller.AsyncTaskTokenController;
import com.mxi.goalkeeper.controller.IntentServiceTokenController;
import com.mxi.goalkeeper.controller.ProgressDialogController;

/**
 * A dagger-free simple way to handle dependencies in the Example project. Most of this work would
 * ordinarily be done in a module class.
 */
public class DependencyHandler {

    /*
     * Change this to your publishable key.
     *
     * You can get your key here: https://manage.stripe.com/account/apikeys
     */
    private static final String PUBLISHABLE_KEY = "pk_test_DBFcXCOSiVe4hfJARYWwlDPr";

    private AsyncTaskTokenController mAsyncTaskController;
    private CardInputWidget mCardInputWidget;
    private Context mContext;
    private ErrorDialogHandler mErrorDialogHandler;
    private IntentServiceTokenController mIntentServiceTokenController;
    private ProgressDialogController mProgresDialogController;

    public DependencyHandler(
            AppCompatActivity activity,
            CardInputWidget cardInputWidget
    ) {

        mCardInputWidget = cardInputWidget;
        mContext = activity.getBaseContext();

        mProgresDialogController =
                new ProgressDialogController(activity.getSupportFragmentManager());

        mErrorDialogHandler = new ErrorDialogHandler(activity.getSupportFragmentManager());
    }

    /**
     * Attach a listener that creates a token using the {@link android.os.AsyncTask}-based method.
     * Only gets attached once, unless you call {@link #clearReferences()}.
     *
     * @param button a button that, when clicked, gets a token.
     * @return a reference to the {@link AsyncTaskTokenController}
     */
    @NonNull
    public AsyncTaskTokenController attachAsyncTaskTokenController(Button button) {
        if (mAsyncTaskController == null) {
            mAsyncTaskController = new AsyncTaskTokenController(
                    button,
                    mCardInputWidget,
                    mContext,
                    mErrorDialogHandler,
                    mProgresDialogController,
                    PUBLISHABLE_KEY);
        }
        return mAsyncTaskController;
    }

    /**
     * Attach a listener that creates a token using an {@link android.app.IntentService} and the
     * synchronous {@link com.stripe.android.Stripe#createTokenSynchronous(Card, String)} method.
     * <p/>
     * Only gets attached once, unless you call {@link #clearReferences()}.
     *
     * @param button a button that, when clicked, gets a token.
     * @return a reference to the {@link IntentServiceTokenController}
     */
    @NonNull
    public IntentServiceTokenController attachIntentServiceTokenController(
            AppCompatActivity appCompatActivity,
            Button button) {
        if (mIntentServiceTokenController == null) {
            mIntentServiceTokenController = new IntentServiceTokenController(
                    appCompatActivity,
                    button,
                    mCardInputWidget,
                    mErrorDialogHandler,
                    mProgresDialogController,
                    PUBLISHABLE_KEY);
        }
        return mIntentServiceTokenController;
    }


    /**
     * Clear all the references so that we can start over again.
     */
    public void clearReferences() {

        if (mAsyncTaskController != null) {
            mAsyncTaskController.detach();
        }


        if (mIntentServiceTokenController != null) {
            mIntentServiceTokenController.detach();
        }

        mAsyncTaskController = null;
        mIntentServiceTokenController = null;
    }
}
