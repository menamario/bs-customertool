package mx.com.bsmexico.customertool.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mx.com.bsmexico.customertool.api.Desktop;
import mx.com.bsmexico.customertool.api.Feature;
import mx.com.bsmexico.customertool.api.MenuNavigator;
import mx.com.bsmexico.customertool.desktop.MainDesktop;
import mx.com.bsmexico.customertool.desktop.menu.DefaultMenuNavigator;

/**
 * Application
 * 
 * @author jchr
 *
 */
public class AppCustomerTool extends Application {
	// Pane root
	private BorderPane root;

	@Override
	public void init() throws Exception {
		root =  new BorderPane();
		//root.setStyle("-fx-background-color: #000000");

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		primaryStage.setTitle("Herramienta Banco Sabadell");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logoSabadellCircle.png")));
		
		
		
		
		ServiceLoader<Feature> loader = ServiceLoader.load(Feature.class);
		List<Feature> features = new ArrayList<Feature>();
		for (Feature om : loader) {
			features.add(om);
		}

		

		
		Pane canvas = new Pane();
		canvas.setStyle("-fx-background-color: #006dff;");
		canvas.setPrefSize(1280, 115);
		canvas.setMaxHeight(115);
		canvas.setMinHeight(115);
		
		
		Image image = new Image("/img/logoSabadell.png");
		ImageView iv1 = new ImageView();
		iv1.setImage(image);
		iv1.relocate(32, 25);
		iv1.setFitWidth(250);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		
		canvas.getChildren().addAll(iv1);
		root.setTop(canvas);
		
		
		
		MenuNavigator menu = new DefaultMenuNavigator(features);
		Desktop desktop = new MainDesktop(menu);
		menu.setDesktop(desktop);
		
		
		
		root.setCenter(desktop);
		Scene scene = new Scene(root, 1280, 790);
		
//		desktop.setMinSize(scene.getWidth(), scene.getHeight()-canvas.getMinHeight());
//		desktop.setPrefSize(scene.getWidth(), scene.getHeight()-canvas.getMinHeight());
		desktop.maxHeightProperty().bind(scene.heightProperty());
		desktop.render();
		
		

		
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		primaryStage.setScene(scene);
		
		
		
		
		primaryStage.show();
	}

	@SuppressWarnings("unused")
	private ImageView getImage(final String file, double h, double w) throws FileNotFoundException {
		final FileInputStream input = new FileInputStream(getClass().getClassLoader().getResource(file).getFile());
		Image image = new Image(input);
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(h);
		imageView.setFitWidth(w);
		return imageView;
		
	}
	@SuppressWarnings("unused")
	private FileInputStream getImageInput(final String file, double h, double w) throws FileNotFoundException {
		final FileInputStream input = new FileInputStream(getClass().getClassLoader().getResource(file).getFile());		
		return input;
		
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
