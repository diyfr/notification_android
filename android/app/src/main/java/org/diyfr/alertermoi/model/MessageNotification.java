package org.diyfr.alertermoi.model;

import java.util.Date;

public class MessageNotification
{
	private long _id=0;
	private Date dateReception;
	private Date dateEmission;
	private String subtitle;
	private String title;
	private String content;
	private String type;
	private String icon;
	private boolean isReaded = false;
	private int level = 0;

	public MessageNotification()
	{
	}

	public MessageNotification(Date emisLe, Date recuLe, int level, String type, String message, boolean isReaded)
	{
		this.dateReception = recuLe;
		this.dateEmission = emisLe;
		this.type = type;
		this.level = level;
		this.content = message;
		this.isReaded = isReaded;
	}

	public Date getDateReception()
	{
		return dateReception;
	}

	public Date getDateEmission()
	{
		return dateEmission;
	}

	public String getContent()
	{
		return content;
	}

	public String getType()
	{
		return type;
	}

	public boolean isReaded()
	{
		return isReaded;
	}

	public int getLevel()
	{
		return level;
	}
	public void setDateReception(Date dateReception)
	{
		this.dateReception = dateReception;
	}

	public void setDateEmission(Date dateEmission)
	{
		this.dateEmission = dateEmission;
	}

	public void setContent(String contenu)
	{
		this.content = contenu;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setReaded(boolean is_readed)
	{
		this.isReaded = is_readed;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public long get_id()
	{
		return _id;
	}

	public void set_id(long _id)
	{
		this._id = _id;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

		
}
