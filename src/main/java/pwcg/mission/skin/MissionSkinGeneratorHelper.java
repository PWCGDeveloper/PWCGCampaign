package pwcg.mission.skin;

import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.options.MapSeasonalParameters.Season;

public class MissionSkinGeneratorHelper 
{

    public static void chooseFactorySkin(PlaneMcu plane, List<Skin> factorySkins)
    {
        PWCGLogger.log(LogLevel.DEBUG, "SKIN: Choose factory skin");
        
        Skin skin = pickSkin(plane.getType(), factorySkins);
        if (skin != null)
        {
            plane.setPlaneSkin(skin);
            PWCGLogger.log(LogLevel.DEBUG, "SKIN: Assign factory skin: " + skin.getSkinName());
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "SKIN: factory skin - no factory available");
        }
    }

    public static void chooseSquadronSkin(Squadron squadron, PlaneMcu plane, List<Skin> squadronSkins, Date date) 
    {
        Skin selectedSkin = null;
        
        for (Skin squadSkin : squadronSkins)
        {
            if (squadSkin.getPlane() == null)
            {
                continue;
            }
            
            if (squadSkin.getPlane().isEmpty())
            {
                continue;
            }

            if (!squadSkin.getPlane().equalsIgnoreCase(plane.getType()))
            {
                continue;
            }

            if (squadSkin.getStartDate().after(date) || squadSkin.getEndDate().before(date))
            {
                continue;
            }
            
            selectedSkin = squadSkin;
        }

        if (selectedSkin != null)
        {
            plane.setPlaneSkin(selectedSkin);
        }
    }

    public static Skin chooseSquadronPersonalSkin(IFlight flight, PlaneMcu plane, List<Skin> squadronSkins)
    {
        Skin skin = pickSkin(plane.getType(), squadronSkins);
        if (skin != null)
        {
            plane.setPlaneSkin(skin);
            flight.getMission().addSkinInUse(skin);
            Season season = PWCGContext.getInstance().getCurrentMap().getMapClimate().getSeason(flight.getCampaign().getDate());
            if ((season == Season.WINTER) && skin.isWinter())
            {
                PWCGLogger.log(LogLevel.DEBUG, "SKIN: Assign squadron personal: " + skin.getSkinName());
            }
        }
        else
        {
            PWCGLogger.log(LogLevel.DEBUG, "SKIN: no squadron personal skin available");
        }
        
        return skin;
    }

    public static Skin chooseNonSquadronPersonalSkin(IFlight flight, PlaneMcu plane, List<Skin> personalSkins)
    {
        Skin skin = null;
        
        skin = pickSkin(plane.getType(), personalSkins);
        if (skin != null)
        {
            plane.setPlaneSkin(skin);
            flight.getMission().addSkinInUse(skin);
            PWCGLogger.log(LogLevel.DEBUG, "SKIN: Assign non squadron personal: " + skin.getSkinName());
        }
        else
        {
            PWCGLogger.log(LogLevel.DEBUG, "SKIN: no non squadron personal skin available");
        }

        return skin;
    }

    private static  Skin pickSkin(String planeName, List<Skin> skinSet)
    {              
        Skin skin = null;
        
        if (skinSet.size() > 0)
        {
            int squadronSkinIndex = RandomNumberGenerator.getRandom(skinSet.size());
            skin = skinSet.get(squadronSkinIndex);
        }
        
        return skin;

    }
}
