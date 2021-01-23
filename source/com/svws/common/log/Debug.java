package com.svws.common.log;

import java.util.Properties;
import java.io.*;

import com.svws.common.utils.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.RootLogger;

/**
 * Created by
 * User: sverma
 * Date: Feb 21, 2006
 * Time: 11:31:16 AM
 */
/**
 * This class is being to log the information
 * used for debugging purpose
 */
public class Debug
{

	private static final Logger logger = Logger.getLogger(Debug.class.getName());

    //private static Thread reloadThread;
    //private static Writer logWriter = null;
    public static final String WARNING = "WARNING";
    public static final String ERROR = "ERROR";
    public static final String INFO = "INFO";
    //private static String fullPath = "";

    static
    {
        // This will configure console appender by default
        BasicConfigurator.configure();
    }

    /**
     * Initialized the logger with file name.
     *
     * @throws IOException on error
     * @throws ProcessingException on error
     */
    public static void initialize ( ) throws IOException, ProcessingException
    {
        Properties log4jProps = ReadSource.getResourceProperties(ServletConstants.LOG_FILE_PROPS_LOC, true);

        boolean needConsoleAppender = log4jProps.size() == 0;

        Level level = Level.ALL;
		if(Logger.getRootLogger()!=null)
        {
            RootLogger root = (RootLogger) Logger.getRootLogger();
            info ("root.getChainedLevel() = " + root.getChainedLevel());
            if(root.getChainedLevel()==null)
            {
                log4jProps.put("log4j.rootLogger", level.toString()+",NEWSVWS");
            }
            else
            {
                log4jProps.put("log4j.logger." + Debug.class.getName(), level.toString()+",NEWSVWS");
            }
        }
        else
        {
            info ("root logger null");
            log4jProps.put("log4j.rootLogger",level.toString()+",NEWSVWS");
        }

        if(needConsoleAppender)
            log4jProps.put("log4j.appender.NEWSVWS","org.apache.log4j.ConsoleAppender");

        BasicConfigurator.resetConfiguration();
        PropertyConfigurator.configure(log4jProps);

        warning("log4jProps = " + log4jProps);
    }


    /**
     * Overloaded method.
     *
     * @param message string to print
     */
    public static void info ( Object message )
    {
		//logger.info(message);
		write (Level.INFO, message);
    }

    /**
     * Overloaded method.
     *
     * @param message string to print
     */
    public static void warning ( Object message )
    {
        //logger.warn(message);
        write (Level.WARN, message);
    }

    /**
     * Overloaded method.
     *
     * @param message string to print
     */
    public static void error ( Object message )
    {
        //logger.error(message);
        error (message, null);
    }

    /**
     * Overloaded method.
     *
     * @param message string to print
     * @param t throwable obj to get stack trace
     */
    public static void error ( Object message, Throwable t )
    {
        //logger.error (message, t);
        write (Level.ERROR, message, t);
    }

    /**
     * Overloaded method.
     *
     * @param level Level obj
     * @param message string to log
     */
    public static void write ( Level level, Object message )
    {
        write (level, message, null);
    }

    /**
     * Writes the debug statement in log file.
     * If log file could not be initialized
     * thn output would be redirected to console.
     *
     * @param level Level ERROR or INFO
     * @param message - debug statement
     * @param t - throwable object
     */
    public static void write ( Level level, Object message, Throwable t)
    {
        logger.log (level, message, t);
    }

    /**
     * Returs stacktrace as string for throwable object.
     * In case throwable object is null returns empty string.
     *
     * @param t - throwable object
     * @return string
     */
/*
    private static String getStackTrace ( Throwable t )
    {
        if (t == null)
            return "";
        StringWriter sw = new StringWriter( );
        PrintWriter pw = new PrintWriter( sw );
        t.printStackTrace( pw );
        return( sw.toString() );
    }
*/


    /**
     * test.
     *
     * @param args string arr
     * @throws Exception on error
     */
    public static void main ( String[] args ) throws Exception
    {
        initialize ( );
        for (int i = 0; i < 20000; i++)
            write (Level.INFO, "Logging test statement.");
    }
}