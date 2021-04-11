package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.squadron.Squadron;
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
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        Role missionRole = squadron.getSquadronRoles().selectRoleForMission(campaign.getDate());
        missionRole = convertUnaccaptableRolesForSkirmish(missionRole, squadron);
        missionRole = skirmish.forceRoleConversion(missionRole, squadron.determineSide());
        
        FlightTypes flightType = FlightTypes.ANY;
        if (skirmish.hasFlighTypeForRole(squadron, missionRole))
        {
            flightType = skirmish.getFlighTypeForRole(squadron, missionRole);
            PWCGLogger.log(LogLevel.DEBUG, "Skirmish flight type factory returned: " + flightType + " for role " + missionRole + " Squadron " + squadron.determineDisplayName(campaign.getDate()));
        }
        else
        {
            flightType = backupFlightTypeFactory.getFlightType(squadron, isPlayerFlight);
            PWCGLogger.log(LogLevel.DEBUG, "Backup flight type factory returned: " + flightType + " for role " + missionRole + " Squadron " + squadron.determineDisplayName(campaign.getDate()));
        }

        return flightType;
    }
    
    private Role convertUnaccaptableRolesForSkirmish (Role missionRole, Squadron squadron) throws PWCGException
    {
        if (missionRole == Role.ROLE_STRATEGIC_INTERCEPT)
        {
            return Role.ROLE_FIGHTER;
        }
        
        if (missionRole == Role.ROLE_STRAT_BOMB)
        {
            return Role.ROLE_BOMB;
        }
        
        if (missionRole == Role.ROLE_RECON)
        {
            Role squadronPrimaryRole = squadron.determineSquadronPrimaryRole(campaign.getDate());
            if (squadronPrimaryRole == Role.ROLE_FIGHTER)
            {
                return Role.ROLE_FIGHTER;
            }
            else if (squadronPrimaryRole == Role.ROLE_BOMB)
            {
                return Role.ROLE_BOMB;
            }
            else if (squadronPrimaryRole == Role.ROLE_RECON)
            {
                return Role.ROLE_BOMB;
            }
        }
        
        return missionRole;
    }
}
