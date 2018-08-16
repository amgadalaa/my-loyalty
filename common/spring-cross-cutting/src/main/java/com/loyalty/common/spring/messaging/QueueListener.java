package com.loyalty.common.spring.messaging;

import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

public interface QueueListener extends ChannelAwareMessageListener {

	String getQueueName();
}
