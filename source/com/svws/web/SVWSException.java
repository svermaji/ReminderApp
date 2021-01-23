package com.svws.web;

/**
 * Created by
 * User: sverma
 * Date: Nov 21, 2005
 * Time: 7:37:32 PM
 */

/**
 * Exception class for web events.
 */
public class SVWSException extends Exception
{
    //Nested Exception to hold wrapped exception.
    //private Throwable detail;

    /**
     * Constructor with string message.
     *
     * @param message String
     */
    public SVWSException(String message)
    {
		super(message);
    }

    /**
     * Constructor with exception object.
     *
     * @param e of type exception
     */
    public SVWSException(Exception e)
    {
		super(e.getMessage());
    }
}
