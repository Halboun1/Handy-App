package Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.handyapp_v2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import Activities.fragments.HomeFragment;
import Activities.fragments.MessagesFragment;
import Activities.fragments.ProfileFragment;
import Activities.fragments.SearchFragment;

public class BuyerDashboardActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private HomeFragment home;
    private TabLayout tabLayout;
    private List<Fragment> fragmentList;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);

        frameLayout = findViewById(R.id.framlayout);
        tabLayout = findViewById(R.id.tablayout);

        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MessagesFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new ProfileFragment());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.getTabAt(2).getIcon().setTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFragment(tab.getPosition());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tab.getIcon().setTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_d86041)));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tab.getIcon().setTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.getTabAt(0).getIcon().setTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_light_ffe0de)));
        }
        setFragment(0);

    }

    public void setFragment(int position) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(frameLayout.getId(), fragmentList.get(position));
        fragmentTransaction.commit();
    }
}