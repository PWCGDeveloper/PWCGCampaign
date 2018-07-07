package pwcg.aar;

import org.mockito.Mock;
import org.mockito.Mockito;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.inmission.phase3.reconcile.ReconciledInMissionData;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.outofmission.phase1.elapsedtime.ReconciledOutOfMissionData;
import pwcg.aar.outofmission.phase2.resupply.AARResupplyData;
import pwcg.aar.outofmission.phase2.resupply.SquadronTransferData;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;

public abstract class AARTestSetup
{
    @Mock protected Campaign campaign;
    @Mock protected CampaignPersonnelManager personnelManager;
    @Mock protected Squadron squadronEsc103;
    @Mock protected AARContext aarContext;
    @Mock protected PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock protected PwcgMissionData pwcgMissionData;
    @Mock protected MissionHeader missionHeader;
    @Mock protected AARPreliminaryData preliminaryData;
    @Mock protected SquadronMembers campaignMembersInMission;
    @Mock protected ReconciledInMissionData reconciledInMissionData;
    @Mock protected ReconciledVictoryData reconciledVictoryData;
    @Mock protected ReconciledOutOfMissionData reconciledOutOfMissionData;
    @Mock protected AARPersonnelLosses personnelLossesOutOfMissionData;
    @Mock protected AARPersonnelLosses personnelLossesInMissionData;
    @Mock protected AARPersonnelLosses personnelLossesCampaignUpdate;
    @Mock protected AARPersonnelAwards campaignMemberAwards;
    @Mock protected AARResupplyData transferData;
    @Mock protected SquadronTransferData acesTransferred;
    @Mock protected SquadronTransferData squadronMembersTransferred;
    @Mock private CampaignUpdateData campaignUpdateData;
    @Mock protected SquadronMember player;
    @Mock protected SquadronMember pilot1;
    @Mock protected SquadronMember pilot2;
    @Mock protected SquadronMember pilot3;
    @Mock protected Ace ace1;
    @Mock protected Ace ace2;
    @Mock protected Ace ace3;
    @Mock protected Ace ace4;
    @Mock protected EquippedPlane plane1;
    @Mock protected EquippedPlane plane2;
    @Mock protected EquippedPlane plane3;

    protected void setupAARMocks() throws PWCGException
    {
        PWCGContextManager.setRoF(true);

        mockCampaign();        
        mockAARContext();
        mockPreliminaryData();
        mockInMissionData();        
        mockOutOfMissionData();
        mockCampaignUpdate();
        mockPersonnel();
    }

    private void mockCampaign() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170701"));
        Mockito.when(campaign.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(campaign.getSquadronId()).thenReturn(101103);
        Mockito.when(campaign.getPlayer()).thenReturn(player);
        Mockito.when(campaign.getName()).thenReturn("Player Name");
        Mockito.when(squadronEsc103.determineDisplayName(Mockito.any())).thenReturn("Esc 103");
        Mockito.when(squadronEsc103.getSquadronId()).thenReturn(101103);

        ICountry country = CountryFactory.makeCountryByCountry(Country.FRANCE);
        Mockito.when(campaign.determineCountry()).thenReturn(country);

        Mockito.when(campaign.getSquadronId()).thenReturn(101103);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1)).thenReturn(pilot1);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2)).thenReturn(pilot2);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+3)).thenReturn(pilot3);
    }

    private void mockPreliminaryData()
    {
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("MissionFileName");
        Mockito.when(preliminaryData.getPwcgMissionData()).thenReturn(pwcgMissionData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
    }

    private void mockAARContext() throws PWCGException
    {
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(aarContext.getNewDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170704"));
        Mockito.when(aarContext.getCampaignUpdateData()).thenReturn(campaignUpdateData);
        Mockito.when(aarContext.getReconciledInMissionData()).thenReturn(reconciledInMissionData);
        Mockito.when(aarContext.getReconciledOutOfMissionData()).thenReturn(reconciledOutOfMissionData);
    }

    private void mockInMissionData()
    {
        Mockito.when(reconciledInMissionData.getPersonnelLossesInMission()).thenReturn(personnelLossesInMissionData);
    }

    private void mockOutOfMissionData()
    {
        Mockito.when(reconciledOutOfMissionData.getPersonnelLossesOutOfMission()).thenReturn(personnelLossesOutOfMissionData);
        Mockito.when(reconciledOutOfMissionData.getPersonnelAwards()).thenReturn(campaignMemberAwards);
        Mockito.when(reconciledOutOfMissionData.getResupplyData()).thenReturn(transferData);
        
        Mockito.when(transferData.getAcesTransferred()).thenReturn(acesTransferred);
        Mockito.when(transferData.getSquadronTransferData()).thenReturn(squadronMembersTransferred);
    }

    private void mockCampaignUpdate()
    {
        Mockito.when(campaignUpdateData.getPersonnelLosses()).thenReturn(personnelLossesCampaignUpdate);
        Mockito.when(campaignUpdateData.getResupplyData()).thenReturn(transferData);
        Mockito.when(campaignUpdateData.getPersonnelAwards()).thenReturn(campaignMemberAwards);
    }

    private void mockPersonnel() throws PWCGException
    {
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_SERIAL_NUMBER);
        Mockito.when(player.getSquadronId()).thenReturn(101103);
        Mockito.when(player.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(player.determineSquadron()).thenReturn(squadronEsc103);


        Mockito.when(pilot1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        Mockito.when(pilot2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        Mockito.when(pilot3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);

        Mockito.when(pilot1.getSquadronId()).thenReturn(101103);
        Mockito.when(pilot2.getSquadronId()).thenReturn(101103);
        Mockito.when(pilot3.getSquadronId()).thenReturn(101103);

        Mockito.when(pilot1.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(pilot2.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(pilot3.getCountry()).thenReturn(Country.FRANCE);
        
        Mockito.when(pilot1.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(pilot2.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(pilot3.determineSquadron()).thenReturn(squadronEsc103);
        
        Mockito.when(ace1.getSerialNumber()).thenReturn(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1);
        Mockito.when(ace2.getSerialNumber()).thenReturn(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2);
        Mockito.when(ace3.getSerialNumber()).thenReturn(SerialNumber.ACE_STARTING_SERIAL_NUMBER+3);
        Mockito.when(ace4.getSerialNumber()).thenReturn(SerialNumber.ACE_STARTING_SERIAL_NUMBER+4);

        Mockito.when(ace1.getSquadronId()).thenReturn(101103);
        Mockito.when(ace2.getSquadronId()).thenReturn(101103);
        Mockito.when(ace3.getSquadronId()).thenReturn(101103);
        Mockito.when(ace4.getSquadronId()).thenReturn(101103);

        Mockito.when(ace1.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(ace2.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(ace3.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(ace4.determineSquadron()).thenReturn(squadronEsc103);

        Mockito.when(ace1.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(ace2.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(ace3.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(ace4.getCountry()).thenReturn(Country.FRANCE);
        
        Mockito.when(plane1.getSquadronId()).thenReturn(101103);
        Mockito.when(plane2.getSquadronId()).thenReturn(101103);
        Mockito.when(plane3.getSquadronId()).thenReturn(101103);
    }

}
