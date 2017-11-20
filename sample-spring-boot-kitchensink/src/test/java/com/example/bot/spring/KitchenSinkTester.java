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

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { KitchenSinkTester.class, SQLDatabaseEngine.class})
public class KitchenSinkTester {

	@Autowired
	private SQLDatabaseEngine databaseEngine;

	@Test
	public void testNotFound() throws Exception {
		boolean thrown = false;
		try {
			this.databaseEngine.search("no");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(true);
	}

	@Test
	public void tourTest() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("Yangshan Hot Spring Tour please");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
//		assert (result.equals("2D002 Yangshan Hot Spring Tour * Unlimited use of hot spring * Famous Yangshan roaster cusine\n"));
	}

	@Test
	public void tourTestByDate() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("Monday");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
//		assert (result.equals("2D002 Yangshan Hot Spring Tour * Unlimited use of hot spring * Famous Yangshan roaster cusine\n"));
	}

	@Test
	public void tourTestByDateAndPrice() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("Monday show price");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
	}
	
	@Test
	public void tourTestByDateAndSort() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("Monday sort by id");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
	}
	
	@Test
	public void tourTestByDateAndSortByPrice() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("Monday sort by price");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
	}

	@Test
	public void tourTestByAttraction() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("spring");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
	}
	
	@Test
	public void tourTestByAttractionAndSort() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("spring sort by id");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
	}
	
	@Test
	public void tourTestByAttractionAndPrice() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("spring show price");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
	}
	
	@Test
	public void tourTestByAttractionAndSortByPrice() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("spring sort by price");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
	}
	
	@Test
	public void tourTestByID() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("ID: 2D002");
		} catch (Exception e) {
			thrown = true;
		}
		System.out.println(result);
	}
	
	@Test
	public void faqTest() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("any additional charge");
		} catch (Exception e) {
			thrown = true;
		}
		assert (result!= null);
		assert (result.equals("Each customer need to pay an additional service charge at the rate $60/day/person on top of the tour fee. It is collected by the tour guide at the end of the tour."));
	}
	
	@Test
	public void getSelectedTourTestNonNull() throws Exception {
		boolean thrown = false;
		Tour tour = new Tour(null, null, null, 0, 0, 0, null);
		Tour result = null;
		try {
			this.databaseEngine.setSelectedTour(tour);
			result = this.databaseEngine.getSelectedTour();
		} catch (Exception e) {
			thrown = true;
		}
		assert (result != null);
	}
	
	@Test
	public void getTourListTestResetNull() throws Exception {
		boolean thrown = false;
		List<Tour> result = null;  
		try {
			this.databaseEngine.resetTourList();
			result = this.databaseEngine.getTourList();
		} catch (Exception e) {
			thrown = true;
		}
		assert (result == null);
	}
	
	@Test
	public void getTourIdListTestResetNull() throws Exception {
		boolean thrown = false;
		List<String> result = null;
		try {
			this.databaseEngine.resetTourIDList();
			result = this.databaseEngine.getTourIDList();
		} catch (Exception e) {
			thrown = true;
		}
		assert (result == null);
	}
	
	@Test
	public void filterPreferenceTestNoConnection() throws Exception {
		boolean thrown = false;
		try {
			this.databaseEngine.resetPreferenceInput();
			this.databaseEngine.addPreferenceInput("3");
			this.databaseEngine.addPreferenceInput("spring");
			this.databaseEngine.addPreferenceInput("10000");
			this.databaseEngine.filterPreference();
		} catch (Exception e) {
			thrown = true;
		}
		assert (thrown == false);
	} 
	
	
}
