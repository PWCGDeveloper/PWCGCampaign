package pwcg.mission.skin;

import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinFilter;
import pwcg.campaign.skin.SkinManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.options.MapSeasonalParameters.Season;

public class MissionSkinSetBuilder 
{
    public static MissionSkinSet buildSummerMissionSkinSet(IFlight flight) throws PWCGException
    {
        return buildMissionSkinSetForSeason(flight, Season.SUMMER);
    }
    
    public static MissionSkinSet buildWinterMissionSkinSet(IFlight flight) throws PWCGException
    {
        return buildMissionSkinSetForSeason(flight, Season.WINTER);
    }

    private static MissionSkinSet buildMissionSkinSetForSeason(IFlight flight, Season season) throws PWCGException
    {
        MissionSkinSet missionSkinSet = new MissionSkinSet();

        SkinManager skinManager = PWCGContext.getInstance().getSkinManager();
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            Squadron squadron = flight.getSquadron();
            Date date = flight.getCampaign().getDate();

            List<Skin> factorySkinsForPlane = skinManager.getSkinsBySquadronPlaneDate(plane.getType(), Skin.FACTORY_GENERIC, date);
            factorySkinsForPlane = filterSkinSet(factorySkinsForPlane, flight, season);;

            List<Skin> squadronSkinsForPlane = skinManager.getSquadronSkinsByPlaneSquadronDate(plane.getType(), squadron.getSquadronId(), date);
            squadronSkinsForPlane = filterSkinSet(squadronSkinsForPlane, flight, season);;

            List<Skin> squadronPersonalSkinsForPlane = skinManager.getSkinsByPlaneSquadronDateInUse(plane.getType(), squadron.getSquadronId(), date);
            squadronPersonalSkinsForPlane = filterSkinSet(squadronPersonalSkinsForPlane, flight, season);;

            List<Skin> nonSquadronPersonalSkinsForPlane = skinManager.getPersonalSkinsByPlaneCountryDateInUse(plane.getType(), squadron.determineSquadronCountry(date).getCountryName(), date);
            nonSquadronPersonalSkinsForPlane = filterSkinSet(nonSquadronPersonalSkinsForPlane, flight, season);;

            missionSkinSet.addFactorySkins(plane.getType(), factorySkinsForPlane);
            missionSkinSet.addSquadronSkins(plane.getType(), squadronSkinsForPlane);
            missionSkinSet.addSquadronPersonalSkins(plane.getType(), squadronPersonalSkinsForPlane);
            missionSkinSet.addNonSquadronPersonalSkins(plane.getType(), nonSquadronPersonalSkinsForPlane);
        }
        return missionSkinSet;
    }
    
    private static List<Skin> filterSkinSet(List<Skin> skins, IFlight flight, Season season) 
    {
        skins = SkinFilter.skinFilterCountry(skins, flight.getSquadron().getCountry().getCountryName());
        skins = SkinFilter.skinFilterDate(skins, flight.getCampaign().getDate());
        skins = SkinFilter.skinFilterSeason(skins, season);
        return skins;

    }

}
