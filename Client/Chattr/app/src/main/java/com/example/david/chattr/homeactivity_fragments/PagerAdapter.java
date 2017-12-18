package com.example.david.chattr.homeactivity_fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by david on 15.11.17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Returns the Right Fragment depending on which Tab you are in.
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChatListFragment Tab1 = new ChatListFragment();
                return Tab1;
            case 1:
                ContactListFragment Tab2 = new ContactListFragment();
                return Tab2;
            case 2:
                NewContactFragment Tab3 = new NewContactFragment();
                return Tab3;
            default:
                return null;
        }
    }

    //Number of Tabs/Fragments. You need to increment this number for each new fragment/tab.
    @Override
    public int getCount() {
        return 3;
    }

    //The Tab Title
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chats";
//                        Resources.getSystem().getString(R.string.chatlist_title_fragment); Nullpointerexeption for some reason, I left it hard coded.
            case 1:
                return "Contacts";
            case 2:
                return "New Contact";
            default:
                return null;
        }
    }
}
