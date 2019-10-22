package pwcg.campaign.personnel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class InitialSquadronStafferTest
{
    @Before
    public void setup() throws PWCGException
    {
    }
    
    @Test
    public void generateFighterPersonnelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
        
        InitialSquadronStaffer initialSquadronStaffer = new InitialSquadronStaffer(campaign, squadron);
        SquadronPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        

        assert(squadronMembers.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }
    
    @Test
    public void generateReconPersonnelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_2_PROFILE);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.RFC_2_PROFILE.getSquadronId());
        
        InitialSquadronStaffer initialSquadronStaffer = new InitialSquadronStaffer(campaign, squadron);
        SquadronPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        
        assert(squadronMembers.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }
    
    @Test
    public void generatePersonnelWithAcesTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        
        InitialSquadronStaffer initialSquadronStaffer = new InitialSquadronStaffer(campaign, squadron);
        SquadronPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        SquadronMembers squadronMembers = squadronPersonnel.getSquadronMembersWithAces();
        SquadronMembers filteredSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronMembers.getSquadronMemberCollection(), campaign.getDate());        
        
        assert(filteredSquadronMembers.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }

}
