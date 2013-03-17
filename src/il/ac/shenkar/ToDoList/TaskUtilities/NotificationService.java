package il.ac.shenkar.ToDoList.TaskUtilities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class NotificationService extends Service
{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(getClass().getSimpleName(), "service");

	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent, startId);
	}

	
	
}
