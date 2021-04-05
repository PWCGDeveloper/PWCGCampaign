package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.Skirmish;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public class SkirmishFlightTypeFactory implements IFlightTypeFactory
{
    private Campaign campaign;
    private Skirmish skirmish;
    private IFlightTypeFactory backupFlightTypeFactory;

    public SkirmishFlightTypeFactory (Campaign campaign, Skirmish skirmish, IFlightTypeFactory backupFlightTypeFactory) 
    {
        this.campaign = campaign;
        this.skirmish = skirmish;
        this.backupFlightTypeFactory = backupFlightTypeFactory;
    }

    @Override
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        Role missionRole = squadron.getSquadronRoles().selectRoleForMission(campaign.getDate());
        missionRole = skirmish.forceRoleConversion(missionRole, squadron.determineSide());
        
        FlightTypes flightType = FlightTypes.ANY;
        if (skirmish.hasFlighTypeForRole(squadron, missionRole))
        {
            flightType = skirmish.getFlighTypeForRole(squadron, missionRole);
        }
        else
        {
            flightType = backupFlightTypeFactory.getFlightType(squadron, isPlayerFlight);
        }

        return flightType;
    }
}
