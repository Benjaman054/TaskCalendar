import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import java.util.Calendar;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class CalendarController {
	
	List<Button> buttons = new ArrayList<>();
	
	private static final String[] MONTHS = {
		    "January", "February", "March", "April", "May", "June",
		    "July", "August", "September", "October", "November", "December"
		};

    @FXML
    private ComboBox<String> month_selector;

    @FXML
    private ComboBox<Integer> year_selector;
    
    @FXML
    private GridPane gridCalendar;
    
   
    
    @FXML
    private void initialize() {
    	for(int i = 1 ; i <= 6 ; i++) {
    		for(int j = 0 ; j <=6 ; j++) {
    			Button btn = (Button) gridCalendar.lookup("#" + String.format("day%d%d" , i , j));
    			buttons.add(btn);
    		}
    	}
    	
    	Calendar c = Calendar.getInstance();
    	
    	for(int y = 2000 ; y <= 2100 ; y++) {
    		year_selector.getItems().add(y);
    	}
    	month_selector.getItems().addAll(MONTHS);
    	year_selector.setValue(c.get(Calendar.YEAR));
    	month_selector.setValue(MONTHS[c.get(Calendar.MONTH)]);
    	c.set(Calendar.DAY_OF_MONTH, 1);
    	fillGrid(c.get(Calendar.DAY_OF_WEEK), c.getActualMaximum(Calendar.DAY_OF_MONTH));
    	year_selector.getSelectionModel()
    	.selectedItemProperty().addListener((obs, oldYear, newYear) ->{
    		if(newYear != null) {
    			c.set(Calendar.YEAR, newYear);
    			fillGrid(c.get(Calendar.DAY_OF_WEEK), c.getActualMaximum(Calendar.DAY_OF_MONTH));
    		}
    	});
    	
    	month_selector.getSelectionModel()
    	.selectedItemProperty().addListener((obs, oldMonth, newMonth)->{
    		if(newMonth != null) {
    			int i = getMonthindex(newMonth);
    			c.set(Calendar.MONTH, i);
    			fillGrid(c.get(Calendar.DAY_OF_WEEK), c.getActualMaximum(Calendar.DAY_OF_MONTH));

    		}
    	});
    	
    	
    }
    public void fillGrid(int start_day , int daysInMonth) {
    	int day_count = 1;
    	for(int b = 0 ; b < 42 ; b++) {
    		Button btn = buttons.get(b);
			if(b < start_day - 1 || b >= start_day - 1 + daysInMonth)
			{
				btn.setVisible(false);
			}
			else {
				btn.setVisible(true);
				buttons.get(b).setText(String.format("%d",day_count));
				day_count++;
			}
			
		}
    
    }
    
    
    
    
    @FXML
    void openTasksWindow(ActionEvent event) throws Exception {
    	String dayText = ((Button) event.getSource()).getText();
    	String month = (String) month_selector.getValue();
		String date = String.format("%d/%d/%d", Integer.valueOf(dayText) , getMonthindex(month) + 1  , year_selector.getValue());
		Calendar c = Calendar.getInstance();
		c.set(year_selector.getValue() , getMonthindex(month) , Integer.valueOf(dayText));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tasks.fxml"));
		Parent root = loader.load();
		TasksController tasksController = loader.getController();
		tasksController.setDate(c);
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("Tasks " + date);
		stage.setScene(scene);
		stage.show();
    }
    
    public int getMonthindex(String month) {
    	int i=0;
    	while(!month.equals(MONTHS[i])) i++;
    	return i;
    }

}
