package cymru.asheiou.gdprbot

import com.google.gson.Gson
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.lib.manager.UserMapManager
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Supplier


class UserMapManager {
  companion object {
    lateinit var plugin: JavaPlugin
    var isStandalone: Boolean = false
    lateinit var filePath: String
    var userMap = HashMap<String, String>()
    val gson = Gson()

    fun init(plugin: JavaPlugin) {
      this.plugin = plugin
      filePath = "${Companion.plugin.dataFolder}${File.separator}users.json"

      if(!Bukkit.getPluginManager().isPluginEnabled("AeoliaLib")) {
        plugin.logger.info("AeolaLib not found! Running standalone...")
        isStandalone = true
      }

      val file = File(filePath)

      if (file.exists()) {
        try {
          gson.toJson(HashMap<String?, String?>(), FileWriter(filePath))
        } catch (e: IOException) {
          throw RuntimeException(e)
        }
      } else {
        CompletableFuture.supplyAsync(Supplier supplyAsync@{
          try {
            val jsonContent: String? = Files.readString(Paths.get(filePath))
            return@supplyAsync gson.fromJson(jsonContent, MutableMap::class.java)
          } catch (e: Exception) {
            throw RuntimeException(e)
          }
        }
        ).thenAccept(
          Consumer { result: MutableMap<*, *>? ->
            @Suppress("UNCHECKED_CAST")
            userMap = result as HashMap<String, String>
            plugin.logger.info("users.json loaded with " + userMap.size + " users!")
          })
      }
    }

    fun getUuidFromName(name: String): UUID? {
      if(!isStandalone) return UserMapManager.getUuidFromName(name)

      if(userMap.containsKey(name)) return UUID.fromString(userMap[name])

      return null
    }


  }
}