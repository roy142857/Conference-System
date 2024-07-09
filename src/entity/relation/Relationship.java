package entity.relation;

import java.io.Serializable;

/**
 * A relationship is a mutable integer.
 */
public class Relationship implements Serializable{
	
	private int layer;
	
	public Relationship(int initialValue){
		this.layer = initialValue;
	}
	
	/**
	 * Get the layer of connection to this relationship.
	 *
	 * @return the layer of connection.
	 */
	public int getLayer(){
		return layer;
	}
	
	/**
	 * Apply an offset of 1.
	 */
	public void increase(){
		applyOffset(1);
	}
	
	/**
	 * Apply an offset of -1.
	 */
	public void decrease(){
		applyOffset(- 1);
	}
	
	/**
	 * Apply an offset.
	 * If the offset makes the layer becomes negative, set it to 0.
	 */
	public void applyOffset(int offset){
		if(layer + offset < 0)
			// layer + offset < 0 <=> layer < - offset
			layer = 0;
		else
			layer += offset;
	}
}
