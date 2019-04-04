package assignment4_trangnguyen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * In this program the user will be able to create members: 
 * - First user will add member of a crew (Name and jobs are mandatory, note is optional) 
 * the crew list will shows at the right hand 
 * - Name of the member must not be the same, an alert will shows if user have enter
 * duplicate name 
 * - User could be able to search and choose a specific member in crew list base on their number
 * to make it convenience I put number in the front of each member 
 * - User could delete crew member, crew member list will be update, 
 * their order number will be updated as well
 * - User can modify crew member information as well.
 * - After user satisfying with their crew group, they will add to text file "myData.txt"
 * - "MyData.txt" will add information without overwitting it 
 * 
 * 
 * - To read text file, click Read file button
 * - User are not allow to add member that already exist in Text file (base on name)
 * an alert will be showed if that member already exist in file, that member will be discard
 *
 * @author Trang Nguyen
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label message;
    @FXML
    private TextField name;
    @FXML
    private TextField note;
    @FXML
    private Button search;
    @FXML
    private Button searchInFile;
    @FXML
    private ComboBox<String> job;
    @FXML
    private Button clear;
    @FXML
    private Button addToCrew;
    @FXML
    private Button show;
    @FXML
    private Button save;

    @FXML
    private Button readFile;
    @FXML
    private Label nameBlanked;
    @FXML
    private Label jobBlank;

    @FXML
    private Label crewMember;
    @FXML
    private TextField showCrewMem;
    @FXML
    private Label searchResult;
    @FXML
    private TextField showCrewList;

    private ArrayList<Members> members = new ArrayList<>();
    private ArrayList<String> nameCollection = new ArrayList<>();
    private Jobs j;
    
    private File file;
    private String printCrew = "";
   

    // search a member by their number order
    @FXML
    private void handdleButtonSearch(ActionEvent event) {

        try {
            int numberNo = Integer.parseInt(showCrewMem.getText());

            if (numberNo <= members.size() && numberNo > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Infomation");
                VBox dialogPaneContent = new VBox();
                Label label = new Label(members.get(numberNo - 1).toString());

                dialogPaneContent.getChildren().addAll(label);
                alert.getDialogPane().setContent(dialogPaneContent);
                ButtonType delete = new ButtonType("Delete");
                ButtonType ok = new ButtonType("ok");
                ButtonType modify = new ButtonType("Modify");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(modify,delete, ok);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == delete) {
                    Alert alertDel = new Alert(Alert.AlertType.CONFIRMATION); 
                    alertDel.setContentText("Are you sure you want to delete?"); 
                    Optional<ButtonType> resultDel = alertDel.showAndWait();
                    if (resultDel.get() == ButtonType.OK){ 
                    members.remove(numberNo - 1);
                    message.setText("we have " + (members.size()) + " members now");
                    printCrew = "";
                    for (int x = 0; x < members.size(); x++) {
                        printCrew += (x + 1) + "." + members.get(x).toString() + "\n";
                    }
                    crewMember.setText(printCrew);
                    }
                    else if(resultDel.get() == ButtonType.CANCEL)
                            {alertDel.onCloseRequestProperty();}
                    
                    
                }
               // start modify 
            if (result.get() == modify){
            Stage stageModify = new Stage();
            stageModify.setTitle("Modify");
            VBox root = new VBox();   
            root.setPadding(new Insets(40, 40, 40, 40));
            root.setSpacing(20);
            
            HBox namebox = new HBox();
            Label newname = new Label("Name");
            TextField nameField = new TextField();
            Label error1 = new Label("");
            nameField.setText(members.get(numberNo - 1).getName());
            namebox.getChildren().addAll(newname,nameField,error1);

            root.getChildren().add(namebox);
            
                                 
            HBox jobbox = new HBox();
            Label newjobs = new Label("Job");
            Label error2 = new Label("");
            ComboBox newJob = new ComboBox();
        newJob.getItems().removeAll(newJob.getItems());
        newJob.getItems().addAll("CAPTAIN", "MECHANIC", "CHEF", "OPERATOR");
        newJob.setOnAction(e -> {
            switch (newJob.getSelectionModel().getSelectedIndex()) {
                case 0:
                    j = Jobs.CAPTAIN;
                    break;
                case 1:
                    j = Jobs.MECHANIC;
                    break;
                case 2:
                    j = Jobs.CHEF;
                    break;
                case 3:
                    j = Jobs.OPERATOR;
                    break;
            }
        });
             jobbox.getChildren().addAll(newjobs,newJob,error2);
             root.getChildren().add(jobbox);
             
            HBox notebox = new HBox();
            Label newnote = new Label();
            TextField noteField = new TextField("Note");
            noteField.setText(members.get(numberNo - 1).getNote());
            notebox.getChildren().addAll(newnote,noteField);
            root.getChildren().add(notebox);
            
            HBox btn = new HBox();
            Button confirm = new Button("Confirm");
            Button cancel = new Button ("Cancel");
            btn.getChildren().addAll(confirm,cancel);
            root.getChildren().add(btn);
            
            confirm.setOnAction(e -> {
                try{
            if (nameField.getText().isEmpty()) {
                error1.setText("Name couldn't be blanked");
                throw new Exception();
            }
            if (newJob.getSelectionModel().getSelectedIndex() < 0) {
                error2.setText("job couldn't be blanked");
                throw new Exception();    
                   
                }
            members.get(numberNo - 1).setName(nameField.getText());
            members.get(numberNo - 1).setJob(j);
            members.get(numberNo - 1).setNote(noteField.getText());
            
            printCrew = "";
                    for (int x = 0; x < members.size(); x++) {
                        printCrew += (x + 1) + "." + members.get(x).toString() + "\n";
                    }
                    crewMember.setText(printCrew);
             stageModify.close();
                }catch (Exception e1) {
                    e1.getCause();
                }
            });
            cancel.setOnAction(e -> {
                stageModify.close();
            });
            
            
        Scene scenemodify = new Scene(root, 400, 400);
        stageModify.setScene(scenemodify);
        stageModify.show();
                    
                }   
                // end modify
                else {
                    alert.onCloseRequestProperty();
                }

            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            searchResult.setText("Invalid input");
        }

    }

    // clear name and note field 
    @FXML
    private void handleButtonClear(ActionEvent event) {
        name.setText(null);
        note.setText(null);
    }

    // add member into a crew
    @FXML
    private void handleButtonAdd(ActionEvent event) {
        try {
            // if name and job blanked then show message
            if (name.getText().isEmpty()) {
                nameBlanked.setText("Name couldn't be blanked");
                throw new Exception();
            }
            if (job.getSelectionModel().getSelectedIndex() < 0) {
                jobBlank.setText("job couldn't be blanked");
                throw new Exception();
            }

            // check if that member already exist in crew list, if yes, show alert
            if (members.size() > 0) {
                for (int x = 0; x < members.size(); x++) {
                    if (name.getText().equals(members.get(x).getName())) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setContentText("Name already exist");
                        alert.showAndWait();
                        name.setText(null);// they have to enter different name
                        note.setText(null);
                        throw new Exception(); // not allow to create new member
                    }
                }
            }

            //
            message.setText("we have " + (members.size() + 1) + " members now");
            members.add(new Members(name.getText(), j, note.getText()));

            printCrew += (members.size()) + "." + members.get(members.size() - 1).toString() + "\n";
            crewMember.setText(printCrew);

            // after the member is added successfully, clear all error notifications and input field
            nameBlanked.setText(null);
            jobBlank.setText(null);
            name.setText(null);
            note.setText(null);

        } catch (Exception e) {
            e.getCause();
        }
    }

    @FXML
