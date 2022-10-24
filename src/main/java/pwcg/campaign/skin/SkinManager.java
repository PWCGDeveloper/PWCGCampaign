package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SkinManager 
{
    private Map<String, SkinsForPlane> skinsForPlanes = new HashMap<>();
		
    public SkinManager ()
	{
	}

	public void initialize()
    {
        SkinLoader skinLoader = new SkinLoader();
        skinsForPlanes = skinLoader.loadPwcgSkins();
    }

    public List<Skin> getSquadronSkinsByPlaneSquadronDate(String planeName, int squadronId, Date date)
    {
    	List<Skin> skinsForSquadron = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet squadronSkinSet = skinsForPlanes.get(planeName).getSquadronSkins();
        	skinsForSquadron = new ArrayList<>(squadronSkinSet.getSkins().values());
            skinsForSquadron = SkinFilter.skinFilterSquadron(skinsForSquadron, squadronId);
            skinsForSquadron = SkinFilter.skinFilterDate(skinsForSquadron, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsBySquadronPlaneDateInUse: Invalid plane " + planeName);
        }
        
        return skinsForSquadron;
    }

    public List<Skin> getSkinsBySquadron(int squadronId)
    {
        List<Skin> configuredSkinsForSquadron = new ArrayList<Skin>();

        for (SkinsForPlane skinsForPlane : skinsForPlanes.values())
        {
            SkinSet configuredSkins = skinsForPlane.getConfiguredSkins();
        	configuredSkinsForSquadron = new ArrayList<>(configuredSkins.getSkins().values());
            List<Skin> skinsForSquadronAndPlane = SkinFilter.skinFilterSquadron(configuredSkinsForSquadron, squadronId);
            
            configuredSkinsForSquadron.addAll(skinsForSquadronAndPlane);
        }

        
        return configuredSkinsForSquadron;
    }

	public List<Skin> getSkinsByPlaneSquadronDateInUse(String planeName, int squadronId, Date date)
	{
        List<Skin> skinsForSquadronPlaneDate = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
            SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
            skinsForSquadronPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForSquadronPlaneDate = SkinFilter.skinFilterSquadron(skinsForSquadronPlaneDate, squadronId);
            skinsForSquadronPlaneDate = SkinFilter.skinFilterDate(skinsForSquadronPlaneDate, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsBySquadronPlaneDateInUse: Invalid plane " + planeName);
        }
        
        if (skinsForSquadronPlaneDate.isEmpty())
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsBySquadronPlaneDateInUse: No skins " + planeName);
        }
		
		return skinsForSquadronPlaneDate;
	}

    public List<Skin> getPersonalSkinsByPlaneCountryDateInUse(String planeName, String countryName, Date date)
    {
        List<Skin> skinsForCountryPlaneDate = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
        	skinsForCountryPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
        	skinsForCountryPlaneDate = SkinFilter.skinFilterSquadron(skinsForCountryPlaneDate, Skin.PERSONAL_SKIN);
        	skinsForCountryPlaneDate = SkinFilter.skinFilterDate(skinsForCountryPlaneDate, date);
        	skinsForCountryPlaneDate = SkinFilter.skinFilterCountry(skinsForCountryPlaneDate, countryName);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getPersonalSkinsByPlaneCountryDateInUse: Invalid plane " + planeName);
        }
        
        return skinsForCountryPlaneDate;
    }

    public List<Skin> getSkinsBySquadronPlaneDate(String planeName, int squadronId, Date date)
    {
        List<Skin> skinsForSquadronPlaneDate = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
        	skinsForSquadronPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForSquadronPlaneDate = SkinFilter.skinFilterSquadron(skinsForSquadronPlaneDate, squadronId);
            skinsForSquadronPlaneDate = SkinFilter.skinFilterDate(skinsForSquadronPlaneDate, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsBySquadronPlaneDate: Invalid plane " + planeName);
        }
        
        return skinsForSquadronPlaneDate;
    }

    public List<Skin> getSkinsByPlaneSquadron(String planeName, int squadronId)
    {
        List<Skin> skinsForSquadronPlane = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
        	skinsForSquadronPlane = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForSquadronPlane = SkinFilter.skinFilterSquadron(skinsForSquadronPlane, squadronId);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByPlaneSquadron: Invalid plane " + planeName);
        }
        
        return skinsForSquadronPlane;
    }

    public List<Skin> getSkinsByPlaneCountry(String planeName,  String countryName)
    {
        List<Skin> skinsForPlaneCountry = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
        	skinsForPlaneCountry = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForPlaneCountry = SkinFilter.skinFilterCountry(skinsForPlaneCountry, countryName);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByPlaneCountry: Invalid plane " + planeName);
        }
        
        return skinsForPlaneCountry;
    }

    public Skin getConfiguredSkinByName(String planeName, String skinName)
    {
        Skin returnSkin = null;

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinsForPlane skinsForPlane = skinsForPlanes.get(planeName);
            SkinSet configuredSkins = skinsForPlane.getConfiguredSkins();
            List<Skin>  skinsByName = new ArrayList<>(configuredSkins.getSkins().values());
            skinsByName = SkinFilter.skinFilterSkinName(skinsByName, skinName);
            
            if (skinsByName.size() > 0)
            {
                returnSkin = skinsByName.get(0);
            }
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getConfiguredSkinByName: Invalid plane " + planeName);
        }
        
        return returnSkin;
    }

    public List<Skin> getLooseSkinByPlane(String planeName)
    {
        List<Skin> looseSkinsForPlane = new ArrayList<>();

        if (skinsForPlanes.containsKey(planeName))
        {
            SkinSet looseSkins = skinsForPlanes.get(planeName).getLooseSkins();
            looseSkinsForPlane = new ArrayList<>(looseSkins.getSkins().values());
        }
        else
        {
            PWCGLogger.log(LogLevel.INFO, "getLooseSkinByPlane: Unconfigured plane for PWCG " + planeName);
        }
        
        return looseSkinsForPlane;
    }
    
    public Map<String, List<Skin>> getAllSkinsByPlane()
    {
        Map<String, List<Skin>> allSkinsInPWCG = new HashMap<String, List<Skin>>();
        
        for (String planeType : skinsForPlanes.keySet())
        {
            SkinsForPlane skinsForPlane = skinsForPlanes.get(planeType);
            List<Skin> skins = skinsForPlane.getAllUsedByPWCG();
            
            allSkinsInPWCG.put(planeType, skins);
        }
        
        return allSkinsInPWCG;
    }

    public String getSkinCategory(String planeName, String skinName)
    {
        if (skinsForPlanes.containsKey(planeName))
        {
            SkinsForPlane skinsForPlane = skinsForPlanes.get(planeName);
            return skinsForPlane.getSkinCategory(skinName);
        }
        
        return "";
    }

    public SkinsForPlane getSkinsForPlane(String planeTypeDesc)
    {
        return skinsForPlanes.get(planeTypeDesc);
    }

	public Map<String, SkinsForPlane> getSkinsForPlanes()
	{
		return skinsForPlanes;
	}
}
