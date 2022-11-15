package com.apgroup.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.apgroup.pms.thread.ProductionLine;
import com.apgroup.pms.thread.WorkTimer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class PmsApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PmsApplication.class, args);
		
		Thread workTimer = new Thread(context.getBean(WorkTimer.class));
		workTimer.start();
		
		Thread productionLine = new Thread(context.getBean(ProductionLine.class));
		productionLine.start();
	}

}
