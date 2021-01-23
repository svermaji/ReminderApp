package com.svws.api;

import org.apache.axis.client.Service;
import org.apache.axis.client.Call;

import java.net.URL;


class SVWSAPIClient
{
    private String sendRequest(String param) throws Exception
    {
        Service service = new Service();
        Call call    = (Call)service.createCall();
        call.setTargetEndpointAddress(new URL("http://192.168.100.86:9278/axis/services/SVWS"));
        call.setOperationName("getEvents");

        return (String)call.invoke(new Object[] {param});
    }

    public static void main(String[] args) throws Exception
    {
        SVWSAPIClient obj = new SVWSAPIClient();
        System.out.println(obj.sendRequest ("sv"));
    }
}