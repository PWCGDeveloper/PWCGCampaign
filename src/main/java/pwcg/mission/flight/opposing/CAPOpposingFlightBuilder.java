package pwcg.mission.flight.opposing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class CAPOpposingFlightBuilder implements IOpposingFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private Squadron playerSquadron;
    
    public CAPOpposingFlightBuilder (Mission mission, Squadron playerSquadron)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.playerSquadron = playerSquadron;
    }

    @Override
    public List<IFlight> createOpposingFlight() throws PWCGException 
    {
        SimpleOpposingFlightFinisher opposingFlightFinisher = new SimpleOpposingFlightFinisher(campaign, mission, playerSquadron);
        List<PwcgRole> opposingFlightRoles = new ArrayList<>(Arrays.asList(PwcgRole.ROLE_ATTACK));
        List<IFlight> opposingFlights = opposingFlightFinisher.createOpposingFlights(FlightTypes.GROUND_ATTACK, opposingFlightRoles);
        return opposingFlights;
    }
}
