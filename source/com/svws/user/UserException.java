package com.svws.user;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.db.DBException;

/**
 * Exception class for email events.
 */
public class UserException extends DBException
{
    /**
     * Constructor with string message.
     *
     * @param msg String
     */
    public UserException (String msg)
    {
        super(msg);
        final String LOGINFO = LogInfo.getLogInfo ("UserException", "String" );
        Debug.error ( LOGINFO + msg );
    }

    /**
     * Constructor with exception object.
     *
     * @param e of type exception
     */
    public UserException (DBException e)
    {
        super(e);
        final String LOGINFO = LogInfo.getLogInfo ("UserException", "DBException" );
        Debug.error ( LOGINFO + e.toString () );
    }
}
