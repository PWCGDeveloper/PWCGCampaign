package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;

public class SquadronRoleFinder
{
    public static List<PwcgRole> getRolesForService (ArmedService service, Date date) throws PWCGException
    {
        Map<String, PwcgRole> rolesForService = new TreeMap<>();
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getActiveSquadronsForService(date, service))
        {
            for (PwcgRole role : squadron.getSquadronRolesForDate(date))
            {
                rolesForService.put(role.getRoleDescription(), role);
            }
        }
        
        return new ArrayList<>(rolesForService.values());
    }

    public static List<PwcgRoleCategory> getRoleCategoriesForService (ArmedService service, Date date) throws PWCGException
    {
        Map<String, PwcgRoleCategory> rolesForService = new TreeMap<>();
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getActiveSquadronsForService(date, service))
        {
            PwcgRoleCategory roleCategory = squadron.determineSquadronPrimaryRoleCategory(date);
            rolesForService.put(roleCategory.getRoleCategoryDescription(), roleCategory);
        }
        
        return new ArrayList<>(rolesForService.values());
    }
}
