package com.svws.common.log;

/**
 * Created by
 * User: sverma
 * Date: Feb 21, 2006
 * Time: 5:16:51 PM
 */
/**
 * This class will take name of class and method
 * and provide a string used for logging purpose
 */
public class LogInfo
{
    public static String logInfo = "";

    public static String getLogInfo (Object object, String methodName)
    {
        return getLogInfo (object.getClass (), methodName);
    }

    public static String getLogInfo (Class classVar, String methodName)
    {
        return getLogInfo (classVar.getName (), methodName);
    }

    public static String getLogInfo (String className, String methodName)
    {
        if (className == null)
            className = "NoClass";
        if (methodName == null)
            methodName = "NoMethod";
        logInfo = className + "." + methodName + ": ";
        return logInfo;
    }
}
