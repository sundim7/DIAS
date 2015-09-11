package gaurav.sundim7.dias;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class Notice implements KvmSerializable {

	public static final String KEY_ID = "_id";
	public static final String KEY_SUBJECT = "subject";
	public static final String KEY_DATE = "date";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_ATTACHMENT = "attachment";

	public static final String[] KEYS_NOTICE = new String[] { KEY_ID, KEY_SUBJECT, KEY_DATE, KEY_MESSAGE,
			KEY_ATTACHMENT };

	String id;
	String subject;
	String date;
	String message;
	String attachment;

	public Notice() {
	}

	public Notice(String id, String subject, String date, String message, String attachment) {
		this.id = id;
		this.subject = subject;
		this.date = date;
		this.message = message;
		this.attachment = attachment;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the filename
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Object getProperty(int arg0) {

		switch (arg0) {
		case 0:
			return id;
		case 1:
			return subject;
		case 2:
			return date;
		case 3:
			return message;
		case 4:
			return attachment;
		}

		return null;
	}

	public int getPropertyCount() {
		return 5;
	}

	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "_id";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "subject";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "date";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "message";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "attachment";
			break;
		default:
			break;
		}
	}

	@Override
	public String getInnerText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInnerText(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			id = value.toString();
			break;
		case 1:
			subject = value.toString();
			break;
		case 2:
			date = value.toString();
			break;
		case 3:
			message = value.toString();
			break;
		case 4:
			attachment = value.toString();
			break;
		default:
			break;
		}
	}
}
