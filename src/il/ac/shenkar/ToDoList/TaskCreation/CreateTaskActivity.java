/*
 * CreateTaskActivity.java
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


package il.ac.shenkar.ToDoList.TaskCreation;

import il.ac.shenkar.ToDoList.R;
import il.ac.shenkar.ToDoList.TaskAlarmManager.MyAlarmManager;
import il.ac.shenkar.ToDoList.TaskDAO.TaskDataBaseSQL;
import il.ac.shenkar.ToDoList.TaskDAO.TaskList;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import il.ac.shenkar.ToDoList.TaskLocationManager.GPSCallback;
import il.ac.shenkar.ToDoList.TaskLocationManager.TaskGPSManager;
import il.ac.shenkar.ToDoList.TaskUtilities.MyConstants;

import java.util.List;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;

/**
 * CreateTaskActivity, create the task
 * @author Eran Altay
 *
 */
public class CreateTaskActivity  extends SherlockActivity implements OnCheckedChangeListener, GPSCallback, OnOpenListener, OnCloseListener, OnClickListener  
{
	private 		Switch 			  	   notifySwitch;
	private 		Button				   clearTitleButton;
	private 		Button				   clearDescriptionButton;
	private 		Spinner			   	   repeatSpinner;
	private 		EditText 			   titleInputext;
	private 		EditText			   descriptionInputText;
	private 		TimePicker			   timepicker;
	private 		DatePicker 			   datepicker;
	private 		TaskDataBaseSQL 	   myDataBase = new TaskDataBaseSQL(this);
	private 		TaskObject 			   taskBeforeEdit;
	private			ArrayAdapter<String>   repeatAdapter;
	private 		InputMethodManager 	   inputMethodManager;
	private 		MyAlarmManager         myalarmmanager;
	private			TaskObject 			   taskDummy;
	private			long			       repeatAlarmInterval;
	private   		int					   intervalIndexFromSpinner;
	private			long 				   differenceInMillis;
	private 		String				   title;
	private			String 				   description;
	private			String 				   location;
	private			long 				   taskID;
	private			int 				   isDone;
	private			int 				   important;
	private 		int 				   proximity;
	private 		String 				   TimeFromPickers;
	private 		int 					positionOftaskToEdit;
	private 		double 					latitude;
	private 		double 					longitude;
	private 		Geocoder 				coder = null;
	private 		TaskGPSManager			myGPSManager = null;
	private 		List<Address> 			addresses = null;
    private 		SlidingMenu				drawer;
    private   		ActionBar				actionBar;

	public long getRepeatAlarmInterval()
	{
		return repeatAlarmInterval;
	}


	public long getDifferenceInMillis() {
		return differenceInMillis;
	}


	public void setDifferenceInMillis(long differenceInMillis) {
		this.differenceInMillis = differenceInMillis;
	}


	public void setRepeatAlarmInterval(long repeatAlarmInterval)
	{
		this.repeatAlarmInterval = repeatAlarmInterval;
	}


	public int getIntervalIndexFromSpinner()
	{
		return intervalIndexFromSpinner;
	}


	public void setIntervalIndexFromSpinner(int intervalIndexFromSpinner)
	{
		this.intervalIndexFromSpinner = intervalIndexFromSpinner;
	}

	
	public TaskObject getTaskDummy()
	{
		return taskDummy;
	}


	public void setTaskDummy(TaskObject taskDummy) 
	{
		this.taskDummy = taskDummy;
	}


	public String getLocation() 
	{
		return location;
	}


	public void setLocation(String location) 
	{
		this.location = location;
	}
	

	public String getTaskTitle() 
	{
		return title;
	}


	public void setTitle(String title)
	{
		this.title = title;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description) 
	{
		this.description = description;
	}


	public long getId() 
	{
		return taskID;
	}


	public void setId(long taskID)
	{
		this.taskID = taskID;
	}


	public double getLocationLatitude()
	{
		return latitude;
	}


