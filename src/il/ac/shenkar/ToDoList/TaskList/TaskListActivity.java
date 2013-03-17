/*
 * TaskListActivity.java
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


package il.ac.shenkar.ToDoList.TaskList;


import il.ac.shenkar.ToDoList.R;
import il.ac.shenkar.ToDoList.ShowTask.ShowTaskActivity;
import il.ac.shenkar.ToDoList.TaskCreation.CreateTaskActivity;
import il.ac.shenkar.ToDoList.TaskDAO.TaskDataBaseSQL;
import il.ac.shenkar.ToDoList.TaskDAO.TaskList;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import il.ac.shenkar.ToDoList.TaskUtilities.MyConstants;
import il.ac.shenkar.ToDoList.TaskUtilities.TaskOperationsManager;
import il.ac.shenkar.ToDoList.TaskifyAcknowledgement.CreditsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;

/**
 * 
 * @author EranAltay
 *
 */
public class TaskListActivity extends SherlockListActivity implements OnItemClickListener, OnCloseListener, OnOpenListener, OnClickListener
{
	 private 			TaskDataBaseSQL 			myDataBase = new TaskDataBaseSQL(this);
	 private 			TaskListBaseAdapter			baseAdapter;
	 private	   		TextToSpeech 				textToSpeech = null;
	 private 			boolean 					textToSpeechInit = false;
	 private 			TaskOperationsManager 		taskOperationsManager;
	 private  			SlidingMenu 				slidingMenu;
	 private 			ListView 					slideMenuList;
	 private 			ActionBar					actionBar;
	 private 			Handler						handler;
	 private 			Menu						editmenu;
	 private 			TaskObject					deletedTask;
	 private 			int							deletedTaskPosition;
	 private 			View 						viewUndoFeature;

	 @Override
		protected void onStart() 
		{
			super.onStart();
			EasyTracker.getInstance().activityStart(this);	
			myDataBase.addTasksToArray();	
		}


	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    getWindow().setSoftInputMode(
	    	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
   	    baseAdapter = new TaskListBaseAdapter(this, myDataBase);
        setListAdapter(baseAdapter); 
        setContentView(R.layout.tasklist);
        taskOperationsManager = new TaskOperationsManager(this);
        viewUndoFeature = findViewById(R.id.undobar);
        handler = new Handler();
                
         
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setSlidingEnabled(false);
        
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.menulist);
        slidingMenu.setOnCloseListener(this);
        slidingMenu.setOnOpenListener(this);
             
        if(TaskList.getTasksList().isEmpty())
        {
        	slidingMenu.showMenu(false);   	
        }
        
        
        SlidingMenuItems[] menuItems = 
        	{ 
            new SlidingMenuItems(android.R.drawable.ic_menu_add, "Compose A Task!"), 
            new SlidingMenuItems(android.R.drawable.ic_btn_speak_now, "Task For Today!"), 
            new SlidingMenuItems(android.R.drawable.ic_menu_delete, "Delete Done Tasks!"), 
            new SlidingMenuItems(android.R.drawable.ic_delete, "Delete All Tasks!"), 
            new SlidingMenuItems(android.R.drawable.ic_menu_share, "Share My List!"), 
            new SlidingMenuItems(R.drawable.ic_launcher, "About!"), 
        }; 
        
        
        MenuAdapter menuAdapter = new MenuAdapter (slidingMenu.getContext(),
                R.layout.menuitems, menuItems);
        slideMenuList = (ListView) findViewById(R.id.slidemenu);
        slideMenuList.setAdapter(menuAdapter);    
        slideMenuList.setOnItemClickListener(this);
        
        
		initTextToSpeech();
		
		 ListView listView = getListView();
	        
