import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

/**
 * 
 * @author rafeh This class represents a pair of portals with a method that
 *         takes in a character and checks to see if that character is touching
 *         either portal. If it is, the character is teleported as necessary
 */
public class PortalPair {

	private Block orange, blue;
	private PShape orangePortal, bluePortal;

	/*
	 * orient: 1 front 2 back 3 left 4 right orientation is based on looking at the
	 * cubes along the +x axis
	 */
	private int bOrient, oOrient;

	public PortalPair(PShape blue, PShape orange) {
		orangePortal = orange;
		bluePortal = blue;
	}

	/**
	 * Sets both the portals to null
	 */
	public void reset() {
		orange = null;
		blue = null;
	}

	public void draw(PApplet p) {
		p.pushMatrix();
		p.pushStyle();
		if (orange != null) {
			p.fill(255, 140, 0);
			orange.draw(p);
		}
		if (blue != null) {
			p.fill(30, 144, 255);
			blue.draw(p);
		}

		p.popMatrix();
		p.popStyle();
	}

	/**
	 * Replaces the current blue portal with a new one at the specified coordinates
	 * and orientation
	 * 
	 * @param x
	 *            x-coordinate of the portal
	 * @param y
	 *            y-coordinate of the portal
	 * @param z
	 *            z-coordinate of the portal
	 * @param orient
	 *            where the portal is on the block relative to +x axis (1 front, 2
	 *            back, 3 left, 4 right)
	 */
	public void addBlue(float x, float y, float z, int orient) {
		// checks to make sure that orange is not in the same spot as the blue portal
		if ((orange != null && (x != orange.getX() || y != orange.getY() || z != orange.getZ())) || orange == null) {
			blue = new Block(bluePortal, x, y, z, 4f, 3f);
			bOrient = orient;
		}
	}

	/**
	 * Replaces the current orange portal with a new one at the specified
	 * coordinates and orientation
	 * 
	 * @param x
	 *            x-coordinate of the portal
	 * @param y
	 *            y-coordinate of the portal
	 * @param z
	 *            z-coordinate of the portal
	 * @param orient
	 *            where the portal is on the block relative to +x axis (1 front, 2
	 *            back, 3 left, 4 right)
	 */
	public void addOrange(float x, float y, float z, int orient) {
		// makes sure that the blue portal isn't where you want to put the orange portal
		if ((blue != null && (x != blue.getX() || y != blue.getY() || z != blue.getZ())) || blue == null) {
			orange = new Block(orangePortal, x, y, z, 4f, 3f);
			oOrient = orient;
		}
	}

