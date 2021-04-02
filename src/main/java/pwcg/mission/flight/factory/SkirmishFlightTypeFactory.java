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

        FlightTypes flightType = skirmish.getFlighTypeForRole(squadron, missionRole);
        if (flightType == FlightTypes.ANY)
        {
            flightType = backupFlightTypeFactory.getFlightType(squadron, isPlayerFlight);
        }

        return flightType;
    }
}
