package il.ac.shenkar.ToDoList.TaskUtilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TaskReceiverRecovery extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent serviceIntent = new Intent();
		serviceIntent.setAction(MyConstants.NOTIFICATION_SERVICE);
		context.startService(serviceIntent);		
	}
}
