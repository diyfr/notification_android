package org.diyfr.alertermoi.services;

import java.util.Date;

import org.diyfr.alertermoi.activity.MainActivity;
import org.diyfr.alertermoi.constantes.ConstantesGCM;
import org.diyfr.alertermoi.content.provider.messageprovider.messages.MessagesContentValues;
import org.diyfr.alertermoi.model.MessageNotification;
import org.diyfr.alertermoi.utils.DateHelper;
import org.diyfr.alertermoi.utils.LogHelper;
import org.diyfr.alertermoi.utils.PreferencesSettingsHelper;
import org.diyfr.alertermoi.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	public static int NOTIFICATION_ID = 1;

	public GCMIntentService() {
		super("");// super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		LogHelper.i(TAG, "Device registered: regId = " + registrationId);
		PreferencesSettingsHelper.set_REGISTERED_ID(context, registrationId);
		Intent intent = new Intent(ConstantesGCM.ACTION_GCM_REGISTERED_RECEIPT);
		intent.putExtra(ConstantesGCM.EXTRA_KEY_GCM_REGISTERD_ID, registrationId);
		context.sendBroadcast(intent);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		LogHelper.i(TAG, "Device unregistered");
		PreferencesSettingsHelper.set_REGISTERED_ID(context, "");
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		LogHelper.i(TAG, "Received message");
		// String message=null;
		// int typeMessage=CST_GCM.EXTRA_VALUE_TYPE_OTHER;
		if (intent.getExtras().containsKey(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_CONTENT)
				&& intent.getExtras().containsKey(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_LEVEL)
				&& intent.getExtras().containsKey(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_EMISSION)
				&& intent.getExtras().containsKey(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_TYPE)) {
			MessageNotification notif = new MessageNotification();
			notif.setLevel(Integer.parseInt(intent.getExtras().getString(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_LEVEL)));
			notif.setType(intent.getExtras().getString(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_TYPE));
			notif.setDateEmission(DateHelper.parse(intent.getExtras().getString(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_EMISSION)));
			notif.setContent(intent.getExtras().getString(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_CONTENT));
			notif.setTitle(intent.getExtras().getString(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_TITLE));
			notif.setSubtitle(intent.getExtras().getString(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_SUBTITLE));
			if (intent.getExtras().containsKey(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_ICON))
				notif.setIcon(intent.getExtras().getString(ConstantesGCM.EXTRA_KEY_GCM_MESSAGE_ICON));
			saveAndNotify(this, notif);
		}
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		LogHelper.i(TAG, "Received deleted messages notification");
	}

	@Override
	public void onError(Context context, String errorId) {
		LogHelper.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		LogHelper.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	private void saveAndNotify(Context context, MessageNotification message) {
		MessagesContentValues currentMessage = new MessagesContentValues();
		currentMessage.putMessageContent(message.getContent());
		currentMessage.putMessageTitle(message.getTitle());
		if (message.getIcon() != null)
			currentMessage.putMessageIcon(message.getIcon());
		currentMessage.putMessageSubtitle(message.getSubtitle());
		currentMessage.putMessageType(message.getType());
		currentMessage.putMessageDateEmission(message.getDateEmission());
		currentMessage.putMessageDateReception(new Date());
		currentMessage.putMessageLevel(message.getLevel());
		currentMessage.putMessageLu(false);
		currentMessage.insert(getApplicationContext().getContentResolver());
		// On créé la notification
		createNotification(context, message);
	}

	private final void createNotification(Context context, MessageNotification message) {
		// Récupération du notification Manager
		final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Définition de la redirection au moment du clic sur la notification.
		// Dans notre cas la notification redirige vers notre application
		final Intent resultIntent = new Intent(this, MainActivity.class);
		Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_large);
		if (message.getIcon() != null) {
			try {
				int width = largeIcon.getWidth();
				int height = largeIcon.getHeight();
				byte[] decodedString = Base64.decode(message.getIcon(), Base64.DEFAULT);
				largeIcon=Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), width, height, false);
			} catch (IllegalArgumentException i) {
			}
		}
		// Récupération du titre et description de la notification
		final String notificationTitle = message.getTitle();
		final String notificationDesc = message.getSubtitle();
		int notificationIcone = R.drawable.notification_icon;
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).setSmallIcon(notificationIcone).setLargeIcon(largeIcon)
				.setContentTitle(notificationTitle).setContentText(notificationDesc).setDefaults(Notification.DEFAULT_ALL);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
		notificationBuilder.setContentIntent(resultPendingIntent);
		notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
		Intent intent = new Intent(ConstantesGCM.ACTION_GCM_NOTIFICATION_RECEIPT);
		context.sendBroadcast(intent);

	}
}
