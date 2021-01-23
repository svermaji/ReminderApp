package com.svws.common.log;

import org.apache.log4j.Level;

/**
 * Created by
 * User: sverma
 * Date: Feb 22, 2006
 * Time: 1:21:55 PM
 */
/**
 * A class which represents the different debug levels available for logging.
 * The following levels are available :
 * ALL, INFO, WARNING, ERROR
 */
public final class DebugLevel extends Level
  {
    public static final Level ALL = new DebugLevel(0, "ALL", 0);

    public static final Level INFO = new DebugLevel(1, "INFO", 1);
    public static final Level WARNING = new DebugLevel(2, "WARNING", 2);
    public static final Level ERROR = new DebugLevel(3, "ERROR", 3);

    /**
     * Creates a new <code>DebugLevel</code> instance.
     *
     * @param level The int value for this level
     * @param levelStr The string value for this level
     * @param syslogString The syslog string for this level
     */
    protected DebugLevel(int level, String levelStr, int syslogString)
    {
      super(level, levelStr, syslogString);
    }
}