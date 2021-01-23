package com.svws.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.utils.ReadSource;

public class XMLParser
{
    DocumentBuilderFactory dbf;
    DocumentBuilder db;
    XMLParser xmlP;

    final String NAME = "name";

    public XMLParser ()
    {
        final String LOGINFO = LogInfo.getLogInfo ( "XMLParser", "Constructor");
        try
        {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            Debug.info ( LOGINFO + "Initialized." );
        }
        catch (ParserConfigurationException e)
        {
            Debug.error ( LOGINFO, e );
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO, e );
        }
    }

    public Document getDocument (String uri)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "XMLParser", "getDocument");
        Document doc = null;
        try
        {
            doc = db.parse(ReadSource.getResource(uri, false));
        }
        catch (SAXException e)
        {
            Debug.error ( LOGINFO, e );
        }
        catch (IOException e)
        {
            Debug.error ( LOGINFO, e );
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO, e );
        }
        return doc;
    }

    public NodeList getDocElemList(Document doc, String elementTagName)
    {
        return doc.getElementsByTagName(elementTagName);
    }

    public NodeList getElemList(Element elem, String elementTagName)
    {
        return elem.getElementsByTagName(elementTagName);
    }

    public String getElementName (Element elem)
    {
        return elem.getAttribute(NAME);
    }

    public String getElementName (Node node)
    {
        return getElementName ((Element) node);
    }

    public String getElementAttr (Element elem, String attr)
    {
        return elem.getAttribute(attr);
    }

    public String getElementAttr (Node node, String attr)
    {
        return getElementAttr ((Element) node, attr);
    }

    public boolean isElement(Node node)
    {
        return node instanceof Element;
    }

    public boolean isTextNode(Node node)
    {
        return node.getNodeType() == Node.TEXT_NODE;
    }

    /**
     * Returns an array containing the children of node.
     *
     * @param node  The node whose children are to be returned.
     *
     * @return The accessible children (elements) of node
     */
    public final Node[] getChildren(Node node, String name)
    {
        List children = new ArrayList();


        for (Node child = node.getFirstChild();child != null;
             child = child.getNextSibling())
        {
            if (child.getNodeType() == Node.ELEMENT_NODE) {

                if (name == null)
                    children.add(child);
                else if (name.equals(child.getNodeName()))
                    children.add(child);
            }

        }

        return (Node [])children.toArray(new Node[children.size()]);


    }

    public final String getText(Node node)
    {
      if ( node == null)
        return null;

      // If a TEXT node already exists under the parent node, set the value on it
      // instead of inserting a new TEXT node.
      NodeList nl = node.getChildNodes( );
      int len = nl.getLength( );

      // Walk the list,looking for a TEXT node.
      for ( int Ix = 0;  Ix < len;  Ix ++ ) {
        Node n = nl.item( Ix );

        // return  the existing TEXT node's value.
        if ( n.getNodeType() == Node.TEXT_NODE ) {
          return n.getNodeValue();
        }
      }

      return null;

    }

}