package cymru.asheiou.gdprbot

import org.bukkit.plugin.java.JavaPlugin

class GDPRBot : JavaPlugin() {

  override fun onEnable() {
    getCommand("gdpr")?.setExecutor(GDPRCommand(this))
    logger.info("GDPRBot enabled")
  }

  override fun onDisable() {
    logger.info("ttyl")
  }
}
