import processing.core.*;

/**
 * 
 * @author asampath803 This class represents a block in 3D space, whether it is
 *         the ground, the wall, or a portal
 */
public class Block {
	private float x, y, z, size, height;
	private PShape block;

	public Block(PShape block, float x, float y, float z, float size, float height) {
		// Image is assumed to come in textured
		this.block = block;

		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;

		// find out the true height of the object including the height of the pshape and
		// the scale factor
		this.height = (float) (size / 1.6 * height);
	}

	public void draw(PApplet g) {
		g.pushMatrix();
		g.pushStyle();
		g.translate(x, y, z);
		g.scale(size / 1.6f);
		g.shape(block);
		g.popMatrix();
		g.popStyle();
	}

	public boolean isPointInCube(float x, float y, float z) {
		// the x y z coords of the block are in the center so +/- by size/2 in all
		// directions to get the edges
		float left = this.x - size / 2;
		float right = this.x + size / 2;
		float top = this.y - height / 2;
		float bottom = this.y + height / 2;
		float front = this.z - size / 2;
		float back = this.z + size / 2;
		if (x > left && x < right && y > top && y < bottom && z > front && z < back) {
			return true;
		}

		return false;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getSize() {
		return size;
	}

	public float getHeight() {
		return height;
	}
}