		 SwipeDismissListViewTouchListener touchListener =
	                new SwipeDismissListViewTouchListener
	                (
	                        listView,
	                        new SwipeDismissListViewTouchListener.OnDismissCallback() 
	                        {
	                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
	                                for (int position : reverseSortedPositions) 
	                                {
	                                   deletedTask = TaskList.getTasksList().get(position);
	                                   taskOperationsManager.removeTask(position);     
	                                }
	                                
	                                baseAdapter.notifyDataSetChanged();
	                                TaskOperationsManager.showUndo(viewUndoFeature);
	                            }
	        
	                        });
		 
	        listView.setOnTouchListener(touchListener);
	        listView.setOnScrollListener(touchListener.makeScrollListener());
	  
	        
	    getListView().setOnItemClickListener(new OnItemClickListener()
	     {
	    	MenuItem editMenuitem;
	 		MenuItem showMenuitem;
	 		MenuItem shareMenuitem;
	 		MenuItem doneMenuItem;
	 		MenuItem importantMenuItem;


			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3)
			{
				startItemClickSound();
				
				if(TaskList.getTasksList().get(position).getTaskImportantIndicator() == MyConstants.ISIMPORTANT)
					importantMenuItem = editmenu.findItem(R.id.masimportant).setIcon(R.drawable.notimportantmenu);
				else
					importantMenuItem = editmenu.findItem(R.id.masimportant).setIcon(R.drawable.importantmenu);
				
				
				if(TaskList.getTasksList().get(position).getTaskDoneIndicator() == MyConstants.ISDONE)
				{
					doneMenuItem = editmenu.findItem(R.id.masdone).setIcon(R.drawable.undone);
					editMenuitem  = editmenu.findItem(R.id.edittask).setVisible(false);
					showMenuitem  = editmenu.findItem(R.id.showtask).setVisible(false);
					shareMenuitem  = editmenu.findItem(R.id.sharetask).setVisible(false);
					importantMenuItem = editmenu.findItem(R.id.masimportant).setVisible(false);
				}
				
				else
				{
					doneMenuItem = editmenu.findItem(R.id.masdone).setIcon(R.drawable.donestrike);
					editMenuitem  = editmenu.findItem(R.id.edittask).setVisible(true);
					showMenuitem  = editmenu.findItem(R.id.showtask).setVisible(true);
					shareMenuitem  = editmenu.findItem(R.id.sharetask).setVisible(true);
					importantMenuItem = editmenu.findItem(R.id.masimportant).setVisible(true);
				}
		
				
				
			}

		});		
     	
   }
	


	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.optionsmenu, menu);
		editmenu = menu;
		return super.onPrepareOptionsMenu(menu);
	}
	    
	  @Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		return super.onCreateOptionsMenu(menu);
	}
	  
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int position = getListView().getCheckedItemPosition();
		
		if(item.getItemId() == android.R.id.home)
		{
			slidingMenu.toggle();
			return false;
		}
			
		if(position == ListView.INVALID_POSITION || TaskList.getTasksList().isEmpty())
		{
			Toast.makeText(getApplicationContext(), "Select A Task To Edit Or Slide The Menu", Toast.LENGTH_SHORT).show();
			return false;
		}
		

		TaskObject chosenTask = TaskList.getTasksList().get(position);
		
			
		switch(item.getItemId())
		{
				
			case R.id.edittask:
				 Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
		         Log.i(getClass().getSimpleName(), "Starting Activity-CreateTaskActivity");
		         intent.putExtra("ob", position);
		         startActivity(intent);
			break;

			
			case R.id.showtask:
				 Intent showTaskIntent = new Intent(getApplicationContext(), ShowTaskActivity.class);
        		 showTaskIntent.putExtra("tasktoshow", chosenTask);
		    	 Log.i(getClass().getSimpleName(), "Starting Activity-Show Task");
		    	 startActivity(showTaskIntent);   	 
		    	 EasyTracker.getTracker().sendEvent("UI-Popup Menu", "Show- Activity", "Popup Menu", (long) 0); 
		    	 break;
		    	 
			case R.id.masimportant:
            	switch(chosenTask.getTaskImportantIndicator())
            	{
	            	case MyConstants.NOTIMPORTANT:
	            		taskOperationsManager.changeTaskImportanceState(chosenTask,MyConstants.ISIMPORTANT);
	            	break;
	            	
	            	
	            	case MyConstants.ISIMPORTANT:
	            		taskOperationsManager.changeTaskImportanceState(chosenTask,MyConstants.NOTIMPORTANT);
	            	break;            			               		            	
            	}
            break;
            
			case R.id.masdone:
				switch(chosenTask.getTaskDoneIndicator())
				{
		        	case MyConstants.NOTDONE:
		        		taskOperationsManager.changeTaskDoneState(chosenTask,MyConstants.ISDONE);
		        	break;
		        	
		        	
		        	case MyConstants.ISDONE:
		        		taskOperationsManager.changeTaskDoneState(chosenTask,MyConstants.NOTDONE);  
		        	break;	
				}
			break;

			case R.id.sharetask:
              	taskOperationsManager.shareTask(chosenTask);
   		    	 EasyTracker.getTracker().sendEvent("UI-Popup Menu", "Share Task", "Popup Menu", (long) 0); 
            break;        
	
		}

		getListView().clearChoices();
		invalidateOptionsMenu();
       	baseAdapter.notifyDataSetChanged();
		return super.onOptionsItemSelected(item);
	}

	@Override
	  public void onStop()
	  {
	    super.onStop();
	    EasyTracker.getInstance().activityStop(this); 
	  }

	 @Override
     public void onResume()
	 {
	        super.onResume();
	        baseAdapter.notifyDataSetChanged();
     }

	@Override
	public void onDestroy()
	{
		if (textToSpeech != null) 
		{
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
		super.onDestroy(); 
	} 
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e)
	{
	    switch(keycode) 
	    {
	        case KeyEvent.KEYCODE_MENU:
	            slidingMenu.toggle(true);
	        return true;
	    }

	    return super.onKeyDown(keycode, e);
	}
	

	public void onOpen() 
	{
		getListView().setEnabled(false);
		getListView().clearChoices();	
	}


	public void onClose() 
	{
		getListView().setEnabled(true);
	}

	private void initTextToSpeech() 
	{
		Intent intent = new Intent(Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(intent, MyConstants.CHECK_TTS_DATA);
	}
	 
	/**
	* TextToSpeech to section
	*/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == MyConstants.CHECK_TTS_DATA) 
		{
			if (resultCode == Engine.CHECK_VOICE_DATA_PASS) 
			{
				textToSpeech = new TextToSpeech(this, new OnInitListener()
				{
					public void onInit(int status)
					{
						if (status == TextToSpeech.SUCCESS) 
						{
							textToSpeechInit = true;
							if (textToSpeech.isLanguageAvailable(Locale.US) >= 0)
									textToSpeech.setLanguage(Locale.US);
							textToSpeech.setPitch(1);
							textToSpeech.setSpeechRate(1);
						}
				
					}
				}); 
			} 
			
			else
			{
				Intent installVoice = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installVoice);
			}
		}
	}
	
	/**
	* TextToSpeech to section
	*/
	private void speak(String inputString) 
	{
		if (textToSpeech != null && textToSpeechInit)
		{  
			textToSpeech.speak(inputString, TextToSpeech.QUEUE_ADD, null);
		}
	}
	
	
	/**
	* this function will invoke the asynctask, and will run
	* in another thread from the UI, and will get the relevant task for the
	* textToSpeech system.
	* 
	*/
	private void checkForTodayTaskAndSpeech()
	{
		new SpeakTodayTasks().execute();
	}
