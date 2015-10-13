package itti.com.pl.ontology.server.config;

import itti.com.pl.ontology.server.ontology.OntologyManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@ComponentScan("mawa.com.pl.springmvc.controller")
public class OntologyServererConfig {

	@Bean
    public UrlBasedViewResolver setupViewResolver() {
            UrlBasedViewResolver resolver = new UrlBasedViewResolver();
            resolver.setPrefix("/WEB-INF/pages/");
            resolver.setSuffix(".jsp");
            resolver.setViewClass(JstlView.class);
            return resolver;
    }

	public OntologyManager getOntologyManager(){
		OntologyManager manager = new OntologyManager();
		return manager;
	}
}
