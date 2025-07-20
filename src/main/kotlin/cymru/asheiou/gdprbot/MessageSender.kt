package cymru.asheiou.gdprbot

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage

class MessageSender {
  companion object {
    fun sendMessage(recipient: Audience, message: String, prefix: Boolean = true) {
      val prefixed = if (prefix) "[<#bbaa99>GDPR</#bbaa99>] $message" else message
      val toSend = MiniMessage.miniMessage().deserialize(prefixed)
      recipient.sendMessage(toSend)
    }
  }
}