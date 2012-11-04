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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * 
 * This class is used for the /party help
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class HelpCommand extends AbstractCommand {

    @Override
    public boolean canExecute(final CommandSender sender, final String[] split) {
        return split.length == 2
                && split[0].equalsIgnoreCase("party")
                && split[1].equalsIgnoreCase("help");
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] split) {
        HelpCommand.help(sender);
        return true;
    }

    public static void help(final CommandSender player) {
        if (player.hasPermission("parties.use")) {
            player.sendMessage(ChatColor.GOLD + "Basic party commands:");
            player.sendMessage(ChatColor.GOLD + "/p list " + ChatColor.AQUA + "- List the players in your party");
            player.sendMessage(ChatColor.GOLD + "/p accept " + ChatColor.AQUA + "- Accept an invitation to join a party");
            player.sendMessage(ChatColor.GOLD + "/p decline " + ChatColor.AQUA + "- Decline an invitation to join a party");
            player.sendMessage(ChatColor.GOLD + "/p leave " + ChatColor.AQUA + "- Leave a party");
            player.sendMessage(ChatColor.GOLD + "/p chat [Message] " + ChatColor.AQUA + "- Send a message to party chat");
            player.sendMessage(ChatColor.GOLD + "/p chat " + ChatColor.AQUA + "- Toggle party chat");
            player.sendMessage(ChatColor.GOLD + "/pc [Message] " + ChatColor.AQUA + "- Short for /p chat [Message]");
            player.sendMessage(ChatColor.GOLD + "/pc " + ChatColor.AQUA + "- Short for /p chat");
            player.sendMessage(ChatColor.GOLD + "Party op commands:");
            player.sendMessage(ChatColor.GOLD + "/p invite [Player] " + ChatColor.AQUA + "- Invite a player to the party");
            player.sendMessage(ChatColor.GOLD + "/p kick [Player] " + ChatColor.AQUA + "- Kick a player from the party");
            player.sendMessage(ChatColor.GOLD + "Party leader commands:");
            player.sendMessage(ChatColor.GOLD + "/p leader [Player] " + ChatColor.AQUA + "- Set a new party leader");
            player.sendMessage(ChatColor.GOLD + "/p op [Player] " + ChatColor.AQUA + "- Op a player in the party");
            player.sendMessage(ChatColor.GOLD + "/p deop [Player] " + ChatColor.AQUA + "- Deop a player in the party");
        }
    }

    @Override
    public String[] getPermission() {
        return new String[] { "parties.use" };
    }
}
