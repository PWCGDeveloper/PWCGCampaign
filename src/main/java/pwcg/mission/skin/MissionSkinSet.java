package pwcg.mission.skin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.skin.Skin;

public class MissionSkinSet
{
    Map<String, List<Skin>> factorySkins = new HashMap<>();
    Map<String, List<Skin>> squadronSkins = new HashMap<>();
    Map<String, List<Skin>> squadronPersonalSkins = new HashMap<>();
    Map<String, List<Skin>> nonSquadronPersonalSkin = new HashMap<>();

    public void addFactorySkins(String planeType, List<Skin> factorySkinsForPlane)
    {
        factorySkins.put(planeType, factorySkinsForPlane);
    }

    public void addSquadronSkins(String planeType, List<Skin> squadronSkinsForPlane)
    {
        squadronSkins.put(planeType, squadronSkinsForPlane);
    }

    public void addSquadronPersonalSkins(String planeType, List<Skin> squadronPersonalSkinsForPlane)
    {
        squadronPersonalSkins.put(planeType, squadronPersonalSkinsForPlane);
    }

    public void addNonSquadronPersonalSkins(String planeType, List<Skin> nonSquadronPersonalSkinsForPlane)
    {
        nonSquadronPersonalSkin.put(planeType, nonSquadronPersonalSkinsForPlane);
    }

    public List<Skin> getFactorySkins(String planeType)
    {
        return factorySkins.get(planeType);
    }

    public List<Skin> getSquadronSkins(String planeType)
    {
        return squadronSkins.get(planeType);
    }

    public List<Skin> getSquadronPersonalSkins(String planeType)
    {
        return squadronPersonalSkins.get(planeType);
    }

    public List<Skin> getNonSquadronPersonalSkin(String planeType)
    {
        return nonSquadronPersonalSkin.get(planeType);
    }
}
