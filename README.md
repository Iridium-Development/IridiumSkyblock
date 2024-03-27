# IridiumSkyblock
![GitHub](https://img.shields.io/github/license/Iridium-Development/IridiumSkyblock?color=479fc0)

![Spiget Rating](https://img.shields.io/spiget/rating/62480?color=479fc0&style=for-the-badge)
![Spiget Downloads](https://img.shields.io/spiget/downloads/62480?color=479fc0&style=for-the-badge)
![Spiget tested server versions](https://img.shields.io/spiget/tested-versions/62480?color=479fc0&style=for-the-badge)

## Introduction

Skyblock: **Redefined**

IridiumSkyblock is a Skyblock plugin designed from the ground up for speed, performance, and excellence. It features an advanced missions system, a shop, island upgrades and boosters, teamwork and cooperation features, and so much more.

This plugin, as with all of Iridium Development's software, aims to deliver a premium image, streamlining the setup process and eliminating the headache of having to figure out the details, while also proving to be extremely configurable to create an unparalleled experience. 

Every aspect of the plugin has been carefully crafted to suit the server's needs, and between the extensive GUI customization and plethora of extra options, server owners can rest easy knowing that there's a Skyblock plugin out there that fits their needs. It's this one, if you were wondering.

## Getting Started

Download the plugin from [Spigot](https://www.spigotmc.org/resources/iridium-skyblock-1-13-1-19.62480/), [Modrinth](https://modrinth.com/plugin/iridiumskyblock), [Hangar](https://hangar.papermc.io/IridiumDevelopment/IridiumSkyblock), [Github Releases](https://github.com/Iridium-Development/IridiumSkyblock/releases), or compile it yourself.

Once you have a copy of the plugin (it should be a ``.jar`` file), simply place it in the ``server/plugins`` folder.

After the plugin is set up and running, check out our [**wiki**](https://iridium-development.gitbook.io/iridiumskyblock/) for documentation.

### Requirements
- [Vault](https://www.spigotmc.org/resources/vault.34315/) - Required Dependency

### Recomendations
- [World Edit](https://enginehub.org/worldedit)
- [Multiverse](https://dev.bukkit.org/projects/multiverse-core)
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- [DecentHolograms](https://www.spigotmc.org/resources/decentholograms-1-8-1-19-4-papi-support-no-dependencies.96927/)
- A spawn plugin 
- An economy plugin (features will be limited without one)
- [EssentialsX](https://essentialsx.net/)
- [RoseStacker](https://www.spigotmc.org/resources/rosestacker.82729/)

### Compiling

Clone the repo, and run the [build.gradle.kts](https://github.com/Iridium-Development/IridiumSkyblock/blob/master/build.gradle.kts) script.

If you need to take a look, here is the [Nexus](https://nexus.iridiumdevelopment.net/#browse/browse:maven-public:com%2Firidium%2FIridiumSkyblock) repo.

## Development

You may notice when compiling and developing against IridiumSkyblock that there is a significant portion of code that isn't located in this repo. That's because IridiumSkyblock is an extension of IridiumTeams, and also uses functions from IridiumCore.

- [IridiumCore](https://github.com/Iridium-Development/IridiumCore)
  - A sort of library for all of Iridium Development's plugins
- [IridiumTeams](https://github.com/Iridium-Development/IridiumTeams)
  - The generic plugin, which extends IridiumCore, and involves all of the code for team management, including leveling, missions, team members, the bank, etc.
- [IridiumSkyblock](https://github.com/Iridium-Development/IridiumSkyblock)
  - This plugin, which extends IridiumTeams, and houses its own code specific to Skyblock, such as the world generation.

When developing with IridiumSkyblock, you may have to reference all three repos for your purposes.

## Support

If you think you've found a bug, please make sure you isolate the issue down to IridiumSkyblock and its dependencies before posting an issue in our [Issues](https://github.com/Iridium-Development/IridiumSkyblock/issues) tab. While you're there, please follow our issues guidelines.

If you encounter any issues while using the plugin, feel free to join our support [Discord](https://discord.gg/6HJ73mWE7P).
