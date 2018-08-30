package mx.com.bsmexico.customertool.desktop;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import mx.com.bsmexico.customertool.api.Desktop;
import mx.com.bsmexico.customertool.api.MenuNavigator;

public class MainDesktop extends Desktop {
	
	BorderPane layout;
	
	public MainDesktop(final MenuNavigator menu) { 
		super(menu);
	}	


	@Override
	protected Pane buildDesktop() {
		setStyle("-fx-background-color: black");

		layout = new BorderPane();
		layout.prefHeightProperty().bind(super.heightProperty());
		layout.prefWidthProperty().bind(super.widthProperty());
		
		
		setWorkArea(buildDefaultWorkArea());
		
		showMenu();
		loadWorkArea();
		
		return layout;
	}
	
	public Pane buildDefaultWorkArea(){
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: black");
		pane.setMinSize(500, getMinHeight());
		return pane;
	}


	@Override
	public void hideMenu() {
		layout.setLeft(null);
	}
	
	@Override
	public void showMenu() {
		layout.setLeft(getMenu());
		BorderPane.setMargin(getMenu(), new Insets(15,0,0,32));
	}


	@Override
	public void loadWorkArea() {
		layout.setCenter(getWorkArea());
		BorderPane.setMargin(getWorkArea(), new Insets(15,10,0,10));
	}
	
	
	
	
	

}
