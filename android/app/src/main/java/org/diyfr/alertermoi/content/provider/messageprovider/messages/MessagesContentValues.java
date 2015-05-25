package org.diyfr.alertermoi.content.provider.messageprovider.messages;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import org.diyfr.alertermoi.content.provider.messageprovider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code messages} table.
 */
public class MessagesContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return MessagesColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     * 
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, MessagesSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public MessagesContentValues putMessageType(String value) {
        mContentValues.put(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }

    public MessagesContentValues putMessageTypeNull() {
        mContentValues.putNull(MessagesColumns.MESSAGE_TYPE);
        return this;
    }


    public MessagesContentValues putMessageTitle(String value) {
        if (value == null) throw new IllegalArgumentException("value for messageTitle must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_TITLE, value);
        return this;
    }



    public MessagesContentValues putMessageSubtitle(String value) {
        if (value == null) throw new IllegalArgumentException("value for messageSubtitle must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_SUBTITLE, value);
        return this;
    }



    public MessagesContentValues putMessageIcon(String value) {
        if (value == null) throw new IllegalArgumentException("value for messageIcon must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_ICON, value);
        return this;
    }



    public MessagesContentValues putMessageContent(String value) {
        if (value == null) throw new IllegalArgumentException("value for messageContent must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_CONTENT, value);
        return this;
    }



    public MessagesContentValues putMessageDateEmission(Date value) {
        if (value == null) throw new IllegalArgumentException("value for messageDateEmission must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_DATE_EMISSION, value.getTime());
        return this;
    }


    public MessagesContentValues putMessageDateEmission(long value) {
        mContentValues.put(MessagesColumns.MESSAGE_DATE_EMISSION, value);
        return this;
    }


    public MessagesContentValues putMessageDateReception(Date value) {
        if (value == null) throw new IllegalArgumentException("value for messageDateReception must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_DATE_RECEPTION, value.getTime());
        return this;
    }


    public MessagesContentValues putMessageDateReception(long value) {
        mContentValues.put(MessagesColumns.MESSAGE_DATE_RECEPTION, value);
        return this;
    }


    public MessagesContentValues putMessageLu(boolean value) {
        mContentValues.put(MessagesColumns.MESSAGE_LU, value);
        return this;
    }



    public MessagesContentValues putMessageLevel(int value) {
        mContentValues.put(MessagesColumns.MESSAGE_LEVEL, value);
        return this;
    }


}
