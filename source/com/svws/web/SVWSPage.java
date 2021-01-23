package com.svws.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by
 * User: sverma
 * Date: Nov 21, 2005
 * Time: 7:39:43 PM
 */
/**
 * This is a Java-object representation of a HTML Page.<P/>
 * It is intended that each JSP would have a corresponding implementation
 * of this interface.
 */

public abstract class SVWSPage
{
	public abstract void show(HttpServletRequest request, HttpServletResponse response) throws SVWSException;

} // end class rbacPage

