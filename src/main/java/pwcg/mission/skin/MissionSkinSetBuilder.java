package pwcg.mission.skin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinFilter;
import pwcg.campaign.skin.SkinManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.options.MapSeasonalParameters.Season;

public class MissionSkinSetBuilder 
{
    private IFlight flight;
    public MissionSkinSetBuilder(IFlight flight)
    {
        this.flight = flight;
    }
    
    public MissionSkinSet buildSummerMissionSkinSet() throws PWCGException
    {
        return buildMissionSkinSetForSeason(Season.SUMMER);
    }
    
    public MissionSkinSet buildWinterMissionSkinSet() throws PWCGException
    {
        return buildMissionSkinSetForSeason(Season.WINTER);
    }

    private MissionSkinSet buildMissionSkinSetForSeason(Season season) throws PWCGException
    {
        MissionSkinSet missionSkinSet = new MissionSkinSet();

        SkinManager skinManager = PWCGContext.getInstance().getSkinManager();
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            Squadron squadron = flight.getSquadron();
            Date date = flight.getCampaign().getDate();

            List<Skin> factorySkinsForPlane = skinManager.getSkinsBySquadronPlaneDate(plane.getType(), Skin.FACTORY_GENERIC, date);
            factorySkinsForPlane = filterSkinSet(factorySkinsForPlane, season);

            List<Skin> squadronSkinsForPlane = skinManager.getSquadronSkinsByPlaneSquadronDate(plane.getType(), squadron.getSquadronId(), date);
            squadronSkinsForPlane = filterSkinSet(squadronSkinsForPlane, season);

            List<Skin> squadronPersonalSkinsForPlane = skinManager.getSkinsByPlaneSquadronDateInUse(plane.getType(), squadron.getSquadronId(), date);
            squadronPersonalSkinsForPlane = filterSkinSet(squadronPersonalSkinsForPlane, season);

            List<Skin> nonSquadronPersonalSkinsForPlane = skinManager.getPersonalSkinsByPlaneCountryDateInUse(plane.getType(), squadron.determineSquadronCountry(date).getCountryName(), date);
            nonSquadronPersonalSkinsForPlane = filterSkinSet(nonSquadronPersonalSkinsForPlane, season);

            missionSkinSet.addFactorySkins(plane.getType(), factorySkinsForPlane);
            missionSkinSet.addSquadronSkins(plane.getType(), squadronSkinsForPlane);
            missionSkinSet.addSquadronPersonalSkins(plane.getType(), squadronPersonalSkinsForPlane);
            missionSkinSet.addNonSquadronPersonalSkins(plane.getType(), nonSquadronPersonalSkinsForPlane);
        }
        return missionSkinSet;
    }
    
    private List<Skin> filterSkinSet(List<Skin> skins, Season season) throws PWCGException 
    {
        skins = SkinFilter.skinFilterCountry(skins, flight.getSquadron().getCountry().getCountryName());
        skins = SkinFilter.skinFilterDate(skins, flight.getCampaign().getDate());
        List<Skin> skinsBeforeWinter = new ArrayList<>(skins);
        List<Skin> skinsAfterWinter = new ArrayList<>();
        if (useWinterSkins())
        {
            skinsAfterWinter = SkinFilter.skinFilterSeason(skinsBeforeWinter, season);
        }
        
        if (skinsAfterWinter.isEmpty())
        {
            skins = skinsBeforeWinter;
        }
        else
        {
            skins = skinsAfterWinter;
        }
        
        return skins;

    }

    private boolean useWinterSkins() throws PWCGException
    {
        if (flight.getCampaign().getCampaignMap() == FrontMapIdentifier.BODENPLATTE_MAP     || 
            flight.getCampaign().getCampaignMap() == FrontMapIdentifier.NORMANDY_MAP)
        {
            return false;
        }
            
            
        Date date = flight.getCampaign().getDate();
        if (!DateUtils.isDateInRange(date, DateUtils.getDateYYYYMMDD("19411001"), DateUtils.getDateYYYYMMDD("19430501")))
        {
            return false;
        }

        return true;
    }

}
