package itti.com.pl.ontology.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.ws.Endpoint;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.core.ontology.LocalOntologyRepository;
import itti.com.pl.ontology.core.ontology.OntologyManager;
import itti.com.pl.ontology.server.exeption.MetadataHandlerException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataHandlerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetadataHandler.class);

	private Properties properties = new Properties();

	private LocalOntologyRepository ontologyRepository = null;
	private OntologyManager ontologyManager = null;
	private MetadataHandler implementor = null;

	public static void main(String[] args) {

		LOGGER.info("Starting Server");
		MetadataHandlerConfig config = new MetadataHandlerConfig();
		try {
			LOGGER.info("Loading default properties");
			config.loadProperties();

			if(args.length > 0){
				LOGGER.info("Loading custom propeties from file {}", args[1]);
				config.loadProperties(args[0]);				
			}
			config.initOntology();
			config.initService();

		} catch (OntologyException exc) {
		} catch (MetadataHandlerException exc) {
			LOGGER.error(exc.getLocalizedMessage(), exc);
		}
	}

	private void loadProperties() {
		try(InputStream stream = getClass().getResourceAsStream(Constants.DEFAULT_PROPERTIES_FILE)){
			loadProperties(stream);
		}catch (IOException | RuntimeException e) {
			throw new MetadataHandlerException(String.format("Could not read data from '%s' property file", Constants.DEFAULT_PROPERTIES_FILE), e);
		}
	}

	private void loadProperties(String propertyFile) {
		try(InputStream stream = new FileInputStream(propertyFile)){
			loadProperties(stream);
		}catch (IOException | RuntimeException e) {
			throw new MetadataHandlerException(String.format("Could not read data from '%s' property file", propertyFile), e);
		}
	}

	private void loadProperties(InputStream stream) {
		try {
			properties.load(stream);
		} catch (IOException e) {
			throw new MetadataHandlerException("Could not load property data", e);
		}
	}

	private void initOntology() throws OntologyException {

		LOGGER.info("Initialising ontology {}", properties.getProperty(Constants.ONTOLOGY));

		try {
			ontologyRepository = new LocalOntologyRepository(properties.getProperty(Constants.ONTOLOGY_REPOSITORY));

			ontologyManager = (OntologyManager) ontologyRepository
					.loadOntology(properties.getProperty(Constants.ONTOLOGY));
			ontologyManager.setOntologyNamespace(properties.getProperty(Constants.NAMESPACE));
		} catch (RuntimeException e) {
			throw new MetadataHandlerException("Could not load ontology", e);
		}
	}

	private void initService() {

		LOGGER.info("Initialising web service using address {}", properties.getProperty(Constants.ADDRESS));

		implementor = new MetadataHandler();
		implementor.setOntology(ontologyManager);

		try {
			Endpoint.publish(properties.getProperty(Constants.ADDRESS), implementor);
		} catch (RuntimeException e) {
			throw new MetadataHandlerException("Could not publish an endpoint", e);
		}

	}

	private void createCustomTransport(Bus bus) {
		DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
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
			public Destination getDestination(EndpointInfo arg0, Bus arg1) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		SoapBindingFactory soapBindingFactory = new SoapBindingFactory();
		soapBindingFactory.setBus(bus);
		bus.getExtension(BindingFactoryManager.class).registerBindingFactory("http://schemas.xmlsoap.org/wsdl/soap/",
				soapBindingFactory);

		SoapTransportFactory soapTransportFactory = new SoapTransportFactory();
		dfm.registerDestinationFactory("http://schemas.xmlsoap.org/wsdl/soap/", soapTransportFactory);
		dfm.registerDestinationFactory(SoapBindingConstants.SOAP11_BINDING_ID, soapTransportFactory);
		dfm.registerDestinationFactory("http://cxf.apache.org/transports/local", soapTransportFactory);
		LocalTransportFactory localTransport = new LocalTransportFactory();
		dfm.registerDestinationFactory("http://schemas.xmlsoap.org/soap/http", localTransport);
		dfm.registerDestinationFactory("http://schemas.xmlsoap.org/wsdl/soap/http", localTransport);
		dfm.registerDestinationFactory("http://cxf.apache.org/bindings/xformat", localTransport);
		dfm.registerDestinationFactory("http://cxf.apache.org/transports/local", localTransport);
		ConduitInitiatorManager extension = bus.getExtension(ConduitInitiatorManager.class);
		extension.registerConduitInitiator(LocalTransportFactory.TRANSPORT_ID, localTransport);
		extension.registerConduitInitiator("http://schemas.xmlsoap.org/wsdl/soap/", localTransport);
		extension.registerConduitInitiator("http://schemas.xmlsoap.org/soap/http", localTransport);
		extension.registerConduitInitiator(SoapBindingConstants.SOAP11_BINDING_ID, localTransport);
		try {
			bus.setExtension(new WSDLManagerImpl(), WSDLManager.class);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// addNamespace("wsdl",SOAPConstants.WSDL11_NS);
		// addNamespace("wsdlsoap",SOAPConstants.WSDL11_SOAP_NS);
		// addNamespace("xsd",SOAPConstants.XSD);
	}
}
