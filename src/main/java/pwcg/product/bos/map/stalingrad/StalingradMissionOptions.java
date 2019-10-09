package pwcg.product.bos.map.stalingrad;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MissionOptions;


public class StalingradMissionOptions extends MissionOptions
{
    public StalingradMissionOptions()
    {
        super();

    	makeWinter();
    	makeSpring();
    	makeSummer();
        makeAutumn();
    }

	private void makeWinter()
	{
		winter.setHeightMap("graphics\\LANDSCAPE_Stalin_w\\height.hini");
    	winter.setTextureMap("graphics\\LANDSCAPE_Stalin_w\\textures.tini");
    	winter.setForrestMap("graphics\\LANDSCAPE_Stalin_w\\trees\\woods.wds");
    	winter.setGuiMap("stalingrad-1942");
    	winter.setSeasonAbbreviation(MapSeasonalParameters.WINTER_ABREV);
    	winter.setSeason(MapSeasonalParameters.WINTER_STRING);
	}

	private void makeSpring()
	{
		spring.setHeightMap("graphics\\LANDSCAPE_Stalin_s\\height.hini");
		spring.setTextureMap("graphics\\LANDSCAPE_Stalin_s\\textures.tini");
		spring.setForrestMap("graphics\\LANDSCAPE_Stalin_s\\trees\\woods.wds");
    	spring.setGuiMap("stalingrad-summer-1942");
    	spring.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	spring.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeSummer()
	{
		summer.setHeightMap("graphics\\LANDSCAPE_Stalin_s\\height.hini");
		summer.setTextureMap("graphics\\LANDSCAPE_Stalin_s\\textures.tini");
		summer.setForrestMap("graphics\\LANDSCAPE_Stalin_s\\trees\\woods.wds");
    	summer.setGuiMap("stalingrad-summer-1942");
    	summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeAutumn()
	{
		autumn.setHeightMap("graphics\\LANDSCAPE_Stalin_a\\height.hini");
		autumn.setTextureMap("graphics\\LANDSCAPE_Stalin_a\\textures.tini");
		autumn.setForrestMap("graphics\\LANDSCAPE_Stalin_a\\trees\\woods.wds");
		autumn.setGuiMap("stalingrad-autumn-1942");
		// Yes, SUMMER is correct!!
    	autumn.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	autumn.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}
}
