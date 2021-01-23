/**
 * SVWSSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.svws.api;

public class SVWSSoapBindingSkeleton implements com.svws.api.SVWSAPIIntfc, org.apache.axis.wsdl.Skeleton {
    private com.svws.api.SVWSAPIIntfc impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getEvents", _params, new javax.xml.namespace.QName("", "getEventsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:SVWS", "getEvents"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getEvents") == null) {
            _myOperations.put("getEvents", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getEvents")).add(_oper);
    }

    public SVWSSoapBindingSkeleton() {
        this.impl = new com.svws.api.SVWSSoapBindingImpl();
    }

    public SVWSSoapBindingSkeleton(com.svws.api.SVWSAPIIntfc impl) {
        this.impl = impl;
    }
    public java.lang.String getEvents(java.lang.String in0) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getEvents(in0);
        return ret;
    }

}
