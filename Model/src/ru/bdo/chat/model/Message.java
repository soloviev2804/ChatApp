package ru.bdo.chat.model;

import java.util.Date;
import java.util.UUID;

/**
 * Chat message
 * @author solovyev.vladimir
 * @since 10.09.2015
 */
public class Message {
	private String id = UUID.randomUUID().toString();
	private String text;
	private String author;
	private Date created;

	public Message(String author, String text) {
		this.author = author;
		this.text = text;
		this.created = new Date();
	}

	public String getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Message message = (Message) o;

		return !(id != null ? !id.equals(message.id) : message.id != null);

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
