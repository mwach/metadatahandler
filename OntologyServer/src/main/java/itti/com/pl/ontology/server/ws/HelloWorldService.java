package itti.com.pl.ontology.server.ws;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

@WebService(endpointInterface = "itti.com.pl.ontology.server.ws.HelloWorld")
@Service
public class HelloWorldService implements HelloWorld {
	
    public String sayHi(String text) {
        System.out.println("sayHi called");
        return "Hello " + text;
    }
}