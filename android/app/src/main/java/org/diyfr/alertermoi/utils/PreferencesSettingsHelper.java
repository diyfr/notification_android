package org.diyfr.alertermoi.utils;

import org.diyfr.alertermoi.constantes.ConstantesApplication;

import android.content.Context;

public class PreferencesSettingsHelper
{
	public static String get_UUID(Context context)
	{
		return new PreferencesHelper(context).getString(ConstantesApplication.PREF_KEY_UUID, "");
	}

	public static void set_UUID(Context context, String value)
	{
		new PreferencesHelper(context).setString(ConstantesApplication.PREF_KEY_UUID, value);
	}

	public static boolean is_REGISTERED(Context context)
	{
		return new PreferencesHelper(context).getBoolean(ConstantesApplication.PREF_KEY_REGISTERED_WS, false);
	}

	public static void set_REGISTERED(Context context, boolean value)
	{
		new PreferencesHelper(context).setBoolean(ConstantesApplication.PREF_KEY_REGISTERED_WS, value);
	}

	public static String get_REGISTERED_ID(Context context)
	{
		return new PreferencesHelper(context).getString(ConstantesApplication.PREF_KEY_REGISTERED_ID, "");
	}

	public static void set_REGISTERED_ID(Context context, String value)
	{
		new PreferencesHelper(context).setString(ConstantesApplication.PREF_KEY_REGISTERED_ID, value);
	}

    public static void set_USER_GOOGLE_EMAIL(Context context, String value)
    {
        new PreferencesHelper(context).setString(ConstantesApplication.PREF_KEY_USER_EMAIL, value);
    }

    public static String get_USER_GOOGLE_EMAIL(Context context)
    {
        return new PreferencesHelper(context).getString(ConstantesApplication.PREF_KEY_USER_EMAIL, "");
    }


    public static void set_URL_WS(Context context, String value)
    {
        new PreferencesHelper(context).setString(ConstantesApplication.PREF_KEY_WS_URL, value);
    }

    public static String get_URL_WS(Context context)
    {
        return new PreferencesHelper(context).getString(ConstantesApplication.PREF_KEY_WS_URL, "");
    }

    public static void set_SENDER_ID(Context context, String value)
    {
        new PreferencesHelper(context).setString(ConstantesApplication.PREF_KEY_SENDER_ID, value);
    }

    public static String get_SENDER_ID(Context context)
    {
        return new PreferencesHelper(context).getString(ConstantesApplication.PREF_KEY_SENDER_ID, "");
    }

}
