package com.silentnotary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import com.silentnotary.R;
import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.ui.conversation.view.ConversationFragment;
import com.silentnotary.ui.files.view.DetailFileFragment;
import com.silentnotary.ui.files.view.FilesFragment;
import com.silentnotary.ui.files.view.UploadFilesFragment;
import com.silentnotary.ui.main.view.BaseActivity;
import com.silentnotary.ui.main.view.IFragmentChangable;
import com.silentnotary.ui.main.view.MainToolbar;
import com.silentnotary.ui.pin.PinActivity;
import com.silentnotary.ui.setting.UserProfileFragment;
import com.silentnotary.ui.zip.ZipFragment;
import com.silentnotary.api.service.ApiBuilder;
import com.silentnotary.util.PrefUtil;

public class FirstActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        IFragmentChangable, IProgressBar {

    public static final int SETUP_PINCODE = 113;

    @Override
    public void showLoader() {
        this.mainToolbar.showLoader();
    }

    @Override
    public void hideLoader() {
        this.mainToolbar.hideLoader();
    }

    public interface Extra {
        public static final String SEARCH_FILTER = "search_filter";
    }

    private String filterExtra;
    DrawerLayout drawer = null;
    MainToolbar mainToolbar;

    TextView mEditTextUserEmail = null;
    Toolbar toolbar;
    NavigationView navigationView = null;
    MaterialSearchView searchView = null;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ApiBuilder.setActivity(this);
        initToolbar();
        mainToolbar
                .setOnNavigationButterClick(v -> drawer.openDrawer(Gravity.START))
                .setOnBackClicked(v -> onBackPressed())
                .setOnUploadFilesButtonClick(v -> showFragment(FRAGMENT_PAGE.UploadFile, null))
                .setOnAddFileButtonClick(v -> {
                    if (getCurrentFragment() != null
                            && getCurrentFragment() instanceof UploadFilesFragment) {
                        ((UploadFilesFragment) getCurrentFragment())
                                .addFile();
                    }
                });


        filterExtra = getIntent().getStringExtra(Extra.SEARCH_FILTER);

        showFragment(FRAGMENT_PAGE.UploadFile, null);
        if (LockManager.getInstance().getAppLock() != null
                && !LockManager.getInstance().getAppLock().isPasscodeSet()) {
            Intent intent = new Intent(FirstActivity.this, PinActivity.class);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            startActivityForResult(intent, SETUP_PINCODE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (PrefUtil.getSessionId().isEmpty()) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }
        this.mEditTextUserEmail.setText(PrefUtil.getUserEmail());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        toolbar.setNavigationIcon(null);
        drawer = findViewById(R.id.drawer_layout);
        // Navigation view
        navigationView = findViewById(R.id.nav_view);
        mEditTextUserEmail = navigationView.getHeaderView(0)
                .findViewById(R.id.user_email);
        navigationView.setCheckedItem(R.id.nav_upload);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.findViewById(R.id.searchIcon)
                .setOnClickListener(view -> searchView.showSearch());
        //Search view
        searchView = findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                filterExtra = null;
            }
        });
        mainToolbar = new MainToolbar(toolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Fragment currentFragment = getCurrentFragment();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragment instanceof DetailFileFragment
                ) {
            showFragment(FRAGMENT_PAGE.Files, null);
        } else if (getCurrentFragmentNotSupport() != null
                && getCurrentFragmentNotSupport() instanceof UserProfileFragment) {
            showFragment(FRAGMENT_PAGE.EmptyFragment, null);
            showFragment(FRAGMENT_PAGE.Files, null);
        } else if (currentFragment instanceof ZipFragment
                && DetailFileFragment.latestDetailFile != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(DetailFileFragment.Extra.FILE, DetailFileFragment.latestDetailFile);
            showFragment(IFragmentChangable.FRAGMENT_PAGE.FileDetail, arguments);
        } else {
            super.onBackPressed();
        }
    }

    Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }

    android.app.Fragment getCurrentFragmentNotSupport() {
        return getFragmentManager().findFragmentById(R.id.content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Fragment fragment = getCurrentFragment();
        initSearchView(fragment);
        return super.onCreateOptionsMenu(menu);
    }

    public String tryToApplyQueryFilter() {
        if (filterExtra != null && !filterExtra.isEmpty()) {
            searchView.setQuery(filterExtra, false);
            searchView.clearFocus();
            return filterExtra;
        }
        return "";
    }

    void initSearchView(Fragment fragment) {
        if (searchView == null) {
            return;
        }

        if (fragment != null) {
            boolean searchable = fragment instanceof ISearchableFragment;
            searchView.setVisibility(searchable ? View.VISIBLE : View.GONE);
            findViewById(R.id.searchIcon)
                    .setVisibility(searchable ? View.VISIBLE : View.GONE);
            if (searchable) {
                searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        ((ISearchableFragment) fragment)
                                .onQuery(newText);
                        return false;
                    }
                });
            } else {
                searchView.setOnQueryTextListener(null);
            }
        }
    }


    @Override
    public void showFragment(IFragmentChangable.FRAGMENT_PAGE page, Bundle argument) {
        Fragment supportFragment = null;
        android.app.Fragment fragment = null;
        switch (page) {
            case Conversation:
                navigationView.setCheckedItem(R.id.nav_conversations);
                supportFragment = new ConversationFragment();
                break;
            case Files:
                navigationView.setCheckedItem(R.id.nav_files);
                supportFragment = new FilesFragment();
                break;
            case UploadFile:
                supportFragment = new UploadFilesFragment();
                break;
            case FileDetail:
                supportFragment = new DetailFileFragment();
                break;
            case UserProfile:
                navigationView.setCheckedItem(R.id.nav_user_profile);
                fragment = new UserProfileFragment();
                showFragment(FRAGMENT_PAGE.EmptyCompatFragment, null);
                break;
            case EmptyFragment:
                fragment = new EmptyFakeFragment.EmptyFragment();
                break;
            case ZipViewer:
                supportFragment = new ZipFragment();
                break;
            case EmptyCompatFragment:
                supportFragment = new EmptyFakeFragment.EmptyCompatFragment();
            default:
                break;
        }


        if (searchView.isSearchOpen())
            searchView.closeSearch();
        mainToolbar.proceedFragment(page);
        mainToolbar.hideLoader();
        if (supportFragment != null)
            supportFragment.setArguments(argument);
        initSearchView(supportFragment);
        if (supportFragment != null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, supportFragment)
                    .commit();
        else if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_files) {
            showFragment(IFragmentChangable.FRAGMENT_PAGE.Files, null);
        } else if (id == R.id.nav_conversations) {
            showFragment(IFragmentChangable.FRAGMENT_PAGE.Conversation, null);
        } else if (id == R.id.nav_user_profile) {
            showFragment(FRAGMENT_PAGE.UserProfile, null);
        } else if (id == R.id.nav_logout) {
            PrefUtil.setSessionId("");
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        } else if (id == R.id.nav_upload) {
            showFragment(FRAGMENT_PAGE.UploadFile, null);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
