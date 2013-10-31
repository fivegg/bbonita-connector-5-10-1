package org.bonitasoft.connectors.ws.cxf.helloHeader;
import java.util.List;
import java.util.Map;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;

public class HttpHeaderInInterceptor extends AbstractSoapInterceptor {

    public HttpHeaderInInterceptor() {
        super(Phase.READ);
       
    }

    
    
    public void handleMessage(SoapMessage message) throws Fault {
    	  final Map headers =(Map) message.get( org.apache.cxf.message.Message.PROTOCOL_HEADERS );
    	  System.out.println("HTTP HEADERS : " + headers);
    	  
    	  final Map<String, List<String>> typedHeaders = (Map<String, List<String>>) headers;
    	  if (!typedHeaders.containsKey("testName")) {
    		  throw new Fault(new Exception("TestName not found"));
    	  }
    	  final List<String> testNameValue = typedHeaders.get("testName");
    	  if (testNameValue.size() != 1) {
    		  throw new Fault(new Exception("TestName contains more than 1 element"));
    	  }
    	  final String value = testNameValue.get(0);
    	  if (!"testValue".equals(value)) {
    		  throw new Fault(new Exception("TestName value is not equals to testValue: " + value));
    	  }
    	  
    }



  
    

}