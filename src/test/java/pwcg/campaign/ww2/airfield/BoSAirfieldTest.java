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
}
