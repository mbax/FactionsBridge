package cc.javajobs.factionsbridge.bridge.impl.kingdoms;

import cc.javajobs.factionsbridge.bridge.IClaim;
import cc.javajobs.factionsbridge.bridge.IFaction;
import cc.javajobs.factionsbridge.bridge.IFactionPlayer;
import cc.javajobs.factionsbridge.bridge.IRelationship;
import cc.javajobs.factionsbridge.bridge.exceptions.BridgeMethodUnsupportedException;
import org.kingdoms.constants.kingdom.Kingdom;
import org.kingdoms.constants.kingdom.model.KingdomRelation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Kingdoms implementation of IFaction.
 *
 * @author Callum Johnson
 * @version 1.0
 * @since 26/02/2021 - 17:11
 */
public class KingdomsKingdom implements IFaction {

    private final Kingdom k;

    public KingdomsKingdom(Kingdom kingdom) {
        this.k = kingdom;
    }

    /**
     * Method to get the Id of the Faction.
     *
     * @return Id in the form of String.
     */
    @Override
    public String getId() {
        return k.getId().toString();
    }

    /**
     * Method to get the Name of the Faction.
     *
     * @return name of the Faction.
     */
    @Override
    public String getName() {
        return k.getName();
    }

    /**
     * Method to get the IFactionPlayer Leader.
     *
     * @return the person who created the Faction.
     */
    @Override
    public IFactionPlayer getLeader() {
        return new KingdomsPlayer(k.getKing());
    }

    /**
     * Method to get the name of the Leader.
     *
     * @return name of the person who created the Faction.
     * @see IFaction#getLeader()
     */
    @Override
    public String getLeaderName() {
        return getLeader().getName();
    }

    /**
     * Method to get all Claims related to the Faction.
     *
     * @return Claims in the form List of {@link IClaim}
     */
    @Override
    public List<IClaim> getAllClaims() {
        return k.getLands().stream().map(KingdomsClaim::new).collect(Collectors.toList());
    }

    /**
     * Method to get all of the Members for the Faction.
     *
     * @return List of IFactionPlayer
     */
    @Override
    public List<IFactionPlayer> getMembers() {
        return k.getKingdomPlayers().stream().map(KingdomsPlayer::new).collect(Collectors.toList());
    }

    /**
     * Method to get the relationship between two Factions.
     *
     * @param other faction to test
     * @return {@link IRelationship}
     */
    @Override
    public IRelationship getRelationTo(IFaction other) {
        KingdomRelation relationWith = k.getRelationWith((Kingdom) other.asObject());
        switch (relationWith) {
            case NEUTRAL:
                return IRelationship.NONE;
            case TRUCE:
                return IRelationship.TRUCE;
            case ALLY:
                return IRelationship.ALLY;
            case ENEMY:
                return IRelationship.ENEMY;
            default:
                return IRelationship.MEMBER;
        }
    }

    /**
     * Method to get the relationship between an IFaction and an IFactionPlayer.
     *
     * @param other IFactionPlayer to test
     * @return {@link IRelationship}
     */
    @Override
    public IRelationship getRelationTo(IFactionPlayer other) {
        return getRelationTo(other.getFaction());
    }

    /**
     * Method to return the IFaction as an Object (API friendly)
     *
     * @return object of API.
     */
    @Override
    public Object asObject() {
        return k;
    }

    /**
     * Method to test if this Faction is a Server Faction
     * <p>
     * Server Factions: Wilderness, SafeZone, WarZone.
     * </p>
     *
     * @return {@code true} if yes, {@code false} if no.
     */
    @Override
    public boolean isServerFaction() {
        return false;
    }

    /**
     * Method to get the bank balance of the Faction.
     *
     * @return in the form of Double.
     */
    @Override
    public double getBank() {
        return k.getBank();
    }

    /**
     * Method to get the points of a Faction.
     *
     * @return in the form of Integer.
     */
    @Override
    public int getPoints() {
        throw new BridgeMethodUnsupportedException("Kingdoms doesn't support getPoints().");
    }

}