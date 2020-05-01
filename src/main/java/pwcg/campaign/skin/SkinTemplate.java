package pwcg.campaign.skin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.DDSWriter;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PointsAlongCurve;
import pwcg.gui.dialogs.FontCache;
import pwcg.gui.dialogs.ImageCache;

public class SkinTemplate {
    enum HorizAlign {
        LEFT,
        CENTER,
        RIGHT
    }

    enum VertAlign {
        TOP,
        CENTER,
        BOTTOM
    }

    public static class ElementDef
    {
        public String text;
        public String image;
        public HorizAlign horizAlign = HorizAlign.LEFT;
        public VertAlign vertAlign = VertAlign.BOTTOM;
        public String font;
        public float size;
        public int x;
        public int y;
        public int width;
        public int height;
        public float orientation = 0;
        public boolean horizFlip = false;
        public String fillColor;
        public int strokeWidth = 0;
        public String strokeColor;
        public String fillShine;
        public String strokeShine;
        public Integer x2;
        public Integer y2;
        public Integer cx1;
        public Integer cy1;
        public Integer cx2;
        public Integer cy2;
    }

    private String templateName;
    private String planeType;
    @SuppressWarnings("unused") // credit isn't used in PWCG, but is stored in the templates
    private String credit;
    private LinkedHashMap<String,Object> parameters;
    private String baseImagePath;
    private String weatherImagePath;
    private ElementDef[] defs;

    public class SkinTemplateInstance
    {
        private Object[] values;
        private BufferedImage skinImage;

        public SkinTemplateInstance(Map<String, Object> overrides)
        {
            values = new Object[parameters.size()];
            int index = 0;

            for (String param : parameters.keySet())
            {
                if (overrides != null && overrides.containsKey(param))
                    values[index] = overrides.get(param);
                else
                    values[index] = parameters.get(param);
                index++;
            }

        }

        public void generate() throws PWCGException {
            render();
            write();
            // Clear reference to image to avoid using too much heap while generating
            skinImage = null;
        }

        public void write() throws PWCGIOException {
            String fileName = getFilename();
            String skinDir = Skin.PRODUCT_SKIN_DIR + "\\" + planeType;
            FileUtils.createDirIfNeeded(skinDir);

            DDSWriter.writeImage(skinImage, new File(skinDir + "\\" + fileName));
        }

