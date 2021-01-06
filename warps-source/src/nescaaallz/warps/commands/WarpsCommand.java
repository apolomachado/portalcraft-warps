package nescaaallz.warps.commands;

import nescaaallz.warps.Warps;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Set;

public class WarpsCommand extends BukkitCommand {

    protected final Warps plugin;
    protected Set<String> warps;

    public WarpsCommand(Warps plugin, String command) {
        super(command);
        this.plugin = plugin;
        if(getConfig().getConfigurationSection("Warps") != null)
            this.warps = Objects.requireNonNull(getConfig().getConfigurationSection("Warps")).getKeys(false);
    }

    public Warps getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }

    public Set<String> getWarps() {
        return warps;
    }

    public String getMessage(String message) {
        return getPlugin().getMessage(message);
    }

    @Deprecated
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            TextComponent interactiveMessage = null;
            for(String warp : getWarps()) {
                if(player.hasPermission("warps.warp." + warp)) {
                    TextComponent temp;
                    BaseComponent[] texts;
                    HoverEvent hoverEvent;
                    if(interactiveMessage != null) {
                        temp = new TextComponent(getMessage("Warps.Separator") + getMessage("Warps.WarpColor") + warp);
                        temp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warp));
                        texts = (new ComponentBuilder(getMessage("Warps.Hover").replace("%warp%", warp))).create();
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, texts);
                        temp.setHoverEvent(hoverEvent);
                        interactiveMessage.addExtra(temp);
                    } else {
                        temp = new TextComponent(getMessage("Warps.WarpColor") + warp);
                        temp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warp));
                        texts = (new ComponentBuilder(getMessage("Warps.Hover").replace("%warp%", warp))).create();
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, texts);
                        temp.setHoverEvent(hoverEvent);
                        interactiveMessage = temp;
                    }
                }
            }
            if(interactiveMessage == null) {
                interactiveMessage = new TextComponent(getMessage("Warps.NoResults"));
            }
            player.sendMessage(getMessage("Warps.Title").replace("%warps%", interactiveMessage.getText()));
            if(getConfig().getBoolean("Warps.SendInteractiveMessage")) {
                player.spigot().sendMessage(interactiveMessage);
            }
        } else {
            commandSender.sendMessage(getMessage("Warps.OnlyPlayers"));
        }
        return false;
    }
}
