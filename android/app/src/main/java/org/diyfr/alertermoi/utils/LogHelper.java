package org.diyfr.alertermoi.utils;

import org.diyfr.alertermoi.BuildConfig;

/**
 * Permet d'afficher les logs si l'application est en mode debug
 * 
 * @author info@diyfr.org
 * 
 */
public class LogHelper {

	/**
	 * TAG de l'application utilisé pour les Logs
	 */
	private static final String TAG = "AlerTerMoi";

	/**
	 * Permet de définir si la classe utils/view doit afficher les log Sa
	 * désactivation active Acra !
	 */
	public static boolean DEBUG_MODE = BuildConfig.DEBUG;

	/**
	 * Affiche dans les log en mode info
	 * 
	 * @param title
	 *            Tag associé au message
	 * @param message
	 *            Message à afficher dans les logs
	 */
	public static void i(String title, String message) {
		if (DEBUG_MODE) {
			android.util.Log.i(TAG, title + ":" + message);
		}
	}

	/**
	 * Affiche dans les log en mode error
	 * 
	 * @param title
	 *            Tag associé au message
	 * @param message
	 *            Message à afficher dans les logs
	 */
	public static void e(String title, String message) {
		if (DEBUG_MODE) {
			android.util.Log.e(TAG, title + ":" + message);
		}
	}

	/**
	 * Affiche dans les log en mode error
	 * 
	 * @param title
	 *            Tag associé au message
	 * @param message
	 *            Message à afficher dans les logs
	 *            levée
	 */
	public static void e(String title, String message, Throwable trow) {
		if (DEBUG_MODE) {
			android.util.Log.e(TAG, title + ":" + message, trow);
		}
	}

	/**
	 * Affiche dans les log en mode warn
	 * 
	 * @param title
	 *            Tag associé au message
	 * @param message
	 *            Message à afficher dans les logs
	 */
	public static void w(String title, String message) {
		if (DEBUG_MODE) {
			android.util.Log.w(TAG, title + ":" + message);
		}
	}

	/**
	 * Affiche dans les log en mode verbose
	 * 
	 * @param title
	 *            Tag associé au message
	 * @param message
	 *            Message à afficher dans les logs
	 */
	public static void v(String title, String message) {
		if (DEBUG_MODE) {
			android.util.Log.v(TAG, title + ":" + message);
		}
	}

	/**
	 * Affiche dans les log en mode debug
	 * 
	 * @param title
	 *            Tag associé au message
	 * @param message
	 *            Message à afficher dans les logs
	 */
	public static void d(String title, String message) {
		if (DEBUG_MODE) {
			android.util.Log.d(TAG, title + ":" + message);
		}
	}
}
