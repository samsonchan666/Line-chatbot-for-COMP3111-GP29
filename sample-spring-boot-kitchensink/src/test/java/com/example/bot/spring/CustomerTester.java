package com.example.bot.spring;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.example.bot.spring.DatabaseEngine;

//import org.springframework.boot.SpringApplication;
//import org.springframework.data.repository.CrudRepository;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { CustomerTester.class, Customer.class})
public class CustomerTester {
	
	@Autowired
	private Customer c;
	
	@Test
	public void ConstructorTest() throws Exception {
		boolean thrown = false;
		try {
			Tour t = new Tour("T0000", "Random Tour", "Random Attraction", 3, 100, 100, "Weekday1|Weekday2|Weekday3"); 
			c = new Customer("000", "Ryan", 20, t, 0.0);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isFalse();
	}

	@Test
	public void setAndgetIDTest() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			c.setId("abc123!");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getId()=="abc123!").isTrue();
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void setAndgetNameTest() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			c.setName("abc123!");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getName()=="abc123!").isTrue();
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void setAndgetAgeTest() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			c.setAge(20);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getAge()).isEqualTo(20);
		assertThat(thrown).isFalse();
	}

	@Test
	public void setAndgetTourTest() throws Exception {
		boolean thrown = false;
		Tour t = new Tour("T0000", "Random Tour", "Random Attraction", 3, 100, 100, "Weekday1|Weekday2|Weekday3");
		
		try {
			c = new Customer(null, null, 0, null, 0);
			c.setTour(t);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getTour()).isEqualTo(t);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void setAndgetCustomerNoTest() throws Exception {
		boolean thrown = false;
		CustomerNo cn = new CustomerNo(0, 0, 0);
		cn.setAdultNo(1);
		int cn_adultNo = cn.getAdultNo();
		cn.setChildrenNo(10);
		int cn_childrenNo = cn.getChildrenNo();
		cn.setToodlerNo(100);
		int cn_toodlerNo = cn.getToodlerNo();
		
		try {
			c = new Customer(null, null, 0, null, 0);
			c.setCustomerNo(cn);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getCustomerNo()).isEqualTo(cn);
		assertThat(c.getCustomerNo().getAdultNo()).isEqualTo(cn_adultNo);
		assertThat(c.getCustomerNo().getChildrenNo()).isEqualTo(cn_childrenNo);
		assertThat(c.getCustomerNo().getToodlerNo()).isEqualTo(cn_toodlerNo);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void setAndgetFeeTest() throws Exception {
		boolean thrown = false;
		Fee f = new Fee(0, 0, 0);
		f.setAdultFee(10);
		double f_adultFee = f.getAdultFee();
		f.setChildrenFee(100);
		double f_childrenFee = f.getChildrenFee();
		f.setTotalFee();
		double f_totalFee = f.getTotalFee();
		
		try {
			c = new Customer(null, null, 0, null, 0);
			c.setFee(f);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee()).isEqualTo(f);
		assertThat(c.getFee().getAdultFee()).isEqualTo(f_adultFee);
		assertThat(c.getFee().getChildrenFee()).isEqualTo(f_childrenFee);
		assertThat(c.getFee().getTotalFee()).isEqualTo(f_totalFee);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void calculateFeeTestFail() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Weekday1|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(0);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void calculateFeeTestSuccessMon() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Mon|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(180);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void calculateFeeTestSuccessTue() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Tue|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(180);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void calculateFeeTestSuccessWed() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Wed|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(180);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void calculateFeeTestSuccessThu() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Thu|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(180);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void calculateFeeTestSuccessFri() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Fri|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(180);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void calculateFeeTestSuccessSat() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Sat|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(360);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void calculateFeeTestSuccessSun() throws Exception {
		boolean thrown = false;
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Sun|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(360);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void payAndPaidAmountTestSuccesswithNoReturns() throws Exception {
		boolean thrown = false;
		double returns = 0;
		
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Sun|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
			returns = c.pay(100);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(360);
		assertThat(c.getPayAmount()).isEqualTo(100);
		assertThat(c.haveRemainPayment()).isTrue();
		assertThat(returns).isEqualTo(0);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void payAndPaidAmountTestSuccesswithReturns() throws Exception {
		boolean thrown = false;
		double returns = 0;
		
		try {
			c = new Customer(null, null, 0, null, 0);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Sun|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
			returns = c.pay(400);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(360);
		assertThat(c.getPayAmount()).isEqualTo(360);
		assertThat(c.haveRemainPayment()).isFalse();
		assertThat(returns).isEqualTo(40);
		assertThat(thrown).isFalse();
	}
	
	@Test
	public void payAndPaidAmountTestFail() throws Exception {
		boolean thrown = false;
		double returns = 0;
		
		try {
			c = new Customer(null, null, 0, null, 360);
			
			Tour t = new Tour(null, null, null, 0, 100, 200, "Sun|Weekday2|Weekday3"); 
			c.setTour(t);
			
			CustomerNo cn = new CustomerNo(1, 1, 1);
			c.setCustomerNo(cn);
			
			c.calculateFee();
			returns = c.pay(400);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(c.getFee().getTotalFee()).isEqualTo(360);
		assertThat(c.getPayAmount()).isEqualTo(360);
		assertThat(c.haveRemainPayment()).isFalse();
		assertThat(returns).isEqualTo(400);
		assertThat(thrown).isFalse();
	}
}
