package rosesmp.roseranks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import rosesmp.roseranks.data.Group;

import static rosesmp.roseranks.RoseRanks.groups;

@SuppressWarnings("ALL")
public class RankList implements CommandManager.CommandRegistry {

	@Override public void register(CommandDispatcher<CommandSource> dispatcher) {
		String[] aliases = {"ranklist", "rl"};
		for (String alias : aliases) {
			dispatcher.register((ArgumentBuilderLiteral) ArgumentBuilderLiteral
				.literal(alias)
				.requires(source -> ((CommandSource) source).hasAdmin())
				.executes(context -> run((CommandSource) context.getSource())));
		}

	}

	/**Prints a list of the server's exisitng groups.*/
	private int run(CommandSource sender) {
		sender.sendMessage("Ranks:");

		int c = 1;
		for (Group group : groups().values()) {
			sender.sendMessage(c + ": " + group.getPrefix() + TextFormatting.RESET + " " + group.getName());
			c++;
		}
		return 1;
	}
}
