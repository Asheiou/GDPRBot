package cymru.asheiou.gdprbot

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserMapEvent : Listener {
  @EventHandler(priority = EventPriority.LOWEST)
  fun onPlayerJoinEvent(event: PlayerJoinEvent) {
    val player = event.player
    val refUuid = UserMapManager.getUuidFromName(player.name).let {
      if (it == player.uniqueId) return
    }
    UserMapManager.userMap.put(player.name, player.uniqueId.toString())
    return
  }
}