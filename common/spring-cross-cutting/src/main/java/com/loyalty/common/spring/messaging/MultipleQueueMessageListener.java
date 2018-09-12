package com.loyalty.common.spring.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

@Service
@Lazy
public class MultipleQueueMessageListener implements ChannelAwareMessageListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private List<QueueListener> allConsumers;

	private Map<String, QueueListener> listOfQueueListeners = new HashMap<>();

	@PostConstruct
	public void postConstruct() {

		allConsumers.stream().forEach(listener -> {
			String queueName = listener.getQueueName();
			if (listOfQueueListeners.putIfAbsent(queueName, listener) != null) {
				String errorMessage = String.format("queue %s has already listener", queueName);
				log.error(errorMessage);
				throw new RuntimeException(errorMessage);
			}
		});
	}

	public void onMessage(Message message, Channel channel) throws Exception {
		log.info("Received message: {} from channel: {}", message, channel);
		String queueName = message.getMessageProperties().getConsumerQueue();
		QueueListener listener = listOfQueueListeners.get(queueName);
		if (listener == null) {
			// no listener for queue
			// Then throw exception to roll back
			String errorMessage = String.format("No listener registered for queue: %s", queueName);
			log.error(errorMessage);
			throw new RuntimeException(errorMessage);
		} else {
			// pass message
			listener.onMessage(message, channel);
		}
	}

}
