package pwcg.mission.options;

public class MapSeasonalParameters
{
    public enum Season
    {
        WINTER,
        SPRING,
        SUMMER,
        AUTUMN
    }

    public static String WINTER_STRING = "winter";
    public static String SPRING_STRING = "spring";
    public static String SUMMER_STRING = "summer";
    public static String AUTUMN_STRING = "autumn";  // Autumn uses the summer prefix

    public static String WINTER_ABREV = "wi";
    public static String SPRING_ABREV = "sp";
    public static String SUMMER_ABREV = "su";
    public static String AUTUMN_ABREV = "au"; // Autumn uses the summer prefix

	private String heightMap = "Oops";
	private String textureMap = "Oops";
	private String forrestMap = "Oops";
	private String guiMap = "Oops";
	private String season = "Oops";
	private String seasonAbbreviation = "Oops";

	public String getHeightMap()
	{
		return heightMap;
	}

	public void setHeightMap(String heightMap)
	{
		this.heightMap = heightMap;
	}

	public String getTextureMap()
	{
		return textureMap;
	}

	public void setTextureMap(String textureMap)
	{
		this.textureMap = textureMap;
	}

	public String getForrestMap()
	{
		return forrestMap;
	}

	public void setForrestMap(String forrestMap)
	{
		this.forrestMap = forrestMap;
	}

	public String getGuiMap()
	{
		return guiMap;
	}

	public void setGuiMap(String guiMap)
	{
		this.guiMap = guiMap;
	}

	public String getSeasonAbbreviation()
	{
		return seasonAbbreviation;
	}

	public void setSeasonAbbreviation(String seasonAbbreviation)
	{
		this.seasonAbbreviation = seasonAbbreviation;
	}

	public String getSeason()
	{
		return season;
	}

	public void setSeason(String season)
	{
		this.season = season;
	}

}
