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

	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
			log.info("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private void replyText(@NonNull String replyToken, @NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "..";
		}
		this.reply(replyToken, new TextMessage(message));
	}


	private void handleSticker(String replyToken, StickerMessageContent content) {
		reply(replyToken, new StickerMessage(content.getPackageId(), content.getStickerId()));
	}

	private void handleTextContent(String replyToken, Event event, TextMessageContent content)
			throws Exception {
		String text = content.getText();

		log.info("Got text message from {}: {}", replyToken, text);
		//0 for searching, 1 for confirm tour, 2 for ask input
        int stage = customer.getStage();
        switch (stage) {            
        	case 0: {
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

				//Creating Filter Result & Template Messages if filtering is done
				this.reply(replyToken, createMessages(reply, text));
				break;
			}
        	
        	case 1: {
        		if ((text.toLowerCase().matches("no(.)*"))) {
        			this.replyText(replyToken, "Okay. You may continue searching for other tours.");
    				customer.stageRestore();    	
    				break;
        		}
        		customer.setTour(database.getSelectedTour());
        		this.reply(replyToken, createInputMenu());
        		customer.stageProceed();
                break;
        	}
        	
        	case 2: {
        		String reply = null;        		    			
        		if (customer.getInputOption() == -1)
        			askInputReply(replyToken, text);
        		else
        			inputReceive(replyToken, text);        			
        		break;
        	}
        	
        	case 3: {
        		if ((text.toLowerCase().matches("no(.)*"))) {
        			List<Message> multiMessages = new ArrayList<Message>();
        			multiMessages.add(new TextMessage("Okay. You may input your info again."));
        			multiMessages.add(createInputMenu());
        			this.reply(replyToken, multiMessages);
    				customer.stageRestore();    	
    				break;
        		}
        		outputFee(replyToken);
    			customer.stageZero();    	
    			break;        		
        	}        	
		}
	}

	private List<Message> createMessages(String reply, String text){
		List<Message> multiMessages = new ArrayList<Message>();
		multiMessages.add(new TextMessage(reply));
		if (text.matches("I want to enroll in(.)*")) {
			createConfirm("Do you want to book this one?", multiMessages);
		}		
		createFilterMenu(text, multiMessages);
		return multiMessages;
	}
	
	private void createConfirm(String question, List<Message> multiMessages) {
		customer.stageProceed();
    	ConfirmTemplate confirmTemplate = new ConfirmTemplate(
    			question, 
    			new MessageAction("Yes", "Yes"), 
    			new MessageAction("No", "No")
        );
    	multiMessages.add(new TemplateMessage("Confirm alt text", confirmTemplate));		
	}
	
	private void createFilterMenu(String text, List<Message> multiMessages) {
		List<Tour> tourList = database.getTourList();    
		if (tourList != null) {
			List<CarouselTemplate> carouselTemplate = new ArrayList<CarouselTemplate>();
			List<CarouselColumn> carouselColumn;
			List<Action> tourEnroll;
			int count = 0;
			int numTour = tourList.size();
			int templateCount = 0;        	
			while (count < numTour) {
				carouselColumn = new ArrayList<CarouselColumn>();
				for (int columnCount = 0; columnCount < 5 && count < numTour; columnCount++) {            		
					tourEnroll = new ArrayList<Action>();            			
					for (int actionCount = 0; actionCount < 3 && count < numTour; actionCount++) {            			
						String tourID = tourList.get(count).getID();
						tourEnroll.add(new MessageAction(
								tourID, "I want to enroll in " + tourID + "."));
						count++;
						if (columnCount != 0 && actionCount+1 < 3 && count == numTour) {
							for (int temp = actionCount+1; temp < 3; temp++) {
								tourEnroll.add(new MessageAction(" ", " "));
							}
						}
					}
					carouselColumn.add(new CarouselColumn(null, null, "Tour Selection", tourEnroll));
				}
				carouselTemplate.add(new CarouselTemplate(carouselColumn));
				multiMessages.add(new TemplateMessage("Carousel alt text", carouselTemplate.get(templateCount++)));
			}
		}
		database.resetTourList();
	}
	
	private TemplateMessage createInputMenu() {
		CarouselTemplate carouselTemplate = new CarouselTemplate(
				Arrays.asList(
						new CarouselColumn(null, null, "Please select the info you want to input", Arrays.asList(
								new MessageAction("ID", "ID"),
								new MessageAction("Name", "Name"), 
								new MessageAction("Age", "Age")
						)),
						new CarouselColumn(null, null, "Please select the info you want to input", Arrays.asList(
								new MessageAction("No. of Adults", "No. of Adults"),
								new MessageAction("No. of Children", "No. of Children"), 
								new MessageAction("No. of Toodlers", "No. of Toodlers")
						))
				));
		return new TemplateMessage("Carousel alt text", carouselTemplate);
		
	}
	
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
			case "No. of Adults": {
				this.replyText(replyToken, askInput("No. of Adults"));
				customer.setInputOption(3);
				break;
			}
			case "No. of Children": {
				this.replyText(replyToken, askInput("No. of Children"));
				customer.setInputOption(4);
				break;
			}
			case "No. of Toodlers": {
				this.replyText(replyToken, askInput("No. of Toodlers"));
				customer.setInputOption(5);
				break;
			}
		}
	}
	
	private String askInput(String option) {
		return "Please input " + option + ".";
	}
	
	private void inputReceive(String replyToken, String text) {
		int inputOption = customer.getInputOption();
		switch (inputOption) {
			case 0: { customer.setId(text); break;}
			case 1: { customer.setName(text); break;}
			case 2: { customer.setAge(Integer.parseInt(text)); break;}
			case 3: { customer.getCustomerNo().setAdultNo(Integer.parseInt(text)); break;}
			case 4: { customer.getCustomerNo().setChildrenNo(Integer.parseInt(text)); break;}
			case 5: { customer.getCustomerNo().setToodlerNo(Integer.parseInt(text)); break;}
		}
		customer.resetInputOption();
		if (customer.inputFinished())
			this.reply(replyToken, confirmInfo());
		else if (customer.getNumInput() > 2) {
			this.reply(replyToken, createInputMenu());
			customer.resetNumInput();
		}
	}
	
	private List<Message> confirmInfo() {
		List<Message> multiMessages = new ArrayList<Message>();
		StringBuilder currentInfo = new StringBuilder();
		currentInfo.append("Please confirm you have input the correct info.\n");
		currentInfo.append("Tour chosen: " + customer.getTour().getID() + customer.getTour().getName() + "\n");
		currentInfo.append("ID: " + customer.getId() + "\n");
		currentInfo.append("Name: " + customer.getName() + "\n");
		currentInfo.append("Age: " + Integer.toString(customer.getAge()) + "\n");
		currentInfo.append("No. of Adults: " + Integer.toString(customer.getCustomerNo().getAdultNo()) + "\n");
		currentInfo.append("No. of Children: " + Integer.toString(customer.getCustomerNo().getChildrenNo()) + "\n");
		currentInfo.append("No. of Toodler: " + Integer.toString(customer.getCustomerNo().getToodlerNo()));
		multiMessages.add(new TextMessage(currentInfo.toString()));
		createConfirm("Is the info correct?", multiMessages);
		return multiMessages;
	}
	
	private void outputFee(String replyToken) {
		customer.calculateFee();
		StringBuilder feeInfo = new StringBuilder();
		feeInfo.append("The adult fee is $" + Double.toString(customer.getFee().getAdultFee()) + "\n");
		feeInfo.append("The children fee is $" + Double.toString(customer.getFee().getChildrenFee()) + "\n");
		feeInfo.append("No fee charged for toodlers\n");
		feeInfo.append("The total fee is $" + Double.toString(customer.getFee().getTotalFee()));
		this.reply(replyToken, new TextMessage(feeInfo.toString()));
	}
	
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





	public KitchenSinkController() {
		database = new SQLDatabaseEngine();
		customer = new Customer(null, null, -1, null, -1);
		itscLOGIN = System.getenv("ITSC_LOGIN");
	}

	private SQLDatabaseEngine database;
	private Customer customer;	
	private String itscLOGIN;


	//The annontation @Value is from the package lombok.Value
	//Basically what it does is to generate constructor and getter for the class below
	//See https://projectlombok.org/features/Value
	@Value
	public static class DownloadedContent {
		Path path;
		String uri;
	}


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