/*
 * TaskGPSManager.java
 *
 * Copyright 2013 Eran Altay
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 1.13 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package il.ac.shenkar.ToDoList.TaskLocationManager;

import il.ac.shenkar.ToDoList.TaskDAO.TaskList;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import il.ac.shenkar.ToDoList.TaskUtilities.MyConstants;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class TaskGPSManager 
{
	
	private static final int gpsMinTime = 500;
	private static final int gpsMinDistance = 0;
	private LocationManager locationManager = null;
	private LocationListener locationListener = null;
	private GPSCallback gpsCallback = null;
	private Context context;
	
	public TaskGPSManager(Context context)
	{
		this.context = context;
	}
	

	public TaskGPSManager()
	{
		locationListener = new LocationListener() 
		{
			public void onProviderDisabled(final String provider)
			{
			}

			public void onProviderEnabled(final String provider) 
			{
			}

			public void onStatusChanged(final String provider,
					final int status, final Bundle extras)
			{
			}

			public void onLocationChanged(final Location location) 
			{
				if (location != null && gpsCallback != null)
				{
					gpsCallback.onGPSUpdate(location);
				}
			}
		};
	}

	public void startListening(final Activity activity) 
	{
		if (locationManager == null) 
		{
			locationManager = (LocationManager) activity
					.getSystemService(Context.LOCATION_SERVICE);
		}

		final Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		final String bestProvider = locationManager.getBestProvider(criteria,	true);
	

		if (bestProvider != null && bestProvider.length() > 0) 
		{
			locationManager.requestLocationUpdates(bestProvider,
					TaskGPSManager.gpsMinTime, TaskGPSManager.gpsMinDistance,
					locationListener);
		}
		
		else 
		{
			final List<String> providers = locationManager.getProviders(true);

			for (final String provider : providers) 
			{
				locationManager.requestLocationUpdates(provider,
						TaskGPSManager.gpsMinTime, TaskGPSManager.gpsMinDistance,
						locationListener);
			}
		}
	}
	
	public void setProximityAlert(int taskToSetProximityIndex, double latitude, double longitude)
	{
		Intent intentSender = new Intent(MyConstants.LOCATION_RECEIVER);
		//sets the massages to be sent to the broadcast receiver, id of task, task name
		TaskObject taskToSetProximity = TaskList.getTasksList().get(taskToSetProximityIndex);
		intentSender.putExtra("taskToSetProximity", taskToSetProximity);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 
				(int) taskToSetProximity.getTaskId(), intentSender, 0);
	
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.addProximityAlert(latitude, longitude, MyConstants.RADIUS, -1, pendingIntent);
		
	
		Log.i(getClass().getSimpleName(), "Proximity Alert Activated for task at:"  + 
				taskToSetProximityIndex + " " +  latitude + "  " +  longitude +  " For Task: " + taskToSetProximity.getTaskId());
		
	}
	
	
	public void cancelProximityAlert(TaskObject taskToCancelProximity)
	{
		Intent intentSender = new Intent(MyConstants.LOCATION_RECEIVER);
		//sets the massages to be sent to the broadcast receiver, id of task, task name
		intentSender.putExtra("taskPosition", TaskList.getTasksList().indexOf(taskToCancelProximity));
		
		PendingIntent.getBroadcast(context, 
				(int) taskToCancelProximity.getTaskId(), intentSender, 0).cancel();
	
		Log.i(getClass().getSimpleName(), "Proximity Alert Cancelled for task :"  +taskToCancelProximity.getTaskId()); 
				
	}
	
	public void cancelAllProximityAlerts()
	{
		Intent updateServiceIntent = new Intent(MyConstants.LOCATION_RECEIVER);
	    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	    PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(context, 0, updateServiceIntent, 0);

	    // Cancel Alerts
	    try
	    {
	    	locationManager.removeUpdates(pendingUpdateIntent);
	    } 
	    
	    catch (Exception e) 
	    {
	        Log.e(getClass().getSimpleName(), "AlarmManager update was not canceled. " + e.toString());
	    }
		
		
	}
	
		
	public void stopListening() 
	{
		try 
		{
			if (locationManager != null && locationListener != null)
			{
				locationManager.removeUpdates(locationListener);
			}

			locationManager = null;
		} catch (final Exception ex) {

		}
	}

	public void setGPSCallback(final GPSCallback gpsCallback)
	{
		this.gpsCallback = gpsCallback;
	}

	public GPSCallback getGPSCallback()
	{
		return gpsCallback;
	}
}
