package de.MrStein.Stuff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Configs {
	
	FileConfiguration config = null;
	File file = null;
	String filename = null;
	private Plugin plugin;
	
	public Configs(Plugin plugin, String filename) {
		this.plugin = plugin;
		this.filename = filename;
	}
	
	public FileConfiguration get() {
		if(config == null) {
			reload();
		}
		return config;
	}
	
	public Configs save() {
		try {
			config.save(file);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't save File to " + file.getName(), e);
		}
		reload();
		return this;
	}
	
	public Configs reload() {
		file = new File(plugin.getDataFolder(), filename);
		if(!file.exists()) {
			plugin.saveResource(filename, false);
		}
		config = YamlConfiguration.loadConfiguration(file);
		InputStream defPlayerData = plugin.getResource(filename);
		if(defPlayerData != null) {
			config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defPlayerData)));
		}
		return this;
	}
}
