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

	private String group;

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
		setGroup(isNew ? config().getDefaultGroup() : yaml.getString("group"));
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
		data.put("group", group);

		yaml.save(data);
	}

	/**
	 * Grants a permission to a user.
	 * @return Grant success.
	 */
	public boolean grant() {
		return false;
	}

	/**
	 * Revokes a permission from a user.
	 * @return Revocation success.
	 */
	public boolean revoke() {
		return false;
	}

	/**
	 * Checks whether a user has a given permission.
	 * @return Whether the user has the permission.
	 */
	public boolean hasPermission(String permission) throws IOException {
		return resolveGroup().hasPermission(permission);
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) throws IOException {
		//Fallback is needed for when the default group did not exist at user creation
		this.group = (!group.isEmpty()) ? group : config().getDefaultGroup();
	}

	private Group resolveGroup() throws IOException {
		if (groups().containsKey(group)) {
			return groups().get(group);
		} else {
			Group group = new Group();
			group.load(this.getGroup());
			return group;
		}
	}

	public String getPrefix() throws IOException {
		return resolveGroup().getPrefix();
	}
}
