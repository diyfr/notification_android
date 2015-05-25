package org.diyfr.alertermoi.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * Classe de gestion des données préférences
 * 
 * @author info@diyfr.org
 */
public class PreferencesHelper
{

	/**
	 * Le nom de la section des préférences de l'application
	 */
	public static final String PREFERENCE_NAME = "sharedPref";

	/**
	 * L'objet contenant les préférences liées é l'application
	 */
	private SharedPreferences preferences;

	/**
	 * Editeur de préférences
	 */
	private static SharedPreferences.Editor editorTransaction;

	/**
	 * 
	 * Constructeur de la classe
	 * 
	 * @param context
	 *            Le contexte de l'application
	 * 
	 */
	public PreferencesHelper(Context context)
	{

		preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
	}

	/**
	 * Début de la transaction
	 */
	public void beginTransaction()
	{
		editorTransaction = preferences.edit();
	}

	/**
	 * Cléture de la transaction
	 */
	public void endTransaction()
	{
		if (editorTransaction != null)
		{
			editorTransaction.commit();
		}
		editorTransaction = null;
	}

	/**
	 * Suppression d'une valeur
	 * 
	 * @param key
	 *            Clé de préférence
	 */
	public void remove(String key)
	{
		if (editorTransaction == null)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.remove(key);
			editor.commit();
		}
		else
		{
			editorTransaction.remove(key);
		}
	}

	/**
	 * Obtient un boolean é partir d'un clé
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param defValue
	 *            Valeur par défaut
	 * @return
	 */
	public Boolean getBoolean(String key, boolean defValue)
	{
		return preferences.getBoolean(key, defValue);
	}

	/**
	 * Définit un boolean pour un clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param value
	 */
	public void setBoolean(String key, boolean value)
	{
		if (editorTransaction == null)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(key, value);
			editor.commit();
		}
		else
		{
			editorTransaction.putBoolean(key, value);
		}
	}

	/**
	 * Retourne un String pour une clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param defValue
	 *            Valeur par défaut
	 * @return
	 */
	public String getString(String key, String defValue)
	{
		return preferences.getString(key, defValue);
	}

	/**
	 * Définit un String pour un clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param value
	 */
	public void setString(String key, String value)
	{
		if (editorTransaction == null)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(key, value);
			editor.commit();
		}
		else
		{
			editorTransaction.putString(key, value);
		}
	}

	/**
	 * Retourne un integer pour une clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param defValue
	 *            Valeur par défaut
	 * @return
	 */
	public int getInt(String key, int defValue)
	{
		return preferences.getInt(key, defValue);
	}

	/**
	 * Définit un Integer pour un clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param value
	 */
	public void setInt(String key, int value)
	{
		if (editorTransaction == null)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(key, value);
			editor.commit();
		}
		else
		{
			editorTransaction.putInt(key, value);
		}
	}

	/**
	 * Retourne un Float pour une clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param defValue
	 *            Valeur par défaut
	 * @return
	 */
	public float getFloat(String key, float defValue)
	{
		return preferences.getFloat(key, defValue);
	}

	/**
	 * Définit un Float pour un clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param value
	 */
	public void setFloat(String key, float value)
	{
		if (editorTransaction == null)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putFloat(key, value);
			editor.commit();
		}
		else
		{
			editorTransaction.putFloat(key, value);
		}
	}

	/**
	 * Retourne un Long pour une clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param defValue
	 *            Valeur par défaut
	 * @return
	 */
	public long getLong(String key, long defValue)
	{
		return preferences.getLong(key, defValue);
	}

	/**
	 * Définit un Long pour un clé donnée
	 * 
	 * @param key
	 *            Clé de préférence
	 * @param value
	 */
	public void setLong(String key, long value)
	{
		if (editorTransaction == null)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putLong(key, value);
			editor.commit();
		}
		else
		{
			editorTransaction.putLong(key, value);
		}
	}
}
