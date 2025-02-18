/**
 * Copyright John E. Lloyd, 2004. All rights reserved. Permission to use,
 * copy, modify and redistribute is granted, provided that this copyright
 * notice is retained and the author is given credit whenever appropriate.
 *
 * This  software is distributed "as is", without any warranty, including 
 * any implied warranty of merchantability or fitness for a particular
 * use. The author assumes no responsibility for, and shall not be liable
 * for, any special, indirect, or consequential damages, or any damages
 * whatsoever, arising out of or in connection with the use of this
 * software.
 * 
 * 
 * Changelog:
 *  30.10.2007
 *  - Wurzelberechnung angepasst
 */

package ca.ubc.cs.spider.lloyed.quickhull3d;

import info.kriese.sopra.math.Math2;

import java.util.Random;

/**
 * A three-element vector. This class is actually a reduced version of the
 * Vector3d class contained in the author's matlib package (which was partly
 * inspired by org.jogamp.vecmath). Only a mininal number of methods which are
 * relevant to convex hull generation are supplied here.
 * 
 * @author John E. Lloyd, Fall 2004
 */
public class Vector3d {
    /**
     * Precision of a double.
     */
    static private final double DOUBLE_PREC = 2.2204460492503131e-16;

    /**
     * First element
     */
    public double x;

    /**
     * Second element
     */
    public double y;

    /**
     * Third element
     */
    public double z;

    /**
     * Creates a 3-vector and initializes its elements to 0.
     */
    public Vector3d() {
    }

    /**
     * Creates a 3-vector with the supplied element values.
     * 
     * @param x
     *                first element
     * @param y
     *                second element
     * @param z
     *                third element
     */
    public Vector3d(double x, double y, double z) {
	set(x, y, z);
    }

    /**
     * Creates a 3-vector by copying an existing one.
     * 
     * @param v
     *                vector to be copied
     */
    public Vector3d(Vector3d v) {
	set(v);
    }

    /**
     * Adds this vector to v1 and places the result in this vector.
     * 
     * @param v1
     *                right-hand vector
     */
    public void add(Vector3d v1) {
	this.x += v1.x;
	this.y += v1.y;
	this.z += v1.z;
    }

    /**
     * Adds vector v1 to v2 and places the result in this vector.
     * 
     * @param v1
     *                left-hand vector
     * @param v2
     *                right-hand vector
     */
    public void add(Vector3d v1, Vector3d v2) {
	this.x = v1.x + v2.x;
	this.y = v1.y + v2.y;
	this.z = v1.z + v2.z;
    }

    /**
     * Computes the cross product of v1 and v2 and places the result in this
     * vector.
     * 
     * @param v1
     *                left-hand vector
     * @param v2
     *                right-hand vector
     */
    public void cross(Vector3d v1, Vector3d v2) {
	double tmpx = v1.y * v2.z - v1.z * v2.y;
	double tmpy = v1.z * v2.x - v1.x * v2.z;
	double tmpz = v1.x * v2.y - v1.y * v2.x;

	this.x = tmpx;
	this.y = tmpy;
	this.z = tmpz;
    }

