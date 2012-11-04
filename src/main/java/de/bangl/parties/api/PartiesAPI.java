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
package de.bangl.parties.api;

import de.bangl.parties.Invite;
import de.bangl.parties.Party;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class PartiesAPI {

    private static ArrayList<Party> parties = new ArrayList<>();
    private static ArrayList<Player> directedChat = new ArrayList<>();
    private static HashMap<Player, Invite> invites = new HashMap<>();

    private PartiesAPI(){}

    private static class PartiesAPIHolder {
        private static final PartiesAPI INSTANCE = new PartiesAPI();
    }

    public static PartiesAPI getInstance() {
        return PartiesAPI.PartiesAPIHolder.INSTANCE;
    }

    public void addParty(final Party party) {
        PartiesAPI.parties.add(party);
    }

    public void removeParty(final Party party) {
        PartiesAPI.parties.remove(party);
    }

    public boolean contains(final Party party) {
        return PartiesAPI.parties.contains(party);
    }

    public boolean isMember(final Party party, final Player player) {
        return party.isMember(player);
    }

    public boolean inParty(final Player player) {
        for (final Party party: PartiesAPI.parties) {
            if (party.isMember(player)) {
                return true;
            }
        }
        return false;
    }

    public Party getParty(final Player player) {
        for (final Party party: PartiesAPI.parties) {
            if (party.isMember(player)) {
                return party;
            }
        }
        return null;
    }

    public boolean getPartyChatMode(final Player player) {
        return PartiesAPI.directedChat.contains(player);
    }

    public void setPartyChatMode(final Player player, final boolean partyChat) {
        if (partyChat) {
            if (!PartiesAPI.directedChat.contains(player)) {
                PartiesAPI.directedChat.add(player);
            }
        } else {
            if (PartiesAPI.directedChat.contains(player)) {
                PartiesAPI.directedChat.remove(player);
            }
        }
    }

    public boolean isInvited(final Player player) {
        return PartiesAPI.invites.containsKey(player);
    }

    public Invite getInvite(final Player player) {
        return PartiesAPI.invites.get(player);
    }

    public Collection<Invite> getInvites() {
        return PartiesAPI.invites.values();
    }

    public void invite(final Player host, final Player summoned) {
        PartiesAPI.invites.put(summoned, new Invite(host, summoned));
    }

    public void uninvite(final Player summoned) {
        PartiesAPI.invites.remove(summoned);
    }
}
