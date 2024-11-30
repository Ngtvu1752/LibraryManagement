package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.sql.*;

public class ManageStudent {

    @FXML
    private TextField isbnField, titleField, authorField, languageField, searchField;

    @FXML
    private Label isbnLabel, titleLabel, authorLabel, languageLabel;

    @FXML
    private Button updateButton, deleteButton, searchButton, backButton;

    @FXML
    private TableView<IssueBookDBHistory> tableBook;

    @FXML
    private TableColumn<IssueBookDBHistory, String> idColumn, nameColumn, classColumn, schoolColumn, isbnColumn,
            returnColumn, feeColumn, dueColumn;


    private ObservableList<IssueBookDBHistory> informations;
    private DatabaseHelper dbHelper = DatabaseHelper.getInstance();
    @FXML
    private void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        schoolColumn.setCellValueFactory(new PropertyValueFactory<>("schoolName"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        returnColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        feeColumn.setCellValueFactory(new PropertyValueFactory<>("lateFee"));
        dueColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        informations = getTableBook();
        tableBook.setItems(informations);

        // Thêm sự kiện cho các nút
        updateButton.setOnAction(event -> updateStudent());
        deleteButton.setOnAction(event -> deleteStudent());
        searchButton.setOnAction(event -> searchStudent());
        backButton.setOnAction(event -> handleBackButton());
    }

    public ObservableList<IssueBookDBHistory> getTableBook() {
        String INFOR = "SELECT users.id, users.name, users.class, users.school, issuebook.ISBN, issuebook.due_date, issuebook.is_returned, issuebook.late_fee\n" +
        "FROM issuebook\n" +
        "JOIN users ON users.id = issuebook.student_id";
        ObservableList<IssueBookDBHistory> infomations = FXCollections.observableArrayList();
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(INFOR)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String className = rs.getString("class");
                String school = rs.getString("school");
                String isbn = rs.getString("isbn");
                Date dueDate = rs.getDate("due_date");
                String status = rs.getString("is_returned");
                int lateFee = rs.getInt("late_fee");
                infomations.add(new IssueBookDBHistory(id, name, className, school, isbn, dueDate, status, lateFee));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return infomations;
    }

    private void findStudent() {
        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            tableBook.setItems(informations);
            return;
        }

        ObservableList<IssueBookDBHistory> filteredStudents = FXCollections.observableArrayList();

        try {
            int studentIdKeyword = Integer.parseInt(keyword);

            for (IssueBookDBHistory student : informations) {
                if (student.getStudentId() == studentIdKeyword ||
                        student.getStudentName().toLowerCase().contains(keyword)) {
                    filteredStudents.add(student);
                }
            }
        } catch (NumberFormatException e) {
            for (IssueBookDBHistory student : informations) {
                if (student.getStudentName().toLowerCase().contains(keyword)) {
                    filteredStudents.add(student);
                }
            }
        }
        tableBook.setItems(filteredStudents);
    }


    private void updateStudent() {
    }

    private void deleteStudent() {
    }

    private void searchStudent() {
        findStudent();
    }

    private void handleBackButton() {
        SceneController.getInstance().switchScene("HomePage");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
