package itti.com.pl.ontology.server.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.ws.Endpoint;

import itti.com.pl.ontology.server.ws.MetadataHandler;

import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.binding.soap.SoapBindingFactory;
import org.apache.cxf.binding.soap.SoapTransportFactory;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.local.LocalTransportFactory;
import org.apache.cxf.wsdl.WSDLManager;
import org.apache.cxf.wsdl11.WSDLManagerImpl;

public class Application {

	public static void main(String[] args) {

		System.out.println("Starting Server");
		MetadataHandler implementor = new MetadataHandler();
		String address = "http://localhost:9000/metadataHandler";
		Endpoint.publish(address, implementor);
	}


	private void createCustomTransport(Bus bus) {
		DestinationFactoryManager dfm = bus
				.getExtension(DestinationFactoryManager.class);
		DestinationFactory df = new DestinationFactory() {

			@Override
			public Set<String> getUriPrefixes() {
				Set<String> uris = new HashSet<>();
				uris.add("http://");
				return uris;
			}

			@Override
			public List<String> getTransportIds() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Destination getDestination(EndpointInfo arg0, Bus arg1)
					throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		SoapBindingFactory soapBindingFactory = new SoapBindingFactory();
		soapBindingFactory.setBus(bus);
		bus.getExtension(BindingFactoryManager.class).registerBindingFactory("http://schemas.xmlsoap.org/wsdl/soap/",soapBindingFactory);
		
		SoapTransportFactory soapTransportFactory = new SoapTransportFactory();
		dfm.registerDestinationFactory("http://schemas.xmlsoap.org/wsdl/soap/",soapTransportFactory);
		  dfm.registerDestinationFactory(SoapBindingConstants.SOAP11_BINDING_ID,soapTransportFactory);
		  dfm.registerDestinationFactory("http://cxf.apache.org/transports/local",soapTransportFactory);
		  LocalTransportFactory localTransport =new LocalTransportFactory();
		  dfm.registerDestinationFactory("http://schemas.xmlsoap.org/soap/http",localTransport);
		  dfm.registerDestinationFactory("http://schemas.xmlsoap.org/wsdl/soap/http",localTransport);
		  dfm.registerDestinationFactory("http://cxf.apache.org/bindings/xformat",localTransport);
		  dfm.registerDestinationFactory("http://cxf.apache.org/transports/local",localTransport);
		  ConduitInitiatorManager extension=bus.getExtension(ConduitInitiatorManager.class);
		  extension.registerConduitInitiator(LocalTransportFactory.TRANSPORT_ID,localTransport);
		  extension.registerConduitInitiator("http://schemas.xmlsoap.org/wsdl/soap/",localTransport);
		  extension.registerConduitInitiator("http://schemas.xmlsoap.org/soap/http",localTransport);
		  extension.registerConduitInitiator(SoapBindingConstants.SOAP11_BINDING_ID,localTransport);
		  try {
			bus.setExtension(new WSDLManagerImpl(),WSDLManager.class);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		  addNamespace("wsdl",SOAPConstants.WSDL11_NS);
//		  addNamespace("wsdlsoap",SOAPConstants.WSDL11_SOAP_NS);
//		  addNamespace("xsd",SOAPConstants.XSD);
	}
}
