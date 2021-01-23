package com.svws.common.utils;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;

import java.util.Properties;

/**
 * aliases utility
 */
 
public class Aliases
{
    static Aliases aliases = null;
    static Properties props = null;

    public static Aliases getInstance()
    {
        final String LOGINFO = LogInfo.getLogInfo ( "Aliases", "getInstance");
        try
        {
            if (props == null)
            {
                props = ReadSource.getResourceProperties (ServletConstants.ALIASES_PROPERTIES, true);
                aliases = new Aliases ();
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

        return aliases;
    }

    public String getAliases (String str)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "Aliases", "getAliases");
        String val = str;
        if (StringUtils.hasValue(str))
        {
            if (props.containsKey(str))
                val = (String) props.get(str);
        }

        Debug.info ( LOGINFO + "Returning aliases as [" + val + "] for string [" + str + "]" );
        return val;
    }
}