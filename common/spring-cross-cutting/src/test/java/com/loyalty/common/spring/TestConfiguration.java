package com.loyalty.common.spring;

import static org.mockito.Mockito.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.loyalty.common.spring.messaging.QueueListener;

@Configuration
public class TestConfiguration {

	@Bean
	public QueueListener mockQueueListener() {
		QueueListener mockObj = mock(QueueListener.class);
		when(mockObj.getQueueName()).thenReturn("Q1");

		return mockObj;
	}
}
