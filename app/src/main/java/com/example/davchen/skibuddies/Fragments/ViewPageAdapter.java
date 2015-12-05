package com.example.davchen.skibuddies.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by emy on 11/29/15.
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private CharSequence[] charSequence;
    private int no_of_tabs;

    public ViewPageAdapter(FragmentManager fragmentManager, CharSequence[] charSequence, int no_of_tabs) {
        super(fragmentManager);
        this.charSequence = charSequence;
        this.no_of_tabs = no_of_tabs;
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            ProfileTab profileTab = new ProfileTab();
            return profileTab;
        }
        else if(position==1){
           EventTab tab2 = new EventTab();
            return tab2;
        }
        else{
        InviteTab inviteTab = new InviteTab();
            return inviteTab;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return charSequence[position];
    }

    @Override
    public int getCount() {
        return no_of_tabs;
    }
}
