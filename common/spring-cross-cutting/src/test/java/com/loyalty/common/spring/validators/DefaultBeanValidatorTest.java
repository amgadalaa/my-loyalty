package com.loyalty.common.spring.validators;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = DefaultBeanValidator.class)
public class DefaultBeanValidatorTest {

	@ClassRule
	public static final SpringClassRule springClassRule = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private DefaultBeanValidator<TestValidationClass> validator;

	@Test
	@Parameters(method = "provideCasesFor_test_isValid_When_Object")
	public void test_isValid_When_Object(TestValidationClass obj, int violationCount) {
		List<String> listOfViolations = validator.isValid(obj);

		Assert.assertEquals("Wrong validation for class:" + obj, violationCount, listOfViolations.size());

	}

	private Object[] provideCasesFor_test_isValid_When_Object() {

		List<Object> casesList = new ArrayList<>();
		List<Object> oneCase = null;

		// Valid Object
		oneCase = new ArrayList<>();
		oneCase.add(new TestValidationClass("test", 123));
		oneCase.add(0);
		casesList.add(oneCase.toArray());

		// invalid Object, null string
		oneCase = new ArrayList<>();
		oneCase.add(new TestValidationClass(null, 123));
		oneCase.add(1);
		casesList.add(oneCase.toArray());

		// invalid Object, null str and non postive int
		oneCase = new ArrayList<>();
		oneCase.add(new TestValidationClass(null, -1));
		oneCase.add(2);
		casesList.add(oneCase.toArray());

		return casesList.toArray();
	}

}

class TestValidationClass {

	@NotNull
	private String testStr;

	@Positive
	private Integer testInt;

	public TestValidationClass(String testStr, Integer testInt) {
		super();
		this.testStr = testStr;
		this.testInt = testInt;
	}

	public String getTestStr() {
		return testStr;
	}

	public void setTestStr(String testStr) {
		this.testStr = testStr;
	}

	public Integer getTestInt() {
		return testInt;
	}

	public void setTestInt(Integer testInt) {
		this.testInt = testInt;
	}

	@Override
	public String toString() {
		return "TestValidationClass [testStr=" + testStr + ", testInt=" + testInt + "]";
	}

}
