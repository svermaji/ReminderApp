package com.svws.web;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by:
 * User: sverma
 * Date: Aug 4, 2005
 * Time: 8:16:42 PM
 */

/**
 * This is a utility class containing HTTP-related convenience functions.
 *
 */

public class SVWSHttp
{

    /**
    * Disable instantiation.
    */
    private SVWSHttp()
    {
    }

    /**
    * Forwards request to the specified URL
    */
    //public static void forward(String url, HttpServletRequest req, HttpServletResponse resp)
    public static void forward(ServletContext context, String url, ServletRequest req, ServletResponse resp)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SVWSHttp", "forward");
        try
        {
            Debug.info ( LOGINFO + "url is [" + url + "]");
            if (context!=null)
            {
                Debug.info ( LOGINFO + "Context" );
                context.getRequestDispatcher(url).forward(req, resp);
            }
            else
            {
                Debug.info ( LOGINFO + "Request" );
                req.getRequestDispatcher(url).forward(req, resp);
            }
        }
        catch (ServletException e)
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
        catch (IOException e)
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
        catch (Exception e)
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
    }

    /**
    * Redirects response to the specified URL
    */
    public static void redirect(String url, HttpServletResponse res)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SVWSHttp", "redirect");
        try
        {
            Debug.info ( LOGINFO + "url is [" + url + "]");
            res.sendRedirect(url);
        }
        catch (IOException e)
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
    }
}