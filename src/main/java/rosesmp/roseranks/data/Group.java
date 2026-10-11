package rosesmp.roseranks.data;

import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;

import static rosesmp.roseranks.RoseRanks.MOD_ID;
import static rosesmp.roseranks.RoseRanks.groups;

/**
 * A group of players used for player categorization and permission allocation.
 */
public class Group {
	private YamlConfiguration yaml;
	private File file;
	private String name;
	private String prefix;

	public Group() {}

	/**
	 * Loads a group's data from file.
	 */
	public void load(String name) throws IOException {
		this.name = name;

		//Load yml
		file = new File(FabricLoader.getInstance().getConfigDir() + "/" + MOD_ID + "/groups/" + name + ".yml");
		if (!file.exists()) {
			intialize();
		}

		yaml = new YamlConfiguration(file);
		yaml.load(this.getClass());

		//Populate fields of this object with the yml's data
		setPrefix(yaml.getString("prefix"));

		//Add group to list of loaded groups, so it doesn't need to be loaded every time it's accessed
		groups().putIfAbsent(name, this);
	}

	/**
	 * Creates a group's file for creation.
	 */
	private void intialize() throws IOException {
		file.getParentFile().mkdirs();
		file.createNewFile();

		try (InputStream inputStream = getClass().getResourceAsStream("/group.yml")) {
			if (inputStream == null) {
				throw new FileNotFoundException("Failed to get resource group.yml!");
			}
			Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * Updates group's file with changes made during runtime.
	 */
	public void save() throws IOException {
		if (name.isEmpty()) {
			return;
		}

		Map<String, Object> data = new LinkedHashMap<>();
		data.put("prefix", prefix);

		yaml.save(data);
	}

	/**
	 * Deletes a group.
	 */
	public void delete() {}

	/*
	 * Getters and Setters
	 */

	public YamlConfiguration getYaml() {
		return yaml;
	}

	public void setYaml(YamlConfiguration yaml) {
		this.yaml = yaml;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
