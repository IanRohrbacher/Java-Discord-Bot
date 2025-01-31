package Main.Commands;

import java.util.List;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DeleteCommand extends ListenerAdapter {
	
	public static void run(MessageReceivedEvent event, String[] args) {
		int num;
		
		try {
			num = Integer.parseInt(args[1]);
			
		} catch (NumberFormatException e) {
			num = -1;
		}
		
		event.getMessage().delete().queue();
		
		if(num > 0 && num < 100) {
			
			List<Message> message = event.getChannel().getHistory().retrievePast(num+1).complete();
			event.getChannel().purgeMessages(message);
			//event.getChannel().sendMessage("Deleted " + num + " lines").queue();
			
			System.out.println("Deleted " + num + " lines");
		} else if(num == -1) {
			event.getChannel().sendMessage("Invalid Number").queue();
		} else if (num > 100) {
			event.getChannel().sendMessage("Number needs to be shorter then 100").queue();
		} else {
			event.getChannel().sendMessage("Number needs to be longer then 0").queue();
		}
	}
}
