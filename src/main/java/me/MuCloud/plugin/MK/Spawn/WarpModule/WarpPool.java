package me.MuCloud.plugin.MK.Spawn.WarpModule;

import me.MuCloud.API.MK.Spawn.IWarpPool;
import me.MuCloud.plugin.MK.Spawn.Main;
import me.MuCloud.plugin.MK.Spawn.WarpModule.Warps.Warp;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class WarpPool implements IWarpPool {

    private final Main main;
    private static final List<Warp> Pool = new ArrayList<>();

    public WarpPool(Main plugin){
        main = plugin;
    }

    public void onLoad(){
        fetchWarps();
    }

    private void fetchWarps(){
        FileConfiguration fc = new YamlConfiguration();
        for(File f : Objects.requireNonNull(main.getConfiguration().getWarpFolder().listFiles())){
            if(f.getName().lastIndexOf(".yml") == -1 ||
                    f.length() == 0){
                continue;
            }

            try {
                fc.load(f);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }

            String internalName = fc.getString("InternalName");
            String displayName = fc.getString("DisplayName");
            String type = fc.getString("Type");
            String ow = fc.getString("World");
            Object ox = fc.get("X");
            Object oy = fc.get("Y");
            Object oz = fc.get("Z");
            Object oyaw = fc.get("Yaw");
            Object opitch = fc.get("Pitch");
            UUID creator = (UUID) fc.get("Creator");

            if(internalName == null ||
                    type == null ||
                    ow == null ||
                    ox == null ||
                    oy == null ||
                    oz == null ||
                    oyaw == null ||
                    opitch == null){
                main.getLogger().log(Level.WARNING,"This file -> \"" + f.getName() + "\" cannot be read. It may be corrupt or contain invalid settings. Please check this file. Reading the file will be ignored." );
                continue;
            }

            if(Objects.equals(type, "NORMAL") && fc.get("Condition") == null){
                main.getLogger().log(Level.WARNING,"This file -> \"" + f.getName() + "\" not defined Condition. This Warp represented in this file will be converted to a Normal Warp.");
                type = "NORMAL";
            }

            World w = main.getServer().getWorld(ow);
            double x = fc.getDouble("X");
            double y = fc.getDouble("Y");
            double z = fc.getDouble("Z");
            float yaw = (float) fc.getDouble("Yaw");
            float pitch = (float) fc.getDouble("Pitch");
            Location location = new Location(w,x,y,z,yaw,pitch);

            switch (Objects.requireNonNull(type)){
                case "NORMAL":

                case "ITEM":

                case "MONEY":

                case "EXP":

                case "ACHIEVEMENT":

            }



        }

        main.getServer().getConsoleSender().sendMessage("§7§l[" + main.getPrefix() + "§7§l] §6 已加载 " + "" + "个地标");

    }

    @Override
    public boolean isContain(Warp warp) {
        return Pool.contains(warp);
    }

    @Override public void deleteWarp(Warp warp) {
        if(isContain(warp)){
            Pool.remove(warp);
        }
    }

    @Override public void createWarp(Warp warp) {
        if(!isContain(warp)){
            Pool.add(warp);
        }
    }

    @Override public void clearPool() {
        Pool.clear();
    }
}
