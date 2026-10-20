package com.enderdevelopment.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {

    private static final List<String> RANKS = Arrays.asList(
            "founder", "owner", "developer", "moderator", "senior-helper", "helper", "trail-helper", "media", "booster", "loyal", "vip", "mvp"
    );

    @Override
    public void onEnable() {
        getLogger().info("RankPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RankPlugin has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rank")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /rank <set|remove> <player> <rank>");
                return true;
            }

            String action = args[0];
            String playerName = args[1];
            String rank = args[2];

            if (!RANKS.contains(rank)) {
                sender.sendMessage(ChatColor.RED + "Invalid rank!");
                return true;
            }

            Player targetPlayer = getServer().getPlayer(playerName);
            if (targetPlayer == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }

            if (action.equalsIgnoreCase("set")) {
                setRank(sender, targetPlayer, rank);
            } else if (action.equalsIgnoreCase("remove")) {
                removeRank(sender, targetPlayer, rank);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid action! Use set or remove.");
            }
            return true;
        }
        return false;
    }

    private void setRank(CommandSender sender, Player targetPlayer, String rank) {
        if (!sender.hasPermission("rankplugin.rank")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to assign ranks.");
            return;
        }

        if (sender instanceof Player) {
            Player senderPlayer = (Player) sender;
            if (rank.equals("developer") && !senderPlayer.hasPermission("rankplugin.rank.developer")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to assign developer rank.");
                return;
            }
        }

        String coloredRank = getColoredRank(rank);
        targetPlayer.setDisplayName(coloredRank + targetPlayer.getName());
        targetPlayer.setPlayerListName(coloredRank + targetPlayer.getName());
        sender.sendMessage(ChatColor.GREEN + "Rank " + coloredRank + " has been assigned to " + targetPlayer.getName() + ".");
    }

    private void removeRank(CommandSender sender, Player targetPlayer, String rank) {
        if (!sender.hasPermission("rankplugin.rank")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to remove ranks.");
            return;
        }

        targetPlayer.setDisplayName(targetPlayer.getName());
        targetPlayer.setPlayerListName(targetPlayer.getName());
        sender.sendMessage(ChatColor.GREEN + "Rank " + rank + " has been removed from " + targetPlayer.getName() + ".");
    }

    private String getColoredRank(String rank) {
        switch (rank.toLowerCase()) {
            case "founder":
                return ChatColor.DARK_RED + "[Founder] ";
            case "owner":
                return ChatColor.RED + "[Owner] ";
            case "developer":
                return ChatColor.GOLD + "[Developer] ";
            case "moderator":
                return ChatColor.DARK_GREEN + "[Moderator] ";
            case "senior-helper":
                return ChatColor.GREEN + "[Senior-Helper] ";
            case "helper":
                return ChatColor.DARK_AQUA + "[Helper] ";
            case "trail-helper":
                return ChatColor.AQUA + "[Trail-Helper] ";
            case "media":
                return ChatColor.LIGHT_PURPLE + "[Media] ";
            case "booster":
                return ChatColor.YELLOW + "[Booster] ";
            case "loyal":
                return ChatColor.BLUE + "[Loyal] ";
            case "vip":
                return ChatColor.DARK_PURPLE + "[VIP] ";
            case "mvp":
                return ChatColor.GRAY + "[MVP] ";
            default:
                return "";
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("rank")) {
            if (args.length == 1) {
                return Arrays.asList("set", "remove");
            } else if (args.length == 3) {
                return RANKS;
            }
        }
        return super.onTabComplete(sender, command, alias, args);
    }
}