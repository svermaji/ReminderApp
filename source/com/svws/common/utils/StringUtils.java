package com.svws.common.utils;

import com.svws.common.log.Debug;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 25, 2007
 * Time: 1:09:14 PM
 */
public final class StringUtils
{

    // Set of values that imply a true value.
    private static final String[] trueValues = {"Y", "YES", "TRUE", "T"};

    // Set of values that imply a false value.
    private static final String[] falseValues = {"N", "NO", "FALSE", "F"};

    /**
     * Encode the String for html purpose
     *
     * @param s String to encode
     * @return string
     */
    public static final String encodeForHtml( String s )
    {
        s = s.replaceAll("'", "\'");
        return s;
    }

    /**
     * Decode the String
     *
     * @param s String to decode
     * @return string
     */
    public static final String decodeForHtml( String s )
    {
        s = s.replaceAll("%20", " ");
        return s;
    }

    /**
     * return true if param has non-null value
     * @param item string value
     * @return boolean
     */
    public static final boolean hasValue ( String item )
    {
        return( (item != null) && (item.length() > 0) );
    }

    /**
     * return true if param has non-null value and not equals to "null"
     * @param item string value
     * @return boolean
     */
    public static final boolean hasValueNotNull ( String item )
    {
        return hasValue (item) && !item.equalsIgnoreCase("null");
    }

    /**
     * Test to see if string is non-null and has a non-zero length.
     * Also that it is not equals to NULL. Case is not considered.
     *
     * @param  item  String to test.
     *
     * @return  'true' if string is non-null and has a non-zero length, otherwise 'false'.
     */
    public static final boolean hasValue ( String item, boolean checkNULLString )
    {
        boolean status = hasValue ( item );

        if (checkNULLString && status && item.equalsIgnoreCase ( "NULL" ))
            status = false;

        return status;
    }

    /**
     * Replace all occurrences of the substring in the target string with replacement
     * string and return the transformed value.
     *
     * @param  target       Target string to transform.
     * @param  substr       String to replace in 'target'.
     * @param  replacement  String to use in replacement of 'substr'.
     *
     * @return  Transformed string.
     */
	public static final String replaceSubstrings ( String target, String substr, String replacement )
    {
        StringBuffer sb = new StringBuffer( );

        int substrLen = substr.length( );

        do
        {
            // Locate start of substring to be replaced.
            int startIndex = target.indexOf( substr );

            // If we can't find substring, we're done, so append remaining part of
            // target to transformed string.
            if ( startIndex == -1 )
            {
                sb.append( target );

                break;
            }

            // Append text from start of 'target' to start of 'substring' to new string.
            sb.append( target.substring( 0, startIndex ) );

            // Replace substring with that passed-in.
            sb.append( replacement );

            // Make new target equal to remaining part of target just past the substring we just replaced.
            target = target.substring( startIndex + substrLen );
        }
        while ( true );

        return( sb.toString() );
    }

    /**
    * Performs the same functionality as <code>replaceSubstrings</code>
    * except that case is ignored when looking for occurrences of
    * <code>substr</code> in <code>target</code>.
    */
    public static final String replaceSubstringsIgnoreCase
                                                  ( String target,
                                                   String substr,
                                                   String replacement )
    {

        StringBuffer sb = new StringBuffer( );

        int substrLen = substr.length( );
        String littleTarget = target.toLowerCase();
        String littleSub    = substr.toLowerCase();

        do
        {
            // Locate start of substring to be replaced.
            int startIndex = littleTarget.indexOf( littleSub );

            // If we can't find substring, we're done, so append remaining part of
            // target to transformed string.
            if ( startIndex == -1 )
            {
                sb.append( target );

                break;
            }

            // Append text from start of 'target' to start of 'substring' to new string.
            sb.append( target.substring( 0, startIndex ) );

            // Replace substring with that passed-in.
            sb.append( replacement );

            // Make new target equal to remaining part of target just past the substring we just replaced.
            target = target.substring( startIndex + substrLen );
            littleTarget = littleTarget.substring( startIndex + substrLen );
        }
        while ( true );

        return( sb.toString() );

    }

