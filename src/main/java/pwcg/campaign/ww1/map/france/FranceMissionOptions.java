package pwcg.campaign.ww1.map.france;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MissionOptions;


public class FranceMissionOptions extends MissionOptions
{
    public FranceMissionOptions()
    {
        super();

    	makeWinter();
    	makeSpring();
    	makeSummer();
        makeAutumn();
    }

	private void makeWinter()
	{
		winter.setHeightMap("graphics\\landscape_winter\\height.ini");
    	winter.setTextureMap("graphics\\landscape_winter\\textures.ini");
    	winter.setForrestMap("graphics\\landscape_winter\\trees\\woods.wds");
    	winter.setGuiMap("swf\\map\\map1");
    	winter.setSeasonAbbreviation(MapSeasonalParameters.WINTER_ABREV);
    	winter.setSeason(MapSeasonalParameters.WINTER_STRING);
	}

	private void makeSpring()
	{
		spring.setHeightMap("graphics\\landscape\\height.ini");
    	spring.setTextureMap("graphics\\landscape\\textures.ini");
    	spring.setForrestMap("graphics\\landscape\\trees\\woods.wds");
    	spring.setGuiMap("swf\\map\\map1");
    	spring.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	spring.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeSummer()
	{
		summer.setHeightMap("graphics\\landscape\\height.ini");
    	summer.setTextureMap("graphics\\landscape\\textures.ini");
    	summer.setForrestMap("graphics\\landscape\\trees\\woods.wds");
    	summer.setGuiMap("swf\\map\\map1");
    	summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeAutumn()
	{
		autumn.setHeightMap("graphics\\landscape_autumn\\height.ini");
    	autumn.setTextureMap("graphics\\landscape_autumn\\textures.ini");
    	autumn.setForrestMap("graphics\\landscape_autumn\\trees\\woods.wds");
    	autumn.setGuiMap("swf\\map\\map1");
    	autumn.setSeasonAbbreviation(MapSeasonalParameters.AUTUMN_ABREV);
    	autumn.setSeason(MapSeasonalParameters.AUTUMN_STRING);
	}
}
