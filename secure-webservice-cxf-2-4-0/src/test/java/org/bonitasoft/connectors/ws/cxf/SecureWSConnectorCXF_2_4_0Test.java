package org.bonitasoft.connectors.ws.cxf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPBinding;

import junit.framework.TestCase;

public class SecureWSConnectorCXF_2_4_0Test extends TestCase {

	/*
  private static final String nsSchema = "http://www.weather.gov/forecasts/xml/DWMLgen/schema/DWML.xsd";
  private static final String soapSchema = "http://schemas.xmlsoap.org/soap/envelope/";
  private static final String xsiSchema = "http://www.w3.org/2001/XMLSchema-instance";
  private static final String encodingStyle = "http://schemas.xmlsoap.org/soap/encoding/";
	 */

	public void testCustomer() throws Exception {
		final Server server = new Server(9002);
		server.start();
		final StringBuilder request = new StringBuilder("");

		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:man=\"http://www.orangecaraibe.com/soa/v2/Interfaces/ManageCustomerOrderInternal\">");
		request.append("<soapenv:Header/>");
		request.append("<soapenv:Body>");
		request.append("  <man:executeStep>");
		request.append("    <!--Optional:-->");
		request.append("    <man:processStepId>7586</man:processStepId>");
		request.append("    <man:processStepDate>20110713</man:processStepDate>");
		request.append("  </man:executeStep>");
		request.append("</soapenv:Body>");
		request.append("</soapenv:Envelope>");

