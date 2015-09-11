package gaurav.sundim7.dias;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class FirstRunActivity extends Activity {
    private SqlHandler db;

    public static final String namespace = "http://tempuri.org/";
    public static final String method = "getAllNotices";
    public static final String action = namespace + method;
    public static final String url = "http://192.168.2.101:7214/WebService1.asmx";
    public Notice[] nn = null;
    private Notice[] downloadedNotices = null;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

            new downloadAllNotices().execute();


    }

    private class downloadAllNotices extends AsyncTask<Void,Void,Notice[]>{
        Notice[] tempNotices;
        @Override
        protected Notice[] doInBackground(Void... params) {
            SoapObject req = new SoapObject(namespace, method);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(req);
            envelope.dotNet = true;
            envelope.encodingStyle = "utf-8";
            HttpTransportSE transport = new HttpTransportSE(url);
            transport.debug = true;
            try {
                transport.call(action, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject obj0 = (SoapObject) response.getProperty(1);
                SoapObject obj1 = (SoapObject) obj0.getProperty(0);
                tempNotices = new Notice[obj1.getPropertyCount()];

                for (int i = 0; i < obj1.getPropertyCount(); i++) {
                    SoapObject obj2 = (SoapObject) obj1.getProperty(i);
                    Notice n = new Notice();
                    n.setId(obj2.getProperty(0).toString());
                    n.setSubject(obj2.getProperty(1).toString());
                    n.setDate(obj2.getProperty(2).toString());
                    n.setMessage(obj2.getProperty(3).toString());
                    tempNotices[i] = n;
                }
                Log.d("DEBUG: ", transport.responseDump);
            }catch(ConnectException e){
                Log.d("DEBUG: ","Server is probably not reachable or off.");
                e.printStackTrace();
            }
            catch (SocketTimeoutException e) {
                Log.d("DEBUG: ", "Connection to server timed out.");
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
            return tempNotices;

        }

        @Override
        protected void onPostExecute(Notice[] notices) {
            super.onPostExecute(notices);
            if (!(notices == null))
                db = new SqlHandler(FirstRunActivity.this);
            db.open();
            for (int i = 0; i < notices.length; i++) {
                Notice temp = notices[i];
                try {
                    db.saveNoticeRecord(temp);
                } catch (Exception e) {
                    Log.d("ERROR", "Notices are already up to date.");
                }
            }
            db.close();

            prefs=getSharedPreferences("gaurav.sundim7.dias",MODE_PRIVATE);
            prefs.edit().putBoolean("firstRun",false).apply();
            Intent in =new Intent(FirstRunActivity.this,MainActivity.class);
            startActivity(in);
            finish();
        }
        }
    }

