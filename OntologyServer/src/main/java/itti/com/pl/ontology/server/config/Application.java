package itti.com.pl.ontology.server.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import itti.com.pl.ontology.core.ontology.Ontology;
import itti.com.pl.ontology.core.ontology.OntologyManager;
import itti.com.pl.ontology.server.ws.HelloWorld;
import itti.com.pl.ontology.server.ws.OntologyWS;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.managers.DestinationFactoryManagerImpl;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
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
public class Application extends SpringBootServletInitializer{

	@Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Replaces the need for web.xml
    @Bean
    public ServletRegistrationBean servletRegistrationBean(ApplicationContext context) {
        return new ServletRegistrationBean(new CXFServlet(), "/api/*");
    }

    @Autowired
    HelloWorld helloWorld;

    @Autowired
    OntologyWS ontologyWS;

    // Replaces cxf-servlet.xml
    @Bean
    // <jaxws:endpoint id="helloWorld" implementor="demo.spring.service.HelloWorldImpl" address="/HelloWorld"/>
    public EndpointImpl helloService() {
        Bus bus = (Bus) applicationContext.getBean(Bus.DEFAULT_BUS_ID);
        Object implementor = helloWorld;
        EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        endpoint.publish("/hello");
        return endpoint;
    }

    @Bean
    // <jaxws:endpoint id="helloWorld" implementor="demo.spring.service.HelloWorldImpl" address="/HelloWorld"/>
    public EndpointImpl webService() {
        Bus bus = (Bus) applicationContext.getBean(Bus.DEFAULT_BUS_ID);
        Object implementor = ontologyWS;
        EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        endpoint.publish("/ontology");
//DestinationFactoryManager dfmi = bus.getExtension(DestinationFactoryManager.class);
//DestinationFactory df = new DestinationFactory() {
//	
//	@Override
//	public Set<String> getUriPrefixes() {
//		HashSet<String> uris = new HashSet<>();
//		uris.add("http://");
//		return uris;
//	}
//	
//	@Override
//	public List<String> getTransportIds() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public Destination getDestination(EndpointInfo arg0, Bus arg1)
//			throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//};
//dfmi.registerDestinationFactory("TRANSPORT_IDENTIFIER", df);

return endpoint;
    }
    

    // Configure the embedded tomcat to use same settings as default standalone tomcat deploy
//    @Bean
//    public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
//        // Made to match the context path when deploying to standalone tomcat- can easily be kept in sync w/ properties
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory("/ws-server-1.0", 8080);
//        return factory;
//    }

    // Used when deploying to a standalone servlet container, i.e. tomcat
        
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public Ontology getOntology(){
    	return new OntologyManager();
    }
}