/***///////////////////////////////////////////////////////////////////////////////////	

	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) 
	{
		
		switch(position)
		{
			case 0:
			       Intent creationIntent = new Intent(this, CreateTaskActivity.class);
		    		Log.i(getClass().getSimpleName(), "Starting Activity-CreateTask");
		    		startActivity(creationIntent);
		    		EasyTracker.getTracker().sendEvent("UI-SlideMenu", "Create- Activity", "SlideMenu", (long) 0); 
		    		break;
			case 1:
				checkForTodayTaskAndSpeech();
	    		EasyTracker.getTracker().sendEvent("UI-SlideMenu", "button_Speech", "SlideMenu", (long) 0); 
		    	break;
	    	
			case 2:
				taskOperationsManager.deleteDoneTasks();
				baseAdapter.notifyDataSetChanged();
	    		EasyTracker.getTracker().sendEvent("UI-SlideMenu", "DELETE DONE Task Only", "SlideMenu", (long) 0); 
				break;
		    	
			case 3:
    		if(!TaskList.getTasksList().isEmpty())
			{
					 Log.i(getClass().getSimpleName(), "AlertDialog diaBox = makeListClearDialogBox() Activated");
					 AlertDialog diaBox = makeListClearDialogBox();
					 diaBox.show();
			 }
    		EasyTracker.getTracker().sendEvent("UI-SlideMenu", "DELETE all Task", "SlideMenu", (long) 0); 
       		break;    	
    			
			case 4:
				taskOperationsManager.shareList();
	    		EasyTracker.getTracker().sendEvent("UI-SlideMenu", "Share List", "SlideMenu", (long) 0); 
				break;
				
			case 5:
				 Intent creditsIntent = new Intent(this, CreditsActivity.class);
	    		 startActivity(creditsIntent);
		     	 EasyTracker.getTracker().sendEvent("UI-SlideMenu", "About Activity", "SlideMenu", (long) 0); 
		    	 break;					
		}
	
	}	
	
	private void startItemClickSound()
	{
        
	handler.post(new Runnable() 
    		      {
    		       public void run() 
    		       {
    		    	 final MediaPlayer mediaplayer;
    		    	  //delete from database and tasklist 
    		    	  mediaplayer = MediaPlayer.create(TaskListActivity.this, R.raw.tick); 
    		    	  mediaplayer.setVolume(1, 1);
    		    	  mediaplayer.start();
    		       }
    		      });
	}

	//setting up the dialoug box after click on the clear list btn
	private AlertDialog makeListClearDialogBox()
	{
    	
        AlertDialog listClearDialog = 

        	new AlertDialog.Builder(this) 
        	//set message, title, and icon
        	.setTitle(getApplicationContext().getString(R.string.appName)) 
        	.setMessage("Are you sure that you want clear your list?") 
        	
           	.setPositiveButton("YES!", new DialogInterface.OnClickListener()
           	{ 
        		public void onClick(DialogInterface dialog, int whichButton) 
        		{ 
        			taskOperationsManager.clearList();
        			TaskList.getTasksList().clear();
           			baseAdapter.notifyDataSetChanged();
           			
           		}              
        	})

        	.setNegativeButton("NO!", new DialogInterface.OnClickListener() { 
        		public void onClick(DialogInterface dialog, int whichButton)
        		{ 
        			dialog.cancel();
        		} 
        	})
        	
        	.create();
        	
        	return listClearDialog;
    }
	
	
	class SpeakTodayTasks extends AsyncTask<Void, Integer, Void>
	{
			private Integer taskWithNotificationCounter = 0;
	    
		     @Override
	         protected void onPreExecute() 
	         {
	                Log.i( getClass().getSimpleName(), "onPreExecute()" );
	                super.onPreExecute();
	         }

			@Override	
			protected void onPostExecute(Void result)
			{	
				Log.i( getClass().getSimpleName(), "onPostExecute(String result)" );
				super.onPostExecute(result);
				
			}	

			@Override
			protected Void doInBackground(Void... arg0)
			{
				int size = TaskList.getTasksList().size();
				Date currentTime = null;
				Date taskNotifyTime = null;
				
	
				for (int i=0 ;i<size; i++)
				{
										
					TaskObject it = TaskList.getTasksList().get(i);
			           
					 if(it.getTaskNotificationDate()!=null)
					 {
										
						 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
							 //parsing the date string into date objects
					 
			             try 
					    {
			            	 taskNotifyTime = simpleDateFormat.parse(it.getTaskNotificationDate());
			            	 currentTime = simpleDateFormat.parse(TaskList.getCurrentSystemTime());
						} 
					 
					 	catch (ParseException e) 
					 	{
					 		Log.e( getClass().getSimpleName(), "Parse Error",e.fillInStackTrace());
							
						}
	     
					
			     		if(taskNotifyTime.equals(currentTime))
						{
			     			
				     		taskWithNotificationCounter++;
				     		if(taskWithNotificationCounter == 1)
				     			speak("Tasks For Today");
				     				
				     		speak("Task,");
				     		speak(taskWithNotificationCounter.toString());
							speak(it.getTaskTitle());
							Log.i( getClass().getSimpleName(), "Task Specch");
			     		}
						             
					 }
										
				}
				
				if(taskWithNotificationCounter==0)
				{
						speak("no scheduled tasks for today");
						Log.i( getClass().getSimpleName(), "No Tasks");
				}
				
				return null;
				
			
			}
				
		}


	public void onClick(View v)
	{
		
		
	}


}
 

 
