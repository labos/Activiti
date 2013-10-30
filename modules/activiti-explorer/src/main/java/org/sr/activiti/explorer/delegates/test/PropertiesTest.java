package org.sr.activiti.explorer.delegates.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.sr.activiti.bean.ApplicationConf;


public class PropertiesTest {
	private Environment env;
	private AnnotationConfigApplicationContext context =
      	     new AnnotationConfigApplicationContext(ApplicationConf.class);
	@Test
	public void checkPropertiesLoading() throws Exception {

	     Properties configFile = new Properties();
	        String result = "";
	        setEnvironment();
	        	 
	        	
	        try {
	        	
	            configFile.load(ClassLoader.getSystemClassLoader().getResourceAsStream("messages.properties"));
	            result = configFile.getProperty("app.title");
				System.out.println("Lettura property:" + result);

				System.out.println("Lettura property da env:" + env.getProperty("alfresco.port"));
				System.out.println("Lettura property da parameter:" + getParameter("alfresco.tag.determinazione"));



	        } catch (IOException e) {
				System.out.println("Problema caricamento propertie's file ");

	        }
		



	}
	private void setEnvironment(){
		
		AnnotationConfigApplicationContext ctx =
       	     new AnnotationConfigApplicationContext();
       	ctx.register(ApplicationConf.class);
       	ctx.refresh();
       	env = ctx.getEnvironment();
       	ApplicationConf myBean = ctx.getBean(ApplicationConf.class);
		System.out.println("Da spring property1:" + ctx.getEnvironment().getProperty("alfresco.port"));
		//System.out.println("Da spring property:" + myBean.getProperty());
		
		
	}
	
	private String getParameter(String key){
		
       	return context.getEnvironment().getProperty(key);
	}
	

}