/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import com.linecorp.bot.model.profile.UserProfileResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * <h1>Main Controller of receiving and sending line messages</h1>
 */
@Slf4j
@LineMessageHandler
public class KitchenSinkController {

	@Autowired
	private LineMessagingClient lineMessagingClient;

	@SuppressWarnings("LossyEncoding")

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		log.info("This is your entry point:");
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		TextMessageContent message = event.getMessage();
		handleTextContent(event.getReplyToken(), event, message);
	}
/*
	@EventMapping
	public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
		handleSticker(event.getReplyToken(), event.getMessage());
	}

	@EventMapping
	public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
		LocationMessageContent locationMessage = event.getMessage();
		reply(event.getReplyToken(), new LocationMessage(locationMessage.getTitle(), locationMessage.getAddress(),
				locationMessage.getLatitude(), locationMessage.getLongitude()));
	}

	@EventMapping
	public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
		final MessageContentResponse response;
		String replyToken = event.getReplyToken();
		String messageId = event.getMessage().getId();
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		DownloadedContent jpg = saveContent("jpg", response);
		reply(((MessageEvent) event).getReplyToken(), new ImageMessage(jpg.getUri(), jpg.getUri()));

	}

	@EventMapping
	public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
		final MessageContentResponse response;
		String replyToken = event.getReplyToken();
		String messageId = event.getMessage().getId();
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		DownloadedContent mp4 = saveContent("mp4", response);
		reply(event.getReplyToken(), new AudioMessage(mp4.getUri(), 100));
	}

	@EventMapping
	public void handleUnfollowEvent(UnfollowEvent event) {
		log.info("unfollowed this bot: {}", event);
	}

	@EventMapping
	public void handleFollowEvent(FollowEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got followed event");
	}

	@EventMapping
	public void handleJoinEvent(JoinEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Joined " + event.getSource());
	}

	@EventMapping
	public void handlePostbackEvent(PostbackEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got postback " + event.getPostbackContent().getData());
	}

	@EventMapping
	public void handleBeaconEvent(BeaconEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got beacon message " + event.getBeacon().getHwid());
	}

	@EventMapping
	public void handleOtherEvent(Event event) {
		log.info("Received message(Ignored): {}", event);
	}
*/
	/**
	 * Guides the reply message to the correct reply function if the message is not in list form
	 * 
	 * Calls reply() in a correct format.
	 * 
	 * @param replyToken		like an ID for the reply message
	 * @param message		the reply message
	 */
	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	/**
	 * Correct reply function, creates a reply for Line.
	 * 
	 * Calls Line functions to make the chatbot 'say' the messages.
	 * 
	 * @param replyToken		like an ID for the reply message
	 * @param messages		the reply messages
	 */
	private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
			log.info("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Converts a String into a TextMessage, which reply() can take in.
	 * 
	 * Throws if replyToken is empty.
	 * Shorten the message if it is longer than 1000 characters using an ellipsis.
	 * 
	 * Converts the String into a TextMessage and pass it to reply().
	 * 
	 * @param replyToken		ID for the message
	 * @param message		reply message
	 */
	private void replyText(@NonNull String replyToken, @NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "..";
		}
		this.reply(replyToken, new TextMessage(message));
	}

	/**
	 * A function to handle stickers.
	 * 
	 * Replies the same sticker to the client calling reply().
	 * 
	 * @param replyToken		ID of reply message.
	 * @param content		content of sticker message
	 */
	private void handleSticker(String replyToken, StickerMessageContent content) {
		reply(replyToken, new StickerMessage(content.getPackageId(), content.getStickerId()));
	}

	/**
	 * Function handling text messages from client.
	 * 
	 * Uses a swtich to handle text input.
	 * -1: Preference
	 * Reads client's preference 
	 * and checks if there is a suitable tour using filterPreference.
	 * 
	 * 
	 * 0: Initial State
	 * Gives default response if client:
	 * is looking for Tour on discount
	 * is stating preference
	 * is saying Hi
	 * 
	 * If none of the above is true, 
	 * search in database the input.
	 * 
	 * If there is no result after searching database, 
	 * give default "don't understand" response
	 * 
	 * 
	 * 1: Confirms client's booking of a tour
	 * If client chooses to book another tour, reset to stage 0.
	 * Else confirm client's choice.
	 * 
	 * 2: Asks for input and passes to stage 3.
	 * 
	 * 3: Receives input.
	 * 
	 * 4: Confirms input.
	 * 
	 * 5: Confirms fee for booked tours.
	 * Also provides information of the paying method.
	 * 
	 * 
	 * @param replyToken		ID for reply message
	 * @param event			an event
	 * @param content		content of text message from client
	 * @throws Exception		calls errorConfirm()
	 */
	private void handleTextContent(String replyToken, Event event, TextMessageContent content)
			throws Exception {
		String text = content.getText();

		log.info("Got text message from {}: {}", replyToken, text);

		if (customer.getShowDiscount()) {
			specialDiscountCase(replyToken,text);
			if (customer.getShowDiscount()) return;
		}
		//-1 for preference, 0 for searching, 1 for confirm tour, 2 for ask input
		//3 for receive input, 4 for confirm input, 5 for confirm fee

        int stage = customer.getStage();
        switch (stage) {
        	case -1: {
        		int preferenceNum = customer.getPreferenceNum();
        		if (!customer.isPreferenceFinished())
        			receivePreference(replyToken, text, preferenceNum);
        		if (customer.isPreferenceFinished()) {
        			String reply = null;
        			try {
        				reply = database.filterPreference();
        			} catch (Exception e) {
        				reply = "Sorry, there is no tour suitable for you. "
        						+ "You may continue searching for other tours.";
        			}    				
        			this.reply(replyToken, stage0Messages(reply, text));
        			database.resetPreferenceInput();
        			customer.resetPreferenceNum();
        			customer.resetPreferenceFinished();
        			customer.stageProceed();
        		}
        		break;
        	}
        
        	case 0: {
        		if (text.toLowerCase().matches("discount")) customer.setShowDiscount(true);
        		if (text.toLowerCase().matches("(.*)preference(.*)")) {
        			customer.preferenceNumIncre();
        			askPreference(replyToken);
        			customer.stageRestore();
        			break;
        		}        		
				if ((text.toLowerCase().matches("hi(.*)|hello(.*)")))
				{
					String userId = event.getSource().getUserId();
					if (userId != null) {
						lineMessagingClient
						.getProfile(userId)
						.whenComplete(new ProfileGetter (this, replyToken, "Welcome"));
					}
					break;
				}
				String reply = null;
				try {
					reply = database.search(text);
				} catch (Exception e) {
					reply = "Sorry, I don't quite understand. Can you be more precise?";
				}
				log.info("Returns error message {}: {}", replyToken, reply);

				//Creating Filter Menu & Day Selection Menu if filtering is done
				this.reply(replyToken, stage0Messages(reply, text));
				break;
			}
        	//Create Confirm
        	case 1: {
        		if ((text.toLowerCase().matches("choose other tours"))) {
        			customer.stageRestore();
        			database.resetWeekDayOnlyTourIDList();
        			this.replyText(replyToken, "Okay. You may continue searching for other tours.");
        		}        			
        		else this.reply(replyToken, stage1Messages(text));
				break;	
        	}
        	
        	case 2: {
        		if ((text.toLowerCase().matches("no(.)*"))) {
        			this.replyText(replyToken, "Okay. You may pick another date.");
    				customer.stageRestore();
        		}
        		else if ((text.toLowerCase().matches("yes(.)*"))) {
        			customer.setTour(database.getSelectedTour());
        			database.setSelectedBooking();
        			customer.getTour().setID(database.getSelectedBooking().getID());
        			this.reply(replyToken, createInputMenu());
        			customer.stageProceed();
        		}
        		else errorConfirm(replyToken);
        		break;
        	}
        	
        	case 3: {
        		String reply = null;        		    			
        		if (customer.getInputOption() == -1)
        			askInputReply(replyToken, text);
        		else
        			inputReceive(replyToken, text);        			
        		break;
        	}
        	
        	case 4: {
        		List<Message> multiMessages = new ArrayList<Message>();
        		if ((text.toLowerCase().matches("no(.)*"))) {        			
        			reinputInfo(replyToken, multiMessages);
    				customer.stageRestore();
        		}
        		else if ((text.toLowerCase().matches("yes(.)*"))) {
        			outputFee(multiMessages);
        			this.reply(replyToken, multiMessages);
        		}
        		else errorConfirm(replyToken);
        		break;
        	}
        	
        	case 5: {
        		List<Message> multiMessages = new ArrayList<Message>();
        		if ((text.toLowerCase().matches("no(.)*"))) {        			
        			reinputInfo(replyToken, multiMessages);
    				customer.stageRestore();
    				customer.stageRestore();    //back to stage 3 for receiving input
        		}
        		else if ((text.toLowerCase().matches("yes(.)*"))) {
        			//Attach customer to observe a booking
        			attachCustomerToBooking();
        			//Save the customer to the database
        			database.saveCustomerToDb(customer);

        			this.reply(replyToken, new TextMessage(
        					"Thank you. Please pay the tour fee by ATM to 123-345-432-211 "
        							+ "of ABC Bank or by cash in our store. When you complete "
        							+ "the ATM payment, please send the bank in slip to us. "
        							+ "Our staff will validate it."));
        			database.resetWeekDayOnlyTourIDList();
        			customer.stageZero();//reset all except name, id, age, phoneNum
        		}
        		else errorConfirm(replyToken);
        		break;
        	}
		}
	}

	/**
	 * Similar to handleTextContext, but with a discounted tour available (read the documentation of that)
	 * 
	 * @param replyToken		ID of reply
	 * @param text			text input by client
	 * @throws Exception
	 */
	private void specialDiscountCase(String replyToken, String text) throws Exception{
		Booking dis_booking = new Booking(null,null,null,null,0,0,0);
		Tour dis_tour = new Tour(null,null,null,0,0,0,null);
		switch (customer.getStage()){
			case 0:{
				if (database.searchDiscountTour(dis_tour,dis_booking)){
					List<Message> multiMessages = new ArrayList<Message>();
					dis_tour = database.searchTourByID(dis_tour.getID());
					dis_booking = database.searchBookingByID(dis_booking.getID());
					database.setSelectedBooking(dis_booking);
					database.setSelectedTour(dis_tour);

					String dis_msg;
					if (dis_booking.dateToDay() >=2 && dis_booking.dateToDay() <=6) {
						dis_msg = dis_tour.getDiscountTourInfo(true,dis_booking.dateToString()).toString();
					}
					else{
						dis_msg = dis_tour.getDiscountTourInfo(false,dis_booking.dateToString()).toString();
					}
					multiMessages.add(new TextMessage(dis_msg));
					createConfirm("Do you want to book this one?", multiMessages);
					customer.setStage(2);
					this.reply(replyToken, multiMessages);
				}
				else {
					customer.setShowDiscount(false);
				}
				break;
			}
			case 2: {
				if ((text.toLowerCase().matches("no(.)*"))) {
					this.replyText(replyToken, "Okay. You may continue your search ^.^");
					customer.stageZero();
					customer.setShowDiscount(false);
				}
				else if ((text.toLowerCase().matches("yes(.)*"))) {
					customer.setTour(database.getSelectedTour());
					customer.getTour().setID(database.getSelectedBooking().getID());
					if (!(database.searchDiscountTour(dis_tour,dis_booking))){
						customer.stageZero();//reset all except name, id, age, phoneNum
						customer.setShowDiscount(false);
						this.replyText(replyToken, "Sorry ticket sold out");
						break;
					}
					this.reply(replyToken, createInputMenu());
					customer.stageProceed();
				}
				else errorConfirm(replyToken);
				break;
			}
			case 3: {
				String reply = null;
				if (customer.getInputOption() == -1)
					askInputReply(replyToken, text);
				else
					discountInputReceive(replyToken, text);
				break;
			}
			case 4: {
				List<Message> multiMessages = new ArrayList<Message>();
				if ((text.toLowerCase().matches("no(.)*"))) {
					reinputInfo(replyToken, multiMessages);
					customer.stageRestore();
				}
				else if ((text.toLowerCase().matches("yes(.)*"))) {
					outputDisCountFee(multiMessages);
					this.reply(replyToken, multiMessages);
				}
				else errorConfirm(replyToken);
				break;
			}

			case 5: {
				List<Message> multiMessages = new ArrayList<Message>();
				if ((text.toLowerCase().matches("no(.)*"))) {
					reinputInfo(replyToken, multiMessages);
					customer.stageRestore();
					customer.stageRestore();   //back to stage 3 for receiving input
				}
				else if ((text.toLowerCase().matches("yes(.)*"))) {
					//Attach customer to observe a booking
					attachCustomerToBooking();
					//Save the customer to the database
					database.saveCustomerToDb(customer);
					database.updateDiscountTour(database.getSelectedBooking().getID());
					this.reply(replyToken, new TextMessage(
							"Thank you. Please pay the tour fee by ATM to 123-345-432-211 "
									+ "of ABC Bank or by cash in our store. When you complete "
									+ "the ATM payment, please send the bank in slip to us. "
									+ "Our staff will validate it."));
					database.resetWeekDayOnlyTourIDList();
					customer.stageZero();//reset all except name, id, age, phoneNum
					customer.setShowDiscount(false);
				}
				else errorConfirm(replyToken);
				break;
			}

		}

	}
	
	/**
	 * Function that asks for preference specifications.
	 * 
	 * Looks at preferenceNum of client and asks for preference specifications accordingly.
	 * 0: Duration
	 * 1: Attractions
	 * 2: Budget
	 * 
	 * @param replyToken
	 */
	private void askPreference(String replyToken) {
		int preferenceNum = customer.getPreferenceNum();
		switch (preferenceNum) {
			case 0: { this.replyText(replyToken, "Please input your desired duration."); break;}		
			case 1: { this.replyText(replyToken, "Please input your interest."); break;}
			case 2: { this.replyText(replyToken, "Please input your budget."); break;}		
		}
	}
	
	/**
	 * Function that adds preference input to the database.
	 * 
	 * If preferenceNum is 2, which means duration, interest and budget are all asked, 
	 * Finish the preference asking process
	 * If not, 
	 * askPreference again.
	 * 
	 * @param replyToken		ID of reply message
	 * @param text			text input of client
	 * @param preferenceNum	how many times have preference been asked
	 */
	private void receivePreference(String replyToken, String text, int preferenceNum) {
		if (!numOnlyPreference(preferenceNum, replyToken, text))
			return;
		database.addPreferenceInput(text);
		if (customer.getPreferenceNum() == 2)
			customer.setPreferenceFinished(true);
		if (!customer.isPreferenceFinished()) {
			customer.preferenceNumIncre();
			askPreference(replyToken);
			return;
		}		
	}
	
	/**
	 * Function that supports client info reinput.
	 * 
	 * @param replyToken 	ID of reply message
	 * @param multiMessages 	a list of messages
	 */
	private void reinputInfo (String replyToken, List<Message> multiMessages) {
		multiMessages.add(new TextMessage("Okay. You may input your info again."));
		multiMessages.add(createInputMenu());
		this.reply(replyToken, multiMessages);
	}
	
	/**
	 * Function that give the reply asking for only yes or no from client.
	 * @param replyToken
	 */
	private void errorConfirm(String replyToken) {
		this.reply(replyToken, new TextMessage("Please only answer yes or no."));
	}
	
	/**
	 * Possible replies for stage 0
	 * 
	 * @param reply		reply message
	 * @param text		text input from client
	 * @return			a list of messages consisting of replies considering the input
	 */
	private List<Message> stage0Messages(String reply, String text){
		List<Message> multiMessages = new ArrayList<Message>();
		multiMessages.add(new TextMessage(reply));

		if ((text.toLowerCase().matches("(.)*gathering(.)*|(.)*assemble(.)*|(.)*dismiss(.)*"))){
			String imageUrl = createUri("/static/gather.jpg");
			multiMessages.add(new ImageMessage(imageUrl, imageUrl));
		}
		if (text.toLowerCase().matches("(.)*enroll in(.)*")) {
			createDaySelectMenu("Which Day do you want to pick?", multiMessages);
		}
		createFilterMenu("Tour Selection", multiMessages);

		return multiMessages;
	}
	
	/**
	 * Possible replies for stage 1
	 * 
	 * @param reply		reply message
	 * @param text		text input from client
	 * @return			a list of messages consisting of replies considering the input
	 */
	private List<Message> stage1Messages(String text){
		List<Message> multiMessages = new ArrayList<Message>();
		if (text.matches("I pick (.)*")) {
			database.setSelectedBookingText(text);
			createConfirm("Do you want to book this one?", multiMessages);
		}
		return multiMessages;
	}

	/**
	 * Function that asks for Yes/No input.
	 * 
	 * @param question			question requiring yes/no answer to
	 * @param multiMessages		a list of replies
	 */
	private void createConfirm(String question, List<Message> multiMessages) {
		customer.stageProceed();
    	ConfirmTemplate confirmTemplate = new ConfirmTemplate(
    			question, 
    			new MessageAction("Yes", "Yes"), 
    			new MessageAction("No", "No")
        );
    	multiMessages.add(new TemplateMessage("Confirm alt text", confirmTemplate));		
	}
	
	/**
	 * Function that creates a menu asking which tour the client would like to enroll in.
	 * 
	 * @param title			title of menu
	 * @param multiMessages	a list of replies
	 */
	private void createFilterMenu(String title, List<Message> multiMessages) {
		List<String> tourIDList = database.getTourIDList();    
		if (tourIDList != null)
			createMenu(tourIDList, title, "I want to enroll in ", multiMessages);
		database.resetTourIDList();
	}
	
	/**
	 * Creates a selection menu for dates for the client to choose from.
	 * 
	 * Gets a list of dates and then create a selection menu based on that.
	 * @param title			title of menu
	 * @param multiMessages	a list of replies
	 */
	private void createDaySelectMenu(String title, List<Message> multiMessages) {
		List<String> bookingDateList = null;
		customer.stageProceed();
		try {
			database.createBookingDateList();
			bookingDateList = database.getBookingDateList();
		} catch (Exception e) {
			return;
		}
		if (bookingDateList != null) {
			bookingDateList.add("Choose Other Tours");
			createMenu(bookingDateList, title, "I pick ", multiMessages);
		}
	}
	
	/**
	 * Actual implementation of menu creation.
	 * 
	 * @param list				list that the menu is based on
	 * @param title				title of menu
	 * @param message			message that appears on the menu
	 * @param multiMessages		a list of replies
	 */
	private void createMenu(List<String> list, String title, String message, List<Message> multiMessages) {
		List<CarouselTemplate> carouselTemplate = new ArrayList<CarouselTemplate>();
		List<CarouselColumn> carouselColumn;
		List<Action> action;
		int count = 0;
		int numTour = list.size();
		int templateCount = 0;
		while (count < numTour) {
			carouselColumn = new ArrayList<CarouselColumn>();
			for (int columnCount = 0; columnCount < 5 && count < numTour; columnCount++) {            		
				action = new ArrayList<Action>();            			
				for (int actionCount = 0; actionCount < 3 && count < numTour; actionCount++) {            			
					String element = list.get(count);
					if (element.matches("Choose Other Tours"))
						action.add(new MessageAction(element, element));
					else
						action.add(new MessageAction(element, message + element + "."));
					count++;
					if (columnCount != 0 && actionCount+1 < 3 && count == numTour) {
						for (int temp = actionCount+1; temp < 3; temp++) {
							action.add(new MessageAction(" ", " "));
						}
					}
				}
				carouselColumn.add(new CarouselColumn(null, null, title, action));
			}
			carouselTemplate.add(new CarouselTemplate(carouselColumn));
			multiMessages.add(new TemplateMessage("Carousel alt text", carouselTemplate.get(templateCount++)));
		}
	}
	
	/** 
	 * Creates a menu guiding information input from client.
	 * 
	 * 
	 * @return	a template message
	 */
	private TemplateMessage createInputMenu() {
		CarouselTemplate carouselTemplate = new CarouselTemplate(
				Arrays.asList(
						new CarouselColumn(null, null, "Please select the info you want to input", Arrays.asList(
								new MessageAction("ID", "ID"),
								new MessageAction("Name", "Name"), 
								new MessageAction("Age", "Age")
						)),
						new CarouselColumn(null, null, "Please select the info you want to input", Arrays.asList(
								new MessageAction("Phone Number", "Phone Number"),
								new MessageAction("No. of Adults", "No. of Adults"),
								new MessageAction("No. of Children", "No. of Children")
						)),
						new CarouselColumn(null, null, "Please select the info you want to input", Arrays.asList(
								new MessageAction("No. of Toddlers", "No. of Toddlers"),
								new MessageAction(" ", " "),
								new MessageAction(" ", " ")
						))
				));
		return new TemplateMessage("Carousel alt text", carouselTemplate);
		
	}
	
	/**
	 * Follow-up function of TemplateMessage()
	 * 
	 * @param replyToken	ID of reply message
	 * @param text		text input from client (option chosen from TemplateMessage)
	 */
	private void askInputReply(String replyToken, String text) {
		switch (text) {
			case "ID": {
				this.replyText(replyToken, askInput("ID"));
				customer.setInputOption(0);
				break;
			}
			case "Name": {
				this.replyText(replyToken, askInput("Name"));
				customer.setInputOption(1);
				break;
			}
			case "Age": {
				this.replyText(replyToken, askInput("Age"));
				customer.setInputOption(2);
				break;
			}
			case "Phone Number": {
				this.replyText(replyToken, askInput("Phone Number"));
				customer.setInputOption(3);
				break;
			}
			case "No. of Adults": {
				this.replyText(replyToken, askInput("No. of Adults"));
				customer.setInputOption(4);
				break;
			}
			case "No. of Children": {
				this.replyText(replyToken, askInput("No. of Children"));
				customer.setInputOption(5);
				break;
			}
			case "No. of Toddlers": {
				this.replyText(replyToken, askInput("No. of Toddlers"));
				customer.setInputOption(6);
				break;
			}
		}
	}

	/**
	 * Actually asking input of option chosen in TemplateMessage.
	 * Follow-up function of askInputReply()
	 * @param option		option to input chosen by client
	 * @return			a String asking for input of said option
	 */
	private String askInput(String option) {
		return "Please input " + option + ".";
	}

	/**
	 * Input receive function for discount tour.
	 * 
	 * If input is not number when number is wanted, return.
	 * If reserve is full, return.
	 * @param replyToken		ID of reply message
	 * @param text			user input
	 */
	private void discountInputReceive(String replyToken, String text) {
		int inputOption = customer.getInputOption();
		if (!numOnlyInfo(inputOption, replyToken, text))
			return;
		if (checkExcessReserve(inputOption, replyToken, text)) return;
		switch (inputOption) {
			case 0: { customer.setId(text); break;}
			case 1: { customer.setName(text); break;}
			case 2: { customer.setAge(Integer.parseInt(text)); break;}
			case 3: { customer.setPhoneNum(text); break;}
			case 4: { customer.getCustomerNo().setAdultNo(Integer.parseInt(text)); break;}
			case 5: { customer.getCustomerNo().setChildrenNo(Integer.parseInt(text)); break;}
			case 6: { customer.getCustomerNo().setToodlerNo(Integer.parseInt(text)); break;}
		}
		customer.resetInputOption();
		if (customer.inputFinished())
			this.reply(replyToken, confirmInfo());
		else if (customer.getNumInput() > 2) {
			this.reply(replyToken, createInputMenu());
			customer.resetNumInput();
		}
	}

	/**
	 * Checks if client reserves more than 2 seats in a tour. (Discount tour related)
	 * 
	 * Checks info of client to see if they have children.
	 * Adds up all people and if the number is greater than 2, warn them.
	 * 
	 * @param inputOption	input option
	 * @param replyToken		ID of reply message
	 * @param text			user input
	 * @return				a boolean if the reservation intended is greater than 2
	 */
	private boolean checkExcessReserve(int inputOption, String replyToken, String text){
		switch (inputOption) {
			case 4:  {
				int childrenNo = customer.getCustomerNo().getChildrenNo();
				int toodlerNo = customer.getCustomerNo().getToodlerNo();
				if (childrenNo < 0) childrenNo = 0;
				if (toodlerNo < 0) toodlerNo = 0;
				if ((Integer.parseInt(text)) + childrenNo + toodlerNo  > 2) {
					this.reply(replyToken,
							new TextMessage("Each client can reserve 2 seats at most."));
					return true;
				}
				break;
			}
			case 5: {
				int adultNo = customer.getCustomerNo().getAdultNo();
				int toodlerNo = customer.getCustomerNo().getToodlerNo();
				if (adultNo < 0) adultNo = 0;
				if (toodlerNo < 0) toodlerNo = 0;
				if ((Integer.parseInt(text)) + adultNo + toodlerNo  > 2) {
					this.reply(replyToken,
							new TextMessage("Each client can reserve 2 seats at most."));
					return true;
				}
				break;
			}
			case 6: {
				int adultNo = customer.getCustomerNo().getAdultNo();
				int childrenNo = customer.getCustomerNo().getChildrenNo();
				if (adultNo < 0) adultNo = 0;
				if (childrenNo < 0) childrenNo = 0;
				if ((Integer.parseInt(text)) + adultNo + childrenNo  > 2) {
					this.reply(replyToken,
							new TextMessage("Each client can reserve 2 seats at most."));
					return true;
				}
				break;
			}
		}
		return false;
	}

	/**
	 * Input receive function for regular tour.
	 * 
	 * If input is not number when number is wanted, return.
	 * Handles input options
	 * 
	 * @param replyToken		ID of reply message
	 * @param text			user input
	 */
	private void inputReceive(String replyToken, String text) {
		int inputOption = customer.getInputOption();
		if (!numOnlyInfo(inputOption, replyToken, text))
			return;
		switch (inputOption) {
			case 0: { customer.setId(text); break;}
			case 1: { customer.setName(text); break;}
			case 2: { customer.setAge(Integer.parseInt(text)); break;}
			case 3: { customer.setPhoneNum(text); break;}
			case 4: { customer.getCustomerNo().setAdultNo(Integer.parseInt(text)); break;}
			case 5: { customer.getCustomerNo().setChildrenNo(Integer.parseInt(text)); break;}
			case 6: { customer.getCustomerNo().setToodlerNo(Integer.parseInt(text)); break;}
		}
		customer.resetInputOption();
		if (customer.inputFinished())
			this.reply(replyToken, confirmInfo());
		else if (customer.getNumInput() > 2) {
			this.reply(replyToken, createInputMenu());
			customer.resetNumInput();
		}
	}
	
	/**
	 * Returns a boolean indicating if input message is number when in cases where only numbers are legal.
	 * 
	 * If illegal, ask the customer to input numbers only
	 * 
	 * @param inputOption		input option
	 * @param replyToken			ID for reply message
	 * @param text				user input
	 * @return					a boolean indicating if input message is number when in cases where only numbers are legal.
	 */
	private boolean numOnlyInfo(int inputOption, String replyToken, String text) {
		boolean numOnlyInfo = true;
		switch (inputOption) {
			case 2: case 3: case 4: case 5: case 6: {
				if (!(text.matches("\\d*"))) {
					this.reply(replyToken, 
							new TextMessage("Please input numbers only for this option."));
					numOnlyInfo = false;
				}				
				break;
			}			
		}
		return numOnlyInfo;
	}

	/**
	 * Returns a boolean indicating if input message is number when in cases where only numbers are legal. (in preference)
	 * 
	 * If illegal, ask the customer to input numbers only for the preference
	 * @param inputOption	input option
	 * @param replyToken		ID for reply message
	 * @param text			user input
	 * @return				a boolean indicating if input message is number when in cases where only numbers are legal. (in preference)
	 */
	private boolean numOnlyPreference(int inputOption, String replyToken, String text) {
		boolean numOnlyPreference = true;
		switch (inputOption) {
			case 0: case 2: {
				if (!(text.matches("\\d*"))) {
					this.reply(replyToken, 
							new TextMessage("Please input numbers only for this preference."));
					numOnlyPreference = false;
				}				
				break;
			}			
		}
		return numOnlyPreference;
	}
	
	/**
	 * Function double checking information input with client.
	 * 
	 * @return	a list of reply messages
	 */
	private List<Message> confirmInfo() {
		List<Message> multiMessages = new ArrayList<Message>();
		StringBuilder currentInfo = new StringBuilder();
		currentInfo.append("Please confirm you have input the correct info.\n");
		currentInfo.append("Tour chosen: " + customer.getTour().getName() + "\n");
		currentInfo.append("Tour ID: " + customer.getTour().getID() + "\n");
		currentInfo.append("Date: " + database.getSelectedBooking().dateToString() + "\n");
		currentInfo.append("ID: " + customer.getId() + "\n");
		currentInfo.append("Name: " + customer.getName() + "\n");
		currentInfo.append("Age: " + Integer.toString(customer.getAge()) + "\n");
		currentInfo.append("Phone: " + customer.getPhoneNum() + "\n");
		currentInfo.append("No. of Adults: " + Integer.toString(customer.getCustomerNo().getAdultNo()) + "\n");
		currentInfo.append("No. of Children: " + Integer.toString(customer.getCustomerNo().getChildrenNo()) + "\n");
		currentInfo.append("No. of Toddler: " + Integer.toString(customer.getCustomerNo().getToodlerNo()));
		multiMessages.add(new TextMessage(currentInfo.toString()));
		createConfirm("Is the info correct?", multiMessages);
		return multiMessages;
	}
	
	/**
	 * Function informing client of total fee of tour.
	 * 
	 * Informs adult fee,
	 * informs children fee,
	 * kindly inform no charge for toddlers,
	 * informs total fee.
	 * 
	 * Confirm with client if they decide to make booking.
	 * 
	 * @param multiMessages		a list of reply messages
	 */
	private void outputFee(List<Message> multiMessages) {
		customer.calculateFee(database.getSelectedBooking());
		StringBuilder feeInfo = new StringBuilder();
		feeInfo.append("The adult fee is $" + Double.toString(customer.getFee().getAdultFee()) + "\n");
		feeInfo.append("The children fee is $" + Double.toString(customer.getFee().getChildrenFee()) + "\n");
		feeInfo.append("No fee charged for toddlers\n");
		feeInfo.append("The total fee is $" + Double.toString(customer.getFee().getTotalFee()));
		multiMessages.add(new TextMessage(feeInfo.toString()));
		createConfirm("Confirm?", multiMessages);		
	}

	/**
	 * Function informing client of total fee of discount tour.
	 * 
	 * Informs adult fee,
	 * informs children fee,
	 * kindly inform no charge for toddlers,
	 * informs total fee.
	 * 
	 * Confirm with client if they decide to make booking.
	 * 
	 * @param multiMessages		a list of reply messages
	 */
	private void outputDisCountFee(List<Message> multiMessages) {
		customer.calculateDiscountFee(database.getSelectedBooking());
		StringBuilder feeInfo = new StringBuilder();
		feeInfo.append("The adult fee is $" + Double.toString(customer.getFee().getAdultFee()) + "\n");
		feeInfo.append("The children fee is $" + Double.toString(customer.getFee().getChildrenFee()) + "\n");
		feeInfo.append("No fee charged for toddlers\n");
		feeInfo.append("The total fee is $" + Double.toString(customer.getFee().getTotalFee()));
		multiMessages.add(new TextMessage(feeInfo.toString()));
		createConfirm("Confirm?", multiMessages);
	}

	/**
	 * Function attatching customer to a booking.
	 * 
	 * Indicating that they have booked the tour.
	 * increases number of people joining booked tour accordingly
	 * 
	 */
	private void attachCustomerToBooking(){
		Booking booking = database.getSelectedBooking();
		booking.attach(customer);
		//Increase the number of people in the booking
		booking.addCurrentCustomer(customer.getCustomerNo().getTotalNo());
	}

	/**
	 * Creates a Uri
	 * @param path	path to the Uri
	 * @return		a string of characted identifying the resource
	 */
	static String createUri(String path) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUriString();
	}

	private void system(String... args) {
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		try {
			Process start = processBuilder.start();
			int i = start.waitFor();
			log.info("result: {} =>  {}", Arrays.toString(args), i);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {
			log.info("Interrupted", e);
			Thread.currentThread().interrupt();
		}
	}
/*
	private static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
		log.info("Got content-type: {}", responseBody);

		DownloadedContent tempFile = createTempFile(ext);
		try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
			ByteStreams.copy(responseBody.getStream(), outputStream);
			log.info("Saved {}: {}", ext, tempFile);
			return tempFile;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static DownloadedContent createTempFile(String ext) {
		String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString() + '.' + ext;
		Path tempFile = KitchenSinkApplication.downloadedContentDir.resolve(fileName);
		tempFile.toFile().deleteOnExit();
		return new DownloadedContent(tempFile, createUri("/downloaded/" + tempFile.getFileName()));
	}
*/




	public KitchenSinkController() {
		database = new SQLDatabaseEngine();
		customer = new Customer(null, null, -1, null, -1);
		itscLOGIN = System.getenv("ITSC_LOGIN");
	}

	private SQLDatabaseEngine database;
	private Customer customer;	
	private String itscLOGIN;

	public void setCustomer(Customer customer) {this.customer = customer; }
	public Customer getCustomer() { return customer;}

/*
	//The annontation @Value is from the package lombok.Value
	//Basically what it does is to generate constructor and getter for the class below
	//See https://projectlombok.org/features/Value
	@Value
	public static class DownloadedContent {
		Path path;
		String uri;
	}
*/

	//an inner class that gets the user profile and status message
	class ProfileGetter implements BiConsumer<UserProfileResponse, Throwable> {
		private KitchenSinkController ksc;
		private String replyToken;
		private String text;

		public ProfileGetter(KitchenSinkController ksc, String replyToken, String text) {
			this.ksc = ksc;
			this.replyToken = replyToken;
			this.text = text;
		}
		@Override
		public void accept(UserProfileResponse profile, Throwable throwable) {
			if (throwable != null) {
				ksc.replyText(replyToken, throwable.getMessage());
				return;
			}
			ksc.reply(
					replyToken,
					Arrays.asList(new TextMessage(text + " " + profile.getDisplayName() + "!"))
			);
		}
	}



}