        public void render() throws PWCGException {
            ImageCache imageCache = ImageCache.getInstance();
            FontCache fontCache = FontCache.getInstance();
            String skinTemplatesDir = PWCGContext.getInstance().getDirectoryManager().getPwcgSkinTemplatesDir();
            String imagesDir = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir();
            String fontsDir = PWCGContext.getInstance().getDirectoryManager().getPwcgFontsDir();

            skinImage = imageCache.getBufferedImage(skinTemplatesDir + MessageFormat.format(baseImagePath, values));
            skinImage = new BufferedImage(skinImage.getColorModel(), skinImage.copyData(skinImage.getRaster().createCompatibleWritableRaster()), false, null);

            BufferedImage skin = getColorImage();
            BufferedImage shineMap = getShineImage();

            Graphics2D graphics = skin.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            Graphics2D shineGraphics = shineMap.createGraphics();
            shineGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            shineGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            AffineTransform origTransform = graphics.getTransform();

            for (ElementDef def : defs) {
                Rectangle2D bounds;

                if (def.text != null)
                {
                    String text = MessageFormat.format(def.text, values);
                    if (text.equals(""))
                        continue;

                    Font font = fontCache.getFont(fontsDir + def.font, def.size);

                    // Java seems to get weird values for a font's ascent/descent metrics, so measure the letter "M" instead
                    Rectangle2D mBounds = font.createGlyphVector(graphics.getFontRenderContext(), "M").getVisualBounds();

                    GlyphVector glyphVector = font.layoutGlyphVector(graphics.getFontRenderContext(), text.toCharArray(), 0, text.length(), Font.LAYOUT_LEFT_TO_RIGHT);
                    bounds = glyphVector.getVisualBounds();
                    Shape outline;
                    if (def.x2 != null && def.y2 != null && def.cx1 != null && def.cy1 != null && def.cx2 != null && def.cy2 != null) {
                        Area stringArea = new Area();
                        PointsAlongCurve pac = new PointsAlongCurve(new CubicCurve2D.Float(def.x, def.y, def.cx1, def.cy1, def.cx2, def.cy2, def.x2, def.y2));

                        for (int i = 0; i < glyphVector.getNumGlyphs(); i++)
                        {
                            Rectangle2D glyphBounds = glyphVector.getGlyphVisualBounds(i).getBounds2D();
                            double glyphX;
                            if (def.horizAlign == HorizAlign.RIGHT)
                                glyphX = glyphBounds.getMaxX() - bounds.getWidth() + pac.getLength();
                            else if (def.horizAlign == HorizAlign.CENTER)
                                glyphX = glyphBounds.getCenterX() - bounds.getWidth() / 2.0 + pac.getLength() / 2.0;
                            else
                                glyphX = glyphBounds.getX();
                            PWCGLocation glyphPosOrient = pac.getPoint(glyphX);

                            glyphBounds = new Rectangle2D.Double(glyphBounds.getX(), mBounds.getY(), glyphBounds.getWidth(), mBounds.getHeight());
                            Area glyph = new Area(glyphVector.getGlyphOutline(i));
                            glyph.transform(getTransform(glyphBounds, def.horizAlign, def.vertAlign, glyphPosOrient.getPosition().getXPos(), glyphPosOrient.getPosition().getYPos(), glyphPosOrient.getOrientation().getyOri(), def.horizFlip, 1));
                            stringArea.add(glyph);
                        }

                        outline = stringArea;
                    } else {
                        outline = glyphVector.getOutline();
                        bounds = new Rectangle2D.Double(bounds.getX(), mBounds.getY(), bounds.getWidth(), mBounds.getHeight());

                        graphics.setTransform(getTransform(bounds, def.horizAlign, def.vertAlign, def.x, def.y, def.orientation, def.horizFlip, 1));
                        shineGraphics.setTransform(graphics.getTransform());
                    }

                    Color fillColor = Color.decode("0x" + MessageFormat.format(def.fillColor, values));
                    graphics.setColor(fillColor);
                    graphics.fill(outline);

                    if (def.fillShine != null) {
                        Color fillShine = Color.decode("0x" + MessageFormat.format(def.fillShine + def.fillShine + def.fillShine, values));
                        shineGraphics.setColor(fillShine);
                        shineGraphics.fill(outline);
                    }

                    if (def.strokeWidth > 0) {
                        Color strokeColor = Color.decode("0x" + MessageFormat.format(def.strokeColor, values));
                        graphics.setColor(strokeColor);
                        graphics.setStroke(new BasicStroke(def.strokeWidth));
                        graphics.draw(outline);

                        if (def.strokeShine != null) {
                            Color strokeShine = Color.decode("0x" + MessageFormat.format(def.strokeShine + def.strokeShine + def.strokeShine, values));
                            shineGraphics.setColor(strokeShine);
                            shineGraphics.setStroke(new BasicStroke(def.strokeWidth));
                            shineGraphics.draw(outline);
                        }
                    }
                } else {
                    String imagePath = MessageFormat.format(def.image, values);
                    if (imagePath == null || imagePath.equals("") || imagePath.equals("null"))
                        continue;
                    BufferedImage image = imageCache.getBufferedImage(imagesDir + imagePath);
                    if (image == null)
                        continue;
                    bounds = new Rectangle(0, -image.getHeight(), image.getWidth(), image.getHeight());
                    double width  = (def.width != 0) ? def.width : image.getWidth();
                    double height = (def.height != 0) ? def.height : image.getHeight();
                    double scalingFactor = Math.min((double) width / image.getWidth(), (double) height / image.getHeight());

                    BufferedImageOp op = null;

                    if (def.fillColor != null) {
                        String colorString = MessageFormat.format(def.fillColor, values);
                        if (colorString == null || colorString.equals("") || colorString.equals("null"))
                            continue;
                        Color fillColor = Color.decode("0x" + colorString);

                        op = new RescaleOp(new float[] {fillColor.getRed() / 255f, fillColor.getGreen() / 255f, fillColor.getBlue() / 255f, 1f}, new float[] {0f, 0f, 0f, 0f}, null);
                    }

                    graphics.setTransform(getTransform(bounds, def.horizAlign, def.vertAlign, def.x, def.y, def.orientation, def.horizFlip, scalingFactor));
                    shineGraphics.setTransform(graphics.getTransform());

                    graphics.drawImage(image, op, 0, -image.getHeight());

                    if (def.fillShine != null) {
                        int shineVal = Integer.parseUnsignedInt(MessageFormat.format(def.fillShine, values), 16);

                        op = new RescaleOp(new float[] {0, 0, 0, 1}, new float[] {shineVal, shineVal, shineVal, 0}, null);
                        shineGraphics.drawImage(image, op, 0, -image.getHeight());
                    }
                }

                graphics.setTransform(origTransform);
            }

            BufferedImage weatherImage = imageCache.getBufferedImage(skinTemplatesDir + MessageFormat.format(weatherImagePath, values));
            graphics.drawImage(weatherImage, 0, 0, null);

            graphics.dispose();
        }

