package com.svws.web;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Nov 15, 2005
 * Time: 9:07:08 PM
 */
public class ControllerServlet extends HttpServlet
{
    String LOGINFO = LogInfo.getLogInfo ( "ControllerServlet", "" );

    /**
     * Redefinition of init() in HttpServlet.  This allows for all the resources
     * to be initialized before the servlet starts accepting requests.
     *
     * @exception  javax.servlet.ServletException  Thrown when any ERROR occurs during initialization.
     */

    public void init() throws ServletException
    {
        LOGINFO = LogInfo.getLogInfo ( "ControllerServlet", "init" );
        super.init();
        Debug.info ( LOGINFO + "Initialization complete." );
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGINFO = LogInfo.getLogInfo ( "ControllerServlet", "doGet" );
        try
        {
            Debug.info ( LOGINFO );
            handleRequest (request, response, getActionValue (request));
        }
        catch ( Exception e )
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGINFO = LogInfo.getLogInfo ( "ControllerServlet", "doPost" );
        Debug.info ( LOGINFO );
        doGet (request, response);
    }

    private void handleRequest ( HttpServletRequest request, HttpServletResponse response, String target)
    {
        LOGINFO = LogInfo.getLogInfo ( "ControllerServlet", "handleRequest" );
        try
        {
            Debug.info ( LOGINFO + "Target is [" + target + "]");
            SVWSHttp.forward ( getServletContext (), target, request, response);
        }
        catch (Exception e)
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
    }

    private String getActionValue (ServletRequest request)
    {
        LOGINFO = LogInfo.getLogInfo ( "ControllerServlet", "getActionValue" );
        String action = "";
        if (request.getParameter ( "Action" ) != null)
        {
            action = request.getParameter ( "Action" );
        }
        Debug.info ( LOGINFO + "action is [" + action + "]");
        return action;
    }
}
