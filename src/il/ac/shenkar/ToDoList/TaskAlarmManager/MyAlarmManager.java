/*
 * MyAlarmManager.java
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


package il.ac.shenkar.ToDoList.TaskAlarmManager;

import il.ac.shenkar.ToDoList.ITaskInterfaces.ITaskAlarm;
import il.ac.shenkar.ToDoList.TaskDAO.TaskList;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import il.ac.shenkar.ToDoList.TaskUtilities.MyConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;


public class MyAlarmManager implements ITaskAlarm
{
	private Context context;
	private	SimpleDateFormat  simpleDateFormat =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
	private long timeDiff;
	private PendingIntent pendingIntent;
	private AlarmManager alarmManager;
	private Intent intentSender;
		
	public MyAlarmManager(Context context)
	{
		this.context = context;
		intentSender = new Intent(MyConstants.ALARM_RECEIVER);
	}
	
	/**
	 * Set the date and time of the task on the date and time pickers
	 * invoked only when a task with alarm is on edit.
	 *
	 */
	public void setTaskDateOnPickers(TaskObject taskToSet, DatePicker datepicker, TimePicker timePicker, Switch notifySwitch)
	{
		String notifictionBeforeEdit = taskToSet.getTaskNotificationDate();	

		Date dateObj = null;
		
		try
		{
			dateObj = simpleDateFormat.parse(notifictionBeforeEdit);
		} 
		
		catch (ParseException e)
		{
			Log.e(getClass().getSimpleName(), "Parse failed", e.fillInStackTrace());
		}
	
		
		 Calendar myCal = Calendar.getInstance();
		 myCal.setTime(dateObj);
		 int month = myCal.get(Calendar.MONTH);
		 int day = myCal.get(Calendar.DATE);
		 int year = myCal.get(Calendar.YEAR);
		 int hour = myCal.get(Calendar.HOUR_OF_DAY);
		 int min = myCal.get(Calendar.MINUTE);
		 
	   	 notifySwitch.setChecked(true);
		 timePicker.setCurrentHour(hour);
		 timePicker.setCurrentMinute(min);
		 datepicker.init(year,month,day,null);
	}
		
	
	
	public int getHourOfDay()
	{
		Date dateObj = null;
		
		try
		{
			dateObj = simpleDateFormat.parse(TaskList.getCurrentSystemTime());
		} 
		
		catch (ParseException e)
		{
			Log.e(getClass().getSimpleName(), "Parse failed", e.fillInStackTrace());
		}
	
		 Calendar myCal = Calendar.getInstance();
		 myCal.setTime(dateObj);
		 
		 return myCal.get(Calendar.HOUR_OF_DAY);	
	}
	
	/**
	 * get the time from time and date pickers, when user want to
	 * set alarm for his task
	 * @return the date as a String that include the date and time like: 15.12.13 15:15
	 */
	public String getTimeFromPickers(DatePicker datepicker, TimePicker timePicker)
	{
			
		//Defining stringbuilder for better peformence of building strings
		StringBuilder timeStrBuilder  = new StringBuilder(); 	
					
		 //cast to string, avoid the deletion of leading zero, because of the int parse
	  	 String day = Integer.toString(datepicker.getDayOfMonth());
	  	 String month = Integer.toString(datepicker.getMonth() + 1);
	  	 int year = datepicker.getYear();
	  		   	
	  		 
	  	 //cast to string, avoid the deletion of leading zero, because of the int parse
	  	 String hour = Integer.toString(timePicker.getCurrentHour());
	  	 String min = Integer.toString(timePicker.getCurrentMinute());
	  		 
	  		 
	   	 //build string of the notify date
	   	 timeStrBuilder.append(day).append(".").append(month).append(".").append(year).append(" ")
	   	 .append(hour).append(":").append(min);
	   	 
	   	 return timeStrBuilder.toString();
	}
	

	/**
	 * 
	 * set the alarm for the task, after checking validation of the time
	 * checking if has repeating signal for the the alarm
	 */
	public void setAlarm(TaskObject chosenTask, long differenceInMillis, long repeatAlarmInterval)
	{
				
		//sets the massages to be sent to the broadcast receiver, id of task, task name
		intentSender.putExtra("taskToSetAlarm", chosenTask);
		
		pendingIntent = PendingIntent.getBroadcast(context, 
				(int) chosenTask.getTaskId(), intentSender, 0);
	
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		

		if(chosenTask.getTaskInterval() != 0)
		{
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					java.lang.System.currentTimeMillis() + differenceInMillis, repeatAlarmInterval, pendingIntent);
			Log.i(getClass().getSimpleName(), "setRepeating - alarm was set by the user for id " + chosenTask.getTaskId());
		}				
		
		
	
		else
		{
			alarmManager.set(AlarmManager.RTC_WAKEUP,
				java.lang.System.currentTimeMillis()
						+ differenceInMillis, pendingIntent);
			Log.i(getClass().getSimpleName(), "set- alarm was set by the user for id " +chosenTask.getTaskId());
		}
		
		
	}

	/**
	 * cancel the alarm of the task 
	 *
	 */
	public void CancelAlarm(TaskObject chosenTask)
	{
			Log.i(getClass().getSimpleName(), "Remove alarm from editing function, task with id " + chosenTask.getTaskId());								
			PendingIntent.getBroadcast(context, (int)  chosenTask.getTaskId(), intentSender, 0).cancel();
			
	}

	
	
	public void canceAllAlarms()
	{
		    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		    pendingIntent = PendingIntent.getBroadcast(context, 0, intentSender, 0);

		    // Cancel alarms
		    try
		    {
		        alarmManager.cancel(pendingIntent);
		    } 
		    
		    catch (Exception e) 
		    {
		        Log.e(getClass().getSimpleName(), "AlarmManager update was not canceled. " + e.toString());
		    }
	}

	/**
	 * check if the time is valid with the current time zone
	 * @return -1 for invalid time, other positive number for valid time, differance
	 */
	public long compareBetweenDates(String timeAndDateFromPickers)
	{
  		 try 
  		 {	 
  			 //parsing the date string into date objects
            Date currentTime = simpleDateFormat.parse(TaskList.getCurrentSystemTime());
            Date pickedTime = simpleDateFormat.parse(timeAndDateFromPickers);
       
             //check the time, before and after
            if(currentTime.before(pickedTime))
            { 
           	   //while the time is correct, we check the difference between the dates, in millis
           	   this.timeDiff = Math.abs(currentTime.getTime() - pickedTime.getTime());
           	   //the difference is saved.
           	  return this.timeDiff;           
            }
            
            else
            {
                Log.i(getClass().getSimpleName(), "getCurrentDateOnPickers()");
                return -1;
            }           		

        } 
  		 
  		 catch (ParseException e)
        {
  			Log.e(getClass().getSimpleName(),e.getMessage());
        }
		return -1;

	}

}
