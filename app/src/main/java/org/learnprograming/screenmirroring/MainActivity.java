package org.learnprograming.screenmirroring;


import android.os.Bundle;

import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import me.riddhimanadib.library.BottomBarHolderActivity;
import me.riddhimanadib.library.NavigationPage;


public class MainActivity extends BottomBarHolderActivity implements FirstFragment.OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationPage page1 = new NavigationPage("Home", ContextCompat.getDrawable(this, R.drawable.home), FirstFragment.newInstance());
        NavigationPage page2 = new NavigationPage("Server", ContextCompat.getDrawable(this, R.drawable.server), SecondFragment.newInstance());
        NavigationPage page3 = new NavigationPage("Client", ContextCompat.getDrawable(this, R.drawable.client), ThirdFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Settings", ContextCompat.getDrawable(this, R.drawable.settings), FourthFragment.newInstance());

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);

        super.setupBottomBarHolderActivity(navigationPages);
    }
}
