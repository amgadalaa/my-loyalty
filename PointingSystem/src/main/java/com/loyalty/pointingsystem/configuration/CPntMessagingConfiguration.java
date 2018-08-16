package com.loyalty.pointingsystem.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.loyalty.common.general.messaging.ExchangeNames;
import com.loyalty.common.spring.messaging.MultipleQueueMessageListener;
import com.loyalty.pointingsystem.cpnt.listeners.NewCPntMessagingListener;

@Configuration
public class CPntMessagingConfiguration {

	public static final String newCPntQueueName = "pointing.CPnt.create";
	private static final String newCPntQBeanName = "NewCpntQ";
	private static final String newCPntExBeanName = "NewCpntEx";

	public static final String deleteCPntQueueName = "pointing.CPnt.delete";
	private static final String deleteCPntQBeanName = "deleteCpntQ";
	private static final String deleteCPntExBeanName = "deleteCpntEx";

	public static final String updateCPntQueueName = "pointing.CPnt.update";
	private static final String updateCPntQBeanName = "updateCpntQ";
	private static final String updateCPntExBeanName = "updateCpntEx";

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private MultipleQueueMessageListener listenerHandler;

	// TODO what if RabbitMQ server still not available

	@Bean(updateCPntQBeanName)
	Queue updateCPntQueue() {
		return new Queue(updateCPntQueueName, true);
	}

	@Bean(updateCPntExBeanName)
	FanoutExchange updateCPntExchange() {
		return new FanoutExchange(ExchangeNames.UPDATE_CPNT_EXCHANGE);
	}

	@Bean
	Binding updateCPntBinding(@Qualifier(updateCPntQBeanName) Queue queue,
			@Qualifier(updateCPntExBeanName) FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean(newCPntQBeanName)
	Queue newCPntQueue() {
		return new Queue(newCPntQueueName, true);
	}

	@Bean(newCPntExBeanName)
	FanoutExchange newCPntExchange() {
		return new FanoutExchange(ExchangeNames.NEW_CPNT_EXCHANGE);
	}

	@Bean
	Binding newCPntBinding(@Qualifier(newCPntQBeanName) Queue queue,
			@Qualifier(newCPntExBeanName) FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean(deleteCPntQBeanName)
	Queue deleteCPntQueue() {
		return new Queue(deleteCPntQueueName, true);
	}

	@Bean(deleteCPntExBeanName)
	FanoutExchange deleteCPntExchange() {
		return new FanoutExchange(ExchangeNames.DELETE_CPNT_EXCHANGE);
	}

	@Bean
	Binding deleteCPntBinding(@Qualifier(deleteCPntQBeanName) Queue queue,
			@Qualifier(deleteCPntExBeanName) FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, NewCPntMessagingListener listener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		//TODO configure number of consumers needed
		container.setQueueNames(newCPntQueueName, deleteCPntQueueName, updateCPntQueueName);
		container.setChannelAwareMessageListener(listenerHandler);
		container.setDefaultRequeueRejected(false);
		container.setErrorHandler((error) -> {
			log.error("EX: occured while processing message", error.getMessage(), error);
		});

		// TODO configure dead letter
		return container;
	}

}
