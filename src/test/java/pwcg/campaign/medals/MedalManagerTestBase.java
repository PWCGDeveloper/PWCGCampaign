package pwcg.campaign.medals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.SquadronRoleSet;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberVictories;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;

public abstract class MedalManagerTestBase
{
    @Mock protected Campaign campaign;
    @Mock protected CrewMember player;
    @Mock protected Company squadron;
    @Mock protected ICountry country;
    @Mock protected CrewMemberVictories squadronMemberVictories;
    @Mock protected SquadronRoleSet squadronRoleSet;
    
    protected List<CrewMember> players = new ArrayList<>();
    protected List<Victory> victories = new ArrayList<>();
    protected List<Medal> medals = new ArrayList<>();
    protected ArmedService service;
    protected IMedalManager medalManager;

    public void setupBase() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);
        
        Mockito.when(player.getMedals()).thenReturn(medals);
        Mockito.when(player.getCrewMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadron.getSquadronRoles()).thenReturn(squadronRoleSet);
        Mockito.when(squadronRoleSet.isSquadronThisRole(ArgumentMatchers.<Date>any(), ArgumentMatchers.<PwcgRole>any())).thenReturn(true);
    }

    protected void awardMedal(int medalId, int numVictoriesNeededForMedal, int victoriesThisMission) throws PWCGException
    {
        makeVictories(numVictoriesNeededForMedal);
        Medal medal = medalManager.award(campaign, player, service, victoriesThisMission);
        Medal referenceMedal = medalManager.getMedal(medalId);
        Assertions.assertTrue (medal.getMedalName().equals(referenceMedal.getMedalName()));
        medals.add(medal);
    }


    protected void awardWoundedAward(CrewMember crewMember, ArmedService service) throws PWCGException
    {
        Medal medal = medalManager.awardWoundedAward(crewMember, service);
        medals.add(medal);
    }

    protected void makeVictories(int numVictories)
    {
        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(numVictories);
    }

}