        public BufferedImage getShineImage() {
            WritableRaster alphaRaster = skinImage.getAlphaRaster();
            // Create our own 1:1 lookup table for the shininess map, as otherwise Java does weird gamma correction
            byte[] lut = new byte[256];
            for (int i = 0; i < 256; i++)
                lut[i] = (byte) i;
            ColorModel shineColorModel = new IndexColorModel(8, 256, lut, lut, lut);
            BufferedImage shineMap = new BufferedImage(shineColorModel, alphaRaster, false, null);
            return shineMap;
        }

        public BufferedImage getColorImage() {
            if (skinImage != null) {
                // Alpha channel of the base image contains the shininess map, which should
                // be unchanged. Create a new image backed by the same data arrays, but only
                // using the color components.
                int[] bandList = {0, 1, 2};
                WritableRaster colorOnlyRaster = skinImage.getRaster().createWritableChild(
                                                     0, 0,
                                                     skinImage.getWidth(), skinImage.getHeight(),
                                                     0, 0,
                                                     bandList);

                ColorModel colorModel = new ComponentColorModel(skinImage.getColorModel().getColorSpace(), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
                BufferedImage skin = new BufferedImage(colorModel, colorOnlyRaster, false, null);
                return skin;
            } else
                return null;
        }

        private AffineTransform getTransform(Rectangle2D bounds, HorizAlign hAlign, VertAlign vAlign, double x, double y, double orientation, boolean horizFlip, double scalingFactor) {
            int xOffset = 0;
            int yOffset = 0;

            if (hAlign == HorizAlign.RIGHT) {
                xOffset = (int) -(bounds.getWidth() + bounds.getX());
            } else if (hAlign == HorizAlign.CENTER) {
                xOffset = (int) -(bounds.getWidth() / 2 + bounds.getX());
            } else if (hAlign == HorizAlign.LEFT) {
                xOffset = (int) -bounds.getX();
            }

            if (vAlign == VertAlign.TOP) {
                yOffset = (int) -bounds.getY();
            } else if (vAlign == VertAlign.CENTER) {
                yOffset = (int) -(bounds.getHeight() / 2 + bounds.getY());
            } else if (vAlign == VertAlign.BOTTOM) {
                yOffset = (int) -(bounds.getHeight() + bounds.getY());
            }

            AffineTransform transform = new AffineTransform();
            transform.translate(x, y);
            transform.rotate(Math.toRadians(orientation));
            transform.scale(scalingFactor, scalingFactor);
            if (horizFlip)
                transform.scale(-1, 1);
            transform.translate(xOffset, yOffset);

            return transform;
        }

        public String getFilename()
        {
            return "PWCG_" + templateName + "_" + Integer.toHexString(Arrays.deepHashCode(values)) + ".dds";
        }

        public boolean skinExists()
        {
            File file = new File(Skin.PRODUCT_SKIN_DIR + "\\" + planeType + "\\" + getFilename());
            return file.exists();
        }
    }

    public SkinTemplateInstance instantiate(Map<String, Object> overrides)
    {
        return new SkinTemplateInstance(overrides);
    }

    public String[] getParameters() {
        return parameters.keySet().toArray(new String[] {});
    }

}
