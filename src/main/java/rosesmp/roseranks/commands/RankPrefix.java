package rosesmp.roseranks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import rosesmp.roseranks.data.Group;

import static rosesmp.roseranks.RoseRanks.groups;

@SuppressWarnings("ALL")
public class RankPrefix implements CommandManager.CommandRegistry {

	@Override public void register(CommandDispatcher< CommandSource > dispatcher) {
		String[] aliases = {"rankprefix", "rprefix"};
		for (String alias : aliases) {
			dispatcher.register((ArgumentBuilderLiteral) ArgumentBuilderLiteral
				.literal(alias)
				.requires(source -> ((CommandSource) source).hasAdmin())
				.then(ArgumentBuilderRequired
					.argument("rank", ArgumentTypeString.word())
					.executes(context -> run(context))
				));
		}
	}

	/**Prints the specified group's prefix.*/
	private int run(CommandContext context) {
		CommandSource sender = (CommandSource) context.getSource();
		String rank = context.getArgument("rank", String.class).toString();

		if (groups().containsKey(rank)) {
			Group group = groups().get(rank);
			sender.sendMessage(group.getName() + TextFormatting.RESET + "'s prefix: " + group.getPrefix());
		} else {
			sender.sendMessage(TextFormatting.RED + "No rank '" + rank + "' exists!");
		}

		return 1;
	}
}
