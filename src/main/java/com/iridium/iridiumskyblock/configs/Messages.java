package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

/**
 * The message configuration used by IridiumSkyblock (messages.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Messages {

    public String reloaded = "%prefix% &7Configuration has been reloaded.";
    public String noPermission = "%prefix% &7You don't have permission for that.";
    public String mustBeAPlayer = "%prefix% &7You must be a player to execute this command.";
    public String notAPlayer = "%prefix% &7That player doesn't exist.";
    public String unknownCommand = "%prefix% &7Unknown Command, Try /is help.";
    public String helpCommandHeader = "&8===== &b&lIridiumSkyblock Help &r&8=====";
    public String helpCommandMessage = "<GRADIENT:09C6F9>/is %command%</GRADIENT:045DE9>&r: &7%description%";
    public String helpCommandFooter = " &7Page %page% of %max_page% ";
    public String helpCommandPreviousPage = "&b<<";
    public String helpCommandNextPage = "&b>>";
    public String helpCommandNextPageHover = "&7Click to go to the next page.";
    public String helpCommandPreviousPageHover = "&7Click to go to the previous page.";
    public String creatingIsland = "%prefix% &7Creating Island...";
    public String regeneratingIsland = "%prefix% &7Regenerating Island...";
    public String alreadyHaveIsland = "%prefix% &7You already have an Island.";
    public String noIsland = "%prefix% &7You don't have an Island.";
    public String noIslandFound = "%prefix% &7No island found for that player.";
    public String islandWithNameAlreadyExists = "%prefix% &7An Island with that name already exists.";
    public String islandSchematicNotFound = "%prefix% &7No schematic with that name exists.";
    public String islandDeleted = "%prefix% &7Your Island has been deleted.";
    public String otherIslandDeleted = "%prefix% &7The Island has been deleted.";
    public String teleportingHome = "%prefix% &7Teleporting to your Island home.";
    public String teleportingHomeOther = "%prefix% &7Teleporting to %owner%'s Island home.";
    public String setHome = "%prefix% &7Your Island home has been set to this location.";
    public String notSafe = "%prefix% &7This location is not safe.";
    public String invitedPlayer = "%prefix% &7You have invited %player% to join your Island.";
    public String userInvitedPlayer = "%prefix% &7%inviter% has invited %player% to join your Island.";
    public String youHaveBeenInvited = "%prefix% &7%inviter% has invited you to join their Island. Click here to join the Island!";
    public String alreadyInYourIsland = "%prefix% &7This player is already a member of your Island.";
    public String inviteRevoked = "%prefix% &7Island invite for %player% has been revoked.";
    public String noActiveInvite = "%prefix% &7%player% has no active invite for your Island.";
    public String islandMemberLimitReached = "%prefix% &7The Island max member count has been reached.";
    public String alreadyInvited = "%prefix% &7You have already invited this user to your Island.";
    public String playerJoinedYourIsland = "%prefix% &7%player% has joined your Island!";
    public String noInvite = "%prefix% &7You have not been invited to this Island.";
    public String userNoIsland = "%prefix% &7That user doesn't have an Island.";
    public String cannotLeaveIsland = "%prefix% &7You cannot leave your Island, do /is delete instead.";
    public String youHaveLeftIsland = "%prefix% &7You have left your Island.";
    public String playerLeftIsland = "%prefix% &7%player% has left your Island.";
    public String userNotInYourIsland = "%prefix% &7That user is not in your Island.";
    public String youHaveBeenKicked = "%prefix% &7You have been kicked by %player%.";
    public String youKickedPlayer = "%prefix% &7You have kicked %player%.";
    public String kickedPlayer = "%prefix% &7%kicker% has kicked %player%.";
    public String islandIsPrivate = "%prefix% &7That player's Island is private.";
    public String islandNowPrivate = "%prefix% &7Your island is now private and %amount% players have been expelled from it.";
    public String islandNowPublic = "%prefix% &7Your Island is now public.";
    public String youHaveBeenBanned = "%prefix% &7You have been banned from visiting this island.";
    public String youHaveBeenUnBanned = "%prefix% &7You have been unbanned from visiting %player%'s Island.";
    public String playerBanned = "%prefix% &7You have banned %player% from visiting your Island.";
    public String alreadyBanned = "%prefix% &7This player is already banned from your Island.";
    public String notBanned = "%prefix% &7That player is not banned from your Island.";
    public String cannotBanned = "%prefix% &7That player cannot be banned from visiting your Island!";
    public String playerUnBanned = "%prefix% &7You have un-banned %player% from visiting your Island.";
    public String userNotVisitingYourIsland = "%prefix% &7That user is not visiting your Island.";
    public String expelledIslandLocked = "%prefix% &7%player%'s Island is now private.";
    public String youHaveBeenExpelled = "%prefix% &7You were expelled from %player%'s Island!";
    public String expelledVisitor = "%prefix% &7%player% has been expelled from your Island.";
    public String cannotExpelPlayer = "%prefix% &7%player% cannot be expelled!";
    public String inYourTeam = "%prefix% &7%player% is a member of your Island.";
    public String cannotKickUser = "%prefix% &7You cannot kick this user.";
    public String cannotDeleteIsland = "%prefix% &7Only the Island owner can delete an Island.";
    public String cannotPromoteUser = "%prefix% &7You cannot promote this user.";
    public String promotedPlayer = "%prefix% &7You have promoted %player% to %rank%.";
    public String userPromotedPlayer = "%prefix% &7%promoter% has promoted %player% to %rank%.";
    public String islandMemberChat = "%prefix% &7%player% &8» &f%message%";
    public String cannotDemoteUser = "%prefix% &7You cannot demote this user.";
    public String demotedPlayer = "%prefix% &7You have demoted %player% to %rank%.";
    public String userDemotedPlayer = "%prefix% &7%promoter% has demoted %player% to %rank%.";
    public String transferredOwnership = "%prefix% &7%oldowner% has transferred Island ownership to %newowner%.";
    public String islandValue = "%prefix% &b&l * &7%rank%: &b%value%";
    public String cannotChangeName = "%prefix% &7You cannot change the Island name!";
    public String islandNameTooLong = "%prefix% %name% is too long for the Island name, the maximum length is %max_length%";
    public String islandNameTooShort = "%prefix% %name% is too short for the Island name, the minimum length is %min_length%";
    public String islandNameChanged = "%prefix% &7%player% has changed the Island name to %name%";
    public String cannotTransferYourself = "%prefix% &7You cannot transfer the Island ownership to yourself.";
    public String cannotInteract = "%prefix% &7You cannot interact with this Island.";
    public String cannotTrampleCrops = "%prefix% &7You cannot trample crops on this Island.";
    public String cannotDestroyVehicles = "%prefix% &7You cannot destroy vehicles on this Island.";
    public String cannotBreakBlocks = "%prefix% &7You cannot break blocks on this Island.";
    public String cannotPlaceBlocks = "%prefix% &7You cannot place blocks on this Island.";
    public String cannotUseBuckets = "%prefix% &7You cannot use buckets on this Island.";
    public String cannotMineSpawners = "%prefix% &7You cannot place blocks on this Island.";
    public String cannotOpenDoors = "%prefix% &7You cannot open doors on this Island.";
    public String cannotOpenContainers = "%prefix% &7You cannot open containers on this Island.";
    public String cannotChangePermissions = "%prefix% &7You cannot change this permission.";
    public String cannotUseRedstone = "%prefix% &7You cannot use redstone on this Island.";
    public String cannotHurtPlayer = "%prefix% &7PvP is disabled on this Island.";
    public String cannotHurtMember = "%prefix% &7You cannot hurt members of your Island.";
    public String cannotHurtMobs = "%prefix% &7You cannot hurt mobs on this Island.";
    public String cannotInviteMember = "%prefix% &7You cannot invite members to this Island.";
    public String cannotRegenIsland = "%prefix% &7You cannot regenerate your Island.";
    public String cannotInteractEntities = "%prefix% &7You cannot interact with entities on this Island.";
    public String cannotDropItems = "%prefix% &7You cannot drop items on this Island.";
    public String cannotTransferOwnership = "%prefix% &7Only the Island owner can transfer ownership.";
    public String cannotSpawnEntities = "%prefix% &7You cannot spawn entities on this Island.";
    public String cannotUsePortal = "%prefix% &7You cannot use a portal on this Island.";
    public String cannotChangeSettings = "%prefix% &7You cannot change the Island Settings.";
    public String nowBypassing = "%prefix% &7You are now bypassing Island restrictions.";
    public String noLongerBypassing = "%prefix% &7You are no longer bypassing Island restrictions.";
    public String noSuchBankItem = "%prefix% &7That bank item doesn't exist.";
    public String islandBorderChanged = "%prefix% &7%player% has changed your Island border to %color%.";
    public String notAColor = "%prefix% &7That is not a valid color.";
    public String notANumber = "%prefix% &7That is not a valid number.";
    public String gaveBank = "%prefix% &7You gave %player% %amount% %item%.";
    public String setBank = "%prefix% &7You set %player%'s %amount% to %item%.";
    public String removedBank = "%prefix% &7You took %amount% %item%'s from %player%.";
    public String extraValueInfo = "%prefix% &7The extra value of %player% is %amount%.";
    public String extraValueSet = "%prefix% &7You have set %player%'s island extra value to %amount%.";
    public String invalidMissionType = "%prefix% &7That is not a valid mission type.";
    public String maxLevelReached = "%prefix% &7Maximum level reached.";
    public String cannotAfford = "%prefix% &7You cannot afford this.";
    public String unknownUpgrade = "%prefix% &7Unknown Island upgrade.";
    public String unknownBooster = "%prefix% &7Unknown Island booster.";
    public String successfullyBoughtBooster = "%prefix% &7You have successfully bought the %booster% Booster &r&7for $%vault_cost% and %crystal_cost% Crystals.";
    public String successfullyBoughtUpgrade = "%prefix% &7You have successfully bought the %upgrade% Upgrade &r&7for $%vault_cost% and %crystal_cost% Crystals.";
    public String unknownWarp = "%prefix% &7Unknown Island warp.";
    public String alreadyTrusted = "%prefix% &7This player is already trusted.";
    public String trustedPlayer = "%prefix% &7%truster% has trusted %player% to your Island.";
    public String playerTrustedYou = "%prefix% &7%truster% has trusted you to their Island.";
    public String trustRevoked = "%prefix% &7Island Trust for %player% has been revoked.";
    public String notTrusted = "%prefix% &7%player% is not trusted in your Island.";
    public String createdWarp = "%prefix% &7Created warp %name%.";
    public String warpAlreadyExists = "%prefix% &7A warp with that name already exists.";
    public String warpIconSet = "%prefix% &7warp icon set.";
    public String warpDescriptionSet = "%prefix% &7warp description set.";
    public String noSuchMaterial = "%prefix% &7Material doesn't exist.";
    public String warpLimitReached = "%prefix% &7The max warp limit has already been reached.";
    public String incorrectPassword = "%prefix% &7Incorrect Password.";
    public String teleportingWarp = "%prefix% &7Teleporting to warp %name%.";
    public String deletingWarp = "%prefix% &7Deleting warp %name%.";
    public String notOnAnIsland = "%prefix% &7You are not on an Island.";
    public String flightBoosterNotActive = "%prefix% &7The flight booster is not currently active.";
    public String flightEnabled = "%prefix% &7Island flight enabled.";
    public String flightDisabled = "%prefix% &7Island flight disabled.";
    public String bankWithdrew = "%prefix% &7You successfully withdrew %amount% %type% from your Island bank.";
    public String bankDeposited = "%prefix% &7You successfully deposited %amount% %type% to your Island bank.";
    public String insufficientFundsToWithdrew = "%prefix% &7You do not have enough %type% to withdraw from your Island bank.";
    public String insufficientFundsToDeposit = "%prefix% &7You do not have enough %type% to deposit into your Island bank.";
    public String blockLimitReached = "%prefix% &7The maximum block limit for %block% (%limit%) has been reached!";
    public String cannotManageWarps = "%prefix% &7You cannot manage warps on this Island.";
    public String cannotWithdraw = "%prefix% &7You cannot withdraw from the Island bank.";
    public String cannotManageTrusts = "%prefix% &7You cannot manage Island Trusts.";
    public String cannotManageBorder = "%prefix% &7You cannot change the Island Border.";
    public String teleportCanceled = "%prefix% &7Teleportation Canceled, Please stand still.";
    public String unknownSchematic = "%prefix% &7Unknown Schematic.";
    public String onlySetWarpOnIsland = "%prefix% &7You can only create an Island warp on your Island.";
    public String onlySetHomeOnIsland = "%prefix% &7You can only set your Island home on your Island.";
    public String islandLevel = "%prefix% &7Island Level %island_level% %island_experience%/%island_experienceRequired% Experience";
    public String dataReset = "%prefix% &7All data has been reset.";
    public String noBiomeCategory = "%prefix% &7There is no category with this name.";
    public String noShopCategory = "%prefix% &7There is no category with this name.";
    public String inventoryFull = "%prefix% &7Your inventory is full!";
    public String noSuchItem = "%prefix% &7You don't have this item!";
    public String successfullyBought = "%prefix% &7You have successfully bought %amount%x %item% &r&7for $%vault_cost% and %crystal_cost% Crystals.";
    public String successfullyBoughtBiome = "%prefix% &7You have successfully bought %item% &r&7for $%vault_cost% and %crystal_cost% Crystals.";
    public String successfullySold = "%prefix% &7You have successfully sold %amount%x %item% &r&7for $%vault_reward% and %crystal_reward% Crystals.";
    public String calculatingIslands = "%prefix% &7Calculating %amount% Players Islands, Estimated time: %minutes% Minutes and %seconds% Seconds";
    public String calculatingFinished = "%prefix% &7Calculating Players Islands Finished";
    public String calculationAlreadyInProcess = "%prefix% &7Calculating Players Islands already in Process";
    public String netherIslandsDisabled = "%prefix% &7Nether Islands have been disabled";
    public String endIslandsDisabled = "%prefix% &7End Islands have been disabled";
    public String islandLevelUp = "%prefix% &7Your Island has reached level %level%!";
    public String islandChatEnabled = "%prefix% &7Island Chat Enabled!";
    public String islandChatDisabled = "%prefix% &7Island Chat Disabled!";
    public String islandChatSpyEnabled = "%prefix% &7You can now see the Island chats of other Islands!";
    public String islandChatSpyDisabled = "%prefix% &7You no longer see the Island chats of other Islands!";
    public String islandChatSpyMessage = "%prefix% &7[%island%] %player% &8» &f%message%";
    public String invalidBiome = "%prefix% &7That Biome doesn't exist.";
    public String changedBiome = "%prefix% &7You have changed your island biome to %biome%!";
    public String borderColorDisabled = "%prefix% &7That border color has been disabled.";
    public String activeCooldown = "%prefix% &7You cannot do that due to a running cooldown, please wait %hours% hour(s), %minutes% minute(s), %seconds% second(s)!";
    public String clickToJoinHover = "&7Click to join this Island!";
    public String islandCenterOffset = "%prefix% &7Your position relative to the Island center is: x=%x%, y=%y%, z=%z%, yaw=%yaw%";
    public String yes = "Yes";
    public String no = "No";
    public String none = "None";
    public String visitable = "Visitable";
    public String notVisitable = "Private";
    public String ownerRankDisplayName = "Owner";
    public String coOwnerRankDisplayName = "Co-Owner";
    public String moderatorRankDisplayName = "Moderator";
    public String memberRankDisplayName = "Member";
    public String visitorRankDisplayName = "Visitor";

    public String infoTitle = "&8[ &b&lIsland info for %island_name% &8]";
    public String infoFiller = "&8&m ";

    public List<String> infoCommand = Arrays.asList(
            "<GRADIENT:09C6F9>Island Name</GRADIENT:045DE9>&r: &7%island_name%",
            "<GRADIENT:09C6F9>Island Owner</GRADIENT:045DE9>&r: &7%owner%",
            "<GRADIENT:09C6F9>Island Members</GRADIENT:045DE9>&r: &7%members%",
            "<GRADIENT:09C6F9>Island Level</GRADIENT:045DE9>&r: &7%level%",
            "<GRADIENT:09C6F9>Island Value</GRADIENT:045DE9>&r: &7%value%",
            "<GRADIENT:09C6F9>Island Visitable</GRADIENT:045DE9>&r: &7%visitable%"
    );

    public List<String> commandHelpMessage = Arrays.asList(
            "&7Description: &b%description%",
            "&7Syntax: &b%syntax% %subcommands%");

}
