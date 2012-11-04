/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.bangl.parties.commands;

import de.bangl.parties.PartiesPlugin;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class manages all of the plugins commands.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class CommandManager {

    private final LinkedList<AbstractCommand> gameCommands = new LinkedList<AbstractCommand>();

    public CommandManager() {
        gameCommands.add(new AcceptCommand());
        gameCommands.add(new ChatCommand());
        gameCommands.add(new DeclineCommand());
        gameCommands.add(new DeopCommand());
        gameCommands.add(new HelpCommand());
        gameCommands.add(new InviteCommand());
        gameCommands.add(new KickCommand());
        gameCommands.add(new LeaderCommand());
        gameCommands.add(new LeaveCommand());
        gameCommands.add(new ListCommand());
        gameCommands.add(new OpCommand());
    }

    public boolean onGameCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        // Process aliases
        final String[] split = new String[args.length + 1];

        int shift = 0;
        if (label.equalsIgnoreCase("pc")) {
            split[0] = "party";
            split[1] = "chat";
            shift = 1;
        } else if (label.equalsIgnoreCase("party")
                || label.equalsIgnoreCase("parties")
                || label.equalsIgnoreCase("p")) {
            split[0] = "party";
        }

        // Get args
        for (int i = 1; i - 1 < args.length; i++) {
            split[i + shift] = args[i - 1];
        }

        boolean processed = false;
        for (final AbstractCommand c : gameCommands) {
            if (c.canExecute(sender, split)) {
                if (hasPerms(sender, c)) {
                    try {
                        c.execute(sender, split);
                        processed = true;
                    } catch (final Exception e) {
                        PartiesPlugin.debug(e);
                    }
                } else {
                    processed = true;
                }
            }
        }

        if (!processed) {
            HelpCommand.help(sender);
        }
        return processed;
    }

    private boolean hasPerms(final CommandSender sender, final AbstractCommand command) {
        if (sender instanceof Player) {
            if (sender.hasPermission("parties.*")) {
                return true;
            }
            for (final String permission : command.getPermission()) {
                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to perform this command.");
                    return false;
                }
            }
        }
        return true;
    }
}
