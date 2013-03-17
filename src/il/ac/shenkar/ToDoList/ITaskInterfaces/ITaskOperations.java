/*
 * ITaskOperations.java
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 */

package il.ac.shenkar.ToDoList.ITaskInterfaces;

import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;

public interface ITaskOperations 
{
	public abstract void changeTaskDoneState(TaskObject chosenTask, int state);
	
	public abstract void changeTaskImportanceState(TaskObject chosenTask, int state);
	
	public abstract void removeTask(int position);
	
	public abstract void shareTask(TaskObject chosenTask);
	
	public abstract void shareList();
	
	public abstract void deleteDoneTasks();
	
	public abstract void toastNotification(String msg);
	
	public abstract void clearList();
}

