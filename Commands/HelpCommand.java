package Main.Commands;

import java.awt.Color;

import Main.Keys;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {
	
	public static void run(MessageReceivedEvent event, String[] args, boolean hasAdmin) {
		int maxPageLength = (hasAdmin) ? 3 : 1;
		int helpPage = 1;
				
		if(args.length == 2 && (Integer.parseInt(args[1]) >= 1 && Integer.parseInt(args[1]) <= maxPageLength))
			helpPage = Integer.parseInt(args[1]);
		
		EmbedBuilder embedHelp = new EmbedBuilder();
		embedHelp.setThumbnail("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
		embedHelp.setTitle("**Guild Bot Commands**");
		switch(helpPage) {
		
		// page 1 of help
		case 1:
			embedHelp.addField("**List of Commands**", Keys.prefix + "help 1-" + maxPageLength,  false);
			break;	
		
		// Below Holds Admin Commands
		case 2:
			embedHelp.setDescription("Admin Commands");
			embedHelp.addField("**Deletes Lines**", Keys.prefix + "delete 1-99", false);
			embedHelp.addField("**Prints The Names of All Date Bases**", Keys.prefix + "NOT IMPLEMENTED", false);
			embedHelp.addField("**Prints a Data Base**", Keys.prefix + "printdb {Data Base Name}", false);
			embedHelp.addField("**Change a Data Base**", Keys.prefix + "changedb {DBname} {Row} {UserName} {New Info}", false);
			break;
			
		case 3:
			embedHelp.setDescription("Admin Test Commands");
			embedHelp.addField("**Sends a Test Message**", Keys.prefix + "test", false);
			embedHelp.addField("**Sends a Better Test Message**", Keys.prefix + "test again", false);
			embedHelp.addField("**Sends a Test Embed Message**", Keys.prefix + "embed", false);
			break;
					
		// needs default to work
		default:
			System.out.println("-----Help Page Didn't Send Right-----");
			break;
		}
		embedHelp.setFooter("_Page " + helpPage + " of " + maxPageLength + "_");
		float[] hsb = Color.RGBtoHSB(0, 143, 190, null);
		embedHelp.setColor(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
		event.getChannel().sendMessageEmbeds(embedHelp.build()).queue();
		embedHelp.clear();
	}

}
