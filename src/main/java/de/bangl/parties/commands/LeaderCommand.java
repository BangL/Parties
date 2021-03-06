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
import org.bukkit.Bukkit;
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
public class LeaderCommand extends AbstractCommand {

    @Override
    public boolean canExecute(final CommandSender sender, final String[] split) {
        return split.length == 3
                && split[0].equalsIgnoreCase("party")
                && split[1].equalsIgnoreCase("leader");
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] split) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final Party party = PartiesAPI.getInstance().getParty(player);
            if (party == null) {
                player.sendMessage(ChatColor.RED + "You're not in a party!");
            } else {
                if (party.getLeader().equals(player)) {
                    final Player leader = Bukkit.getPlayer(split[2]);
                    if (leader == null) {
                        player.sendMessage(ChatColor.RED + "This player does not exist.");
                    } else if (PartiesAPI.getInstance().inParty(leader)
                            && PartiesAPI.getInstance().getParty(leader).equals(party)) {
                        party.setLeader(leader);
                        if (party.isOp(player)) {
                            party.deop(player);
                        }
                        party.sendPartyMessage(leader.getDisplayName() + ChatColor.GOLD + " is the new party leader!");
                    } else {
                        player.sendMessage(ChatColor.RED + "This player is not part of your party.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You're not allowed to set a new leader of this party.");
                }
            }
        } else {
            sender.sendMessage("You can't set a leader of any party!");
        }
        return true;
    }

    @Override
    public String[] getPermission() {
        return new String[] { "parties.use" };
    }
}
