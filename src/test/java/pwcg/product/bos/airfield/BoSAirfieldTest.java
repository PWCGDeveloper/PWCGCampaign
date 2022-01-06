package pwcg.product.bos.airfield;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.IPWCGContextManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.Runway;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.options.MissionWeather;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class BoSAirfieldTest
{
    @Mock private ConfigManagerCampaign configManagerCampaign;
    @Mock private Mission mission;
    @Mock private MissionWeather weather;

	private static final String airfieldName = "Bahmutovo";
	private static final String refChart =
	        "    Chart\n" +
	        "    {\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 0;\n" +
	        "        X = 96.34;\n" +
	        "        Y = -245.55;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 105.38;\n" +
	        "        Y = -276.83;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 47.62;\n" +
	        "        Y = -293.34;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -0.97;\n" +
	        "        Y = -298.54;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -270.50;\n" +
	        "        Y = -247.34;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -540.04;\n" +
	        "        Y = -196.15;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -590.24;\n" +
	        "        Y = -150.25;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -599.54;\n" +
	        "        Y = -86.14;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -568.19;\n" +
	        "        Y = -23.01;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -515.79;\n" +
	        "        Y = -2.11;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 2;\n" +
	        "        X = -438.63;\n" +
	        "        Y = -2.03;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 2;\n" +
	        "        X = 438.63;\n" +
	        "        Y = 2.03;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 521.63;\n" +
	        "        Y = -3.20;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 574.28;\n" +
	        "        Y = -27.51;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 602.17;\n" +
	        "        Y = -78.35;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 598.02;\n" +
	        "        Y = -139.77;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 549.35;\n" +
	        "        Y = -192.18;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 436.64;\n" +
	        "        Y = -215.44;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 105.08;\n" +
	        "        Y = -277.50;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 0;\n" +
	        "        X = 96.34;\n" +
	        "        Y = -245.55;\n" +
	        "      }\n" +
	        "    }\n";

	public BoSAirfieldTest() throws PWCGException
	{
		PWCGContext.setProduct(PWCGProduct.BOS);
	}

	@Test
	public void airfieldCheckMoscowTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        campaign.setCampaignConfigManager(configManagerCampaign);
        PWCGContext.getInstance().setCampaign(campaign);

        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AllowAirStartsKey)).thenReturn(0);

        Airfield airfield = (Airfield) PWCGContext.getInstance().getAirfieldAllMaps(airfieldName);

        Mockito.when(mission.getWeather()).thenReturn(weather);
        Mockito.when(weather.getWindDirection()).thenReturn(90);        
        assert(airfield.getChart(mission).equals(refChart));
	}

	@Test
	public void pathTest() throws PWCGException
	{
	    IPWCGContextManager contextManager = PWCGContext.getInstance();

	    for (PWCGMap map : contextManager.getAllMaps())
	    {
	        if (map.getMapIdentifier() == FrontMapIdentifier.BODENPLATTE_MAP || map.getMapIdentifier() == FrontMapIdentifier.EAST1945_MAP || map.getMapIdentifier() == FrontMapIdentifier.STALINGRAD_MAP)
	        {
	            continue;
	        }
	        
	        AirfieldManager airfieldManager = map.getAirfieldManager();
	        if (airfieldManager != null)
	        {
	            for (String airfieldName : airfieldManager.getAllAirfields().keySet())
	            {
	                Airfield airfield = (Airfield) airfieldManager.getAirfield(airfieldName);

	                for (Runway runway : airfield.getAllRunways())
	                {
	                    // Runway start/end should be at least 75m from last taxi point
	                    // otherwise planes can take a long time to get into position
	                    assert(MathUtils.calcDist(runway.getStartPos(), runway.getTaxiToStart().get(runway.getTaxiToStart().size() - 1)) >= 75);
	                    assert(MathUtils.calcDist(runway.getEndPos(), runway.getTaxiFromEnd().get(0)) >= 75);

	                    // Intermediate points on taxi paths should be closer to the next
	                    // point than to the start of the runway, else planes will skip
	                    // the rest of the taxi path
	                    for (int i = 0; i < runway.getTaxiToStart().size() - 1; i++)
	                    {
	                        double distToNext = MathUtils.calcDist(runway.getTaxiToStart().get(i), runway.getTaxiToStart().get(i+1));
	                        double distToRunway = MathUtils.calcDist(runway.getTaxiToStart().get(i), runway.getStartPos());
	                        assert(distToNext < (distToRunway - 5));
	                    }
	                    for (int i = 1; i < runway.getTaxiFromEnd().size(); i++)
	                    {
	                        double distToNext = MathUtils.calcDist(runway.getTaxiFromEnd().get(i), runway.getTaxiFromEnd().get(i-1));
	                        double distToRunway = MathUtils.calcDist(runway.getTaxiFromEnd().get(i), runway.getEndPos());
	                        assert(distToNext < (distToRunway - 5));
	                    }

	                    // Angle from last taxi point to runway should not be too steep
	                    double runwayAngle = MathUtils.calcAngle(runway.getStartPos(), runway.getEndPos());
	                    double angleToRunway = MathUtils.calcAngle(runway.getTaxiToStart().get(runway.getTaxiToStart().size() - 1), runway.getStartPos());
	                    double angleFromRunway = MathUtils.calcAngle(runway.getEndPos(), runway.getTaxiFromEnd().get(0));

	                    assert(MathUtils.calcNumberOfDegrees(angleToRunway, runwayAngle) < 5);
	                    assert(MathUtils.calcNumberOfDegrees(runwayAngle, angleFromRunway) < 5);
	                }
	            }
	        }
	    }
	}
}
