package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.MissionEntityBuilder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AcesKilledEventGeneratorTest
{
    @Mock private Campaign campaign;
    @Mock CampaignPersonnelManager personnelManager;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {         
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170420"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
    }

    @Test
    public void aceKilled () throws PWCGException
    {       
        TankAce aceKilledInMission = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());

        List<CrewMember> acesKilledInMissionAndElapsedTime = new ArrayList<>();
        acesKilledInMissionAndElapsedTime.add(aceKilledInMission);

        AcesKilledEventGenerator acesKilledEventGenerator = new AcesKilledEventGenerator(campaign);
        List<AceKilledEvent> acesKilledEvents = acesKilledEventGenerator.createAceKilledEvents(acesKilledInMissionAndElapsedTime);
        
        Assertions.assertTrue (acesKilledEvents.size() == 1);
    }

    @Test
    public void aceKilledNotEnoughVictories () throws PWCGException
    {       
        TankAce aceKilledInMission = new TankAce();
        aceKilledInMission.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        for (int i = 0; i < AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY-1; ++i)
        {
            Victory victory = new Victory();
            aceKilledInMission.addVictory(victory);
        }

        List<CrewMember> acesKilledInMissionAndElapsedTime = new ArrayList<>();
        acesKilledInMissionAndElapsedTime.add(aceKilledInMission);

        AcesKilledEventGenerator acesKilledEventGenerator = new AcesKilledEventGenerator(campaign);
        List<AceKilledEvent> acesKilledEvents = acesKilledEventGenerator.createAceKilledEvents(acesKilledInMissionAndElapsedTime);
        
        Assertions.assertTrue (acesKilledEvents.size() == 0);
    }
}
