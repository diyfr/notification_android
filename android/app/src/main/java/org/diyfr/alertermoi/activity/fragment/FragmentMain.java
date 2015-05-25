package org.diyfr.alertermoi.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.diyfr.alertermoi.activity.view.MessageView;
import org.diyfr.alertermoi.activity.view.SwipeDismissListViewTouchListener;
import org.diyfr.alertermoi.activity.view.SwipeToDeleteCursorWrapper;
import org.diyfr.alertermoi.constantes.ConstantesGCM;
import org.diyfr.alertermoi.content.provider.messageprovider.messages.MessagesContentValues;
import org.diyfr.alertermoi.content.provider.messageprovider.messages.MessagesCursor;
import org.diyfr.alertermoi.content.provider.messageprovider.messages.MessagesSelection;
import org.diyfr.alertermoi.model.MessageNotification;
import org.diyfr.alertermoi.services.GCMIntentService;
import org.diyfr.alertermoi.utils.LogHelper;
import org.diyfr.alertermoi.R;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class FragmentMain extends Fragment {
	ListView lstMessages;
	List<MessageNotification> currentMessages;
	MessagesCursorAdapter messagesCursorAdapter;
	SwipeDismissListViewTouchListener touchListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		lstMessages = (ListView) rootView.findViewById(R.id.lv_message);
		currentMessages = new ArrayList<MessageNotification>();
		loadCursor();
		lstMessages.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FragmentMain.this.messagesCursorAdapter.onClick(position);
			}
		});
		touchListener = new SwipeDismissListViewTouchListener(lstMessages, new SwipeDismissListViewTouchListener.DismissCallbacks() {
			@Override
			public boolean canDismiss(int position) {
				return true;
			}

			@Override
			public void onDismiss(ListView listView, int[] reverseSortedPositions) {

				SwipeToDeleteCursorWrapper cursorWrapper = new SwipeToDeleteCursorWrapper((MessagesCursor) messagesCursorAdapter.getCursor(),
						reverseSortedPositions[0]);
				LogHelper.w("FragMain", "remove " + reverseSortedPositions[0]);
				FragmentMain.this.messagesCursorAdapter.remove(reverseSortedPositions[0]);
				messagesCursorAdapter.swapCursor(cursorWrapper);
			}
		});
		lstMessages.setOnTouchListener(touchListener);
		lstMessages.setOnScrollListener(touchListener.makeScrollListener());
		return rootView;
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleNotificationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogHelper.w("FragMain", "Receive notificaiton");
			if (intent.getAction().equals(ConstantesGCM.ACTION_GCM_NOTIFICATION_RECEIPT)) {
				FragmentMain.this.loadCursor();
				if (FragmentMain.this.messagesCursorAdapter != null)
					FragmentMain.this.messagesCursorAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onResume() {
		LogHelper.w("FragMain", "Registering Receiver");
		getActivity().registerReceiver(mHandleNotificationReceiver, new IntentFilter(ConstantesGCM.ACTION_GCM_NOTIFICATION_RECEIPT));
		loadCursor();
		super.onResume();
	}

	@Override
	public void onPause() {
		try {
			LogHelper.w("FragMain", "UnRegistering Receiver");
			getActivity().unregisterReceiver(mHandleNotificationReceiver);
		} catch (Exception e) {
			LogHelper.d("FgMain", e.getMessage());
		}
		super.onPause();
	}

	private void loadCursor() {
		new AsyncTask<Void, Void, Cursor>() {
			@Override
			protected Cursor doInBackground(Void... params) {
				MessagesSelection lstMessagesSelection = new MessagesSelection();
				MessagesCursor cursor = null;
				if (getActivity() != null && getActivity().getContentResolver() != null) {
					cursor = lstMessagesSelection.query(getActivity().getContentResolver());
				}
				return cursor;
			}

			@Override
			protected void onPostExecute(Cursor result) {
				if (result != null) {
					if (FragmentMain.this.messagesCursorAdapter == null || FragmentMain.this.lstMessages.getAdapter() == null) {
						if (getActivity() != null && getActivity().getApplicationContext() != null) {
							FragmentMain.this.messagesCursorAdapter = new MessagesCursorAdapter(getActivity().getApplicationContext(), result, 0);
							FragmentMain.this.lstMessages.setAdapter(messagesCursorAdapter);
						}
					} else {
						FragmentMain.this.messagesCursorAdapter.swapCursor(result);
					}
				}
			}
		}.execute();
	}

	class MessagesCursorAdapter extends CursorAdapter {

		public MessagesCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);

		}

		public void onClick(int position) {
			MessagesCursor currentCursor = (MessagesCursor) getCursor();
			if (currentCursor.moveToPosition(position)) {
				if (!currentCursor.getMessageLu()) {
					MessagesSelection currentSelection = new MessagesSelection();
					currentSelection.id(currentCursor.getId()).query(getActivity().getContentResolver());
					MessagesContentValues currentMessage = new MessagesContentValues();
					currentMessage.putMessageContent(currentCursor.getMessageContent());
					currentMessage.putMessageTitle(currentCursor.getMessageTitle());
					currentMessage.putMessageSubtitle(currentCursor.getMessageSubtitle());
					currentMessage.putMessageType(currentCursor.getMessageType());
					currentMessage.putMessageDateEmission(currentCursor.getMessageDateEmission());
					currentMessage.putMessageDateReception(currentCursor.getMessageDateReception());
					currentMessage.putMessageLevel(currentCursor.getMessageLevel());
					currentMessage.putMessageLu(true);
					currentMessage.update(getActivity().getContentResolver(), currentSelection);
					FragmentMain.this.loadCursor();
					FragmentMain.this.removeNotifcation();
					notifyDataSetChanged();
				}
			}

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			MessageView finalView = new MessageView(getActivity().getApplicationContext());

			return finalView;
		}

		public void remove(int position) {
			MessagesCursor currentCursor = (MessagesCursor) getCursor();
			if (currentCursor.moveToPosition(position)) {
				MessagesSelection currentSelection = new MessagesSelection();
				LogHelper.w("FragMain", "CursorId " + currentCursor.getId());
				currentSelection.id(currentCursor.getId()).delete(getActivity().getContentResolver());
			} else {
				LogHelper.w("FragMain", "moveToPosition " + position + " impossible count=" + currentCursor.getCount());
			}
			loadCursor();
			notifyDataSetChanged();
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			MessagesCursor currentCursor = (MessagesCursor) cursor;
			MessageNotification message = new MessageNotification();
			message.setContent(currentCursor.getMessageContent());
			message.set_id(currentCursor.getId());
			message.setTitle(currentCursor.getMessageTitle());
			message.setSubtitle(currentCursor.getMessageSubtitle());
			message.setIcon(currentCursor.getMessageIcon());
			message.setDateEmission(currentCursor.getMessageDateEmission());
			message.setDateReception(currentCursor.getMessageDateReception());
			message.setLevel(currentCursor.getMessageLevel());
			message.setReaded(currentCursor.getMessageLu());
			message.setType(currentCursor.getMessageType());
			((MessageView) view).setMessage(message);
		}

	}

	public void removeNotifcation() {
		final NotificationManager notificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(GCMIntentService.NOTIFICATION_ID);

	}
}
