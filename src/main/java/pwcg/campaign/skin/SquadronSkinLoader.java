package pwcg.campaign.skin;

import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
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
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getAllSquadrons())
        {
            registerSquadronSkins(squadron);
        }
        
    }

    private void registerSquadronSkins(Squadron squadron)
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
                    PWCGLogger.log(LogLevel.ERROR, "Invalid plane for squadron skin <" + squadron.getSquadronId()
                    + "><"  + squadronSkin.getSkinName() + ">" );
                }
            }
            for (String archTypeName : squadronSkin.getArchTypes())
            {
                PlaneArchType archType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(archTypeName);
                for (PlaneType planeType : archType.getAllMemberPlaneTypes())
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
