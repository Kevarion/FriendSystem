package net.kevarion.friendsystem.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.kevarion.friendsystem.FriendManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("friend|f")
public class FriendCommand extends BaseCommand {

    private final FriendManager friendManager;

    public FriendCommand(FriendManager friendManager) {
        this.friendManager = friendManager;
    }

    @Default
    public void main(Player player) {
        player.sendMessage(ChatColor.YELLOW + "Usage: /friend add <player>, /friend remove <player>, /friend list, /friend accept <player>, /friend deny <player>");
    }

    @Subcommand("add")
    public void addFriend(Player player, String targetPlayerName) {
        Player target = Bukkit.getPlayer(targetPlayerName);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        String playerName = player.getName();
        String targetName = target.getName();

        if (friendManager.getFriends(playerName).contains(targetName)) {
            player.sendMessage(ChatColor.RED + targetName + " is already your friend.");
        } else {
            friendManager.sendFriendRequest(playerName, targetName);
            target.sendMessage(ChatColor.YELLOW + playerName + " has sent you a friend request! Use /friend accept " + playerName + " or /friend deny " + playerName + ".");
            player.sendMessage(ChatColor.GREEN + "You have sent a friend request to " + targetName + ".");
        }
    }

    @Subcommand("remove")
    public void removeFriend(Player player, String targetPlayerName) {
        Player target = Bukkit.getPlayer(targetPlayerName);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        String playerName = player.getName();
        String targetName = target.getName();

        if (!friendManager.getFriends(playerName).contains(targetName)) {
            player.sendMessage(ChatColor.RED + targetName + " is not your friend.");
        } else {
            friendManager.removeFriend(playerName, targetName);
            player.sendMessage(ChatColor.GREEN + "You have removed " + targetName + " from your friends.");
        }
    }

    @Subcommand("accept")
    public void acceptFriend(Player player, String senderName) {
        String playerName = player.getName();

        friendManager.acceptFriendRequest(playerName, senderName);
        player.sendMessage(ChatColor.GREEN + "You have accepted " + senderName + "'s friend request.");
    }

    @Subcommand("deny")
    public void denyFriend(Player player, String senderName) {
        String playerName = player.getName();

        friendManager.denyFriendRequest(playerName, senderName);
        player.sendMessage(ChatColor.RED + "You have denied " + senderName + "'s friend request.");
    }

    @Subcommand("list")
    public void listFriends(Player player) {
        String playerName = player.getName();
        List<String> friends = friendManager.getFriends(playerName);

        if (friends.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You have no friends.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "Your friends: " + String.join(", ", friends));
        }
    }
}
