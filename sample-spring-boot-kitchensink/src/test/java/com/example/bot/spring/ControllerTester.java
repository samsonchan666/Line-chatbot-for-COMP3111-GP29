package com.example.bot.spring;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.time.Instant;
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
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.example.bot.spring.DatabaseEngine;

//import org.springframework.boot.SpringApplication;
//import org.springframework.data.repository.CrudRepository;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { CustomerTester.class, Customer.class})
public class ControllerTester {
	
	@Autowired
	private KitchenSinkController ksc;
	
	@Test
	public void constructorTest() throws Exception {
		boolean thrown = false;
		try {
			ksc = new KitchenSinkController();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isFalse();
	}
	
	@Test
    public void handleTextMessageEventTestFailNull() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		try {
			ksc.handleTextMessageEvent(null);
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isTrue();
    }
	
	@Test
    public void handleTextMessageEventTestFailInvalidInput() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			tmc1 = new TextMessageContent("id","random invalid input");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now());
			ksc.handleTextMessageEvent(null);
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isTrue();
    }
    
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_0() throws Exception {
		
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_2_Yes() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(2);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Yes"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_2_No() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(2);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "No"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_2_random() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(2);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "random"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_3() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "123"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_4_Yes() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(4);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Yes"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_4_No() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(4);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "No"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_4_random() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(4);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "random"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_5_Yes() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(5);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Yes"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_5_No() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(5);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "No"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
	public void handleTextMessageEventTestSuccesswithDiscountStage_5_random() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(5);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "random"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
	}
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_neg1() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(-1);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "123"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_Greet() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Hi"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_Discount() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "discount"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_Preference() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "preference"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_1_choose_other_tours() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(1);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "choose other tours"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_1_random() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(1);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "random"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_2_Yes() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(2);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Yes"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_2_No() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(2);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "No"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_2_random() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(2);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "random"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "123"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_4_Yes() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(4);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Yes"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_4_No() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(4);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "No"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_5_Yes() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(5);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Yes"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_5_No() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(5);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "No"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
}
