package com.svws.common.utils;

import com.svws.common.log.LogInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 25, 2007
 * Time: 1:09:14 PM
 */
public final class DateUtils
{
    /**
     * Returns current date formatted in given format.
     *
     * @param format specified to convert date in.
     * @return String contains date in specified format
     * @throws ProcessingException on error
     */
    public static final String getFormattedDate ( String format ) throws ProcessingException
    {
        return getFormattedDate ( format, new Date() );
    }

    /**
     * Returns given date formatted in given format.
     *
     * @param format specified to convert date in.
     * @param date to format
     * @return String contains date in specified format
     * @throws ProcessingException on error
     */
    public static final String getFormattedDate ( String format, Date date ) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("DateUtils", "getFormattedDate" );

        if (!StringUtils.hasValue(format))
            throw new ProcessingException (LOGINFO + "Null format provided.");

        return new SimpleDateFormat(format).format ( date );
    }

    /**
     * Returns present date formatted in dd-MM-yyyy format.
     * @return String contains date in specified format
     */
    public static final String getFormattedDate () throws ProcessingException
    {
        return getFormattedDate ( new Date() );
    }

    /**
     * Returns given date formatted in dd-MM-yyyy format.
     *
     * @param date to format
     * @return String contains date in specified format
     * @throws ProcessingException on error
     */
    public static final String getFormattedDate ( Date date ) throws ProcessingException
    {
        return new SimpleDateFormat(UtilConstants.DATE_FORMAT_DD_MM_YYYY).format ( date );
    }

}
