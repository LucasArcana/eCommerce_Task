package com.example.ecommerce_task;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_task.API.TokenManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NavigationActivity extends AppCompatActivity {

    private LinearLayout[] navItems;
    private LinearLayout header;
    private TextView tvHeader;
    private ImageView[] navIcons;
    private TextView[] navTexts;
    private Fragment[] fragments;
    private Fragment active;
    private FloatingActionButton fab;
    private final int COLOR_ACTIVE   = 0xFF004252;
    private final int COLOR_INACTIVE = 0x50536166;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        setupFragments();
        setupNav();
        setupFab();
        setupMenu();
    }

    private void init(){
        navItems = new LinearLayout[]{
                findViewById(R.id.nav_home),
                findViewById(R.id.nav_search),
                findViewById(R.id.nav_cart),
                findViewById(R.id.nav_profile)
        };
        navIcons = new ImageView[]{
                findViewById(R.id.icon_home),
                findViewById(R.id.icon_search),
                findViewById(R.id.icon_cart),
                findViewById(R.id.icon_profile)
        };
        navTexts = new TextView[]{
                findViewById(R.id.text_home),
                findViewById(R.id.text_search),
                findViewById(R.id.text_cart),
                findViewById(R.id.text_profile)
        };

        fragments = new Fragment[]{
                new HomeFragment(),
                new SearchFragment(),
                new CartFragment(),
                new ProfileFragment()
        };
        fab = findViewById(R.id.fab_cart);
        header = findViewById(R.id.header);
        tvHeader = findViewById(R.id.tvHeader);
    }
    private void setupFragments() {
        var ft = getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < fragments.length; i++) {
            ft.add(R.id.fragmentContainer, fragments[i]);
            if (i != 0) ft.hide(fragments[i]);
        }

        ft.commit();
        active = fragments[0];
        setNavActive(0);
    }

    private void setupMenu(){
        ImageButton btnMenu = findViewById(R.id.btn_menu);

        btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.app_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_home) {
                    switchTab(0);
                } else if (id == R.id.menu_profile) {
                    switchTab(3);
                } else if (id == R.id.menu_order_history) {
                    startActivity(new Intent(this, PurchaseActivity.class));
                } else if (id == R.id.menu_settings) {
                    startActivity(new Intent(this, Settings.class));
                } else if (id == R.id.menu_search) {
                    switchTab(1);
                } else if (id == R.id.menu_cart) {
                    switchTab(2);
                } else if (id == R.id.menu_logout) {
                    new TokenManager(this).clearToken();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                return true;
            });

            popup.show();
        });
    }
    private void setupFab() {
        fab.setOnClickListener(v -> switchTab(2));
    }

    private void setupNav() {
        for (int i = 0; i < navItems.length; i++) {
            int index = i;
            navItems[i].setOnClickListener(v -> switchTab(index));
        }
    }

    private void switchTab(int index) {
        if (fragments[index] == active) return;

        getSupportFragmentManager().beginTransaction()
                .hide(active)
                .show(fragments[index])
                .commit();

        active = fragments[index];

        if (index == 0){
            header.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            tvHeader.setText("NotMazon");
        }else if (index == 2){
            header.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            tvHeader.setText("My Cart");
        }else if (index == 3){
            header.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            tvHeader.setText("My Profile");
        }else {
            header.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }

        setNavActive(index);
    }
    private void setNavActive(int index) {
        for (int i = 0; i < navItems.length; i++) {
            boolean isActive = (i == index);
            int color = isActive ? COLOR_ACTIVE : COLOR_INACTIVE;

            navIcons[i].setImageTintList(ColorStateList.valueOf(color));
            navTexts[i].setTextColor(color);
            navTexts[i].setTypeface(null, isActive ? Typeface.BOLD : Typeface.NORMAL);

        }
    }
    public void switchToSearch() {
        switchTab(1);
    }
}
