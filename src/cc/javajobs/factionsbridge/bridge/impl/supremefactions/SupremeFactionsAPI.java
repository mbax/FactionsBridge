package cc.javajobs.factionsbridge.bridge.impl.supremefactions;

import cc.javajobs.factionsbridge.bridge.IClaim;
import cc.javajobs.factionsbridge.bridge.IFaction;
import cc.javajobs.factionsbridge.bridge.IFactionPlayer;
import cc.javajobs.factionsbridge.bridge.impl.factionsuuid.FactionsUUIDAPI;
import cc.javajobs.factionsbridge.bridge.impl.savagefactions.SavageFactionsFaction;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Callum Johnson
 * @version 1.0
 * @since 27/02/2021 - 17:34
 */
public class SupremeFactionsAPI extends FactionsUUIDAPI {

    /**
     * Method to obtain an IClaim from Location.
     *
     * @param location to get IClaim from.
     * @return IClaim object.
     */
    @Override
    public IClaim getClaimAt(Location location) {
        return new SupremeFactionsClaim((FLocation) super.getClaimAt(location).asObject());
    }

    /**
     * Method to obtain an IClaim from Chunk.
     *
     * @param chunk to convert
     * @return IClaim object.
     */
    @Override
    public IClaim getClaimAt(Chunk chunk) {
        return new SupremeFactionsClaim((FLocation) super.getClaimAt(chunk).asObject());
    }

    /**
     * Method to obtain all Factions.
     *
     * @return IFactions in the form of a List.
     */
    @Override
    public List<IFaction> getAllFactions() {
        return Factions.getInstance().getAllFactions()
                .stream().map(SavageFactionsFaction::new).collect(Collectors.toList());
    }

    /**
     * Method to obtain a Faction from Location.
     *
     * @param location of the faction.
     * @return IFaction at that location
     */
    @Override
    public IFaction getFactionAt(Location location) {
        return new SupremeFactionsFaction((Faction) super.getFactionAt(location).asObject());
    }

    /**
     * Method to retrieve an IFaction from Id.
     *
     * @param id of the IFaction
     * @return IFaction implementation.
     */
    @Override
    public IFaction getFaction(String id) {
        return new SupremeFactionsFaction((Faction) super.getFaction(id).asObject());
    }

    /**
     * Method to retrive an IFaction from Name.
     *
     * @param name of the IFaction
     * @return IFaction implementation.
     */
    @Override
    public IFaction getFactionByName(String name) {
        return new SupremeFactionsFaction((Faction) super.getFactionByName(name).asObject());
    }

    /**
     * Method to retrieve an IFaction from Player/OfflinePlayer.
     *
     * @param player in the IFaction.
     * @return IFaction implementation.
     */
    @Override
    public IFaction getFaction(OfflinePlayer player) {
        return new SupremeFactionsFaction((Faction) super.getFaction(player).asObject());
    }

    /**
     * Method to get an IFactionPlayer from Player/OfflinePlayer.
     *
     * @param player related to the IFactionPlayer.
     * @return IFactionPlayer implementation.
     */
    @Override
    public IFactionPlayer getFactionPlayer(OfflinePlayer player) {
        return new SupremeFactionsPlayer((FPlayer) super.getFactionPlayer(player).asObject());
    }

    /**
     * Method to create a new Faction with the given name.
     *
     * @param name of the new Faction.
     * @return IFaction implementation.
     * @throws IllegalStateException if the IFaction exists already.
     */
    @Override
    public IFaction createFaction(String name) throws IllegalStateException {
        return new SupremeFactionsFaction((Faction) super.createFaction(name).asObject());
    }

}
