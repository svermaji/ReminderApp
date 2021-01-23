package com.svws.common.db;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;

/**
 * Exception class for database operations.
 */
public class DBException extends Exception
{
    /**
     * Constructor with string message.
     *
     * @param msg String
     */
    public DBException (String msg)
    {
        super(msg);
        final String LOGINFO = LogInfo.getLogInfo ("DBException", "String" );
        Debug.error ( LOGINFO + msg );
    }

    /**
     * Constructor with exception object.
     *
     * @param e of type exception
     */
    public DBException (Exception e)
    {
        super(e);
        final String LOGINFO = LogInfo.getLogInfo ("DBException", "Exception" );
        Debug.error ( LOGINFO + e.getMessage () );
    }
}
