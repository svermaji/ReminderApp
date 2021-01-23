/**
 * SVWSAPIIntfcServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.svws.api;

public class SVWSAPIIntfcServiceLocator extends org.apache.axis.client.Service implements com.svws.api.SVWSAPIIntfcService {

    public SVWSAPIIntfcServiceLocator() {
    }


    public SVWSAPIIntfcServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SVWSAPIIntfcServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SVWS
    private java.lang.String SVWS_address = "http://192.168.100.86:9278/axis/services/SVWS";

    public java.lang.String getSVWSAddress() {
        return SVWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SVWSWSDDServiceName = "SVWS";

    public java.lang.String getSVWSWSDDServiceName() {
        return SVWSWSDDServiceName;
    }

    public void setSVWSWSDDServiceName(java.lang.String name) {
        SVWSWSDDServiceName = name;
    }

    public com.svws.api.SVWSAPIIntfc getSVWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SVWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSVWS(endpoint);
    }

    public com.svws.api.SVWSAPIIntfc getSVWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.svws.api.SVWSSoapBindingStub _stub = new com.svws.api.SVWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getSVWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSVWSEndpointAddress(java.lang.String address) {
        SVWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.svws.api.SVWSAPIIntfc.class.isAssignableFrom(serviceEndpointInterface)) {
                com.svws.api.SVWSSoapBindingStub _stub = new com.svws.api.SVWSSoapBindingStub(new java.net.URL(SVWS_address), this);
                _stub.setPortName(getSVWSWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SVWS".equals(inputPortName)) {
            return getSVWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:SVWS", "SVWSAPIIntfcService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:SVWS", "SVWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SVWS".equals(portName)) {
            setSVWSEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
