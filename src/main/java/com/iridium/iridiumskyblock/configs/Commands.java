package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumskyblock.commands.*;

/**
 * The command configuration of IridiumSkyblock (commands.yml).
 * Is deserialized automatically on plugin startup and reload.
 * <p>
 * <b>Commands that are added to this config will be registered automatically.</b>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commands {

    public AboutCommand aboutCommand = new AboutCommand();
    public BanCommand banCommand = new BanCommand();
    public BankCommand bankCommand = new BankCommand();
    public BiomeCommand biomeCommand = new BiomeCommand();
    public BlockValueCommand blockValueCommand = new BlockValueCommand();
    public BoosterCommand boostersCommand = new BoosterCommand();
    public BorderCommand borderCommand = new BorderCommand();
    public BypassCommand bypassCommand = new BypassCommand();
    public ChatCommand chatCommand = new ChatCommand();
    public ClearDataCommand clearDataCommand = new ClearDataCommand();
    public CreateCommand createCommand = new CreateCommand();
    public DeleteIslandCommand deleteIslandCommand = new DeleteIslandCommand();
    public DeleteIslandWarpCommand deleteIslandWarpCommand = new DeleteIslandWarpCommand();
    public DemoteCommand demoteCommand = new DemoteCommand();
    public DepositCommand depositCommand = new DepositCommand();
    public EditWarpCommand editWarpCommand = new EditWarpCommand();
    public ExpelCommand expelCommand = new ExpelCommand();
    public ExtraValueCommand extraValueCommand = new ExtraValueCommand();
    public FlyCommand flyCommand = new FlyCommand();
    public HelpCommand helpCommand = new HelpCommand();
    public HomeCommand homeCommand = new HomeCommand();
    public InfoCommand infoCommand = new InfoCommand();
    public InviteCommand inviteCommand = new InviteCommand();
    public JoinCommand joinCommand = new JoinCommand();
    public KickCommand kickCommand = new KickCommand();
    public LeaveCommand leaveCommand = new LeaveCommand();
    public LevelCommand levelCommand = new LevelCommand();
    public LogsCommand logsCommand = new LogsCommand();
    public MembersCommand membersCommand = new MembersCommand();
    public MissionCommand missionCommand = new MissionCommand();
    public PermissionsCommand permissionsCommand = new PermissionsCommand();
    public PrivateCommand privateCommand = new PrivateCommand();
    public PromoteCommand promoteCommand = new PromoteCommand();
    public PublicCommand publicCommand = new PublicCommand();
    public RecalculateCommand recalculateCommand = new RecalculateCommand();
    public RegenCommand regenCommand = new RegenCommand();
    public ReloadCommand reloadCommand = new ReloadCommand();
    public RenameCommand renameCommand = new RenameCommand();
    public RewardsCommand rewardsCommand = new RewardsCommand();
    public SetHomeCommand setHomeCommand = new SetHomeCommand();
    public SetWarpCommand setWarpCommand = new SetWarpCommand();
    public SettingsCommand settingsCommand = new SettingsCommand();
    public ShopCommand shopCommand = new ShopCommand();
    public SpyCommand spyCommand = new SpyCommand();
    public TopCommand topCommand = new TopCommand();
    public TransferCommand transferCommand = new TransferCommand();
    public TrustCommand trustCommand = new TrustCommand();
    public UnBanCommand unBanCommand = new UnBanCommand();
    public UnInviteCommand unInviteCommand = new UnInviteCommand();
    public UnTrustCommand unTrustCommand = new UnTrustCommand();
    public UpgradesCommand upgradesCommand = new UpgradesCommand();
    public ValueCommand valueCommand = new ValueCommand();
    public VisitCommand visitCommand = new VisitCommand();
    public WarpsCommand warpsCommand = new WarpsCommand();
    public WithdrawCommand withdrawCommand = new WithdrawCommand();

}
