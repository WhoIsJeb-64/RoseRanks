package rosesmp.roseranks.data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ArgumentTypeUser implements ArgumentType<User> {

	public static ArgumentTypeUser user() {
		return new ArgumentTypeUser();
	}

	@Override
	public User parse(StringReader reader) throws CommandSyntaxException {
		String name = reader.readString();
		reader.setCursor(reader.getTotalLength());

		return null;
	}
}
