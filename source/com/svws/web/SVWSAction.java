package com.svws.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Nov 21, 2005
 * Time: 7:36:12 PM
 */


/**
 * Represents a request from the UI Layer, for performing an action.
 * This is an implementaton of Command design pattern. It is intended that
 * concrete subclasses would represent one or more actions emanating out of
 * a Page.
 * These objects are the ones that contacts the service layer and specifies
 * the next page (JSP).
 *
 * @author Shailendra Verma
 */


public interface SVWSAction{
    /**
     * Performs the relevant action and specifies the next page by
     * returning the appropriate impl. of Page.
     *
     * @return the next <B>Page</B> to be displayed.
     */

	public SVWSPage performAction(HttpServletRequest request, HttpServletResponse response)throws SVWSException;


} // end interface rbacAction
