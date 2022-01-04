package pwcg.mission.skin;

import java.util.Date;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.skin.Skin;
import pwcg.mission.flight.plane.PlaneMcu;

public class MissionSkinInitializer 
{
    public static void intitializeSkin(MissionSkinSet missionSkinSet, Company company, PlaneMcu plane, Date date)
    {
        setFactorySkin(missionSkinSet, plane, date);
        setSquadronSkin(missionSkinSet, company, plane, date);
    }

    private static void setFactorySkin(MissionSkinSet missionSkinSet, PlaneMcu plane, Date date)
    {
        List<Skin> factorySkins = missionSkinSet.getFactorySkins(plane.getType());
        MissionSkinGeneratorHelper.chooseFactorySkin(plane, factorySkins);
    }

    private static void setSquadronSkin(MissionSkinSet missionSkinSet, Company squadron, PlaneMcu plane, Date date)
    {
        List<Skin> squadronSkins = missionSkinSet.getSquadronSkins(plane.getType());
        MissionSkinGeneratorHelper.chooseSquadronSkin(squadron, plane, squadronSkins, date);
    }
}
