package pwcg.campaign.ww2.airfield;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.IPWCGContextManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.ww2.airfield.BoSAirfield.Runway;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

@RunWith(MockitoJUnitRunner.class)
public class BoSAirfieldTest
{
	private static final String airfieldName = "Bahmutovo";
	private static final String refChart =
	        "    Chart\n" +
	        "    {\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 0;\n" +
	        "        X = 93.61;\n" +
	        "        Y = -245.80;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 102.58;\n" +
	        "        Y = -277.11;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 44.78;\n" +
	        "        Y = -293.49;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -3.82;\n" +
	        "        Y = -298.57;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -542.65;\n" +
	        "        Y = -194.92;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -592.75;\n" +
	        "        Y = -148.91;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -601.89;\n" +
	        "        Y = -84.78;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -570.40;\n" +
	        "        Y = -21.72;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -517.95;\n" +
	        "        Y = -0.95;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 2;\n" +
	        "        X = -466.69;\n" +
	        "        Y = -1.46;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 2;\n" +
	        "        X = 466.69;\n" +
	        "        Y = -1.43;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 519.46;\n" +
	        "        Y = -4.45;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 572.05;\n" +
	        "        Y = -28.88;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 599.83;\n" +
	        "        Y = -79.78;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 595.54;\n" +
	        "        Y = -141.19;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 546.74;\n" +
	        "        Y = -193.48;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 102.28;\n" +
	        "        Y = -277.77;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 0;\n" +
	        "        X = 93.61;\n" +
	        "        Y = -245.80;\n" +
	        "      }\n" +
	        "    }\n";

	@Before
	public void setup() throws PWCGException
	{
		PWCGContextManager.setRoF(false);
	}

	@Test
	public void airfieldCheckMoscowTest() throws PWCGException
	{
        BoSAirfield airfield = (BoSAirfield) PWCGContextManager.getInstance().getAirfieldAllMaps(airfieldName);

		PWCGContextManager.getInstance().getCurrentMap().getMapWeather().setWindDirection(180);
        assert(airfield.getChart().equals(refChart));
	}

	@Test
	public void windTest() throws PWCGException
	{
		BoSAirfield airfield = (BoSAirfield) PWCGContextManager.getInstance().getAirfieldAllMaps("Rogachevko");

		PWCGContextManager.getInstance().getCurrentMap().getMapWeather().setWindDirection(0);
		assert((int) airfield.getTakeoffLocation().getOrientation().getyOri() == 225);

		PWCGContextManager.getInstance().getCurrentMap().getMapWeather().setWindDirection(90);
		assert((int) airfield.getTakeoffLocation().getOrientation().getyOri() == 278);

		PWCGContextManager.getInstance().getCurrentMap().getMapWeather().setWindDirection(180);
		assert((int) airfield.getTakeoffLocation().getOrientation().getyOri() == 45);

		PWCGContextManager.getInstance().getCurrentMap().getMapWeather().setWindDirection(270);
		assert((int) airfield.getTakeoffLocation().getOrientation().getyOri() == 98);
	}

	@Test
	public void pathTest() throws PWCGException
	{
	    IPWCGContextManager contextManager = PWCGContextManager.getInstance();

	    for (PWCGMap map : contextManager.getAllMaps())
	    {
	        AirfieldManager airfieldManager = map.getAirfieldManager();
	        if (airfieldManager != null)
	        {
	            for (String airfieldName : airfieldManager.getAllAirfields().keySet())
	            {
	                BoSAirfield airfield = (BoSAirfield) airfieldManager.getAirfield(airfieldName);

	                for (Runway runway : airfield.getAllRunways())
	                {
	                    // Runway start/end should be at least 50m from last taxi point
	                    // otherwise planes can take a long time to get into position
	                    assert(MathUtils.calcDist(runway.startPos, runway.taxiToStart.get(runway.taxiToStart.size() - 1)) >= 50);
	                    assert(MathUtils.calcDist(runway.endPos, runway.taxiFromEnd.get(0)) >= 50);

	                    // Intermediate points on taxi paths should be closer to the next
	                    // point than to the start of the runway, else planes will skip
	                    // the rest of the taxi path
	                    for (int i = 0; i < runway.taxiToStart.size() - 1; i++)
	                    {
	                        double distToNext = MathUtils.calcDist(runway.taxiToStart.get(i), runway.taxiToStart.get(i+1));
	                        double distToRunway = MathUtils.calcDist(runway.taxiToStart.get(i), runway.startPos);
	                        assert(distToNext < distToRunway);
	                    }
	                    for (int i = 1; i < runway.taxiFromEnd.size(); i++)
	                    {
	                        double distToNext = MathUtils.calcDist(runway.taxiFromEnd.get(i), runway.taxiFromEnd.get(i-1));
	                        double distToRunway = MathUtils.calcDist(runway.taxiFromEnd.get(i), runway.endPos);
	                        assert(distToNext < distToRunway);
	                    }

	                    // Angle from last taxi point to runway should not be too steep
	                    double runwayAngle = MathUtils.calcAngle(runway.startPos, runway.endPos);
	                    double angleToRunway = MathUtils.calcAngle(runway.taxiToStart.get(runway.taxiToStart.size() - 1), runway.startPos);
	                    double angleFromRunway = MathUtils.calcAngle(runway.endPos, runway.taxiFromEnd.get(0));

	                    assert(MathUtils.calcNumberOfDegrees(angleToRunway, runwayAngle) < 45);
	                    assert(MathUtils.calcNumberOfDegrees(runwayAngle, angleFromRunway) < 45);
	                }
	            }
	        }
	    }
	}
}
