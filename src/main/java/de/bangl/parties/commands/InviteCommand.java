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
public class InviteCommand extends AbstractCommand {

    @Override
    public boolean canExecute(final CommandSender sender, final String[] split) {
        return split.length == 3
                && split[0].equalsIgnoreCase("party")
                && split[1].equalsIgnoreCase("invite");
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] split) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final Party party = PartiesAPI.getInstance().getParty(player);
            if (party == null
                    || party.getLeader().equals(player)
                    || party.isOp(player)) {
                final Player summoned = Bukkit.getPlayer(split[2]);
                if (summoned == null) {
                    player.sendMessage(ChatColor.RED + "Player not found!");
                } else {
                    if (summoned.equals(player)) {
                        player.sendMessage(ChatColor.RED + "You can't invite yourself.");
                    } else {
                        if (summoned.hasPermission("parties.use")) {
                            if (PartiesAPI.getInstance().isInvited(player)) {
                                if (PartiesAPI.getInstance().getInvite(summoned).getHost().equals(player)) {
                                    player.sendMessage(ChatColor.RED + "This player is already invited by you.");
                                } else {
                                    player.sendMessage(ChatColor.RED + "This player is already invited by someone else.");
                                }
                            } else {
                                if (PartiesAPI.getInstance().inParty(summoned)) {
                                    player.sendMessage(ChatColor.RED + "This player is already in a party.");
                                } else {
                                    PartiesAPI.getInstance().invite(player, summoned);
                                    summoned.sendMessage(ChatColor.GOLD + "You have been invited to a party by " + ChatColor.RESET + player.getDisplayName());
                                    summoned.sendMessage(ChatColor.GOLD + "Type " + ChatColor.AQUA + "/p accept" + ChatColor.GOLD + " or " + ChatColor.AQUA + "/p decline" + ChatColor.GOLD + " to react.");
                                    summoned.sendMessage(ChatColor.GOLD + "This invite will expire in ~" + PartiesPlugin.TIMEOUT + " seconds.");
                                    if (party == null) {
                                        player.sendMessage(summoned.getDisplayName() + ChatColor.GOLD + " has been invited to build a party with you.");
                                    } else {
                                        party.sendPartyMessage(summoned.getDisplayName() + ChatColor.GOLD + " has been invited by " + ChatColor.RESET + player.getDisplayName() + ChatColor.GOLD + " to join the party.");
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Sorry, this player can't join parties.");
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You're not allowed to invite others to this party. Tell the party leader to op you.");
            }
        } else {
            sender.sendMessage("You can't invite to parties!");
        }
        return true;
    }

    @Override
    public String[] getPermission() {
        return new String[] { "parties.use" };
    }
}
