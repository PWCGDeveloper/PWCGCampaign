package pwcg.gui.rofmap.brief;

public class PwcgGuiModSupport
{
    
    private static boolean runningIntegrated = false;
    private static boolean runningDebrief = false;

    public static boolean isRunningIntegrated() {
        return PwcgGuiModSupport.runningIntegrated;
    }

    public static void setRunningIntegrated(boolean runningIntegrated) {
        PwcgGuiModSupport.runningIntegrated = runningIntegrated;
    }

    public static boolean isRunningDebrief() {
        return PwcgGuiModSupport.runningDebrief;
    }

    public static void setRunningDebrief(boolean runningDebrief) {
        PwcgGuiModSupport.runningDebrief = runningDebrief;
    }

}
