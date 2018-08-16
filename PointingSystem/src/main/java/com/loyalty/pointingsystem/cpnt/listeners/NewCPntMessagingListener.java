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

@Service
public class NewCPntMessagingListener extends CPntMessagingAbstractListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ICPntService cPntService;

	@Override
	public String getQueueName() {
		return CPntMessagingConfiguration.newCPntQueueName;
	}

	@Override
	void handleMessage(CpntMessagingDto cpntMessagingDto) {
		try {
			log.info("attempting to insert Cpnt: {}", cpntMessagingDto);
			CPntEntity insertedCpnt = cPntService.insertNewCPnt(cpntMessagingDto);
			log.info("New CPnt inserted:{}", insertedCpnt);
		} catch (ValidationException e) {
			log.error("Invalid Dto received will be ignored, violations found: {}", e.getListOfViolations());
		} catch (ResourceAlreadyExists e) {
			log.error("{}, will be ignored", e.getMessage());
		}

	}

}
