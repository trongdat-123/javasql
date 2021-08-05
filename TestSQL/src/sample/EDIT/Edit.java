package sample.EDIT;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.DatabaseConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Edit implements Initializable {

    @FXML
    private TextField Name;
    @FXML
    private TextField IdentityCard;
    @FXML
    private DatePicker Date;
    @FXML
    private TextField Money;

    //Tạo table
    @FXML
    private TableView<Card> table1;
    @FXML
    private TableColumn<Card, String> typeColumn;
    @FXML
    private TableColumn<Card, String> nameColumn;
    @FXML
    private TableColumn<Card, String> identityCardColumn;
    @FXML
    private TableColumn<Card, String> dateColumn;
    @FXML
    private TableColumn<Card, Double> moneyColumn;
    @FXML
    private TableColumn<Card, Double> feeColumn;

    private ObservableList<Card> cardList;
    //DataBase
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection conn = databaseConnection.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardList = FXCollections.observableArrayList();
        nameColumn.setCellValueFactory(new PropertyValueFactory<Card, String>("Name"));
        identityCardColumn.setCellValueFactory(new PropertyValueFactory<Card, String>("IdentityCard"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Card, String>("Date"));
        moneyColumn.setCellValueFactory(new PropertyValueFactory<Card, Double>("Money"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Card, String>("Type"));
        feeColumn.setCellValueFactory(new PropertyValueFactory<Card, Double>("Fee"));
        String sql = "select * from Card";
        try{
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()){
                if (rs.getString(1).equals("VIPCard")) {
                    VIPCard vipCard = new VIPCard();
                    vipCard.setType(rs.getString(1));
                    vipCard.setName(rs.getString(2));
                    vipCard.setIdentityCard(rs.getString(3));
                    vipCard.setDate(rs.getString(4));
                    vipCard.setMoney(Double.parseDouble(rs.getString(5)));
                    vipCard.setFee(Double.parseDouble(rs.getString(6)));
                    cardList.add(vipCard);
                }
                if (rs.getString(1).equals("BasicCard")) {
                    BasicCard basicCard = new BasicCard();
                    basicCard.setType(rs.getString(1));
                    basicCard.setName(rs.getString(2));
                    basicCard.setIdentityCard(rs.getString(3));
                    basicCard.setDate(rs.getString(4));
                    basicCard.setMoney(Double.parseDouble(rs.getString(5)));
                    basicCard.setFee(Double.parseDouble(rs.getString(6)));
                    cardList.add(basicCard);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        table1.setItems(cardList);
        comboBox.setItems(list);
    }

    //Combobox
    @FXML
    public ComboBox<String> comboBox;
    @FXML
    public Label label;
    ObservableList<String> list = FXCollections.observableArrayList("BasicCard", "VIPCard");
    public void comboboxChange(ActionEvent e) {
        label.setText(comboBox.getValue());
    }

    //Check lỗi
    public void check1(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Khách hàng không hợp lệ !!");
        alert.show();
    }
    public void check2(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Cập nhật không thành công !!");
        alert.show();
    }
    public void check3(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("ERROR !!");
        alert.show();
    }

    //Xử lí Date
    StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

    //*** CHỨC NĂNG XỬ LÍ THÔNG TIN ***//

    //Thêm thẻ//
    public void Add(ActionEvent event) {
        Date.setConverter(converter);
        if (comboBox.getValue() == null || Name.getText().equals("") || IdentityCard.getText().equals("") || Date.getValue() == null || Money.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("VUI LÒNG KIỂM TRA LẠI THÔNG TIN !!");
            alert.show();
        } else {
            if (comboBox.getValue() == "VIPCard" && Date.getValue() != null) {
                VIPCard vipCard = new VIPCard();
                vipCard.setType("VIPCard");
                vipCard.setName(Name.getText());
                vipCard.setIdentityCard(IdentityCard.getText());
                vipCard.setDate(String.format(Date.getValue().toString()));
                vipCard.setMoney(Double.parseDouble(Money.getText()));
                vipCard.setFee(vipCard.FEE(Double.parseDouble(Money.getText())));
                if (Double.parseDouble(Money.getText()) < 0) {
                    check3();
                } else {
                    cardList.add(vipCard);
                    for (int i = 1; i < cardList.size(); i++) {
                        if (vipCard.getType().equals(cardList.get(i - 1).getType()) && vipCard.getIdentityCard().equals(cardList.get(i - 1).getIdentityCard())) {
                            check1();
                            cardList.remove(vipCard);
                        }else {
                            //thêm vào sql
                            String type = vipCard.getType();
                            String name = vipCard.getName();
                            String identityCard = vipCard.getIdentityCard();
                            String date = vipCard.getDate();
                            double money = vipCard.getMoney();
                            double fee = vipCard.getFee();
                            String sql  = "INSERT INTO Card (type, name, [CMND/CCCD], date, money, fee) VALUES (N'" + type + "', N'" + name + "', N'" + identityCard + "', '"
                                        + date + "', " + money + ", "+ fee +" )";
                            databaseConnection.excute(sql);
                            break;
                        }
                    }
                }

            }
            if (comboBox.getValue() == "BasicCard" && Date.getValue() != null) {
                BasicCard basicCard = new BasicCard();
                basicCard.setType("BasicCard");
                basicCard.setName(Name.getText());
                basicCard.setIdentityCard(IdentityCard.getText());
                basicCard.setDate(String.format(Date.getValue().toString()));
                basicCard.setMoney(Double.parseDouble(Money.getText()));
                basicCard.setFee(basicCard.FEE(Double.parseDouble(Money.getText())));
                if (Double.parseDouble(Money.getText()) < 0) {
                    check3();
                } else {
                    cardList.add(basicCard);
                    for (int i = 1; i < cardList.size(); i++) {
                        if (basicCard.getType().equals(cardList.get(i - 1).getType()) && basicCard.getIdentityCard().equals(cardList.get(i - 1).getIdentityCard())) {
                            check1();
                            cardList.remove(basicCard);
                        }else {
                            //thêm vào sql
                            String type = basicCard.getType();
                            String name = basicCard.getName();
                            String identityCard = basicCard.getIdentityCard();
                            String date = basicCard.getDate();
                            double money = basicCard.getMoney();
                            double fee = basicCard.getFee();
                            String sql  = "INSERT INTO Card (type, name, [CMND/CCCD], date, money, fee) VALUES (N'" + type + "', N'" + name + "', N'" + identityCard + "', '"
                                    + date + "', " + money + ", "+ fee + " )";
                            databaseConnection.excute(sql);
                            break;
                        }
                    }
                }
            }
        }
    }

    //Xóa//
    public void delete(ActionEvent event) {
        Card select = table1.getSelectionModel().getSelectedItem();
        String sType = select.getType();
        String sId = select.getIdentityCard();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa thẻ");
        alert.setContentText("Bạn có muốn xóa thẻ này không? ");
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == yesButton) {
                cardList.remove(select);
                String sql = "delete from Card where type = N'" + sType + "' and [CMND/CCCD] = N'" + sId + "'";
                databaseConnection.excute(sql);
            } else if (type == noButton) {
            }
        });
    }
    public void Show (MouseEvent e){
        Card select = table1.getSelectionModel().getSelectedItem();
        comboBox.setValue(select.getType());
        Name.setText(select.getName());
        IdentityCard.setText(select.getIdentityCard());
        Date.setValue(LocalDate.parse(select.getDate()));
        Money.setText(String.valueOf(select.getMoney()));
    }

    //Sửa//
    public void Update(ActionEvent event) {
        Card select1 = table1.getSelectionModel().getSelectedItem();
        String sType = select1.getType();
        String sName = select1.getName();
        String sIdentityCard = select1.getIdentityCard();
        String sDate = select1.getDate();
        double sMoney = select1.getMoney();
        double sfee1 = select1.getMoney() * (1.0 / 100) + 20000;
        double sfee2 = select1.getMoney() * (0.5 / 100);
        int pos = cardList.indexOf(select1);
        if (comboBox.getValue() == null && Name.getText().equals("") || IdentityCard.getText().equals("") || Date.getValue() == null || Money.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("VUI LÒNG KIỂM TRA LẠI THÔNG TIN !!");
            alert.show();
        } else {
            if (Double.parseDouble(Money.getText()) < 0) {
                check3();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cập nhật thông tin");
                alert.setContentText("Bạn có muốn cập nhật thông tin không ? ");
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);
                alert.showAndWait().ifPresent(type -> {
                    if (type == yesButton) {
                        if (comboBox.getValue().equals("VIPCard") && Date.getValue() != null) {
                            VIPCard vipCard = new VIPCard();
                            vipCard.setType(comboBox.getValue());
                            vipCard.setName(Name.getText());
                            vipCard.setIdentityCard(IdentityCard.getText());
                            vipCard.setDate(String.format(Date.getValue().toString()));
                            vipCard.setMoney(Double.parseDouble(Money.getText()));
                            vipCard.setFee(vipCard.FEE(vipCard.getMoney()));
                            cardList.set(pos, vipCard);
                            String name = vipCard.getName();
                            String identityCard = vipCard.getIdentityCard();
                            String date = vipCard.getDate();
                            double money = vipCard.getMoney();
                            double fee = vipCard.getFee();
                            String sql1 = "update Card set type = N'VIPCard' , name = N'"+name+"' , [CMND/CCCD] = N'"+identityCard+"', date = '"+date+"', money = " +money+ ", fee = "+fee;
                            String sql2 = "where type like N'"+sType+"' and name like N'"+sName+"' and [CMND/CCCD] like N'"+sIdentityCard+"'and date like '"+sDate+"'and money = " +sMoney+ "and fee = "+sfee1;
                            String sql = sql1 + sql2;
                            if (pos == 0) {
                                for (int i = 1; i < cardList.size(); i++) {
                                    if (vipCard.getType().equals(cardList.get(i).getType()) && vipCard.getIdentityCard().equals(cardList.get(i).getIdentityCard())) {
                                        check2();
                                        cardList.set(pos, select1);
                                        break;
                                    }else {
                                        databaseConnection.excute(sql);
                                        break;
                                    }
                                }
                            }
                            if (pos > 0 && pos < cardList.size()) {
                                for (int i = 0; i < pos - 1; i++) {
                                    if (vipCard.getType().equals(cardList.get(i).getType()) && vipCard.getIdentityCard().equals(cardList.get(i).getIdentityCard())) {
                                        check2();
                                        cardList.set(pos, select1);
                                        break;
                                    }else {
                                        databaseConnection.excute(sql);
                                        break;
                                    }
                                }
                                for (int i = pos + 1; i < cardList.size(); i++) {
                                    if (vipCard.getType().equals(cardList.get(i).getType()) && vipCard.getIdentityCard().equals(cardList.get(i).getIdentityCard())) {
                                        check2();
                                        cardList.set(pos, select1);
                                        break;
                                    }else {
                                        databaseConnection.excute(sql);
                                        break;
                                    }
                                }
                            }
                            if (pos == cardList.size() - 1) {
                                for (int i = pos - 1; i >= 0; i--) {
                                    if (vipCard.getType().equals(cardList.get(i).getType()) && vipCard.getIdentityCard().equals(cardList.get(i).getIdentityCard())) {
                                        check2();
                                        cardList.set(pos, select1);
                                        break;
                                    }else {
                                        databaseConnection.excute(sql);
                                        break;
                                    }
                                }
                            }
                        }

                        if (comboBox.getValue().equals("BasicCard") && Date.getValue() != null) {
                            BasicCard basicCard = new BasicCard();
                            basicCard.setType(comboBox.getValue());
                            basicCard.setName(Name.getText());
                            basicCard.setIdentityCard(IdentityCard.getText());
                            basicCard.setDate(String.format(Date.getValue().toString()));
                            basicCard.setMoney(Double.parseDouble(Money.getText()));
                            basicCard.setFee(basicCard.FEE(basicCard.getMoney()));
                            cardList.set(pos, basicCard);
                            String name = basicCard.getName();
                            String identityCard = basicCard.getIdentityCard();
                            String date = basicCard.getDate();
                            double money = basicCard.getMoney();
                            double fee = basicCard.getFee();
                            String sql1 = "update Card set type = N'BasicCard' , name = N'"+name+"' , [CMND/CCCD] = N'"+identityCard+"', date = '"+date+"', money = " +money+ ", fee = "+fee;
                            String sql2 = "where type like N'"+sType+"' and name like N'"+sName+"'and [CMND/CCCD] like N'"+sIdentityCard+"'and date like '"+sDate+"'and money = " +sMoney+ "and fee = "+sfee2;
                            String sql = sql1 + sql2;
                            if (pos == 0) {
                                for (int i = 1; i < cardList.size(); i++) {
                                    if (basicCard.getType().equals(cardList.get(i).getType()) && basicCard.getIdentityCard().equals(cardList.get(i).getIdentityCard())) {
                                        check2();
                                        cardList.set(pos, select1);
                                        break;
                                    }else {
                                        databaseConnection.excute(sql);
                                        break;
                                    }
                                }
                            }
                            if (pos > 0 && pos < cardList.size()) {
                                for (int i = 0; i < pos - 1; i++) {
                                    if (basicCard.getType().equals(cardList.get(i).getType()) && basicCard.getIdentityCard().equals(cardList.get(i).getIdentityCard())) {
                                        check2();
                                        cardList.set(pos, select1);
                                        break;
                                    }else {
                                        databaseConnection.excute(sql);
                                        break;
                                    }
                                }
                                for (int i = pos + 1; i < cardList.size(); i++) {
                                    if (basicCard.getType().equals(cardList.get(i).getType()) && basicCard.getIdentityCard().equals(cardList.get(i).getIdentityCard())) {
                                        check2();
                                        cardList.set(pos, select1);
                                        break;
                                    }
                                }
                            }
                            if (pos == cardList.size() - 1) {
                                for (int i = pos - 1; i >= 0; i--) {
                                    if (basicCard.getType().equals(cardList.get(i).getType()) && basicCard.getIdentityCard().equals(cardList.get(i).getIdentityCard())) {
                                        check2();
                                        cardList.set(pos, select1);
                                        break;
                                    }else {
                                        databaseConnection.excute(sql);
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (type == noButton) {
                    }
                });
            }
        }
    }

    //Làm mới//
    public void reset(ActionEvent e) {
        comboBox.setValue("Loại thẻ");
        Name.setText("");
        IdentityCard.setText("");
        Date.setValue(null);
        Money.setText("");
        searchName.setText("");
        searchDate.setValue(null);
        Number.setText("");
        Month.setText("");
    }

    //*** CHỨC NĂNG XỬ LÍ YÊU CẦU ***//

    //Tìm kiếm theo tên hoặc theo ngày mở thẻ//
    @FXML
    private TextField searchName;
    @FXML
    private DatePicker searchDate;
    private ObservableList<Card> cardSearchList;
    public void SEARCH(ActionEvent e) {
        cardSearchList = FXCollections.observableArrayList(); // Tạo 1 list mới để thêm những khách hàng muốn tìm kiếm vào bảng
        searchDate.setConverter(converter);
        if (searchName.getText().isBlank() == true && searchDate.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Vui lòng nhập thông tin để tìm kiếm !!");
            alert.show();
            table1.setItems(cardList);
        }

        if (searchName.getText().isBlank() == false && searchDate.getValue() == null) {
            for (int i = 0; i < cardList.size(); i++) {
                if (searchName.getText().equals(cardList.get(i).getName())) {
                    cardSearchList.add(cardList.get(i));
                }
            }
            table1.setItems(cardSearchList);
        }

        if (searchName.getText().isBlank() == true && searchDate.getValue() != null) {
            for (int i = 0; i < cardList.size(); i++) {
                if (String.format(searchDate.getValue().toString()).equals(cardList.get(i).getDate())) {
                    cardSearchList.add(cardList.get(i));
                }
            }
            table1.setItems(cardSearchList);
        }

        if (searchName.getText().isBlank() == false && searchDate.getValue() != null) {
            for (int i = 0; i < cardList.size(); i++) {
                if (searchName.getText().equals(cardList.get(i).getName()) && String.format(searchDate.getValue().toString()).equals(cardList.get(i).getDate())) {
                    cardSearchList.add(cardList.get(i));
                }
            }
            table1.setItems(cardSearchList);
        }

    }

    //Thống kê danh sách các thẻ có phí duy trì 1 tháng lớn hơn 1 số nhập vào//
    @FXML
    private TextField Number;
    private ObservableList<Card> cardPrintCard;
    public void Print(ActionEvent event) {
        double number = Double.parseDouble(Number.getText());
        cardPrintCard = FXCollections.observableArrayList(); // Tạo 1 list mới để in ra danh sách các thẻ có phí duy trì 1 tháng lớn hơn 1 số nhập vào vào bảng
        if (Double.parseDouble(Number.getText()) < 0) {
                check3();
            } else {
                for (int i = 0; i < cardList.size(); i++) {
                    if (cardList.get(i).getType().equals("VIPCard") && number < ((cardList.get(i).getMoney()) * (1.0 / 100) + 20000)) {
                        cardPrintCard.add(cardList.get(i));
                    }
                }

                for (int i = 0; i < cardList.size(); i++) {
                    if (cardList.get(i).getType().equals("BasicCard") && number < ((cardList.get(i).getMoney()) * (0.5 / 100))) {
                        cardPrintCard.add(cardList.get(i));
                    }
                }
                table1.setItems(cardPrintCard);
            }
        }


    //Tính doanh thu từ phí duy trì thẻ của ngân hàng trong một khoảng thời gian nhập vào
    @FXML
    private TextField Month;
    public void Caculate(ActionEvent event) {
        int month = Integer.parseInt(Month.getText());
        double sum1 = 0, sum2 = 0;
        if (month <= 0.0) {
            check3();
        } else {
            for (int i = 0; i < cardList.size(); i++) {
                if (cardList.get(i).getType().equals("VIPCard")) {
                    VIPCard vipCard = new VIPCard();
                    vipCard.setFee(vipCard.Doanhthu(cardList.get(i).getMoney(), month));
                    sum1 += vipCard.getFee();
                }
                if (cardList.get(i).getType().equals("BasicCard")) {
                    BasicCard basicCard = new BasicCard();
                    basicCard.setFee(basicCard.Doanhthu(cardList.get(i).getMoney(), month));
                    sum2 += basicCard.getFee();
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Doanh thu từ phí duy trì thẻ của ngân hàng " + "\n" + "trong " + month + " tháng là: " + (sum1 + sum2) + "đ");
            alert.show();
        }
    }

    //Quay lại list khách hàng ban đầu
    public void Back(ActionEvent e) {
        table1.setItems(cardList);
    }

    //Đóng cửa sổ//
    @FXML
    private Button close1;
    public void Close1(ActionEvent event) {
        Stage stage = (Stage) close1.getScene().getWindow();
        stage.close();
    }
}