    /**
     * Returns the Euclidean distance between this vector and vector v.
     * 
     * @return distance between this vector and v
     */
    public double distance(Vector3d v) {
	double dx = this.x - v.x;
	double dy = this.y - v.y;
	double dz = this.z - v.z;

	return Math2.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Returns the squared of the Euclidean distance between this vector and
     * vector v.
     * 
     * @return squared distance between this vector and v
     */
    public double distanceSquared(Vector3d v) {
	double dx = this.x - v.x;
	double dy = this.y - v.y;
	double dz = this.z - v.z;

	return (dx * dx + dy * dy + dz * dz);
    }

    /**
     * Returns the dot product of this vector and v1.
     * 
     * @param v1
     *                right-hand vector
     * @return dot product
     */
    public double dot(Vector3d v1) {
	return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }

    /**
     * Gets a single element of this vector. Elements 0, 1, and 2 correspond to
     * x, y, and z.
     * 
     * @param i
     *                element index
     * @return element value throws ArrayIndexOutOfBoundsException if i is not
     *         in the range 0 to 2.
     */
    public double get(int i) {
	switch (i) {
	    case 0: {
		return this.x;
	    }
	    case 1: {
		return this.y;
	    }
	    case 2: {
		return this.z;
	    }
	    default: {
		throw new ArrayIndexOutOfBoundsException(i);
	    }
	}
    }

    /**
     * Returns the 2 norm of this vector. This is the square root of the sum of
     * the squares of the elements.
     * 
     * @return vector 2 norm
     */
    public double norm() {
	return Math2.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Normalizes this vector in place.
     */
    public void normalize() {
	double lenSqr = this.x * this.x + this.y * this.y + this.z * this.z;
	double err = lenSqr - 1;
	if (err > (2 * DOUBLE_PREC) || err < -(2 * DOUBLE_PREC)) {
	    double len = Math2.sqrt(lenSqr);
	    this.x /= len;
	    this.y /= len;
	    this.z /= len;
	}
    }

    /**
     * Returns the square of the 2 norm of this vector. This is the sum of the
     * squares of the elements.
     * 
     * @return square of the 2 norm
     */
    public double normSquared() {
	return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    /**
     * Scales the elements of this vector by <code>s</code>.
     * 
     * @param s
     *                scaling factor
     */
    public void scale(double s) {
	this.x = s * this.x;
	this.y = s * this.y;
	this.z = s * this.z;
    }

    /**
     * Scales the elements of vector v1 by <code>s</code> and places the
     * results in this vector.
     * 
     * @param s
     *                scaling factor
     * @param v1
     *                vector to be scaled
     */
    public void scale(double s, Vector3d v1) {
	this.x = s * v1.x;
	this.y = s * v1.y;
	this.z = s * v1.z;
    }

    /**
     * Sets the elements of this vector to the prescribed values.
     * 
     * @param x
     *                value for first element
     * @param y
     *                value for second element
     * @param z
     *                value for third element
     */
    public void set(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    /**
     * Sets a single element of this vector. Elements 0, 1, and 2 correspond to
     * x, y, and z.
     * 
     * @param i
     *                element index
     * @param value
     *                element value
     * @return element value throws ArrayIndexOutOfBoundsException if i is not
     *         in the range 0 to 2.
     */
    public void set(int i, double value) {
	switch (i) {
	    case 0: {
		this.x = value;
		break;
	    }
	    case 1: {
		this.y = value;
		break;
	    }
	    case 2: {
		this.z = value;
		break;
	    }
	    default: {
		throw new ArrayIndexOutOfBoundsException(i);
	    }
	}
    }

    /**
     * Sets the values of this vector to those of v1.
     * 
     * @param v1
     *                vector whose values are copied
     */
    public void set(Vector3d v1) {
	this.x = v1.x;
	this.y = v1.y;
	this.z = v1.z;
    }

    /**
     * Sets the elements of this vector to zero.
     */
    public void setZero() {
	this.x = 0;
	this.y = 0;
	this.z = 0;
    }

    /**
     * Subtracts v1 from this vector and places the result in this vector.
     * 
     * @param v1
     *                right-hand vector
     */
    public void sub(Vector3d v1) {
	this.x -= v1.x;
	this.y -= v1.y;
	this.z -= v1.z;
    }

    /**
     * Subtracts vector v1 from v2 and places the result in this vector.
     * 
     * @param v1
     *                left-hand vector
     * @param v2
     *                right-hand vector
     */
    public void sub(Vector3d v1, Vector3d v2) {
	this.x = v1.x - v2.x;
	this.y = v1.y - v2.y;
	this.z = v1.z - v2.z;
    }

    /**
     * Returns a string representation of this vector, consisting of the x, y,
     * and z coordinates.
     * 
     * @return string representation
     */
    @Override
    public String toString() {
	return this.x + " " + this.y + " " + this.z;
    }

    /**
     * Sets the elements of this vector to uniformly distributed random values
     * in a specified range, using a supplied random number generator.
     * 
     * @param lower
     *                lower random value (inclusive)
     * @param upper
     *                upper random value (exclusive)
     * @param generator
     *                random number generator
     */
    protected void setRandom(double lower, double upper, Random generator) {
	double range = upper - lower;

	this.x = generator.nextDouble() * range + lower;
	this.y = generator.nextDouble() * range + lower;
	this.z = generator.nextDouble() * range + lower;
    }
}
