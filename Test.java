import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.sql.* ;
import java.text.SimpleDateFormat;

import javax.mail.*;
import javax.mail.internet.*;
import javax.xml.ws.ProtocolException;

import java.util.Scanner;
//import java.math.* ;

import org.json.*;

public class Test{
	public static String user_id = "";
	public static String api_key = "";
	public static Statement st;
	public static String db_name;
	public static Connection conn;
	
	public static void main(String args[]){
		System.out.println("Welcome to Oculus system....\n");
		/*Connecting localhost database*/
		db_name = "acuity_db";
		db_connect();
		init_statement();
		System.out.println("Connecting with database....\n");
		Scanner sc = new Scanner(System.in).useDelimiter("\n");;
		/*connect with Acuity account*/
		System.out.println("Please enter an acuity email address: ");
		/* log in to oculus*/
		String user_email = "";
		if(sc.hasNext()) {
			user_email = sc.next();
		}
		user_id = get_Acuity_userID(user_email);
		api_key = get_Acuity_auth(user_email);
		System.out.println("user id: "+ "******");
		System.out.println("api_key: " + "*******");
		
		System.out.println("Connecting with Acuity....\n");
		//init_tc();
		
		System.out.println("Please enter an one of the following instructions: \n");
		System.out.println("CREATE \nTIMESLOT \nINVITATION \nSTATUS \nBLOCK \nUNBLOCK \nAPPOINTMENT \nCANCEL \nRESCHEDULE \n");
		//Scanner sc = new Scanner(System.in).useDelimiter("\n");;
	    //int i = sc.nextInt();
	    while (sc.hasNext()) {
	          String instruction = sc.next();
	          
	          switch(instruction) {
	          	case "CREATE":
	          		/*create a new candidate in Oculus with given info, as long as in Acuity*/
	          		System.out.println("Please enter first name: ");
	          		String first_name = "";
	          		if(sc.hasNext()) {
	          			first_name = sc.next();
	          		}
	          		System.out.println("Please enter last name: ");
	          		String last_name = "";
	          		if(sc.hasNext()) {
	          			last_name = sc.next();
	          		}
	          		System.out.println("Please enter email address: ");
	          		String email_addr = "";
	          		if(sc.hasNext()) {
	          			email_addr = sc.next();
	          		}
	          		System.out.println("Please enter phone number: ");
	          		String phone = "";
	          		if(sc.hasNext()) {
	          			phone = sc.next();
	          		}
	          		
	          		create_cand(first_name, last_name, email_addr, phone);
	          		break;
	          	case "TIMESLOT":
	          		/* display current user`s available timeslots within the given time period*/
	          		
	          		System.out.println("All available appointment types are shown below: \n");
	          		//get_time_slots();
	          		get_time_slots_type();
	          		
	          		System.out.println("Please enter an appointment type number: ");
	          		String type = "";
	          		if(sc.hasNext()) {
	          			type = sc.next();
	          		}
	          		String month = "";
	          		System.out.println("Please enter a month with format: \"2019-03\": ");
	          		if(sc.hasNext()) {
	          			month = sc.next();
	          		}
	          		System.out.println("All available appointment dates are shown below: \n");
	          		get_time_slots_date(type, month);
	          		System.out.println("Please enter a date:");
	          		String date = "";
	          		if(sc.hasNext()) {
	          			date = sc.next();
	          		}
	          		System.out.println("All available timeslots are shown below: \n");
	          		get_time_slots_time(date, type);
	          		break;
	          	case "INVITATION":
	          		/*send an email containing appointment invitation page to current candidate(with guid)*/
	          		System.out.println("Sending appointment invitaion to current candidate.......");
	          		send_invitation();
	          		
	          		break;
	          	case "STATUS":
	          		/* synchronize tc database with acuity database, so we can look up given candidate`s status*/
	          		update_tc();
	          		System.out.println("Synchronizing database........ ");
	          		
	          		//System.out.println("to be continued...... going to call update_tc() \n");
	          		System.out.println("Please enter a candidate`s email address: ");
	          		String email = "";
	          		if(sc.hasNext()) {
	          			email = sc.next();
	          		}
	          		checkStatus(email);
	          		
	          		break;
	          	case "BLOCK" :
	          		/* block current user`s timeslots within given dates*/
	          		System.out.println("Please enter a start date with format \"2019-01-01\" \n");
	          		String start = "";
	          		if(sc.hasNext()) {
	          			start = sc.next();
	          		}
	          		/*System.out.println("Please enter a start time with format \"2019-01-01\" \n");
	          		String start_time = "";
	          		if(sc.hasNext()) {
	          			start_time = sc.next();
	          		}*/
	          		System.out.println("Please enter an end date with format \"2019-01-01\" \n");
	          		String end = "";
	          		if(sc.hasNext()) {
	          			end = sc.next();
	          		}
	          		/*System.out.println("Please enter an end time with format \"2019-01-01\" \n");
	          		String end_time = "";
	          		if(sc.hasNext()) {
	          			end_time = sc.next();
	          		}*/
	          		block(start, end);
	          		break;
	          	case "UNBLOCK" :
	          		/* unblock current user`s timeslots within given dates*/
	          		System.out.println("Blocked timeslots within two months are shown below: \n");
	          		display_blocks();
	          		System.out.println("Please enter a block id: \n");
	          		String block_id = "";
	          		if(sc.hasNext()) {
	          			block_id = sc.next();
	          		}
	          		delete_block(block_id);
	          		break;
	          	case "APPOINTMENT":
	          		/*display available appointment types and dates, select one of them and display corresponding appointmets
	          		 * and update tc datebase*/
	          		
	          		System.out.println("please enter a date with format like \"April 5 \"");
	          		//Scanner apmt_sc = new Scanner(System.in);
	          		String appointment_date = "";
	          		if(sc.hasNext()) {
	          			
	          			appointment_date = sc.next();	
	          		}
	          		System.out.println("Your appointments on " + appointment_date + " are shown below:" );
	          		get_req(appointment_date);
	          		//apmt_sc.close();
	          		break;
	          	case "CANCEL":
	          		System.out.println("Please enter an appointment id you want to cancel:");
	          		String apmt_id = "";
	          		if(sc.hasNext()) {
	          			apmt_id = sc.next();
	          		}
	          		cancel(apmt_id);
	          		break;
	          	case "RESCHEDULE":
	          		System.out.println("Please enter an appointment id you want to reschedule: ");
	          		String appointment_id = "";
	          		if(sc.hasNext()) {
	          			appointment_id = sc.next();
	          		}
	          		System.out.println("Please enter a new appointment time with format \"YYYY-MM-DD HH:MM\": ");
	          		String time = "";
	          		if(sc.hasNext()) {
	          			time = sc.next();
	          		}
	          		reschedule(appointment_id, time);
	          		break;
	          	default:
	          		System.out.println("Please enter an one of the following instructions: \n");
	          		System.out.println("CREATE \nTIMESLOT \nINVITATION \nSTATUS \nBOLCK \nUNBLOCK \nAPPOINTMENT \nCANCEL \nRESCHEDULE \n");
	          		break;
	          }
	          
	      }
		
	}
	private static void reschedule(String appointment_id, String time) {
		//input format: YYYY-MM-DD HH-MM
		//transform time format
		int space_idx = time.indexOf(' ');
		String sub_date = time.substring(0, space_idx);
		String sub_time = time.substring(space_idx + 1);
		String date_time = sub_date + "T" + sub_time + ":00-0500";
		URL url;
		StringBuilder resp = new StringBuilder();
		try {
			url = new URL("https://acuityscheduling.com/api/v1/appointments/" + appointment_id + "/reschedule");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		      //set auth
		     byte[] message = (user_id +":"+ api_key).getBytes("UTF-8");
		      String auth = javax.xml.bind.DatatypeConverter.printBase64Binary(message);

		    //System.out.println(auth);
		    connection.setRequestProperty("Authorization", "Basic "+ auth);
		    connection.setRequestProperty("Content-Type", "application/json");
		    connection.setRequestProperty("Accept", "application/json");//not sure if useful
		    
			connection.setRequestMethod("PUT");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			String json_input = "{\"datetime\": \""+ date_time +"\"}";
			System.out.println(json_input);
			OutputStream os = connection.getOutputStream();
			byte[] input = json_input.getBytes("utf-8");
			os.write(input, 0, input.length);           
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
	        
	        for (String line = null; (line = reader.readLine()) != null;) {
	            resp.append(line).append("\n");
	        }
	        System.out.println(resp);
	        if(resp.length() > 0) {
	        		System.out.println("Rescheduled successfully.....");
	        }else {
	        		System.out.println("Rescheduling failed.......");
	        }
	        reader.close();
	        connection.disconnect();
			
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*cancel appointment, and change candidate`s status back to Qualified*/
	private static void cancel(String apmt_id) {
		JSONObject response = cancel_appointment(apmt_id);
		
		System.out.println("Appointment canceled.......");
		String email = response.getString("email");
		cand_status_to_q(email);
		System.out.println("The candidate`s status changed to Qualified.......\n");
		
	}
	/*cancel appointment with given id*/
	private static JSONObject cancel_appointment(String apmt_id) {
		String delete_url = "https://acuityscheduling.com/api/v1/appointments/" + apmt_id +"/cancel";
		StringBuilder resp = new StringBuilder();
		try {
			URL url = new URL(delete_url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		    //set auth
		    byte[] message = (user_id +":"+ api_key).getBytes("UTF-8");
		    String auth = javax.xml.bind.DatatypeConverter.printBase64Binary(message);

		    connection.setRequestProperty("Authorization", "Basic "+ auth);
		    
		    connection.setDoOutput(true);
		    connection.setRequestMethod("PUT");
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("test test, cancel appointment"); /*hope this works*/
			System.out.println("Response Code : " + connection.getResponseCode());
			//out.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		      
		      String line = null;
		      while ((line = reader.readLine()) != null){
		          resp.append(line + "\n");
		        
		      }
		      
		      reader.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject(resp.toString());
	}
	/* query candidate`s email by guid*/
	private static String cand_email(String guid) {
		String email = "";
		//String guid = "guid_test";
		ResultSet rs;
		String query = "SELECT * FROM cand where guid = '" + guid + "';";
		
		try {
			rs = st.executeQuery(query);
			if(rs.next()) {
				String person_id = rs.getString("person_id");
				String person_query = "SELECT * FROM person where id = '" + person_id + "';";
				
				ResultSet person_rs = st.executeQuery(person_query);
				if(person_rs.next()) {
					email = person_rs.getString("email");
					//return email;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return email;
	}
	/*return true if candidate with given email exists*/
	private static boolean cand_exist(String email) {
		String query = "SELECT * FROM tc_db where email = '" + email + "';";
		ResultSet rs;
		try {
			rs = st.executeQuery(query);
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/*send appointment invitation to the given email*/
	private static void send_email(String email) {
		String to = email;
		System.out.println(email);
		String from = "";//edit
		String pwd = "";//edit
		
		Properties properties = new Properties();
		//properties.put("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "587");
		//properties.setProperty("mail.debug", "true");
		//properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.user", from);
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.password", pwd);
		
		Session session = Session.getInstance(properties, 
		          new javax.mail.Authenticator() { 
		             //override the getPasswordAuthentication method 
		            protected PasswordAuthentication getPasswordAuthentication() { 
		                return new PasswordAuthentication(from, pwd); 
		            } 
		          }); 
	    
		try {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("This is the Subject Line!");
	         // Now set the actual message
	         message.setText("This is appointment invitation link", "UTF-8");
	         // Send message
	         
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
	/* change the status of given candiate to RI*/
	private static void cand_status_to_ri(String email) {
		String update = "UPDATE tc_db SET status = 'ri' where email = '" + email + "';";
		try {
			st.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* change the status of given candiate to Q*/
	private static void cand_status_to_q(String email) {
		String update = "UPDATE tc_db SET status = 'q' where email = '" + email + "';";
		try {
			st.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* return the status of given candidate*/
	private static void checkStatus(String email) {
		
		String query = "SELECT * FROM tc_db where email = '" + email + "';";
		
		ResultSet rs;
		
		try {
			rs = st.executeQuery(query);
			if(rs.next()) {
				System.out.println(rs.getString("status"));
				//return rs.getString("status");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Couldn`t find candidate!!!!!!!!!!!");
			//e.printStackTrace();
		}
		
	}
	/* only create new candidate in Oculus*/
	private static void create_cand(String first_name, String last_name, String email, String phone_number) {
		/*if candidate with this email address already existed, then don`t create a new one*/
		if(cand_exist(email)) {
			System.out.println("This candidate already existed......");
		}else {
			String insert = "INSERT INTO tc_db " 
		    	  	+ "VALUES( 0, '" + email + "','"
		    	  	+ phone_number + "','"
		  		+ first_name + "','" 
		    	  	+ last_name + "',"
		    	    + "null" + ","
		    	    + "null" + ","
		    	    + "null" + ", 'Q');";
			//System.out.println(insert);
			try {
				st.executeUpdate(insert);
				System.out.println("Candidate successfully created!!");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/*initiate statement*/
	private static void init_statement() {
		// TODO Auto-generated method stub
		try {
			st = conn.createStatement(
					ResultSet.TYPE_SCROLL_SENSITIVE, 
			        ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* send appointment invitation page by clicking on a button, if it`s status is RI(?), show the button, vice versa*/
	private static void send_invitation() {
		String guid = "guid_test";
		
		String email = cand_email(guid);
		System.out.println(email);
		if(cand_exist(email)) {
			cand_status_to_ri(email);
			cand_canceled_to_false(email);
			send_email(email);
		}else {
			System.out.println("Candidate doesn`t exist, please create it first.......");
		}
	}
	
	private static void cand_canceled_to_false(String email) {
		// TODO Auto-generated method stub
		String update = "UPDATE acuity_db SET canceled = 0 where email = '" + email + "';";
		try {
			st.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* connecting Oculus account with Acuity account, get user_id
	 * using a new table*/
	private static String get_Acuity_userID(String email) {
		
		ResultSet rs;
		String query = "SELECT * FROM acuity_accounts where email = '" + email +  "';";
		try {
			rs = st.executeQuery(query);
			if(rs.next()) {
				return rs.getString("user_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/* connecting Oculus account with Acuity account, get api_key*/
	private static String get_Acuity_auth(String email) {
		
		ResultSet rs;
		String query = "SELECT * FROM acuity_accounts where email = '" + email +  "';";
		try {
			rs = st.executeQuery(query);
			if(rs.next()) {
				return rs.getString("api_key");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/*Block a few timeslots between start time and end time*/
	private static void block(String start_time, String end_time) {
		
		//yu zhang`s calendar id: 2784712
		URL url;
		StringBuilder resp = new StringBuilder();
		try {
			url = new URL("https://acuityscheduling.com/api/v1/blocks");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		      //set auth
		     byte[] message = (user_id +":"+ api_key).getBytes("UTF-8");
		      String auth = javax.xml.bind.DatatypeConverter.printBase64Binary(message);

		    //System.out.println(auth);
		    connection.setRequestProperty("Authorization", "Basic "+ auth);
		    connection.setRequestProperty("Content-Type", "application/json");
		    connection.setRequestProperty("Accept", "application/json");//not sure if useful
		    
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			String json_input = "{\"start\": \" " + start_time + "\", \"end\": \"" + end_time + "\", \"calendarID\": 2784712}";
			System.out.println(json_input);
			OutputStream os = connection.getOutputStream();
			byte[] input = json_input.getBytes("utf-8");
			os.write(input, 0, input.length);           
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
	        
	        for (String line = null; (line = reader.readLine()) != null;) {
	            resp.append(line).append("\n");
	        }
	        System.out.println(resp);
	        if(resp.length() > 0) {
	        		System.out.println("Blocked the given time period successfully.....");
	        }else {
	        		System.out.println("Block failed.......");
	        }
	        reader.close();
	        connection.disconnect();
			
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	/*delete block with given id*/
	private static void delete_block(String block_id) {
		//2721968819
		String delete_url = "https://acuityscheduling.com/api/v1/blocks/" + block_id;
		URL url;
		try {
			url = new URL(delete_url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//set auth
		    byte[] message = (user_id +":"+ api_key).getBytes("UTF-8");
		    String auth = javax.xml.bind.DatatypeConverter.printBase64Binary(message);

		    connection.setRequestProperty("Authorization", "Basic "+ auth);
		    
		    connection.setRequestMethod("DELETE");
		    System.out.println("Response Code : " + connection.getResponseCode());
		    System.out.println("Unblocked successfully.....");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* display each block`s id, start time and end time*/
	private static void display_blocks() {
		String url = "https://acuityscheduling.com/api/v1/blocks";
		JSONArray myresponse = send_acuity_request(url);
		String current_time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.format(Calendar.getInstance().getTime());
		
		//System.out.println(current_time);
		Calendar aft_two_months = Calendar.getInstance();
		aft_two_months.add(Calendar.MONTH, 2);
		String end_time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.format(aft_two_months.getTime());
		//System.out.println(end_time);
		if(conn != null) {
			for(int i = 0; i < myresponse.length(); i++) {
				JSONObject obj = (JSONObject) myresponse.get(i);
				
				String start = (String) obj.get("start");
				//System.out.println(obj);
				if(start.compareTo(current_time) >= 0 && start.compareTo(end_time) <= 0) {
					System.out.print(obj.get("id") + "   ");
					System.out.print(start + "   ");
					System.out.println(obj.getString("end"));
				}
				
			}
		}
	}
	/* initialize database connection */
	private static void db_connect() {
		// TODO Auto-generated method stub
		try {

			/* requesting database connection*/
			Class.forName("com.mysql.jdbc.Driver");
			String Url = "jdbc:mysql://localhost:3306/test?useSSL=false";
			conn = DriverManager.getConnection(Url,"yz","123456");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
	        System.out.println("Connection Failed! Check output console");
	        e.printStackTrace();
	     }
	}
	/*
	    * traverse auity_db, for each tuple, if its email OR phone exists in tc_db, 
	    * update corresponding tuple in tc_db (update date, time, status -> RI, canceled).
	    * if its email AND phone don`t exist in tc_db, create a new row with all the info.
	    * one thing needs to be careful about is that, if a new row is inserted from acuity_db, 
	    * then status is RI(cuz already has appointment), i think that doesn`t make sense in practice though,
	    * so added init_tc_db in addition, which makes status to be "Q", data/time = null */
	private static void update_tc() {
		String traverse = "SELECT * FROM " + db_name + ";";
		//System.out.println(traverse);
		ResultSet rs;
		try {
			rs = st.executeQuery(traverse);
			List<List<String>> store = new ArrayList();
			while(rs.next()) {
				List<String> tmp = Arrays.asList(
						new String[] {rs.getString("email"), rs.getString("phone"), rs.getString("date"), rs.getString("time"),
								rs.getString("canceled"), rs.getString("first_name"), rs.getString("last_name")});
				store.add(tmp);
				
			}
			for(int i = 0; i < store.size(); i++) {
				List<String> cur_list = store.get(i);
				
				/*if canceled = true, then set status to 'Q'*/
				String update = "";
				if(cur_list.get(4).equals("1")) {
					update = "UPDATE tc_db"+ " SET date = '" +
							cur_list.get(2) + "', time = '" + cur_list.get(3) 
							+ "', canceled = " + cur_list.get(4) + ", status = 'Q'"
							+ " WHERE email = '" 
			    			+ cur_list.get(0) + "' OR phone = '" 
			    			+ cur_list.get(1) + "';"; 
				}else {
					update = "UPDATE tc_db"+ " SET date = '" +
							cur_list.get(2) + "', time = '" + cur_list.get(3) 
							+ "', canceled = " + cur_list.get(4) + ", status = 'RI'"
							+ " WHERE email = '" 
			    			+ cur_list.get(0) + "' OR phone = '" 
			    			+ cur_list.get(1) + "';";
				}
				if(st.executeUpdate(update) != 1) {
					//not exist
					//insert
					//System.out.println("going to insert new data into tc_db......");
					String last_name = cur_list.get(6).replaceAll("'", "\\\\'");
					//System.out.println(last_name);
					String insert = "INSERT INTO tc_db " 
				    	  	+ "VALUES( 0, '" + cur_list.get(0) + "','"
				    	  	+ cur_list.get(1) + "','"
				  		+ cur_list.get(5) + "','" 
				    	  	+ last_name + "','"
				    	    + cur_list.get(2) + "','"
				    	    + cur_list.get(3) + "','"
				    	    + cur_list.get(4) + "', 'Q');";
					//System.out.println(insert);
					st.executeUpdate(insert);
				}
			}
			//System.out.println("tc_db updated ......");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("update tc datebase failure.....");
			e.printStackTrace();
		}
	}
	/*
	 * this is just a helper function, in practice it is executed when a candidate`s status move to Q,
	 * then he/she is able to make appointments, this is for simulating the procedure.
	 * you can comment it if don`t need it, actually only one of init_tc and update_tc should execute in a single run */
		
	private static void init_tc() {
		String select = "SELECT * FROM " + db_name + ";";
		ResultSet rs;
		List<List<String>> list = new ArrayList();
		try {
			
			rs = st.executeQuery(select);
			while(rs.next()) {
				List<String> tmp = new ArrayList();
				tmp.add(rs.getString("email"));
				tmp.add(rs.getString("phone"));
				tmp.add(rs.getString("first_name").replaceFirst("'", "\\\\'"));
				tmp.add(rs.getString("last_name").replaceFirst("'", "\\\\'"));
				list.add(tmp);
				
			}
			rs.close();
			for(int i = 0; i < list.size(); i++) {
				List<String> tmp = list.get(i);
				String insert = "INSERT INTO tc_db " 
			    	  	+ "VALUES( 0, '" + tmp.get(0) + "','"
			    	  	+ tmp.get(1) + "','"
			  		+ tmp.get(2) + "','"
			    	  	+ tmp.get(3) + "',"
			    	    + "null" + ","
			    	    + "null" + ", 0, 'Q');";
				
				st.executeUpdate(insert);
				
			}
			System.out.println("tc database initialized........");
		} catch (SQLException e) {
			System.out.println("init tc failure");
			e.printStackTrace();
		}
		
	}
	/* display appointments */
	public static void get_req(String date) {
		
		try {
			   
				 /* showall=true implies cancelled items are displayed, and noShow = true*/
				   String input_url = "https://acuityscheduling.com/api/v1/appointments.json?showall=true";// no hard coded

				   JSONArray myresponse = send_acuity_request(input_url);
				   
				   if(conn != null) {
				    	  //filter json fields
				   for(int i = 0; i < myresponse.length(); i++) {
					    JSONObject obj = (JSONObject) myresponse.get(i);
					    	  
					    	//check if table already exists
					    	DatabaseMetaData dbm = conn.getMetaData();
					    	ResultSet tables = dbm.getTables(null, null, "acuity_db", null);
					    	 
					    	/*if table doesn`t exist*/
					    	if(!tables.next())
					    		create_table();
					    	 
					    	/* this candidate exists when email or phone exists in db*/
					    	String check = "SELECT * from " + db_name + " WHERE email = '" 
					    			+ obj.getString("email") + "' OR phone = '" 
					    			+ obj.getString("phone") + "';";
					   
					    	/*display each appointment`s id, first name, last name, phone, email, type, time, duration  */
					    /* don`t display canceled appointments*/
					    	if(obj.getString("date").contains(date) && (!obj.getBoolean("canceled"))){
					    		//System.out.println(obj);
					    		System.out.println("id: " + obj.get("id") 
					    		+ "   firstName: " + obj.getString("firstName") 
					    		+ "   lastName: " + obj.getString("lastName")
					    		+ "   phone: " + obj.getString("phone")
					    		+ "   email: " + obj.getString("email")
					    		+ "   time: " + obj.getString("time")
					    		+ "   endTime: " + obj.getString("endTime"));
					    	}
					    //	System.out.println(obj);
					    	ResultSet rs = st.executeQuery(check);
					    	if(rs.next()) {
					    		//already exist
					    		update_row(obj);
					    	}else {
					    		//System.out.println("not exist");
					    		add_row(obj);
					    	}
					    	rs.close();
					    	
					    }
				   System.out.println("database updated ..........");
				      }else {
				    	  		System.out.println("sql returns null");
				      }
					
			}
			catch (SQLException e) {
		        System.out.println("Connection Failed! Check output console");
		        e.printStackTrace();
		     }

	}
	/* 
	 * update rows when appointment changes for a specific candidate in acuity database*/
	private static void update_row(JSONObject obj) {
		String update = "UPDATE " + db_name + " SET date = '" +
				obj.getString("date") + "', time = '" + obj.getString("time") 
				+ "', canceled = " + obj.get("canceled") + " WHERE email = '" 
    			+ obj.getString("email") + "' OR phone = '" 
    			+ obj.getString("phone") + "';";
		try {
			st.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("update failure");
		}
	}
	
	private static void create_table() {
		String create_sql = "CREATE TABLE " + db_name
    	  		+ " (ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, "
  				+ "email VARCHAR(64),"
    	  		+ "phone VARCHAR(20),"
    	  		+ "first_name VARCHAR(20), last_name VARCHAR(20), "
    	  		+ "date VARCHAR(20), time VARCHAR(20),"
    	  		+ "canceled VARCHAR(20));";
		try {
			st.executeUpdate(create_sql);
			System.out.println("created a new table in db ..........");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("creating table fails");
			e.printStackTrace();
		}
		
		
	}
	/*
	 * insert new candidates into acuity database*/
	public static void add_row(JSONObject obj) {
		//System.out.println(obj);
		try {
			String insert = "INSERT INTO " + db_name + 
	    	  	" VALUES( 0, '" + obj.getString("email") + "','"
	    	  	+ obj.getString("phone") + "','"
	  		+ obj.getString("firstName").replaceFirst("'", "\\\\'") + "','"
	    	  	+ obj.getString("lastName").replaceFirst("'", "\\\\'") + "','"
	    	    + obj.getString("date") + "','"
	    	    + obj.getString("time") + "', 0);";
			//System.out.println(insert);
		st.executeUpdate(insert);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		System.out.println("insertion error");
		e.printStackTrace();
	}
	}
	/* acuity connection*/
	public static JSONArray send_acuity_request(String input_url) {
		URL url;
		StringBuilder resp = new StringBuilder();
		try {
			url = new URL(input_url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		      //set auth
		      byte[] message = (user_id +":"+ api_key).getBytes("UTF-8");
		      String auth = javax.xml.bind.DatatypeConverter.printBase64Binary(message);

		      connection.setRequestProperty("Authorization", "Basic "+ auth);

		      //set req method
		      connection.setRequestMethod("GET");

		      connection.setReadTimeout(5*1000);
		      connection.connect();
		      // read output from server
		      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		      
		      String line = null;
		      while ((line = reader.readLine()) != null)
		      {
		    	  
		        resp.append(line + "\n");
		        
		      }
		      
		      reader.close();
		}catch(FileNotFoundException fe) {
			System.out.println("cannot find file, good luck");
			fe.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      return new JSONArray(resp.toString());
	}
	
	/* display all the appointment types*/
	public static void get_time_slots_type() {
		 /* showall=true implies cancelled items are displayed, and noShow = true*/
		String input_url = "https://acuityscheduling.com/api/v1/appointment-types?showall=true";
		/*should retrieve name and id from /appointment-types*/
		JSONArray myresponse = send_acuity_request(input_url);
		if(conn != null) {
			for(int i = 0; i < myresponse.length(); i++) {
				JSONObject obj = (JSONObject) myresponse.get(i);
				//System.out.println(obj);
				String name = (String) obj.get("name");
				int appt_type = (int)obj.get("id");
				
				System.out.print(appt_type + "    ");
				System.out.println(name);
			}
		}
	}
	/* display all the dates*/
	public static void get_time_slots_date(String type, String month) {
		String input_url =  "https://acuityscheduling.com/api/v1/availability/dates?month=" + month + "&appointmentTypeID=" 
				+ type + "&showall=true";
		JSONArray myresponse = send_acuity_request(input_url);
		if(conn != null) {
			for(int j = 0; j < myresponse.length(); j++) {
			JSONObject date_obj = (JSONObject) myresponse.get(j);
			//System.out.println(date_obj);
			String date = (String)date_obj.get("date");
			System.out.println(date);
		}
		}
		
	}
	/*display time slots with given date*/
	public static void get_time_slots_time(String date, String type) {

		String input_url = "https://acuityscheduling.com/api/v1/availability/times?date="
				+ date + "&appointmentTypeID=" + type;
		JSONArray myresponse = send_acuity_request(input_url);
		
		if(conn != null) {
			for(int k = 0; k < myresponse.length(); k++) {
				JSONObject time_obj = (JSONObject)myresponse.get(k);
				String time = (String) time_obj.get("time");
				
				System.out.println(time);
				
			}
		}
	}

	
}
