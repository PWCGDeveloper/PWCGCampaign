package pwcg.product.fc.map.arras;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.product.bos.map.IMapSeason;
import pwcg.product.bos.map.MapSeasonBase;


public class ArrasMapSeason extends MapSeasonBase implements IMapSeason
{
    public ArrasMapSeason()
    {
        super();
    }

	protected void makeWinter()
	{
		winter.setHeightMap("graphics\\LANDSCAPE_Arras_sp\\height.hini");
    	winter.setTextureMap("graphics\\LANDSCAPE_Arras_sp\\textures.tini");
    	winter.setForrestMap("graphics\\LANDSCAPE_Arras_sp\\trees\\woods.wds");
    	winter.setGuiMap("arras-spring");
    	winter.setSeasonAbbreviation(MapSeasonalParameters.WINTER_ABREV);
    	winter.setSeason(MapSeasonalParameters.WINTER_STRING);
	}

	protected void makeSpring()
	{
	    spring.setHeightMap("graphics\\LANDSCAPE_Arras_sp\\height.hini");
        spring.setTextureMap("graphics\\LANDSCAPE_Arras_sp\\textures.tini");
        spring.setForrestMap("graphics\\LANDSCAPE_Arras_sp\\trees\\woods.wds");
        spring.setGuiMap("arras-spring");
    	spring.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	spring.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	protected void makeSummer()
	{
	    summer.setHeightMap("graphics\\LANDSCAPE_Arras_sp\\height.hini");
        summer.setTextureMap("graphics\\LANDSCAPE_Arras_sp\\textures.tini");
        summer.setForrestMap("graphics\\LANDSCAPE_Arras_sp\\trees\\woods.wds");
        summer.setGuiMap("arras-spring");
    	summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	protected void makeAutumn()
	{
	    autumn.setHeightMap("graphics\\LANDSCAPE_Arras_sp\\height.hini");
        autumn.setTextureMap("graphics\\LANDSCAPE_Arras_sp\\textures.tini");
        autumn.setForrestMap("graphics\\LANDSCAPE_Arras_sp\\trees\\woods.wds");
        autumn.setGuiMap("arras-spring");
    	autumn.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	autumn.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}
}
