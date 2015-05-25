package org.diyfr.alertermoi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper
{
	/**
	 * Méthode statique permettant de savoir sir le mobile est connecté ou en cours de connexion
	 * @param mContext Contexte de l'application
	 * @return (boolean)
	 */
	public static boolean isConnectedOrConnecting(Context mContext)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = connectivityManager.getActiveNetworkInfo();
		return (network != null && network.isAvailable());
	}
	

}
