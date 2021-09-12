package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.outofmission.phase3.resupply.AARResupplyData;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.resupply.personnel.SquadronTransferData;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;
import pwcg.testutils.SquadronTestProfile;

public abstract class AARTestSetup
{
    @Mock protected Campaign campaign;
    @Mock protected CampaignData campaignData;
    @Mock protected CampaignPersonnelManager personnelManager;
    @Mock protected Squadron squadronEsc103;
    @Mock protected Squadron jasta11;
    @Mock protected AARContext aarContext;
    @Mock protected PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock protected PwcgMissionData pwcgMissionData;
    @Mock protected MissionHeader missionHeader;
    @Mock protected AARPreliminaryData preliminaryData;
    @Mock protected SquadronMembers campaignMembersInMission;
    @Mock protected ReconciledMissionVictoryData reconciledVictoryData;
    @Mock protected AARPersonnelLosses personnelLosses;
    @Mock protected AARPersonnelLosses personnelLossesCampaignUpdate;
    @Mock protected AARPersonnelAwards personnelAwards;
    @Mock protected AARPersonnelAcheivements personnelAcheivements;
    @Mock protected AARPersonnelAwards campaignMemberAwardsCampaignUpdate;
    @Mock protected AARResupplyData transferData;
    @Mock protected SquadronTransferData acesTransferred;
    @Mock protected SquadronTransferData squadronMembersTransferred;
    @Mock private CampaignUpdateData campaignUpdateData;
    @Mock protected SquadronMember player;
    @Mock protected SquadronMember enemyPilot1;
    @Mock protected SquadronMember pilot1;
    @Mock protected SquadronMember pilot2;
    @Mock protected SquadronMember pilot3;
    @Mock protected Ace ace1;
    @Mock protected Ace ace2;
    @Mock protected Ace ace3;
    @Mock protected Ace ace4;
    @Mock protected EquippedPlane enemyPlane1;
    @Mock protected EquippedPlane plane1;
    @Mock protected EquippedPlane plane2;
    @Mock protected EquippedPlane plane3;
    @Mock protected ArmedService frenchAirForce;
    @Mock protected ArmedService germanAirForce;
    
    protected List<SquadronMember> players = new ArrayList<>();

    protected void setupAARMocks() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        players = new ArrayList<>();
        players.add(player);
        
        mockCampaign();        
        mockAARContext();
        mockPreliminaryData();
        mockOutOfMissionData();
        mockCampaignUpdate();
        mockPersonnel();
    }

    private void mockCampaign() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170901"));
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaign.getReferencePlayer()).thenReturn(player);
        Mockito.when(squadronEsc103.determineDisplayName(Mockito.any())).thenReturn("Esc 103");
        Mockito.when(squadronEsc103.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(jasta11.determineDisplayName(Mockito.any())).thenReturn("Jasta 11");
        Mockito.when(jasta11.getSquadronId()).thenReturn(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        Mockito.when(campaignData.getName()).thenReturn("Player Name");

        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1)).thenReturn(pilot1);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2)).thenReturn(pilot2);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+3)).thenReturn(pilot3);
        
        Mockito.when(frenchAirForce.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.FRANCE));
        Mockito.when(germanAirForce.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.GERMANY));
        Mockito.when(frenchAirForce.getNameCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.FRANCE));
        Mockito.when(germanAirForce.getNameCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.GERMANY));
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
        Mockito.when(aarContext.getNewDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170901"));
        Mockito.when(aarContext.getPersonnelLosses()).thenReturn(personnelLosses);
        Mockito.when(aarContext.getPersonnelAwards()).thenReturn(personnelAwards);
        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(aarContext.getResupplyData()).thenReturn(transferData);
    }

    private void mockOutOfMissionData()
    {
        Mockito.when(transferData.getAcesTransferred()).thenReturn(acesTransferred);
        Mockito.when(transferData.getSquadronTransferData()).thenReturn(squadronMembersTransferred);
    }

    private void mockCampaignUpdate()
    {
        Mockito.when(campaignUpdateData.getPersonnelLosses()).thenReturn(personnelLossesCampaignUpdate);
        Mockito.when(campaignUpdateData.getResupplyData()).thenReturn(transferData);
        Mockito.when(campaignUpdateData.getPersonnelAwards()).thenReturn(campaignMemberAwardsCampaignUpdate);
    }

    private void mockPersonnel() throws PWCGException
    {
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(player.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(player.determineService(ArgumentMatchers.any())).thenReturn(frenchAirForce);
        Mockito.when(player.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(player.determineSquadron()).thenReturn(squadronEsc103);


        Mockito.when(enemyPilot1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+100);
        Mockito.when(pilot1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        Mockito.when(pilot2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        Mockito.when(pilot3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);

        Mockito.when(enemyPilot1.getSquadronId()).thenReturn(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        Mockito.when(pilot1.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(pilot2.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(pilot3.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        Mockito.when(enemyPilot1.getCountry()).thenReturn(Country.GERMANY);
        Mockito.when(pilot1.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(pilot2.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(pilot3.getCountry()).thenReturn(Country.FRANCE);
        
        Mockito.when(enemyPilot1.determineService(ArgumentMatchers.any())).thenReturn(germanAirForce);
        Mockito.when(pilot1.determineService(ArgumentMatchers.any())).thenReturn(frenchAirForce);
        Mockito.when(pilot2.determineService(ArgumentMatchers.any())).thenReturn(frenchAirForce);
        Mockito.when(pilot3.determineService(ArgumentMatchers.any())).thenReturn(frenchAirForce);

        
        Mockito.when(enemyPilot1.determineSquadron()).thenReturn(jasta11);
        Mockito.when(pilot1.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(pilot2.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(pilot3.determineSquadron()).thenReturn(squadronEsc103);
        
        Mockito.when(ace1.getSerialNumber()).thenReturn(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1);
        Mockito.when(ace2.getSerialNumber()).thenReturn(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2);
        Mockito.when(ace3.getSerialNumber()).thenReturn(SerialNumber.ACE_STARTING_SERIAL_NUMBER+3);
        Mockito.when(ace4.getSerialNumber()).thenReturn(SerialNumber.ACE_STARTING_SERIAL_NUMBER+4);

        Mockito.when(ace1.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(ace2.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(ace3.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(ace4.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        
        Mockito.when(ace1.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(ace2.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(ace3.determineSquadron()).thenReturn(squadronEsc103);
        Mockito.when(ace4.determineSquadron()).thenReturn(squadronEsc103);

        Mockito.when(ace1.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(ace2.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(ace3.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(ace4.getCountry()).thenReturn(Country.FRANCE);
        
        Mockito.when(enemyPlane1.getSquadronId()).thenReturn(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        Mockito.when(plane1.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(plane2.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Mockito.when(plane3.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        
        Mockito.when(enemyPlane1.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+100);
        Mockito.when(plane1.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        Mockito.when(plane2.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+1);
        Mockito.when(plane3.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+2);
        
        Mockito.when(enemyPlane1.determinePrimaryRole()).thenReturn(PwcgRole.ROLE_FIGHTER);
        Mockito.when(plane1.determinePrimaryRole()).thenReturn(PwcgRole.ROLE_FIGHTER);
        Mockito.when(plane2.determinePrimaryRole()).thenReturn(PwcgRole.ROLE_FIGHTER);
        Mockito.when(plane3.determinePrimaryRole()).thenReturn(PwcgRole.ROLE_FIGHTER);
    }

}
