package com.svws.city;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.db.DBException;

/**
 * Exception class for CityDistances.
 */
public class CityDistancesException extends DBException
{
    /**
     * Constructor with string message.
     *
     * @param msg String
     */
    public CityDistancesException (String msg)
    {
        super(msg);
        final String LOGINFO = LogInfo.getLogInfo ("CityDistancesException", "String" );
        Debug.error ( LOGINFO + msg );
    }

    /**
     * Constructor with exception object.
     *
     * @param e of type exception
     */
    public CityDistancesException (DBException e)
    {
        super(e);
        final String LOGINFO = LogInfo.getLogInfo ("CityDistancesException", "DBException" );
        Debug.error ( LOGINFO + e.toString () );
    }
}
