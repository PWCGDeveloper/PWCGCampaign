package pwcg.dev.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.dev.utils.AirfieldDistanceOrganizer.AirfieldSet;

public class AirfieldBestMMatchFinder
{
    public static IAirfield recommendBestMatch(Squadron squadron, Date date) throws PWCGException
    {        
        IAirfield squadronField = squadron.determineCurrentAirfieldAnyMap(date);

        double closest = 100000000.0;
        IAirfield bestField = null;
        
        AirfieldDistanceOrganizer airfieldDistanceOrganizer = new AirfieldDistanceOrganizer();
        airfieldDistanceOrganizer.process(date, FrontMapIdentifier.MOSCOW_MAP);
        
        AirfieldSet airfieldSet = airfieldDistanceOrganizer.axisAirfieldSet;
        if (squadron.determineSquadronCountry(date).getSide() == Side.ALLIED)
        {
            airfieldSet = airfieldDistanceOrganizer.alliedAirfieldSet;
        }
        
        List<IAirfield> relativeFields = new ArrayList<IAirfield>(airfieldSet.getBomberFields().values());
        Role squadronRole = squadron.determineSquadronPrimaryRole(date);
        if (squadronRole.isRoleCategory(RoleCategory.FIGHTER))
        {
            relativeFields = new ArrayList<IAirfield>(airfieldSet.getFighterFields().values());
        }
        
        for (IAirfield field: relativeFields)
        {
            double distanceToOtherField = MathUtils.calcDist(squadronField.getPosition(), field.getPosition());
            if (distanceToOtherField < closest)
            {
                closest = distanceToOtherField;
                bestField = field;
            }
        }
        
        System.out.println(bestField.getName() + "   Km to front: " + closest);
        
        return bestField;
    }
}
