package pwcg.campaign.ww2.airfield;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

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
			"        X = 92.77;\n" +
			"        Y = -246.09;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 101.15;\n" +
			"        Y = -280.73;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 43.97;\n" +
			"        Y = -293.81;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -4.63;\n" +
			"        Y = -298.92;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -543.52;\n" +
			"        Y = -195.60;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -593.65;\n" +
			"        Y = -149.63;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -602.83;\n" +
			"        Y = -85.50;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -571.38;\n" +
			"        Y = -22.42;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -518.94;\n" +
			"        Y = -1.61;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 2;\n" +
			"        X = -469.69;\n" +
			"        Y = -1.45;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 2;\n" +
			"        X = 469.69;\n" +
			"        Y = -1.44;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 518.47;\n" +
			"        Y = -4.48;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 571.08;\n" +
			"        Y = -28.88;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 598.88;\n" +
			"        Y = -79.77;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 594.63;\n" +
			"        Y = -141.18;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 545.87;\n" +
			"        Y = -193.50;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 107.42;\n" +
			"        Y = -276.63;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 0;\n" +
			"        X = 92.77;\n" +
			"        Y = -246.09;\n" +
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
}
