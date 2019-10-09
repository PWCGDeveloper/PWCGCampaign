package pwcg.product.bos.airfield;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.IPWCGContextManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.AirfieldManager;
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
	        "        X = 94.18;\n" +
	        "        Y = -245.08;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 103.22;\n" +
	        "        Y = -276.37;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 45.45;\n" +
	        "        Y = -292.88;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -3.13;\n" +
	        "        Y = -298.08;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -542.20;\n" +
	        "        Y = -195.68;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -592.41;\n" +
	        "        Y = -149.79;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -601.70;\n" +
	        "        Y = -85.68;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -570.35;\n" +
	        "        Y = -22.55;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = -517.96;\n" +
	        "        Y = -1.65;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 2;\n" +
	        "        X = -466.68;\n" +
	        "        Y = -1.58;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 2;\n" +
	        "        X = 466.68;\n" +
	        "        Y = 1.58;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 519.46;\n" +
	        "        Y = -2.74;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 572.11;\n" +
	        "        Y = -27.05;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 600.00;\n" +
	        "        Y = -77.89;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 595.85;\n" +
	        "        Y = -139.31;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 547.18;\n" +
	        "        Y = -191.71;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 1;\n" +
	        "        X = 102.91;\n" +
	        "        Y = -277.03;\n" +
	        "      }\n" +
	        "      Point\n" +
	        "      {\n" +
	        "        Type = 0;\n" +
	        "        X = 94.18;\n" +
	        "        Y = -245.08;\n" +
	        "      }\n" +
	        "    }\n";

	@Before
	public void setup() throws PWCGException
	{
		PWCGContext.setProduct(PWCGProduct.BOS);
	}

	@Test
	public void airfieldCheckMoscowTest() throws PWCGException
	{
        BoSAirfield airfield = (BoSAirfield) PWCGContext.getInstance().getAirfieldAllMaps(airfieldName);

		PWCGContext.getInstance().getCurrentMap().getMapWeather().setWindDirection(180);
        assert(airfield.getChart().equals(refChart));
	}

	@Test
	public void windTest() throws PWCGException
	{
		BoSAirfield airfield = (BoSAirfield) PWCGContext.getInstance().getAirfieldAllMaps("Rogachevko");

		PWCGContext.getInstance().getCurrentMap().getMapWeather().setWindDirection(0);
		assert((int) airfield.getTakeoffLocation().getOrientation().getyOri() == 225);

		PWCGContext.getInstance().getCurrentMap().getMapWeather().setWindDirection(90);
		assert((int) airfield.getTakeoffLocation().getOrientation().getyOri() == 278);

		PWCGContext.getInstance().getCurrentMap().getMapWeather().setWindDirection(180);
		assert((int) airfield.getTakeoffLocation().getOrientation().getyOri() == 45);

		PWCGContext.getInstance().getCurrentMap().getMapWeather().setWindDirection(270);
		assert((int) airfield.getTakeoffLocation().getOrientation().getyOri() == 98);
	}

	@Test
	public void pathTest() throws PWCGException
	{
	    IPWCGContextManager contextManager = PWCGContext.getInstance();

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
	                    assert(MathUtils.calcDist(runway.getStartPos(), runway.getTaxiToStart().get(runway.getTaxiToStart().size() - 1)) >= 50);
	                    assert(MathUtils.calcDist(runway.getEndPos(), runway.getTaxiFromEnd().get(0)) >= 50);

	                    // Intermediate points on taxi paths should be closer to the next
	                    // point than to the start of the runway, else planes will skip
	                    // the rest of the taxi path
	                    for (int i = 0; i < runway.getTaxiToStart().size() - 1; i++)
	                    {
	                        double distToNext = MathUtils.calcDist(runway.getTaxiToStart().get(i), runway.getTaxiToStart().get(i+1));
	                        double distToRunway = MathUtils.calcDist(runway.getTaxiToStart().get(i), runway.getStartPos());
	                        assert(distToNext < distToRunway);
	                    }
	                    for (int i = 1; i < runway.getTaxiFromEnd().size(); i++)
	                    {
	                        double distToNext = MathUtils.calcDist(runway.getTaxiFromEnd().get(i), runway.getTaxiFromEnd().get(i-1));
	                        double distToRunway = MathUtils.calcDist(runway.getTaxiFromEnd().get(i), runway.getEndPos());
	                        assert(distToNext < distToRunway);
	                    }

	                    // Angle from last taxi point to runway should not be too steep
	                    double runwayAngle = MathUtils.calcAngle(runway.getStartPos(), runway.getEndPos());
	                    double angleToRunway = MathUtils.calcAngle(runway.getTaxiToStart().get(runway.getTaxiToStart().size() - 1), runway.getStartPos());
	                    double angleFromRunway = MathUtils.calcAngle(runway.getEndPos(), runway.getTaxiFromEnd().get(0));

	                    assert(MathUtils.calcNumberOfDegrees(angleToRunway, runwayAngle) < 45);
	                    assert(MathUtils.calcNumberOfDegrees(runwayAngle, angleFromRunway) < 45);
	                }
	            }
	        }
	    }
	}
}
