package mx.com.bsmexico.customertool.desktop.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import mx.com.bsmexico.customertool.api.Feature;
import mx.com.bsmexico.customertool.api.NavRoute.NavNode;

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
		setStyle("-fx-background-color : black");
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
				graphic = new ToggleButton(navNode.getTitle(), new ImageView(new Image(navNode.getImg())));
			}
			graphic.setId(navNode.getName());
			//((ToggleButton)graphic).setMaxSize(261, 216);
			//((ToggleButton)graphic).setMinSize(261, 216);
			((ToggleButton)graphic).setContentDisplay(ContentDisplay.TOP);
			graphic.getStyleClass().add("menu-toggle-button");
			//((ToggleButton)graphic).setToggleGroup(tg1);
			break;
		}
		case LEAF_NODE: {
			if (navNode.getImg() == null) {
				graphic = new ToggleButton(navNode.getTitle());
			} else {
				graphic = new ToggleButton(navNode.getTitle(), new ImageView(new Image(navNode.getImg())));
				
			}
			graphic.setId(navNode.getName());

			((ToggleButton)graphic).setContentDisplay(ContentDisplay.TOP);
			graphic.getStyleClass().add("menu-toggle-button");
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


				double height = (getDesktop().getMaxHeight() - 115 - 15 - (section.getChilden().size() - 1 ) * 9 ) / section.getChilden().size();

				section.getChilden().forEach(s -> {
					Region graphic = s.getGraphic();
				    graphic.setMinSize(261, height);
				    graphic.setMaxSize(261, height);
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
					setSpacing(9);
				}
			};
			// bind with id
			submenu.setId(node.getId());
			node.getChilden().forEach(s -> {
				Region g = s.getGraphic();
				g.setMaxSize(176, 328);
				g.setMinSize(176, 328);
				
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
					sm.setVisible(node.getId().equals(sm.getId()));
				});
				// hide others levels
				hideLevels(deepLevel);
			});

		} else {
			if (node.getType() == NODETYPE.LEAF_NODE) {
				// Set event when click button
				graphic.setOnMouseClicked(evt -> {
					// show work area
					if (node.getElement() != null && node.getElement() instanceof Feature) {
						Feature component = (Feature) node.getElement();
						component.launch();
//						if (!getDesktop().getWorkArea().getChildren().isEmpty()) {
//							getDesktop().getWorkArea().getChildren().remove(0);
//						}
//						getDesktop().getWorkArea().getChildren().add(component.getLayout().getNode());					
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

}
