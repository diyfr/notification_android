package org.diyfr.alertermoi.activity.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.diyfr.alertermoi.R;
import org.diyfr.alertermoi.activity.MainActivity;
import org.diyfr.alertermoi.constantes.ConstantesGCM;
import org.diyfr.alertermoi.utils.LogHelper;
import org.diyfr.alertermoi.utils.PreferencesSettingsHelper;

/**
 * Created by Stephane on 23/05/2015.
 */
public class FragmentSettings extends Fragment implements View.OnClickListener {

    private static String TAG = "Settings";
    private TextView tv_sender_id, tv_url_ws;
    private Button btn_sender, btn_ws;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        tv_sender_id = (TextView) rootView.findViewById(R.id.tv_sender_id);
        tv_url_ws = (TextView) rootView.findViewById(R.id.tv_url_ws);
        btn_sender = (Button) rootView.findViewById(R.id.btn_valid_sender_id);
        btn_ws = (Button) rootView.findViewById(R.id.btn_valid_ws_url);
        btn_sender.setOnClickListener(this);
        btn_ws.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        updateState();
        super.onResume();
    }


    private void updateState() {
        btn_ws.setVisibility(View.VISIBLE);
        btn_sender.setVisibility(View.VISIBLE);
        if (PreferencesSettingsHelper.get_SENDER_ID(getActivity().getApplicationContext()).isEmpty()) {
            // pas de senderId il faut que l'utilisateur le saisisse
            btn_ws.setEnabled(false);
            tv_url_ws.setEnabled(false);
            btn_sender.setEnabled(true);
            tv_sender_id.setEnabled(true);
        } else {
            // Il y a un sender ID
            tv_sender_id.setText(PreferencesSettingsHelper.get_SENDER_ID(getActivity().getApplicationContext()));
            btn_ws.setEnabled(true);
            tv_url_ws.setEnabled(true);
            btn_sender.setEnabled(false);
            tv_sender_id.setEnabled(false);

            if (PreferencesSettingsHelper.get_REGISTERED_ID(getActivity().getApplicationContext()).isEmpty()) {
                //pas de registered_id il faut réaliser l'appel vers GCM pour enregistrer le device
                LogHelper.i(TAG, "Launch GCM registrar");
                getActivity().registerReceiver(mHandleMessageReceiver, new IntentFilter(ConstantesGCM.ACTION_GCM_REGISTERED_RECEIPT));
                Toast.makeText(getActivity().getApplicationContext(), "Enregistrement auprès de GCM", Toast.LENGTH_SHORT).show();
                GCMRegistrar.register(getActivity(), PreferencesSettingsHelper.get_SENDER_ID(getActivity().getApplicationContext()));
                // On désactive tout par default
                btn_ws.setEnabled(false);
                tv_url_ws.setEnabled(false);
                btn_sender.setEnabled(false);
                tv_sender_id.setEnabled(false);
            } else {
                // il y a le registered_id
                if (!PreferencesSettingsHelper.get_URL_WS(getActivity().getApplicationContext()).isEmpty()) {
                    tv_url_ws.setText(PreferencesSettingsHelper.get_URL_WS(getActivity().getApplicationContext()));
                }
                if (!PreferencesSettingsHelper.is_REGISTERED(getActivity().getApplicationContext())) {
                    // L'utilisateur n'a pas été enregistrer auprès du serveur
                    btn_ws.setEnabled(true);
                    tv_url_ws.setEnabled(true);
                    btn_sender.setEnabled(false);
                    tv_sender_id.setEnabled(false);
                } else {
                    // Tout est bon
                    btn_ws.setVisibility(View.GONE);
                    tv_url_ws.setEnabled(false);
                    btn_sender.setVisibility(View.GONE);
                    tv_sender_id.setEnabled(false);
                }
            }
        }
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

    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!PreferencesSettingsHelper.get_REGISTERED_ID(getActivity().getApplicationContext()).isEmpty()) {
                LogHelper.i(TAG, "Get registered ID");
                Toast.makeText(getActivity().getApplicationContext(), "L'enregistrement réussi auprès de GCM", Toast.LENGTH_SHORT).show();
                updateState();
            } else {
                PreferencesSettingsHelper.set_REGISTERED_ID(getActivity().getApplicationContext(), "");
                PreferencesSettingsHelper.set_SENDER_ID(getActivity().getApplicationContext(), "");
                Toast.makeText(getActivity().getApplicationContext(), "L'enregistrement auprès de GCM a échoué.", Toast.LENGTH_LONG).show();
                updateState();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_valid_sender_id:
                if (!tv_sender_id.getText().toString().isEmpty()) {
                    //registered_id
                    LogHelper.i(TAG, "Launch GCM registrar");
                    getActivity().registerReceiver(mHandleMessageReceiver, new IntentFilter(ConstantesGCM.ACTION_GCM_REGISTERED_RECEIPT));
                    Toast.makeText(getActivity().getApplicationContext(), "Enregistrement auprès de GCM", Toast.LENGTH_SHORT).show();
                    PreferencesSettingsHelper.set_SENDER_ID(getActivity().getApplicationContext(), tv_sender_id.getText().toString().trim());
                    GCMRegistrar.register(getActivity(), tv_sender_id.getText().toString().trim());
                    btn_sender.setEnabled(false);
                    tv_sender_id.setEnabled(false);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Saisissez d'abord votre sender Id.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_valid_ws_url:
                if (!tv_url_ws.getText().toString().isEmpty()) {
                    //test register to serveur??
                    PreferencesSettingsHelper.set_URL_WS(getActivity().getApplicationContext(), tv_url_ws.getText().toString().trim());
                    if (PreferencesSettingsHelper.get_USER_GOOGLE_EMAIL(getActivity().getApplicationContext()).isEmpty()) {
                        ((MainActivity) getActivity()).returnAccountFragment();
                    } else {
                        ((MainActivity) getActivity()).returnMainFragment();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Saisissez d'abord l'url de votre serveur.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}