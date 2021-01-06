package nescaaallz.warps.commands;

import nescaaallz.warps.Warps;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DelwarpCommand extends BukkitCommand {

    protected final Warps plugin;

    public DelwarpCommand(Warps plugin, String command) {
        super(command);
        this.plugin = plugin;
    }

    public Warps getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }

    public boolean removeWarp(String warp) {
        boolean successfully;
        if(getConfig().getString("Warps." + warp) == null) {
            successfully = false;
        } else {
            getConfig().set("Warps." + warp, null);
            getPlugin().saveConfig();
            successfully = true;
        }
        return successfully;
    }

    public String getMessage(String message) {
        return getPlugin().getMessage(message);
    }

    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("warps.commands.delwarp")) {
                if(args.length == 0) {
                    player.sendMessage(getMessage("Delwarp.Usage"));
                } else if(args.length == 1) {
                    String warp = args[0].toLowerCase();
                    if (removeWarp(warp)) {
                        player.sendMessage(getMessage("Delwarp.RemovedWarp").replace("%warp%", warp));
                    } else {
                        player.sendMessage(getMessage("Delwarp.Undefined").replace("%warp%", warp));
                    }
                }
            } else {
                player.sendMessage(getMessage("Delwarp.Denied"));
            }
        } else {
            commandSender.sendMessage(getMessage("Delwarp.OnlyPlayers"));
        }
        return false;
    }
}