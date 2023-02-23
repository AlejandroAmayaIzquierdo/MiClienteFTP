package ftp.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class FTPConnection {
	
	private FTPClient client;
		
	private StringProperty server = new SimpleStringProperty();
	private IntegerProperty puerto = new SimpleIntegerProperty();
	private StringProperty usuario = new SimpleStringProperty();
	private StringProperty password = new SimpleStringProperty();
	
	
	private ListProperty<MyFile> files = new SimpleListProperty<>(FXCollections.observableArrayList());

	private BooleanProperty isConnected = new SimpleBooleanProperty();
	
	public FTPConnection(String server,int puerto,String user,String passWord) {
		setServer(server);
		setPuerto(puerto);
		setUsuario(user);
		setPassword(passWord);
		
		client = new FTPClient();
	}
	
	public FTPConnection() {
		client = new FTPClient();
	}
	
	public void connect() {
		try {
			client.connect(getServer(),getPuerto());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void login() {
		try {
			client.login(getUsuario(), getPassword());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connectAndLogin() {
		connect();
		login();
		
		isConnected.set(true);
		loadData();
	}
	public void downloadFile(MyFile file, OutputStream outputStream) throws IOException {
		Task t = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
			    try {
				    // Crear el InputStream para leer el archivo desde el servidor FTP
				    InputStream inputStream = client.retrieveFileStream(file.getLink());

				    // Copiar los datos del InputStream al OutputStream
				    byte[] buffer = new byte[1024];
				    int bytesRead;
				    while ((bytesRead = inputStream.read(buffer)) != -1) {
				        outputStream.write(buffer, 0, bytesRead);
				    }

				    // Cerrar el InputStream y finalizar la transferencia del archivo
				   
			    	inputStream.close();
					client.completePendingCommand();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		new Thread(t).start();
	}

	
	public void disconnect() {
		try {
			client.disconnect();
			isConnected.set(false);
			files.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	
	public void changeDirectory(String str) {
		try {
			client.changeWorkingDirectory(str);
			loadData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getWorkingDirectory() {
		try {
			return client.printWorkingDirectory();
		} catch (IOException e) {
			return null;
		}
	}


	
	private void loadData() {
		try {
			FTPFile [] ficheros = client.listFiles();
			files.clear();
			Arrays.asList(ficheros)
			.forEach(fichero -> {
				FileType type = fichero.isDirectory() ? FileType.Directorio : FileType.Archivo;
				files.add(new MyFile(
						fichero.getName(),
						fichero.getSize(),
						type));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FTPClient getClient() {
		return this.client;
	}

	public final StringProperty serverProperty() {
		return this.server;
	}
	

	public final String getServer() {
		return this.serverProperty().get();
	}
	

	public final void setServer(final String server) {
		this.serverProperty().set(server);
	}
	

	public final IntegerProperty puertoProperty() {
		return this.puerto;
	}
	

	public final int getPuerto() {
		return this.puertoProperty().get();
	}
	

	public final void setPuerto(final int puerto) {
		this.puertoProperty().set(puerto);
	}
	

	public final StringProperty usuarioProperty() {
		return this.usuario;
	}
	

	public final String getUsuario() {
		return this.usuarioProperty().get();
	}
	

	public final void setUsuario(final String usuario) {
		this.usuarioProperty().set(usuario);
	}
	

	public final StringProperty passwordProperty() {
		return this.password;
	}
	

	public final String getPassword() {
		return this.passwordProperty().get();
	}
	

	public final void setPassword(final String password) {
		this.passwordProperty().set(password);
	}

	public final ListProperty<MyFile> filesProperty() {
		return this.files;
	}
	

	public final ObservableList<MyFile> getFiles() {
		return this.filesProperty().get();
	}
	

	public final void setFiles(final ObservableList<MyFile> files) {
		this.filesProperty().set(files);
	}

	public final BooleanProperty isConnectedProperty() {
		return this.isConnected;
	}
	

	public final boolean isIsConnected() {
		return this.isConnectedProperty().get();
	}
	

	public final void setIsConnected(final boolean isConnected) {
		this.isConnectedProperty().set(isConnected);
	}
	
	
	
	
	

}
