package com.svws.events;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.db.DBException;

/**
 * Exception class for email events.
 */
public class EventException extends DBException
{
    /**
     * Constructor with string message.
     *
     * @param msg String
     */
    public EventException (String msg)
    {
        super(msg);
        final String LOGINFO = LogInfo.getLogInfo ("EventException", "String" );
        Debug.error ( LOGINFO + msg );
    }

    /**
     * Constructor with exception object.
     *
     * @param e of type exception
     */
    public EventException (DBException e)
    {
        super(e);
        final String LOGINFO = LogInfo.getLogInfo ("EventException", "DBException" );
        Debug.error ( LOGINFO + e.toString () );
    }
}
