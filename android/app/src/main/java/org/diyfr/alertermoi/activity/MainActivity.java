package org.diyfr.alertermoi.activity;

import org.diyfr.alertermoi.activity.fragment.FragmentAbout;
import org.diyfr.alertermoi.activity.fragment.FragmentAccount;
import org.diyfr.alertermoi.activity.fragment.FragmentMain;
import org.diyfr.alertermoi.activity.fragment.FragmentSettings;
import org.diyfr.alertermoi.services.GCMIntentService;
import org.diyfr.alertermoi.utils.ActionBarUtils;
import org.diyfr.alertermoi.utils.PreferencesSettingsHelper;
import org.diyfr.alertermoi.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    private FragmentSettings fSettings = new FragmentSettings();
	private FragmentAccount fAccount = new FragmentAccount();
	private FragmentMain fMain = new FragmentMain();
	private FragmentAbout fAbout = new FragmentAbout();

	enum FragmentList {
			MAIN,
            ACCOUNT,
			SETTINGS,
			ABOUT
	};

	private FragmentList currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBarUtils.AffichageNormal(this);
		currentFragment = FragmentList.MAIN;

        if (PreferencesSettingsHelper.get_URL_WS(getApplicationContext()).isEmpty()||PreferencesSettingsHelper.get_SENDER_ID(getApplicationContext()).isEmpty()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setSubtitle(R.string.action_serveur);
            getFragmentManager().beginTransaction().add(R.id.container, fSettings).commit();
        }
        else{
            if (PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getApplicationContext()).isEmpty()) {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setSubtitle(R.string.action_account);
                getFragmentManager().beginTransaction().add(R.id.container, fAccount).commit();
            } else {
                returnMainFragment();
            }
            }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.action_account:
			if (!currentFragment.equals(FragmentList.ACCOUNT)) {
				currentFragment = FragmentList.ACCOUNT;
				getActionBar().setHomeButtonEnabled(true);
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setSubtitle(R.string.action_account);
				getFragmentManager().beginTransaction().replace(R.id.container, fAccount).commit();
			}
			break;
            case R.id.action_settings:
                if (!currentFragment.equals(FragmentList.SETTINGS)) {
                    currentFragment = FragmentList.SETTINGS;
                    getActionBar().setSubtitle(R.string.action_serveur);
                    getActionBar().setHomeButtonEnabled(true);
                    getActionBar().setDisplayHomeAsUpEnabled(true);
                    getFragmentManager().beginTransaction().replace(R.id.container, fSettings).commit();
                }
                break;
		case R.id.action_about:
			if (!currentFragment.equals(FragmentList.ABOUT)) {
				currentFragment = FragmentList.ABOUT;
				getActionBar().setSubtitle(R.string.action_about);
				getActionBar().setHomeButtonEnabled(true);
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getFragmentManager().beginTransaction().replace(R.id.container, fAbout).commit();
			}
			break;
		default:
			returnMainFragment();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(GCMIntentService.NOTIFICATION_ID);
	}

	@Override
	public void onBackPressed() {
		if (currentFragment != FragmentList.MAIN) {
			returnMainFragment();
		} else {
			super.onBackPressed();
		}
	}

	public void returnMainFragment() {
		currentFragment = FragmentList.MAIN;
        if (PreferencesSettingsHelper.get_SENDER_ID(getApplicationContext()).isEmpty()){
            getActionBar().setSubtitle(R.string.mismatchsenderid);
        }else{
            if (PreferencesSettingsHelper.get_URL_WS(getApplicationContext()).isEmpty()){
                getActionBar().setSubtitle(R.string.mismatchurlws);
            }else{
                if (PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getApplicationContext()).isEmpty()) {
                    getActionBar().setSubtitle(R.string.mismatchaccount);
                } else {
                    getActionBar().setSubtitle(PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getApplicationContext()));
                }
            }
        }
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getFragmentManager().beginTransaction().replace(R.id.container, fMain).commit();
	}

    public void returnAccountFragment() {
        currentFragment = FragmentList.ACCOUNT;
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setSubtitle(R.string.action_account);
        getFragmentManager().beginTransaction().replace(R.id.container, fAccount).commit();
    }
}
