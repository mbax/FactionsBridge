package cc.javajobs.factionsbridge.bridge.impl.factionsuuid;

import cc.javajobs.factionsbridge.bridge.IClaim;
import cc.javajobs.factionsbridge.bridge.IFaction;
import cc.javajobs.factionsbridge.bridge.IFactionPlayer;
import cc.javajobs.factionsbridge.bridge.IRelationship;
import cc.javajobs.factionsbridge.bridge.exceptions.BridgeMethodException;
import cc.javajobs.factionsbridge.bridge.exceptions.BridgeMethodUnsupportedException;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.zcore.persist.MemoryFaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FactionsUUID implementation of IFaction.
 *
 * @author Callum Johnson
 * @since 26/02/2021 - 14:55
 */
public class FactionsUUIDFaction implements IFaction {

    protected final Faction f;

    public FactionsUUIDFaction(Faction faction) {
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
        return f.getTag();
    }

    /**
     * Method to get the IFactionPlayer Leader.
     *
     * @return the person who created the Faction.
     */
    @Override
    public IFactionPlayer getLeader() {
        return new FactionsUUIDPlayer(f.getFPlayerAdmin());
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
        return f.getAllClaims().stream().map(FactionsUUIDClaim::new).collect(Collectors.toList());
    }

    /**
     * Method to get all of the Members for the Faction.
     *
     * @return List of IFactionPlayer
     */
    @Override
    public List<IFactionPlayer> getMembers() {
        return f.getFPlayers().stream().map(FactionsUUIDPlayer::new).collect(Collectors.toList());
    }

    /**
     * Method to set the 'Home' of a Faction.
     *
     * @param location to set as the new home.
     */
    @Override
    public void setHome(Location location) {
        f.setHome(location);
    }

    /**
     * Method to retrieve the 'Home' of the Faction.
     *
     * @return {@link Bukkit}, {@link Location}.
     */
    @Override
    public Location getHome() {
        return f.getHome();
    }

    /**
     * Method to get the relationship between two Factions.
     *
     * @param other faction to test
     * @return {@link IRelationship}
     */
    @Override
    public IRelationship getRelationTo(IFaction other) {
        try {
            Class.forName("com.massivecraft.factions.zcore.persist.MemoryFaction");
            MemoryFaction faction = (MemoryFaction) f;
            MemoryFaction otherFaction = (MemoryFaction) other.asObject();
            return IRelationship.getRelationship(faction.getRelationTo(otherFaction).name());
        } catch (ClassNotFoundException ex) {
            // FactionsBridge.get().error("Failed to find 'persist mfac'");
            try {
                Class.forName("com.massivecraft.factions.data.MemoryFaction");
                com.massivecraft.factions.data.MemoryFaction faction = (com.massivecraft.factions.data.MemoryFaction) f;
                com.massivecraft.factions.data.MemoryFaction otherFaction = (com.massivecraft.factions.data.MemoryFaction) other.asObject();
                return IRelationship.getRelationship(faction.getRelationTo(otherFaction).name());
            } catch (ClassNotFoundException ignored) {
                // FactionsBridge.get().error("Failed to find 'data mfac'");
                throw new BridgeMethodException(getClass(), "getRelationTo(IFaction)");
            }
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
        if (other.getFaction() == null) return IRelationship.NONE;
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
        return (f.isSafeZone() || f.isWarZone() || f.isWilderness());
    }

    /**
     * Method to determine if the IFaction is the WarZone.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isWarZone() {
        return f.isWarZone();
    }

    /**
     * Method to determine if the IFaction is a SafeZone.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isSafeZone() {
        return f.isSafeZone();
    }

    /**
     * Method to determine if the IFaction is the Wilderness.
     *
     * @return {@code true} if it is.
     */
    @Override
    public boolean isWilderness() {
        return f.isWilderness();
    }

    /**
     * Method to determine if the Faction is in a Peaceful State.
     *
     * @return {@code true} if yes, {@code false} if no.
     */
    @Override
    public boolean isPeaceful() {
        return f.isPeaceful();
    }

    /**
     * Method to get the bank balance of the Faction.
     *
     * @return in the form of Double.
     */
    @Override
    public double getBank() {
        try {
            if (!Econ.hasAccount(f)) return 0.0;
            return Econ.getBalance(f);
        } catch (Exception ex) {
            throw new BridgeMethodException(getClass(), "getBank()", "Economy Potentially Disabled.");
        }
    }

    /**
     * Method to get the points of a Faction.
     *
     * @return in the form of Integer.
     */
    @Override
    public int getPoints() {
        throw new BridgeMethodUnsupportedException("FactionsUUID doesn't support getPoints().");
    }

    /**
     * Method to get the Location of a Faction Warp by Name.
     *
     * @param name of the warp
     * @return {@link Location} of the warp.
     */
    @Override
    public Location getWarp(String name) {
        return f.getWarp(name).getLocation();
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
        HashMap<String, Location> data = new HashMap<>();
        f.getWarps().forEach((key, value) -> data.put(key, value.getLocation()));
        return data;
    }

    /**
     * Method to create a warp for the Faction.
     *
     * @param name     of the warp.
     * @param location of the warp.
     */
    @Override
    public void createWarp(String name, Location location) {
        f.setWarp(name, new LazyLocation(location));
    }

    /**
     * Method to manually remove a Warp using its name.
     *
     * @param name of the warp to be deleted.
     */
    @Override
    public void deleteWarp(String name) {
        f.removeWarp(name);
    }

    /**
     * Add strikes to a Faction.
     *
     * @param sender who desires to Strike the Faction.
     * @param reason for the Strike.
     */
    @Override
    public void addStrike(String sender, String reason) {
        throw new BridgeMethodUnsupportedException("FactionsUUID doesn't support addStrike(Sender, String).");
    }

    /**
     * Remove strike from a Faction.
     *
     * @param sender who desires to remove the Strike from the Faction.
     * @param reason of the original Strike.
     */
    @Override
    public void removeStrike(String sender, String reason) {
        throw new BridgeMethodUnsupportedException("FactionsUUID doesn't support removeStrike(Sender, String).");
    }

    /**
     * Method to obtain the Total Strikes a Faction has.
     *
     * @return integer amount of Strikes.
     */
    @Override
    public int getTotalStrikes() {
        throw new BridgeMethodUnsupportedException("FactionsUUID doesn't support getTotalStrikes().");
    }

    /**
     * Method to clear all Strikes.
     */
    @Override
    public void clearStrikes() {
        throw new BridgeMethodUnsupportedException("FactionsUUID doesn't support clearStrikes().");
    }

}
