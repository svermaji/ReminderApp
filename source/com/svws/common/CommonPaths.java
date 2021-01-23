package com.svws.common;

import com.svws.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Do not use Debug statements as it would cause cyclic effect
 * 
 * Created by
 * User: sverma
 * Date: Nov 22, 2005
 * Time: 7:00:17 PM.
 */
public class CommonPaths
{
    private static String currentContext = "";
    private static HttpServletRequest req = null;

    /**
     * Return the current context path.
     *
     * @param request
     * @return string
     */
    public static String getCurrentContext (HttpServletRequest request)
    {
        req = request;
        if (!StringUtils.hasValue ( currentContext.trim()))
            setCurrentContext ( request );
        return currentContext;
    }

    /**
     * Return the current context path.
     *
     * @return string
     */
    public static String getClientIP ()
    {
        String ip="";

        try
        {
            if (req != null)
            {
                if (StringUtils.hasValue ( req.getRemoteHost () ))
                {
                    ip = req.getRemoteHost ();
                }
            }
        }
        catch (Exception e)
        {
            ip="";
        }

        return ip;
    }

    /**
     * sets the currect context path from request.
     *
     * @param request of type HttpServletRequest
     */
    public static void setCurrentContext ( HttpServletRequest request )
    {
        currentContext = request.getContextPath ();
    }
}
