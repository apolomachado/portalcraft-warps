package nescaaallz.warps.commands;

import nescaaallz.warps.Warps;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class WarpCommand extends BukkitCommand {

    protected final Warps plugin;

    public WarpCommand(Warps plugin, String command) {
        super(command);
        this.plugin = plugin;
    }

    public Warps getPlugin() {
        return plugin;
    }

    public String getMessage(String message) {
        return getPlugin().getMessage(message);
    }

    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(args.length == 0) {
                player.sendMessage(getMessage("Warp.Usage"));
            } else if(args.length == 1) {
                String warp = args[0].toLowerCase();
                if(player.hasPermission("warps.warp." + warp)) {
                    Location requestedLocation = getPlugin().getWarpLocation(warp);
                    if(requestedLocation != null) {
                        player.teleport(requestedLocation);
                        player.sendMessage(getMessage("Warp.Teleported").replace("%warp%", warp));
                    } else {
                        player.sendMessage(getMessage("Warp.Undefined").replace("%warp%", warp));
                    }
                } else {
                    player.sendMessage(getMessage("Warp.Denied"));
                }
            }
        } else {
            commandSender.sendMessage(getMessage("Warp.OnlyPlayers"));
        }
        return false;
    }
}