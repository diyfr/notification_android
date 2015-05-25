package org.diyfr.alertermoi.activity.view;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.diyfr.alertermoi.model.MessageNotification;
import org.diyfr.alertermoi.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageView extends RelativeLayout {
	private Context context;
	private ImageView icon;
	private TextView level, content, title, subtitle, receipt;
	private static SimpleDateFormat spf = new SimpleDateFormat("dd/MM HH:mm:ss", Locale.getDefault());
	private static final float opacity = 0.7f;

	public MessageView(Context context) {
		super(context);
		this.context = context;
		init(this.context);
	}

	public MessageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(this.context);
	}

	private void init(Context context) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_message, this);
		icon = (ImageView) findViewById(R.id.img_icon);
		level = (TextView) findViewById(R.id.tv_level);
		title = (TextView) findViewById(R.id.tv_title);
		subtitle = (TextView) findViewById(R.id.tv_subtitle);
		content = (TextView) findViewById(R.id.tv_content);
		receipt = (TextView) findViewById(R.id.tv_receiptdate);
	}

	public void setMessage(MessageNotification message) {
		Bitmap bitmap = null;
		this.icon.setImageBitmap(null);
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.googlep_ico);
		if (message.getIcon() != null) {
			try {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				byte[] decodedString = Base64.decode(message.getIcon(), Base64.DEFAULT);
				bitmap =Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), width, height, false);
			} catch (IllegalArgumentException i) {
			}
		}
		this.icon.setImageBitmap(bitmap);
		this.title.setText(message.getTitle());
		this.subtitle.setText(message.getSubtitle());
		this.content.setText(Html.fromHtml(message.getContent()));
		switch (message.getLevel()) {
		case 1:
			this.level.setBackgroundColor(getResources().getColor(R.color.red));
			break;
		case 2:
			this.level.setBackgroundColor(getResources().getColor(R.color.orange));
			break;
		case 3:
			this.level.setBackgroundColor(getResources().getColor(R.color.green));
			break;
		default:
			this.level.setBackgroundColor(getResources().getColor(R.color.blue));
			break;
		}
		this.receipt.setText("Emis le " + spf.format(message.getDateEmission()) + " - reçu le " + spf.format(message.getDateReception()));
		setReadingStatus(message.isReaded());
	}

	public void setReadingStatus(boolean readed) {
		int resText = (readed) ? getResources().getColor(R.color.textreaded) : getResources().getColor(R.color.textunreaded);
		this.title.setTextColor(resText);
		this.subtitle.setTextColor(resText);
		this.content.setTextColor(resText);
		float opacityResult = 1.0f;
		if (readed) {
			opacityResult = opacity;
			ColorMatrix matrix = new ColorMatrix();
			matrix.setSaturation(0); // 0 means grayscale
			ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
			this.icon.setColorFilter(cf);
			this.level.setBackgroundColor(getResources().getColor(R.color.textreaded));
		} else {
			this.icon.setColorFilter(null);
		}
		this.icon.setAlpha(opacityResult);
	}
}
