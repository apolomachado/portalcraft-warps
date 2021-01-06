package nescaaallz.warps.commands;

import nescaaallz.warps.Warps;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class SetwarpCommand extends BukkitCommand {

    protected final Warps plugin;

    public SetwarpCommand(Warps plugin, String command) {
        super(command);
        this.plugin = plugin;
    }

    public Warps getPlugin() {
        return plugin;
    }

    public String getMessage(String message) {
        return getPlugin().getMessage(message);
    }

    public void defineWarp(Player player, String warp) {
        String parsedLocation = player.getWorld().getName() + " " + player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ() + " " + player.getLocation().getYaw() + " " + player.getLocation().getPitch();
        getPlugin().getConfig().set("Warps." + warp, parsedLocation);
        getPlugin().saveConfig();
    }

    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("warps.commands.setwarp")) {
                if(args.length == 0) {
                    player.sendMessage(getMessage("Setwarp.Usage"));
                } else if(args.length == 1) {
                    String warp = args[0].toLowerCase();
                    defineWarp(player, warp);
                    player.sendMessage(getMessage("Setwarp.DefinedWarp").replace("%warp%", warp));
                }
            } else {
                player.sendMessage(getMessage("Setwarp.Denied"));
            }
        } else {
            commandSender.sendMessage(getMessage("Setwarp.OnlyPlayers"));
        }
        return false;
    }
}