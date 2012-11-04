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

import de.bangl.parties.Party;
import de.bangl.parties.api.PartiesAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class ChatCommand extends AbstractCommand {

    @Override
    public boolean canExecute(final CommandSender sender, final String[] split) {
        return split.length >= 2
                && split[0].equalsIgnoreCase("party")
                && split[1].equalsIgnoreCase("chat");
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] split) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't chat with parties!");
            return true;
        }
        final Player player = (Player) sender;
        Party p;
        if (!PartiesAPI.getInstance().inParty(player)) {
            player.sendMessage(ChatColor.RED
                    + "You must be in a party to chat with the party!");
            return true;
        }
        if (split.length == 1) {
            if (PartiesAPI.getInstance().getPartyChatMode(player)) {
                PartiesAPI.getInstance().setPartyChatMode(player, false);
                player.sendMessage(ChatColor.GOLD
                        + "You messages will now go to game chat!");
                return true;
            } else {
                PartiesAPI.getInstance().setPartyChatMode(player, true);
                player.sendMessage(ChatColor.GOLD
                        + "You messages will now go to party chat!");
                return true;
            }
        } else {
            if ((p = PartiesAPI.getInstance().getParty(player)) != null) {
                String message = "";
                for (int i = 1; i < split.length; i++) {
                    message += split[i] + " ";
                }
                p.sendPartyChat(player,
                        message.substring(0, message.length() - 1));
                return true;
            }
            player.sendMessage(ChatColor.RED
                    + "Your party was not found... say what?!?!?");
        }
        player.sendMessage(ChatColor.RED
                + "Invalid command usage! Type /p help for help!");
        return true;
    }

    @Override
    public String[] getPermission() {
        return new String[] { "parties.use" };
    }
}
