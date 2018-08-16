package com.loyalty.pointingsystem.cpnt.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.common.general.messaging.dto.CpntMessagingDto;
import com.loyalty.common.spring.messaging.QueueListener;
import com.loyalty.pointingsystem.configuration.CPntMessagingConfiguration;
import com.loyalty.pointingsystem.cpnt.ICPntService;
import com.loyalty.pointingsystem.entities.CPntEntity;
import com.rabbitmq.client.Channel;

public abstract class CPntMessagingAbstractListener implements QueueListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageConverter messageConverter;

	@Autowired
	private ICPntService cPntService;

	@Override
	public void onMessage(Message message, Channel channel) {
		log.info("Received message : {} from channel: {}", message, channel);
		Object receivedObj = null;
		receivedObj = messageConverter.fromMessage(message);

		log.debug("Received object: {} ", receivedObj);
		if (receivedObj instanceof CpntMessagingDto) {
			try {
				CpntMessagingDto receivedDto = (CpntMessagingDto) receivedObj;
				log.debug("Successully converted to CpntMessagingDto");
				// validate
				cPntService.isValidDto(receivedDto);
				handleMessage(receivedDto);
			} catch (ValidationException e) {
				log.error("Invalid Dto received will be ignored, violations found: {}", e.getListOfViolations());
			}
		} else {
			log.error("Unexpected type received, will be ignored");
		}

	}

	abstract void handleMessage(CpntMessagingDto cpntMessagingDto);

}
