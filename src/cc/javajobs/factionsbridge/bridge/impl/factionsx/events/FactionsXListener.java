package cc.javajobs.factionsbridge.bridge.impl.factionsx.events;

import cc.javajobs.factionsbridge.FactionsBridge;
import cc.javajobs.factionsbridge.bridge.IFactionsAPI;
import cc.javajobs.factionsbridge.bridge.events.*;
import cc.javajobs.factionsbridge.bridge.impl.factionsx.FactionsXFaction;
import cc.javajobs.factionsbridge.bridge.impl.factionsx.FactionsXPlayer;
import net.prosavage.factionsx.event.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * FactionsX implementation of the Bridges needed to handle all Custom Events.
 *
 * @author Callum Johnson
 * @since 28/02/2021 - 10:23
 */
public class FactionsXListener implements Listener {

    private final IFactionsAPI api = FactionsBridge.getFactionsAPI();

    @EventHandler
    public void onClaim(FactionPreClaimEvent event) {
        IClaimClaimEvent bridgeEvent = new IClaimClaimEvent(
                api.getClaimAt(event.getFLocation().getChunk()),
                api.getFaction(String.valueOf(event.getFactionClaiming().getId())),
                api.getFactionPlayer(event.getFplayer().getPlayer()),
                event
        );
        Bukkit.getPluginManager().callEvent(bridgeEvent);
        event.setCancelled(bridgeEvent.isCancelled());
    }

    @EventHandler
    public void onUnclaimAll(FactionUnClaimAllEvent event) {
        IClaimUnclaimAllEvent bridgeEvent = new IClaimUnclaimAllEvent(
                api.getFaction(String.valueOf(event.getUnclaimingFaction().getId())),
                api.getFactionPlayer(event.getFplayer().getPlayer()),
                event
        );
        Bukkit.getPluginManager().callEvent(bridgeEvent);
        event.setCancelled(bridgeEvent.isCancelled());
    }

    @EventHandler
    public void onUnclaim(FactionUnClaimEvent event) {
        IClaimUnclaimEvent bridgeEvent = new IClaimUnclaimEvent(
                api.getClaimAt(event.getFLocation().getChunk()),
                api.getFaction(String.valueOf(event.getFactionUnClaiming().getId())),
                api.getFactionPlayer(event.getFplayer().getPlayer()),
                event
        );
        Bukkit.getPluginManager().callEvent(bridgeEvent);
        event.setCancelled(bridgeEvent.isCancelled());
    }

    @EventHandler
    public void onCreate(FactionCreateEvent event) {
        IFactionCreateEvent bridgeEvent = new IFactionCreateEvent(
                new FactionsXFaction(event.getFaction()),
                new FactionsXPlayer(event.getFPlayer()),
                event
        );
        Bukkit.getPluginManager().callEvent(bridgeEvent);

    }

    @EventHandler
    public void onDisband(FactionDisbandEvent event) {
        IFactionDisbandEvent bridgeEvent = new IFactionDisbandEvent(
                api.getFactionPlayer(event.getFPlayer().getPlayer()),
                api.getFaction(String.valueOf(event.getFaction().getId())),
                IFactionDisbandEvent.DisbandReason.UNKNOWN,
                event
        );
        Bukkit.getPluginManager().callEvent(bridgeEvent);
    }

    @EventHandler
    public void onRename(FactionRenameEvent event) {
        IFactionRenameEvent bridgeEvent = new IFactionRenameEvent(
                api.getFaction(String.valueOf(event.getFaction().getId())),
                event.getNewTag(),
                event
        );
        Bukkit.getPluginManager().callEvent(bridgeEvent);
    }

    @EventHandler
    public void onJoin(FPlayerFactionJoinEvent event) {
        IFactionPlayerJoinIFactionEvent bridgeEvent = new IFactionPlayerJoinIFactionEvent(
                api.getFaction(String.valueOf(event.getFaction().getId())),
                api.getFactionPlayer(event.getFPlayer().getPlayer()),
                event
        );
        Bukkit.getPluginManager().callEvent(bridgeEvent);
    }

    @EventHandler
    public void onLeave(FPlayerFactionJoinEvent event) {
        IFactionPlayerLeaveIFactionEvent bridgeEvent = new IFactionPlayerLeaveIFactionEvent(
                api.getFaction(String.valueOf(event.getFaction().getId())),
                api.getFactionPlayer(event.getFPlayer().getPlayer()),
                (event.isAdmin() ? IFactionPlayerLeaveIFactionEvent.LeaveReason.KICK : IFactionPlayerLeaveIFactionEvent.LeaveReason.LEAVE),
                event
        );
        Bukkit.getPluginManager().callEvent(bridgeEvent);
    }

}