// save function,check if member already exist and save new member into the existing file without overwritting it 
    private void handleButtonSave(ActionEvent event) throws Exception {
        crewMember.setText(null); // delete context in label cause they already saved
        message.setText(" ");
        printCrew = "";

        FileWriter fw = null;
        PrintWriter output = null;

        // check if the member is already exist in the list, if yes then avoid adding (key : name )
        // start check
        // read file
        Scanner fileInput = null;
        String name = "";
        try {
            fileInput = new Scanner(file);
            fileInput.useDelimiter("[\n]");

            while (fileInput.hasNext()) {
                
                name = fileInput.next();
                String[] field = name.split(",");
                System.out.println(field.length);
                nameCollection.add(field[0]); // put all the name into an arraylist 
            
            }
        } catch (FileNotFoundException e) {
            System.out.println("File cant be readed");
        } finally {
            if (fileInput != null) {
                fileInput.close();
            }
        }

        // loop through nameList array and members array to see if there is a name duplicate
        for (int i = members.size() - 1; i >= 0; i--) {
            for (int j = 0; j < nameCollection.size(); j++) {
                if (members.get(i).getName().equals(nameCollection.get(j))) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setContentText(members.get(i).getName() + " already exist");
                    alert.showAndWait();
                    members.remove(members.get(i)); // remove that element if name has already existed
                    i--;
                }
            }
        }

        // end check
        // save all of the other member in to file without overwritting information
        try {
            fw = new FileWriter("myData.txt", true);
            BufferedWriter bf = new BufferedWriter(fw);
            output = new PrintWriter(bf);
            
            for (Members mem : members) {
                output.println( mem.getName()+","+ mem.getJob() + "," + mem.getNote());
            }
            

        } catch (IOException e) {
            System.out.println("Error" + e);

        } finally {
            if (output != null) {
                output.close();
            }
        }

// after save to file, clear the list to avoid overlapse information
        members.clear();
        searchResult.setText("save successfully");

    }

// read all of the member that we have been added     
    @FXML
    private void handdleButtonReadFile(ActionEvent event) throws Exception {
        Stage stageList = new Stage();
        stageList.setTitle("All members");
        VBox showMem = new VBox();

        Button btn3 = new Button("Close");
        Label lb4 = new Label("This is the whole group: ");
        Label lb5 = new Label();
        showMem.setPadding(new Insets(40, 40, 40, 40));

        showMem.getChildren().addAll(lb4, lb5, btn3);
        Scanner fileInput = null;
        String record = "";
        try {
            fileInput = new Scanner(file);
            while (fileInput.hasNext()) {
                record += fileInput.nextLine() + "\n";
            }

            lb5.setText(record);
        } catch (FileNotFoundException e) {
            System.out.println("File cant be readed");
        } finally {
            if (fileInput != null) {
                fileInput.close();
            }
        }

        btn3.setOnAction(e -> {
            stageList.close();
        });

        Scene sceneList = new Scene(showMem, 200, 250);

        stageList.setScene(sceneList);
        stageList.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        message.setText("we have 0 members now");
        // create file 
        file = new File("myData.txt");

        // innitialize the comboBox
        job.getItems().removeAll(job.getItems());
        job.getItems().addAll("CAPTAIN", "MECHANIC", "CHEF", "OPERATOR");
        job.setOnAction(e -> {
            switch (job.getSelectionModel().getSelectedIndex()) {
                case 0:
                    j = Jobs.CAPTAIN;
                    break;
                case 1:
                    j = Jobs.MECHANIC;
                    break;
                case 2:
                    j = Jobs.CHEF;
                    break;
                case 3:
                    j = Jobs.OPERATOR;
                    break;
            }
        });

    }

}
