package pwcg.mission.skin;

import java.util.List;

import pwcg.campaign.skin.Skin;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.options.MapSeasonalParameters.Season;

public class MissionSkinGenerator 
{
    public static void assignSkinsForFlight(IFlight flight) throws PWCGException
    {
        MissionSkinSet missionSkinSet = buildMissionSkinSet(flight);
                
        if (flight.getFlightInformation().isPlayerFlight())
        {
            MissionPlayerFlightSkinGenerator playerFlightSkinGenerator = new MissionPlayerFlightSkinGenerator(flight, missionSkinSet);
            playerFlightSkinGenerator.applyPlayerSkin();
        }
        else
        {
            MissionAiSkinGenerator aiSkinGenerator = new MissionAiSkinGenerator(flight, missionSkinSet);
            aiSkinGenerator.applyAiSkin();
        }
    }
    
    private static MissionSkinSet buildMissionSkinSet(IFlight flight) throws PWCGException
    {
        if (flight.getCampaign().getSeason() == Season.WINTER)
        {
            MissionSkinSet winterMissionSkinSet = MissionSkinSetBuilder.buildWinterMissionSkinSet(flight);
            if (isWinterSkinSetValid(flight, winterMissionSkinSet))
            {
                return winterMissionSkinSet;
            }
        }
        
        MissionSkinSet missionSkinSet = MissionSkinSetBuilder.buildSummerMissionSkinSet(flight);
        return missionSkinSet;
    }

    private static boolean isWinterSkinSetValid(IFlight flight, MissionSkinSet winterMissionSkinSet)
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            List<Skin> factorySkinSetForPlane = winterMissionSkinSet.getFactorySkins(plane.getType());
            if (factorySkinSetForPlane.size() > 0)
            {
                return false;
            }
        }
        return true;
    }
}