	public void setLocationLatitude(double latitude)
	{
		this.latitude = latitude;
	}


	public double getLocationLongitude()
	{
		return longitude;
	}


	public void setLocationLongitude(double longitude)
	{
		this.longitude = longitude;
	}


	public int getIsDone() 
	{
		return isDone;
	}


	public void setIsDone(int isDone) 
	{
		this.isDone = isDone;
	}


	public int getImportant() 
	{
		return important;
	}


	public void setImportant(int important)
	{
		this.important = important;
	}
	
	
	public int getProximity()
	{
		return proximity;
	}


	public void setProximity(int proximity)
	{
		this.proximity = proximity;
	}
	

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.create_layout);
        //ActionBar gets initiated
        actionBar = getSupportActionBar();
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Compose");
        myDataBase = new TaskDataBaseSQL(this); 	
        
        drawer = new SlidingMenu(this);
        drawer.setMode(SlidingMenu.LEFT);
        drawer.setShadowDrawable(R.drawable.shadow);
        drawer.setShadowWidthRes(R.dimen.shadow_width);
        drawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        drawer.setFadeDegree(0.35f);
        drawer.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        drawer.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        drawer.offsetTopAndBottom(25);
        drawer.setMenu(R.layout.alarmslidingmenu);
        drawer.setSlidingEnabled(false);
        positionOftaskToEdit = -1;
        initilizeViewVariables();
        coder = new Geocoder(this);
        myGPSManager = new TaskGPSManager(this);
        myalarmmanager = new MyAlarmManager(this);
    	timepicker.setCurrentHour(myalarmmanager.getHourOfDay());

		
		if(getIntent().hasExtra("ob"))
		{
			positionOftaskToEdit = getIntent().getIntExtra("ob", 0);
			taskBeforeEdit = TaskList.getTasksList().get(positionOftaskToEdit);
			initilizeViewForExistingTask();
			actionBar.setTitle("Edit");
			Log.d(getClass().getSimpleName(), "Editing A Task" + "id: " +  taskBeforeEdit.getTaskId());
		}
		
		else
		{
			initilizeForNewTask(); 
			Log.d(getClass().getSimpleName(), "Creating New Task");
		}
	       	
    }
	
	  
	  private void  initilizeViewVariables()
	  {
		clearDescriptionButton = (Button) findViewById(R.id.descriptionCLRbtn);
		clearTitleButton = (Button) findViewById(R.id.titleCLRbtn);
	    notifySwitch = (Switch) findViewById(R.id.switchNotificationLO);
	  	repeatSpinner = (Spinner) findViewById(R.id.spinnerRepeatingLO);
	   	timepicker = (TimePicker) findViewById(R.id.timePickerLO);
	   	datepicker = (DatePicker) findViewById(R.id.datePickerLO);
		timepicker.setIs24HourView(true);
		titleInputext = (EditText) findViewById(R.id.TitleEditTextLO);
		titleInputext.requestFocus();
	  	descriptionInputText  = (EditText) findViewById(R.id.descriptionEditTextLO);
	    	
	  	repeatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources()
	  	            .getStringArray(R.array.repeatArray));
	  	repeatSpinner.setAdapter(repeatAdapter);
	    
	  	clearDescriptionButton.setOnClickListener(this);
	  	clearTitleButton.setOnClickListener(this);
	  	drawer.setOnOpenListener(this);
	  	drawer.setOnCloseListener(this);
	  	notifySwitch.setOnCheckedChangeListener(this);
	  	initilizeVariables();
	  	
	  	inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	      
	  	if (inputMethodManager != null) 
	      {
	          inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	      }
			  
	  }

	public void initilizeVariables() 
	{
	  	taskDummy = null;
	  	setIntervalIndexFromSpinner(0);
	  	TimeFromPickers = null;
	  	setTitle(null);
	  	setDescription(null);
	  	setId(0);
	  	setLocation(null);
	  	setProximity(MyConstants.NOPROXIMITY);
	  	setIsDone(MyConstants.NOTDONE);
	  	setImportant(MyConstants.NOTIMPORTANT);
	  	setLocationLatitude(0.0);
		setLocationLongitude(0.0);
		addresses = null;
	}
	  

	public void initilizeForNewTask() 
	{
	    repeatAlarmInterval = -1;
	    //disable the time picker on the oncreate
  	//set the timepicker to view 24 hours format
		timepicker.setEnabled(false);
		datepicker.setEnabled(false);
		repeatSpinner.setEnabled(false);
		notifySwitch.setChecked(false);
		//button, add the task
	}
	
	
		 
	private void initilizeViewForExistingTask()
	{
		Handler handle = new Handler();
		handle.post(new Runnable() 
	    {
			public void run() 
		    {
			  	 titleInputext.setText(taskBeforeEdit.getTaskTitle());
				 descriptionInputText.setText(taskBeforeEdit.getTaskDescription());
				 setLocation(taskBeforeEdit.getTaskLocation());
				 setProximity(taskBeforeEdit.getIfTaskHasProximity());
			
					
			  	 String notifictionBeforeEdit = taskBeforeEdit.getTaskNotificationDate();
				 if(notifictionBeforeEdit != null)
				 {
					myalarmmanager.setTaskDateOnPickers(taskBeforeEdit, datepicker, timepicker, notifySwitch);
					setIntervalIndexFromSpinner(taskBeforeEdit.getTaskInterval());
					repeatSpinner.setSelection(getIntervalIndexFromSpinner());
				 }
					 
				 else
				 {
					repeatSpinner.setEnabled(false); 
					timepicker.setEnabled(false);
					datepicker.setEnabled(false);
				 }
		    }
			
	   });
	}

	
  @Override
	public boolean onCreateOptionsMenu(Menu menu)
  {
		MenuInflater inflater = getSupportMenuInflater();
		
		if(positionOftaskToEdit != -1)
			inflater.inflate(R.menu.edittaskoptionsmenu, menu);
	
		
		else
			inflater.inflate(R.menu.createtaksoptionmenu, menu);

		return super.onCreateOptionsMenu(menu);
  }

  
  
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean isValid = false;
		switch(item.getItemId())
		{
			case R.id.savetaskMI:
					isValid = editATaskAction();					
			if(isValid == true)
				finish();		
			break;
		
			case R.id.addtaskMI:
				isValid = createATaskAction();
				if(isValid == true)
				{
					initilizeVariables();
					initilizeForNewTask(); 
					titleInputext.setText("");
					descriptionInputText.setText("");
					
					if(drawer.isMenuShowing())
						drawer.toggle();
					
					toastWithGravity("Task Added!");
				}
				EasyTracker.getTracker().sendEvent("UI-ActionBar", "Add Task-Append", "ActionBar", (long) 0); 
			break;
		
			case R.id.proximityMI:
				AlertDialog diaBox = locationEditText();
				diaBox.show();
				EasyTracker.getTracker().sendEvent("UI-ActionBar", "Add Proximity", "ActionBar", (long) 0); 
				break;
			
			case R.id.alarmMI:
				drawer.toggle(false);
				EasyTracker.getTracker().sendEvent("UI-ActionBar", "Toggle Alarm Menu", "ActionBar", (long) 0); 
			break;
				
				
			case R.id.backMI:
				EasyTracker.getTracker().sendEvent("UI-ActionBar", "back To Main", "ActionBar", (long) 0); 
				finish();
			break;	
				
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	public void onClick(View button)
	{
		switch(button.getId())
		{
			case R.id.titleCLRbtn:
				titleInputext.setText("");
			break;
				
			case R.id.descriptionCLRbtn:
				descriptionInputText.setText("");
			break;	
		}
	}

	

	@Override
	public boolean onKeyDown(int keycode, KeyEvent e)
	{
	    switch(keycode) 
	    {
	        case KeyEvent.KEYCODE_MENU:
	        	drawer.toggle(false);
	        return true;
	    }

	    return super.onKeyDown(keycode, e);
	}
	
	
	@Override
	protected void onStart() 
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

		
    @Override
    public void onStop()
    {
    	super.onStop();
    	EasyTracker.getInstance().activityStop(this); 
    }
	
	
 
	public boolean editATaskAction()
	{

		setId(taskBeforeEdit.getTaskId());
		setTitle(titleInputext.getText().toString().trim());
		setDescription(descriptionInputText.getText().toString().trim());
		setIsDone(taskBeforeEdit.getTaskDoneIndicator());
		setImportant(taskBeforeEdit.getTaskImportantIndicator());
				
					
		if(getTaskTitle() != null && getTaskTitle() .length() > 0)	
		{	
			if(notifySwitch.isChecked() == true)
			{
				  setIntervalFromSpinner();
				  TimeFromPickers = myalarmmanager.getTimeFromPickers(datepicker, timepicker);
				  updateTaskAction();
				  setDifferenceInMillis(myalarmmanager.compareBetweenDates(TimeFromPickers));
				  boolean isLater = getDifferenceInMillis() != MyConstants.INVALID_TIME;
			  
				  if (isLater == true)
				  {
		    	  	myalarmmanager.setAlarm(taskDummy, getDifferenceInMillis(), getRepeatAlarmInterval());
		    	  	Log.i(getClass().getSimpleName(), "notify for: " +getId() + " changed");  
		    	  	return true;
				  }
				  
				  else
				  {
					 toastWithGravity( "Invalid Time" + "\n" + "Turn Off Switch or Pick A different Time" );
					 TimeFromPickers = null;
		           	 drawer.showMenu(true);
		           	 return false;
				  }
		
			}
		
			else
			{	
				updateTaskAction();
					
				if(taskBeforeEdit.getTaskNotificationDate() != null)
				{
					myalarmmanager.CancelAlarm(taskDummy);
					return true;
				}
				
				//if the notification switch is off and there where no notification before the editing
				//of the task, user want a task without any alarm
				else
				{
					return true;
				}
				
			}
					
	    	}
		
		else
		{
			 toastWithGravity("First Fill in Task Title");
			 return false;
		}
		
	}

	public void updateTaskAction()
	{
		  taskDummy = new TaskObject(getId(), getTaskTitle() , getDescription(), getLocation(),
				  getProximity(),  TimeFromPickers,getIntervalIndexFromSpinner(),
				  getIsDone() , getImportant());	
		  myDataBase.open();
		  myDataBase.updateTask(taskDummy);
		  TaskList.getTasksList().set(positionOftaskToEdit, taskDummy);
		  
		  if(taskDummy.getIfTaskHasProximity()!=0)
		  {
			  Log.i(getClass().getSimpleName(), "Got Task To set proximity at position" +
					  positionOftaskToEdit + " with id: " +  taskDummy.getTaskId());			
			  myGPSManager.setProximityAlert(positionOftaskToEdit, getLocationLatitude(), getLocationLongitude());
		  }
				  
		  myDataBase.close();
	}

		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		{
			
			int buttonID = buttonView.getId();
			
				if(buttonID == R.id.switchNotificationLO)
				{
					if(isChecked)
					{
						repeatSpinner.setEnabled(true);
						timepicker.setEnabled(true);
						datepicker.setEnabled(true);
					}
					
					else
					{
						repeatSpinner.setEnabled(false);
						timepicker.setEnabled(false);
						datepicker.setEnabled(false);
					}
				}
			
		}


	/**
	 * set the right alarm interval from the spinner
	*/
	public void setIntervalFromSpinner()
	{
		
		Object selectedInterval = repeatSpinner.getItemAtPosition(repeatSpinner.getSelectedItemPosition());
		
     	     	if(selectedInterval.equals("No Repeating"))
		    	{
     	     		
		    	}
		    	
		    	else if(selectedInterval.equals("Daily"))
		        {
		           setRepeatAlarmInterval(AlarmManager.INTERVAL_DAY);
		        }
     	     	
		        else if (selectedInterval.equals("Every Hour"))
		        {
		        	setRepeatAlarmInterval(AlarmManager.INTERVAL_HOUR);
		        }
		        
		        else if (selectedInterval.equals("Every 30 Minutes"))
		        {
		        	setRepeatAlarmInterval(AlarmManager.INTERVAL_HALF_HOUR);
		        }
		        
		        else if (selectedInterval.equals("Every 15 Minutes"))
		        {
		        	setRepeatAlarmInterval( AlarmManager.INTERVAL_FIFTEEN_MINUTES);
		        }
     	    
     	     //set the index of the child of the spinner, for edit mode	
         	setIntervalIndexFromSpinner(repeatSpinner.getSelectedItemPosition());
	        Log.i(getClass().getSimpleName(), "interval: " + getRepeatAlarmInterval());
         	Log.i(getClass().getSimpleName(), "Interval index selected: " + getIntervalIndexFromSpinner());
      }


	/**
	 * Listener for Sliding Menu
	 */
	public void onClose() 
	{
		openSoftKeyboard();
	}

	/**
	 * Listener for Sliding Menu
	 */
	public void onOpen()
	{
		closeSoftKeyboard();
	}    
		

	public void openSoftKeyboard()
	{
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) 
        {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            Log.i( getClass().getSimpleName(), "Open Soft Keyboard"); 
        }
	}

	public void closeSoftKeyboard()
	{
		inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(titleInputext.getWindowToken(), 0);
		Log.i( getClass().getSimpleName(), "close Soft Keyboard"); 
	}

	/**
	 * Alert dialog for location input
	 */
	public AlertDialog locationEditText()
	{ 
		  final EditText locationInput = new EditText(this);
		  openSoftKeyboard();	
		  
		  //set hint
		  if(getLocation() == null)
		  {
			  locationInput.setHint("No Location...Yet");
		  }
	
		  //if there's any location on edit mode, then the location string will atached
		  else
			  locationInput.setText(getLocation());

		  
         final AlertDialog DescriptionTask = 

        	new AlertDialog.Builder(this) 
        	//set message, title, and icon
      
        	.setTitle("Search A Location For Your Task")
           	.setView(locationInput)
        	.setCancelable(true)
        	.setIcon(R.drawable.proximityindicator)
   
        	.setNeutralButton("Find It!", new DialogInterface.OnClickListener()
        	{
				
				public void onClick(DialogInterface dialog, int which)
				{
					String newLocation = locationInput.getText().toString().trim();
													
					if(newLocation != null && newLocation.length()>0)
					{
						setLocation(newLocation);  
						//invoke the Geocoder Class
	                    new GeocodeTask().execute(); 
	                    dialog.dismiss();
					}	
					
				}
			})
			

        	.create(); 
        	return DescriptionTask;
    }
	
	/**
	 * gets the input from the user for enter new tasks
	 * we check here if user set any alarm for his task or not
	 * in this method we update the list and the DB.
	 * 
	 */
	private boolean createATaskAction()
	{
		//gets the text from the edittext view,and
	    //validate the strings to be not empty, or with useless "white" chars
	    setTitle(titleInputext.getText().toString().trim());
	    setDescription(descriptionInputText.getText().toString().trim());
	    //validate the string to be not empty, or with useless "white" chars
		  		
		  if(getTaskTitle()  != null && getTaskTitle().length() > 0)
		  {			 		   
				  	  			
				   //check if the notification checkbox is checked
		  			if (notifySwitch.isChecked() == true)
		  			{
		  				TimeFromPickers = myalarmmanager.getTimeFromPickers(datepicker, timepicker);
		  				setDifferenceInMillis(myalarmmanager.compareBetweenDates(TimeFromPickers));
		  				boolean isLater = getDifferenceInMillis() != MyConstants.INVALID_TIME;
		  			
		  				//check if user choose an alarm for his task
		  				if(isLater == true)
		  				{
		  					setIntervalFromSpinner();
		  					addANewTask();
		  					myalarmmanager.setAlarm(taskDummy, getDifferenceInMillis(), getRepeatAlarmInterval());
					   		return true;
		  				}
		  				
		  				else
		  				{
		  					 TimeFromPickers = null;
							 toastWithGravity( "Invalid Time" + "\n" + "Turn Off Switch or Pick A different Time" );
		  	               	 drawer.showMenu(false);
		  				}
		  			} 
		  			
		  			
		  			//else, the task is without any Alarm
		  			else
		  			{
		  				addANewTask();
		  				return true;			
			   		}
			  			
			}
		  	
		  		//if edittext is empty, send toast to user
		  		else
		  		{
		  			toastWithGravity("First Fill The Text!");
		  		}
		return false;		  		
	}
	
	
	public void addANewTask()
	{
		myDataBase.open();  
			taskDummy = new TaskObject(getId(),getTaskTitle() ,getDescription(),
					getLocation(),getProximity(),TimeFromPickers,getIntervalIndexFromSpinner()
					,MyConstants.NOTDONE,MyConstants.NOTIMPORTANT);
			
			taskDummy.setTaskId((myDataBase.insertTask(taskDummy)));
			Log.i(getClass().getSimpleName(), "Task Created: id: " + taskDummy.getTaskId());
			TaskList.addToList(taskDummy);
			
			if(taskDummy.getIfTaskHasProximity() == MyConstants.HASPROXIMITY)
			{
				myGPSManager.setProximityAlert(MyConstants.INDEXOFFIRSTTASK, getLocationLatitude(), getLocationLongitude());
			}
			
			
	  	    Handler handle = new Handler();       	
        	handle.post(new Runnable() 
	      {
	       public void run() 
	       {
	    	 final MediaPlayer mediaplayer;
	    	  //delete from database and tasklist 
	    	  mediaplayer = MediaPlayer.create(CreateTaskActivity.this, R.raw.plustask); 
	    	  mediaplayer.setVolume(1, 1);
	    	  mediaplayer.start();
          }
	      });
		
				
			myDataBase.close();	
	}
	
	
	/**
	 * set the toast to be in the center of the screen
	 * 
	 * 
	 * @param msg
	 */
	private void toastWithGravity(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
  		toast.setGravity(Gravity.CENTER, 0, 0);
  		toast.show();
	}	


	private AlertDialog makeLocationChoiceDialogBox()
	{
		 final String[] addressStrings = new String[addresses.size()]; 
		 Address address = null;
		 openSoftKeyboard();
			     
	     for(int index = 0; index < addresses.size(); ++index)
	     {
	       address = addresses.get(index);
	       addressStrings[index] = (address.getAddressLine(0) + "\n"  + 
	       address.getLocality() + "\n" + address.getCountryName());
	     }     
			
		AlertDialog LocationChoice =  new AlertDialog.Builder(this).setTitle("Select Location")
			   
		    .setItems(addressStrings, new DialogInterface.OnClickListener()
		    {
		        public void onClick(DialogInterface dialog, int whichChoice) 
		        {            	
		      		setLocation(addressStrings[whichChoice].toString());
		        	Address chosenAddress = addresses.get(whichChoice);
		        	setLocationLatitude(chosenAddress.getLatitude());
		        	setLocationLongitude(chosenAddress.getLongitude());		
                    setProximity(1);
		          	dialog.dismiss();
		        }
		    })	    
		    
		    .create();   
	        return LocationChoice;

	}
	
 @Override
    protected Dialog onCreateDialog(int id)
    {
     int stringid = 0;
     
             switch(id)
             {
                     case MyConstants.DIALOG_ERROR_GPS:                
                    	 stringid = R.string.done_bt_txt;      				 
                     break;
                     
                     case MyConstants.DIALOG_ERROR_GEOCODE: 
                    	 stringid = R.string.locationhint;
                     break;
                     
                     case MyConstants.DIALOG_NO_INFO:
                    	 stringid = R.string.nolocation;
                     break;	 
             }
             
             Dialog dialog = null;
             
             if(stringid != 0 )
             {
                     dialog = createAlertDialog(CreateTaskActivity.this,getString(R.string.appName),getString(stringid));
             }
             else
             {
                     dialog = super.onCreateDialog(id);
             }
             
             return dialog;
     }

	public void onGPSUpdate(Location location)
	{    
	  	 setLocationLatitude(location.getLatitude());
	   	 setLocationLongitude(location.getLongitude());
	}
	
	
		
	private int FetchLocationInfo(final String locationName)
	 {
	     int result = MyConstants.ERROR_SUCCESS;
	     
	     try 
	     {
	         addresses = coder.getFromLocationName(locationName, MyConstants.NUMBEROFLOCATIONS); 
	         Log.i(getClass().getSimpleName(), "Get Addresses");
	     }
	     catch (Exception e)
	     {
	       Log.e(getClass().getSimpleName(),"Error on get addresses", e.fillInStackTrace());           
	     }
	     
	     return result;
	 }
	 
	 public ProgressDialog createProgressDialog(final Context context)
	 {
		  closeSoftKeyboard();
	      final ProgressDialog progressDialog = new ProgressDialog(context);
	      progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	      progressDialog.setCancelable(false);
	      progressDialog.setTitle("GPS");
	      progressDialog.setMessage("Getting Location Info...");
	      progressDialog.show();
	      return progressDialog;
	  }
	 
	 
	 public static Dialog createAlertDialog(final Context context, final String title, final String message)
	{
	     return new AlertDialog.Builder(context).setTitle(title).setMessage(message).create();
	}
	 
	 private int getErrorDialogIdFromCode(int code)
	 {
	     int dialogid = 0;
	     
	     switch(code)
	     {
	             case MyConstants.ERROR_LOCATION_TIMEOUT:   	 	 
	            	 dialogid = MyConstants.DIALOG_ERROR_GPS;    		
	             break;
	             
	             case MyConstants.ERROR_GEOCODER:          			 
	            	 dialogid = MyConstants.DIALOG_ERROR_GEOCODE;    	
	             break;
	             
	             case MyConstants.ERROR_SUCCESS:			  		 
	            	 dialogid = MyConstants.DIALOG_NO_INFO;       
	             break;   	  	            	 
	     }
	     
	     return dialogid; 
	 }
	 
	 
	private class GeocodeTask extends AsyncTask<Void, Integer, Integer>
	 {
	             private String locationName = "";
	             private ProgressDialog progress = null;
	             
	             @Override
	             protected Integer doInBackground(Void... task)
	             {
	                 Integer result = 0;
	                 result = FetchLocationInfo(locationName);       
	                 return result;
	             }
	
	             @Override
	             protected void onCancelled()
	             {
	                     super.onCancelled();
	             }
	
	             @Override
	             protected void onPostExecute(Integer result)
	             {
	                progress.dismiss();
	                
	                if(addresses != null && addresses.size() > 0)
	                {
	                   	 Log.i(getClass().getSimpleName(), "AlertDialog diaBox = makeLocationChoiceDialogBox() Activated");
						 AlertDialog dialogBox = makeLocationChoiceDialogBox();
						 dialogBox.show();
	                }
	                
	                else
	                {	
	                	 setLocation(null);
	                   	 Log.i(getClass().getSimpleName(),"showing error GPS dialog" + result);
 	                   	 showDialog(getErrorDialogIdFromCode(result));
	                }
	                     
	                super.onPostExecute(result);
	             }
	
	             @Override
	             protected void onPreExecute()
	             {       
	                 locationName = getLocation();
	                 progress = createProgressDialog(CreateTaskActivity.this);
	                 super.onPreExecute();
	             }
	
	             @Override
	             protected void onProgressUpdate(Integer... values)
	             {
	                 super.onProgressUpdate(values);
	             }
	 }

}
	
	
