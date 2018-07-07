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
			"        X = 558.52;\n" +
			"        Y = -254.92;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 566.35;\n" +
			"        Y = -289.68;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 508.97;\n" +
			"        Y = -301.86;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 460.29;\n" +
			"        Y = -306.21;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -76.90;\n" +
			"        Y = -194.42;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -126.30;\n" +
			"        Y = -147.66;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -134.47;\n" +
			"        Y = -83.39;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -102.03;\n" +
			"        Y = -20.82;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = -49.27;\n" +
			"        Y = -0.84;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 2;\n" +
			"        X = 0.00;\n" +
			"        Y = 0.00;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 2;\n" +
			"        X = 939.26;\n" +
			"        Y = -14.78;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 987.97;\n" +
			"        Y = -20.03;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 1040.18;\n" +
			"        Y = -45.26;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 1067.18;\n" +
			"        Y = -96.58;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 1061.97;\n" +
			"        Y = -157.92;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 1012.38;\n" +
			"        Y = -209.46;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 1;\n" +
			"        X = 572.69;\n" +
			"        Y = -285.68;\n" +
			"      }\n" +
			"      Point\n" +
			"      {\n" +
			"        Type = 0;\n" +
			"        X = 558.52;\n" +
			"        Y = -254.92;\n" +
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

        assert(airfield.getChart().equals(refChart));
	}
}
