package org.diyfr.alertermoi.activity.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher
{
	private EditText currentEdit;

	public CustomTextWatcher(EditText et)
	{
		this.currentEdit = et;
	}

	@Override
	public void afterTextChanged(Editable s)
	{

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		this.currentEdit.setError(null);
	}

}