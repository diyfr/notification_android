package org.diyfr.alertermoi.activity.view;

import org.diyfr.alertermoi.content.provider.messageprovider.messages.MessagesCursor;

public class SwipeToDeleteCursorWrapper extends MessagesCursor {
	private int mVirtualPosition;
	private int mHiddenPosition;

	public SwipeToDeleteCursorWrapper(MessagesCursor cursor, int hiddenPosition) {
		super(cursor);
		mVirtualPosition = -1;
		mHiddenPosition = hiddenPosition;
	}

	@Override
	public int getCount() {
		return super.getCount() - 1;
	}

	@Override
	public int getPosition() {
		return mVirtualPosition;
	}

	@Override
	public boolean move(int offset) {
		return moveToPosition(getPosition() + offset);
	}

	@Override
	public boolean moveToFirst() {
		return moveToPosition(0);
	}

	@Override
	public boolean moveToLast() {
		return moveToPosition(getCount() - 1);
	}

	@Override
	public boolean moveToNext() {
		return moveToPosition(getPosition() + 1);
	}

	@Override
	public boolean moveToPosition(int position) {
		mVirtualPosition = position;
		int cursorPosition = position;
		if (cursorPosition >= mHiddenPosition) {
			cursorPosition++;
		}
		return super.moveToPosition(cursorPosition);
	}

	@Override
	public boolean moveToPrevious() {
		return moveToPosition(getPosition() - 1);
	}

	@Override
	public boolean isBeforeFirst() {
		return getPosition() == -1 || getCount() == 0;
	}

	@Override
	public boolean isFirst() {
		return getPosition() == 0 && getCount() != 0;
	}

	@Override
	public boolean isLast() {
		int count = getCount();
		return getPosition() == (count - 1) && count != 0;
	}

	@Override
	public boolean isAfterLast() {
		int count = getCount();
		return getPosition() == count || count == 0;
	}
}