    /**
     * Replace all occurrences of the given word in the target string with replacement
     * string and return the transformed value.
     *
     * @param  target       Target string to transform.
     * @param  word         Word (or sub-string) to replace in 'target'.
     * @param  replacement  String to use in replacement of 'word'.
     *
     * @return  Transformed string.
     */
	public static final String replaceWord( String target, String word, String replacement ){
        StringBuffer sb = new StringBuffer( );
		// Split the target string into individual words
        String[] result = target.split("\\s");
		// Check each word, if it matches the word to be replaced, replace it with replacement string
        for (int x=0; x<result.length; x++){
            if(result[x].equals(word)) result[x] = replacement;
            sb.append(result[x]).append(" ");
        }
        return sb.toString().trim();
    }


    /**
     * Convert the string argument to its equivalent integer value.
     *
     * @param  value  String value containing an integer.
     *
     * @return  Integer equivalent of argument.
     *
     * @exception  ProcessingException  Thrown if argument is invalid.
     */
    public static int getInteger ( String value ) throws ProcessingException
    {
        if ( !StringUtils.hasValue( value ) )
        {
            throw new ProcessingException ( "ERROR: Can't convert a null/empty string value to an integer." );
        }

        try
        {
            return( Integer.parseInt( value.trim() ) );
        }
        catch ( NumberFormatException nfe )
        {
            throw new ProcessingException( "ERROR: Invalid integer value [" + value + "]:\n" + nfe.toString() );
        }
    }

    /**
     * Return the boolean equivalent of the string argument.
     *
     * @param  value  Value containing string representation of a boolean value.
     *
     * @return Boolean true/false depending on the value of the input.
     *
     * @exception Exception  Thrown if input does not have a valid value.
     */
    public static final boolean getBoolean ( String value ) throws Exception
    {
        if ( !StringUtils.hasValue( value ) )
        {
            throw new Exception( "ERROR: Can't convert a null/empty string value to a boolean." );
        }

        value = value.trim( );

        for ( int Ix = 0;  Ix < trueValues.length;  Ix ++ )
        {
            if ( value.equalsIgnoreCase( trueValues[Ix] ) )
                return true;
        }

        for ( int Ix = 0;  Ix < falseValues.length;  Ix ++ )
        {
            if ( value.equalsIgnoreCase( falseValues[Ix] ) )
                return false;
        }

        //Construct error message containing list of valid values
        StringBuffer validValues = new StringBuffer( );

        for ( int Ix = 0;  Ix < trueValues.length;  Ix ++ )
        {
            if ( Ix > 0 )
                validValues.append( ", " );

            validValues.append( trueValues[Ix] );
        }

        for ( int Ix = 0;  Ix < falseValues.length;  Ix ++ )
        {
            validValues.append( ", " );
            validValues.append( falseValues[Ix] );
        }

        throw new Exception ( "ERROR: Candidate boolean value [" + value
                                       + "] not in valid-value set [" + validValues.toString() + "]." );
    }


    /**
     * Return the boolean equivalent of the string argument.
     *
     * @param  value  Value containing string representation of a boolean value.
     *
     * @param defaultBool Default boolean to use if the value is empty
     * or if it is an invalid value.
     * @return Boolean true/false depending on the value of the input.
     *
     * @exception FrameworkException  Thrown if input does not have a valid value.
     */
    public static final boolean getBoolean ( String value , boolean defaultBool)
    {
        if ( !StringUtils.hasValue( value ) )
            return defaultBool;

        try {
            return getBoolean(value);
        }
        catch (Exception e) {
            Debug.warning("Invalid boolean value : " + e.getMessage() +", defaulting to " + defaultBool);
            return defaultBool;
        }
    }}
