package pwcg.mission.flight.opposing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class BalloonBustOpposingFlightBuilder implements IOpposingFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private Squadron playerSquadron;
    
    public BalloonBustOpposingFlightBuilder (Mission mission, Squadron playerSquadron)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.playerSquadron = playerSquadron;
    }

    @Override
    public List<IFlight> createOpposingFlight() throws PWCGException 
    {
        SimpleOpposingFlightFinisher opposingFlightFinisher = new SimpleOpposingFlightFinisher(campaign, mission, playerSquadron);
        List<Role> opposingFlightRoles = new ArrayList<>(Arrays.asList(Role.ROLE_FIGHTER));
        List<IFlight> opposingFlights = opposingFlightFinisher.createOpposingFlights(FlightTypes.BALLOON_DEFENSE, opposingFlightRoles);
        return opposingFlights;
    }
}