	/**
	 * Teleports the specified Player to the opposite portal if it is touching a
	 * portal. Must be called in the draw() method of the PApplet
	 * 
	 * @param c
	 *            The Player that is potentially going to touch the portals
	 * @param p
	 *            PApplet which is only used to implement the delay() method after
	 *            touching a portal
	 */
	public void teleport(Player c, PApplet p) {
		if (touchingBlue(c) && orange != null) {
			if (oOrient == 1) {
				c.setPan(Math.PI);
				if (bOrient == 1) {
					c.getVelocity().x = -c.getVelocity().x;
					c.getVelocity().z = -c.getVelocity().z;
				} else if (bOrient == 3) {
					float z = c.getVelocity().x;
					c.getVelocity().x = -c.getVelocity().z;
					c.getVelocity().z = z;
				} else if (bOrient == 4) {
					float z = -c.getVelocity().x;
					c.getVelocity().x = c.getVelocity().z;
					c.getVelocity().z = z;
				}
				c.moveTo(orange.getX() - 3, orange.getY(), orange.getZ());
			} else if (oOrient == 2) {
				c.setPan(0);
				if (bOrient == 2) {
					c.getVelocity().x = -c.getVelocity().x;
					c.getVelocity().z = -c.getVelocity().z;
				} else if (bOrient == 3) {
					float z = -c.getVelocity().x;
					c.getVelocity().x = c.getVelocity().z;
					c.getVelocity().z = z;
				} else if (bOrient == 4) {
					float z = c.getVelocity().x;
					c.getVelocity().x = -c.getVelocity().z;
					c.getVelocity().z = z;
				}
				c.moveTo(orange.getX() + 3, orange.getY(), orange.getZ());
			} else if (oOrient == 3) {
				c.setPan(Math.PI * 3 / 2);
				if (bOrient == 3) {
					c.getVelocity().x = -c.getVelocity().x;
					c.getVelocity().z = -c.getVelocity().z;
				} else if (bOrient == 1) {
					float x = c.getVelocity().z;
					c.getVelocity().z = -c.getVelocity().x;
					c.getVelocity().x = x;
				} else if (bOrient == 2) {
					float x = -c.getVelocity().x;
					c.getVelocity().z = c.getVelocity().x;
					c.getVelocity().x = x;
				}
				c.moveTo(orange.getX(), orange.getY(), orange.getZ() - 3);
			} else if (oOrient == 4) {
				c.setPan(Math.PI / 2);
				if (bOrient == 4) {
					c.getVelocity().x = -c.getVelocity().x;
					c.getVelocity().z = -c.getVelocity().z;
				} else if (bOrient == 1) {
					float x = -c.getVelocity().z;
					c.getVelocity().z = c.getVelocity().x;
					c.getVelocity().x = x;
				} else if (bOrient == 2) {
					float x = c.getVelocity().z;
					c.getVelocity().z = -c.getVelocity().x;
					c.getVelocity().x = x;
				}
				c.moveTo(orange.getX(), orange.getY(), orange.getZ() + 3);
			}

			p.delay(90);

		} else if (touchingOrange(c) && blue != null) {
			if (bOrient == 1) {
				c.setPan(Math.PI);
				if (oOrient == 1) {
					c.getVelocity().x = -c.getVelocity().x;
					c.getVelocity().z = -c.getVelocity().z;
				} else if (oOrient == 3) {
					float z = c.getVelocity().x;
					c.getVelocity().x = -c.getVelocity().z;
					c.getVelocity().z = z;
				} else if (oOrient == 4) {
					float z = -c.getVelocity().x;
					c.getVelocity().x = c.getVelocity().z;
					c.getVelocity().z = z;
				}
				c.moveTo(blue.getX() - 3, blue.getY(), blue.getZ());
			} else if (bOrient == 2) {
				c.setPan(0);
				if (oOrient == 2) {
					c.getVelocity().x = -c.getVelocity().x;
					c.getVelocity().z = -c.getVelocity().z;
				} else if (oOrient == 3) {
					float z = -c.getVelocity().x;
					c.getVelocity().x = c.getVelocity().z;
					c.getVelocity().z = z;
				} else if (oOrient == 4) {
					float z = c.getVelocity().x;
					c.getVelocity().x = -c.getVelocity().z;
					c.getVelocity().z = z;
				}
				c.moveTo(blue.getX() + 3, blue.getY(), blue.getZ());
			} else if (bOrient == 3) {
				c.setPan(Math.PI * 3 / 2);
				if (oOrient == 3) {
					c.getVelocity().x = -c.getVelocity().x;
					c.getVelocity().z = -c.getVelocity().z;
				} else if (oOrient == 1) {
					float x = c.getVelocity().z;
					c.getVelocity().z = -c.getVelocity().x;
					c.getVelocity().x = x;
				} else if (oOrient == 2) {
					float x = -c.getVelocity().x;
					c.getVelocity().z = c.getVelocity().x;
					c.getVelocity().x = x;
				}
				c.moveTo(blue.getX(), blue.getY(), blue.getZ() - 3);
			} else if (bOrient == 4) {
				c.setPan(Math.PI / 2);
				if (oOrient == 4) {
					c.getVelocity().x = -c.getVelocity().x;
					c.getVelocity().z = -c.getVelocity().z;
				} else if (oOrient == 1) {
					float x = -c.getVelocity().z;
					c.getVelocity().z = c.getVelocity().x;
					c.getVelocity().x = x;
				} else if (oOrient == 2) {
					float x = c.getVelocity().z;
					c.getVelocity().z = -c.getVelocity().x;
					c.getVelocity().x = x;
				}
				c.moveTo(blue.getX(), blue.getY(), blue.getZ() + 3);
			}

			p.delay(90);
		}
	}

	private boolean touchingBlue(Player c) {
		if (blue == null)
			return false;

		PVector position = c.getPosition();
		float w = c.getWidth();
		float d = c.getDepth();
		float left = position.x - w / 2;
		float right = position.x + w / 2;
		float front = position.z + d / 2;
		float back = position.z - d / 2;

		if (blue.isPointInCube(left, position.y, position.z) || blue.isPointInCube(right, position.y, position.z)
				|| blue.isPointInCube(position.x, position.y, front)
				|| blue.isPointInCube(position.x, position.y, back)) {
			return true;
		}

		return false;
	}

	private boolean touchingOrange(Player c) {
		if (orange == null)
			return false;

		PVector position = c.getPosition();
		float w = c.getWidth();
		float d = c.getDepth();
		float left = position.x - w / 2;
		float right = position.x + w / 2;
		float front = position.z + d / 2;
		float back = position.z - d / 2;

		if (orange.isPointInCube(left, position.y, position.z) || orange.isPointInCube(right, position.y, position.z)
				|| orange.isPointInCube(position.x, position.y, front)
				|| orange.isPointInCube(position.x, position.y, back)) {
			return true;
		}

		return false;
	}

}