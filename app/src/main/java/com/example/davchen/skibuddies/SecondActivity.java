package com.example.davchen.skibuddies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android4devs.slidingtab.SlidingTabLayout;
import com.example.davchen.skibuddies.Fragments.ProfileTab;
import com.example.davchen.skibuddies.Fragments.ViewPageAdapter;

public class SecondActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private SlidingTabLayout slidingTabLayout;
    private String name = null;
    private String userId = null;
    private ProfileTab profileTab;
    private CharSequence[] charSequenceTitles=new CharSequence[]{"Profile","Events", "Invitation"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        toolbar = (Toolbar)findViewById(R.id.activitySecondToolBar);
        setSupportActionBar(toolbar);

        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), charSequenceTitles, charSequenceTitles.length);
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(viewPageAdapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        slidingTabLayout.setDistributeEvenly(true);

        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.com_facebook_blue);
            }
        });
        slidingTabLayout.setViewPager(viewPager);

        getUserInformation();
    }

    public void getUserInformation() {
        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        userId = intent.getStringExtra("Id");

        profileTab = new ProfileTab();
        Bundle bundle = new Bundle();
        bundle.putString("Name", name);
        bundle.putString("Id", userId);
        profileTab.setArguments(bundle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
       // getMenuInflater().inflate(R.menu.menu_second, menu);
        menuInflater.inflate(R.menu.menu_second, menu);
        SearchManager searchManager =  (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.searchBar1).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.createEventMenu) {
            Intent intent = new Intent(this, NewEventActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.searchBar1) {

        }

        return super.onOptionsItemSelected(item);
    }
}
