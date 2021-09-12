package pwcg.gui.utils;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;

public class ScrollBarWrapper
{


    /**
     * @param outerPanel
     * @param innerGrid
     * @param skinNames
     */
    public static void createScrollPaneWithMax(Pane outerPanel, Pane innerGrid, int itemSize, int maxItems)
    {
        // If we only have a few skins pack them in the NORTH part of the Panel
        if (itemSize < maxItems)
        {
            outerPanel.add(innerGrid, BorderLayout.NORTH);
        }
        // More skins and we need the scroll bar.  
        // Scroll bar is only working in the CENTER of the layout
        else
        {
            // Wrap the grid in a scroll pane
            // Scroll pane always has to be center
            JScrollPane scrollPane = makeScrollPane(innerGrid);
            outerPanel.add(scrollPane, BorderLayout.CENTER);
            
            Pane skinSelectScrollPanel = new Pane(new BorderLayout());
            skinSelectScrollPanel.setOpaque(false);
            skinSelectScrollPanel.add(scrollPane, BorderLayout.CENTER);
    
            outerPanel.add(skinSelectScrollPanel, BorderLayout.CENTER);
        }
    }   
 

    /**
     * @param skinButtonGridPanelSquadron
     * @return
     */
    public static JScrollPane makeScrollPane(JComponent innerComponent)   
    {
        JScrollPane scrollPane = new JScrollPane(innerComponent);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        return scrollPane;
    }



    /**
     * @param skinButtonGridPanelSquadron
     * @return
     */
    public static JScrollPane makeScrollPaneForWrappingText(JComponent innerComponent)   
    {
        JScrollPane scrollPane = new JScrollPane(innerComponent);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        return scrollPane;
    }


}
