package de.leonhardt.sbm;

import java.io.File;

/**
 * I/O interface.
 * 
 * @author Frederik Leonhardt
 *
 * @param <E>
 */
public interface FileIO<E> {

	public E readFrom(String filePath) throws IllegalArgumentException;
	public E readFrom(File file) throws IllegalArgumentException;
	public void writeTo(E objs, String filePath) throws IllegalArgumentException;
	public void writeTo(E objs, File file) throws IllegalArgumentException;	
}
