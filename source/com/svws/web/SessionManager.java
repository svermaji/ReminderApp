package com.svws.web;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.UserContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import java.util.*;

/**
 * Created by:
 * User: sverma
 * Date: Aug 1, 2005
 * Time: 4:50:04 PM
 *
 * This class maintains the HashMap of the sessions
 * and store them on the basis of their sessionId
 * in singleton pattern
 */
public class SessionManager implements HttpSessionListener
{

    private static HashMap hmSessionTracker = new HashMap();
    private static SessionManager sessionManager = null;
    private static final int TIMEINMS = 30*60*1000;

    static
    {
        new MyTimer (TIMEINMS);
    }

    public static SessionManager getSessionManager ()
    {
        return sessionManager;
    }

    /**
     * This method return the session based on the request
     * from the hashmap on the basis of sessionId
     *
     * @param req - HttpServletRequest
     * @return HttpSession
     */
    public static HttpSession getSessionInManager (HttpServletRequest req)
    {
        synchronized (hmSessionTracker)
        {
            return (HttpSession) hmSessionTracker.get(req.getSession().getId());
        }
    }

    /**
     * This method sets the session based on the request
     * into the hashmap on the basis of sessionId
     *
     * @param req - HttpServletRequest
     */
    public static synchronized void setSessionInManager(HttpServletRequest req)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SessionManager", "setSessionInManager");
        final String sessionID = req.getSession ().getId ();
        if (!hmSessionTracker.containsKey(sessionID))
        {
            req.getSession ().setMaxInactiveInterval ( TIMEINMS );
            hmSessionTracker.put(sessionID, req.getSession());
            Debug.info ( LOGINFO + "sessionID is [" + sessionID + "]");
            describe  ();
        }
        else
        {
            Debug.info ( LOGINFO + "Unable to store session. Already contains sessionId [" + req.getSession().getId() + "]");
        }
    }

    private static void describe ()
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SessionManager", "describe");
        Debug.info ( LOGINFO + "Total sessions are [" + hmSessionTracker.size() + "]");
        Iterator iterator = hmSessionTracker.keySet ().iterator ();
        int elem=1;
        while (iterator.hasNext ())
        {
            String sessionId = (String) iterator.next ();
            HttpSession session = (HttpSession) hmSessionTracker.get(sessionId);
            Vector vUser = (Vector) session.getAttribute ("vUserInfo");
            String userName = "NoName";
            if (vUser != null)
            {
                Debug.info ( LOGINFO + "retriving user information from session");
                userName = (String) vUser.get ( 1 );
            }
            else
                Debug.info ( LOGINFO + "user information from session is null");

            Debug.info ( LOGINFO + "Sessions[" + (elem++) + "] for user [" + userName + "]");
        }
    }

    /**
     *
     * This method removes the session based on the request
     * from the hashmap on the basis of sessionId
     *
     * @param req - HttpServletRequest
     */
    public static synchronized void removeSessionInManager(HttpServletRequest req)
    {
        removeSessionInManager(req.getSession().getId());
    }

    /**
     *
     * This method removes the session based on the sessionId
     *
     * @param sessionId - String
     */
    public static synchronized void removeSessionInManager (String sessionId)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SessionManager", "removeSessionInManager");
        if (hmSessionTracker.containsKey ( sessionId ))
        {
            Debug.info ( LOGINFO + "removing...");
            ((HttpSession) hmSessionTracker.get(sessionId)).invalidate ();
            hmSessionTracker.remove(sessionId);
            Debug.info ( LOGINFO + "Removed sessionID [" + sessionId + "]");
            destroyContext ();
            describe  ();
        }
        else
        {
            Debug.info ( LOGINFO + "No session found with sessionId [" + sessionId + "]");
        }
    }

    /**
     * This method returns whether the session from current
     * request is the part of hashmap or not

     * @param req
     * @return boolean - status of validation
     */
    public static boolean isValidSession (HttpServletRequest req)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SessionManager", "isValidSession");
        boolean isValid = false;
        synchronized (hmSessionTracker)
        {
            isValid=hmSessionTracker.containsKey(req.getSession().getId());
        }
        Debug.info ( LOGINFO + "isValid is [" + isValid + "]");
        return isValid;
    }

    /**
     * Indicates that a session has been created
     *
     * @param  event  HttpSessionEvent object.
     */
    public void sessionCreated(HttpSessionEvent event)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SessionManager", "sessionCreated");
        Debug.info ( LOGINFO + "event.getSession ().getId () is [" + event.getSession ().getId () + "] at time [" + new Date () + "]");
    }

    /**
     * Indicates that a context is going to be destroyed.
     */
    public static void destroyContext ()
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SessionManager", "destroyContext");
        UserContext uc = null;
        try
        {
            uc = UserContext.getInstance ();
            Debug.info ( LOGINFO + "Destroying context for user [" + uc.getUserId () + "]");
            uc.destroy ();
        }
        catch (Exception e)
        {
            Debug.error (LOGINFO + "Unable to destroy context for user [" + uc.getUserId () + "]");
        }
    }
    /**
     * Indicates that a session is going to be destroyed.
     *
     * @param  event  HttpSessionEvent object.
     */
    public void sessionDestroyed(HttpSessionEvent event)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "SessionManager", "sessionDestroyed");
        destroyContext ();
        Debug.info ( LOGINFO + "event.getSession ().getId () is [" + event.getSession ().getId () + "] now destroyed.");
    }

    /**
     * This inner class act as a Timer Task to check session validity
     * from time to time
     */
    private static class MyTimerTask extends TimerTask
    {
        public void run ()
        {
            final String LOGINFO = LogInfo.getLogInfo ( "SessionManager.MyTimerTask", "run");
            synchronized ( hmSessionTracker )
            {
                Iterator itar = hmSessionTracker.keySet ().iterator ();
                Vector vRemovingSessionIds = new Vector ();
                while (itar.hasNext ())
                {
                    HttpSession session = (HttpSession) hmSessionTracker.get ( itar.next () );
                    if (session != null)
                    {
                        Date date = new Date ();
                        Debug.info ( LOGINFO + "Session checked for validation is [" + session.getId () + "] at [" + date + "]");
                        if ((session.getLastAccessedTime ()+session.getMaxInactiveInterval ()) < date.getTime ())
                        {
                            Debug.info ( LOGINFO + "Session [" + session.getId () + "] is expired.");
                            vRemovingSessionIds.add ( session.getId () );
                        }
                    }
                }
                int vRemovingSessionIdsSize = vRemovingSessionIds.size ();
                Debug.info ( LOGINFO + "size of vector for removing session ids is [" + vRemovingSessionIdsSize + "]");
                for ( int elem = 0; elem < vRemovingSessionIdsSize; elem++ )
                {
                    SessionManager.removeSessionInManager ((String) vRemovingSessionIds.get ( elem ));
                }

                Debug.info ( LOGINFO + "Timer executed...");
                describe  ();
            }
        }
    }

    /**
     * This class sets the time and instantiate the
     * Timer task class
     */
    private static class MyTimer extends Timer
    {
        Timer timer;

        public MyTimer ( int timeInSec )
        {
            final String LOGINFO = LogInfo.getLogInfo ( "SessionManager.MyTimer", "Constructor");
            timer = new Timer (true);
            timer.schedule (new MyTimerTask ( ), timeInSec, timeInSec);
            Debug.info ( LOGINFO + "timeInSec is [" + timeInSec + "]");
        }
    }
}
