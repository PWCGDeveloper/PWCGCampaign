package pwcg.product.bos.map.normandy;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.product.bos.map.IMapSeason;
import pwcg.product.bos.map.MapSeasonBase;

public class NormandyMapSeason extends MapSeasonBase implements IMapSeason
{
    public NormandyMapSeason()
    {
        super();
    }

    protected void makeWinter()
    {
        winter.setHeightMap("graphics\\LANDSCAPE_Normandy_su\\height.hini");
        winter.setTextureMap("graphics\\LANDSCAPE_Normandy_su\\textures.tini");
        winter.setForrestMap("graphics\\LANDSCAPE_Normandy_su\\trees\\woods.wds");
        winter.setGuiMap("normandy-summer-early");
        winter.setSeasonAbbreviation(MapSeasonalParameters.WINTER_ABREV);
        winter.setSeason(MapSeasonalParameters.WINTER_STRING);
    }

    protected void makeSpring()
    {
        spring.setHeightMap("graphics\\LANDSCAPE_Normandy_su\\height.hini");
        spring.setTextureMap("graphics\\LANDSCAPE_Normandy_su\\textures.tini");
        spring.setForrestMap("graphics\\LANDSCAPE_Normandy_su\\trees\\woods.wds");
        spring.setGuiMap("normandy-summer-early");
        spring.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
        spring.setSeason(MapSeasonalParameters.SUMMER_STRING);
    }

    protected void makeSummer()
    {
        summer.setHeightMap("graphics\\LANDSCAPE_Normandy_su\\height.hini");
        summer.setTextureMap("graphics\\LANDSCAPE_Normandy_su\\textures.tini");
        summer.setForrestMap("graphics\\LANDSCAPE_Normandy_su\\trees\\woods.wds");
        summer.setGuiMap("normandy-summer-early");
        summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
        summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
    }

    protected void makeSummerPostInvasion()
    {
        makeSummer();
        summer.setGuiMap("normandy-summer");
    }

    protected void makeAutumn()
    {
        autumn.setHeightMap("graphics\\LANDSCAPE_Normandy_su\\height.hini");
        autumn.setTextureMap("graphics\\LANDSCAPE_Normandy_su\\textures.tini");
        autumn.setForrestMap("graphics\\LANDSCAPE_Normandy_su\\trees\\woods.wds");
        autumn.setGuiMap("normandy-summer-early");
        autumn.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
        autumn.setSeason(MapSeasonalParameters.SUMMER_STRING);
    }
}
