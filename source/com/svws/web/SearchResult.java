package com.svws.web;


import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.utils.ServletConstants;
import com.svws.common.utils.ProcessingException;
import com.svws.common.utils.StringUtils;
import com.svws.web.didYouKnow.DidYouKnow;
import com.svws.web.poetry.Poetry;
import com.svws.web.englishFacts.EnglishFacts;
import com.svws.web.imdb.Imdb;
import com.svws.web.blogs.BlogsList;
import com.svws.web.idioms.Idioms;
import com.svws.email.EventsEmailGenerator;

import java.util.*;
import java.io.File;

/**
 * Created by:
 * User: sverma
 * Date: Aug 4, 2005
 * Time: 4:02:22 PM
 */

/**
 * This class has static methods that generates
 * the code for displaying the Search Result, Delete Result
 * with Header and Query etc
 * on the Graphical User Interface
 */
public final class SearchResult
{
    /**
     * This method takes the vector and context name
     * as a parameter and return the string that needs to be
     * build for search result display

     * @param headings - Column headings
     * @param headingWidth - Column width in percentage
     * @param vRecords - Records to display
     * @param strContext - path
     * @param deleteFunction - js function
     * @param editFunction - js function
     * @param addPath - jsp path
     * @param aliases - 2-d string array with first as display value and second as value
     * @param imgColumns - ArrayList of columns for which image will be displayed instead of data (values are column position)
     * @param zeroRedOneGreen - if true then for value zero '0' Red ball image will be displayed and for '1' green else vice versa.
     * @param hightlightRowForCol - if this int has non-negative value then if the value of this column matched with current date then that row will be highlighted else not
     * @return String
     */
    public static String getSearchResultCode(String[] headings, String[] headingWidth, Vector vRecords, String strContext, String deleteFunction, String editFunction, String addPath, String[][] aliases, ArrayList imgColumns, boolean zeroRedOneGreen, int hightlightRowForCol)
    {
        final String LOGINFO = LogInfo.getLogInfo ("SearchResult", "getSearchResultCode" );
        StringBuffer innerHTML = new StringBuffer("");
        boolean highlightRow = hightlightRowForCol >= 0;
        int dateDiff = -1;
        try
        {
            int headingLength = headings.length;
            innerHTML.append("<table width='80%' border=1 align='center' class='searchTable'>\n");
            innerHTML.append("<tr>\n");
            innerHTML.append("<td align='right' colspan='").append(headingLength).append("'>");
            innerHTML.append("<br><span style='margin-right: 100pt;'>");
            innerHTML.append("<input class='button' name='submit' type='button' onClick='window.location = \"").append(addPath).append("\"' value='Add New'>\n");
            innerHTML.append("</span>").append("</td>").append("</tr>").append("\n");
            innerHTML.append("<tr>\n");
            for (int item=0; item < headingLength; item++)
            {
                innerHTML.append("<td class='searchTableHeading'");
                if (headingWidth != null)
                    innerHTML.append(" width='").append(headingWidth[item]).append("%'");
                innerHTML.append(">").append(headings[item]).append("</td>\n");
            }
            innerHTML.append("</tr>\n");
            int rows = vRecords.size();
            int cols = 0;
            if (rows > 0) cols = ((Vector) (vRecords.get(0))).size();
            Debug.info ( LOGINFO+"Rows are [" + rows + "], Cols are [" + cols + "]");
            Vector vTempRecord;
            for (int records = 0; records < rows; records++)
            {
                vTempRecord = (Vector) (vRecords.get(records));
                Debug.info ( LOGINFO+"vTempRecord.size() is [" + vTempRecord.size() + "]");

                StringBuffer rowHTML = new StringBuffer();
                innerHTML.append("<tr class='").append(getSearchCSSClass(records)).append("'>\n");
                rowHTML.append("<td class='searchTableData' align='center'>").append(records + 1).append(".</td>");
                rowHTML.append("<td class='searchTableData' align='center'>");
                rowHTML.append("<A href='#' class='searchLink'><img src='").append(strContext).append("/images/delete.gif").append("' border=0 onClick='").append(deleteFunction).append("(\"").append(vTempRecord.get(0).toString()).append("\")'></A></td>\n");
                rowHTML.append("<td class='searchTableData' align='center'>");
                rowHTML.append("<A href='#' class='searchLink'><img src='").append(strContext).append("/images/edit.gif").append("' border=0 onClick='").append(editFunction).append("(\"").append(vTempRecord.get(0).toString()).append("\")'></A></td>\n");

                boolean highlight = false;
                for (int vals = 1; vals < cols; vals++)
                {
                    boolean displayImg = (imgColumns != null) && imgColumns.contains ( new Integer (vals) );
                    String cellValue = vTempRecord.get(vals).toString();

                    if (highlightRow && hightlightRowForCol == vals && !highlight) // this must be date column
                    {
                        dateDiff = EventsEmailGenerator.getDateDiff(cellValue);
                        if (dateDiff >= 0 && dateDiff <=3 ) highlight = true;
                        Debug.info ( LOGINFO + "highlight [" + highlight + "]");
                    }

                    if ( displayImg )
                    {
                        rowHTML.append("<td class='searchTableData' align='center'>");
                        if (StringUtils.hasValue ( cellValue ))
                        {
                            int val;
                            try
                            {
                                val = Integer.parseInt ( cellValue );
                            }
                            catch (NumberFormatException e)
                            {
                                val = 1;
                                Debug.warning ( LOGINFO + "Error while retrieving value for Image column. Considering 1 as default value." + e.getMessage ());
                            }
                            catch (Exception e)
                            {
                                val = 1;
                                Debug.warning ( LOGINFO + "Error while retrieving value for Image column. Considering 1 as default value." + e.getMessage ());
                            }
                            
                            if (val == 1 && zeroRedOneGreen)
                                rowHTML.append("<img src='").append(strContext).append("/images/green-ball.gif").append("' border=0 ").append("</td>\n");
                            else
                                rowHTML.append("<img src='").append(strContext).append("/images/red-ball.gif").append("' border=0 ").append("</td>\n");
                        }
                    }
                    else
                    {
                        rowHTML.append("<td class='searchTableData' align='left'>");
                        if (vTempRecord.get(vals).toString().length() != 0)
                            rowHTML.append(getAlias(cellValue, aliases));
                        else
                            rowHTML.append("&nbsp;");
                    }
                    rowHTML.append("</td>\n");
                }

                if (highlight) innerHTML.append(highLightRow(rowHTML.toString(), dateDiff));
                else innerHTML.append(rowHTML);

                innerHTML.append("</tr>\n");
            }
            innerHTML.append("<tr>\n");
            innerHTML.append("<td align='right' colspan='").append(headingLength).append("'>");
            innerHTML.append("<br><span style='margin-right: 100pt;'>");
            innerHTML.append("<input class='button' name='submit' type='button' onClick='window.location = \"").append(addPath).append("\"' value='Add New'>\n");
            innerHTML.append("</span>").append("</td>").append("</tr>").append("</table>\n");
            Debug.info ( LOGINFO+"Rows are [" + rows + "], Cols are [" + cols + "]");
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e);
        }
        return innerHTML.toString();
    }

    public static String getIdiomsHtml(String ctx, String letter)
    {
        final String LOGINFO = LogInfo.getLogInfo ("SearchResult", "getIdiomsHtml" );
        StringBuffer innerHTML = new StringBuffer("");

        if (!StringUtils.hasValue(letter))
            letter = "A";

        boolean isAll = letter.equals("All");
        

        try
        {
            innerHTML.append("<table class='searchTableWB' width='100%'><tr><td align='center'><a name=\"top\">Show Idioms</a>: ");

            if (!isAll)
                innerHTML.append("<a onMouseOver=\"this.style.textDecoration='underline';\" ")
                         .append("onMouseOut=\"this.style.textDecoration='none';\" ")
                         .append("class=\"menuItem\" href=\"idioms.jsp?letter=All\">");

            innerHTML.append("All");

            if (!isAll)
                innerHTML.append("</a>");

            innerHTML.append("&nbsp;");

            for (char i = 'A'; i <= 'Z'; i++)
            {
                String s = i + "";
                boolean isLetter = letter.equals(s);
                if (!isLetter)
                    innerHTML.append("<a onMouseOver=\"this.style.textDecoration='underline';\" ")
                         .append("onMouseOut=\"this.style.textDecoration='none';\" ")
                         .append("class=\"menuItem\" href=\"idioms.jsp?letter=").append(i).append("\">");

                innerHTML.append(i);

                if (!isLetter)
                    innerHTML.append("</a>");

                innerHTML.append("&nbsp;");
            }
            innerHTML.append("</td></tr></table><br><br>");
            if (letter.equals("All"))
            {
                innerHTML.append("<table class='searchTableWB' width='80%' align=\"center\"><tr><td>Directly go to: ");
                for (char i = 'A'; i <= 'Z'; i++)
                {
                    innerHTML.append("<a onMouseOver=\"this.style.textDecoration='underline';\" ")
                         .append("onMouseOut=\"this.style.textDecoration='none';\" ")
                         .append("class=\"menuItem\" href=\"#").append(i).append("\">").append(i).append("</a>&nbsp;");
                }
                innerHTML.append("</td></tr></table><br>");
            }

            Idioms idioms = Idioms.getInstance();
            TreeMap allIdioms = idioms.getIdioms();
            Iterator itar = allIdioms.keySet ().iterator ();
            int row = 0, totalIdioms = 0;

            while (itar.hasNext())
            {
                String key = (String) itar.next();
                if (!isAll && !key.equals(letter))
                    continue;
                Idioms.Idiom[] obj = (Idioms.Idiom[]) allIdioms.get(key);

                innerHTML.append("<table width='80%' border=1 align='center' class='searchTable'>\n");
                innerHTML.append("<tr>\n");
                innerHTML.append("<td class='searchTableHeading' colspan=\"3\"").append(">Idioms for character '<a name=\"").append(key).append("\">").append(key).append("</a>'");
                if (isAll)
                    innerHTML.append("&nbsp; (<a onMouseOver=\"this.style.textDecoration='underline';\" ")
                             .append("onMouseOut=\"this.style.textDecoration='none';\" ")
                             .append("class=\"menuInHeading\" href=\"#top\">Go Top</a>)");
                innerHTML.append("</td>\n");

                innerHTML.append("<tr>\n");
                innerHTML.append("<td class='searchTableHeading' width=\"5%\"").append(">#</td>\n");
                innerHTML.append("<td class='searchTableHeading' width=\"25%\"").append(">Idiom</td>\n");
                innerHTML.append("<td class='searchTableHeading' width=\"70%\"").append(">Description</td></tr>\n");
                totalIdioms += obj.length; 

                for (int i = 0; i < obj.length; i++)
                {
                    Idioms.Idiom idiom = obj[i];

                    innerHTML.append("<tr class='").append(getSearchCSSClass(row++)).append("'>\n");
                    innerHTML.append("<td class='searchTableData' align='left'>").append(i+1).append(".</td>");
                    innerHTML.append("<td class='searchTableData' align='left'>").append(idiom.getIdiom()).append(".</td>");
                    innerHTML.append("<td class='searchTableData' align='left'>").append(idiom.getDescription()).append(".</td></tr>");
                }
                innerHTML.append("</table><br><br>");
            }
            innerHTML.append("Total Idioms: ").append(totalIdioms);
            innerHTML.append("&nbsp; (<a onMouseOver=\"this.style.textDecoration='underline';\" ")
                     .append("onMouseOut=\"this.style.textDecoration='none';\" ")
                     .append("class=\"menuItem\" href=\"#top\">Go Top</a>)");
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e);
        }
        return innerHTML.toString();
    }

    private static String highLightRow(String rowHTML, int dateDiff)
    {
        String TD_START = "<td";
        String TD_END = "</td>";
        String GT = ">";
        StringBuffer sb = new StringBuffer();
        while (rowHTML.indexOf(TD_START) > -1)
        {
            sb.append(rowHTML.substring(0, rowHTML.indexOf(TD_START)));
            rowHTML = rowHTML.substring(rowHTML.indexOf(TD_START));
            sb.append(rowHTML.substring(0, rowHTML.indexOf(GT) + GT.length()));
            rowHTML = rowHTML.substring(rowHTML.indexOf(GT) + GT.length());
            sb.append("<b>");
            if (dateDiff == 0) sb.append("<i>");
            sb.append(rowHTML.substring(0, rowHTML.indexOf(TD_END)));
            rowHTML = rowHTML.substring(rowHTML.indexOf(TD_END));
            sb.append("</b>");
            if (dateDiff == 0) sb.append("</i>");
        }
        sb.append(TD_END);
        return sb.toString();
    }

    private static String getAlias ( String value, String[][] aliases )
    {
        return getAlias ( value, aliases, 25 );     
    }

    private static String getAlias ( String value, String[][] aliases, int checkLen )
    {
        String returnStr = value;
        if (aliases != null)
        {
            int aliasesLen = aliases.length;
            for (int elem = 0; elem < aliasesLen; elem++)
            {
                String[] alias = aliases[elem];
                if (alias[0].equalsIgnoreCase ( value ))
                {
                    returnStr = alias [1];
                    break;
                }
            }
        }
        returnStr = checkLen (returnStr, checkLen);
        return returnStr;
    }

    private static String checkLen ( String str, int checkLen )
    {
        if (str.length () > checkLen) str = str.substring (0, checkLen) + "...";
        return str;
    }

    /**
     * This class returns the CSS class name
     * since the every alternate row needs to
     * be in different color and on Mouse Over
     * it shows a specific color effect
     *
     * @param row int
     * @return String - CSS name
     */
    private static String getSearchCSSClass(int row)
    {
        if (row % 2 == 1) return "searchTableRowColor1";
        return "searchTableRowColor2";
    }

    public static String makeLinks (String dirName, int linksPerLine, String strCtx)
    {
        return makeLinks (dirName, linksPerLine, strCtx, "openWindow", "", "");
    }
    
    public static String makeLinks (String dirName, int linksPerLine, String strCtx, String editActionForUser, String deleteActionForUser)
    {
        return makeLinks (dirName, linksPerLine, strCtx, "openWindow", editActionForUser, deleteActionForUser);
    }

    public static String makeLinks (String dirName, int linksPerLine, String strCtx, String jsFuncName, String editActionForUser, String deleteActionForUser)
    {
        if (!StringUtils.hasValue(jsFuncName)) jsFuncName = "openWindow";
        return makeLinks (dirName, linksPerLine, strCtx, jsFuncName, editActionForUser, deleteActionForUser, "ALL");
    }
    public static String makeLinks (String dirName, int linksPerLine, String strCtx, String jsFuncName, String editActionForUser, String deleteActionForUser, String resultsForUser)
    {
        final String LOGINFO = LogInfo.getLogInfo ("SearchResult", "makeLinks" );
        String html = null;
        try
        {
            Debug.info (LOGINFO + "Coming params are: dirName as [" + dirName + "], linksPerLine as [" + linksPerLine + "] and strCtx as [" + strCtx + "]");
            html = "";

            if (linksPerLine < 4 && linksPerLine > 10)
                linksPerLine = 4;

            if (dirName.equals (ServletConstants.DID_YOU_KNOW))
            {
                html = populateFileNames(DidYouKnow.getInstance ().getDidYouKnowFiles (), ServletConstants.DYK_FILE_PREFIX, strCtx, dirName, linksPerLine, ServletConstants.DID_YOU_KNOW_TITLE, jsFuncName);
            }
            if (dirName.equals (ServletConstants.ENGLISH_FACTS))
            {
                html = populateFileNames(EnglishFacts.getInstance ().getEnglishFactsFiles (), ServletConstants.ENGLISH_FILE_PREFIX, strCtx, dirName, linksPerLine, ServletConstants.ENGLISH_TITLE, jsFuncName);
            }
            if (dirName.equals (ServletConstants.POETRY))
            {
                html = populateFileNames(Poetry.getInstance ().getPoetryFiles (), ServletConstants.POETRY_FILE_PREFIX, strCtx, dirName, linksPerLine, ServletConstants.POETRY_TITLE, jsFuncName);
            }
            if (dirName.equals (ServletConstants.IMDB))
            {
                html = populateFileNames(Imdb.getInstance ().getImdbFiles (), ServletConstants.IMDB_FILE_PREFIX, strCtx, dirName, linksPerLine, ServletConstants.IMDB_TITLE, jsFuncName);
            }
            if (dirName.equals (ServletConstants.BLOGS))
            {
                html = populateFileNames(BlogsList.getInstance ().getBlogsListFiles (resultsForUser), ServletConstants.BLOGS_FILE_PREFIX, strCtx, dirName, linksPerLine, ServletConstants.BLOGS_TITLE, jsFuncName, editActionForUser, deleteActionForUser);
            }
        }
        catch (ProcessingException e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e);
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e);
        }
        return html;
    }

    private static String populateFileNames(TreeMap files, String filePrefix, String strCtx, String dirName, int linksPerLine, String title, String jsFunctionName)
    {
        return populateFileNames(files, filePrefix, strCtx, dirName, linksPerLine, title, jsFunctionName, "", "");
    }

    /**
     * This function prepars link.
     * 
     * @param files names whose links to be prepared
     * @param filePrefix prefix of the file
     * @param strCtx context as string
     * @param dirName directory name
     * @param linksPerLine to be displayed per row
     * @param title of the page
     * @param jsFunctionName java script function name to be used
     * @param editActionForUser adds addEditFunc action only for present user if this is true
     * @param deleteActionForUser adds addDeleteFunc action only for present user if this is true
     * @return String as html
     */
    private static String populateFileNames(TreeMap files, String filePrefix, String strCtx, String dirName, int linksPerLine, String title, String jsFunctionName, String editActionForUser, String deleteActionForUser)
    {
        final String LOGINFO = LogInfo.getLogInfo ("SearchResult", "populateFileNames" );
        Debug.info (LOGINFO + "Coming params are: files as [" + files
                + "], filePrefix as [" + filePrefix
                + "], strCtx as [" + strCtx
                + "], dirName as [" + dirName
                + "], linksPerLine as [" + linksPerLine
                + "], title as [" + title
                + "], jsFunctionName as [" + jsFunctionName
                + "], editActionForUser as [" + editActionForUser
                + "] and deleteActionForUser as [" + deleteActionForUser
                + "]");
        String DOT = ".";
        StringBuffer sb = new StringBuffer ();

        try
        {
            Iterator itar = files.keySet ().iterator();
            int elem = 1;
            boolean newLine = false;
            while (itar.hasNext ())
            {
                // strFile will work as key
                String strFile = (String) itar.next ();
                // file will work as object i.e. File object
                File file = (File) files.get(strFile);
                String origFileName = strFile;
                strFile = strFile.substring (filePrefix.length (), strFile.lastIndexOf (DOT));
                strFile = strFile.substring (0, 1).toUpperCase () + strFile.substring (1).toLowerCase ();
                strFile = strFile.replaceAll ("-", " ");
                Debug.info (LOGINFO + "making link for file [" + origFileName + "] as [" + strFile + "]");
                if (elem != 1 && !newLine)
                    sb.append ("<span class=\"impInfo\">&nbsp;*&nbsp;&nbsp;</span>");

                sb.append ("<SPAN class=\"menuItem\" ")
                        .append ("onMouseOver=\"this.style.textDecoration='underline';\" ")
                        .append ("onMouseOut=\"this.style.textDecoration='none';\" ")
                        .append ("onClick=\"").append (jsFunctionName).append ("('").append (strCtx)
                        .append ("', '").append (dirName).append ("', '").append (encodeSpecialChar(origFileName))
                        .append ("', '").append (encodeSpecialChar(title)).append ("')\">")
                        .append (getAlias (strFile, null, 80)).append ("</SPAN>&nbsp;");
                if (StringUtils.hasValue(editActionForUser) && strFile.toLowerCase().startsWith(editActionForUser.toLowerCase()))
                    sb.append("<A href='#' class='searchLink'><img src='").append(strCtx).append("/images/edit.gif").append("' border=0 onClick='").append("editBlog").append("(\"").append(file.toURI().toURL()).append("\")'></A>\n");
                    //sb.append("<A href='#' class='searchLink'><img src='").append(strCtx).append("/images/edit.gif").append("' border=0 onClick='").append("editBlog").append("(\"").append(encodeUrl (file.toURI().toURL().toString())).append("\")'></A>\n");
                if (StringUtils.hasValue(deleteActionForUser) && strFile.toLowerCase().startsWith(deleteActionForUser.toLowerCase()))
                    sb.append("<A href='#' class='searchLink'><img src='").append(strCtx).append("/images/delete.gif").append("' border=0 onClick='").append("deleteBlog").append("(\"").append(file.toURI().toURL().toString()).append("\")'></A>\n");
                sb.append("\n\n");
                newLine = false;
                if (elem % linksPerLine == 0)
                {
                    sb.append ("<br><br><br>");
                    newLine = true;
                }
                elem++;
            }
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e);
        }
        return sb.toString ();
    }

