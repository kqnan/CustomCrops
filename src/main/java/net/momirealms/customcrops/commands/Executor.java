/*
 *  Copyright (C) <2022> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customcrops.commands;

import net.momirealms.customcrops.utils.AdventureManager;
import net.momirealms.customcrops.ConfigReader;
import net.momirealms.customcrops.CustomCrops;
import net.momirealms.customcrops.utils.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Executor implements CommandExecutor {

    private final CustomCrops plugin;

    public Executor(CustomCrops plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender.hasPermission("customcrops.admin") || sender.isOp())){
            AdventureManager.playerMessage((Player) sender, ConfigReader.Message.prefix + ConfigReader.Message.noPerm);
            return true;
        }

        if (args.length < 1) {
            lackArgs(sender);
            return true;
        }
        switch (args[0]){
            case "reload" -> {
                long time = System.currentTimeMillis();
                ConfigReader.reloadConfig();
                if(sender instanceof Player){
                    AdventureManager.playerMessage((Player) sender,ConfigReader.Message.prefix + ConfigReader.Message.reload.replace("{time}", String.valueOf(System.currentTimeMillis() - time)));
                }else {
                    AdventureManager.consoleMessage(ConfigReader.Message.prefix + ConfigReader.Message.reload.replace("{time}", String.valueOf(System.currentTimeMillis() - time)));
                }
                return true;
            }
            case "forcegrow" -> {
                if (args.length < 2) {
                    lackArgs(sender);
                    return true;
                }
                Bukkit.getScheduler().runTaskAsynchronously(CustomCrops.plugin, ()-> {
                    switch (ConfigReader.Config.growMode){
                        case 1 -> plugin.getCropManager().growModeOne(args[1]);
                        case 2 -> plugin.getCropManager().growModeTwo(args[1]);
                        case 3 -> plugin.getCropManager().growModeThree(args[1]);
                        case 4 -> plugin.getCropManager().growModeFour(args[1]);
                    }
                });
                if (sender instanceof Player player){
                    AdventureManager.playerMessage(player,ConfigReader.Message.prefix + ConfigReader.Message.forceGrow.replace("{world}",args[1]));
                }else {
                    AdventureManager.consoleMessage(ConfigReader.Message.prefix + ConfigReader.Message.forceGrow.replace("{world}",args[1]));
                }
                return true;
            }
            case "forcewater" -> {
                if (args.length < 2) {
                    lackArgs(sender);
                    return true;
                }
                Bukkit.getScheduler().runTaskAsynchronously(CustomCrops.plugin, ()-> {
                    switch (ConfigReader.Config.growMode){
                        case 1 -> plugin.getSprinklerManager().workModeOne(args[1]);
                        case 2 -> plugin.getSprinklerManager().workModeTwo(args[1]);
                        case 3 -> plugin.getSprinklerManager().workModeThree(args[1]);
                        case 4 -> plugin.getSprinklerManager().workModeFour(args[1]);
                    }
                });
                if (sender instanceof Player player){
                    AdventureManager.playerMessage(player,ConfigReader.Message.prefix + ConfigReader.Message.forceWater.replace("{world}",args[1]));
                }else {
                    AdventureManager.consoleMessage(ConfigReader.Message.prefix + ConfigReader.Message.forceWater.replace("{world}",args[1]));
                }
                return true;
            }
            case "forcesave" -> {
                if (args.length < 2) {
                    lackArgs(sender);
                    return true;
                }
                Bukkit.getScheduler().runTaskAsynchronously(CustomCrops.plugin, ()->{
                    switch (args[1]){
                        case "all" -> {
                                plugin.getSprinklerManager().updateData();
                                plugin.getSprinklerManager().saveData();
                                if (ConfigReader.Season.enable && !ConfigReader.Season.seasonChange){
                                    plugin.getSeasonManager().saveData();
                                }
                                plugin.getCropManager().updateData();
                                plugin.getCropManager().saveData();
                                plugin.getPotManager().saveData();
                                forceSave(sender);
                        }
                        case "crop" -> {
                            plugin.getCropManager().updateData();
                            plugin.getCropManager().saveData();
                            forceSave(sender);
                        }
                        case "pot" -> {
                            plugin.getPotManager().saveData();
                            forceSave(sender);
                        }
                        case "season" -> {
                            plugin.getSeasonManager().saveData();
                            forceSave(sender);
                        }
                        case "sprinkler" -> {
                            plugin.getSprinklerManager().updateData();
                            plugin.getSprinklerManager().saveData();
                            forceSave(sender);
                        }
                     }
                });
            }
            case "backup" -> {
                FileUtil.backUpData();
                if (sender instanceof Player player){
                    AdventureManager.playerMessage(player,ConfigReader.Message.prefix + ConfigReader.Message.backUp);
                }else {
                    AdventureManager.consoleMessage(ConfigReader.Message.prefix + ConfigReader.Message.backUp);
                }
                return true;
            }
            case "cleandata" -> {
                plugin.getCropManager().cleanData();
                plugin.getSprinklerManager().cleanData();
                return true;
            }
            case "setseason" -> {
                if (args.length < 3) {
                    lackArgs(sender);
                    return true;
                }
                if (plugin.getSeasonManager().setSeason(args[1], args[2])){
                    if (sender instanceof Player player){
                        AdventureManager.playerMessage(player,ConfigReader.Message.prefix + ConfigReader.Message.setSeason.replace("{world}",args[1]).replace("{season}",args[2]));
                    }else {
                        AdventureManager.consoleMessage(ConfigReader.Message.prefix + ConfigReader.Message.setSeason.replace("{world}",args[1]).replace("{season}",args[2]));
                    }
                }else {
                    if (sender instanceof Player player){
                        AdventureManager.playerMessage(player,ConfigReader.Message.prefix + ConfigReader.Message.wrongArgs);
                    }else {
                        AdventureManager.consoleMessage(ConfigReader.Message.prefix + ConfigReader.Message.wrongArgs);
                    }
                }
                return true;
            }
            default -> {
                if (sender instanceof Player player){
                    AdventureManager.playerMessage(player,"<color:#F5DEB3>/customcrops reload ????????????");
                    AdventureManager.playerMessage(player,"<color:#F5DEB3>/customcrops setseason <world> <season> ???????????????????????????");
                    AdventureManager.playerMessage(player,"<color:#F5DEB3>/customcrops backup ????????????");
                    AdventureManager.playerMessage(player,"<color:#F5DEB3>/customcrops forcegrow <world> ????????????????????????????????????????????????");
                    AdventureManager.playerMessage(player,"<color:#F5DEB3>/customcrops forcewater <world> ????????????????????????????????????????????????");
                    AdventureManager.playerMessage(player,"<color:#F5DEB3>/customcrops forcesave <file> ???????????????????????????");
                }else {
                    AdventureManager.consoleMessage("<color:#F5DEB3>/customcrops reload ????????????");
                    AdventureManager.consoleMessage("<color:#F5DEB3>/customcrops setseason <world> <season> ???????????????????????????");
                    AdventureManager.consoleMessage("<color:#F5DEB3>/customcrops backup ????????????");
                    AdventureManager.consoleMessage("<color:#F5DEB3>/customcrops forcegrow <world> ????????????????????????????????????????????????");
                    AdventureManager.consoleMessage("<color:#F5DEB3>/customcrops forcewater <world> ????????????????????????????????????????????????");
                    AdventureManager.consoleMessage("<color:#F5DEB3>/customcrops forcesave <file> ???????????????????????????");
                }
            }
        }
        return true;
    }

    /**
     * ????????????????????????
     * @param sender ?????????
     */
    private void lackArgs(CommandSender sender){
        if (sender instanceof Player){
            AdventureManager.playerMessage((Player) sender,ConfigReader.Message.prefix + ConfigReader.Message.lackArgs);
        }else {
            AdventureManager.consoleMessage(ConfigReader.Message.prefix + ConfigReader.Message.lackArgs);
        }
    }

    /**
     * ????????????????????????
     * @param sender ?????????
     */
    private void forceSave(CommandSender sender){
        if (sender instanceof Player player){
            AdventureManager.playerMessage(player,ConfigReader.Message.prefix + ConfigReader.Message.forceSave);
        }else {
            AdventureManager.consoleMessage(ConfigReader.Message.prefix + ConfigReader.Message.forceSave);
        }
    }
}
