package rosesmp.roseranks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import rosesmp.roseranks.data.ArgumentTypeUser;

@SuppressWarnings("ALL")
public class UserRank implements CommandManager.CommandRegistry {

	@Override public void register(CommandDispatcher<CommandSource> dispatcher) {
		String[] aliases = {"userrank", "rank"};
		for (String alias : aliases) {
			dispatcher.register((ArgumentBuilderLiteral) ArgumentBuilderLiteral
				.literal(alias)
				.requires(source -> ((CommandSource) source).hasAdmin())
				.then(ArgumentBuilderRequired
					.argument("user", ArgumentTypeUser.user())
					.executes(context -> run(context))
				));
		}
	}

	private int run(CommandContext context) {

		return 1;
	}
}
