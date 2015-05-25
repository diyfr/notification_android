package org.diyfr.alertermoi.content.provider.messageprovider.messages;

import android.net.Uri;
import android.provider.BaseColumns;

import org.diyfr.alertermoi.content.provider.messageprovider.MessagesProvider;

/**
 * Columns for the {@code messages} table.
 */
public interface MessagesColumns extends BaseColumns {
    String TABLE_NAME = "messages";
    Uri CONTENT_URI = Uri.parse(MessagesProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    String _ID = BaseColumns._ID;
    String MESSAGE_TYPE = "message_type";
    String MESSAGE_TITLE = "message_title";
    String MESSAGE_SUBTITLE = "message_subtitle";
    String MESSAGE_ICON = "message_icon";
    String MESSAGE_CONTENT = "message_content";
    String MESSAGE_DATE_EMISSION = "message_date_emission";
    String MESSAGE_DATE_RECEPTION = "message_date_reception";
    String MESSAGE_LU = "message_lu";
    String MESSAGE_LEVEL = "message_level";

    String DEFAULT_ORDER = _ID;

	// @formatter:off
    String[] FULL_PROJECTION = new String[] {
            _ID,
            MESSAGE_TYPE,
            MESSAGE_TITLE,
            MESSAGE_SUBTITLE,
            MESSAGE_ICON,
            MESSAGE_CONTENT,
            MESSAGE_DATE_EMISSION,
            MESSAGE_DATE_RECEPTION,
            MESSAGE_LU,
            MESSAGE_LEVEL
    };
    // @formatter:on
}