package com.svws.web;

import com.svws.common.CommonPaths;
import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by
 * User: sverma
 * Date: Nov 28, 2005
 * Time: 6:21:45 PM
 */
public class SecurityFilter implements Filter
{
    /**
     * Implementation of Filter's init().  This allows for this filter to perform
     * any initialization tasks.
     *
     * @param  filterConfig  FilterConfig object.
     *
     * @exception  ServletException  Thrown when any error occurs during initialization.
     */
    public void init(FilterConfig filterConfig) throws ServletException
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SecurityFilter", "init");
        Debug.info ( LOGINFO );
    }

    /**
     * Implementation of Filter's doFilter().  This is where any filtering tasks
     * get perform.  In this case, it's user-authentication.
     *
     * @param  request   ServletRequest object.
     * @param  response  ServletResponse object.
     * @param  chain     The view into the invocation chain of a filtered request
     *                   for a resource
     *
     * @exception  ServletException  Thrown when an error occurs during processing.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SecurityFilter", "doFilter");
        try
        {
            String ctx = CommonPaths.getCurrentContext ((HttpServletRequest)request);
            Debug.info ( LOGINFO + "Context is [" + ctx + "]");
            String action = request.getParameter ( "Action" );
            Debug.info ( LOGINFO + "Action is [" + action + "]");
            boolean isValid = true;
            Debug.info ( LOGINFO + "isSessionRequired (action) is [" + isSessionRequired ( action ) + "]");
            if (isSessionRequired (action))
            {
                Debug.info ( LOGINFO + "Validating session." );
                isValid = SessionManager.isValidSession ( (HttpServletRequest) request ) && isValid ;
            }

            Debug.info ( LOGINFO + "isValid is [" + isValid + "]");
            if (isValid == false)
            {
                Debug.info ( LOGINFO + "Action null or Session invalid." );
                Debug.info ( LOGINFO + "CommonPaths.getCurrentContext((HttpServletRequest) request) is [" + CommonPaths.getCurrentContext ( ( HttpServletRequest ) request ) + "]");
                SVWSHttp.forward ( null, "/pages/login.jsp", request, response);
                return;
            }

            chain.doFilter(request, response);
        }
        catch ( Exception e )
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
    }

    private boolean isSessionRequired ( String action )
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SecurityFilter", "isSessionRequired");
        boolean result = false;
        if (action == null)
        {
            result = false;
            Debug.info ( LOGINFO + "Action null. Returning result is [" + result + "]");
            return false;
        }
        String [] actions = {"Login", "LoginAction", "ForgotPassword",
                                    "ForgotPasswordAction", "AddPatient", "AddPatientAction"};
        final int ACTIONS_LENGTH = actions.length;
        for ( int elem = 0; elem < ACTIONS_LENGTH; elem++ )
        {
            if (action.indexOf(actions[elem]) > -1)
            {
                result = false;
                break;
            }
        }
        Debug.info ( LOGINFO + "The result for action ["+ action +"] is [" + result + "]");
        //return result;
        return false;
    }

    /**
     * Implementation of Filter's destroy().  This is where any cleanup tasks
     * get perform before this filter gets taken out of service.
     */
    public void destroy()
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SecurityFilter", "destroy");
        Debug.info ( LOGINFO + "SecurityFilter.destroy" );
    }
}
