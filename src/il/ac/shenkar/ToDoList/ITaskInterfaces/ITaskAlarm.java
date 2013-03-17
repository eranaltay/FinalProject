/*
 * ITaskAlarm.java
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

package il.ac.shenkar.ToDoList.ITaskInterfaces;

import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;

public interface ITaskAlarm 
{
	public abstract long compareBetweenDates(String timeAndDate);
		
	public abstract void setTaskDateOnPickers(TaskObject chosenTask, DatePicker datepicker, TimePicker timePicker, Switch notifySwitch);
	
	public abstract String getTimeFromPickers(DatePicker datepicker, TimePicker timePicker);
	
	public abstract void setAlarm(TaskObject chosenTask, long differenceInMillis, long repeatAlarmInterval);
	
	public abstract void CancelAlarm(TaskObject chosenTask);

}
