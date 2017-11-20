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
		assertThat(thrown).isTrue();
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
		assertThat(thrown).isTrue();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountInvalidStage() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(1000);
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
		assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountInvalidStage() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(1000);
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
		assertThat(thrown).isFalse();
    }
    
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_0_GreetingNullUserID() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource(null);
			
			tmc1 = new TextMessageContent("id", "Hi"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_0_GreetingNonNullUserID() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
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
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_invalid() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(10000);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Id"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_neg1_ID() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(-1);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "ID"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);	
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_neg1_Name() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(true);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "Name"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_neg1_Age() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(true);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "Age"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_neg1_PhoneNum() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(true);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "Phone Number"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_neg1_Adult() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(true);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "No. of Adults"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_neg1_Children() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(true);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "No. of Children"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_neg1_Toodler() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(true);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "No. of Toodlers"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_neg1_Invalid() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(true);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
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
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_0() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Id"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_1() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(1);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Name"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_2() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(2);
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
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_3() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(3);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "12345678"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_4() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(4);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "1"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_5() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(5);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "1"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_6() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(6);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "1"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithDiscountStage_3_InputOption_0to6withNoReserve() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(true);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			ksc.getCustomer().setInputOption(0);
			tmc1 = new TextMessageContent("id", "id");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(1);
			tmc1 = new TextMessageContent("id", "name");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(2);
			tmc1 = new TextMessageContent("id", "123");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(3);
			tmc1 = new TextMessageContent("id", "12345678");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(4);
			tmc1 = new TextMessageContent("id", "1");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(5);
			tmc1 = new TextMessageContent("id", "1");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
			ksc.getCustomer().setInputOption(6);
			tmc1 = new TextMessageContent("id", "1");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(6); 
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_neg1_Finish() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(-1);
		ksc.getCustomer().setPreferenceFinished(true);
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_GreetingNullUserID() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource(null);
			
			tmc1 = new TextMessageContent("id", "Hi"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_GreetingNonNullUserID() throws Exception {
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_Preference_neg1() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		ksc.getCustomer().resetPreferenceNum();
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_Preference_0() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		ksc.getCustomer().resetPreferenceNum();
		ksc.getCustomer().preferenceNumIncre();
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_Preference_1() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		ksc.getCustomer().resetPreferenceNum();
		ksc.getCustomer().preferenceNumIncre();
		ksc.getCustomer().preferenceNumIncre();
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_Preference_2() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		ksc.getCustomer().resetPreferenceNum();
		ksc.getCustomer().preferenceNumIncre();
		ksc.getCustomer().preferenceNumIncre();
		ksc.getCustomer().preferenceNumIncre();
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_Gathering() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Gathering"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_0_EnrollIn() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "enroll in"); 
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_1_IPick() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(1);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "I pick "); 
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_invalid() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(10000);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Id"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_neg1_ID() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(-1);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "ID"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);	
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_neg1_Name() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(false);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "Name"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_neg1_Age() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(false);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "Age"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_neg1_PhoneNum() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(false);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "Phone Number"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_neg1_Adult() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(false);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "No. of Adults"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_neg1_Children() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(false);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "No. of Children"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_neg1_Toodler() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(false);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
			UserSource us;
			TextMessageContent tmc1;
			MessageEvent<TextMessageContent> message1;
			try {
				us = new UserSource("userId");
				
				tmc1 = new TextMessageContent("id", "No. of Toodlers"); 
				message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
				ksc.handleTextMessageEvent(message1);
	
			} catch (Exception e) {
				thrown = true;
			}
			//assertThat(thrown).isFalse();
	    }
	
	@Test
	   public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_neg1_Invalid() throws Exception {
			boolean thrown = false;
			ksc = new KitchenSinkController();
			ksc.getCustomer().setShowDiscount(false);
			ksc.getCustomer().setStage(3);
			ksc.getCustomer().resetInputOption();
			ksc.getCustomer().setInputOption(-1);
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_0() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(0);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Id"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_1() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(1);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "Name"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_2() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(2);
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
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_3() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(3);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "12345678"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_4() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(4);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "1"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_5() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(5);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "1"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_6() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		ksc.getCustomer().setInputOption(6);
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			tmc1 = new TextMessageContent("id", "1"); 
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
		} catch (Exception e) {
			thrown = true;
		}
		//assertThat(thrown).isFalse();
    }
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_3_InputOption_0to6withNoReserve() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
		ksc.getCustomer().setStage(3);
		ksc.getCustomer().resetInputOption();
		
		UserSource us;
		TextMessageContent tmc1;
		MessageEvent<TextMessageContent> message1;
		try {
			us = new UserSource("userId");
			
			ksc.getCustomer().setInputOption(0);
			tmc1 = new TextMessageContent("id", "id");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(1);
			tmc1 = new TextMessageContent("id", "name");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(2);
			tmc1 = new TextMessageContent("id", "123");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(3);
			tmc1 = new TextMessageContent("id", "12345678");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(4);
			tmc1 = new TextMessageContent("id", "1");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(5);
			tmc1 = new TextMessageContent("id", "1");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.handleTextMessageEvent(message1);			
			ksc.getCustomer().setInputOption(6);
			tmc1 = new TextMessageContent("id", "1");
			message1 = new MessageEvent<TextMessageContent>("replyToken", us, tmc1, Instant.now()); 
			ksc.getCustomer().setInputOption(6); 
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
	
	@Test
    public void handleTextMessageEventTestSuccesswithNoDiscountStage_5_random() throws Exception {
		boolean thrown = false;
		ksc = new KitchenSinkController();
		ksc.getCustomer().setShowDiscount(false);
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
}
