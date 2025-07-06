package cymru.asheiou.gdprbot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.lib.manager.UserMapManager
import xyz.aeolia.lib.sender.MessageSender
import xyz.aeolia.lib.utils.Message
import java.io.File
import java.util.UUID

class GDPRCommand(val plugin: JavaPlugin) : CommandExecutor {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    if (args.size != 2) {
      MessageSender.sendMessage(sender, Message.Generic.COMMAND_USAGE)
      return false
    }
    val uuid = UserMapManager.getUuidFromName(args[1]) ?: run {
      MessageSender.sendMessage(sender, Message.Player.NOT_FOUND)
      return true
    }
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
      commandCoro(uuid, sender, args)
      return@launch
    }
    return true
  }

  fun commandCoro(uuid: UUID, sender: CommandSender, args: Array<out String>) : Boolean {
    val baseDir = Bukkit.getServer().worldContainer.absoluteFile.toPath()
    val trawl = Trawler(baseDir, uuid.toString()).run()
    when (args[0]) {
      "copy" -> {
        val copyFolder = File(plugin.dataFolder, uuid.toString())
        if(!copyFolder.exists()){
          copyFolder.mkdirs()
        }
        trawl.forEach {
          val copyPath = it.absolutePath.replace(baseDir.toString(),
            "${plugin.dataFolder.absolutePath}${File.separatorChar}}${uuid.toString()}", true)
          val copyFile = File(copyPath)
          it.copyTo(copyFile)
          MessageSender.sendMessage(sender, "Found and copied file from ${it.absolutePath}")
        }
      }
      "delete" -> {
        trawl.forEach {
          if (it.name.contains(uuid.toString())) {
            MessageSender.sendMessage(sender, "Found and deleted file from ${it.absolutePath}")
            it.delete()
          }
        }
      }
      else -> {
        MessageSender.sendMessage(sender, Message.Generic.COMMAND_USAGE)
        return false
      }
    }
    return true
  }
}