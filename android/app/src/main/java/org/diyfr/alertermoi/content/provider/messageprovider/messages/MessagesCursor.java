package org.diyfr.alertermoi.content.provider.messageprovider.messages;

import java.util.Date;

import android.database.Cursor;

import org.diyfr.alertermoi.content.provider.messageprovider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code messages} table.
 */
public class MessagesCursor extends AbstractCursor {
    public MessagesCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code message_type} value.
     * Can be {@code null}.
     */
    public String getMessageType() {
        Integer index = getCachedColumnIndexOrThrow(MessagesColumns.MESSAGE_TYPE);
        return getString(index);
    }

    /**
     * Get the {@code message_title} value.
     * Cannot be {@code null}.
     */
    public String getMessageTitle() {
        Integer index = getCachedColumnIndexOrThrow(MessagesColumns.MESSAGE_TITLE);
        return getString(index);
    }

    /**
     * Get the {@code message_subtitle} value.
     * Cannot be {@code null}.
     */
    public String getMessageSubtitle() {
        Integer index = getCachedColumnIndexOrThrow(MessagesColumns.MESSAGE_SUBTITLE);
        return getString(index);
    }

    /**
     * Get the {@code message_icon} value.
     * Cannot be {@code null}.
     */
    public String getMessageIcon() {
        Integer index = getCachedColumnIndexOrThrow(MessagesColumns.MESSAGE_ICON);
        return getString(index);
    }

    /**
     * Get the {@code message_content} value.
     * Cannot be {@code null}.
     */
    public String getMessageContent() {
        Integer index = getCachedColumnIndexOrThrow(MessagesColumns.MESSAGE_CONTENT);
        return getString(index);
    }

    /**
     * Get the {@code message_date_emission} value.
     * Cannot be {@code null}.
     */
    public Date getMessageDateEmission() {
        return getDate(MessagesColumns.MESSAGE_DATE_EMISSION);
    }

    /**
     * Get the {@code message_date_reception} value.
     * Cannot be {@code null}.
     */
    public Date getMessageDateReception() {
        return getDate(MessagesColumns.MESSAGE_DATE_RECEPTION);
    }

    /**
     * Get the {@code message_lu} value.
     */
    public boolean getMessageLu() {
        return getBoolean(MessagesColumns.MESSAGE_LU);
    }

    /**
     * Get the {@code message_level} value.
     */
    public int getMessageLevel() {
        return getIntegerOrNull(MessagesColumns.MESSAGE_LEVEL);
    }
}
