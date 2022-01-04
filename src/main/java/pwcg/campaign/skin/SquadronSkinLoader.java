package pwcg.campaign.skin;

import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SquadronSkinLoader
{
    private Map<String, SkinsForPlane> skinsForPlanes;
    
    public SquadronSkinLoader (Map<String, SkinsForPlane> skinsForPlanes)
    {
        this.skinsForPlanes = skinsForPlanes;
    }

    public void loadSquadronSkins() throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        for (Company squadron : squadronManager.getAllCompanies())
        {
            registerSquadronSkins(squadron);
        }
        
    }

    private void registerSquadronSkins(Company squadron)
    {
        for (Skin squadronSkin : squadron.getSkins())
        {
            if (!squadronSkin.getPlane().equals(""))
            {
                SkinsForPlane skinsForPlane = skinsForPlanes.get(squadronSkin.getPlane());
                if (skinsForPlane != null)
                {
                    skinsForPlane.addSquadronSkin(squadronSkin);
                }
                else
                {
                    PWCGLogger.log(LogLevel.ERROR, "Invalid plane for squadron skin <" + squadron.getCompanyId()
                    + "><"  + squadronSkin.getSkinName() + ">" );
                }
            }
            for (String archTypeName : squadronSkin.getArchTypes())
            {
                TankArchType archType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(archTypeName);
                for (TankType planeType : archType.getAllMemberTankTypes())
                {
                    String planeName = planeType.getType();
                    Skin planeSkin = squadronSkin.copy();
                    planeSkin.setPlane(planeName);
                    SkinsForPlane skinsForPlane = skinsForPlanes.get(planeName);
                    skinsForPlane.addSquadronSkin(planeSkin);
                }
            }
        }
    }
}
