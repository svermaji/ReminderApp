package com.svws.common;

import com.svws.common.log.*;
import com.svws.common.utils.*;
import com.svws.email.EventsEmailGenerator;

import java.lang.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;

/**
 * <p><strong>BasicBasicContextManager</strong> performs db, property, and debug initialization and
 * cleanup tasks during servlet-container startup and shutdown processes.
 */

public class BasicContextManager implements ServletContextListener
{
    private static ServletContext currentServletContext;
    
    /**
     * Implementation of ServletContextListener's contextInitialized().  This
     * gets called by the servlet container to notify that the web application
     * is ready to process requests.
     *
     * @param  event  ServletContextEvent object.
     */
    public void contextInitialized (ServletContextEvent event)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "BasicContextManager", "contextInitialized");
        try
        {
            init (event);
        }
        catch (ProcessingException e)
        {
            String msg = LOGINFO + "Error while processing: " + e.getMessage ();
            Debug.error ( msg, e);
        }
        catch (Exception e)
        {
            String msg = LOGINFO + "Error occured: " + e.getMessage ();
            Debug.error ( msg, e);
        }
    }

    /**
     * Initialize the other components.
     *
     * @param event of type  ServletContextEvent
     * @throws ProcessingException on error
     */
    public void init (ServletContextEvent event) throws ProcessingException
    {
        ServletContext servletContext = event.getServletContext();
        currentServletContext = servletContext;
        final String LOGINFO = LogInfo.getLogInfo ( "BasicContextManager", "init");

        try
        {
            Debug.info ( LOGINFO + "Initializing debug..." );
            Debug.initialize (  );
            Debug.info ( LOGINFO + "Initializing EventsEmailGenerator...");
            EventsEmailGenerator.getInstance ();
            Debug.info ( LOGINFO + "Initializing context with name [" + servletContext.getServletContextName() + "]" );
        }
        catch ( IOException e )
        {
            Debug.error (LOGINFO + e.getMessage (), e );
        }
        catch (ProcessingException e)
        {
            Debug.error (LOGINFO + "Unable to initialize EventsEmailGenerator...", e);
        }
        catch (Exception e)
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
        Debug.info ( LOGINFO + "Initialization complete." );
    }
    /**
     * Implementation of ServletContextListener's contextInitialized().  This
     * gets called by the servlet container to notify that the servlet context
     * is about to be shutdown.
     *
     * @param  event  ServletContextEvent object.
     */
    public void contextDestroyed (ServletContextEvent event)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "BasicContextManager", "contextDestroyed");
        Debug.info ( LOGINFO + "Destroying context with name [" + event.getServletContext().getServletContextName() +"]");
    }

    /**
     * Retrieves the value of param from the servlet context.
     * If unable to retrieved or value is null then
     * default value is returned.
     *
     * @param param - parameter
     * @param defaultValue
     * @return object
     */
    public static Object getServletContextParamVal (String param, Object defaultValue)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "BasicContextManager", "getServletContextParamVal");
        Debug.info ( LOGINFO + "obtaining value for param [" + param + "].");
        if (StringUtils.hasValue ( param ))
        {
            Object obj = currentServletContext.getInitParameter ( param );
            if (obj != null)
            {
                Debug.info ( LOGINFO + "Returning value [" + obj +"] for param [" + param + "].");
                return obj;
            }
        }
        Debug.info ( LOGINFO + "Unable to get value for param [" + param + "]. Returning default value as [" + defaultValue + "]");
        return defaultValue;
    }

    /**
     * Overloaded method.
     *
     * @param param
     * @return Object
     */
    public static Object getServletContextParamVal (String param)
    {
        return getServletContextParamVal (param, null);
    }

    /**
     * Return the URL of path passed corresponding to web-server.
     *
     * @param path
     * @return URL
     * @throws MalformedURLException
     * @throws ProcessingException
     */
    public static URL getResource (String path) throws MalformedURLException, ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ( "BasicContextManager", "getResource");
        if (!StringUtils.hasValue ( path ))
            throw new ProcessingException ( LOGINFO + "Can not acquire resource. Path is null.");
        return currentServletContext.getResource ( path );
    }

    /**
     * Returns the physical path of the resource.
     *
     * @param path
     * @return string representing physical path
     * @throws ProcessingException
     */
    public static String getRealPath (String path) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ( "BasicContextManager", "getRealPath");
        if (!StringUtils.hasValue ( path ))
            throw new ProcessingException ( LOGINFO + "resource path is null.");
        return currentServletContext.getRealPath ( path );
    }

    /**
     * Return the InputStream of path passed.
     *
     * @param path
     * @return InputStream
     * @throws ProcessingException
     */
    public static InputStream getResourceAsStream (String path) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ( "BasicContextManager", "getResourceAsStream");
        if (!StringUtils.hasValue ( path ))
            throw new ProcessingException ( LOGINFO + "resource path is null.");
        Debug.info(LOGINFO + "returning resource for path [" + path + "].");
        return currentServletContext.getResourceAsStream ( path );
    }

    /**
     * Returns the value of RELOAD_TIME_IN_MIN property
     * and returns 30 if it found null.
     *
     * @return int
     */
    public static int getValueOfReloadProperty ()
    {
        int defaultVal = 30;
        int val = Integer.parseInt ( (String) getServletContextParamVal (ServletConstants.RELOAD_TIME_IN_MIN, new Integer ( defaultVal )) );
        if (val > 40 && val < 10)
            val = defaultVal;

        return val;
    }
}