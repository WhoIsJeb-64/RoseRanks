package rosesmp.roseranks.data;

import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static rosesmp.roseranks.RoseRanks.*;

/**
 * The data structure RoseRanks uses to handle player information.
 */
public class User {
	private YamlConfiguration yaml;
	private File file;
	private UUID uuid;
	private String selectedGroup;
	private List<String> unlockedGroups;

	public User() {}

	/**
	 * Loads a user's data from file.
	 */
	public void load(UUID uuid) throws IOException {
		boolean isNew = false;

		this.uuid = uuid;
		file = new File(FabricLoader.getInstance().getConfigDir() + "/" + MOD_ID + "/users/" + uuid + ".yml");
		if (!file.exists()) {
			intialize();
			isNew = true;
		}
		yaml = new YamlConfiguration(file);
		yaml.load(this.getClass());


		//Populate fields of this object with the yml's data
		setSelectedGroup(isNew ? config().getDefaultGroup() : yaml.getString("selectedGroup"));
		setUnlockedGroups(yaml.getList("unlockedGroups"));
	}

	/**
	 * Creates a user's file for first-time joins.
	 */
	private void intialize() throws IOException {
		file.getParentFile().mkdirs();
		file.createNewFile();

		try (InputStream inputStream = getClass().getResourceAsStream("/user.yml")) {
			if (inputStream == null) {
				throw new FileNotFoundException("Failed to get resource user.yml!");
			}
			Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * Updates user's file with changes made during runtime.
	 */
	public void save() throws IOException {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("selectedGroup", selectedGroup);
		data.put("unlockedGroups", unlockedGroups);

		yaml.save(data);
	}

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

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(String selectedGroup) throws IOException {
		//Fallback is needed for when the default group did not exist at user creation
		this.selectedGroup = (!selectedGroup.isEmpty()) ? selectedGroup : config().getDefaultGroup();
	}

	public List<String> getUnlockedGroups() {
		return unlockedGroups;
	}

	public void setUnlockedGroups(List<String> unlockedGroups) {
		this.unlockedGroups = unlockedGroups;
	}

	private Group resolveGroup() throws IOException {
		if (groups().containsKey(selectedGroup)) {
			return groups().get(selectedGroup);
		} else {
			Group group = new Group();
			group.load(this.getSelectedGroup());
			return group;
		}
	}

	public String getPrefix() throws IOException {
		return resolveGroup().getPrefix();
	}
}
