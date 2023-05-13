# AutoPurge 🧹

AutoPurge is a Discord bot 🤖 designed to automatically delete messages in one or multiple text channels after a predefined time ⏳. This helps in maintaining cleanliness and organization within your Discord server, as well as ensuring unwanted or outdated messages are removed automatically.

## Features 🌟

- Automatic deletion of messages in selected text channels 📜
- Configurable time intervals for message deletion per channel ⏲️
- Configurable interval for checking messages ⏲️
- Easy setup using environment variables or a configuration file 🛠️
- Progress bar displaying the time remaining until the next message check 🔄⏳

## Requirements 📋

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher

## Installation 📥

1. Clone this repository or download it as a zip file and extract it to your desired location 📂
2. Open a terminal/command prompt and navigate to the repository's root directory 🖥️
3. If necessary, grant execute permissions for the `gradlew` script by running the following command:

```
chmod +x gradlew
```

4. Run the following command to compile the Kotlin project and generate a JAR file:

```
./gradlew build
```

5. Once the build is complete, the generated JAR file can be found in `build/libs` directory 📦

## Configuration 🔧

For optimal security, it is recommended to use environment variables for configuration. However, you can also use a `config.properties` file if you prefer.

### Environment Variables (Recommended)

Provide the following environment variables for the bot to function properly:

- `BOT_TOKEN`: The Discord bot token 🎫 (Obtain this from the [Discord Developer Portal](https://discord.com/developers/applications))
- `PURGE_CHANNELS`: A comma-separated list of Text Channel IDs and their corresponding maximum message age (in hours) that the bot will monitor for message deletion 📝 (For example, `1234567890(12),0987654321(24)` for two channels with different maximum message ages)
- `INTERVAL_MINUTES`: (Optional) The interval (in minutes) between each message check ⏲️ (For example, `60` for checking messages every hour). Default value is 60 minutes.

### Configuration File

1. Locate the `config.properties.example` file in the root directory of the project.
2. Edit the `config.properties.example` file and fill in the required information:
3. Save the edited file and rename it to `config.properties`.
4. Move the `config.properties` file to the `build/libs/` directory.

## Running the Bot 🚀

To start the bot, use the following command:

```
java -jar build/libs/AutoPurge-<version>.jar
```

Replace `<version>` with the actual version number of your JAR file.

The bot should now be running and will automatically delete messages in the specified text channels after the configured time interval, while displaying a progress bar for the time remaining until the next check 🎉