		final String response =
				execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:9002/Customer", "ManageCustomerOrderInternalImplService", "ManageCustomerOrderInternalImplPort", "http://hello.cxf.ws.connectors.bonitasoft.org/", null, "guest", "guest");
		assertTrue(response, response.contains("false"));
		server.stop();
	}


	public void testBasicHTTPAuth() throws Exception {
		final Server server = new Server(9002);
		server.start();
		final StringBuilder request = new StringBuilder("");
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spr=\"http://hello.cxf.ws.connectors.bonitasoft.org/\">");
		request.append(" <soapenv:Header/>");
		request.append(" <soapenv:Body>");
		request.append("    <spr:sayHi>");
		request.append("       <arg0>Rodrigue test</arg0>");
		request.append("    </spr:sayHi>");
		request.append(" </soapenv:Body>");
		request.append("</soapenv:Envelope>");

		final String response =
				execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:9002/HelloWorld", "HelloWorldImplService", "HelloWorldImplPort", "http://hello.cxf.ws.connectors.bonitasoft.org/", null, "guest", "guest");
		assertTrue(response, response.contains("Rodrigue test"));
		server.stop();
	}


	public void testHTTPHeaderOK() throws Exception {
		final Server server = new Server(9002);
		server.start();
		final StringBuilder request = new StringBuilder("");
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spr=\"http://hello.cxf.ws.connectors.bonitasoft.org/\">");
		request.append(" <soapenv:Header/>");
		request.append(" <soapenv:Body>");
		request.append("    <spr:sayHi>");
		request.append("       <arg0>Rodrigue test</arg0>");
		request.append("    </spr:sayHi>");
		request.append(" </soapenv:Body>");
		request.append("</soapenv:Envelope>");

		String headerName = "testName";
		String headerValue = "testValue";

		Map<String, List<String>>  requestHeaders = new HashMap<String, List<String>>();
		List<String> header = new ArrayList<String>();
		header.add(headerValue);
		requestHeaders.put(headerName, header);

		execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:9002/HelloHeader", "HelloHeaderImplService", "HelloWorldImplPort", "http://hello.cxf.ws.connectors.bonitasoft.org/", null, "guest", "guest",requestHeaders);


		server.stop();
	}
	
	public void testHTTPHeaderOK2() throws Exception {
		final Server server = new Server(9002);
		server.start();
		final StringBuilder request = new StringBuilder("");
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spr=\"http://hello.cxf.ws.connectors.bonitasoft.org/\">");
		request.append(" <soapenv:Header/>");
		request.append(" <soapenv:Body>");
		request.append("    <spr:sayHi>");
		request.append("       <arg0>Rodrigue test</arg0>");
		request.append("    </spr:sayHi>");
		request.append(" </soapenv:Body>");
		request.append("</soapenv:Envelope>");

		String headerName = "testName";
		String headerValue = "testValue";

		final List<List<Object>>  requestHeaders = new ArrayList<List<Object>>();
		List<Object> header = new ArrayList<Object>();
		requestHeaders.add(header);
		header.add(headerName);
		header.add(headerValue);
		

		execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:9002/HelloHeader", "HelloHeaderImplService", "HelloWorldImplPort", "http://hello.cxf.ws.connectors.bonitasoft.org/", null, "guest", "guest",requestHeaders);


		server.stop();
	}
	

	public void testHTTPHeaderKO() throws Exception {
		final Server server = new Server(9002);
		server.start();
		final StringBuilder request = new StringBuilder("");
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spr=\"http://hello.cxf.ws.connectors.bonitasoft.org/\">");
		request.append(" <soapenv:Header/>");
		request.append(" <soapenv:Body>");
		request.append("    <spr:sayHi>");
		request.append("       <arg0>Rodrigue test</arg0>");
		request.append("    </spr:sayHi>");
		request.append(" </soapenv:Body>");
		request.append("</soapenv:Envelope>");

		String headerName = "testName";
		String headerValue = "testValue2";

		Map<String, List<String>>  requestHeaders = new HashMap<String, List<String>>();
		List<String> header = new ArrayList<String>();
		header.add(headerValue);
		requestHeaders.put(headerName, header);

		try {
			execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:9002/HelloHeader", "HelloHeaderImplService", "HelloWorldImplPort", "http://hello.cxf.ws.connectors.bonitasoft.org/", null, "guest", "guest",requestHeaders);
			fail("This call should fail...");
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals("TestName value is not equals to testValue: testValue2", e.getMessage());
		}

		server.stop();
	}

	public void testReadTimeoutKO() throws Exception{

		//ms
		Long timeout = 10l;
		long timeToWait = 5000;

		final Server server = new Server(9002);
		server.start();
		final StringBuilder request = new StringBuilder("");
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spr=\"http://hello.cxf.ws.connectors.bonitasoft.org/\">");
		request.append(" <soapenv:Header/>");
		request.append(" <soapenv:Body>");
		request.append("    <spr:sayHi>");
		request.append("       <arg0>"+timeToWait+"</arg0>");
		request.append("    </spr:sayHi>");
		request.append(" </soapenv:Body>");
		request.append("</soapenv:Envelope>");

		long start = System.currentTimeMillis();
		try {
			execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:9002/HelloTimeout", "HelloWorldImplService", "HelloWorldImplPort", "http://hello.cxf.ws.connectors.bonitasoft.org/", null, "guest", "guest", timeout, -1);
			fail("This call should fail...");
		} catch (Exception e) {
			long timeToGetException = System.currentTimeMillis() - start;
			assertTrue(timeToGetException < timeToWait);
			//OK
		}	

		server.stop();
	}
	
	public void testReadTimeoutOK() throws Exception{

		//ms
		long timeout = 10000;
		long timeToWait = 2000;

		final Server server = new Server(9002);
		server.start();
		final StringBuilder request = new StringBuilder("");
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spr=\"http://hello.cxf.ws.connectors.bonitasoft.org/\">");
		request.append(" <soapenv:Header/>");
		request.append(" <soapenv:Body>");
		request.append("    <spr:sayHi>");
		request.append("       <arg0>"+timeToWait+"</arg0>");
		request.append("    </spr:sayHi>");
		request.append(" </soapenv:Body>");
		request.append("</soapenv:Envelope>");

		execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:9002/HelloTimeout", "HelloWorldImplService", "HelloWorldImplPort", "http://hello.cxf.ws.connectors.bonitasoft.org/", null, "guest", "guest", timeout, -1);

		server.stop();
	}
	/*
	public void testConnectionTimeout() throws Exception{
		long timeout = 10;
		long maxTime = 20;

		final Server server = new Server(9002);
		server.start();
		final StringBuilder request = new StringBuilder("");
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spr=\"http://hello.cxf.ws.connectors.bonitasoft.org/\">");
		request.append(" <soapenv:Header/>");
		request.append(" <soapenv:Body>");
		request.append("    <spr:sayHi>");
		request.append("       <arg0>4567890</arg0>");
		request.append("    </spr:sayHi>");
		request.append(" </soapenv:Body>");
		request.append("</soapenv:Envelope>");

		long start = System.currentTimeMillis();
		try {
			execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:9002/HelloTimeout", "HelloWorldImplService", "HelloWorldImplPort", "http://hello.cxf.ws.connectors.bonitasoft.org/", null, "guest", "guest", -1, timeout);
			fail("This call should fail...");
		} catch (Exception e) {
			long timeToGetException = System.currentTimeMillis() - start;
			assertTrue("timeToGetException: " + timeToGetException, timeToGetException < maxTime);
			//OK
		}	

		server.stop();
	}
	*/
	

	/*
  public void testWeather() throws Exception {
    final String zipCode = "19110";// Philadelphia
    //final String zipCode = "33157";// Miami

    final StringBuilder request = new StringBuilder("");
    request.append("<S:Envelope xmlns:S=\"" + soapSchema + "\" xmlns:xsi=\"" + xsiSchema + "\" xmlns:schNS=\"" + nsSchema + "\"> ");
    request.append("  <S:Body>");
    request.append("    <LatLonListZipCode S:encodingStyle=\"" + encodingStyle + "\">");
    request.append("      <schNS:zipCodeList xsi:type=\"schNS:zipCodeListType\">" + zipCode + "</schNS:zipCodeList>");
    request.append("    </LatLonListZipCode>");
    request.append("  </S:Body>");
    request.append("</S:Envelope>");

    final String response =
      execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://www.weather.gov/forecasts/xml/SOAP_server/ndfdXMLserver.php", "ndfdXML", "ndfdXMLPort", "http://www.weather.gov/forecasts/xml/DWMLgen/wsdl/ndfdXML.wsdl", null, null, null);
    assertTrue(response, response.contains("&lt;latLonList&gt;39.9525,-75.1657&lt;/latLonList&gt;"));
  }*/

	public void testConversionRateSoap11() throws Exception {
		final StringBuilder request = new StringBuilder();
		request.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		request.append("  <soap:Body>");
		request.append("    <ConversionRate xmlns=\"http://www.webserviceX.NET/\">");
		request.append("      <FromCurrency>AFA</FromCurrency>");
		request.append("      <ToCurrency>TRY</ToCurrency>");
		request.append("    </ConversionRate>");
		request.append("  </soap:Body>");
		request.append("</soap:Envelope>");

		final String response = 
				execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://www.webservicex.net/CurrencyConvertor.asmx", "CurrencyConvertor", "CurrencyConvertorSoap", "http://www.webserviceX.NET/", "http://www.webserviceX.NET/ConversionRate", null, null);
		assertTrue(response, response.contains("ConversionRateResponse"));
		assertTrue(response, response.contains("ConversionRateResult"));
	}

	public void testConversionRateSoap12() throws Exception {
		final StringBuilder request = new StringBuilder();
		request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">");
		request.append("  <soap12:Body>");
		request.append("    <ConversionRate xmlns=\"http://www.webserviceX.NET/\">");
		request.append("      <FromCurrency>EUR</FromCurrency>");
		request.append("      <ToCurrency>USD</ToCurrency>");
		request.append("    </ConversionRate>");
		request.append("  </soap12:Body>");
		request.append("</soap12:Envelope>");

		final String response = 
				execute(request.toString(), SOAPBinding.SOAP12HTTP_BINDING, "http://www.webservicex.net/CurrencyConvertor.asmx", "CurrencyConvertor", "CurrencyConvertorSoap", "http://www.webserviceX.NET/", null, null, null);
		assertTrue(response, response.contains("ConversionRateResponse"));
		assertTrue(response, response.contains("ConversionRateResult"));
	}

    public void testFootballpoolWebServices() throws Exception {
        String envelop = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:foot=\"http://footballpool.dataaccess.eu\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <foot:AllDefenders>\n" +
                "         <foot:sCountryName>?</foot:sCountryName>\n" +
                "      </foot:AllDefenders>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        try {
            execute(
                    envelop,
                    "http://schemas.xmlsoap.org/wsdl/soap/http",
                    "http://footballpool.dataaccess.eu/data/info.wso",
                    "http://footballpool.dataaccess.eu",
                    "InfoSoap",
                    "Info",
                    null,
                    null,
                    null);
            fail("Should fail");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("malformed"));
        }
    }

	private String execute(final String request, final String binding, final String endpoint, final String service, final String port, final String ns, final String soapAction, final String username, final String password) throws Exception {
		return execute(request, binding, endpoint, service, port, ns, soapAction, username, password, null, -1, -1);  
	}

	private String execute(final String request, final String binding, final String endpoint, final String service, final String port, final String ns, final String soapAction, final String username, final String password, final Map<String, List<String>>  requestHeaders) throws Exception {
		return execute(request, binding, endpoint, service, port, ns, soapAction, username, password, requestHeaders, -1, -1);  
	}

	private String execute(final String request, final String binding, final String endpoint, final String service, final String port, final String ns, final String soapAction, final String username, final String password, final List<List<Object>>  requestHeaders) throws Exception {
		return execute(request, binding, endpoint, service, port, ns, soapAction, username, password, null, requestHeaders, -1, -1);  
	}
	
	private String execute(final String request, final String binding, final String endpoint, final String service, final String port, final String ns, final String soapAction, final String username, final String password, final long readTimeout, final long connectionTimeout) throws Exception {
		return execute(request, binding, endpoint, service, port, ns, soapAction, username, password, null, readTimeout, connectionTimeout);
	}

	private String execute(final String request, final String binding, final String endpoint, final String service, final String port, final String ns, final String soapAction, final String username, final String password, final Map<String, List<String>>  requestHeaders, final long readTimeout, final long connectionTimeout) throws Exception {
		return execute(request, binding, endpoint, service, port, ns, soapAction, username, password, requestHeaders, null, readTimeout, connectionTimeout);
	}
	
	private String execute(final String request, final String binding, final String endpoint, final String service, final String port, final String ns, final String soapAction, final String username, final String password, final Map<String, List<String>>  requestHeaders, final List<List<Object>>  requestHeadersAsList, final long readTimeout, final long connectionTimeout) throws Exception {
		if (requestHeadersAsList != null && requestHeaders != null) {
			throw new RuntimeException ("only one of requestHeaders and requestHeadersAsList can be specified");
		}
		final SecureWSConnectorCXF_2_4_0 wsc = new SecureWSConnectorCXF_2_4_0();
		wsc.setEnveloppe(request);
		wsc.setBinding(binding);
		wsc.setEndpointAddress(endpoint);
		wsc.setServiceName(service);
		wsc.setPortName(port);
		wsc.setServiceNS(ns);
		wsc.setSoapAction(soapAction);
		wsc.setAuthUserName(username);
		wsc.setAuthPassword(password);
		wsc.setBuildResponseDocumentEnveloppe(true);
		wsc.setBuildResponseDocumentBody(true);
		wsc.setPrintRequestAndResponse(true);
		wsc.setHttpHeaders(requestHeaders);
		wsc.setHttpHeaders(requestHeadersAsList);
		wsc.setReadTimeout(readTimeout);
		wsc.setConnectionTimeout(connectionTimeout);
		wsc.execute();
		final Source response = wsc.getSourceResponse(); 
		final String resultAsString = parse(response);
		printResponse(resultAsString);
		return resultAsString;
	}

	private String parse(final Source response) throws TransformerFactoryConfigurationError, TransformerException {
		assertNotNull(response);
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		transformer.transform(response, result);
		return writer.toString();
	}

	private void printResponse(final String response) {
		assertNotNull(response);
		System.out.println("reponse=\n" + response);	  
	}
}
