package pwcg.product.rof.map.galicia;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MissionOptions;


public class GaliciaMissionOptions extends MissionOptions
{
    public GaliciaMissionOptions()
    {
        super();

    	makeWinter();
    	makeSpring();
    	makeSummer();
        makeAutumn();
    }

	private void makeWinter()
	{
		winter.setHeightMap("graphics\\landscape_tarnopol_winter\\height.ini");
    	winter.setTextureMap("graphics\\landscape_tarnopol_winter\\textures.ini");
    	winter.setForrestMap("graphics\\landscape_tarnopol_winter\\trees\\woods.wds");
    	winter.setGuiMap("swf\\data\\maps\\tarnopol_winter.info");
    	winter.setSeasonAbbreviation(MapSeasonalParameters.WINTER_ABREV);
    	winter.setSeason(MapSeasonalParameters.WINTER_STRING);
	}

	private void makeSpring()
	{
		spring.setHeightMap("graphics\\landscape_tarnopol\\height.ini");
    	spring.setTextureMap("graphics\\landscape_tarnopol\\textures.ini");
    	spring.setForrestMap("graphics\\landscape_tarnopol\\trees\\woods.wds");
    	spring.setGuiMap("swf\\data\\maps\\tarnopol_summer.info");
    	spring.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	spring.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeSummer()
	{
		summer.setHeightMap("graphics\\landscape_tarnopol\\height.ini");
    	summer.setTextureMap("graphics\\landscape_tarnopol\\textures.ini");
    	summer.setForrestMap("graphics\\landscape_tarnopol\\trees\\woods.wds");
    	summer.setGuiMap("swf\\data\\maps\\tarnopol_summer.info");
    	summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeAutumn()
	{
		autumn.setHeightMap("graphics\\landscape_tarnopol_autumn\\height.ini");
    	autumn.setTextureMap("graphics\\landscape_tarnopol_autumn\\textures.ini");
    	autumn.setForrestMap("graphics\\landscape_tarnopol_autumn\\trees\\woods.wds");
    	autumn.setGuiMap("swf\\data\\maps\\tarnopol_autumn.info");
    	autumn.setSeasonAbbreviation(MapSeasonalParameters.AUTUMN_ABREV);
    	autumn.setSeason(MapSeasonalParameters.AUTUMN_STRING);
	}
}
