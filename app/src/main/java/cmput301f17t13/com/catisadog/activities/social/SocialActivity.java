package cmput301f17t13.com.catisadog.activities.social;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.BaseDrawerActivity;
import cmput301f17t13.com.catisadog.fragments.social.FollowRequestsFragment;
import cmput301f17t13.com.catisadog.fragments.social.FollowersFragment;
import cmput301f17t13.com.catisadog.fragments.social.FollowingFragment;
import cmput301f17t13.com.catisadog.fragments.social.SearchUsersFragment;
import cmput301f17t13.com.catisadog.utils.ViewPagerAdapter;

public class SocialActivity extends BaseDrawerActivity {

    public ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        drawToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.socialPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FollowingFragment(), "Following");
        adapter.addFragment(new FollowersFragment(), "Followers");
        adapter.addFragment(new FollowRequestsFragment(), "Requests");
        adapter.addFragment(new SearchUsersFragment(), "Search");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.socialTabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
