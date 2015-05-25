package org.diyfr.alertermoi.activity.fragment;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

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
import org.diyfr.alertermoi.activity.MainActivity;
import org.diyfr.alertermoi.constantes.ConstantesGCM;
import org.diyfr.alertermoi.utils.LogHelper;
import org.diyfr.alertermoi.utils.NetworkHelper;
import org.diyfr.alertermoi.utils.PreferencesSettingsHelper;
import org.diyfr.alertermoi.R;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentAccount extends Fragment implements OnItemClickListener {

	private ListView lv;
	private AccountAdapter adapter;
	private RegisteredIdTask task;
	private static String TAG = "Account";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_selectaccount, container, false);
		lv = (ListView) rootView.findViewById(R.id.lv_emails);
		lv.setOnItemClickListener(this);
		return rootView;
	}

	private void loadAccounts() {
		if (adapter == null) {
			final List<AccountItem> listAccounts = new ArrayList<AccountItem>();
			AccountManager accountManager = AccountManager.get(getActivity().getApplicationContext());
			Account[] accounts = accountManager.getAccountsByType("com.google");
			for (Account account : accounts) {
				AccountItem accountItem = new AccountItem();
				accountItem.name = account.name;
				listAccounts.add(accountItem);
			}
			adapter = new AccountAdapter(getActivity().getApplicationContext(), R.layout.item_account, listAccounts);
		}
		lv.setAdapter(adapter);
	}

	private void registeringAccount() {
		PreferencesSettingsHelper.set_REGISTERED(getActivity().getApplicationContext(), false);
		if (NetworkHelper.isConnectedOrConnecting(getActivity().getApplicationContext()) && ! PreferencesSettingsHelper.get_URL_WS(getActivity().getApplicationContext()).isEmpty()) {
			// Si nouveau serveur et dejà enregistré auprès de GCM
			task = new RegisteredIdTask();
			task.execute(PreferencesSettingsHelper.get_URL_WS(getActivity().getApplicationContext()), PreferencesSettingsHelper.get_REGISTERED_ID(getActivity().getApplicationContext()),
					PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getActivity().getApplicationContext()),
					PreferencesSettingsHelper.get_UUID(getActivity().getApplicationContext()));
		} else {
			Toast.makeText(getActivity().getApplicationContext(), "Absence de réseau, enregistrement impossible", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onResume() {
		if (PreferencesSettingsHelper.get_REGISTERED_ID(getActivity().getApplicationContext()).isEmpty() && !PreferencesSettingsHelper.get_SENDER_ID(getActivity().getApplicationContext()).isEmpty()) {
			LogHelper.i(TAG, "Launch GCM registrar");
			getActivity().registerReceiver(mHandleMessageReceiver, new IntentFilter(ConstantesGCM.ACTION_GCM_REGISTERED_RECEIPT));
			Toast.makeText(getActivity().getApplicationContext(), "Enregistrement auprès de GCM", Toast.LENGTH_SHORT).show();
			GCMRegistrar.register(getActivity(), PreferencesSettingsHelper.get_SENDER_ID(getActivity().getApplicationContext()));
		} else {
			LogHelper.i(TAG, "Load Account");
			loadAccounts();
		}

		if (PreferencesSettingsHelper.is_REGISTERED(getActivity().getApplicationContext())) {
			boolean found = false;
			LogHelper.i(TAG, "Search Registered Account");
			for (int i = 0; i < adapter.getCount(); i++) {
				if (adapter.getItem(i).name.equals(PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getActivity().getApplicationContext()))) {
					found = true;
					LogHelper.i(TAG, "Find Registered Account:" + adapter.getItem(i).name);
					adapter.isRegistered(i);
					break;
				}
			}
			if (!found) {
				LogHelper.i(TAG, "Not Found Registered Account");
				PreferencesSettingsHelper.set_USER_GOOGLE_EMAIL(getActivity().getApplicationContext(), "");
				PreferencesSettingsHelper.set_REGISTERED(getActivity().getApplicationContext(), false);
				if (adapter != null) {
					adapter.reset();
				}
			}
		} else {
			LogHelper.i(TAG, "Not Registered");
		}

		super.onResume();
	}

	@Override
	public void onPause() {
		try {
			getActivity().unregisterReceiver(mHandleMessageReceiver);
		} catch (Exception e) {
			LogHelper.d(TAG, e.getMessage());
		}
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		AccountItem item = null;
		LogHelper.i(TAG, "Account selected :" + item);
		if (adapter != null && adapter.getCount() > position) {
			item = adapter.getItem(position);
		}
		if (item != null) {
			if (!item.name.equals(PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getActivity().getApplicationContext()))) {
				PreferencesSettingsHelper.set_USER_GOOGLE_EMAIL(getActivity().getApplicationContext(), item.name);
				adapter.registering(position);
				LogHelper.i(TAG, "Account not registered :" + item);
				registeringAccount();
			} else {
				if (PreferencesSettingsHelper.is_REGISTERED(getActivity().getApplicationContext())) {
					LogHelper.i(TAG, "Account Already registered :" + item);
					adapter.isRegistered(position);
				} else {
					adapter.registering(position);
					LogHelper.i(TAG, "Account not registered :" + item);
					registeringAccount();
				}
			}
		} else {
			LogHelper.i(TAG, "Account selected null");
		}

	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!PreferencesSettingsHelper.get_REGISTERED_ID(getActivity().getApplicationContext()).isEmpty()) {
				LogHelper.i(TAG, "Get registered ID");
				loadAccounts();
			} else {
				Toast.makeText(getActivity().getApplicationContext(), "L'enregistrement auprès de GCM a échoué.", Toast.LENGTH_LONG).show();
			}
		}
	};

	public class RegisteredIdTask extends AsyncTask<String, Void, Integer> {
		public static final int TIMEOUT = 10000;
		public static final int SOCKET_TIMEOUT = 15000;
		private String currentAccount;

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
				currentAccount = params[2];
				String uuid = params[3];
				HttpPost httppost = new HttpPost(url);

				JSONObject paramJson = new JSONObject();
				paramJson.put("registered_id", registeredId);
				paramJson.put("email", currentAccount);
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
			resultRegisterdIdTask(result);
		}

		@Override
		protected void onCancelled() {
			if (FragmentAccount.this.adapter != null) {
				FragmentAccount.this.adapter.reset(currentAccount);
			}
		}
	}

	public void resultRegisterdIdTask(Integer result) {
		switch (result) {
		case -2:
			Toast.makeText(getActivity().getApplicationContext(), "Le serveur ne répond pas. Essayez plus tard", Toast.LENGTH_LONG).show();
			this.adapter.reset(PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getActivity().getApplicationContext()));
			break;
		case -1:
			Toast.makeText(getActivity().getApplicationContext(), "Une erreur est survenue lors de l'enregistrement de l'utilisateur", Toast.LENGTH_LONG)
					.show();
			this.adapter.reset(PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getActivity().getApplicationContext()));
			break;
		case 0:
			Toast.makeText(getActivity().getApplicationContext(), "Le serveur n'a pas validé l'enregistrement de l'Utilisateur", Toast.LENGTH_LONG).show();
			this.adapter.reset(PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getActivity().getApplicationContext()));
			break;
		case 1:
			Toast.makeText(getActivity().getApplicationContext(), "Utilisateur enregistré", Toast.LENGTH_LONG).show();
			PreferencesSettingsHelper.set_REGISTERED(getActivity().getApplicationContext(), true);
			((MainActivity) getActivity()).returnMainFragment();
			break;
		default:
			break;
		}

	}

	class AccountItem {
		public String name;
		public boolean showProgress = false;
		public boolean isChecked = false;

		@Override
		public boolean equals(Object o) {
			boolean result = false;
			if (o instanceof AccountItem) {
				AccountItem ob = (AccountItem) o;
				result = this.name.equals(ob.name);
			}
			return result;
		}

	}

	class AccountAdapter extends ArrayAdapter<AccountItem> {
		private List<AccountItem> listAccounts;
		private int layout;
		private int checkedIndex = -1;

		class ViewHolder {
			ProgressBar progress;
			TextView tvname;
			RadioButton selected;
		}

		public AccountAdapter(Context context, int textViewResourceId, List<AccountItem> objects) {
			super(context, textViewResourceId, objects);
			layout = textViewResourceId;
			this.listAccounts = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			LogHelper.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(layout, null);
				holder = new ViewHolder();
				holder.tvname = (TextView) convertView.findViewById(R.id.tv_account_name);
				holder.selected = (RadioButton) convertView.findViewById(R.id.ck_account_selected);
				holder.progress = (ProgressBar) convertView.findViewById(R.id.progress_registering);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AccountItem account = listAccounts.get(position);
			if (account != null) {
				holder.tvname.setText(account.name);
				holder.selected.setChecked(account.isChecked);
				if (account.isChecked) {
					checkedIndex = position;
				}
				holder.selected.setVisibility((!account.showProgress) ? View.VISIBLE : View.GONE);
				holder.progress.setVisibility((account.showProgress) ? View.VISIBLE : View.GONE);
				holder.tvname.setTag(account);
			}
			return convertView;
		}

		public void registering(int position) {

			uniqueSelectedItem(position);
			listAccounts.get(position).isChecked = false;
			listAccounts.get(position).showProgress = true;
		}

		private void uniqueSelectedItem(int position) {
			if (checkedIndex != position) {
				if (checkedIndex != -1)
					listAccounts.get(checkedIndex).isChecked = false;
			}
			checkedIndex = position;
		}

		public void isRegistered(int position) {
			uniqueSelectedItem(position);
			listAccounts.get(position).isChecked = true;
			listAccounts.get(position).showProgress = false;
		}

		public void reset() {
			if (!listAccounts.isEmpty()) {
				for (int i = 0; i < listAccounts.size(); i++) {
					listAccounts.get(i).isChecked = false;
					listAccounts.get(i).showProgress = false;
				}
			}
		}

		public void reset(String name) {
			for (int i = 0; i < listAccounts.size(); i++) {
				if (listAccounts.get(i).name.equals(name)) {
					listAccounts.get(i).isChecked = false;
					listAccounts.get(i).showProgress = false;
				}
			}

		}

	}

}
