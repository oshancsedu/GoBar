package com.example.sifat.Dialogues;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import static com.example.sifat.Utilities.CommonUtilities.*;

import com.example.sifat.gobar.R;

/**
 * Created by Sifat on 10/28/2015.
 */
public class DriverRating extends DialogFragment implements View.OnClickListener,
        RatingBar.OnRatingBarChangeListener {

    Communicator communicator;
    private Button btPayment;
    private TextView tvDriverName, tvRating;
    private RatingBar rbDriverRate;
    private String driverName;
    private SharedPreferences sharedPreferences;
    private float rating;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        driverName = sharedPreferences.getString(SELECTED_DRIVER_NAME, "null");
        rating = sharedPreferences.getFloat(SELECTED_DRIVER_RATING, 0.0f);
        getDialog().setTitle("Rate Your Driver");
        View view = inflater.inflate(R.layout.rate_the_driver, null);
        intiViews(view);
        setCancelable(false);
        return view;
    }

    private void intiViews(View view) {
        tvDriverName = (TextView) view.findViewById(R.id.tvDriverName);
        tvDriverName.setText(driverName);
        tvRating = (TextView) view.findViewById(R.id.tvYourRate);
        rbDriverRate = (RatingBar) view.findViewById(R.id.rbDriverRate);
        rbDriverRate.setRating(rating + 0.5f);
        rbDriverRate.setRating(rating);
        rbDriverRate.setOnRatingBarChangeListener(this);
        btPayment = (Button) view.findViewById(R.id.btPayment);
        btPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btPayment) {
            communicator.RatingDialog();
            dismiss();
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rate, boolean b) {
        tvRating.setText("You Rated: " + rate);
    }


    public interface Communicator {
        public void RatingDialog();
    }
}
