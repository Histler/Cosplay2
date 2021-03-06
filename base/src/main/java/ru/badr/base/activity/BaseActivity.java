package ru.badr.base.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import ru.badr.base.BaseBeanContainer;
import ru.badr.base.R;
import ru.badr.base.service.NavigationService;
import ru.badr.base.util.Navigate;

/**
 * Created by ABadretdinov
 * 26.06.2015
 * 17:53
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final BaseBeanContainer mBeanContainer = BaseBeanContainer.getInstance();
    protected final NavigationService mNavigationService = mBeanContainer.getNavigationService();
    protected DrawerLayout mDrawerLayout;
    protected boolean mHasMenu = true;

    protected NavigationView mNavigationView;
    private int mMenuId;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mHasMenu && mDrawerLayout != null) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Navigate.PARAM_THEME)) {
            setTheme(bundle.getInt(Navigate.PARAM_THEME));
        }
        super.onCreate(savedInstanceState);
        if (bundle != null && bundle.containsKey(Navigate.PARAM_FOR_SELECT) && bundle.getBoolean(Navigate.PARAM_FOR_SELECT)) {
            setContentView(R.layout.simple_activity);

            if (mNavigationService == null) {
                PackageManager pm = getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } else {
            setContentView(R.layout.with_menus_activity);
            initMenuDrawer();
        }
    }

    private void initMenuDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if (mNavigationService == null) {
            PackageManager pm = getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (mNavigationView != null) {
            reInflateMenu();
        }
    }

    protected void reInflateMenu() {
        int newMenuId = mNavigationService.getNavigationMenuResId();
        if (mMenuId != newMenuId) {
            mMenuId = newMenuId;
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(mMenuId);
        }
    }

    public void initFragment(Intent intent) {
        String fragmentName;
        try {
            if (intent.hasExtra(Navigate.PARAM_CLASS)) {
                fragmentName = intent.getStringExtra(Navigate.PARAM_CLASS);
            } else {
                fragmentName = mNavigationService.getMainFragment(mNavigationService.getDefaultFragmentResId()).getName();
            }
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            Class<Fragment> newClass = (Class<Fragment>) Class.forName(fragmentName);
            if (current == null || !current.getClass().equals(newClass)) {
                Fragment fragment = newClass.newInstance();
                fragment.setArguments(intent.getBundleExtra(Navigate.PARAM_ARGS));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment, "main").commitAllowingStateLoss();
            }
        } catch (Exception e) {
            Log.w(getClass().getName(), e.getLocalizedMessage());
            finish();
        }
    }

    public void initFragment() {
        initFragment(getIntent());
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }
}
