package online.flowerinsnow.serverdepend.command;

import online.flowerinsnow.serverdepend.ServerDependPlugin;
import online.flowerinsnow.serverdepend.api.ServerDependMode;
import online.flowerinsnow.serverdepend.api.event.ServerDependSettingsChangeEvent;
import online.flowerinsnow.serverdepend.config.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandMain implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("serverdepend.reload") && !sender.hasPermission("serverdepend.mode")) {
            Message.Command.NO_PERMISSION.send(sender);
            return true;
        }
        if (1 == args.length) {
            switch (args[0].toLowerCase()) {
                case "reload":
                    if (sender.hasPermission("serverdepend.reload")) {
                        ServerDependPlugin.getInstance().reloadConfig();
                        Message.Command.RELOAD.send(sender);
                    } else {
                        Message.Command.NO_PERMISSION.send(sender, "serverdepend.reload");
                    }
                    break;
                case "0":
                case "1":
                case "2":
                case "3":
                    if (sender.hasPermission("serverdepend.mode")) {
                        ServerDependPlugin.getCore().setMode(ServerDependMode.getById(Integer.parseInt(args[0])), ServerDependSettingsChangeEvent.Reason.COMMAND);
                        Message.Command.MODE.send(sender, args[0]);
                    } else {
                        Message.Command.NO_PERMISSION.send(sender, "serverdepend.mode");
                    }
                    break;
                default:
                    Message.Command.USAGE.send(sender);
            }
        } else {
            Message.Command.USAGE.send(sender);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            if (sender.hasPermission("serverdepend.reload")) {
                result.add("reload");
            }
            if (sender.hasPermission("serverdepend.mode")) {
                result.add("0");
                result.add("1");
                result.add("2");
                result.add("3");
            }
            result.removeIf(res -> !res.toLowerCase().startsWith(args[0].toLowerCase()));
            return result;
        }
        return new ArrayList<>();
    }
}
