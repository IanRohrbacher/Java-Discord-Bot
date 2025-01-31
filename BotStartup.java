package Main;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class BotStartup {
	public static void main(String[] args) throws LoginException {
		System.out.println("Running The Guild Bot");
		JDABuilder jda = JDABuilder.createDefault(Keys.discordKey);
		jda.setActivity(Activity.playing("<>help"));
		jda.setStatus(OnlineStatus.ONLINE);
		jda.addEventListeners(new MainCommand());
		jda.setChunkingFilter(ChunkingFilter.ALL);
		jda.setMemberCachePolicy(MemberCachePolicy.ALL);
		jda.enableIntents(GatewayIntent.GUILD_MEMBERS);
		jda.build();
	}
}