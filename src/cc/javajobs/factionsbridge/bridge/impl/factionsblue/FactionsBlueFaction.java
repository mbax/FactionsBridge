package cc.javajobs.factionsbridge.bridge.impl.factionsblue;

import cc.javajobs.factionsbridge.bridge.IClaim;
import cc.javajobs.factionsbridge.bridge.IFaction;
import cc.javajobs.factionsbridge.bridge.IFactionPlayer;
import cc.javajobs.factionsbridge.bridge.IRelationship;
import cc.javajobs.factionsbridge.bridge.exceptions.BridgeMethodUnsupportedException;
import me.zysea.factions.FPlugin;
import me.zysea.factions.faction.Faction;
import me.zysea.factions.faction.role.Role;
import me.zysea.factions.objects.ProtectedLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FactionsBlue Implementation of the IFaction.
 *
 * @author Callum Johnson
 * @since 26/02/2021 - 14:05
 */
public class FactionsBlueFaction implements IFaction {

    private final Faction faction;

    public FactionsBlueFaction(Faction faction) {
        this.faction = faction;
    }

    /**
     * Method to get the Id of the Faction.
     *
     * @return Id in the form of String.
     */
    @Override
    public String getId() {
        return faction.getId().toString();
    }

    /**
     * Method to get the Name of the Faction.
     *
     * @return name of the Faction.
     */
    @Override
    public String getName() {
        return faction.getName();
    }

    /**
     * Method to get the IFactionPlayer Leader.
     *
     * @return the person who created the Faction.
     */
    @Override
    public IFactionPlayer getLeader() {
        for (OfflinePlayer allMember : faction.getMembers().getAllMembers()) {
            Role role = faction.getRoles().getMemberRole(allMember.getUniqueId());
            if (role.getId() == 4) {
                return new FactionsBluePlayer(FPlugin.getInstance().getFPlayers().getFPlayer(allMember));
            }
        }
        return null;
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
        return faction.getAllClaims().stream().map(FactionsBlueClaim::new).collect(Collectors.toList());
    }

    /**
     * Method to get all of the Members for the Faction.
     *
     * @return List of IFactionPlayer
     */
    @Override
    public List<IFactionPlayer> getMembers() {
        return faction.getAllMembers()
                .stream().map(allMember -> FPlugin.getInstance().getFPlayers().getFPlayer(allMember))
                .map(FactionsBluePlayer::new).collect(Collectors.toList());
    }

    /**
     * Method to set the 'Home' of a Faction.
     *
     * @param location to set as the new home.
     */
    @Override
    public void setHome(Location location) {
        faction.setHome(new ProtectedLocation("home", location));
    }

    /**
     * Method to retrieve the 'Home' of the Faction.
     *
     * @return {@link Bukkit}, {@link Location}.
     */
    @Override
    public Location getHome() {
        return faction.getHome().getLocation();
    }

    /**
     * Method to get the relationship between two Factions.
     *
     * @param other faction to test
     * @return {@link IRelationship}
     */
    @Override
    public IRelationship getRelationTo(IFaction other) {
        if (faction.isAlliedTo((Faction) other.asObject())) {
            return IRelationship.ALLY;
        } else if (getId().equals(other.getId())) {
            return IRelationship.MEMBER;
        } else {
            return IRelationship.ENEMY;
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
        if (other.getFaction() == null) return IRelationship.ENEMY;
        return getRelationTo(other.getFaction());
    }

    /**
     * Method to return the IFaction as an Object (API friendly)
     *
     * @return object of API.
     */
    @Override
    public Object asObject() {
        return faction;
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
        return getId().equals("-2") || getId().equals("-1") || getId().equals("0");
    }

    /**
     * Method to determine if the IFaction is the WarZone.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isWarZone() {
        return faction.isWarzone();
    }

    /**
     * Method to determine if the IFaction is a SafeZone.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isSafeZone() {
        return faction.isSafezone();
    }

    /**
     * Method to determine if the IFaction is the Wilderness.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isWilderness() {
        return faction.isWilderness();
    }

    /**
     * Method to determine if the Faction is in a Peaceful State.
     *
     * @return {@code true} if yes, {@code false} if no.
     */
    @Override
    public boolean isPeaceful() {
        throw new BridgeMethodUnsupportedException("FactionsBlue doesn't support isPeaceful().");
    }

    /**
     * Method to get the bank balance of the Faction.
     *
     * @return in the form of Double.
     */
    @Override
    public double getBank() {
        throw new BridgeMethodUnsupportedException("FactionsBlue doesn't support getBank().");
    }

    /**
     * Method to get the points of a Faction.
     *
     * @return in the form of Integer.
     */
    @Override
    public int getPoints() {
        throw new BridgeMethodUnsupportedException("FactionsBlue doesn't support getPoints().");
    }

    /**
     * Method to get the Location of a Faction Warp by Name.
     *
     * @param name of the warp
     * @return {@link Location} of the warp.
     */
    @Override
    public Location getWarp(String name) {
        return faction.getWarp(name).getLocation();
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
        return faction.getWarps().stream()
                .collect(Collectors.toMap(
                        ProtectedLocation::getName, // k
                        ProtectedLocation::getLocation, // v
                        (a, b) -> b, HashMap::new // assign
                ));
    }

    /**
     * Method to create a warp for the Faction.
     *
     * @param name     of the warp.
     * @param location of the warp.
     */
    @Override
    public void createWarp(String name, Location location) {
        faction.setWarp(new ProtectedLocation(name, location));
    }

    /**
     * Method to manually remove a Warp using its name.
     *
     * @param name of the warp to be deleted.
     */
    @Override
    public void deleteWarp(String name) {
        faction.delWarp(name);
    }

    /**
     * Add strikes to a Faction.
     *
     * @param sender who desires to Strike the Faction.
     * @param reason for the Strike.
     */
    @Override
    public void addStrike(String sender, String reason) {
        throw new BridgeMethodUnsupportedException("FactionsBlue doesn't support addStrike(Sender, String).");
    }

    /**
     * Remove strike from a Faction.
     *
     * @param sender who desires to remove the Strike from the Faction.
     * @param reason of the original Strike.
     */
    @Override
    public void removeStrike(String sender, String reason) {
        throw new BridgeMethodUnsupportedException("FactionsBlue doesn't support removeStrike(Sender, String).");
    }

    /**
     * Method to obtain the Total Strikes a Faction has.
     *
     * @return integer amount of Strikes.
     */
    @Override
    public int getTotalStrikes() {
        throw new BridgeMethodUnsupportedException("FactionsBlue doesn't support getTotalStrikes().");
    }

    /**
     * Method to clear all Strikes.
     */
    @Override
    public void clearStrikes() {
        throw new BridgeMethodUnsupportedException("FactionsBlue doesn't support clearStrikes().");
    }

}
