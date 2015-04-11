package com.github.lzyzsd.assetsmanagement.my;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.github.lzyzsd.assetsmanagement.CaptureActivity;
import com.github.lzyzsd.assetsmanagement.R;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

public class MainActivity extends ActionBarActivity {
    FrameLayout container;
    View[] tabs = new View[3];
    Fragment[] tabFragments = new Fragment[3];
    private int selectedTab = 0;

    private static final String STATE_SELECTED_TAB = "selected_tab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout) findViewById(R.id.content_container);
        if (null == savedInstanceState) {
            SearchTabFragment searchTabFragment = new SearchTabFragment();
            ObsoleteTabFragment obsoleteTabFragment = new ObsoleteTabFragment();
            MeTabFragment meTabFragment = new MeTabFragment();

            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_container, searchTabFragment, "search")
                .hide(searchTabFragment)
                .add(R.id.content_container, obsoleteTabFragment, "obsolete")
                .hide(obsoleteTabFragment)
                .add(R.id.content_container, meTabFragment, "me")
                .hide(meTabFragment)
                .commit();
            getSupportFragmentManager().executePendingTransactions();
        } else {
            selectedTab = savedInstanceState.getInt(STATE_SELECTED_TAB);
        }

        tabs[0] = findViewById(R.id.tab_search);
        tabs[1] = findViewById(R.id.tab_obsolete);
        tabs[2] = findViewById(R.id.tab_me);

        tabFragments[0] = getSupportFragmentManager().findFragmentByTag("search");
        tabFragments[1] = getSupportFragmentManager().findFragmentByTag("obsolete");
        tabFragments[2] = getSupportFragmentManager().findFragmentByTag("me");

        selectTab(selectedTab);
        addTabSelectListener();

        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_TAB, selectedTab);
    }

    private void selectTab(int index) {
        tabs[selectedTab].setSelected(false);
        tabs[index].setSelected(true);
        FragmentTransaction transaction = getSupportFragmentManager()
            .beginTransaction();

        for (int i = 0; i < tabFragments.length; i++) {
            if (i == index) {
                transaction.show(tabFragments[i]);
            } else {
                transaction.hide(tabFragments[i]);
            }
        }
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();

        selectedTab = index;
    }

    private void addTabSelectListener() {
        tabs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(0);
            }
        });

        tabs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(1);
            }
        });

        tabs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_scan) {
            startActivityForResult(new Intent(this, CaptureActivity.class), Constants.REQUEST_CODE_CAPTURE_ACTIVITY);
            return true;
        } else if (item.getItemId() == R.id.menu_feedback) {
            FeedbackAgent agent = new FeedbackAgent(this);
            agent.startFeedbackActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
