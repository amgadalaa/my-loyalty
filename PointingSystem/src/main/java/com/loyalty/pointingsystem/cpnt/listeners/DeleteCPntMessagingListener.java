package com.loyalty.pointingsystem.cpnt.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.common.general.messaging.dto.CpntMessagingDto;
import com.loyalty.common.spring.messaging.QueueListener;
import com.loyalty.pointingsystem.configuration.CPntMessagingConfiguration;
import com.loyalty.pointingsystem.cpnt.ICPntService;
import com.loyalty.pointingsystem.entities.CPntEntity;
import com.rabbitmq.client.Channel;

@Service
public class DeleteCPntMessagingListener extends CPntMessagingAbstractListener {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private ICPntService cPntService;

	@Override
	public String getQueueName() {
		return CPntMessagingConfiguration.deleteCPntQueueName;
	}

	@Override
	void handleMessage(CpntMessagingDto cpntMessagingDto) {
		Long systemCPntId = cpntMessagingDto.getSystemCPntId();
		try {
			log.info("attempting to delete CpntId: {}", systemCPntId);
			cPntService.deleteCpntById(systemCPntId);
			log.info("CpntId: {}, deleted", systemCPntId);
		} catch (ResourceNotExistException e) {
			log.error("Cpnt doesn't exists, will be ignored, CpntId: {}", systemCPntId);
		}
	}
	/**
	 * Headers:
	 * 
	 * __TypeId__ = com.loyalty.common.general.messaging.dto.CpntMessagingDto
	 * 
	 * 
	 * =
	 * 
	 * Properties:
	 * 
	 * content_type = json
	 * 
	 * = Payload:
	 * 
	 * {"systemCPntId": "123", "cPntName":"sds"}
	 */
}
