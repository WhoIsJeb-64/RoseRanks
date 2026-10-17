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

import java.io.IOException;

import static rosesmp.roseranks.RoseRanks.groups;

@SuppressWarnings("ALL")
public class RankSetPrefix implements CommandManager.CommandRegistry {

	@Override public void register(CommandDispatcher< CommandSource > dispatcher) {
		String[] aliases = {"ranksetprefix", "rsprefix"};
		for (String alias : aliases) {
			dispatcher.register((ArgumentBuilderLiteral) ArgumentBuilderLiteral
				.literal(alias)
				.requires(source -> ((CommandSource) source).hasAdmin())
				.then(ArgumentBuilderRequired
					.argument("rank", ArgumentTypeString.word())
					.then(ArgumentBuilderRequired
						.argument("prefix", ArgumentTypeString.string())
						.executes(context -> run(context)))
				));
		}
	}

	/**Changes a group's prefix to what is specified.*/
	private int run(CommandContext context) {
		CommandSource sender = (CommandSource) context.getSource();
		String rank = context.getArgument("rank", String.class).toString();
		String prefix = context.getArgument("prefix", String.class).toString();

		if (groups().containsKey(rank)) {
			Group group = groups().get(rank);
			group.setPrefix(prefix.replace("&", "§"));
			
			try {
				group.save();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			sender.sendMessage(group.getName() + TextFormatting.RESET + "'s new prefix: " + group.getPrefix());
		} else {
			sender.sendMessage(TextFormatting.RED + "No rank '" + rank + "' exists!");
		}

		return 1;
	}
}
