package com.svws.events;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.db.DBUtil;
import com.svws.common.db.DBConstants;
import com.svws.common.db.DBException;
import com.svws.common.BasicContextManager;
import com.svws.common.utils.ServletConstants;
import com.svws.common.utils.StringUtils;
import com.svws.common.utils.ReadSource;

import java.util.*;

/**
 * Created by
 * User: sverma
 * Date: Mar 8, 2006
 * Time: 4:55:45 PM
 */
/**
 * This class contains the operation related to events
 */
public final class ManageEvents
{

    private static Thread reloadThread;
    private static TreeMap eventTypes = null;
    private static String[][] eventTypesAsStr = null;
    private static String[][] eventEmailModesAsStr = null;

    /**
     * Private constructor
     */
    private ManageEvents ()
    {
    }

    /**
     * Static block to initiate the class at loading time
     * and start timer.
     */
    static {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "static block" );
        try {
            int defaultReloadTime = 30;
            Object prm = BasicContextManager.getServletContextParamVal (ServletConstants.RELOAD_TIME_IN_MIN, new Integer ( defaultReloadTime ));
            int reloadTime = Integer.parseInt((String) prm);
            //int reloadTime = 30;
            if (reloadTime > 40 && reloadTime < 10)
                reloadTime = 30;

            Debug.info (LOGINFO + "Reload time obtained as [" + reloadTime + "]");

            startReloadThread (reloadTime);
            init ();

        }
        catch(Exception e)
        {
            Debug.error (LOGINFO + e, e);
        }
    }

    /**
     * Initialize method
     */
    private static void init ()
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "init" );
        try
        {
            Debug.info ( LOGINFO + "Loading property file [" + ServletConstants.EVENT_PROPERTIES + "]...");
            Properties prop = ReadSource.getResourceProperties ( ServletConstants.EVENT_PROPERTIES, true );


/*
            File f = new File ("E:\\i312\\tomcat\\webapps\\svws\\resources\\properties\\events.properties");
            FileInputStream fis = new FileInputStream ( f );
            Properties prop = new Properties ( );
            prop.load ( fis );
*/


            Debug.info ( LOGINFO + "Property file loaded successfully as [" + prop.toString () + "].");

            eventTypesAsStr = new String [prop.size ()] [2];
            Debug.info ( LOGINFO + "Reading properties...");

            eventTypes = new TreeMap ();
            Iterator itar = prop.keySet ().iterator ();
            while (itar.hasNext ())
            {
                Object obj = itar.next ();
                if (obj!=null)
                {
                    String key = (String) obj;
                    String value = prop.getProperty (key);
                    Debug.info ( LOGINFO + "Event property obtained as [" + key + "] and its alias as [" + value + "].");
                    eventTypes.put (key, value);
                }
                else
                    Debug.info ( LOGINFO + "Skipping event with property null.");
            }
            Debug.info ( LOGINFO + "Total [" + eventTypes.size () + "] events loaded.");
            /* Re-populating string array to get sorted values only */
            itar = eventTypes.keySet ().iterator ();
            int row = 0;
            while (itar.hasNext ())
            {
                Object obj = itar.next ();
                if (obj!=null)
                {
                    String key = (String) obj;
                    String value = (String) eventTypes.get (key);
                    eventTypesAsStr [row][0] = key;
                    eventTypesAsStr [row][1] = value;
                    row++;
                }
            }

            Debug.info ( LOGINFO + "Loading property file [" + ServletConstants.EVENT_EMAIL_MODES_PROPERTIES + "]...");
            prop = ReadSource.getResourceProperties ( ServletConstants.EVENT_EMAIL_MODES_PROPERTIES, true );


/*
            f = new File ("E:\\i312\\tomcat\\webapps\\svws\\resources\\properties\\event-email-modes.properties");
            fis = new FileInputStream ( f );
            prop = new Properties ( );
            prop.load ( fis );
*/


            Debug.info ( LOGINFO + "Property file loaded successfully as [" + prop.toString () + "].");

            eventEmailModesAsStr = new String [prop.size ()] [2];
            Debug.info ( LOGINFO + "Reading properties...");

            TreeMap eventEmailModes = new TreeMap ();
            itar = prop.keySet ().iterator ();
            while (itar.hasNext ())
            {
                Object obj = itar.next ();
                if (obj!=null)
                {
                    String key = (String) obj;
                    String value = prop.getProperty (key);
                    Debug.info ( LOGINFO + "Event property obtained as [" + key + "] and its alias as [" + value + "].");
                    eventEmailModes.put (key, value);
                }
                else
                    Debug.info ( LOGINFO + "Skipping event with property null.");
            }
            Debug.info ( LOGINFO + "Total [" + eventEmailModes.size () + "] events loaded.");
            /* Re-populating string array to get sorted values only */
            itar = eventEmailModes.keySet ().iterator ();
            row = 0;
            while (itar.hasNext ())
            {
                Object obj = itar.next ();
                if (obj!=null)
                {
                    String key = (String) obj;
                    String value = (String) eventEmailModes.get (key);
                    eventEmailModesAsStr [row][0] = key;
                    eventEmailModesAsStr [row][1] = value;
                    row++;
                }
            }
        }
        catch (Exception e)
        {
            Debug.error (LOGINFO + e, e);
        }
    }

    /**
     * Add and edit the event.
     * @param event - bean object
     * @throws EventException on error
     */
    public static void handleEvent (Event event) throws EventException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "handleEvent" );
        try
        {
            Hashtable attributes = new Hashtable ();
            Debug.info (LOGINFO + "adding values from bean in attributes...");
            attributes.put (DBConstants.USER_NAME, event.getUser_name ());
            Debug.info (LOGINFO + "added " + DBConstants.USER_NAME + " as [" + event.getUser_name () + "]");
            attributes.put (DBConstants.EVENT_TAB_EVENT_DATE, event.getEvent_date ());
            Debug.info (LOGINFO + "added " + DBConstants.EVENT_TAB_EVENT_DATE + " as [" + event.getEvent_date () + "]");
            attributes.put (DBConstants.EVENT_TAB_EVENT_TYPE, event.getEvent_type ());
            Debug.info (LOGINFO + "added " + DBConstants.EVENT_TAB_EVENT_TYPE + " as [" + event.getEvent_type () + "]");
            attributes.put (DBConstants.EVENT_TAB_EVENT_DESC, event.getEvent_desc ());
            Debug.info (LOGINFO + "added " + DBConstants.EVENT_TAB_EVENT_DESC + " as [" + event.getEvent_desc () + "]");
            attributes.put (DBConstants.EVENT_TAB_RECEIVER_MAIL, event.getReceiver_email ());
            Debug.info (LOGINFO + "added " + DBConstants.EVENT_TAB_RECEIVER_MAIL + " as [" + event.getReceiver_email () + "]");
            attributes.put (DBConstants.EVENT_TAB_EVENT_USER, event.getEvent_user ());
            Debug.info (LOGINFO + "added " + DBConstants.EVENT_TAB_EVENT_USER + " as [" + event.getEvent_user () + "]");
            attributes.put (DBConstants.EVENT_TAB_EMAIL_MODE, new Integer ( event.getEmail_mode ()));
            Debug.info (LOGINFO + "added " + DBConstants.EVENT_TAB_EMAIL_MODE + " as [" + event.getEmail_mode () + "]");
            attributes.put (DBConstants.EVENT_TAB_ACTIVE, new Integer ( event.getActive ()));
            Debug.info (LOGINFO + "added " + DBConstants.EVENT_TAB_ACTIVE + " as [" + event.getActive () + "]");

            if (event.getEvent_id () > 0)
            {
                Hashtable where = new Hashtable ();
                where.put (DBConstants.EVENT_TAB_EVENT_ID, new Integer ( event.getEvent_id ()));
                updateEvents (attributes, where);
                Debug.info (LOGINFO + "Record updated." );
            }
            else
            {
                insertEvent (attributes);
                Debug.info (LOGINFO + "Record inserted." );
            }
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new EventException (e.getMessage ());
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new EventException (e.getMessage ());
        }
    }

    /**
     * Adds new event
     *
     * @param attributes of type Hashtable
     * @throws EventException on error
     */
    public static void insertEvent (Hashtable attributes) throws EventException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "insertEvent" );
        try
        {
            int event_id = getNextValOfSequence ();
            attributes.put ( DBConstants.EVENT_TAB_EVENT_ID, event_id+"");
            DBUtil.insertRow (DBConstants.EVENT_TABLE, attributes);
            Debug.info (LOGINFO + "Record inserted." );
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new EventException (e.getMessage ());
        }
    }

    /**
     * Obtains the next value of sequence "SEQ_SV_USERS"
     * @return int
     * @throws DBException on error
     */
    private static int getNextValOfSequence () throws DBException
    {
        return DBUtil.getNextValOfSequence ("SEQ_SV_EVENTS");
    }

    /**
     * Updates the event.
     *
     * @param attributes column values
     * @param where clause
     * @return boolean as status
     * @throws EventException on error
     */
    public static boolean updateEvents (Hashtable attributes, Hashtable where) throws EventException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "updateEvents" );
        try
        {
            Debug.info (LOGINFO + "Record updated." );
            return DBUtil.updateRows (DBConstants.EVENT_TABLE, attributes, where);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new EventException (e.getMessage ());
        }
    }

    /**
     * Update event based on sql.
     *
     * @param sql string
     * @return boolean status.
     * @throws EventException on error
     */
    public static boolean updateEvents (String sql) throws EventException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "updateEvents...sql" );
        try
        {
            Debug.info (LOGINFO + "Record updated....sql" );
            return DBUtil.updateRecords (sql);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new EventException (e.getMessage ());
        }
    }

    /**
     * delets the event matching the criteria.
     *
     * @param where clause
     * @return boolean as status
     * @throws EventException on error
     */
    public static boolean deleteEvents (Hashtable where) throws EventException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "deleteEvents" );
        try
        {
            Debug.info (LOGINFO + "Record deleted." );
            return DBUtil.deleteRows (DBConstants.EVENT_TABLE, where);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new EventException (e.getMessage ());
        }
    }

    /**
     * Selects the events based on criteria
     *
     * @param columns as vector
     * @param where clause
     * @param orderBy as vector
     * @param ascending boolean
     * @return Vector of events
     * @throws EventException on error
     */
    public static Vector selectEvents (Vector columns, Hashtable where, Vector orderBy, boolean ascending) throws EventException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "selectEvents" );
        try
        {
            Debug.info (LOGINFO + "Record selected." );
            return DBUtil.selectRecords (DBConstants.EVENT_TABLE, columns, where, orderBy, ascending);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new EventException (e.getMessage ());
        }
    }

    /**
     * Select event based on sql.
     *
     * @param sql string
     * @return Vector of events.
     * @throws EventException on error
     */
    public static Vector selectEvents (String sql) throws EventException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "selectEvents...sql" );
        try
        {
            Debug.info (LOGINFO + "Record selected." );
            return DBUtil.selectRecords (sql);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new EventException (e.getMessage ());
        }
    }

    /**
     * Returns the events as 2-d array of string
     * @return String [][]
     */
    public static String[][] getEvents ()
    {
        return eventTypesAsStr;
    }

    /**
     * Returns the event-email-modes as 2-d array of string
     * @return String [][]
     */
    public static String[][] getEventEmailModes ()
    {
        return eventEmailModesAsStr;
    }

    /**
     * Returns the alias for event passed
     * @param event name of event
     *
     * @return String as alias
     */
    public static String getEventAlias (String event)
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "getEventAlias" );
        Debug.info ( LOGINFO + "Looking for alias of event [" + event + "]");

        if (StringUtils.hasValue (event) && eventTypes.containsKey ( event ))
        {
            String alias = (String) eventTypes.get ( event );
            Debug.info ( LOGINFO + "Found alias [" + alias + "] for event [" + event + "]");
            return alias;
        }

        Debug.info ( LOGINFO + "No alias exists for event [" + event + "]. Returning same value.");
        return event;
    }

    /**
     * start the reload thread
     *
     * @param reloadInterval as long
     */
    private static synchronized void startReloadThread(long reloadInterval) {
        final String LOGINFO = LogInfo.getLogInfo ("ManageEvents", "startReloadThread" );
        // Start the background reload thread.
        if (reloadThread == null) {
            Debug.info ( LOGINFO + "Creating reloadThread first time for interval [" + reloadInterval + "] minutes");
            reloadThread = new Thread(new ReloadThread(reloadInterval));
            Debug.info ( LOGINFO + "Thread created. Setting Daemon...");
            reloadThread.setDaemon(true);
            Debug.info ( LOGINFO + "Thread set to be Daemon. Starting thread...");
            reloadThread.start();
            Debug.info ( LOGINFO + "Thread started.");
        }
    }

    /**
     * Inner ReloadThread class
     */
    private static class ReloadThread implements Runnable
    {
        private long reloadInterval = 5;
        private static final long MSEC_PER_MINUTE = 60 * 1000;

        /**
         * Constructor
         *
         * @param reloadInterval as long
         */
        public ReloadThread(long reloadInterval) {
            final String LOGINFO = LogInfo.getLogInfo ("ManageEvents.ReloadThread", "constructor" );
            if (reloadInterval < 5)
            {
                Debug.info ( LOGINFO + "Parameter reloadInterval passed as [" + reloadInterval + "].");
                Debug.info ( LOGINFO + "As it is less than 5 minutes, setting reloadInterval to 5.");
                this.reloadInterval = MSEC_PER_MINUTE * 5;
            }
            else
                this.reloadInterval = MSEC_PER_MINUTE * reloadInterval;
        }

        /**
         * run method
         */
        public void run()
        {
            final String LOGINFO = LogInfo.getLogInfo ("ManageEvents.ReloadThread", "run" );
            try
            {
                do
                {
                    Debug.info ( LOGINFO + "Thread is about to sleep for interval [" + reloadInterval + "] msec i.e [" + (reloadInterval / MSEC_PER_MINUTE) + "] mins.");
                    Thread.sleep(reloadInterval);
                    Debug.info ( LOGINFO + "Thread is awake after interval [" + reloadInterval + "] msec i.e [" + (reloadInterval / MSEC_PER_MINUTE) + "] mins.");
                    if (Thread.currentThread().isInterrupted())
                    {
                        Debug.info ( LOGINFO + "Thread interrupted. Breaking loop...");
                        break;
                    }
                    Debug.info (LOGINFO + "Reloading events properties...");
                    init ();
                    Debug.info (LOGINFO + "Reloaded successfully.");
                } while (true);
            }
            catch (Exception e)
            {
                String msg = LOGINFO + e.getMessage ();
                Debug.error (msg, e);
            }
        }
    }

    /**
     * test method
     *
     * @param args test args
     * @throws EventException on error
     */
    public static void main ( String[] args ) throws EventException
    {
        String[][] str = getEvents ();
        for (int i = 0; i < str.length; i++)
        {
            String[] strings = str[i];
            for (int j = 0; j < strings.length; j++)
            {
                String string = strings[j];
                System.out.println ( "string = " + string );
            }
        }
        Event bean = new Event ();
        bean.setEvent_desc ( "test" );
        bean.setEvent_date ( "02-Feb-2007" );
        bean.setEvent_id ( 4 );
        bean.setReceiver_email ( "test@test.com,test@test.com" );
        bean.setEvent_type ( "birth-date" );
        bean.setUser_name ( "test" );
        handleEvent ( bean );
    }
}