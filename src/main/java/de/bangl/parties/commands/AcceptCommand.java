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
public class AcceptCommand extends AbstractCommand {

    @Override
    public boolean canExecute(final CommandSender sender, final String[] split) {
        return split.length == 2
                && split[0].equalsIgnoreCase("party")
                && split[1].equalsIgnoreCase("accept");
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] split) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (PartiesAPI.getInstance().isInvited(player)) {
                final Player host = PartiesAPI.getInstance().getInvite(player).getHost();
                Party party = PartiesAPI.getInstance().getParty(host);
                if (party == null) {
                    party = new Party(host);
                    PartiesAPI.getInstance().addParty(party);
                }
                party.sendPartyMessage(player.getDisplayName() + ChatColor.GOLD + " has joined the party!");
                PartiesAPI.getInstance().uninvite(player);
                party.addMember(player);
                player.sendMessage(ChatColor.GOLD + "You joined a party:");
                player.sendMessage(party.listMembers());
            } else {
                player.sendMessage(ChatColor.RED + "You are not invited by anybody.");
            }
        } else {
            sender.sendMessage("You can't join parties!");
        }
        return true;
    }

    @Override
    public String[] getPermission() {
        return new String[] { "parties.use" };
    }
}
