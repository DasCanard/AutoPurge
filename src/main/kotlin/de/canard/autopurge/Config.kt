package de.canard.autopurge

import java.io.File
import java.io.FileOutputStream
import java.lang.IllegalArgumentException
import java.util.Properties

object Config {
    private const val CONFIG_FILE_NAME = "config.properties"
    private val properties = Properties().apply {
        val configFile = File(CONFIG_FILE_NAME)

        if (!configFile.exists()) {
            configFile.createNewFile()
            FileOutputStream(configFile).use { fos ->
                store(fos, null)
            }
        }

        configFile.inputStream().use { load(it) }
    }

    val TOKEN: String = System.getenv("BOT_TOKEN")?.takeIf { it.isNotBlank() }
        ?: properties.getProperty("BOT_TOKEN").takeIf { it.isNotBlank() }
        ?: throw IllegalArgumentException("BOT_TOKEN must not be empty.")

    data class ChannelPurgeSetting(val channelId: String, val messageAgeHours: Int)

    val PURGE_CHANNELS: List<ChannelPurgeSetting> = run {
        val purgeChannelsStr = System.getenv("PURGE_CHANNELS")?.takeIf { it.isNotBlank() }
            ?: properties.getProperty("PURGE_CHANNELS").takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("PURGE_CHANNELS must not be empty.")

        purgeChannelsStr.split(",").map { entry ->
            val (channelId, messageAgeHoursStr) = entry.trim().split('(').let {
                if (it.size != 2) throw IllegalArgumentException("Invalid PURGE_CHANNELS format.")
                it[0] to it[1].removeSuffix(")")
            }
            val messageAgeHours = messageAgeHoursStr.toIntOrNull()
                ?: throw IllegalArgumentException("Message age must be an integer.")
            if (messageAgeHours > 336) {
                throw IllegalArgumentException("Maximum: 336 hours (14 days) possible")
            }
            ChannelPurgeSetting(channelId, messageAgeHours)
        }
    }

    val INTERVAL_MINUTES: Int = run {
        val intervalStr = System.getenv("INTERVAL_MINUTES")?.takeIf { it.isNotBlank() }
            ?: properties.getProperty("INTERVAL_MINUTES").takeIf { it.isNotBlank() }
            ?: "60"

        val interval = intervalStr.toIntOrNull()
            ?: throw IllegalArgumentException("INTERVAL_MINUTES must be an integer.")
        interval
    }
}
