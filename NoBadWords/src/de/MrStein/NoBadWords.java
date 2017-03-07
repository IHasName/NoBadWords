package de.MrStein;

import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import de.MrStein.Stuff.Configs;

public class NoBadWords extends JavaPlugin implements Listener {
	Configs badw;
	Random ran = new Random();

	public void onEnable() {
		badw = new Configs(this, "blacklist.yml");
		badw.get().options().copyDefaults(true);
		badw.save();
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {}

	public boolean onCommand(CommandSender s, Command cmd, String cmdlabel, String[] args) {
		Player p = (Player) s;
		if (p.hasPermission("nbw.use")) {
			if (cmd.getName().equalsIgnoreCase("nobadwords")) {
				if (args.length == 0) {
					p.sendMessage("§c/nobadwords reload");
					return false;
				}
				if (args[0].equalsIgnoreCase("reload")) {
					badw.reload();
					p.sendMessage("§7Config reloaded");
					return true;
				}
				p.sendMessage("§c/nobadwords reload");
			}
		}
		return false;
	}

	@EventHandler
	public void event(AsyncPlayerChatEvent e) {
		String message = "";
		for (String key : badw.get().getConfigurationSection("blockedwords").getKeys(false)) {
			String filter = e.getMessage().toLowerCase();
			if (badw.get().getBoolean("ignorespacing")) {
				filter = filter.replace(" ", "");
			}
			if (badw.get().getBoolean("useumlauts")) {
				filter = formatback(filter);
			}
			if (filter.contains(key)) {
				if (badw.get().getBoolean("blockedwords." + key + ".replace")) {
					String[] m = e.getMessage().split(" ");
					for (int i = 0; i < m.length; i++) {
						if (m[i].toLowerCase().contains(key)) {
							message += badw.get().getStringList("blockedwords." + key + ".alt").get(ran.nextInt(badw.get().getStringList("blockedwords." + key + ".alt").size())) + " ";
						} else {
							message += e.getMessage().split(" ")[i] + " ";
						}
					}
					e.setMessage(message);
				} else {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', badw.get().getString("message")));
				}
			}
		}
	}

	private static String formatu(String string) {
		string = string.replace("ae", "ä").replace("oe", "ö").replace("ue", "ü");
		return string;
	}

	private static String formatback(String string) {
		string = string.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue");
		return string;
	}
}
