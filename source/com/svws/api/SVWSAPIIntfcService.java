/**
 * SVWSAPIIntfcService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.svws.api;

public interface SVWSAPIIntfcService extends javax.xml.rpc.Service {
    public java.lang.String getSVWSAddress();

    public com.svws.api.SVWSAPIIntfc getSVWS() throws javax.xml.rpc.ServiceException;

    public com.svws.api.SVWSAPIIntfc getSVWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
