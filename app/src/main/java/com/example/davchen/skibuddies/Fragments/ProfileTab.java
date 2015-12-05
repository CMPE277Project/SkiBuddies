package com.example.davchen.skibuddies.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davchen.skibuddies.R;
import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by emy on 12/4/15.
 */
public class ProfileTab extends Fragment {

    private ProfilePictureView profilePictureView;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.profile_tab_fragment, container, false);
        profilePictureView = (ProfilePictureView)rootView.findViewById(R.id.facebookProfileId);
        textView = (TextView)rootView.findViewById(R.id.profilePicName);

        displayName();
        return rootView;
    }

    private void displayName() {

        String name = getActivity().getIntent().getStringExtra("Name");
        String userId = getActivity().getIntent().getStringExtra("Id");

        profilePictureView.setProfileId(userId);
        textView.setText(name);
    }


}
