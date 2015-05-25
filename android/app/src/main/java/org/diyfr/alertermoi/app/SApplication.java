package org.diyfr.alertermoi.app;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.diyfr.alertermoi.BuildConfig;
import org.diyfr.alertermoi.utils.LogHelper;
import org.diyfr.alertermoi.utils.PreferencesSettingsHelper;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gcm.GCMRegistrar;

public class SApplication extends android.app.Application {


	private static SApplication instance = null;

	public static SApplication getInstance() {
		return instance;
	}

	public static void setInstance(SApplication instance) {
		SApplication.instance = instance;
	}

	public SApplication() {
		instance = this;
	}

	public static SApplication getApplication() {
		return instance;
	}

	public static Context getContext() {
		if (instance == null)
			instance = new SApplication();
		return instance.getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		/*
		if (!BuildConfig.DEBUG)
			ACRA.init(this);
		*/
		
        /*****BOUCHON POUR EVITER DE SAISIR LES PARAMETRES GCM vIA LE FORMULAIRE*******/
/*
        if (PreferencesSettingsHelper.get_URL_WS(getApplicationContext()).isEmpty()){
            PreferencesSettingsHelper.set_URL_WS(getApplicationContext(),#VOTREURL#);
        }
        if (PreferencesSettingsHelper.get_SENDER_ID(getApplicationContext()).isEmpty()){
            PreferencesSettingsHelper.set_SENDER_ID(getApplicationContext(),#VOTRESENDERID#);
        }
        /*********************************/


        /********* UUID *********/
        if (PreferencesSettingsHelper.get_UUID(getApplicationContext()).length() == 0) {
            PreferencesSettingsHelper.set_UUID(getApplicationContext(), UUID.randomUUID().toString());
        }

		/**************** GCM ************************/
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);
		// Check if regid already presents
		if (regId.equals("")) {
			LogHelper.d("App", "Pas de RegisteredID");
            if (!PreferencesSettingsHelper.get_SENDER_ID(getApplicationContext()).isEmpty()) {
                GCMRegistrar.register(this, PreferencesSettingsHelper.get_SENDER_ID(getApplicationContext()));
            }
		} else {
            // Si changement de registered ID
			if (!PreferencesSettingsHelper.get_REGISTERED_ID(getApplicationContext()).equals(regId)) {
				PreferencesSettingsHelper.set_REGISTERED(getApplicationContext(), false);
				PreferencesSettingsHelper.set_REGISTERED_ID(getApplicationContext(), regId);
				if (!PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getApplicationContext()).isEmpty() && !PreferencesSettingsHelper.get_URL_WS(getApplicationContext()).isEmpty()) {
					RegisteredIdTask task = new RegisteredIdTask();
					task.execute(PreferencesSettingsHelper.get_URL_WS(getApplicationContext()), regId, PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getApplicationContext()),
							PreferencesSettingsHelper.get_UUID(getApplicationContext()));
				} else {
					LogHelper.d("App", "Pas d'adresse Email");
				}

			} else {
				if (!PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getApplicationContext()).isEmpty()
						&& !PreferencesSettingsHelper.is_REGISTERED(getApplicationContext())&& !PreferencesSettingsHelper.get_URL_WS(getApplicationContext()).isEmpty()) {
					RegisteredIdTask task = new RegisteredIdTask();
					task.execute(PreferencesSettingsHelper.get_URL_WS(getApplicationContext()), regId, PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getApplicationContext()),
							PreferencesSettingsHelper.get_UUID(getApplicationContext()));
				} else {
					LogHelper.d("App", "RegisteredId présent");
				}
			}

		}

		/**************** FIN GCM ************************/
	}

	public class RegisteredIdTask extends AsyncTask<String, Void, Integer> {
		public static final int TIMEOUT = 10000;
		public static final int SOCKET_TIMEOUT = 15000;

		private static final String TAG = "Register";

		@Override
		protected Integer doInBackground(String... params) {
			int result = -1;
			DefaultHttpClient httpClient = null;
			try {
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is
				// established.
				HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT);
				// Set the default socket timeout (SO_TIMEOUT)
				// in milliseconds which is the timeout for waiting for data.
				HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
				httpClient = new DefaultHttpClient(httpParameters);
				String url = params[0];
				String registeredId = params[1];
				String email = params[2];
				String uuid = params[3];
				HttpPost httppost = new HttpPost(url);

				JSONObject paramJson = new JSONObject();
				paramJson.put("registered_id", registeredId);
				paramJson.put("email", email);
				paramJson.put("uuid", uuid);
				StringEntity se = new StringEntity(paramJson.toString());
				httppost.setEntity(se);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");
				HttpResponse response = httpClient.execute(httppost);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String resp = EntityUtils.toString(entity);
					try {
						JSONObject jso = new JSONObject(resp);
						boolean registered = jso.getBoolean("registered");
						result = (registered) ? 1 : 0;
					} catch (JSONException e) {
						LogHelper.e(TAG, "RegisteredIdTask{JSONException}", e);
					}
				}
			} catch (SocketTimeoutException e) {
				LogHelper.e(TAG, "checkWs{SocketTimeoutException}", e);
				result = -2;
			} catch (ParseException e) {
				LogHelper.e(TAG, "checkWs{ParseException}", e);
				result = -1;
			} catch (IOException e) {
				result = -1;
				LogHelper.e(TAG, "checkWs{IOException}", e);
			} catch (IllegalStateException s) {
				result = -1;
				LogHelper.e(TAG, "checkWs{IllegalStateException}", s);
			} catch (Exception e) {
				result = -1;
				LogHelper.e(TAG, "checkWs{Exception}", e);
			}
			return result;
		}

		@Override
		protected void onPostExecute(final Integer result) {
			PreferencesSettingsHelper.set_REGISTERED(getApplicationContext(), result == 1);
		}

		@Override
		protected void onCancelled() {
		}
	}

	public void onTerminate() {
		super.onTerminate();
	}
}
