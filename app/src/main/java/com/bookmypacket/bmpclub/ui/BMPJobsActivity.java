package com.bookmypacket.bmpclub.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.apptentive.android.sdk.Apptentive;
import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.BMPPacket;
import com.bookmypacket.bmpclub.dto.LoginVerifyResponse;
import com.bookmypacket.bmpclub.ui.frags.BMPPacketCompletedFragment;
import com.bookmypacket.bmpclub.ui.frags.BMPPacketFragment;
import com.bookmypacket.bmpclub.ui.frags.BMPPacketProgressFragment;
import com.bookmypacket.bmpclub.ui.frags.OnListFragmentInteractionListener;
import com.bookmypacket.bmpclub.ui.frags.OnPacketListResult;
import com.bookmypacket.bmpclub.utils.AppConstants;

import java.util.List;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.getMobileNo;
import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.getProfile;

public class BMPJobsActivity extends BaseActivity
        implements OnListFragmentInteractionListener<BMPPacket>,
                   NavigationView.OnNavigationItemSelectedListener, OnPacketListResult
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager    mViewPager;
    private DrawerLayout drawer;
    private String    commssionAvailable;
    private String    comissionProgress;
    private String    comissionEarned;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmpjobs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        comissionEarned = getString(R.string.commission_earned) + "-";
        comissionProgress = getString(R.string.commission_progress) + "-";
        commssionAvailable = getString(R.string.commission_available) + "-";
        registerFab();
        initNavDrawer();
        syncData();
        syncProfile();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                updateTitle();
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                updateTitle();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
                updateTitle();
            }
        });

    }

    private void initNavDrawer()
    {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // navigationView.
        final LoginVerifyResponse userProfile = getProfile(BMPJobsActivity.this);
        if (userProfile != null && userProfile.getFirstName() != null)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    View     headerView = navigationView.getHeaderView(0);
                    TextView email      = (TextView) headerView.findViewById(R.id.textView_email);
                    TextView name       = (TextView) headerView.findViewById(R.id.tv_name);
                    email.setText(userProfile.getEmailId());
                    name.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
                    ImageView profileImg = (ImageView) headerView.findViewById(R.id.imageView);
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .withBorder(2)
                            .width(60)
                            .height(60)
                            .bold()
                            .endConfig()
                            .buildRound(userProfile.getFirstName().substring(0, 1),
                                        getSysColor(R.color.accent));
                    profileImg.setImageDrawable(drawable);
                }
            });

        }
    }

    private int getSysColor(int resid)
    {
        int color = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            color = getColor(R.color.accent);
        } else
        {
            color = getResources().getColor(resid);
        }
        return color;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bmp, menu);
        return true;
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onListFragmentInteraction(BMPPacket item)
    {
        Intent i = new Intent(this, BMPPacketDetails.class);
        i.putExtra(AppConstants.BundleExtraKeys.PACKET, item);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_deposit:
                Intent i = new Intent(BMPJobsActivity.this, WalletTopupActivity.class);
                startActivity(i);
                break;
            case R.id.nav_wallet:
                Intent i2 = new Intent(BMPJobsActivity.this, BMPWalletActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_withdraw:
                Intent i3 = new Intent(BMPJobsActivity.this, WithdrawMoneyActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_email:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.bmp_cc_email)});
                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.bm_email_subject)
                        + getMobileNo(this));
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;
            case R.id.nav_statement:
                Intent statement = new Intent(Intent.ACTION_SEND);
                statement.putExtra(Intent.EXTRA_EMAIL,
                                   new String[]{getString(R.string.bmp_cc_email)});
                statement.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.bm_email_statement)
                        + getMobileNo(this));
                statement.setType("message/rfc822");
                startActivity(Intent.createChooser(statement, "Choose an Email client :"));
                break;
            case R.id.nav_contact:
                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string
                                                                                       .bmp_cc_number)));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    makeText(this, R.string.phone_permission, LENGTH_LONG).show();
                    return false;
                }
                try
                {
                    startActivity(in);
                }

                catch (android.content.ActivityNotFoundException ex)
                {
                    makeText(getApplicationContext(), R.string.phone_error, LENGTH_LONG).show();
                }
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            boolean ranApptentive = Apptentive.handleOpenedPushNotification(this);
        }
    }

    @Override
    public void packetsRecieved(final List<BMPPacket> packets, final int i)
    {
        Handler h = new Handler();
        h.post(new Runnable()
        {
            @Override
            public void run()
            {
                calculateBalance(packets, i);
            }
        });

    }

    private void calculateBalance(List<BMPPacket> packets, int i)
    {
        double totalAvailable = 0;
        for (BMPPacket packet : packets)
        {
            if (!isEmpty(packet.getEarnings()))
            {

                totalAvailable += Double.parseDouble(packet.getEarnings());
            }

        }
        switch (i)
        {
            case 0:
                commssionAvailable = getString(R.string.commission_available) + totalAvailable;
                break;
            case 1:
                comissionProgress = getString(R.string.commission_progress) + totalAvailable;
                break;
            case 2:
                comissionEarned = getString(R.string.commission_earned) + totalAvailable;
                break;
        }
        updateTitle();
    }


    private void updateTitle()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                String title = "";
                switch (tabLayout.getSelectedTabPosition())
                {
                    case 0:
                        title = commssionAvailable;
                        break;
                    case 1:
                        title = comissionProgress;
                        break;
                    case 2:
                        title = comissionEarned;
                        break;
                }
                getSupportActionBar().setTitle(title);
            }
        });
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment frag = null;
            switch (position)
            {
                case 0:
                    frag = new BMPPacketFragment();
                    break;
                case 1:
                    frag = new BMPPacketProgressFragment();
                    break;
                case 2:
                    frag = new BMPPacketCompletedFragment();
                    break;
            }
            return frag;
        }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return getString(R.string.title_packet_avilable);
                case 1:
                    return getString(R.string.title_packet_in_progess);
                case 2:
                    return getString(R.string.title_completed);
            }
            return null;
        }
    }
}
