package com.silentnotary.ui.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.silentnotary.R;
import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.ui.conversation.view.ConversationFragment;
import com.silentnotary.ui.files.view.FilesFragment;
import com.silentnotary.ui.files.view.UploadFilesFragment;
import com.silentnotary.ui.setting.SettingActivity;
import com.silentnotary.api.service.ApiBuilder;
import com.silentnotary.util.PrefUtil;

public class MainActivity extends MainActivityBase {
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PrefUtil.getSessionId().isEmpty()) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        ApiBuilder.setActivity(this);
        setContentView(R.layout.activity_main);
        this.initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initUI() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_files:
                    showFragment(FRAGMENT_PAGE.Files, null);
                    return true;
                case R.id.navigation_conversation:
                    showFragment(FRAGMENT_PAGE.Conversation, null);
                    return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_files);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager.getBackStackEntryCount() <= 1) {
            finish();
            return;
        }
        supportFragmentManager.popBackStack();
    }

    @Override
    public void showFragment(FRAGMENT_PAGE page, Bundle args) {
        Fragment fragment = null;

        switch (page) {
            case Conversation:
                fragment = new ConversationFragment();
                break;
            case Files:
                fragment = new FilesFragment();
                break;
            case UploadFile:
                fragment = new UploadFilesFragment();
                break;
            default:
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }
}
