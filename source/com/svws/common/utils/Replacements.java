package com.svws.common.utils;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;

import java.util.Properties;

/**
 * replacement utility
 */
 
public class Replacements
{
    static Replacements replacement = null;
    static Properties props = null;

    public static Replacements getInstance()
    {
        final String LOGINFO = LogInfo.getLogInfo ( "Replacements", "getInstance");
        try
        {
            if (props == null)
            {
                props = ReadSource.getResourceProperties (ServletConstants.REPLACEMENT_PROPERTIES, true);
                replacement = new Replacements ();
            }
        }
        catch (ProcessingException e)
        {
            Debug.error (LOGINFO + e.getMessage (), e );
        }
        catch (Exception e)
        {
            Debug.error (LOGINFO + e.getMessage (), e );
        }

        return replacement;
    }

    public String getReplacement (String str)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "Replacements", "getReplacement");
        String val = str;
        if (StringUtils.hasValue(str))
        {
            if (props.containsKey(str))
                val = (String) props.get(str);
        }

        Debug.info ( LOGINFO + "Returning replacement as [" + val + "] for string [" + str + "]" );
        return val;
    }
}