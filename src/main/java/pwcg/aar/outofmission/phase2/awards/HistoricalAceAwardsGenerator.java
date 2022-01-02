package pwcg.aar.outofmission.phase2.awards;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.aar.ui.events.model.AAREvent;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.HistoricalAce;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class HistoricalAceAwardsGenerator
{
    private HistoricalAceAwards historicalAceEvents = new HistoricalAceAwards();

    private Campaign campaign;
    private Date newDate;
    private List<AAREvent> eventList;

    public HistoricalAceAwardsGenerator(Campaign campaign, Date newDate) 
    {
        this.campaign = campaign;
        this.newDate = newDate;
    }

    public HistoricalAceAwards aceEvents() throws PWCGException 
    {

        for (TankAce ace : campaign.getPersonnelManager().getCampaignAces().getAllCampaignAces().values())
        {
            if (!(ace.getCrewMemberActiveStatus() <= CrewMemberStatus.STATUS_CAPTURED))
            {
                generateEventsForAce(ace);
            }
        }
        
        return historicalAceEvents;
    }

    private void generateEventsForAce(TankAce aceAtPreviousDate) throws PWCGException
    {
        HistoricalAce historicalAce = PWCGContext.getInstance().getAceManager().getHistoricalAceBySerialNumber(aceAtPreviousDate.getSerialNumber());
        if (historicalAce != null)
        {
            TankAce aceAtNewDate = historicalAce.getAtDate(newDate);
            aceMissions(aceAtPreviousDate);
            aceVictories(aceAtPreviousDate, aceAtNewDate);
            aceMedals(aceAtPreviousDate, aceAtNewDate);
            acePromotions(aceAtPreviousDate, aceAtNewDate);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "Ace not found in historical aces: " + aceAtPreviousDate.getSerialNumber());
        }
    }

    private void acePromotions(TankAce aceAtPreviousDate, TankAce aceAtNewDate)
    {
        if (!aceAtNewDate.getRank().equals(aceAtPreviousDate.getRank()))
        {
            historicalAceEvents.addAcePromotions(aceAtPreviousDate.getSerialNumber(), aceAtNewDate.getRank());
        }
    }

    private void aceMissions(TankAce aceAtPreviousDate)
    {
        aceAtPreviousDate.setBattlesFought(aceAtPreviousDate.getBattlesFought() + 3);
    }

    private void aceVictories(TankAce aceAtPreviousDate, TankAce aceAtNewDate) throws PWCGException
    {
        for (Victory victory : aceAtNewDate.getCrewMemberVictories().getAirToAirVictories())
        {
            if (victory.getDate().before(newDate) &&
                            (victory.getDate().equals(campaign.getDate()) || victory.getDate().after(campaign.getDate())))
            {
                historicalAceEvents.addAceVictory(aceAtPreviousDate.getSerialNumber(), victory);
            }
        }
    }

    private void aceMedals(TankAce aceAtPreviousDate, TankAce aceAtNewDate)
    {
        for (int medalIndex = 0; medalIndex < aceAtNewDate.getMedals().size(); ++medalIndex)
        {
            Medal medal = aceAtNewDate.getMedals().get(medalIndex);
            Date medalDate = medal.getMedalDate();
            if (!medalDate.after(newDate))
            {
                List<Medal> acesMedals = aceAtPreviousDate.getMedals();
                boolean hasMedal = false;
                for (Medal aceMedal : acesMedals)
                {
                    if (aceMedal.getMedalName().equals(medal.getMedalName()))
                    {
                        hasMedal = true;
                        break;
                    }
                }

                if (!hasMedal)
                {
                    historicalAceEvents.addAceMedal(aceAtPreviousDate.getSerialNumber(), medal);
                }
            }
        }
    }

    public List<AceKilledEvent> getAcesKilledInMission()
    {
        List<AceKilledEvent> acesKilled = new ArrayList<>();
        for (AAREvent event : eventList)
        {
            if (event instanceof AceKilledEvent)
            {
                AceKilledEvent aceKilled = (AceKilledEvent)event;
                acesKilled.add(aceKilled);
            }
        }
        
        return acesKilled;
    }
}
