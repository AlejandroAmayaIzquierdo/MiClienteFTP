package ftp.model;


import org.apache.commons.net.ftp.FTPFile;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@SuppressWarnings("serial")
public class MyFile extends FTPFile {
	
	public StringProperty nombre = new SimpleStringProperty();
	public LongProperty size = new SimpleLongProperty();
	public ObjectProperty<FileType> type = new SimpleObjectProperty<FileType>();
	
	public MyFile(String nombre,Number size,FileType type) {
		setName(nombre);
		setLink(nombre);
		setNombre(nombre);
		setSize(size.longValue());
		setType(type);
	}
	
	
	public final StringProperty nombreProperty() {
		return this.nombre;
	}
	
	
	public final String getNombre() {
		return this.nombreProperty().get();
	}
	
	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}
	
	public final LongProperty sizeProperty() {
		return this.size;
	}
	
	public final long getSize() {
		return this.sizeProperty().get();
	}
	
	public final void setSize(final long size) {
		this.sizeProperty().set(size);
	}
	
	public final ObjectProperty<FileType> typeProperty() {
		return this.type;
	}
	
	public final void setType(final FileType type) {
		this.typeProperty().set(type);
	}
	
	public boolean esDirectorio() {
		return type.get().equals(FileType.Directorio);
	}
	

	
	@Override
	public String toString() {
		return this.nombre.get();
	}
	



}
