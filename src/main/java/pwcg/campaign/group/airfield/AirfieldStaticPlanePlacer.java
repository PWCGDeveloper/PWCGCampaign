package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.StaticPlaneSelectorFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;

public class AirfieldStaticPlanePlacer
{

    public IStaticPlane getStaticPlane(IAirfield airfield, Date date, Coordinate position) throws PWCGException  
    {
        // Finally put the object in its place - orientation is random
        double orientation = RandomNumberGenerator.getRandom(360);
        Orientation objectOrientation = new Orientation();
        objectOrientation.setyOri(orientation);

        IStaticPlane staticPlane = getStaticPlaneForField(airfield, date);
        if (staticPlane != null)
        {
            staticPlane.setPosition(position);
            staticPlane.setOrientation(objectOrientation);
            
            return staticPlane;
        }
        
        return null;
    }

    private IStaticPlane getStaticPlaneForField(IAirfield airfield, Date date) throws PWCGException 
    {
        IStaticPlane selectedStaticPlane = null;
        
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        
        ICountry country = airfield.createCountry(date);
        if (country.isNeutral())
        {
            return null;
        }
    
        Squadron squadronForField = squadronManager.getAnyActiveSquadronForAirfield(airfield, date);
        if (squadronForField == null)
        {
        	List<Squadron> allSquadronsForSide = squadronManager.getFlyableSquadrons(country, date);
        	if (allSquadronsForSide.isEmpty())
        	{
                return null;
        	}
        	int index = RandomNumberGenerator.getRandom(allSquadronsForSide.size());
        	squadronForField = allSquadronsForSide.get(index);
        }
            
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
    
        // All planes for all squadrons at this airfield
        List<String> planeNames = new ArrayList<String>();
        for (PlaneType plane : squadronForField.determineCurrentAircraftList(campaign.getDate()))
        {
            planeNames.add(plane.getType());
        }
            
        // All available static planes
        List<IStaticPlane> availableStaticPlanes = new ArrayList<IStaticPlane>();
        for (String planeName : planeNames)
        {
            IStaticPlaneSelector staticPlaneFactory = StaticPlaneSelectorFactory.createStaticPlaneSelector();
            IStaticPlane staticPlane = staticPlaneFactory.getStaticPlane(planeName);
            if (staticPlane != null)
            {
                availableStaticPlanes.add(staticPlane);
            }
        }
            
        // Pick one
        if (availableStaticPlanes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(availableStaticPlanes.size());
            selectedStaticPlane = availableStaticPlanes.get(index);
        }
        
        return selectedStaticPlane;
    }
 
}
