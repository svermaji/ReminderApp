package com.svws.web.blogs;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.utils.ProcessingException;

/**
 * Exception class for Blogs.
 */
public class BlogsException extends ProcessingException
{
    /**
     * Constructor with string message.
     *
     * @param msg String
     */
    public BlogsException (String msg)
    {
        super(msg);
        final String LOGINFO = LogInfo.getLogInfo ("BlogsException", "String" );
        Debug.error ( LOGINFO + msg );
    }

    /**
     * Constructor with exception object.
     *
     * @param e of type exception
     */
    public BlogsException (Exception e)
    {
        super(e);
        final String LOGINFO = LogInfo.getLogInfo ("BlogsException", "Exception" );
        Debug.error ( LOGINFO + e.toString () );
    }
}
