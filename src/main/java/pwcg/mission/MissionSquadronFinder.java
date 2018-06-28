package pwcg.mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionSquadronFinder 
{
	private List<Squadron> alliedSquads = new ArrayList<Squadron>();
	private List<Squadron> axisSquads = new ArrayList<Squadron>();
	private Campaign campaign;

	public MissionSquadronFinder(Campaign campaign)
	{
		this.campaign = campaign;
	}

	public void findSquadronsForMission (Date date) throws PWCGException 
	{	
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
		
		int initialSquadronSearchRadiusKey = configManager.getIntConfigParam(ConfigItemKeys.InitialSquadronSearchRadiusKey);

        // Get a point at the front.  We will use the German lines as a reference point.
		String airfieldName = campaign.getAirfieldName();
        IAirfield field =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
		PWCGMap map =  PWCGContextManager.getInstance().getCurrentMap();
		FrontLinesForMap frontLinesForMap =  map.getFrontLinesForMap(campaign.getDate());

        Coordinate closestAlliedFrontPosition =  frontLinesForMap.findClosestFrontCoordinateForSide(field.getPosition(), Side.ALLIED);
        alliedSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsBySide(closestAlliedFrontPosition, 
                                                                                5, 
                                                                                initialSquadronSearchRadiusKey,
                                                                                Side.ALLIED,
                                                                                date);

        Coordinate closestAxisFrontPosition =  frontLinesForMap.findClosestFrontCoordinateForSide(field.getPosition(), Side.AXIS);
        axisSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsBySide(closestAxisFrontPosition, 
                                                                              5, 
                                                                              initialSquadronSearchRadiusKey,
                                                                              Side.AXIS,
                                                                              date);
        determineSquadronAvailability();
	}

    public void findSquadronsForSeaPlaneMission (Date date) throws PWCGException 
    {   
        alliedSquads.clear();
        axisSquads.clear();

        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();

        // Get the Allied lines and pick the northern most position
        Coordinate seaFrontPosition =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate()).getCoordinates(0, Side.ALLIED);

        // Add each sea plane squadron multiple times to increase the number of sea plane missions
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_SEA_PLANE);
        
        List<Squadron> alliedSeaPlaneSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(seaFrontPosition.copy(), 1, 400000.0, acceptableRoles, Side.ALLIED, date);
        List<Squadron> axisSeaPlaneSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(seaFrontPosition.copy(), 1, 400000.0, acceptableRoles, Side.AXIS, date);

        for (Squadron alliedSeaPlaneSquad : alliedSeaPlaneSquads)
        {
            for (int i = 0; i < 3; ++i)
            {
                alliedSquads.add(alliedSeaPlaneSquad);
            }
        }
        
        for (Squadron axisSeaPlaneSquad : axisSeaPlaneSquads)
        {
            for (int i = 0; i < 3; ++i)
            {
                axisSquads.add(axisSeaPlaneSquad);
            }
        }
        
        int initialSquadronSearchRadiusKey = configManager.getIntConfigParam(ConfigItemKeys.InitialSquadronSearchRadiusKey);

        // All missions are available for sea planes although most will be rejected due to lack of proximity
        acceptableRoles = Role.getAllRoles();

        List<Squadron> otherAlliedSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(seaFrontPosition, 
                                                                                     3, 
                                                                                     initialSquadronSearchRadiusKey,
                                                                                     acceptableRoles,
                                                                                     Side.ALLIED,
                                                                                     date);

        List<Squadron> otherAxisSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(seaFrontPosition, 
                                                                                        3, 
                                                                                        initialSquadronSearchRadiusKey,
                                                                                        acceptableRoles,
                                                                                        Side.AXIS,
                                                                                        date);
        
        alliedSquads.addAll(otherAlliedSquads);
        axisSquads.addAll(otherAxisSquads);
        determineSquadronAvailability();
    }

    public void findSquadronsForStrategicMission (Date date) throws PWCGException 
    {   
        alliedSquads.clear();
        axisSquads.clear();

        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();

        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.clear();
        acceptableRoles.add(Role.ROLE_FIGHTER);

        String airfieldName = campaign.getAirfieldName();
        IAirfield field =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);

        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        int initialSquadronSearchRadiusKey = configManager.getIntConfigParam(ConfigItemKeys.InitialSquadronSearchRadiusKey);

        Coordinate closestAlliedFrontPosition =  frontLinesForMap.findClosestFrontCoordinateForSide(field.getPosition(), Side.ALLIED);
        List<Squadron> otherAlliedSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(closestAlliedFrontPosition, 
                                                                                     3, 
                                                                                     initialSquadronSearchRadiusKey,
                                                                                     acceptableRoles,
                                                                                     Side.ALLIED,
                                                                                     date);

        Coordinate closestAxisFrontPosition =  frontLinesForMap.findClosestFrontCoordinateForSide(field.getPosition(), Side.AXIS);
        List<Squadron> otherAxisSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(closestAxisFrontPosition, 
                                                                                        3, 
                                                                                        initialSquadronSearchRadiusKey,
                                                                                        acceptableRoles,
                                                                                        Side.AXIS,
                                                                                        date);
        
        alliedSquads.addAll(otherAlliedSquads);
        axisSquads.addAll(otherAxisSquads);
        determineSquadronAvailability();
    }

	public void findSquadronsForNightMission() 
	{	
		alliedSquads.clear();
		axisSquads.clear();
	}

	private void determineSquadronAvailability() throws PWCGException 
	{	
		alliedSquads = filterSquadrons(alliedSquads);
		axisSquads = filterSquadrons(axisSquads);
	}

	private List<Squadron> filterSquadrons(List<Squadron> squadronsToBeEvaluated) throws PWCGException
	{
		List<Squadron> acceptedSquadrons = new ArrayList<>();
		for (Squadron squadron : squadronsToBeEvaluated)
		{
			if (squadron.getSquadronId() == campaign.getSquadronId())
			{
				continue;
			}
			
			if (isSquadronPersonnelDepleted(squadron))
			{
				continue;
			}

			acceptedSquadrons.add(squadron);
		}
		
		return acceptedSquadrons;
	}
	

	private boolean isSquadronPersonnelDepleted(Squadron squadron) throws PWCGException 
	{
		int numSquadronMembers = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId()).getActiveSquadronMembersWithAces().getActiveCount(campaign.getDate());
		if (numSquadronMembers < 6)
		{
			return true;
		}
		
		if (numSquadronMembers > 10)
		{
			return false;
		}
		
		int diceRoll = RandomNumberGenerator.getRandom(10);
		if (numSquadronMembers < diceRoll)
		{
			return true;
		}
		
		return false;
	}

	public List<Squadron> getAlliedSquads() 
	{
		return alliedSquads;
	}

	public List<Squadron> getAxisSquads()
	{
		return axisSquads;
	}
}
