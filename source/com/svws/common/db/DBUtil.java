package com.svws.common.db;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.utils.StringUtils;
import com.svws.common.utils.ProcessingException;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class is a wrapper to DBServer class methods.
 *
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 23, 2007
 * Time: 4:02:53 PM.
 */
public class DBUtil
{
    private static final String SELECT = " SELECT ";
    private static final String INSERT = " INSERT INTO ";
    private static final String UPDATE = " UPDATE ";
    private static final String DELETE = " DELETE FROM ";

    private static final String VALUES = " VALUES ";
    private static final String FROM = " FROM ";
    private static final String SET = " SET ";

    private static final String WHERE = " WHERE ";

    private static final String ORDERBY = " ORDER BY ";
    private static final String DESC = " DESC ";
    private static final String ASC = " ASC ";

    private static final String AND = " AND ";
    private static final String EQUAL = " = ";


    private static final String SPACE = " ";
    private static final String COMMA = " , ";
    private static final String ASTERISK = " * ";
    //private static final String QUESTION_MARK = " ? ";
    private static final String LEFT_P = " ( ";
    private static final String RIGHT_P = " ) ";

    private static final String SINGLE_QUOTE = "'";
    //private static final String DOUBLE_QUOTE = " \" ";

    /**
     * Selects rows from database based on sql.
     *
     * @param sql string
     * @return Vector object of records
     * @throws DBException on error.
     */
    public static Vector selectRecords (String sql) throws DBException
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBUtil", "selectRecords" );
        Debug.info (LOGINFO + sql );
        return DBServer.getRecords ( sql );
    }

    /**
     * Selects rows from database based on parameters.
     *
     * @param table name of db table
     * @param columns name of db columns
     * @param where db where clause
     * @return Vector object of records
     * @throws DBException on error.
     */
    public static Vector selectRecords (String table, Vector columns, Hashtable where) throws DBException
    {
        return selectRecords (table, columns, where, null);
    }

    /**
     * Selects rows from database based on parameters.
     *
     * @param table name of db table
     * @param columns name of db columns
     * @param where db where clause
     * @param orderBy db order by clause
     * @return Vector object of records
     * @throws DBException on error.
     */
    public static Vector selectRecords (String table, Vector columns, Hashtable where, Vector orderBy) throws DBException
    {
        return selectRecords (table, columns, where, orderBy, true);
    }

    /**
     * Selects rows from database based on parameters.
     *
     * @param table name of db table
     * @param columns name of db columns
     * @param where db where clause
     * @param orderBy db order by clause
     * @param ascending boolean
     * @return Vector object of records
     * @throws DBException on error.
     */
    public static Vector selectRecords (String table, Vector columns, Hashtable where, Vector orderBy, boolean ascending) throws DBException
    {
        return selectRecords (prepareSQL (table, columns, where, orderBy, ascending));
    }

    /**
     * Since DB Results is a collection of Vectors inside Vector.
     * Converting all those into a String.
     *
     * @param recs collection of Vectors (DB Row) inside Vector
     * @return String
     * @throws ProcessingException on error
     */
    public static String getDBResultAsStr (Vector recs) throws ProcessingException
    {
        if (recs == null || recs.size() == 0)
            return "";

        StringBuffer dbResults = new StringBuffer();

        for (int row = 0; row < recs.size(); row++)
        {
            Vector rec = (Vector) recs.get(row);
            for (int col = 0; col < rec.size(); col++)
            {
                dbResults.append(rec.get(col));
                if (col != 0) dbResults.append(COMMA);
            }
            dbResults.append("\n");
        }

        return dbResults.toString();
    }

    /**
     * Prepares sql based on the basis of passed parameters.
     *
     * @param table name of db table
     * @param columns name of db columns
     * @param where db where clause
     * @param orderBy db order by clause
     * @param ascending boolean
     * @return string as sql
     */
    private static String prepareSQL ( String table, Vector columns, Hashtable where, Vector orderBy, boolean ascending )
    {
        String colName;

        StringBuffer columnString = new StringBuffer();


        if ( columns == null || columns.size() == 0 )
        {
            columnString.append(ASTERISK);
        }
        else
        {
            boolean firstField = true;

            Enumeration it = columns.elements();

            while ( it.hasMoreElements() )
            {
                if (firstField)
                {
                    firstField = false;
                }
                else
                {
                    columnString.append(COMMA);
                }
                colName = (String)it.nextElement();
                columnString.append(colName);
            }
        }


        StringBuffer sqlString = new StringBuffer();

        StringBuffer whereClause = getWhereClause(where);


        StringBuffer orderByBuf = new StringBuffer();
        if ( orderBy == null || orderBy.size() == 0 )
        {
            // No column to order.
        }
        else
        {
            orderByBuf.append(ORDERBY);

            boolean firstField  = true;
            Enumeration it = orderBy.elements();

            while ( it.hasMoreElements() )
            {
                if (firstField)
                {
                    firstField = false;
                }
                else
                {
                    orderByBuf.append(COMMA);
                }
                colName = (String)it.nextElement();
                orderByBuf.append(colName);
                if (ascending)
                {
                    orderByBuf.append(ASC);
                }
                else
                {
                    orderByBuf.append(DESC);
                }
            }
        }

        sqlString.append(SELECT);
        sqlString.append(columnString.toString());
        sqlString.append(FROM);
        sqlString.append(table);
        sqlString.append(SPACE);
        sqlString.append(whereClause.toString());
        sqlString.append(orderByBuf.toString());

        return sqlString.toString ();
    }

    /**
     * Prepare the where attributes.
     *
     * @param whereAttributes hashtable
     * @return StringBuffer as where attributes.
     */
    private static StringBuffer getWhereClause(Hashtable whereAttributes)
    {
        StringBuffer whereClause = new StringBuffer();

        if (whereAttributes == null)
        {
            return whereClause;
        }

        Enumeration it = whereAttributes.keys();

        if (! it.hasMoreElements())
        {
            return whereClause;
        }

        String colName;
        Object value;

        whereClause.append(WHERE);

        boolean firstField = true;

        while ( it.hasMoreElements() )
        {
            if (firstField)
            {
                firstField = false;
            }
            else
            {
                whereClause.append(AND); // Use AND to combine all fields.
            }
            colName = (String)it.nextElement();
            value = whereAttributes.get(colName);

            whereClause.append(colName);
            whereClause.append(EQUAL);
            whereClause.append(SINGLE_QUOTE).append(value).append(SINGLE_QUOTE);
        }

        return whereClause;

    }

    /**
     * Update the rows in database.
     *
     * @param table db table name
     * @param attributes columns
     * @param where db where clause
     * @return Vector object of records
     * @throws DBException on error.
     */
    public static boolean updateRows (String table, Hashtable attributes, Hashtable where) throws DBException
    {
        Enumeration it = attributes.keys();

        if (! it.hasMoreElements())
        {
            throw new DBException( "ERROR: SQL-Utilities: Update operation missing input attributes." );
        }

        String colName;
        Object value;

        StringBuffer columns = new StringBuffer();

        boolean firstField = true;

        while ( it.hasMoreElements() )
        {
            if (firstField)
            {
                firstField = false;
            }
            else
            {
                columns.append(COMMA);
            }
            colName = (String)it.nextElement();
            value = attributes.get(colName);

            columns.append(colName);
            columns.append(EQUAL);
            columns.append(SINGLE_QUOTE);
            columns.append(value);
            columns.append(SINGLE_QUOTE);
        }


        StringBuffer whereClause = getWhereClause(where);

        StringBuffer sqlString = new StringBuffer();

        sqlString.append(UPDATE);
        sqlString.append(table);
        sqlString.append(SET);
        sqlString.append(columns.toString());
        sqlString.append(whereClause.toString());

        return updateRecords (sqlString.toString());
    }

    /**
     * Update the rows in database.
     *
     * @param sql string
     * @return Vector object of records
     * @throws DBException on error.
     */
    public static boolean updateRecords (String sql) throws DBException
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBUtil", "updateRecords" );
        Debug.info (LOGINFO + sql );
        return DBServer.updateRecord ( sql );
    }

    /**
     * Delete the rows in database.
     *
     * @param table db table
     * @param where db where clause
     * @return Vector object of records
     * @throws DBException on error.
     */
    public static boolean deleteRows(String table, Hashtable where) throws DBException
    {
        StringBuffer whereClause = getWhereClause(where);
        StringBuffer sqlString = new StringBuffer();

        sqlString.append(DELETE);
        sqlString.append(table);
        sqlString.append(whereClause.toString());

        return updateRecords (sqlString.toString());
    }

    /**
     * Insert the rows in database.
     *
     * @param table db table name
     * @param attributes columns
     * @return Vector object of records
     * @throws DBException on error.
     */
    public static boolean insertRow (String table, Hashtable attributes) throws DBException
    {
        if (attributes == null)
        {
            throw new DBException ( "ERROR: SQL-Utilities: Insert operation missing input attributes." );
        }

        Enumeration it = attributes.keys();

        if (! it.hasMoreElements())
        {
            throw new DBException( "ERROR: SQL-Utilities: Insert operation missing input attributes." );
        }

        String colName;
        Object value;

        StringBuffer columns = new StringBuffer();
        StringBuffer colValues = new StringBuffer();

        boolean firstField = true;

        while ( it.hasMoreElements() )
        {
            if (firstField)
            {
                firstField = false;
            }
            else
            {
                columns.append(COMMA);
                colValues.append(COMMA);
            }
            colName = (String)it.nextElement();
            value = attributes.get(colName);

            columns.append(colName);
            colValues.append(SINGLE_QUOTE);
            colValues.append(value);
            colValues.append(SINGLE_QUOTE);

        }


        StringBuffer sqlString = new StringBuffer();

        sqlString.append(INSERT);
        sqlString.append(table);
        sqlString.append(LEFT_P);
        sqlString.append(columns.toString());
        sqlString.append(RIGHT_P);
        sqlString.append(VALUES);
        sqlString.append(LEFT_P);
        sqlString.append(colValues.toString());
        sqlString.append(RIGHT_P);

        return updateRecords (sqlString.toString());
    }

    /**
     * get next value from sequence.
     *
     * @param seqName name of db sequence
     * @return int as next value of sequence
     * @throws DBException on error
     */
    public static int getNextValOfSequence ( String seqName ) throws DBException
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBUtil", "getNextValOfSequence" );
        Vector vRec = DBServer.getRecords ( "SELECT " + seqName + ".NEXTVAL FROM DUAL");
        if (vRec == null)
            throw new DBException ( LOGINFO + "Could not find sequence [" + seqName + "].");

        Vector vRow = (Vector) vRec.get ( 0 );
        if (vRow == null)
            throw new DBException ( LOGINFO + "Unable to retrieve sequence [" + seqName + "] next value.");

        String val = (String) vRow.get ( 0 );
        if (!StringUtils.hasValue ( val))
            throw new DBException ( LOGINFO + "Next value for sequence [" + seqName + "] does not exists.");

        return Integer.parseInt ( val );
    }
}
