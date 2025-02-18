package stuff;

/*******************************************************************************
 *                      Copyright (c) 1999 Justin Couch
 *                               Java Source
 *
 * Raw J3D Tutorial
 *
 * Version History
 * Date        Version  Programmer
 * ----------  -------  ------------------------------------------
 * 01/08/1998  1.0.0    Justin Couch
 *
 ******************************************************************************/

// no package
// Standard imports
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.ImageComponent;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.IndexedQuadArray;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.TextureAttributes;

// Application specific imports
// none

/**
 * Test class illustrating the use of geometry.
 * <P>
 * A simple Shape3D class that contains a flat square constructed from a raw
 * geometry array. The square is located at the origin with bounds 0.5 along
 * each axis and lies in the X,Y plain. The normals point along the +Z axis.
 * However, the geometry is set to do no backface culling so you should see it
 * regardless of viewing position.
 * <P>
 * The basic appearance is set uses color in each corner to blend towards the
 * others. An emissive color of red is set just in case other colors don't work.
 * 
 * @author Justin Couch
 * @version Who Cares!
 */
public class ExampleGeometry extends Shape3D {

	private IndexedQuadArray geom;

	private Appearance appearance;

	private Texture texture;

	/**
	 * Construct the test object with geometry
	 */
	public ExampleGeometry() {
		constructGeometry();
		constructAppearance();
	}

	private void constructGeometry() {
		int flags = GeometryArray.COORDINATES | GeometryArray.COLOR_4
				| GeometryArray.NORMALS;

		geom = new IndexedQuadArray(4, flags, 4);

		double[] coordinates = { 0.5, 0.5, 0, 0.5, -0.5, 0, -0.5, -0.5, 0,
				-0.5, 0.5, 0 };

		int[] indices = { 0, 1, 2, 3 };

		geom.setCoordinates(0, coordinates);
		geom.setCoordinateIndices(0, indices);

		float[] colors = { 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0 };

		geom.setColors(0, colors);
		geom.setColorIndices(0, indices);

		float[] normal = { 0, 0, 1 };

		geom.setNormal(0, normal);
		geom.setNormal(1, normal);
		geom.setNormal(2, normal);
		geom.setNormal(3, normal);

		setGeometry(geom);
	}

	/**
	 * Construct the default appearance.
	 */
	private void constructAppearance() {
		appearance = new Appearance();

		TextureAttributes tex_attr = new TextureAttributes();
		tex_attr.setTextureMode(TextureAttributes.DECAL);
		tex_attr.setPerspectiveCorrectionMode(TextureAttributes.FASTEST);

		appearance.setTextureAttributes(tex_attr);

		ColoringAttributes col_attr = new ColoringAttributes();
		col_attr.setShadeModel(ColoringAttributes.SHADE_GOURAUD);

		appearance.setColoringAttributes(col_attr);

		PolygonAttributes rend_attr = new PolygonAttributes();
		rend_attr.setCullFace(PolygonAttributes.CULL_NONE);
		// uncomment this if you want it to display in line draw mode
		// rend_attr.setPolygonMode(PolygonAttributes.POLYGON_LINE);

		appearance.setPolygonAttributes(rend_attr);

		Material mat = new Material();
		// Color3f col = new Color3f(1, 0, 0);
		// mat.setEmissiveColor(col);

		appearance.setMaterial(mat);

		setAppearance(appearance);
	}

	/**
	 * Set the texture on our goemetry
	 * <P>
	 * Always specified as a URL so that we may fetch it from anywhere.
	 * 
	 * @param url
	 *            The url to the image.
	 */
	public void setTexture(URL url) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image src_img = tk.createImage(url);
		BufferedImage buf_img = null;

		if (!(src_img instanceof BufferedImage)) {
			// create a component anonymous inner class to give us the image
			// observer we need to get the width and height of the source image.
			Component obs = new Component() {
				/** */
				private static final long serialVersionUID = 1L;
			};

			int width = src_img.getWidth(obs);
			int height = src_img.getHeight(obs);

			// construct the buffered image from the source data.
			buf_img = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);

			Graphics g = buf_img.getGraphics();
			g.drawImage(src_img, 0, 0, null);
			g.dispose();
		} else
			buf_img = (BufferedImage) src_img;

		src_img.flush();

		ImageComponent img_comp = new ImageComponent2D(
				ImageComponent.FORMAT_RGB, buf_img);

		texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, img_comp
				.getWidth(), img_comp.getHeight());

		appearance.setTexture(texture);

		buf_img.flush();
	}
}
