package com.svws.email;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.db.DBException;

/**
 * Exception class for email events.
 */
public class EmailException extends DBException
{
    /**
     * Constructor with string message.
     *
     * @param msg String
     */
    public EmailException (String msg)
    {
        super(msg);
        final String LOGINFO = LogInfo.getLogInfo ("EmailException", "String" );
        Debug.error ( LOGINFO + msg );
    }

    /**
     * Constructor with exception object.
     *
     * @param e of type exception
     */
    public EmailException (DBException e)
    {
        super(e);
        final String LOGINFO = LogInfo.getLogInfo ("EmailException", "DBException" );
        Debug.error ( LOGINFO + e.toString () );
    }
}
