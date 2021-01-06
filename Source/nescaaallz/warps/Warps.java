package nescaaallz.warps;

import nescaaallz.warps.commands.DelwarpCommand;
import nescaaallz.warps.commands.SetwarpCommand;
import nescaaallz.warps.commands.WarpCommand;
import nescaaallz.warps.commands.WarpsCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Warps extends JavaPlugin {

    protected List<Command> commands;

    public void onEnable() {
        saveDefaultConfig();
        setupCommands();
    }

    public Location getWarpLocation(String warp) {
        Location requestedLocation = null;
        String searchWarp = getConfig().getString("Warps." + warp);
        if(searchWarp != null) {
            String[] splitter = searchWarp.split(" ");
            requestedLocation = new Location(Bukkit.getWorld(splitter[0]), Double.parseDouble(splitter[1]), Double.parseDouble(splitter[2]), Double.parseDouble(splitter[3]), Float.parseFloat(splitter[4]), Float.parseFloat(splitter[5]));
        }
        return requestedLocation;
    }

    public String getMessage(String message) {
        StringBuilder finalMessage = null;
        int x = 0;
        List<String> stringList = getConfig().getStringList("Messages." + message);
        int size = stringList.size();
        while (x < size) {
            if(finalMessage != null) {
                finalMessage.append("\n").append(stringList.get(x).replace('&', '§'));
            } else {
                finalMessage = new StringBuilder(stringList.get(x).replace('&', '§'));
            }
            x++;
        }
        if(finalMessage == null) {
            System.out.printf("Warning > '%s' not found in the message configuration section.%n", message);
        }
        assert finalMessage != null;
        return finalMessage.toString();
    }

    public Warps getPlugin() {
        return this;
    }

    public void setupCommands() {

        commands = new ArrayList<>() {
            { new WarpCommand(getPlugin(), "warp"); }

            { new WarpsCommand(getPlugin(), "warps"); }

            { new SetwarpCommand(getPlugin(), "setwarp"); }

            { new DelwarpCommand(getPlugin(), "delwarp"); }
        };

        registerCommands();
    }

    public List<Command> getCommands() {
        return commands;
    }

    protected void registerCommands() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            for(Command command : getCommands()) {
                commandMap.register(command.getName(), command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}