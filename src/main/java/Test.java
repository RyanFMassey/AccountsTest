import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Test {
	/** Application name. */
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            Test.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        Sheets sheetService = getSheetsService();

        // Prints the names and majors of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        String spreadsheetId = "1OOx1_NDL6H4yd3kKBsSM0NHbBc_66uXZGkw2Sk3ryOg";
        String range = "Sheet1!B2:F";
        ValueRange response = sheetService.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {
        	values = blankTo0(values);
        	System.out.println("Date, Card, Delivery, Cash");
        	for (List row : values) {
            // Print columns A and E, which correspond to indices 0 and 4.
        		System.out.println(row.get(0) + ", " + row.get(1) + ", " + row.get(2) + ", " + row.get(3) + ", " + row.get(4));
          }
        }
        
    
        
        
        System.out.println();
        System.out.println("Save to File? Y/N");
        
        Scanner sc = new Scanner(System.in);
        String userInput = sc.next();
        sc.close();
        
        if (userInput.equals("Y")) {
        	writeToFile(values);
        } else if (userInput.equals("N")) {
        	System.out.println("Closing...");
        } else {
        	System.out.println("Please enter a valid input.");
        }
        
    }
    
    private static List<List<Object>> blankTo0 (List<List<Object>> values) {
    	for (List row : values) {
    		for (int i = 0; i < 5; i++) {
    			if (row.get(i) == "") {
    				row.set(i, "0");
            	 }
             }
           }
    	
		return values;
    }
    
    //Practice wirting to file in json format
    private static void writeToFile(List<List<Object>> values) throws FileNotFoundException, UnsupportedEncodingException {
    	System.out.println("Starting Write...");
    	File f = new File("records.json");
    	if(f.exists() && !f.isDirectory()) { 
    	    System.out.println("File already exists. Overwriting...");
    	} else {
    		System.out.println("File not found. Creating...");
    	}
    	
    	try {
			f.createNewFile();
			System.out.println("File created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
   		
   		if (values == null || values.size() == 0) {
   			System.out.println("No data found.");
   		} else {
    		PrintWriter writer = new PrintWriter("records.json", "UTF-8");
    		writer.println("{");
    		writer.println("\t\"records\":[");

    		for (int i = 0; i < values.size(); i++) {
    			writer.println("\t{");
    			writer.println("\t\t\"date\" : \"" + values.get(i).get(0) + "\",");
    			writer.println("\t\t\"card\" : \"" + values.get(i).get(1) + "\",");
    			writer.println("\t\t\"delivery\" : \"" + values.get(i).get(2) + "\",");
    			writer.println("\t\t\"cash\" : \"" + values.get(i).get(3) + "\",");
    			writer.println("\t\t\"total\" : \"" + values.get(i).get(4) + "\"");
    			
    			if (i != values.size() - 1) {
    				writer.println("\t},");
    			} else {
    				writer.println("\t}");
    			}
    		}
    			
    		writer.println("]}");
    		writer.close();
    	}
    	
    	
    }


}