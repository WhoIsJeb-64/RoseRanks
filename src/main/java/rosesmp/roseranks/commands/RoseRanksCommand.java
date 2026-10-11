package rosesmp.roseranks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

import static rosesmp.roseranks.RoseRanks.groups;

@SuppressWarnings("ALL")
public class RoseRanksCommand implements CommandManager.CommandRegistry {

	@Override public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register((ArgumentBuilderLiteral) ArgumentBuilderLiteral
			.literal("roseranks")
			.requires(source -> ((CommandSource) source).hasAdmin())
			.then(ArgumentBuilderRequired
				.argument("subcommand", ArgumentTypeString.word()))
				.executes(this::command));

	}

	private int command(CommandContext<Object> context) throws CommandSyntaxException {
		CommandSource source = (CommandSource) context.getSource();
		String subcommand = context.getArgument("subcommand", String.class);

		switch (subcommand) {
			case "groups" -> groupsCommand((CommandSource) context.getSource());
		}

		return 1;
	}

	/**Prints a list of the server's exisitng groups.*/
	private void groupsCommand(CommandSource sender) {
		sender.sendMessage("Server's groups:");

		for (String groupName : groups().keySet()) {
			sender.sendMessage(groupName);
		}
	}
}
