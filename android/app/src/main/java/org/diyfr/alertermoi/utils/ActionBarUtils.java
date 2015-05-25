package org.diyfr.alertermoi.utils;

import java.lang.reflect.Field;


import android.content.Context;
import android.view.ViewConfiguration;

public class ActionBarUtils {

	/**
	 * Force l'affichage normal de l'action bar 
	 * @param context
	 */
	public static void AffichageNormal(Context context) {

		// Force l'affichage des overflow items dans l'action bar (au lieu
		// d'utiliser le bouton physique)
		try {
			ViewConfiguration config = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
			LogHelper.e("ActionBarUtils", "AffichageNormal :", ex);
		}
	}
}