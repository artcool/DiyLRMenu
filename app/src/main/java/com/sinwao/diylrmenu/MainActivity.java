package com.sinwao.diylrmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sinwao.diylrmenu.fragment.LeftMenuFragment;
import com.sinwao.diylrmenu.ui.MenuUI;

public class MainActivity extends AppCompatActivity {

    private MenuUI menuUI;
    private LeftMenuFragment leftMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuUI = new MenuUI(MainActivity.this);
        setContentView(menuUI);
        leftMenuFragment = new LeftMenuFragment();
        getSupportFragmentManager().beginTransaction().add(MenuUI.LEFT_ID,leftMenuFragment).commit();
    }
}
