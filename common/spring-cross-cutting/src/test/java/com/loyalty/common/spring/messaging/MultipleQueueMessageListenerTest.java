package com.loyalty.common.spring.messaging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.loyalty.common.spring.TestConfiguration;
import com.rabbitmq.client.Channel;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class, MultipleQueueMessageListener.class })
public class MultipleQueueMessageListenerTest {

	@Autowired
	private QueueListener oneQueueListener;

	@Autowired
	private MultipleQueueMessageListener multipleQueueMessageListener;

	private final String testQueueName = "Q1";
	private final String testNonExistingQueueName = "Q2";

	@Test
	public void test_onMessage_When_ListerExists_then_NoException() throws Exception {

		// Given
		Message message = mock(Message.class);
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setConsumerQueue(testQueueName);
		when(message.getMessageProperties()).thenReturn(messageProperties);
		Channel channel = mock(Channel.class);

		// When
		multipleQueueMessageListener.onMessage(message, channel);

		// Then
		verify(oneQueueListener, Mockito.atLeastOnce()).onMessage(message, channel);
	}

	@Test(expected = RuntimeException.class)
	public void test_onMessage_When_ListenerDoesNotExists_then_NoException() throws Exception {

		// Given
		Message message = mock(Message.class);
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setConsumerQueue(testNonExistingQueueName);
		when(message.getMessageProperties()).thenReturn(messageProperties);
		Channel channel = mock(Channel.class);

		// When
		multipleQueueMessageListener.onMessage(message, channel);

	}
}
