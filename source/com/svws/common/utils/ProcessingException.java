package com.svws.common.utils;

/**
 * Exception class for processing.
 */
public class ProcessingException extends Exception
{
    /**
     * Constructor with string message.
     *
     * @param msg String
     */
    public ProcessingException (String msg)
    {
        super(msg);
    }

    /**
     * Constructor with exception object.
     *
     * @param e of type exception
     */
    public ProcessingException (Exception e)
    {
        super(e);
    }
}