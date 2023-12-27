package pwcg.product.fc.map.westernfront;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.product.bos.map.IMapSeason;
import pwcg.product.bos.map.MapSeasonBase;


public class WesternFrontMapSeason extends MapSeasonBase implements IMapSeason
{
    public WesternFrontMapSeason()
    {
        super();
    }

	protected void makeWinter()
	{
		winter.setHeightMap("graphics\\landscape_westernfront_wi\\height.hini");
    	winter.setTextureMap("graphics\\landscape_westernfront_wi\\textures.tini");
    	winter.setForrestMap("graphics\\landscape_westernfront_wi\\trees\\woods.wds");
    	winter.setGuiMap("western_front-spring");
    	winter.setSeasonAbbreviation(MapSeasonalParameters.WINTER_ABREV);
    	winter.setSeason(MapSeasonalParameters.WINTER_STRING);
	}

	protected void makeSpring()
	{
	    spring.setHeightMap("graphics\\landscape_westernfront_sp\\height.hini");
        spring.setTextureMap("graphics\\landscape_westernfront_sp\\textures.tini");
        spring.setForrestMap("graphics\\landscape_westernfront_sp\\trees\\woods.wds");
        spring.setGuiMap("western_front-spring");
    	spring.setSeasonAbbreviation(MapSeasonalParameters.SPRING_ABREV);
    	spring.setSeason(MapSeasonalParameters.SPRING_STRING);
	}

	protected void makeSummer()
	{
	    summer.setHeightMap("graphics\\landscape_westernfront_su\\height.hini");
	    summer.setTextureMap("graphics\\landscape_westernfront_su\\textures.tini");
        summer.setForrestMap("graphics\\landscape_westernfront_su\\trees\\woods.wds");
        summer.setGuiMap("western_front-spring");
    	summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	protected void makeAutumn()
	{
	    autumn.setHeightMap("graphics\\landscape_westernfront_au\\height.hini");
        autumn.setTextureMap("graphics\\landscape_westernfront_au\\textures.tini");
        autumn.setForrestMap("graphics\\landscape_westernfront_au\\trees\\woods.wds");
        autumn.setGuiMap("western_front-spring");
    	autumn.setSeasonAbbreviation(MapSeasonalParameters.AUTUMN_ABREV);
    	autumn.setSeason(MapSeasonalParameters.AUTUMN_STRING);
	}
}
