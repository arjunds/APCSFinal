
/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

import java.awt.*;

import com.jogamp.newt.opengl.GLWindow;

import processing.core.*;

/**
 * 
 * @author jrc03c This class represents the camera on the screen that you can use
 *         to move/look around
 *
 */
public class Camera {

	private Robot robot;
	private PVector center, right, forward, position, velocity;
	private float speed, xSensitivity, ySensitivity, pan, tilt, friction, fov, viewDistance;
	private Point mouse, pMouse;

	public Camera() {
		this(3, 1, 1, .75f, PConstants.PI / 3f, 1000f);
	}

	public Camera(float speed, float xSensitivity, float ySensitivity, float friction, float fov, float viewDistance) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
		}

		this.speed = speed;
		this.xSensitivity = xSensitivity;
		this.ySensitivity = ySensitivity;
		this.friction = friction;
		this.fov = fov;
		this.viewDistance = viewDistance;

		position = new PVector(0f, 0f, 0f);
		right = new PVector(1f, 0f, 0f);
		forward = new PVector(0f, 0f, 1f);
		velocity = new PVector(0f, 0f, 0f);

		pan = 0;
		tilt = 0;
	}

	public void setup(PApplet g) {
		g.perspective(fov, (float) g.width / (float) g.height, 0.01f, viewDistance);
		// Moves the mouse to the center of the screen at the start of the game
		robot.mouseMove((int) ((GLWindow) g.getSurface().getNative()).getX() + g.width / 2,
				(int) ((GLWindow) g.getSurface().getNative()).getY() + g.height / 2);
	}

	public void draw(PApplet g) {
		// Get the coordinates of the borders of the window
		int top = ((GLWindow) g.getSurface().getNative()).getY();
		int left = ((GLWindow) g.getSurface().getNative()).getX();
		int windowRight = g.width + left;
		int bottom = g.height + top;

		mouse = MouseInfo.getPointerInfo().getLocation();

		if (pMouse == null)
			pMouse = new Point(mouse.x, mouse.y);

		// means that the mouse went off the screen to the left so move it to the right
		if (mouse.x < left + 2 && (mouse.x - pMouse.x) < 0) {
			robot.mouseMove(windowRight - 2, mouse.y);
			mouse.x = windowRight - 2;
			pMouse.x = windowRight - 2;
		}

		// means that the mouse went off the screen to the right so move it to the left
		if (mouse.x > windowRight - 2 && (mouse.x - pMouse.x) > 0) {
			robot.mouseMove(left + 2, mouse.y);
			mouse.x = left + 2;
			pMouse.x = left + 2;
		}

		// means that the mouse went up off the screen so move it to the bottom
		if (mouse.y < top + 10 && (mouse.y - pMouse.y) < 0) {
			robot.mouseMove(mouse.x, bottom - 5);
			mouse.y = bottom - 5;
			pMouse.y = bottom - 5;
		}

		// means that the mouse went down off the screen so move it to the top
		if (mouse.y > bottom - 5 && (mouse.y - pMouse.y) > 0) {
			robot.mouseMove(mouse.x, top + 10);
			mouse.y = top + 10;
			pMouse.y = top + 10;
		}

		// map the mouse value to the corresponding angle between 0 and 2PI for x
		// rotation(pan) because you have 360º rotation
		pan += PApplet.map(mouse.x - pMouse.x, 0, g.width, 0, PConstants.TWO_PI) * xSensitivity;
		tilt += PApplet.map(mouse.y - pMouse.y, 0, g.height, 0, PConstants.PI) * ySensitivity;
		tilt = clamp(tilt, -PConstants.PI / 2.01f, PConstants.PI / 2.01f);

		// tan of pi/2 or -pi/2 is undefined so if it happens to be exactly that
		// increase it so the code works
		if (tilt == PConstants.PI / 2)
			tilt += 0.001f;
		if (tilt == -PConstants.PI / 2)
			tilt -= 0.001f;

		forward = new PVector(PApplet.cos(pan), PApplet.tan(tilt), PApplet.sin(pan));

		// make it a unit vector because the direction is all that matters
		forward.normalize();

		// subtract pi/2 from pan to get the vector perpendicular to forward to show
		// which way is right
		right = new PVector(PApplet.cos(pan - PConstants.PI / 2), 0, PApplet.sin(pan - PConstants.PI / 2));

		//have the previous mouse set to the current mouse to use it for the next call to draw()
		pMouse = new Point(mouse.x, mouse.y);

		// account for friction
		velocity.mult(friction);
		// use velocity to find out location of new position
		position.add(velocity);
		// center of the sketch is in the direction of forward but translated based on
		// how you moved so you need to take into account position
		center = PVector.add(position, forward);
		g.camera(position.x, position.y, position.z, center.x, center.y, center.z, 0, 1, 0);
	}

	// "Clamp" the x value to within the range of min-max
	private float clamp(float x, float min, float max) {
		if (x > max)
			return max;
		if (x < min)
			return min;
		return x;
	}

	public PVector getForward() {
		return forward;
	}

	public PVector getPosition() {
		return position;
	}

	public PVector getVelocity() {
		return velocity;
	}

	public PVector getCenter() {
		return center;
	}

	public PVector getRight() {
		return right;
	}

	public float getPan() {
		return pan;
	}

	public float getTilt() {
		return tilt;
	}

	public void moveX(int dir) {
		velocity.add(PVector.mult(right, speed * dir));
	}

	public void moveZ(int dir) {
		velocity.add(PVector.mult(forward, speed * dir));
	}

	public float xSensitivity() {
		return xSensitivity;
	}

	public float ySensitivity() {
		return ySensitivity;
	}

	public void setXSensitivity(float f) {
		xSensitivity = f;
	}

	public void setYSensitivity(float f) {
		ySensitivity = f;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float f) {
		speed = f;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float f) {
		friction = f;
	}

	public float getFOV() {
		return fov;
	}

	public void setFOV(float f) {
		fov = f;
	}

	public float getViewDistance() {
		return viewDistance;
	}

	public void setViewDistance(float f) {
		viewDistance = f;
	}

	public Point getMouse() {
		return mouse;
	}

	public void setMouse(Point mouse) {
		robot.mouseMove(mouse.x, mouse.y);
	}

	public void setMouse(int x, int y) {
		robot.mouseMove(x, y);
	}

	public void setPan(double angle) {
		pan = (float) angle;
	}
}
