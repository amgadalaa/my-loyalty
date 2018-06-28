package com.loyalty.pointingsystem.lusrs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = LusrsResource.class)
public class LusrsResourceTest {

	@Autowired
	private MockMvc mockMvc;

	
	@Test
	public void test_DeleteLUsr_when_LUsrNotExists_Then_404() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/lusrs/123")).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
