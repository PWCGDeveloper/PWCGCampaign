package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
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
    public FlightTypes getFlightType(Company squadron, boolean isPlayerFlight, PwcgRole missionRole) throws PWCGException
    {
        missionRole = skirmish.forceRoleConversion(missionRole, squadron.determineSide());
        
        FlightTypes flightType = FlightTypes.ANY;
        if (skirmish.hasFlighTypeForRole(squadron, missionRole))
        {
            flightType = skirmish.getFlighTypeForRole(squadron, missionRole);
            PWCGLogger.log(LogLevel.DEBUG, "Skirmish flight type factory returned: " + flightType + " for role " + missionRole + " Squadron " + squadron.determineDisplayName(campaign.getDate()));
        }
        else
        {
            flightType = backupFlightTypeFactory.getFlightType(squadron, isPlayerFlight, missionRole);
            PWCGLogger.log(LogLevel.DEBUG, "Backup flight type factory returned: " + flightType + " for role " + missionRole + " Squadron " + squadron.determineDisplayName(campaign.getDate()));
        }

        return flightType;
    }
}
