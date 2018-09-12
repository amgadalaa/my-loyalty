package com.loyalty.common.spring;

import org.springframework.core.Ordered;

/**
 * 
 * @author eamgmuh This class contains the priorities level needed by
 *         {@code Priority} to define bean registration order
 */
public class BeansPriorities {

	public static final int LOWEST = Ordered.LOWEST_PRECEDENCE;
	public static final int LOW = 10;
	public static final int HIGH = 90;
	public static final int HIGHEST = Ordered.HIGHEST_PRECEDENCE;

}
