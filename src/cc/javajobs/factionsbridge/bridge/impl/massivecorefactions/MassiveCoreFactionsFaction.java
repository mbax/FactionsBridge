package cc.javajobs.factionsbridge.bridge.impl.massivecorefactions;

import cc.javajobs.factionsbridge.bridge.IClaim;
import cc.javajobs.factionsbridge.bridge.IFaction;
import cc.javajobs.factionsbridge.bridge.IFactionPlayer;
import cc.javajobs.factionsbridge.bridge.IRelationship;
import cc.javajobs.factionsbridge.bridge.exceptions.BridgeMethodUnsupportedException;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MassiveCoreFactions implementation of IFaction.
 *
 * @author Callum Johnson
 * @since 26/02/2021 - 15:21
 */
public class MassiveCoreFactionsFaction implements IFaction {

    private final Faction f;

    public MassiveCoreFactionsFaction(Faction faction) {
        this.f = faction;
    }

    /**
     * Method to get the Id of the Faction.
     *
     * @return Id in the form of String.
     */
    @Override
    public String getId() {
        return f.getId();
    }

    /**
     * Method to get the Name of the Faction.
     *
     * @return name of the Faction.
     */
    @Override
    public String getName() {
        return f.getName();
    }

    /**
     * Method to get the IFactionPlayer Leader.
     *
     * @return the person who created the Faction.
     */
    @Override
    public IFactionPlayer getLeader() {
        return new MassiveCoreFactionsPlayer(f.getLeader());
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
        return BoardColl.get().getChunks(f).stream().map(MassiveCoreFactionsClaim::new).collect(Collectors.toList());
    }

    /**
     * Method to get all of the Members for the Faction.
     *
     * @return List of IFactionPlayer
     */
    @Override
    public List<IFactionPlayer> getMembers() {
        return f.getMPlayers().stream().map(MassiveCoreFactionsPlayer::new).collect(Collectors.toList());
    }

    /**
     * Method to set the 'Home' of a Faction.
     *
     * @param location to set as the new home.
     */
    @Override
    public void setHome(Location location) {
        f.setHome(PS.valueOf(location));
    }

    /**
     * Method to retrieve the 'Home' of the Faction.
     *
     * @return {@link Bukkit}, {@link Location}.
     */
    @Override
    public Location getHome() {
        return f.getHome().asBukkitLocation();
    }

    /**
     * Method to get the relationship between two Factions.
     *
     * @param other faction to test
     * @return {@link IRelationship}
     */
    @Override
    public IRelationship getRelationTo(IFaction other) {
        Rel rel =  f.getRelationTo((Faction) other.asObject());
        switch (rel) {
            case ENEMY:
                return IRelationship.ENEMY;
            case NEUTRAL:
                return IRelationship.NONE;
            case TRUCE:
                return IRelationship.TRUCE;
            case ALLY:
                return IRelationship.ALLY;
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
        return f;
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
        return Arrays.asList(
                "none", "safezone", "warzone", "wilderness",
                ChatColor.DARK_GREEN + "wilderness", ChatColor.DARK_GREEN + " wilderness"
        ).contains(getId().toLowerCase());
    }

    /**
     * Method to determine if the IFaction is the WarZone.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isWarZone() {
        return getId().equalsIgnoreCase("warzone");
    }

    /**
     * Method to determine if the IFaction is a SafeZone.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isSafeZone() {
        return getId().equalsIgnoreCase("safezone");
    }

    /**
     * Method to determine if the IFaction is the Wilderness.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isWilderness() {
        return Arrays.asList(
                "none", "wilderness", ChatColor.DARK_GREEN + "wilderness", ChatColor.DARK_GREEN + " wilderness"
        ).contains(getId().toLowerCase());
    }

    /**
     * Method to determine if the Faction is in a Peaceful State.
     *
     * @return {@code true} if yes, {@code false} if no.
     */
    @Override
    public boolean isPeaceful() {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support isPeaceful().");
    }

    /**
     * Method to get the bank balance of the Faction.
     *
     * @return in the form of Double.
     */
    @Override
    public double getBank() {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support getBank().");
    }

    /**
     * Method to get the points of a Faction.
     *
     * @return in the form of Integer.
     */
    @Override
    public int getPoints() {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support getPoints().");
    }

    /**
     * Method to get the Location of a Faction Warp by Name.
     *
     * @param name of the warp
     * @return {@link Location} of the warp.
     */
    @Override
    public Location getWarp(String name) {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support getWarp(name).");
    }

    /**
     * Method to retrieve all warps.
     * <p>
     * This method returns a hashmap of String names and Locations.
     * </p>
     *
     * @return hashmap of all warps.
     */
    @Override
    public HashMap<String, Location> getWarps() {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support getWarps().");
    }

    /**
     * Method to create a warp for the Faction.
     *
     * @param name     of the warp.
     * @param location of the warp.
     */
    @Override
    public void createWarp(String name, Location location) {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support createWarp(name, location).");
    }

    /**
     * Method to manually remove a Warp using its name.
     *
     * @param name of the warp to be deleted.
     */
    @Override
    public void deleteWarp(String name) {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support deleteWarp(name).");
    }

    /**
     * Add strikes to a Faction.
     *
     * @param sender who desires to Strike the Faction.
     * @param reason for the Strike.
     */
    @Override
    public void addStrike(String sender, String reason) {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support addStrike(Sender, String).");
    }

    /**
     * Remove strike from a Faction.
     *
     * @param sender who desires to remove the Strike from the Faction.
     * @param reason of the original Strike.
     */
    @Override
    public void removeStrike(String sender, String reason) {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support removeStrike(Sender, String).");
    }

    /**
     * Method to obtain the Total Strikes a Faction has.
     *
     * @return integer amount of Strikes.
     */
    @Override
    public int getTotalStrikes() {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support getTotalStrikes().");
    }

    /**
     * Method to clear all Strikes.
     */
    @Override
    public void clearStrikes() {
        throw new BridgeMethodUnsupportedException("MassiveCore doesn't support clearStrikes().");
    }

}
