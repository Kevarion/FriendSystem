package net.kevarion.friendsystem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FriendManager {

    private final File friendsFile;
    private FileConfiguration friendsConfig;

    private final Map<String, List<String>> pendingRequests = new HashMap<>();

    public FriendManager(JavaPlugin plugin) {
        friendsFile = new File(plugin.getDataFolder(), "friends.yml");

        if (!friendsFile.exists()) {
            friendsFile.getParentFile().mkdirs();
            try {
                friendsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadConfiguration();
    }

    private void loadConfiguration() {
        friendsConfig = YamlConfiguration.loadConfiguration(friendsFile);
    }

    public void addFriend(String playerName, String friendName) {
        List<String> friends = friendsConfig.getStringList(playerName);
        if (!friends.contains(friendName)) {
            friends.add(friendName);
            friendsConfig.set(playerName, friends);
            save();
        }
    }

    public void removeFriend(String playerName, String friendName) {
        List<String> friends = friendsConfig.getStringList(playerName);
        if (friends.contains(friendName)) {
            friends.remove(friendName);
            friendsConfig.set(playerName, friends);
            save();
        }
    }

    public List<String> getFriends(String playerName) {
        return friendsConfig.getStringList(playerName);
    }

    private void save() {
        try {
            friendsConfig.save(friendsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        save();
    }

    public void reloadFile() {
        loadConfiguration();
    }

    public void sendFriendRequest(String sender, String receiver) {
        pendingRequests.computeIfAbsent(receiver, k -> new ArrayList<>()).add(sender);
    }

    public void acceptFriendRequest(String playerName, String sender) {
        List<String> requests = pendingRequests.getOrDefault(playerName, new ArrayList<>());
        if (requests.contains(sender)) {
            requests.remove(sender);
            addFriend(playerName, sender);
            addFriend(sender, playerName);
            save();
        }
    }

    public void denyFriendRequest(String playerName, String sender) {
        List<String> requests = pendingRequests.getOrDefault(playerName, new ArrayList<>());
        requests.remove(sender);
    }

    public List<String> getPendingRequests(String playerName) {
        return pendingRequests.getOrDefault(playerName, new ArrayList<>());
    }
}
