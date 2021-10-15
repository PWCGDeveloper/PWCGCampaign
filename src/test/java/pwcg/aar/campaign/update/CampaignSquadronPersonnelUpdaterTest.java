package pwcg.aar.campaign.update;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.outofmission.phase4.ElapsedTIme.OutOfMissionCommandChangeHandler;
import pwcg.aar.tabulate.campaignupdate.AARCampaignUpdateTabulator;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronMemberPicker;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignSquadronPersonnelUpdaterTest
{
    private Map<Integer, SquadronMember> squadMembersKilled = new HashMap<>();
    private Map<Integer, SquadronMember> squadMembersCaptured = new HashMap<>();
    private Map<Integer, SquadronMember> squadMembersMaimed = new HashMap<>();
    private Map<Integer, SquadronMember> squadMembersWounded = new HashMap<>();
    private Map<Integer, SquadronMember> acesKilled = new HashMap<>();

    private Campaign campaign;
    private static AARContext aarContext;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);

        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        aarContext = new AARContext(campaign);
        aarContext.setNewDate(DateUtils.advanceTimeDays(campaign.getDate(), 3));
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        squadMembersKilled.clear();
        squadMembersMaimed.clear();
        squadMembersCaptured.clear();
        squadMembersWounded.clear();
        acesKilled.clear();
    }

    @AfterEach
    public void reset() throws PWCGException
    {
        SquadronPersonnel personnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        for (SquadronMember squadronMember : personnel.getSquadronMembersWithAces().getSquadronMemberList())
        {
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
        }
    }

    @Test
    public void testSquadronMemberKilled() throws PWCGException
    {
        SquadronMember deadSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        deadSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        squadMembersKilled.put(deadSquadronMember.getSerialNumber(), deadSquadronMember);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (SquadronMember squadronMember : squadMembersKilled.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelKilled(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(deadSquadronMember.getSerialNumber());
        Assertions.assertTrue(squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }

    @Test
    public void testSquadronMemberCaptured() throws PWCGException
    {
        SquadronMember capturedSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        capturedSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        squadMembersCaptured.put(capturedSquadronMember.getSerialNumber(), capturedSquadronMember);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (SquadronMember squadronMember : squadMembersCaptured.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelCaptured(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(capturedSquadronMember.getSerialNumber());
        Assertions.assertTrue(squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_CAPTURED);
    }

    @Test
    public void testSquadronMemberMaimed() throws PWCGException
    {
        SquadronMember maimedSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Date recoveryDate = DateUtils.advanceTimeDays(campaign.getDate(), 21);
        maimedSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), recoveryDate);
        squadMembersMaimed.put(maimedSquadronMember.getSerialNumber(), maimedSquadronMember);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (SquadronMember squadronMember : squadMembersMaimed.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelMaimed(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(maimedSquadronMember.getSerialNumber());
        assert (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
    }

    @Test
    public void testPlayerMemberMaimed() throws PWCGException
    {
        SquadronMember maimedPlayer = SquadronMemberPicker.pickPlayerSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        squadMembersMaimed.put(maimedPlayer.getSerialNumber(), maimedPlayer);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (SquadronMember squadronMember : squadMembersMaimed.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelMaimed(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(maimedPlayer.getSerialNumber());
        assert (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        assert (maimedPlayer.getRecoveryDate().after(campaign.getDate()));
    }

    @Test
    public void testPlayerBecomesCommander() throws PWCGException
    {
        SquadronMember commander = null;
        commander = getAiCommander();
        assert (commander != null);

        SquadronMember player = SquadronMemberPicker.pickPlayerSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        IRankHelper iRank = RankFactory.createRankHelper();
        String commandRank = iRank.getRankByService(0, player.determineService(campaign.getDate()));
        player.setRank(commandRank);

        OutOfMissionCommandChangeHandler commandChangeHandler = new OutOfMissionCommandChangeHandler(campaign);
        AARPersonnelLosses personnelLossesTransferHome = commandChangeHandler.replaceCommanderWithPlayer();

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (SquadronMember squadronMember : personnelLossesTransferHome.getPersonnelTransferredHome().values())
        {
            aarContext.getPersonnelLosses().addPersonnelTransferredHome(squadronMember);
            ;
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        SquadronMember commanderAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(commander.getSerialNumber());
        assert (commanderAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_TRANSFERRED);
        assert (commanderAfterUpdate.getInactiveDate().equals(campaign.getDate()));

        SquadronMember activeCommander = getAiCommander();
        assert (activeCommander == null);
    }

    private SquadronMember getAiCommander() throws PWCGException
    {
        SquadronMember commander = null;
        SquadronPersonnel squadronMembers = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        SquadronMembers activeSquadronMembers = SquadronMemberFilter
                .filterActiveAIAndPlayerAndAces(squadronMembers.getSquadronMembers().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : activeSquadronMembers.getSquadronMemberList())
        {
            if (squadronMember.determineIsSquadronMemberCommander() && !squadronMember.isPlayer())
            {
                commander = squadronMember;
            }
        }
        return commander;
    }

    @Test
    public void testWoundedPilotHealed() throws PWCGException
    {
        SquadronMember woundedSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        squadMembersWounded.put(woundedSquadronMember.getSerialNumber(), woundedSquadronMember);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (SquadronMember squadronMember : squadMembersWounded.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelWounded(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(woundedSquadronMember.getSerialNumber());
        assert (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_WOUNDED);
        assert (woundedSquadronMember.getRecoveryDate().after(campaign.getDate()));
    }

    @Test
    public void testSquadronMemberTransferred() throws PWCGException
    {
        ArmedService armedService = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.ESC_103_PROFILE.getSquadronId())
                .determineServiceForSquadron(campaign.getDate());
        PersonnelReplacementsService serviceReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(armedService.getServiceId());
        SquadronMember transferredSquadronMember = serviceReplacements.findReplacement();

        TransferRecord transferRecord = new TransferRecord(transferredSquadronMember, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(),
                SquadronTestProfile.ESC_3_PROFILE.getSquadronId());

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        campaignUpdateData.getResupplyData().getSquadronTransferData().addTransferRecord(transferRecord);

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(transferredSquadronMember.getSerialNumber());
        Assertions.assertTrue(squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE);

        Assertions.assertTrue(squadronMemberAfterUpdate.getSquadronId() != SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
    }

    @Test
    public void testSquadronAceKilled() throws PWCGException
    {
        Ace guynemerInCampaign = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101064);
        acesKilled.put(101064, guynemerInCampaign);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (SquadronMember squadronMember : acesKilled.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelKilled(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        Ace aceAfterUpdate = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101064);
        Assertions.assertTrue(aceAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }

    @Test
    public void testNonSquadronAceKilled() throws PWCGException
    {
        Ace vossInCampaign = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101175);
        acesKilled.put(101175, vossInCampaign);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (SquadronMember squadronMember : acesKilled.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelKilled(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        Ace aceAfterUpdate = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101175);
        Assertions.assertTrue(aceAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }

}
