package ftp.view;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import ftp.model.FTPConnection;
import ftp.model.FileType;
import ftp.model.MyFile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class RootController implements Initializable {

	// Model

	FTPConnection connection = new FTPConnection();

	private ObjectProperty<MyFile> selectedFile = new SimpleObjectProperty<>();

	// View

	@FXML
	private MenuItem conectarItem;

	@FXML
	private Button descargarButton;

	@FXML
	private MenuItem desconectarItem;

	@FXML
	private TableView<MyFile> filesTableView;
	@FXML
	private TableColumn<MyFile, String> nameTableCollumn;
	@FXML
	private TableColumn<MyFile, Number> sizeTableCollumn;
	@FXML
	private TableColumn<MyFile, FileType> typeTableCollumn;

	@FXML
	private BorderPane view;

	public RootController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// bindings

		filesTableView.itemsProperty().bind(connection.filesProperty());
		conectarItem.disableProperty().bind(connection.isConnectedProperty());
		desconectarItem.disableProperty().bind(connection.isConnectedProperty().not());

		selectedFile.bind(filesTableView.getSelectionModel().selectedItemProperty());
		descargarButton.disableProperty().bind(selectedFile.isNull());

		// cell value factory

		nameTableCollumn.setCellValueFactory(v -> v.getValue().nombreProperty());
		sizeTableCollumn.setCellValueFactory(v -> v.getValue().sizeProperty());
		typeTableCollumn.setCellValueFactory(v -> v.getValue().typeProperty());
		
		//listeners
		
		filesTableView.setRowFactory( tv -> {
		    TableRow<MyFile> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
		        	MyFile rowData = row.getItem();
		        	if(rowData.esDirectorio()) {
		        		connection.changeDirectory(rowData.getNombre());

		        	}
		        }
		    });
		    return row ;
		});

	}

	@FXML
	public void DesconectarAction(ActionEvent event) {
		connection.disconnect();
	}

	@FXML
	void conectarAction(ActionEvent event) throws IOException {
		connection.setServer("ftp.rediris.es");
//		connection.setServer("192.168.1.165");
		connection.setPuerto(21);
		
//		connection.setUsuario("anonymous");
//		connection.setPassword("");

		connection.connectAndLogin();
	}

	@FXML
	void descargarAction(ActionEvent event) {
	    MyFile selectedFile = this.selectedFile.get();
	    if (selectedFile != null) {
	        try {
	            // Crear el OutputStream para escribir el archivo descargado
	            String nombreArchivo = selectedFile.getNombre();
	            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(nombreArchivo));

	            // Descargar el archivo seleccionado
	            connection.downloadFile(selectedFile, outputStream);

	            // Cerrar el OutputStream
	            outputStream.close();

	            // Mostrar un mensaje de éxito
	            System.out.println("Archivo descargado con éxito.");
	        } catch (IOException e) {
	            // Mostrar un mensaje de error en caso de fallo
	            System.err.println("Error al descargar el archivo.");
	            e.printStackTrace();
	        }
	    }
	}


	public BorderPane getView() {
		return this.view;
	}

}
