package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.StaticPlaneSelectorFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;

public class AirfieldStaticPlanePlacer
{

    // TODO TC Currently not used.  Add back in if static planes are useful
    public IStaticPlane getStaticPlane(Airfield airfield, ICountry airfieldCountry, Date date, Coordinate position) throws PWCGException  
    {
        Orientation objectOrientation = Orientation.createRandomOrientation();
        IStaticPlane staticPlane = getStaticPlaneForField(airfield, date);
        if (staticPlane != null)
        {
            staticPlane.setPosition(position);
            staticPlane.setOrientation(objectOrientation);
            
            return staticPlane;
        }
        
        return null;
    }

    private IStaticPlane getStaticPlaneForField(Airfield airfield, Date date) throws PWCGException 
    {
        IStaticPlane selectedStaticPlane = null;
        
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company squadronForField = squadronManager.getAnyActiveCompanyForAirfield(airfield, date);
        if (squadronForField == null)
        {
            return null;
        }
            
        Campaign campaign = PWCGContext.getInstance().getCampaign();
    
        // All planes for all squadrons at this airfield
        List<String> planeNames = new ArrayList<String>();
        for (TankType plane : squadronForField.determineCurrentAircraftList(campaign.getDate()))
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
