package il.ac.shenkar.ToDoList.ShowTask;
/*
 * ShowTaskItems.java
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

/**
 * 
 * The ShowTaskItems Class
 * Populate the view with name and icon
 * @author EranAltay
 *
 */
public class ShowTaskItems 
{
		public String item;
		public String subItem;
	   
	    
	    public ShowTaskItems()
	    {
	        super();
	    }
	    
	    public ShowTaskItems(String item, String subItem)
	    {
	        super();
	        this.item = item;
	        this.subItem = subItem;
	    }
	    
	    @Override
	    public String toString() 
	    {
	        return this.item + "\n" + this.subItem;
	    }

}
