package userInterface;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.BrokenBarrierException;
import javax.swing.JButton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import controllers.MainController;
import database.HistoryManagment;

import com.jfoenix.controls.JFXDrawer.DrawerDirection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import main.MainClass;

public class History implements Initializable{
	
	
	@FXML
	private BorderPane borderPaneHistory;
	@FXML
	private JFXTreeTableView<HistoryStoreView> table;
	
	JFXTreeTableColumn <HistoryStoreView, String> dateCol=new JFXTreeTableColumn<HistoryStoreView,String>("Date");
	
	JFXTreeTableColumn <HistoryStoreView, String> linkCol=new JFXTreeTableColumn<HistoryStoreView,String>("Links");
	
	JFXTreeTableColumn <HistoryStoreView, String> timeCol=new JFXTreeTableColumn<HistoryStoreView,String>("Time");
	
	@FXML
	JFXTextField search;
	
	@FXML
	 private VBox vBox;
	 @FXML
	 private JFXButton btn;
	@FXML
	private TreeView treeView;
	@FXML
	private HBox hBox;
	
	private ArrayList<TreeItem> storeItems;
	private HistoryTree hs;
	private TreeItem rootItem;

	
	private static TabPane tabPane;
	Tab addTab;
	 
	//Lists for maintaining different Histories
	ObservableList<HistoryStoreView> fullHistory=FXCollections.observableArrayList();
	ObservableList<HistoryStoreView> pastHours=FXCollections.observableArrayList();
	ObservableList<HistoryStoreView> todayHistory=FXCollections.observableArrayList();
	ObservableList<HistoryStoreView> yesterdayHistory=FXCollections.observableArrayList();
	ObservableList<HistoryStoreView> pastWeekHistory=FXCollections.observableArrayList();
	ObservableList<HistoryStoreView> pastMonthHistory=FXCollections.observableArrayList();
		
	EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
		    handleMouseClicked(event);
		};
	   private void handleMouseClicked(MouseEvent event) {
		    Node node = event.getPickResult().getIntersectedNode();
		    // Accept clicks only on node cells, and not on empty spaces of the TreeView
		    if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
		        String name = (String) ((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue();
		        System.out.println(name);
		        if(name.equals("History"))
		        {
		        	addListInTable(fullHistory);	
		        	
		        }
		        if(name.equals("Past hour"))
		        {
		        	addListInTable(pastHours);
		        }
		        if(name.equals("Today"))
		        {
		        addListInTable(todayHistory);
		        }
		        
		        if(name.equals("Yesterday"))
		        {
		        addListInTable(yesterdayHistory);
		        }
		        
		        if(name.equals("Past Week"))
		        {
		        addListInTable(pastWeekHistory);
		        }
		        
		        if(name.equals("Past Month"))
		        {	
		        addListInTable(pastMonthHistory);	
		        }
		        
		        borderPaneHistory.setCenter(table);
		        
		        }
		    }
		
	   public void initializeListsWithData()
	   {
		   pastHours = HistoryManagment.pastHoursHistory(pastHours, -1);
		   todayHistory=HistoryManagment.getHistory(todayHistory, 0);
		   yesterdayHistory=HistoryManagment.getHistory(yesterdayHistory, -1);
		   pastWeekHistory=HistoryManagment.getHistory(pastWeekHistory, -7);
		   pastMonthHistory=HistoryManagment.getHistory(pastMonthHistory, -30);
		   
		   
	   }
	   
	   public void initializingView()
	   {
		  
		 //Border pane left View
		   hs = new HistoryTree();
		   storeItems = hs.getStoreItems();
		   rootItem = new TreeItem("History");
		   rootItem.getChildren().addAll(storeItems);
		   treeView.setRoot(rootItem);
		   treeView.setPrefWidth(200);
		   borderPaneHistory.setLeft(treeView); 
		   treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
		    //Border pane Middle view
		    fullHistory=HistoryManagment.fullHistoryShow(fullHistory); 
		    addListInTable(fullHistory);
		    borderPaneHistory.setCenter(table);
		  //  drawersStack.setContent(vBox);
		    //all View in tab
		    
		    tabPane=MainController.tabPane;
			addTab= new Tab("pk " + (tabPane.getTabs().size() + 1));
			addTab.setContent(borderPaneHistory);	
			tabPane.getTabs().add(addTab);
		    tabPane.getSelectionModel().select(addTab);
	   }
	   	
	  
	public void addListInTable (ObservableList<HistoryStoreView> list)
	   {
		dateCol.setPrefWidth(150);
		dateCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
						return param.getValue().getValue().date1;
					}
				});
		linkCol.setPrefWidth(150);
		linkCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
						return param.getValue().getValue().link1;
					}
				});
		timeCol.setPrefWidth(150);
		timeCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
						return param.getValue().getValue().time1;
					}
				});
		final TreeItem<HistoryStoreView> root = new RecursiveTreeItem<HistoryStoreView>(list,RecursiveTreeObject::getChildren);
		table.getColumns().setAll(dateCol, linkCol, timeCol);
		table.setRoot(root);
		table.setShowRoot(false);
		
		
		


	   }
	  	
	@FXML
	    void deleteHistory(MouseEvent event) {
		
			//confirmation dialogue  
		   Alert alert = new Alert(AlertType.CONFIRMATION);
		   alert.setTitle("Confirmation Dialog");
		   alert.setHeaderText("History");
		   alert.setContentText("Are you want to delete all History ?");
		   Optional<ButtonType> result = alert.showAndWait(); 
		  
		   if (alert.getResult()== ButtonType.OK){
		       table.setRoot(null);
		       table.refresh();
		       fullHistory.clear();
		       pastHours.clear();
		       pastMonthHistory.clear();
		       pastWeekHistory.clear();
			   yesterdayHistory.clear();
			   todayHistory.clear();
			   
			   // information dialogue 
			   alert = new Alert(AlertType.INFORMATION);
			   alert.setTitle("History");
			   alert.setHeaderText(null);
			   String s ="History has been deleted!";
			   alert.setContentText(s);
			   alert.show();
		   }
		   HistoryManagment.deleteFromDatabase();
		   
	    }
	   
	@FXML 
	public void SearchDataInTable()
	{
		System.out.println("lalala");
		search.textProperty().addListener((o,oldVal,newVal)->{
		    table.setPredicate(HistoryStoreView -> HistoryStoreView.getValue().time1.get().contains(newVal)
		                || HistoryStoreView.getValue().date1.get().contains(newVal)
		                || HistoryStoreView.getValue().link1.get().contains(newVal));
		});

	}
	   public static  ObservableList addDataInList(String link,String time ,String date,ObservableList list)
	   {
		   list.add(new HistoryStoreView(date,link,time));
		   return list;
	   }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("kakaka");
			initializingView();
			initializeListsWithData();
			
			
		}
				
	}


	class HistoryStoreView extends RecursiveTreeObject<HistoryStoreView> {
	StringProperty date1;
	StringProperty link1;
	StringProperty time1;
	
	public HistoryStoreView(String date,String link, String time ){
		this.date1 = new SimpleStringProperty(date);
		this.link1 = new SimpleStringProperty(link);
		this.time1 = new SimpleStringProperty(time);
	}
}	


			 //method call to fetch the data from history table. 
		
//----------------------------------------plugging different Views in the GUI-------------------------------------------//	   
	
	
	