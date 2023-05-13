package de.canard.autopurge

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import java.time.Duration
import java.time.OffsetDateTime

class MessageDeletionBot() {

    fun start() {
        println("Starting the bot...")

        val jda = JDABuilder.createDefault(Config.TOKEN).setStatus(OnlineStatus.INVISIBLE).build()
        jda.awaitReady()

        runBlocking {
            launch {
                while (jda.status == JDA.Status.CONNECTED) {
                    for ((channelId, maxMessageAge) in Config.PURGE_CHANNELS) {
                        val channel = jda.getTextChannelById(channelId) ?: continue
                        deleteOldMessages(channel, maxMessageAge)
                    }

                    printProgressBar()
                }
            }
        }

        println("Shutting down the bot...")
    }

    private suspend fun deleteOldMessages(channel: TextChannel, maxMessageAge: Int) {
        val now = OffsetDateTime.now()
        val messages = channel.iterableHistory.complete()

        val filteredMessages =
            messages.filter { Duration.between(it.timeCreated, now).toHours() >= maxMessageAge }
        val filteredMessageCount = filteredMessages.size
        var currentMessageCount = 0

        filteredMessages.forEach { message ->
            runBlocking {
                message.delete().queue()
                currentMessageCount++
                println("Deleted message: ${message.id} from channel: ${channel.id} | Count: $currentMessageCount/$filteredMessageCount")
                delay(1000L) // Discord Ratelimit protection
            }
        }
    }

    private suspend fun printProgressBar() {
        val checkIntervalMillis = Config.INTERVAL_MINUTES * 60 * 1000L
        val updateInterval = 1_000L
        val steps = (checkIntervalMillis / updateInterval).toInt()
        val progressBarLength = 50

        for (step in 0 until steps) {
            val progress = (step.toDouble() / steps.toDouble()) * progressBarLength
            val filled = "=".repeat(progress.toInt())
            val empty = " ".repeat(progressBarLength - progress.toInt())
            val remainingTimeMillis = checkIntervalMillis - step * updateInterval
            val remainingTimeSeconds = remainingTimeMillis / 1000
            val remainingMinutes = remainingTimeSeconds / 60
            val remainingSeconds = remainingTimeSeconds % 60
            print("\r[$filled>$empty] ${"%02d:%02d".format(remainingMinutes, remainingSeconds)}")
            delay(updateInterval)
        }
        println()
    }
}

