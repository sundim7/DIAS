package gaurav.sundim7.dias;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class ServiceUpdateDatabase extends IntentService {

	public static final String ACTION_MyUpdate = "com.sundim7.mdias.UPDATE";
	public static final String EXTRA_KEY_NEW_NOTICES = "countNewNotices";
	public static final String PROPERTY_LASTID = "lastid";
	private SqlHandler db;
	public static String FLAG_DOWNLOAD_FAILED = "downloadFailed";
	public static String FLAG_SERVER_DOWN = "serverDown";

	public static final String namespace = "http://tempuri.org/";
	public static final String method = "getAllNotices";
	public static final String action = namespace + method;
	public static final String url = "http://192.168.2.101:7214/WebService1.asmx";
	public Notice[] nn = null;
	private Notice[] downloadedNotices = null;
	private Intent intentUpdate;

	public ServiceUpdateDatabase() {
		super("upddb");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		db = new SqlHandler(this);
		db.open();
		intentUpdate = new Intent();
		if (isOnline()) {
			int lastid = db.getLastId(SqlHandler.TABLE_NOTICES);
			downloadedNotices = fetchNotices(lastid);
			if (downloadedNotices != null) {
				updateDb(downloadedNotices);
				sendUpdateBroadcast(downloadedNotices.length);
			}
		} else {
			intentUpdate.putExtra(FLAG_DOWNLOAD_FAILED, true);
			sendUpdateBroadcast(0);
		}
		db.close();
	}

	private Notice[] fetchNotices(int lastid) {
		SoapObject req = new SoapObject(namespace, method);
		//req.addProperty("startid", String.valueOf(lastid));
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(req);
		envelope.dotNet = true;
		envelope.encodingStyle = "utf-8";
		// envelope.addMapping(namespace, "notices", new Notice().getClass());
		HttpTransportSE transport = new HttpTransportSE(url);
		transport.debug = true;
		try {
			transport.call(action, envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			SoapObject obj0 = (SoapObject) response.getProperty(1);
			SoapObject obj1 = (SoapObject) obj0.getProperty(0);
			nn = new Notice[obj1.getPropertyCount()];

			for (int i = 0; i < obj1.getPropertyCount(); i++) {
				SoapObject obj2 = (SoapObject) obj1.getProperty(i);
				Notice n = new Notice();
				n.setId(obj2.getProperty(0).toString());
				n.setSubject(obj2.getProperty(1).toString());
				n.setDate(obj2.getProperty(2).toString());
				n.setMessage(obj2.getProperty(3).toString());
				nn[i] = n;
			}
			Log.d("iguyg", transport.responseDump);
		} catch (SocketTimeoutException e) {
			Log.d("", "Cannot connect to server");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.d("", "Received 0 notices.");
			e.printStackTrace();
		}
		return nn;
	}

	void updateDb(Notice[] x) {
		db.open();
		for (int i = 0; i < x.length; i++) {
			Notice temp = x[i];
			try {
				db.saveNoticeRecord(temp);
			} catch (Exception e) {
				Log.d("ERROR","Notices are already up to date.");
			}
		}
		db.close();
	}

	private boolean isOnline() {
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
		if (netInfo == null) {
			return false;
		} else {
			return true;
		}
	}

	void sendUpdateBroadcast(int newCount) {
		intentUpdate.setAction(ACTION_MyUpdate);
		intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
		intentUpdate.putExtra(EXTRA_KEY_NEW_NOTICES, newCount);
		sendBroadcast(intentUpdate);
	}

}
