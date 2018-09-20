package mx.com.bsmexico.customertool.desktop;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import mx.com.bsmexico.customertool.api.Desktop;
import mx.com.bsmexico.customertool.api.MenuNavigator;

public class MainDesktop extends Desktop {
	
	BorderPane layout;
	Pane pleca;
	Region opaqueLayer;
	
	public MainDesktop(final MenuNavigator menu) { 
		super(menu);
	}	


	@Override
	protected Pane buildDesktop() {
		setStyle("-fx-background-color: black");
		
		opaqueLayer = new Region();
	    opaqueLayer.setStyle("-fx-background-color: #00000099;");
	    opaqueLayer.setVisible(false);
	    
	    StackPane sp = new StackPane();

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
		sp.getChildren().addAll(layout,opaqueLayer);
		
		return sp;
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
		BorderPane.setMargin(getMenu(), new Insets(15,0,0,65));
	}


	@Override
	public void loadWorkArea() {
		layout.setCenter(getWorkArea());
		if(getWorkArea()!=null)
		BorderPane.setMargin(getWorkArea(), new Insets(0,20,0,45));
	}
	
	
	@Override
	public void updatePleca(String color, String name){
		pleca.setStyle("-fx-background-color: " + color);
	}
	
	@Override
	public void opacar(){
		this.opaqueLayer.setVisible(true);
	}
	
	@Override
	public void desOpacar(){
		this.opaqueLayer.setVisible(false);
	}
	
	
	

}
