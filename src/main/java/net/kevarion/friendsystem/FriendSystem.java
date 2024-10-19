package net.kevarion.friendsystem;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.kevarion.friendsystem.command.FriendCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.signature.qual.SignatureUnknown;

import java.awt.print.Paper;

@SuppressWarnings("LombokGetterMayBeUsed")
public final class FriendSystem extends JavaPlugin {

    @Getter private static FriendSystem instance;
    private PaperCommandManager commandManager;

    private FriendManager friendManager;

    public static FriendSystem getInstance() {
        return instance;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void onEnable() {

        instance = this;
        commandManager = new PaperCommandManager(this);

        friendManager = new FriendManager(this);
        commandManager.registerCommand(new FriendCommand(friendManager));

        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        if (friendManager != null) {
            friendManager.saveFile();
        }
    }
}
