package de.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.msv.datatype.xsd.DatatypeFactory;
import com.sun.xml.bind.v2.util.QNameMap;

public class UsersServicesTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
		
	private String getEncodedObj(Object arg) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(arg.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(arg, output);
		System.out.println(output.toString());
		return ""+output.toString();
	}
	
	private void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }
	
	/*@Test
	public void test() throws Exception {
		fail("Not yet implemented ");
	}*/
	
	/*@Test
	public void testRegister() throws Exception {
		try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            String url = "http://rafael-pc:8080/WebServices/UsersServicesService";
            SOAPMessage soapResponse = soapConnection.call(registerMsg(), url);

            // Process the SOAP Response
            printSOAPResponse(soapResponse);

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
	}*/
	
	private SOAPMessage registerMsg() throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		String serverURI = "http://de/";
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("example", serverURI);
		
		/*
		Constructed SOAP Request Message:
		<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:example="http://ws.cdyne.com/">
		    <SOAP-ENV:Header/>
		    <SOAP-ENV:Body>
		        <example:VerifyEmail>
		            <example:email>mutantninja@gmail.com</example:email>
		            <example:LicenseKey>123</example:LicenseKey>
		        </example:VerifyEmail>
		    </SOAP-ENV:Body>
		</SOAP-ENV:Envelope>
		 */
		
		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		
		// Works
		/*SOAPElement soapBodyElem = soapBody.addChildElement("register", "example");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("name");
		soapBodyElem1.addTextNode("Raf");*/
		
		SOAPElement soapBodyElem = soapBody.addChildElement("register", "example");
		SOAPElement param = soapBodyElem.addChildElement("customer");
		SOAPElement soapBodyElem2 = param.addChildElement("firstName");
		soapBodyElem2.addTextNode("Rafas");
		SOAPElement soapBodyElem3 = param.addChildElement("lastName");
		soapBodyElem3.addTextNode("Cam");
		
		/*MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI  + "VerifyEmail");*/
		
		soapMessage.saveChanges();
		
		/* Print the request message */
		/*soapMessage.writeTo(System.out);
		System.out.println();*/
		
		return soapMessage;
	}
	 
	
}
