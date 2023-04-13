package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.mission.options.MapSeasonalParameters.Season;

/**
 * Filters that can be applied to skin sets
 * 
 * @author Patrick Wilson
 *
 */
public class SkinFilter
{

    static public List<Skin> skinFilterSquadron (List<Skin> skins, int squadronId)
    {
        List<Skin> filteredSkins = new ArrayList<Skin>();
        
        for (Skin skin : skins)
        {
            if (skin.getSquadId() == squadronId)
            {
                filteredSkins.add(skin);
            }
        }
        
        return filteredSkins;
    }


    static public List<Skin> skinFilterCountry (List<Skin> skins, String countryName)
    {
        List<Skin> filteredSkins = new ArrayList<Skin>();
        
        for (Skin skin : skins)
        {
            if (skin.getCountry().equalsIgnoreCase(countryName))
            {
                filteredSkins.add(skin);
            }
        }
        
        return filteredSkins;
    }

    static public List<Skin> skinFilterDate (List<Skin> skins, Date date)
    {
        List<Skin> filteredSkins = new ArrayList<Skin>();
        
        for (Skin skin : skins)
        {
            if (!date.before(skin.getStartDate()) && !date.after(skin.getEndDate()))
            {
                filteredSkins.add(skin);
            }
        }
        
        return filteredSkins;
    }

    static public List<Skin> skinFilterInUse (List<Skin> skins, SkinsInUse skinsInUse)
    {
        List<Skin> filteredSkins = new ArrayList<Skin>();
        
        for (Skin skin : skins)
        {
            if (!skinsInUse.isSkinInUse(skin))
            {
                filteredSkins.add(skin);
            }
        }
        
        return filteredSkins;
    }

    static public List<Skin> skinFilterSkinName (List<Skin> skins, String skinName)
    {
        List<Skin> filteredSkins = new ArrayList<Skin>();
        
        for (Skin skin : skins)
        {
            if (skin.getSkinName().equalsIgnoreCase(skinName))
            {
                filteredSkins.add(skin);
            }
        }
        
        return filteredSkins;
    }

    public static List<Skin> skinFilterSeason(List<Skin> skins, Season season)
    {
        List<Skin> filteredSkins = new ArrayList<Skin>();
        
        for (Skin skin : skins)
        {
            if (skin.isWinter() && season == Season.WINTER)
            {
                filteredSkins.add(skin);
            }
        }
        
        if (filteredSkins.isEmpty())
        {
            for (Skin skin : skins)
            {
                if (!skin.isWinter())
                {
                    filteredSkins.add(skin);
                }
            }
        }

        return filteredSkins;
    }
}
