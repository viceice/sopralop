package ca.ubc.cs.spider.lloyed.quickhull3d;

/**
 * Exception thrown when QuickHull3D encounters an internal error.
 */
public class InternalErrorException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3672197428684626690L;

	public InternalErrorException (String msg)
	 { super (msg);
	 }
}
