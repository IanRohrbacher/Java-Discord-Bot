package Main.Commands;

import java.awt.Color;
import java.sql.*;

import Main.Keys;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.WidgetUtil.Widget.Member;

public class DataBaseCommands {
	private Connection connection;
	
	public DataBaseCommands() {
		try {
			//                                                                    :name of db
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/guildbotdb", "root", Keys.mySQLKey);
			System.out.println("------------------- DateBase Connected -------------------");
		} catch (SQLException e) {
			System.out.println("-!-!-!-!-!-!-!-!-!- DateBase Did Not Connect -!-!-!-!-!-!-!-!-!-");
		}
	}
	
	public void printData(MessageReceivedEvent event, String dbName) {
		String DBString = "";
		
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("select *from " + dbName);
			// sets up a embed to print the data base in a embed
			EmbedBuilder embedDB = new EmbedBuilder();
			embedDB.setTitle("**" + dbName + " Date Base**");
			
			switch(dbName) {
				case "user":
					
					while(result.next()) {
						int id = result.getInt("id");
						String UserId = result.getString("UserId");
						String UserName = result.getString("UserName");
						
						DBString += "id: " + id + " | UserId: " + UserId + " | UserName: " + UserName + "\n";
						//System.out.println("id:" + id + " | UserId:" + UserId + " | UserName:" + UserName);
					}
					break;
			
				default:
					break;
			}
			
			embedDB.addField("", DBString, false);
			embedDB.setColor(Color.blue);
			event.getChannel().sendMessageEmbeds(embedDB.build()).queue();
			
			embedDB.clear();
			statement.close();
			result.close();
			
			//System.out.println("\"------------------- Printing The Date Base -------------------\"");
		} catch (SQLException e) {
			event.getChannel().sendMessage("Inproper Inputs For This Command").queue();
			//System.out.println("-!-!-!-!-!-!-!-!-!- Error While Printing The Date Base -!-!-!-!-!-!-!-!-!-");
		}
	}
	
	public void changeData(MessageReceivedEvent event, String dbName, String change, String where, String changeTo) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("update "+dbName+" set "+change+"=? where id=?"); // sets a value of UserName thats #1 where id is #2
			preparedStatement.setString(1, changeTo); // (#1, what you want to change)
			preparedStatement.setString(2, where); // (#2, the id number of the user)
			
			preparedStatement.executeUpdate();
			event.getChannel().sendMessage("Date Changing...").queue();
			//System.out.println("\"------------------- Updating The Date Base -------------------\"");
		} catch (SQLException e) {
			event.getChannel().sendMessage("Inproper Inputs For This Command").queue();
			//System.out.println("-!-!-!-!-!-!-!-!-!- Error While Updating The Date Base -!-!-!-!-!-!-!-!-!-");
		}
	}
	
	public void addNewDataToUser(MessageReceivedEvent event, String one, String two, String three, String four) { //INSERT INTO `guildbotdb`.`user` (`id`, `UserId`, `UserName`) VALUES ('', 'shadow', 'viper');

		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `guildbotdb`.`user` (`UserId`, `UserName`, `Roles`, `Inventory`) VALUES ('"+one+"', '"+two+"', '"+three+"', '"+four+"')");
			preparedStatement.executeUpdate();
			
			event.getChannel().sendMessage("Adding Data...").queue();
			//System.out.println("\"------------------- Added New Data To The Date Base -------------------\"");
		} catch (SQLException e) {
			event.getChannel().sendMessage("Inproper Inputs For This Command").queue();
			//System.out.println("-!-!-!-!-!-!-!-!-!- Error While Adding New Data The Date Base -!-!-!-!-!-!-!-!-!-");
			System.out.println(one + " " + two + " " + three + " " + four);
		}
	}
	
	public void makeAccount(MessageReceivedEvent event, String UserId, String UserName) {
		try {
			boolean account = true;

			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("select UserId from User");
			while(result.next()) {
				if(UserId.contentEquals(result.getString("UserId"))) {
					account = false;
					break;
				}
			}
			
			if(account) {
				try {
					PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `guildbotdb`.`user` (`UserId`, `UserName`) VALUES ('"+UserId+"', '"+UserName+"')");
					preparedStatement.executeUpdate();
					
					event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("Civilian", false).get(0)).queue();;			
					
					event.getChannel().sendMessage("Welcome " + UserName + " To The Sever!").queue();
					//System.out.println("\"------------------- Added New Data To The Date Base -------------------\"");
				} catch (SQLException e) {
					event.getChannel().sendMessage("Inproper Inputs For This Command").queue();
					//System.out.println("-!-!-!-!-!-!-!-!-!- Error While Adding New Data The Date Base -!-!-!-!-!-!-!-!-!-");
				}
			} else {
				event.getChannel().sendMessage(UserName + "Was Alrady Created").queue();
			}
			
			statement.close();
			result.close();
			
		} catch (SQLException e) {
				event.getChannel().sendMessage(UserName + ", Something when wrong").queue();
				//System.out.println("-!-!-!-!-!-!-!-!-!- Error While Printing The Date Base -!-!-!-!-!-!-!-!-!-");
			}
	}
	
	public void updateUser(MessageReceivedEvent event, String name, String userId) { 
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("select *from user");
			
			while(result.next()) {
				String userIdHold = result.getString("UserId");
				String userNameHold = result.getString("UserName");

				if(userIdHold.equals(userId)) {
					if(!userNameHold.equals(name)) {
						PreparedStatement preparedStatement = connection.prepareStatement("update user set UserName=? where UserId=?"); // sets a value of UserName thats #1 where id is #2
					preparedStatement.setString(1, name);
					preparedStatement.setString(2, userId);
					
					preparedStatement.executeUpdate();
					} else { //System.out.println("-!-!-!-!-!-!-!-!-!- Nothing Happened -!-!-!-!-!-!-!-!-!-"); 
					}
				}
			}
			
			statement.close();
			result.close();
		
			//System.out.println("\"------------------- Updated User's Name -------------------\"");
		} catch (SQLException e) {
			System.out.println("-!-!-!-!-!-!-!-!-!- Error autoAddUser -!-!-!-!-!-!-!-!-!-");
		}
	}
	
	// AddOrRemove - true will add to the total amount false will remove to the total amount
	public void changeItem(MessageReceivedEvent event, String UserName, String ItemName, int Amount, boolean AddOrRemove) { //--------make it add to if item already in inv		
		try {
			Statement userStatement = connection.createStatement();
			ResultSet resultUser = userStatement.executeQuery("select *from user");
			Statement inventoryStatement = connection.createStatement();
			ResultSet resultInventory = inventoryStatement.executeQuery("select *from inventory");
			String userId = "";
			boolean itemNotFound = true;
			
			while(resultUser.next() && itemNotFound) {
				String userName = resultUser.getString("UserName");
				if(UserName.equals(userName)) {
					userId = resultUser.getString("UserId");
					
					while(resultInventory.next() && itemNotFound) {
						int inventroyId = resultInventory.getInt("Id");
						String inventoryUserId = resultInventory.getString("UserId");
						String itemHeld = resultInventory.getString("ItemName");
						int inventroyAmount = resultInventory.getInt("ItemAmount");
					
						if(userId.equals(inventoryUserId) && itemHeld.equals(ItemName)) {
							try {
								if(AddOrRemove) { inventroyAmount += Amount; } else { inventroyAmount -= Amount; } // --------------TEST OF REMOVING WORKS-----------------------
								
								if(Amount >= 0) {
									PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `guildbotdb`.`inventory` SET `ItemAmount` = '"+inventroyAmount+"' WHERE (`Id` = '"+inventroyId+"')");
									preparedStatement.executeUpdate();
									
									if(AddOrRemove) { event.getChannel().sendMessage("Adding More Items...").queue(); } else { event.getChannel().sendMessage("Removing More Items...").queue(); }
									//System.out.println("\"------------------- Added New Data To The Date Base -------------------\"");
									itemNotFound = false;
								} else {
									event.getChannel().sendMessage("You Can't Have Less Then 0").queue();
								}
							} catch (SQLException e) {
								event.getChannel().sendMessage("Error Adding More Items").queue();
								//System.out.println("-!-!-!-!-!-!-!-!-!- Error While Adding New Data The Date Base -!-!-!-!-!-!-!-!-!-");
							}
						}
					}
				} else { System.out.println("Nothing Happened"); }
			}
				
			if(itemNotFound) {
				try {
					PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `guildbotdb`.`inventory` (`UserId`, `ItemName`, `ItemAmount`) VALUES ('"+userId+"', '"+ItemName+"', '"+Amount+"')");
					preparedStatement.executeUpdate();
					
					event.getChannel().sendMessage("Adding Item...").queue();
					//System.out.println("\"------------------- Added New Data To The Date Base -------------------\"");
				} catch (SQLException e) {
					event.getChannel().sendMessage("Inproper Inputs For This Command").queue();
					//System.out.println("-!-!-!-!-!-!-!-!-!- Error While Adding New Data The Date Base -!-!-!-!-!-!-!-!-!-");
				}
			}
			
			
		} catch (SQLException e) {System.out.println("Full error out"); }
	}
	
	public void printItems(MessageReceivedEvent event, String UserId) {
		String DBString = "";
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("select *from inventory");
			// sets up a embed to print the data base in a embed
			EmbedBuilder embedDB = new EmbedBuilder();
			
			while(result.next()) {
				String UserIdData = result.getString("UserId");
				if(UserId.equals(UserIdData)) {
					int amount = result.getInt("ItemAmount");
					String itemName = result.getString("ItemName");
					if(amount > 0) {
						DBString += "Item Name: "+itemName+"\t|\tAmount: "+amount+"\n";
						//System.out.println("id:" + id + " | UserId:" + UserId + " | UserName:" + UserName);
					} else { System.out.println("User Had 0 of " + itemName); }
				} else { System.out.println("Nothing Happened"); }
			}
			// inventory message if you don't have anything
			if(DBString.equals("")) {
				DBString = "Your a Broke Loser. Everyone Point and laugh";
			}
			embedDB.addField("**Your Inventory**", DBString, false);
			embedDB.setColor(Color.gray);
			event.getChannel().sendMessageEmbeds(embedDB.build()).queue();
			
			embedDB.clear();
			statement.close();
			result.close();
			
			//System.out.println("\"------------------- Printing The Date Base -------------------\"");
		} catch (SQLException e) {
			event.getChannel().sendMessage("Your Inventory Is Empty").queue();
			//System.out.println("-!-!-!-!-!-!-!-!-!- Error While Printing The Date Base -!-!-!-!-!-!-!-!-!-");
		}
	}
	
	public void printHealth(MessageReceivedEvent event, String UserId, int changeInHealth) {
		String DBString = "[ ";
		String UserName = "";
		int health = -1;
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("select *from user");
			// sets up a embed to print the data base in a embed
			EmbedBuilder embedDB = new EmbedBuilder();
			
			while(result.next()) {
				String UserIdData = result.getString("UserId");
				if(UserId.equals(UserIdData)) {
					health = result.getInt("UserHealth");
					UserName = result.getString("UserName");
					if(health == 0) {
						DBString += "You Are At Zero HP, You Need To Rest Up Before You Can Continue";
					}
					break;
				} else { System.out.println("Nothing Happened"); }
			}
			System.out.println(changeInHealth);
			int runNormalDashes = (int)((health / 10.) + 0.5);
			int runOtherDashes = (changeInHealth>=0) ? (int)((changeInHealth / 10.) + 0.5) : (int)((changeInHealth / 10.) - 0.5);
			System.out.println(""+runNormalDashes + " | " + runOtherDashes);
			
			for(int i = 1; i <= 10; i++) {
				if(i <= runNormalDashes) {
					DBString += "- ";
				}
				if(i >= (runNormalDashes-(runNormalDashes + runOtherDashes)) && i <= ()) { // ahhhhhhhhhhhhhhhhhhhhhhhhhhhhh
					DBString += "/ ";
				} else {
					DBString += "  ";
				}
			}
			if(!DBString.equals("You Are At Zero HP, You Need To Rest Up Before You Can Continue")) {
				DBString += "]";
			}
			embedDB.addField("**"+UserName+" HP**", DBString+" "+health+"/100", false);
			embedDB.setColor(Color.gray);
			event.getChannel().sendMessageEmbeds(embedDB.build()).queue();
			
			embedDB.clear();
			statement.close();
			result.close();
			
			//System.out.println("\"------------------- Printing The Date Base -------------------\"");
		} catch (SQLException e) {
			System.out.println("-!-!-!-!-!-!-!-!-!- Error While Printing Health Bar -!-!-!-!-!-!-!-!-!-");
		}
	}
	
	public void changeHealth(MessageReceivedEvent event, String UserName, int changeInHealth, boolean AddOrRemove) {
		int health;
		String Id;
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("select *from user");
			
			while(result.next()) {
				String UserNameData = result.getString("UserName");
				if(UserName.equals(UserNameData)) {
					health = result.getInt("UserHealth");
					String UserId = result.getString("UserId");
					Id = result.getString("Id");
										
					if(AddOrRemove) { // --------------TEST OF REMOVING WORKS-----------------------
						if(health + changeInHealth > 100) { 
							health = 100; 
						} else {
						health += changeInHealth; 
						}
					} else {
						if(health - changeInHealth < 0) { 
							health = 0; 
						} else {
						health -= changeInHealth; 
						} 
					} 
					
					System.out.println(health);
					System.out.println(Id);
					PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `guildbotdb`.`user` SET `UserHealth` = '"+health+"' WHERE (`Id` = '"+Id+"')");
					preparedStatement.executeUpdate(); // ---------------------not updating??? -----------------------
					
					if(AddOrRemove) { event.getChannel().sendMessage("Adding Health...").queue(); } else { event.getChannel().sendMessage("Removing Health...").queue(); }
					//System.out.println("\"------------------- Added New Data To The Date Base -------------------\"");
					
					// print new health
					if(AddOrRemove) { printHealth(event, UserId, changeInHealth); } else { printHealth(event, UserId, -(changeInHealth)); }
					
					break;
				} else { System.out.println("Nothing Happened"); }
			}			
			//System.out.println("\"------------------- Printing The Date Base -------------------\"");
		} catch (SQLException e) {
			System.out.println("-!-!-!-!-!-!-!-!-!- Error While Changing Health -!-!-!-!-!-!-!-!-!-");
		}
	}
}
