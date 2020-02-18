package com.linecorp.devday.handson.demo.configuration;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.armeria.common.grpc.GrpcSerializationFormats;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.ContentPreviewingService;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.linecorp.devday.handson.demo.service.DemoGrpcService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ArmeriaServerConfiguration {

	private final DemoGrpcService demoGrpcService;

	@Bean
	public ArmeriaServerConfigurator armeriaServerConfigurator() {

		return serverBuilder -> {

			serverBuilder.decorator(LoggingService.newDecorator());
			serverBuilder.decorator(ContentPreviewingService.newDecorator(Integer.MAX_VALUE, StandardCharsets.UTF_8));
			serverBuilder.accessLogWriter(AccessLogWriter.combined(), false);

			serverBuilder.service(GrpcService.builder()
				.addService(demoGrpcService)
				.supportedSerializationFormats(GrpcSerializationFormats.values())
				.enableUnframedRequests(true)
				.build());

			serverBuilder.serviceUnder("/docs", new DocService());

		};

	}

}