/*
    private static String encodeUrl(String value)
    {
        final String LOGINFO = LogInfo.getLogInfo ("SearchResult", "encodeUrl" );
        Debug.info (LOGINFO + "value to encode for url [" + value + "].");
        value = StringUtils.replaceSubstrings ( value, "'", "\'");
        Debug.info (LOGINFO + "value after encoding is [" + value + "]");
        
        return value;
    }
*/

    /**
     * URL encode any special characters found in the input string.
     *
     * @param  value  The string which may contain special characters.
     * @return  New string with all special characters URL encoded.
     */
    private static String encodeSpecialChar(String value)
    {
        String newValue = value;

        if (newValue.indexOf("%") != -1)
        {
            newValue = StringUtils.replaceSubstrings ( newValue, "%", "%25");
        }

        if (newValue.indexOf("&") != -1)
        {
            newValue = StringUtils.replaceSubstrings ( newValue, "&", "%26");
        }

        if (newValue.indexOf("'") != -1)
        {
            newValue = StringUtils.replaceSubstrings ( newValue, "'", "%27");
        }

        if (newValue.indexOf("\"") != -1)
        {
            newValue = StringUtils.replaceSubstrings ( newValue, "\"", "%22");
        }

        if (newValue.indexOf("=") != -1)
        {
            newValue = StringUtils.replaceSubstrings ( newValue, "=", "%3D");
        }

        return newValue;
    }

    public static void main(String[] args)
    {
        makeLinks (ServletConstants.DID_YOU_KNOW, 4, "http://shailu:8089/svWebSite");
    }

}
