package pwcg.product.bos.map.bodenplatte;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MissionOptions;


public class BodenplatteMissionOptions extends MissionOptions
{
    public BodenplatteMissionOptions()
    {
        super();

    	makeWinter();
    	makeSpring();
    	makeSummer();
        makeAutumn();
    }

	private void makeWinter()
	{
		winter.setHeightMap("graphics\\LANDSCAPE_Rheinland_su\\height.hini");
    	winter.setTextureMap("graphics\\LANDSCAPE_Rheinland_su\\textures.tini");
    	winter.setForrestMap("graphics\\LANDSCAPE_Rheinland_su\\trees\\woods.wds");
    	winter.setGuiMap("rheinland-summer");
    	winter.setSeasonAbbreviation(MapSeasonalParameters.WINTER_ABREV);
    	winter.setSeason(MapSeasonalParameters.WINTER_STRING);
	}

	private void makeSpring()
	{
	    spring.setHeightMap("graphics\\LANDSCAPE_Rheinland_su\\height.hini");
        spring.setTextureMap("graphics\\LANDSCAPE_Rheinland_su\\textures.tini");
        spring.setForrestMap("graphics\\LANDSCAPE_Rheinland_su\\trees\\woods.wds");
    	spring.setGuiMap("rheinland-summer");
    	spring.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	spring.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeSummer()
	{
	    summer.setHeightMap("graphics\\LANDSCAPE_Rheinland_su\\height.hini");
        summer.setTextureMap("graphics\\LANDSCAPE_Rheinland_su\\textures.tini");
        summer.setForrestMap("graphics\\LANDSCAPE_Rheinland_su\\trees\\woods.wds");
    	summer.setGuiMap("rheinland-summer");
    	summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeAutumn()
	{
	    autumn.setHeightMap("graphics\\LANDSCAPE_Rheinland_su\\height.hini");
        autumn.setTextureMap("graphics\\LANDSCAPE_Rheinland_su\\textures.tini");
        autumn.setForrestMap("graphics\\LANDSCAPE_Rheinland_su\\trees\\woods.wds");
		autumn.setGuiMap("rheinland-summer");
		// Yes, SUMMER is correct!!
    	autumn.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	autumn.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}
}
