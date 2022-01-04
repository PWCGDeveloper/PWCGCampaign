package pwcg.campaign.skin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class SkinLoader
{
    private Map<String, SkinsForPlane> skinsForPlanes = new HashMap<>();
    
    public SkinLoader ()
    {
    }
    
    public Map<String, SkinsForPlane> loadPwcgSkins()
    {
        initialize();
        readConfiguredAndDoNotUseSkins();
        loadSquadronSkins();
        loadAceSkins();
        readLooseSkins();
        
        return skinsForPlanes;
    }

    private void initialize()
    {
        try
        {
            List<TankType> allPlanes = PWCGContext.getInstance().getTankTypeFactory().getAllPlanes();
            for(TankType plane : allPlanes)
            {
                SkinsForPlane skinsForPlane = new SkinsForPlane();
                skinsForPlanes.put(plane.getType(), skinsForPlane);
            }
        }
        catch (Exception exp)
        {
             PWCGLogger.logException(exp);
        }
    }

    private void readConfiguredAndDoNotUseSkins()
    {
        try
        {
            ConfiguredSkinReader configuredSkinReader = new ConfiguredSkinReader(skinsForPlanes);
            configuredSkinReader.readConfiguredSkinsFromPlaneSkinFiles();
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
        }
    }

    private void loadSquadronSkins()
    {
        try
        {
            SquadronSkinLoader squadronSkinLoader = new SquadronSkinLoader(skinsForPlanes);
            squadronSkinLoader.loadSquadronSkins();
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
        }        
    }

    private void loadAceSkins()
    {
        try
        {
            AceSkinLoader squadronSkinLoader = new AceSkinLoader(skinsForPlanes);
            squadronSkinLoader.loadHistoricalAceSkins();;
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
        }        
    }

    private void readLooseSkins()
    {
        LooseSkinLoader looseSkinLoader = new LooseSkinLoader(skinsForPlanes);
        looseSkinLoader.readLooseSkins();
    }

}
