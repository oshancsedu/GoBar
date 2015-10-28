package com.example.sifat.gobar;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.FacebookSdk;

/**
 * Created by Sifat on 10/29/2015.
 */
public class LoginFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
    }
}
