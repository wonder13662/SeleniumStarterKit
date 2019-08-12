package com.google.developers.gmail;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.testng.Assert;

import com.base.util.StringUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages.BatchDelete;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.BatchDeleteMessagesRequest;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;

public class GmailApi {
	private static final String APPLICATION_NAME = "Swingvy Gmail API";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * @doc https://developers.google.com/resources/api-libraries/documentation/gmail/v1/java/latest/?hl=ko
	 * @guide https://developers.google.com/gmail/api/v1/reference/users/messages/get
	 */

	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, String tokenPath)
			throws IOException {
		// Load client secrets.
		InputStream in = GmailApi.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokenPath)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}
	
	private static List<String> fetchEmailIds(String email, String query) {
		
		try {
			Gmail service = getGmail(email);
			
			Assert.assertTrue(StringUtil.isNotEmpty(query), "fetchEmailIds: query is not valid!");
			List<Message> messages = listMessagesMatchingQuery(service, email, query);
			List<String> ids = new ArrayList<String>();
			for (Message message : messages) {
				ids.add(message.getId());
			}
			
			return ids;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static String fecthEmailHTMLContent(String email, String query) {
		Gmail service = getGmail(email);
		try {
			List<String> ids = fetchEmailIds(email, query);
			Assert.assertTrue(ids.size() > 0, String.format("email:%s / ids.size():%d <= 0 at fecthEmailHTMLContent", email, ids.size()));
			String lastId = ids.get(ids.size() - 1);

			Message message = service.users().messages().get(email, lastId).execute();
			Assert.assertTrue(message != null, "fetchMessage: No message found!");

			// https://stackoverflow.com/questions/28026099/how-to-get-full-message-body-in-gmail?rq=1
			MessagePart part = message.getPayload();
			String html = StringUtils.newStringUtf8(Base64.decodeBase64(part.getBody().getData()));
			
			return html;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void deleteEmails(String email, String query) {
		Gmail service = getGmail(email);
		try {
			List<String> ids = fetchEmailIds(email, query);
			if(ids.size() > 0) {
				BatchDelete batchDelete = service.users().messages().batchDelete(email, new BatchDeleteMessagesRequest().setIds(ids));
				batchDelete.execute();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Gmail getGmail(String email) {
		try {
			Assert.assertTrue(StringUtil.isNotEmpty(email), "deleteEmails: email is not valid!");
			String tokenPath = getTokenPath(email);
			Assert.assertTrue(StringUtil.isNotEmpty(tokenPath), "deleteEmails: tokenPath is not valid!");
			return initialize(tokenPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	/**
	 * Immediately and permanently deletes the specified thread. This operation
	 * cannot be undone. Prefer threads.trash instead.
	 *
	 * @param service  Authorized Gmail API instance.
	 * @param userId   User's email address. The special value "me" can be used to
	 *                 indicate the authenticated user.
	 * @param threadId ID of Thread to delete.
	 * @throws IOException
	 */
	public static void deleteThread(Gmail service, String userId, String threadId) throws IOException {
		service.users().threads().delete(userId, threadId).execute();
	}

	/**
	 * https://developers.google.com/gmail/api/v1/reference/users/messages/list List
	 * all Messages of the user's mailbox matching the query.
	 *
	 * @param service Authorized Gmail API instance.
	 * @param userId  User's email address. The special value "me" can be used to
	 *                indicate the authenticated user.
	 * @param query   String used to filter the Messages listed.
	 * @throws IOException
	 */
	private static List<Message> listMessagesMatchingQuery(Gmail service, String userId, String query)
			throws IOException {
		ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setQ(query).setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		return messages;
	}

	private static String getTokenPath(String email) {
		return String.format("%s/%s", TOKENS_DIRECTORY_PATH, email.hashCode());
	}

	private static Gmail initialize(String tokenPath) throws IOException, GeneralSecurityException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, tokenPath))
				.setApplicationName(APPLICATION_NAME).build();
	}
}