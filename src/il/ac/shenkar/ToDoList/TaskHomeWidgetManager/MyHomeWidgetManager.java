package il.ac.shenkar.ToDoList.TaskHomeWidgetManager;

import il.ac.shenkar.ToDoList.R;

import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyHomeWidgetManager extends AppWidgetProvider 
{
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds)
	{		
		Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 1000);
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	private class MyTime extends TimerTask
	{
		
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		
		public MyTime(Context context, AppWidgetManager appWidgetManager) 
		{
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			thisWidget = new ComponentName(context, MyHomeWidgetManager.class);
		}
		
		@Override
		public void run() 
		{
		    remoteViews.setTextViewText(R.id.widget_textview,"Soon...");
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action))
		{
			
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
			{
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} 
		
		else 
		{
			super.onReceive(context, intent);
		}
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		Toast.makeText(context, "onDelete", Toast.LENGTH_SHORT).show();
		super.onDeleted(context, appWidgetIds);
	}
		
}
