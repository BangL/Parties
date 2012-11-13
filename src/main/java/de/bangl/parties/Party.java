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
package de.bangl.parties;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class Party {

    private Player leader;
    private transient final ArrayList<Player> members = new ArrayList<Player>();
    private transient final ArrayList<Player> ops = new ArrayList<Player>();

    public Party(final Player leader) {
        this.leader = leader;
        members.add(leader);
    }

    public Player getLeader() {
        return leader;
    }

    public void setLeader(final Player leader) {
        this.leader = leader;
    }

    public void addMember(final Player player) {
        members.add(player);
    }

    public void removeMember(final Player player) {
        members.remove(player);
    }

    public boolean isMember(final Player player) {
        return members.contains(player);
    }

    public void sendPartyChat(final Player player, final String message) {
        for (final Player member : members) {
            member.sendMessage(ChatColor.GOLD + "Party " + ChatColor.RESET + player.getDisplayName() + ChatColor.GOLD + " :" + ChatColor.DARK_GREEN + message);
        }
    }

    public void sendPartyMessage(final String message) {
        for (final Player member : members) {
            member.sendMessage(message);
        }
    }

    public String listMembers() {
        int total = 0;
        String list = "**" + this.getLeader().getDisplayName() + ChatColor.GOLD + ", ";
        for (final Player player : members) {
            if (!this.isLeader(player)) {
                if (this.isOp(player)) {
                    list = list.concat("*");
                }
                list = list.concat(player.getDisplayName() + ChatColor.GOLD + ", ");
            }
            total++;
        }
        return ChatColor.GOLD + "Party members (" + total + "): " + list.substring(0, list.length() - 2);
    }

    public boolean isEmpty() {
        return members.size() <= 1;
    }

    public void setRandomLeader() {
        setLeader(members.get(0));
    }

    public boolean isLeader(final Player player) {
        return getLeader().equals(player);
    }

    public boolean isOp(final Player player) {
        return ops.contains(player);
    }

    public void op(final Player player) {
        ops.add(player);
    }

    public void deop(final Player player) {
        ops.remove(player);
    }
}
