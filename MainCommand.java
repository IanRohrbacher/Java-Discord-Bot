package Main;

import java.awt.Color;

import Main.Commands.DataBaseCommands;
import Main.Commands.DeleteCommand;
import Main.Commands.HelpCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MainCommand extends ListenerAdapter {
	public DataBaseCommands dataBase = new DataBaseCommands();

	
	public void onMessageReceived(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split(" ");
		
		// prints into console what a user said and where it said it
		if(!(event.getAuthor().isBot())) {
			System.out.print(event.getAuthor().getName() + " Said \'");
			for(int i = 0; i < args.length; i++) {
				System.out.print(args[i]);
				if(i < args.length - 1) {
					System.out.print(" ");
				}
			}
			System.out.println("\' In \'" + event.getChannel().getName() + "\' Channel");
			
			// Updates users user name if they changed it
			dataBase.updateUser(event, event.getAuthor().getName(), event.getAuthor().getId());
		}
		
		
		// checks if that user has admin
		boolean hasAdmin = false;
		for(int i = 0; i < event.getMember().getRoles().size(); i++){
			if("admin".equals(event.getMember().getRoles().get(i).getName())){
				hasAdmin = true;
			}
		}
		
		
		// these are the main commands
		
		// help command; show a list of all available commands
		if(args[0].equalsIgnoreCase(Keys.prefix + "help")) {
			HelpCommand.run(event, args, hasAdmin);
		}
		
		// delete the last x amount of line where 0 < x < 100 and not -1
		if(args[0].equalsIgnoreCase(Keys.prefix + "delete") && hasAdmin) {
			DeleteCommand.run(event, args);
		}
		
		// makes a user in the date base if there isn't one yet
		if(event.getChannel().getName().equalsIgnoreCase("create-an-account")) {
			if(args[0].equalsIgnoreCase(Keys.prefix + "create-an-account")) {
				dataBase.makeAccount(event, event.getAuthor().getId(), event.getAuthor().getName());
			} else if(!event.getAuthor().isBot()) {
				event.getMessage().delete().queue();
			}
		}
		
		
		// below are test commands used for testing concepts or ideas | ADMIN ONLY!!!
		
		if(hasAdmin) {
			// test command; shows the bot is working
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase(Keys.prefix + "test") && args[1].equalsIgnoreCase("again")) {
					event.getChannel().sendMessage("running a test but cooler").queue();
				}
			}
			else if(args[0].equalsIgnoreCase(Keys.prefix + "test")) {
				event.getChannel().sendMessage("running a test").queue();
			}
					
			// embed command; sends a test embed message
			if(args[0].equalsIgnoreCase(Keys.prefix + "embed")) {
				EmbedBuilder embedTest = new EmbedBuilder();
				embedTest.setTitle("Title", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
				embedTest.setDescription("Description");
				embedTest.addField("Field1", "Field1 Within", false);
				embedTest.addField("Field2", "Field2 Within", false);
				embedTest.setFooter("Bot created by ShadowViper", "https://cdn.discordapp.com/avatars/383064652176687116/3978ec495c5780ec76176c7a802e59b0.webp?size=100");
				embedTest.setColor(Color.red);
				event.getChannel().sendMessageEmbeds(embedTest.build()).queue();
				embedTest.clear();
				
			}	
			
			// count command; counts the number of times a user uses this command
			if(args[0].equalsIgnoreCase(Keys.prefix + "fix")) {
				event.getChannel().sendMessage("This helps fix the bot").queue();
			}
			
			// test data base
			if(args[0].equalsIgnoreCase(Keys.prefix + "printdb")) {
				dataBase.printData(event, args[1]);
			}
			if(args[0].equalsIgnoreCase(Keys.prefix + "changedb")) {
				dataBase.changeData(event, args[1], args[2], args[3], args[4]);
			}
			if(args[0].equalsIgnoreCase(Keys.prefix + "addUser")) {
				dataBase.addNewDataToUser(event, args[1], args[2], args[3], args[4]);
			}
			if(args[0].equalsIgnoreCase(Keys.prefix + "additem")) {
				String itemName = "";
				for(int i = 2; i < args.length - 1; i++) {
					itemName += args[i];
					if(i != args.length - 2) {
						itemName += " ";
					}
				}
		        try {
		            dataBase.changeItem(event, args[1], itemName, Integer.parseInt(args[args.length-1]), true);
		        } catch (NumberFormatException ex) {
					event.getChannel().sendMessage("Invalid Number At End").queue();
				}
			}
			if(args[0].equalsIgnoreCase(Keys.prefix + "removeitem")) {
				String itemName = "";
				for(int i = 2; i < args.length - 1; i++) {
					itemName += args[i];
					if(i != args.length - 2) {
						itemName += " ";
					}
				}
		        try{
		            dataBase.changeItem(event, args[1], itemName, Integer.parseInt(args[args.length-1]), false);
		        } catch (NumberFormatException ex) {
					event.getChannel().sendMessage("Invalid Number At End").queue();
				}
			}
			if(args[0].equalsIgnoreCase(Keys.prefix + "inventory")) {
				dataBase.printItems(event, event.getAuthor().getId());
			}
			if(args[0].equalsIgnoreCase(Keys.prefix + "health")) {
				dataBase.printHealth(event, event.getAuthor().getId(), 0);
			}
			if(args[0].equalsIgnoreCase(Keys.prefix + "deal")) {
				try{
					dataBase.changeHealth(event, args[1], Integer.parseInt(args[2]), false);
		        } catch (NumberFormatException ex) {
					event.getChannel().sendMessage("Invalid Number At End").queue();
				}
			}
			if(args[0].equalsIgnoreCase(Keys.prefix + "heal")) {
				try{
					dataBase.changeHealth(event, args[1], Integer.parseInt(args[2]), true);
		        } catch (NumberFormatException ex) {
					event.getChannel().sendMessage("Invalid Number At End").queue();
				}
			}
			
			// giverole command; only lets ShadowViper use this command; given the parameters of a "user" and a "role" it will give that user that role
			/* delete while working on it
			if(event.getAuthor().getAsTag().equals("ShadowViper#5330")) { // fix so its only ShadowViper can use it
				if(args[0].equalsIgnoreCase(prefix + "giverole")) {
					if(event.getMessage().getMentionedRoles().toArray().length == 1) {
						if(event.getMessage().getMentionedUsers().toArray().length == 1) {
							Member member = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
							Role roleToGive = event.getMessage().getMentionedRoles().get(0);
							event.getGuild().addRoleToMember(member, roleToGive).queue();
							event.getChannel().sendMessage("Gave the role" + roleToGive + " to " + member).queue();
						} else
							event.getChannel().sendMessage("Please Only Type One User At a Time").queue();
					} else
						event.getChannel().sendMessage("Please Only Type One Role At a Time").queue();
				}
				else if(args[0].equalsIgnoreCase(prefix + "removerole")) {
					if(event.getMessage().getMentionedRoles().toArray().length == 1) {
						if(event.getMessage().getMentionedUsers().toArray().length == 1) {
							Member member = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
							Role roleToGive = event.getMessage().getMentionedRoles().get(0);
							event.getGuild().removeRoleFromMember(member, roleToGive).queue();
							event.getChannel().sendMessage("removed the role" + roleToGive + " to " + member).queue();
						} else
							event.getChannel().sendMessage("Please Only Type One User At a Time").queue();
					} else
						event.getChannel().sendMessage("Please Only Type One Role At a Time").queue();
				}
			}
			
			// mute command; Only Admins can run; given the parameters of a "user" and a "minutes" it will mute that user for that long
			/* delete while working on it
			if(event.getAuthor().getAsTag() == "ShadowViper#5330") { // change so its all people with the role admins ---------------------------
				if(args[0].equalsIgnoreCase(prefix + "mute")) {
					if(event.getMessage().getMentionedRoles().toArray().length == 1) {
						if(event.getMessage().getMentionedUsers().toArray().length == 1) {
							Member member = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
							Role roleToGive = event.getMessage().getMentionedRoles().get(0); // change so its only one role ---------------------
							int minutes = 10;
							event.getGuild().addRoleToMember(member, roleToGive).queue();
							event.getChannel().sendMessage("Gave the role" + roleToGive + " to " + member).queue();
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									event.getGuild().removeRoleFromMember(member, roleToGive).queue();	
								}
							}, (minutes * 1000));
						} else
							event.getChannel().sendMessage("Please Only Type One User At a Time").queue();
					} else
						event.getChannel().sendMessage("Please Only Type One Role At a Time").queue();
				}
			}
			*/
		}
	}
}
