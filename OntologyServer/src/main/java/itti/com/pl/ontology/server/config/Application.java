package itti.com.pl.ontology.server.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.core.ontology.LocalOntologyRepository;
import itti.com.pl.ontology.core.ontology.Ontology;
import itti.com.pl.ontology.core.ontology.OntologyManager;
import itti.com.pl.ontology.server.ws.MetadataHandlerWS;

import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.binding.soap.SoapBindingFactory;
import org.apache.cxf.binding.soap.SoapTransportFactory;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.local.LocalTransportFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.wsdl.WSDLManager;
import org.apache.cxf.wsdl11.WSDLManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
@ComponentScan("itti.com.pl.ontology.server.ws")
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class Application extends SpringBootServletInitializer {

	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// Replaces the need for web.xml
	@Bean
	public ServletRegistrationBean servletRegistrationBean(
			ApplicationContext context) {
		return new ServletRegistrationBean(new CXFServlet(), "/api/*");
	}

	@Autowired
	MetadataHandlerWS metadataHandlerWS;

	@Bean
	// Replaces cxf-servlet.xml
	// <jaxws:endpoint id="helloWorld"
	// implementor="demo.spring.service.HelloWorldImpl" address="/HelloWorld"/>
	public EndpointImpl webService() {
		Bus bus = (Bus) applicationContext.getBean(Bus.DEFAULT_BUS_ID);
		Object implementor = metadataHandlerWS;
		EndpointImpl endpoint = new EndpointImpl(bus, implementor);
		endpoint.publish("/MetadataHandler");

		createCustomTransport(bus);

		return endpoint;
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

	// Configure the embedded tomcat to use same settings as default standalone
	// tomcat deploy
	// @Bean
	// public EmbeddedServletContainerFactory embeddedServletContainerFactory()
	// {
	// // Made to match the context path when deploying to standalone tomcat-
	// can easily be kept in sync w/ properties
	// TomcatEmbeddedServletContainerFactory factory = new
	// TomcatEmbeddedServletContainerFactory("/ws-server-1.0", 8080);
	// return factory;
	// }

	// Used when deploying to a standalone servlet container, i.e. tomcat

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Bean
	public Ontology getOntology() {
		try {
			return new LocalOntologyRepository().loadOntology("");
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
