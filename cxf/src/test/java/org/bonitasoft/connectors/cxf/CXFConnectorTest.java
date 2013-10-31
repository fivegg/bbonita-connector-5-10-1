package org.bonitasoft.connectors.cxf;

import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPBinding;

import junit.framework.TestCase;

public class CXFConnectorTest extends TestCase {

  private static final String nsSchema = "http://www.weather.gov/forecasts/xml/DWMLgen/schema/DWML.xsd";
  private static final String soapSchema = "http://schemas.xmlsoap.org/soap/envelope/";
  private static final String xsiSchema = "http://www.w3.org/2001/XMLSchema-instance";
  private static final String encodingStyle = "http://schemas.xmlsoap.org/soap/encoding/";

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
      execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://www.weather.gov/forecasts/xml/SOAP_server/ndfdXMLserver.php", "ndfdXML", "ndfdXMLPort", "http://www.weather.gov/forecasts/xml/DWMLgen/wsdl/ndfdXML.wsdl", null);
    assertTrue(response, response.contains("&lt;latLonList&gt;39.9525,-75.1657&lt;/latLonList&gt;"));
  }
  
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
      execute(request.toString(), SOAPBinding.SOAP11HTTP_BINDING, "http://www.webservicex.net/CurrencyConvertor.asmx", "CurrencyConvertor", "CurrencyConvertorSoap", "http://www.webserviceX.NET/", "http://www.webserviceX.NET/ConversionRate");
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
      execute(request.toString(), SOAPBinding.SOAP12HTTP_BINDING, "http://www.webservicex.net/CurrencyConvertor.asmx", "CurrencyConvertor", "CurrencyConvertorSoap", "http://www.webserviceX.NET/", null);
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
        CXFConnector wsc = new CXFConnector();
        wsc.setRequest(envelop);
        wsc.setBinding("http://schemas.xmlsoap.org/wsdl/soap/http");
        wsc.setEndpointAddress("http://footballpool.dataaccess.eu/data/info.wso");
        wsc.setServiceName("http://footballpool.dataaccess.eu");
        wsc.setPortName("InfoSoap");
        wsc.setTargetNS("Info");
        wsc.execute();

        assertNotNull(wsc.getResponse());
    }

  private String execute(final String request, final String binding, final String endpoint, final String service, final String port, final String ns, final String soapAction) throws Exception {
    final CXFConnector wsc = new CXFConnector();
    wsc.setRequest(request);
    wsc.setBinding(binding);
    wsc.setEndpointAddress(endpoint);
    wsc.setServiceName(service);
    wsc.setPortName(port);
    wsc.setTargetNS(ns);
    wsc.setSoapAction(soapAction);
    wsc.execute();
    final Source response = wsc.getResponse(); 
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
