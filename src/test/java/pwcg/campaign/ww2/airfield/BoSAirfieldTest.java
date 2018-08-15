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
			"        X = 0.00;\n" +
			"        Y = 0.00;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 15.27;\n" +
			"        Y = -32.19;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -38.02;\n" +
			"        Y = -56.68;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -84.56;\n" +
			"        Y = -71.61;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -633.19;\n" +
			"        Y = -80.58;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -691.65;\n" +
			"        Y = -45.82;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -713.75;\n" +
			"        Y = 15.07;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -695.84;\n" +
			"        Y = 83.24;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -648.77;\n" +
			"        Y = 114.33;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 2;\n" +
			"        X = -600.88;\n" +
			"        Y = 125.98;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 2;\n" +
			"        X = 318.67;\n" +
			"        Y = 317.91;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 367.35;\n" +
			"        Y = 323.49;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 423.83;\n" +
			"        Y = 310.36;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 461.45;\n" +
			"        Y = 266.22;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 469.83;\n" +
			"        Y = 205.24;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 432.79;\n" +
			"        Y = 144.06;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 20.58;\n" +
			"        Y = -26.90;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 0;\n" +
			"        X = 0.00;\n" +
			"        Y = 0.00;\n" +
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
