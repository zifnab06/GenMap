package net.zifnab06.GenMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


class ChunkCoordinate {
    public double x;
    public double z;

    public ChunkCoordinate(double x, double z) {
        this.x = x;
        this.z = z;
    }
}

public class GenMap extends JavaPlugin {

    public int radius = 0;
    public ArrayList<ChunkCoordinate> ListOfChunks = new ArrayList<>();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (name.equalsIgnoreCase("generate")) {
            if (args.length == 2) {
                final World world = Bukkit.getWorld(args[0]);
                radius = Integer.parseInt(args[1]);

                for (double i = -Math.ceil(radius / 16); i < Math.ceil(radius / 32); i += 16) {
                    for (double j = -Math.ceil(radius / 16); j < Math.ceil(radius / 32); j += 16) {
                        ListOfChunks.add(new ChunkCoordinate(i, j));
                    }
                }
                for (int i = 0; i < 2; i++) {
                    BukkitTask task = getServer().getScheduler().runTask(this, new BukkitRunnable() {
                        @Override
                        public void run() {
                            while (ListOfChunks.size() != 0) {
                                ChunkCoordinate coord = ListOfChunks.get(0);
                                ListOfChunks.remove(0);
                                world.loadChunk((int)coord.x, (int)coord.z);
                                world.unloadChunk((int)coord.x, (int)coord.z);
                            }
                        }
                    });
                }

            }
        } else if (name.equalsIgnoreCase("generate-status")){
            sender.sendMessage(String.format("Remaining chunks: %i", ListOfChunks.size()));
        }

        return true;
    }
}
