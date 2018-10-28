package mx.com.bsmexico.customertool.desktop.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;

import com.sun.webkit.WebPage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import mx.com.bsmexico.customertool.api.Feature;
import mx.com.bsmexico.customertool.api.NavRoute.NavNode;
import mx.com.bsmexico.customertool.api.layouts.control.FeatureMenuNavigator;
import mx.com.bsmexico.customertool.api.layouts.control.TreeNavNode;

/**
 * @author jchr
 *
 */
public class DefaultMenuNavigator extends FeatureMenuNavigator {

	private Map<Integer, StackPane> levels;
	ToggleGroup tg1;
	ToggleGroup tg2;

	public DefaultMenuNavigator(final List<Feature> components) throws IllegalArgumentException {
		super(components);
		levels = new HashMap<>();
		init();
		
	}

	protected void init() {
		setStyle("-fx-background-color : white");
		tg1 = new ToggleGroup();
		tg2 = new ToggleGroup();

	}

	@Override
	protected Region getGraphicNavigatorNode(NavNode navNode, NODETYPE type) {
		Region graphic = null;
		switch (type) {
		case ROOT_NODE: {
			break;
		}
		case SECTION_NODE: {
			break;
		}
		case NAV_NODE: {
			if (navNode.getImg() == null) {
				graphic = new ToggleButton(navNode.getTitle());
			} else {
//				ImageView iv = new ImageView(new Image(navNode.getImg()));
//				iv.setFitWidth(126);
//				iv.setFitHeight(126);
//				graphic = new ToggleButton(navNode.getTitle(), iv);
				
				
				String initialEditview = null;
				try {
					initialEditview = this.getHtml(140, readFile(navNode.getImg(), Charset.defaultCharset()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				WebView browser = new WebView();
				browser.setContextMenuEnabled(false);
				browser.setMaxSize(140, 140);
				WebEngine webEngine = browser.getEngine();
				//webEngine.documentProperty().addListener(new WebDocumentListener(webEngine));

				browser.getEngine().loadContent(initialEditview);
				browser.getStyleClass().add("browser");
				//graphic = new ToggleButton(navNode.getTitle(), browser);
				graphic = new ToggleButton(null, browser);
				
			}
			graphic.setId(navNode.getName());
			((ToggleButton)graphic).setContentDisplay(ContentDisplay.TOP);
			

			break;
		}
		case LEAF_NODE: {
			if (navNode.getImg() == null) {
				graphic = new ToggleButton(navNode.getTitle());
			} else {
//				ImageView iv = new ImageView(new Image(navNode.getImg()));
//				iv.setFitWidth(126);
//				iv.setFitHeight(126);
//				graphic = new ToggleButton(navNode.getTitle(), iv);
				
				String initialEditview = null;
				try {
					initialEditview = this.getHtml(140, readFile(navNode.getImg(), Charset.defaultCharset()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				WebView browser = new WebView();
				browser.setContextMenuEnabled(false);
				browser.setMaxSize(140, 140);
				WebEngine webEngine = browser.getEngine();
				//webEngine.documentProperty().addListener(new WebDocumentListener(webEngine));

				browser.getEngine().loadContent(initialEditview);
				browser.getStyleClass().add("browser");
				graphic = new ToggleButton(null, browser);
				
			}
			graphic.setId(navNode.getName());

			((ToggleButton)graphic).setContentDisplay(ContentDisplay.TOP);
			
			((ToggleButton)graphic).setToggleGroup(tg1);
			 
			// TODO implementar style class
			// 

			break;
		}
		default: {
			graphic = new Pane();
		}

		}
		
		return graphic;
	}

	/**
	 * 
	 */
	public void render() {
		// Root : main pane
		HBox rootLayout = new HBox() {
			@Override
			protected void layoutChildren() {
				super.layoutChildren();
				setSpacing(10);
				// TODO acomodar tamaños y posiciones de las secciones
			}
		};
		rootLayout.setAlignment(Pos.CENTER_LEFT);
		// first level : sections
		List<TreeNavNode> sections = root.getChilden();
		getChildren().add(rootLayout);
		if (!sections.isEmpty()) {
			sections.forEach(s -> buildNavigation(s, rootLayout));
		}
	}

	/**
	 * @param section
	 * @param rootLayout
	 */
	private void buildNavigation(final TreeNavNode section, final Pane rootLayout) {
		if (section != null) {
			if (!section.getChilden().isEmpty()) {
				final VBox graphicSection = new VBox();
//				{
//					@Override
//					protected void layoutChildren() {
//						super.layoutChildren();
//						setSpacing(9);
//						// TODO acomodar tamaños y posiciones los nodos principales
//					}
//				};
				rootLayout.getChildren().add(graphicSection);
				graphicSection.setSpacing(9);


				double height = (getDesktop().getMaxHeight() - 89 - 60 - (section.getChilden().size() - 1 ) * 9 ) / section.getChilden().size();

				section.getChilden().forEach(s -> {
					ToggleButton graphic = (ToggleButton)s.getGraphic();
					graphic.getStyleClass().add("menu-toggle-button");
				    //((WebView)graphic.getGraphic()).setMinSize(233, height);
				    //((WebView)graphic.getGraphic()).setMaxSize(233, height);
				    graphic.setMinSize(233, height);
				    graphic.setMaxSize(233, height);
					graphicSection.getChildren().add(graphic);
					buildLevels(s, rootLayout);
				});

			}
		}
	}

	/**
	 * @param node
	 * @param rootLayout
	 */
	private void buildLevels(final TreeNavNode node, final Pane rootLayout) {
		final Integer deepLevel = node.deep();
		StackPane levelPane = null;
		final Parent graphic = node.getGraphic();
		if (node.getType() == NODETYPE.NAV_NODE) {
			if ((levelPane = this.levels.get(deepLevel)) == null) {
				levelPane = new StackPane() {
					@Override
					protected void layoutChildren() {
						super.layoutChildren();
						
						
						// TODO acomodar tamaños y posiciones de las secciones
					}
				};
				this.levels.put(deepLevel, levelPane);
				rootLayout.getChildren().add(levelPane);
				levelPane.setVisible(false);
			}
			final VBox submenu = new VBox() {
				@Override
				protected void layoutChildren() {
					super.layoutChildren();
					// TODO acomodar tamaños y posiciones de las secciones
					
				}
			};
			// bind with id
			submenu.setId(node.getId());
			submenu.setSpacing(9);
			double height = (getDesktop().getMaxHeight() - 89 - 60 - (node.getChilden().size() - 1 ) * 9 ) / node.getChilden().size();
			node.getChilden().forEach(s -> {
				Region g = s.getGraphic();
				g.getStyleClass().add("menu-toggle-button2");
				g.setMaxSize(157, height);
				g.setMinSize(157, height);
				
				submenu.getChildren().add(g);
			});
			submenu.setVisible(false);
			levelPane.getChildren().add(submenu);

			node.getChilden().forEach(n -> buildLevels(n, rootLayout));

			// Set event when click button
			graphic.setOnMouseClicked(evt -> {
				fireEvent(evt);
				final StackPane _levelPane = levels.get(deepLevel);
				_levelPane.setVisible(true);
				_levelPane.getChildren().forEach(sm -> {
					// find submenu and set visible
					if(node.getId().equals(sm.getId())){
						sm.setVisible(!sm.isVisible());
						
					}
					
				});
				// hide others levels
				hideLevels(deepLevel);
			});

		} else {
			if (node.getType() == NODETYPE.LEAF_NODE) {
				// Set event when click button
				
				graphic.setOnMouseClicked(evt -> {
					hideLevels(deepLevel-1);
					// show work area
					if (node.getElement() != null && node.getElement() instanceof Feature) {
						Feature component = (Feature) node.getElement();
						component.launch();				
					}
				});
			}
		}

	}

	/**
	 * @param excludedLevel
	 */
	private void hideLevels(final int initLevel) {
		Set<Integer> klevels = levels.keySet();
		StackPane level = null;
		for (Integer key : klevels) {
			if (key > initLevel) {
				level = levels.get(key);
				level.setVisible(false);
				level.getChildren().forEach(nd -> {
					nd.setVisible(false);
				});
			}

		}
	}
	
	static String readFile(InputStream in, Charset encoding) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer response = new StringBuffer();
		for (String line; (line = reader.readLine()) != null; response.append(line))
			;
		return response.toString();
	}

	private String getHtml(int circle, int image, String circleColor, String svg) {
		StringBuffer sb = new StringBuffer();
		int radio = circle / 2;
		sb.append(String.format(
				"<html><head></head><body style='margin: 0'><svg  width='%d' height='%d'><circle cx='%d' cy='%d' r='%d' fill='%s'></circle><svg>",
				circle, circle, radio, radio, radio, circleColor));
		int desplazamientoImagen = (circle - image) / 2;
		sb.append(String.format("<svg x='%d' y='%d' width='%d' height='%d' style='fill:white'>", desplazamientoImagen,
				desplazamientoImagen, image, image));
		sb.append(svg);
		sb.append("</svg></body></html>");
		return sb.toString();
	}
	
	private String getHtml(int image, String svg) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format(
				"<html><head></head><body style='margin: 0'>"));
		
		sb.append(String.format("<svg width='%d' height='%d'>", image, image));
		sb.append(svg);
		sb.append("</svg></body></html>");
		return sb.toString();
	}
	
	class WebDocumentListener implements ChangeListener<Document> { 
	    private final WebEngine webEngine; 

	    public WebDocumentListener(WebEngine webEngine) { 
	        this.webEngine = webEngine; 
	    } 

	    @Override 
	    public void changed(ObservableValue<? extends Document> arg0, 
	            Document arg1, Document arg2) { 
	        try { 
	            // Use reflection to retrieve the WebEngine's private 'page' field. 
	            Field f = webEngine.getClass().getDeclaredField("page"); 
	            f.setAccessible(true); 
	            WebPage page = (WebPage) f.get(webEngine); 
	            // Set the background color of the page to be transparent. 
	            page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB()); 
	        } catch (Exception e) { 
	            System.out.println("Error: " + e);
	        } 
	    } 
	}


}
