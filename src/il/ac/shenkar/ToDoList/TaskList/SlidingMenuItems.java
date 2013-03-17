/*
 * SlidingMenuItems.java
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

package il.ac.shenkar.ToDoList.TaskList;


/**
 * 
 * The Sliding Menu Class
 * Populate the view with name and icon
 * @author EranAltay
 *
 */
public class SlidingMenuItems 
{
		public int icon;
		public String item;
	   
	    
	    public SlidingMenuItems()
	    {
	        super();
	    }
	    
	    public SlidingMenuItems(int icon,String name)
	    {
	        super();
	        this.item = name;
	        this.icon = icon;
	    }
	    
	    @Override
	    public String toString() 
	    {
	        return this.item;
	    }

}
