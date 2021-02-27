package cc.javajobs.factionsbridge.bridge.impl.factionsx;

import cc.javajobs.factionsbridge.bridge.IFaction;
import cc.javajobs.factionsbridge.bridge.IFactionPlayer;
import cc.javajobs.factionsbridge.bridge.IRelationship;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.util.Relation;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * FactionsX implementation of IFactionPlayer.
 * 
 * @author Callum Johnson
 * @version 1.0
 * @since 26/02/2021 - 16:31
 */
public class FactionsXPlayer implements IFactionPlayer {

    private final FPlayer fpl;

    public FactionsXPlayer(FPlayer fpl) {
        this.fpl = fpl;
    }

    /**
     * Method to get the unique Id of the Faction Player.
     *
     * @return UUID (UniqueId).
     */
    @Override
    public UUID getUniqueId() {
        return fpl.getUuid();
    }

    /**
     * Method to get the Name of the Faction Player.
     *
     * @return name of the Player.
     */
    @Override
    public String getName() {
        return fpl.getName();
    }

    /**
     * Method to get the Faction linked to the IFactionPlayer.
     *
     * @return faction of the player.
     */
    @Override
    public IFaction getFaction() {
        return new FactionsXFaction(fpl.getFaction());
    }

    /**
     * Method to get the Offline form of the Player.
     *
     * @return {@link OfflinePlayer}
     */
    @Override
    public OfflinePlayer getOfflinePlayer() {
        return fpl.getOfflinePlayer();
    }

    /**
     * Method to get the Online form of the Player.
     *
     * @return {@link Player}
     * @see IFactionPlayer#isOnline()
     */
    @Override
    public Player getPlayer() {
        return fpl.getPlayer();
    }

    /**
     * Uses Bukkit methodology to determine if the Player is online.
     *
     * @return {@code true} = yes, {@code false} = no.
     */
    @Override
    public boolean isOnline() {
        return fpl.isOnline();
    }

    /**
     * Method to get the relationship from a Player to a Faction.
     *
     * @param other faction to test
     * @return {@link IRelationship}
     */
    @Override
    public IRelationship getRelationTo(IFaction other) {
        return getFaction().getRelationTo(other);
    }

    /**
     * Method to get the relationship from a Player to another Player.
     *
     * @param other IFactionPlayer to test
     * @return {@link IRelationship}
     */
    @Override
    public IRelationship getRelationTo(IFactionPlayer other) {
        return getFaction().getRelationTo(other.getFaction());
    }

    /**
     * Method to return the IFactionPlayer as an Object (API friendly)
     *
     * @return object of API.
     */
    @Override
    public Object asObject() {
        return fpl;
    }

}
