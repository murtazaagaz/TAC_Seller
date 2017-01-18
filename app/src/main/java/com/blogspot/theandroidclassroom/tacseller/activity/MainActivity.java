package com.blogspot.theandroidclassroom.tacseller.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.blogspot.theandroidclassroom.tacseller.LoginFragment;
import com.blogspot.theandroidclassroom.tacseller.R;
import com.blogspot.theandroidclassroom.tacseller.RegisterFragment;
import com.blogspot.theandroidclassroom.tacseller.adapter.MyFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.main_tabs) TabLayout mTabs;
    @BindView(R.id.main_viewpager) ViewPager mViewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("TAC seller");

        setupViewPager(mViewpager);
        mTabs.setupWithViewPager(mViewpager);


    }
    private void setupViewPager(ViewPager viewPager){
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new RegisterFragment(),"Register");
        adapter.addFragment(new LoginFragment(),"Login");
        viewPager.setAdapter(adapter);
    }
}
