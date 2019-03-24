/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.instances.command;

import java.util.List;

import org.bukkit.entity.Player;
import org.cyberiantiger.minecraft.instances.Instances;
import org.cyberiantiger.minecraft.instances.Party;

/**
 *
 * @author antony
 */
public class PartyInvite extends AbstractCommand {

    public PartyInvite() {
        super(SenderType.PLAYER);
    }

    @Override
    public List<String> execute(Instances instances, Player player, String[] args) {
        if (args.length != 1) {
            return null; // Usage
        }
        Party party = instances.getParty(player);
        if (party == null) {
            throw new InvocationException("You are not in a party.");
        }
        if (instances.isLeaderInvite() && !player.equals(party.getLeader())) {
            throw new InvocationException("You are not the party leader.");
        }
        Player invitee = instances.getServer().getPlayer(args[0]);
        if (invitee == null) {
            throw new InvocationException("No such player: " + args[0] + ".");
        }
        if (!invitee.isOnline()) {
            throw new InvocationException(args[0] + " is not online.");
        }
        boolean invited = party.getInvites().contains(invitee);
        boolean member = party.getMembers().contains(invitee);
        if (member) {
            throw new InvocationException(args[0] + " is already a party member.");
        }
        if (invited) {
            throw new InvocationException(args[0] + " is already invited.");
        }
        party.getInvites().add(invitee);
        party.emote(instances, invitee, "has been invited to join by " + player.getDisplayName() + '.');
        invitee.sendMessage("You have been invited to join party " + party.getName() + " type /pjoin " + party.getName());
        return msg();
    }
}
