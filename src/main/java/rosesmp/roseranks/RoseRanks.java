package rosesmp.roseranks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rosesmp.roseranks.commands.RoseRanksCommand;
import rosesmp.roseranks.data.*;
import rosesmp.roseranks.services.APIService;
import turniplabs.halplibe.HalpLibe;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RoseRanks implements ModInitializer {
	@Environment(EnvType.SERVER)
	public static final String MOD_ID = HalpLibe.registerMod("roseranks", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static Config config;

	public static APIService apiService;
	private static HashMap<String, User> loadedUsers;
	private static HashMap<String, Group> groups;

	@Override
	public void onInitialize() {
		apiService = new APIService();
		config = new Config();
		loadedUsers = new HashMap<>();

		try {
			initGroups();
			config.load();
			LOGGER.info("RoseRanks initialized. Default group: {}", config.getDefaultGroup());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		initCommands();
	}

	private void initGroups() throws IOException {
		groups = new HashMap<>();
		File[] directory =
			new File(FabricLoader.getInstance().getConfigDir() + "/" + MOD_ID + "/groups").listFiles();
		if (directory == null) {
			return;
		}

		//Iterate through groups folder, loading every group
		for (File file : directory) {
			String name = file.getName().replace(".yml", "");
			Group group = new Group();
			group.load(name);
		}
	}

	private void initCommands() {
		CommandManager.registerServerCommand(new RoseRanksCommand());
	}

	public static Config config() {
		return config;
	}

	public static HashMap<String, User> users() {
		return loadedUsers;
	}

	public static HashMap<String, Group> groups() {
		return groups;
	}
}
