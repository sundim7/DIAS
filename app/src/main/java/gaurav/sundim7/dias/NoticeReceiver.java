package gaurav.sundim7.dias;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gaurav on 9/8/2015.
 */
public class NoticeReceiver extends ParsePushBroadcastReceiver{
SqlHandler db;
    Notice n;
    Intent intentUpdate;
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        String data = intent.getExtras().getString("com.parse.Data");
        Log.d("data",data);
        try {
            JSONObject json=new JSONObject(data);
            n=new Notice();
            n.setId(json.getString("id"));
            n.setSubject(json.getString("subject"));
            n.setDate(json.getString("date"));
            n.setMessage(json.getString("message"));
            n.setAttachment(json.getString("attachment"));
            db=new SqlHandler(context);
            db.open();
           Boolean successSave= db.saveNoticeRecord(n);
            if(successSave)
                sendUpdateBroadcast(1,context);
            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendUpdateBroadcast(int newCount,Context context) {
        intentUpdate=new Intent();
        intentUpdate.setAction(ServiceUpdateDatabase.ACTION_MyUpdate);
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        intentUpdate.putExtra(ServiceUpdateDatabase.EXTRA_KEY_NEW_NOTICES, newCount);
        context.sendBroadcast(intentUpdate);
    }
}
