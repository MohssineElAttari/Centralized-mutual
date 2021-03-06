package com.mutuelle.project.controller;
import com.mutuelle.project.implement.FactoryDAO;
import com.mutuelle.project.model.Client;
import com.mutuelle.project.model.Officials;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.security.auth.callback.ConfirmationCallback;
import java.io.FileReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class PanelController implements Initializable {
    public static int id;

    @FXML
    private Button btnSave;
    @FXML
    private TextField inpBadge;
    @FXML
    private TextField inpCompany;
    @FXML
    private TextArea inpAddress;
    @FXML
    private TextField inpCin;
    @FXML
    private TextField inpPasport;
    @FXML
    private RadioButton checkCin;
    @FXML
    private RadioButton checkPasport;
    @FXML
    private TextField inpFname;
    @FXML
    private TextField inpLname;
    @FXML
    private TextField inpEmail;
    @FXML
    private Label msgBadg;
    @FXML
    private Label msgCompany;
    @FXML
    private Label msgAdrress;
    @FXML
    private Label msgCin;
    @FXML
    private Label msgFname;
    @FXML
    private Label msgLname;
    @FXML
    private Label msgEmail;
    @FXML
    private DatePicker inpDateStart;
    @FXML
    private Label lbPassport;
    @FXML
    private Label lbCin;
    @FXML
    private ComboBox<Object> cmbN;
    @FXML
    private Label lbPhone;
    @FXML
    private TextField inpPhone;
    @FXML
    private Label msgDate;

    @FXML
    private TextField fil_fname;
    @FXML
    private TextField fil_lname;
    @FXML
    private TextField fil_email;
    @FXML
    private TextField fil_badge;
    @FXML
    private TextField filcin;

    //labels show information
    @FXML
    private Label official_name;
    @FXML
    private Label official_cin;
    @FXML
    private Label official_phone;
    @FXML
    private Label official_email;

    @FXML
    private Label ent_name;
    @FXML
    private Label ent_address;
    @FXML
    private Label ent_phone;
    @FXML
    private Label ent_web;

    @FXML
    private TableView<Client> tableView;
    @FXML
    private TableColumn<Client, String> badgeNumber;
    @FXML
    private TableColumn<Client, String> company;
    @FXML
    private TableColumn<Client, String> cin;
    @FXML
    private TableColumn<Client, String> passport;
    @FXML
    private TableColumn<Client, String> clmfname;
    @FXML
    private TableColumn<Client, String> lname;
    @FXML
    private TableColumn<Client, String> phone;
    @FXML
    private TableColumn<Client, String> email;
    @FXML
    private TableColumn<Client, String> address;
    @FXML
    private TableColumn<Client, String> dateStart;
    @FXML
    private TableColumn<Client, String> created_at;

    @FXML
    private Tab showClientP;
    @FXML
    private Tab addClientP;
    @FXML
    private Tab homeP;
    @FXML
    private TabPane tabPane;

    @FXML
    private LineChart chartLine;
    @FXML
    private StackedBarChart stakedBarChart;
    @FXML
    private AreaChart areaChart;

    public int cmp = 0;
    private Officials officials;

    private List<Client> clientList = new ArrayList<>();
    ObservableList<Client> clientData;
    private ConfirmationCallback MessageBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        officials = FactoryDAO.getOfficialById(id);
        clientList = FactoryDAO.getListClientForOfficial(id);
        fillCmb();
        fillLabelsInfo();
        fillTable();
        statisticsDateStart();
        statisticsCreatedDay();
        statisticsCreatedYear();

    }

    public void statisticsDateStart() {

        XYChart.Series series = FactoryDAO.getStatisticMonth();

        chartLine.getData().add(series);
        Set<Node> nodes = chartLine.lookupAll(".series" + 0);
        for (Node n : nodes) {
            n.setStyle("-fx-background-color: black, white;\n"
                    + "    -fx-background-insets: 0, 2;\n"
                    + "    -fx-background-radius: 5px;\n"
                    + "    -fx-padding: 5px;");
        }
    }

    public void statisticsCreatedDay() {

        XYChart.Series series = FactoryDAO.getStatisticDayCreated();
        stakedBarChart.getData().add(series);
    }

    public void statisticsCreatedYear() {

        XYChart.Series series = FactoryDAO.getStatisticYearCreated();
        areaChart.getData().add(series);
    }


    public void filterTableClient(String badge, String fName, String lName, String cin, String email) {

        clientList = FactoryDAO.filterClient(badge, fName, lName, cin, email);

    }

    public void onFilterClick(ActionEvent event) {
        String badge = !fil_badge.getText().isEmpty() ? fil_badge.getText() : null;
        String fName = fil_fname.getText().length() > 0 ? fil_fname.getText() : null;
        String lName = fil_lname.getText().length() > 0 ? fil_lname.getText() : null;
        String cin = filcin.getText().length() > 0 ? filcin.getText() : null;
        String email = fil_email.getText().length() > 0 ? fil_email.getText() : null;

        filterTableClient(badge, fName, lName, cin, email);
        fillTable();
    }

    public void fillLabelsInfo() {
        official_cin.setText(officials.getCin());
        official_email.setText(officials.getEmail());
        official_phone.setText(officials.getPhone());
        official_name.setText(officials.getFirstname() + " " + officials.getLastname());

        ent_name.setText(officials.getEntity().getEnt_name());
        ent_phone.setText(officials.getEntity().getEnt_phone());
        ent_address.setText(officials.getEntity().getEnt_address());
        ent_web.setText(officials.getEntity().getEnt_site());
    }

    public void fillTable() {

        clientData = FXCollections.observableArrayList(clientList);

        badgeNumber.setCellValueFactory(new PropertyValueFactory<Client, String>("badgenumber"));
        company.setCellValueFactory(new PropertyValueFactory<Client, String>("CompanyName"));
        clmfname.setCellValueFactory(new PropertyValueFactory<Client, String>("Firstname"));
        lname.setCellValueFactory(new PropertyValueFactory<Client, String>("Lastname"));
        phone.setCellValueFactory(new PropertyValueFactory<Client, String>("Phone"));
        cin.setCellValueFactory(new PropertyValueFactory<Client, String>("cin"));
        email.setCellValueFactory(new PropertyValueFactory<Client, String>("Email"));
        address.setCellValueFactory(new PropertyValueFactory<Client, String>("Address"));
        passport.setCellValueFactory(new PropertyValueFactory<Client, String>("passport"));
        dateStart.setCellValueFactory(new PropertyValueFactory<Client, String>("DateStart"));
        created_at.setCellValueFactory(new PropertyValueFactory<Client, String>("Created_at"));
        tableView.getItems().setAll(clientData);
    }

    public void saveInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        cmp = 0;
        msgBadg.setText(badgeCk());
        msgCompany.setText(companyCk());
        msgAdrress.setText(addressCk());
        msgFname.setText(fnameCk());
        msgLname.setText(lnameCk());
        msgEmail.setText(emailCk());
        lbPhone.setText(phoneCk());
        msgDate.setText(dateCk());

        if (checkCin.isSelected()) {
            msgCin.setText(cinCk());
            inpPasport.setText(null);
        }
        if (checkPasport.isSelected()) {
            msgCin.setText(passportCk());
            inpCin.setText(null);
        }

        if (cmp == 0) {
            Client client = new Client(inpBadge.getText(), inpCin.getText(), inpPasport.getText(), inpFname.getText(), inpLname.getText(), cmbN.getSelectionModel().getSelectedItem() + "-" + inpPhone.getText(), inpEmail.getText(), inpAddress.getText(), inpCompany.getText(), inpDateStart.getValue(), PanelController.id, LocalDate.now());
            Client clientInserted = FactoryDAO.insertClient(client);

            if (clientInserted == null) {

                alert.setTitle("badge work or cin or passport or email already exists in data base");
                alert.setHeaderText("Message error");
                alert.setContentText("Client exists in data base");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                        //showClientP.getSelectionModel().select(someTab);
                    }
                });
            } else {
                clientList.add(clientInserted);
                fillTable();
                inpBadge.clear();
                inpCin.clear();
                inpPasport.clear();
                inpPhone.clear();
                inpEmail.clear();
                inpCompany.clear();
                inpAddress.clear();
                inpDateStart.getEditor().clear();
                inpFname.clear();
                inpLname.clear();


                alert.setTitle("Client Add Successfully");
                alert.setHeaderText("Message Success");
                alert.setContentText("Client " + client.getFirstname() + " " + client.getLastname() + " add with success");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                        selectionModel.select(1);
                    }
                });
            }
        }
        statisticsDateStart();
        statisticsCreatedDay();
        statisticsCreatedYear();
    }

    public void fillCmb() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\otman\\IdeaProjects\\demo1\\src\\main\\resources\\com\\example\\demo1\\json\\paysCode.json")) {
            Object obj = jsonParser.parse(reader);
            JSONArray employeeList = (JSONArray) obj;
            cmbN.getItems().clear();
            for (Object o : employeeList) {
                JSONObject employee = (JSONObject) o;
                cmbN.getItems().add(employee.get("code") + " " + employee.get("dial_code"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String dateCk() {
        if (inpDateStart.getValue() == null) {
            cmp++;
            return "you must be enter date start !!";
        }
        return "";
    }

    public String badgeCk() {
        if (inpBadge.getText().length() < 10 || inpBadge.getText().isEmpty()) {
            cmp++;
            return "Badge must be 10 characters !!";
        }
        return "";
    }

    public String fnameCk() {
        if (inpBadge.getText().length() > 50 || inpBadge.getText().isEmpty()) {
            cmp++;
            return "first name not valide !!";
        }
        return "";
    }

    public String lnameCk() {
        if (inpBadge.getText().length() > 50 || inpBadge.getText().isEmpty()) {
            cmp++;
            return "last name not valide  !!";
        }
        return "";
    }


    public String companyCk() {
        if (inpCompany.getText().length() > 50 || inpCompany.getText().isEmpty()) {
            cmp++;
            return "not accept !!";
        }
        return "";
    }

    public String addressCk() {
        if (inpAddress.getText().isEmpty()) {
            cmp++;
            return "you must be enter address !!";
        }
        return "";
    }

    public String cinCk() {
        if (inpCin.getText().isEmpty() || !inpCin.getText().matches("[a-zA-Z]{2}\\d{6}")) {
            cmp++;
            return "cin not valide!!";
        }
        return "";
    }

    public String passportCk() {
        if (inpPasport.getText().isEmpty() || !inpPasport.getText().matches("[a-zA-Z]{2}\\d{7}")) {
            cmp++;
            return "Passport is not valide!!";
        }
        return "";
    }

    public String emailCk() {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (inpEmail.getText().isEmpty() || !inpEmail.getText().matches(regexPattern)) {
            cmp++;
            return "Email is not valide!!";
        }
        return "";
    }

    public void cinInp() {
        inpCin.visibleProperty().setValue(true);
        lbCin.visibleProperty().setValue(true);

        inpPasport.visibleProperty().setValue(false);
        lbPassport.visibleProperty().setValue(false);

    }

    public void passInp() {
        inpPasport.visibleProperty().setValue(true);
        lbPassport.visibleProperty().setValue(true);

        inpCin.visibleProperty().setValue(false);
        lbCin.visibleProperty().setValue(false);

    }

    public String phoneCk() {

        if (inpPhone.getText().isEmpty() || inpPhone.getText().length() != 9) {
            cmp++;

            return "Phone number in not valid";
        }
        if (cmbN.getSelectionModel().getSelectedItem().equals("MA +212") && (inpPhone.getText().charAt(0) != '6' && inpPhone.getText().charAt(0) != '7')) {
            cmp++;
            System.out.println(inpPhone.getText().charAt(0));
            return "Phone number in not valid first 6 or 7";
        }
        return "";
    }


}
