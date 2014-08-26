package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.auth.OAuth2SecurityConfiguration;
import org.magnum.mobilecloud.video.repository.VideoRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = VideoRepository.class)
@EnableWebMvc
@Configuration
@ComponentScan
@Import(OAuth2SecurityConfiguration.class)
public class Application {

	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	//    1. Run->Run Configurations
	//    2. Under Java Applications, select your run configuration for this app
	//    3. Open the Arguments tab
	//    4. In VM Arguments, provide the following information to use the
	//       default keystore provided with the sample code:
	//
	//       -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit
	//
	//    5. Note, this keystore is highly insecure! If you want more securtiy, you 
	//       should obtain a real SSL certificate:
	//
	//       http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//
	// Tell Spring to launch our app!
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
    // This version uses the Tomcat web container and configures it to
	// support HTTPS. The code below performs the configuration of Tomcat
	// for HTTPS. Each web container has a different API for configuring
	// HTTPS. 
	//
	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	//    1. Run->Run Configurations
	//    2. Under Java Applications, select your run configuration for this app
	//    3. Open the Arguments tab
	//    4. In VM Arguments, provide the following information to use the
	//       default keystore provided with the sample code:
	//
	//       -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit
	//
	//    5. Note, this keystore is highly insecure! If you want more securtiy, you 
	//       should obtain a real SSL certificate:
	//
	//       http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//
//    @Bean
//    EmbeddedServletContainerCustomizer containerCustomizer(
//            @Value("${keystore.file:src/main/resources/private/keystore}") String keystoreFile,
//            @Value("${keystore.pass:changeit}") final String keystorePass) throws Exception {
//
//		// If you were going to reuse this class in another
//		// application, this is one of the key sections that you
//		// would want to change
//    	
//        final String absoluteKeystoreFile = new File(keystoreFile).getAbsolutePath();
//
//        return new EmbeddedServletContainerCustomizer () {
//
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
//		            TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
//		            tomcat.addConnectorCustomizers(
//		                    new TomcatConnectorCustomizer() {
//								@Override
//								public void customize(Connector connector) {
//									connector.setPort(8443);
//			                        connector.setSecure(true);
//			                        connector.setScheme("https");
//
//			                        Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
//			                        proto.setSSLEnabled(true);
//			                        proto.setKeystoreFile(absoluteKeystoreFile);
//			                        proto.setKeystorePass(keystorePass);
//			                        proto.setKeystoreType("JKS");
//			                        proto.setKeyAlias("tomcat");
//								}
//		                    });
//		    
//			}
//        };
//    }
	
}