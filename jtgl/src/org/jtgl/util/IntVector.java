/*
 * IntVector.java
 *
 * Created on 11 de diciembre de 2003, 11:08
 */

package org.jtgl.util;

/**
 * Vector of integers, with similar <code>java.util.Vector</code> API
 * @author  Manuel
 */
public final class IntVector {
    int [] vector;
    int numElements;
    int capacityIncrement;
    
    public IntVector(){
        this(10,0);
    }
    
    public IntVector(int initialSize){
        this(initialSize,0);
    }
    /** Creates a new instance of IntVector */
    public IntVector(int initialSize,int capacityIncrement) {
        initialSize = initialSize > 0 ? initialSize : 1;
        this.capacityIncrement = capacityIncrement > 0 ? capacityIncrement : 0;
        vector = new int[initialSize];        
    }
    
    public int getCapacity(){
        return vector.length;        
    }
    
    public int getCapacityIncrement(){    
        return capacityIncrement;
    }
    
    public int getSize(){
        return numElements;
    }
    
    public int getElement(int index){
        return elementAt(index);
    }
    
    public int elementAt(int index) {
	if (index >= numElements) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + numElements);
	}
        return vector[index];
    }     
    
    public void setElement(int index,int value){
        vector[index] = value;
    }
    
    public void addElement(int value){
        ensureCapacity(numElements + 1);
	vector[numElements++] = value;         
    }
    public void insertElementAt(int value, int index) {	
	if (index > numElements)
	    throw new ArrayIndexOutOfBoundsException(index + " > " + numElements);	
	ensureCapacity(numElements + 1);
	System.arraycopy(vector, index, vector, index + 1, numElements - index);
	vector[index] = value;
	numElements++;
    }     
    
    public void removeElementAt(int index) {	
	if(index >= numElements)
	    throw new ArrayIndexOutOfBoundsException(index + " >= " + numElements);	
	else 
            if(index < 0)
                throw new ArrayIndexOutOfBoundsException(index);	
	int j = numElements - index - 1;
	if (j > 0)
	    System.arraycopy(vector, index + 1, vector, index, j);	
	numElements--;
	vector[numElements] = 0; 
    }     
    
    public boolean removeElement(int value) {
	int i = indexOf(value);
	if(i >= 0){
	    removeElementAt(i);
	    return true;
	}
	return false;
    }
     
    
    public void removeAllElements() { 
        numElements = 0;
    }
    
    public void clear(){
        removeAllElements();
    }
    
    public void trimToSize() {	
	int oldCapacity = vector.length;
	if(numElements < oldCapacity) {
	    int oldVector[] = vector;
	    vector = new int[numElements];
	    System.arraycopy(oldVector, 0, vector, 0, numElements);
	}
    }     
    
    public void setSize(int newSize) {	
	if(newSize > numElements){
	    ensureCapacity(newSize);
	}else {
	    for(int i = newSize ; i < numElements ; i++)
		vector[i] = 0;	    
	}
	numElements = newSize;
    }     
    
    public boolean isEmpty() {
	return numElements == 0;
    }
    
    public int indexOf(int value, int index) {       
        for(int i = index ; i < numElements ; i++)
            if(vector[i] == value)
                return i;
        return -1;
    }
    
    public int indexOf(int value){
        return indexOf(value,0);
    }
    
    public String toString(){
        StringBuffer buf = new StringBuffer("IntVector(");
        for(int i = 0; i < numElements ; i++){
            buf.append('[').append(vector[i]).append(']');
        }
        return buf.append(')').toString();
    }
    
    private void ensureCapacity(int minCapacity) {
	int oldCapacity = vector.length;
	if(minCapacity > oldCapacity) {
	    int oldVector[] = vector;
	    int newCapacity = capacityIncrement > 0 ? oldCapacity + capacityIncrement : oldCapacity << 1;
    	    if(newCapacity < minCapacity)
		newCapacity = minCapacity;	  
	    vector = new int[newCapacity];
	    System.arraycopy(oldVector, 0, vector, 0, numElements);
	}
    }     
}
