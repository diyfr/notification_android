package org.diyfr.alertermoi.content.provider.messageprovider.messages;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import org.diyfr.alertermoi.content.provider.messageprovider.base.AbstractSelection;

/**
 * Selection for the {@code messages} table.
 */
public class MessagesSelection extends AbstractSelection<MessagesSelection> {
    @Override
    public Uri uri() {
        return MessagesColumns.CONTENT_URI;
    }
    
    /**
     * Query the given content resolver using this selection.
     * 
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code MessagesCursor} object, which is positioned before the first entry, or null.
     */
    public MessagesCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new MessagesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public MessagesCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public MessagesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }
    
    
    public MessagesSelection id(long... value) {
        addEquals(MessagesColumns._ID, toObjectArray(value));
        return this;
    }

    public MessagesSelection messageType(String... value) {
        addEquals(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }
    
    public MessagesSelection messageTypeNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }


    public MessagesSelection messageTitle(String... value) {
        addEquals(MessagesColumns.MESSAGE_TITLE, value);
        return this;
    }
    
    public MessagesSelection messageTitleNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_TITLE, value);
        return this;
    }


    public MessagesSelection messageSubtitle(String... value) {
        addEquals(MessagesColumns.MESSAGE_SUBTITLE, value);
        return this;
    }
    
    public MessagesSelection messageSubtitleNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_SUBTITLE, value);
        return this;
    }


    public MessagesSelection messageIcon(String... value) {
        addEquals(MessagesColumns.MESSAGE_ICON, value);
        return this;
    }
    
    public MessagesSelection messageIconNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_ICON, value);
        return this;
    }


    public MessagesSelection messageContent(String... value) {
        addEquals(MessagesColumns.MESSAGE_CONTENT, value);
        return this;
    }
    
    public MessagesSelection messageContentNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_CONTENT, value);
        return this;
    }


    public MessagesSelection messageDateEmission(Date... value) {
        addEquals(MessagesColumns.MESSAGE_DATE_EMISSION, value);
        return this;
    }
    
    public MessagesSelection messageDateEmissionNot(Date... value) {
        addNotEquals(MessagesColumns.MESSAGE_DATE_EMISSION, value);
        return this;
    }

    public MessagesSelection messageDateEmission(long... value) {
        addEquals(MessagesColumns.MESSAGE_DATE_EMISSION, toObjectArray(value));
        return this;
    }

    public MessagesSelection messageDateEmissionAfter(Date value) {
        addGreaterThan(MessagesColumns.MESSAGE_DATE_EMISSION, value);
        return this;
    }

    public MessagesSelection messageDateEmissionAfterEq(Date value) {
        addGreaterThanOrEquals(MessagesColumns.MESSAGE_DATE_EMISSION, value);
        return this;
    }

    public MessagesSelection messageDateEmissionBefore(Date value) {
        addLessThan(MessagesColumns.MESSAGE_DATE_EMISSION, value);
        return this;
    }

    public MessagesSelection messageDateEmissionBeforeEq(Date value) {
        addLessThanOrEquals(MessagesColumns.MESSAGE_DATE_EMISSION, value);
        return this;
    }

    public MessagesSelection messageDateReception(Date... value) {
        addEquals(MessagesColumns.MESSAGE_DATE_RECEPTION, value);
        return this;
    }
    
    public MessagesSelection messageDateReceptionNot(Date... value) {
        addNotEquals(MessagesColumns.MESSAGE_DATE_RECEPTION, value);
        return this;
    }

    public MessagesSelection messageDateReception(long... value) {
        addEquals(MessagesColumns.MESSAGE_DATE_RECEPTION, toObjectArray(value));
        return this;
    }

    public MessagesSelection messageDateReceptionAfter(Date value) {
        addGreaterThan(MessagesColumns.MESSAGE_DATE_RECEPTION, value);
        return this;
    }

    public MessagesSelection messageDateReceptionAfterEq(Date value) {
        addGreaterThanOrEquals(MessagesColumns.MESSAGE_DATE_RECEPTION, value);
        return this;
    }

    public MessagesSelection messageDateReceptionBefore(Date value) {
        addLessThan(MessagesColumns.MESSAGE_DATE_RECEPTION, value);
        return this;
    }

    public MessagesSelection messageDateReceptionBeforeEq(Date value) {
        addLessThanOrEquals(MessagesColumns.MESSAGE_DATE_RECEPTION, value);
        return this;
    }

    public MessagesSelection messageLu(boolean... value) {
        addEquals(MessagesColumns.MESSAGE_LU, toObjectArray(value));
        return this;
    }
    
    public MessagesSelection messageLuNot(boolean... value) {
        addNotEquals(MessagesColumns.MESSAGE_LU, toObjectArray(value));
        return this;
    }


    public MessagesSelection messageLevel(int... value) {
        addEquals(MessagesColumns.MESSAGE_LEVEL, toObjectArray(value));
        return this;
    }
    
    public MessagesSelection messageLevelNot(int... value) {
        addNotEquals(MessagesColumns.MESSAGE_LEVEL, toObjectArray(value));
        return this;
    }

    public MessagesSelection messageLevelGt(int value) {
        addGreaterThan(MessagesColumns.MESSAGE_LEVEL, value);
        return this;
    }

    public MessagesSelection messageLevelGtEq(int value) {
        addGreaterThanOrEquals(MessagesColumns.MESSAGE_LEVEL, value);
        return this;
    }

    public MessagesSelection messageLevelLt(int value) {
        addLessThan(MessagesColumns.MESSAGE_LEVEL, value);
        return this;
    }

    public MessagesSelection messageLevelLtEq(int value) {
        addLessThanOrEquals(MessagesColumns.MESSAGE_LEVEL, value);
        return this;
    }
}
