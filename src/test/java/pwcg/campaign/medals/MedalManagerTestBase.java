package pwcg.campaign.medals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberVictories;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronRoleSet;
import pwcg.core.exception.PWCGException;

public abstract class MedalManagerTestBase
{
    @Mock protected Campaign campaign;
    @Mock protected SquadronMember player;
    @Mock protected Squadron squadron;
    @Mock protected ICountry country;
    @Mock protected SquadronMemberVictories squadronMemberVictories;
    @Mock protected SquadronRoleSet squadronRoleSet;
    
    protected List<SquadronMember> players = new ArrayList<>();
    protected List<Victory> victories = new ArrayList<>();
    protected List<Medal> medals = new ArrayList<>();
    protected ArmedService service;
    protected IMedalManager medalManager;

    public void setup() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);
        
        Mockito.when(campaign.getPlayers()).thenReturn(players);
        Mockito.when(campaign.determineCountry()).thenReturn(country);
        Mockito.when(campaign.determineCountry()).thenReturn(country);
        Mockito.when(player.getVictories()).thenReturn(victories);
        Mockito.when(player.getMedals()).thenReturn(medals);
        Mockito.when(player.getSquadronMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadron.getSquadronRoles()).thenReturn(squadronRoleSet);
        Mockito.when(squadronRoleSet.isSquadronThisRole(Matchers.<Date>any(), Matchers.<Role>any())).thenReturn(true);
    }

    protected void awardMedal(int medalId, int numVictoriesNeededForMedal, int victoriesThisMission) throws PWCGException
    {
        makeVictories(numVictoriesNeededForMedal);
        Medal medal = medalManager.award(campaign, player, service, victoriesThisMission);
        Medal referenceMedal = medalManager.getMedal(medalId);
        assert (medal.getMedalName().equals(referenceMedal.getMedalName()));
        medals.add(medal);
    }

    protected void makeVictories(int numVictories)
    {
        victories.clear();
        for (int i = 0; i < numVictories; ++i)
        {
            victories.add(new Victory());
        }
        
        Mockito.when(squadronMemberVictories.getAirToAirVictories()).thenReturn(numVictories);
    }

}
