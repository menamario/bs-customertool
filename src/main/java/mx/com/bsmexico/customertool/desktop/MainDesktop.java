package mx.com.bsmexico.customertool.desktop;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import mx.com.bsmexico.customertool.api.Desktop;
import mx.com.bsmexico.customertool.api.MenuNavigator;

public class MainDesktop extends Desktop {
	
	BorderPane layout;
	Pane pleca;
	
	public MainDesktop(final MenuNavigator menu) { 
		super(menu);
	}	


	@Override
	protected Pane buildDesktop() {
		setStyle("-fx-background-color: black");

		layout = new BorderPane();
		layout.prefHeightProperty().bind(super.heightProperty());
		layout.prefWidthProperty().bind(super.widthProperty());
		
	
		pleca = new Pane();
		pleca.prefWidthProperty().bind(super.widthProperty());
		pleca.setPrefHeight(5);
		pleca.setMaxHeight(5);
		pleca.setMinHeight(5);
		pleca.setStyle("-fx-background-color: black");
		
		
		
		
		layout.setTop(pleca);
		
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
		if(getWorkArea()!=null)
		BorderPane.setMargin(getWorkArea(), new Insets(0,10,0,10));
	}
	
	
	@Override
	public void updatePleca(String color, String name){
		pleca.setStyle("-fx-background-color: " + color);
	}
	
	
	
	
	

}
