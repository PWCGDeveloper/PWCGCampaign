package pwcg.aar;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARExtendedTimeHandlerTest
{
    private Campaign campaign;
    private AARContext aarContext;
    
    @Mock LogPilot playerLogPilot;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        campaign.getCampaignData().setCampaignMode(CampaignMode.CAMPAIGN_MODE_SINGLE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        aarContext = new AARContext(campaign);
    }

    @Test
    public void testPersonnelReplacedWhenTimePassed () throws PWCGException
    {
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            if (!squadronMember.isPlayer())
            {
                squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
            }
            SquadronMembers squadronMembersLeft = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(
                            campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
            if (squadronMembersLeft.getSquadronMemberCollection().size() < 3)
            {
                break;
            }
        }
        
        SquadronMembers squadronMembersLeft = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        
        int numLeaves = 0;
        int numSquadronMembers = squadronMembersLeft.getSquadronMemberCollection().size();
        while (numSquadronMembers < 9)
        {
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
            extendedTimeHandler.timePassedForLeave(21);
            if (numLeaves == 10)
            {
                break;
            }
            else
            {
                ++numLeaves;
                squadronMembersLeft = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(
                                campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
                numSquadronMembers = squadronMembersLeft.getSquadronMemberCollection().size();
            }
        }
        
        Assertions.assertTrue (numLeaves < 10);
        SquadronMembers squadronMembersAfter = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        assert(squadronMembersAfter.getSquadronMemberCollection().size() >= 9);
    }

    
    @Test
    public void testEquipmentReplacedWhenTimePassed () throws PWCGException
    {
        Map<Integer, EquippedPlane> activePlanes = campaign.getEquipmentManager().getEquipmentForSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getActiveEquippedPlanes();
        for (EquippedPlane equippedPlane : activePlanes.values())
        {
            equippedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
            Map<Integer, EquippedPlane> activePlanesLeft = campaign.getEquipmentManager().getEquipmentForSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getActiveEquippedPlanes();
            if (activePlanesLeft.size() < 3)
            {
                break;
            }
        }
        
        Map<Integer, EquippedPlane> activePlanesLeft = campaign.getEquipmentManager().getEquipmentForSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getActiveEquippedPlanes();
        int numLeaves = 0;
        int numPlanes = activePlanesLeft.size();
        while (numPlanes < 9)
        {
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
            extendedTimeHandler.timePassedForLeave(21);
            if (numLeaves == 10)
            {
                break;
            }
            else
            {
                ++numLeaves;
                activePlanesLeft = campaign.getEquipmentManager().getEquipmentForSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getActiveEquippedPlanes();
                numPlanes = activePlanesLeft.size();
            }
        }
        
        Assertions.assertTrue (numLeaves < 10);
        SquadronMembers squadronMembersAfter = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        assert(squadronMembersAfter.getSquadronMemberCollection().size() >= 9);
    }

    @Test
    public void testTimePassedForLeave () throws PWCGException
    {
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForLeave(21);
        Date endCampaignDate = campaign.getDate();
        
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed > 19);
        assert(daysPassed < 23);
    }
    
    @Test
    public void testTimePassedForTransfer () throws PWCGException
    {
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForTransfer(10);
        Date endCampaignDate = campaign.getDate();
        
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed >= 9);
        assert(daysPassed <= 13);
    }
}
