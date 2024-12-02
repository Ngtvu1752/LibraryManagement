package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class ManageStudent {

    @FXML
    private TextField studentIDField, studentNameFeild, classFeild, schoolField, searchField;

    @FXML
    private Label idLabel, nameLabel, classLabel, schoolLabel;

    @FXML
    private Button updateButton, deleteButton, searchButton, backButton;

    @FXML
    private TableView<IssueBookDBHistory> tableBook;

    @FXML
    private TableColumn<IssueBookDBHistory, String> idColumn, nameColumn, classColumn, schoolColumn, isbnColumn,
            returnColumn, feeColumn, dueColumn;

    @FXML
    private TableColumn<IssueBookDBHistory, Void> optionColumn;

    private ObservableList<IssueBookDBHistory> informations;
    private DatabaseHelper dbHelper = DatabaseHelper.getInstance();
    private StudentDAO studentDAO = new StudentDAO();
    private final NotificationDAO notificationDAO = NotificationDAO.getInstance();
    private final BookDAO bookDAO = BookDAO.getInstance();

    @FXML
    private void initialize() {

        labelClicked(idLabel, studentIDField);
        labelClicked(nameLabel, studentNameFeild);
        labelClicked(classLabel, classFeild);
        labelClicked(schoolLabel, schoolField);


        handleFieldVisibility(studentIDField, idLabel);
        handleFieldVisibility(studentNameFeild, nameLabel);
        handleFieldVisibility(classFeild, classLabel);
        handleFieldVisibility(schoolField, schoolLabel);

        // Thiết lập các cột cho TableView
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


        tableBook.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                studentIDField.setText(String.valueOf(newValue.getStudentId()));
                studentNameFeild.setText(newValue.getStudentName());
                classFeild.setText(newValue.getClassName());
                schoolField.setText(newValue.getSchoolName());

                // Hide labels when fields are populated
                if (!isFieldEmpty(studentIDField)) {
                    idLabel.setVisible(false);
                }
                if (!isFieldEmpty(studentNameFeild)) {
                    nameLabel.setVisible(false);
                }
                if (!isFieldEmpty(classFeild)) {
                    classLabel.setVisible(false);
                }
                if (!isFieldEmpty(schoolField)) {
                    schoolLabel.setVisible(false);
                }
            }
        });


        updateButton.setOnAction(event -> updateStudent());
        deleteButton.setOnAction(event -> deleteStudent());
        searchButton.setOnAction(event -> searchStudent());
        backButton.setOnAction(event -> handleBackButton());
        addOptionButtonToTable();
    }

    // kiểm tra xem feild có rỗng hoặc null hay không.
    private boolean isFieldEmpty(TextField textField) {
        return textField.getText() == null || textField.getText().isEmpty();
    }

    //  ẩn label khi các feild đã được điền thông tin.
    private void handleFieldVisibility(TextField textField, Label label) {
        updateLabelVisibility(textField, label);
        textField.setOnMouseClicked(event -> updateLabelVisibility(textField, label));
        textField.setOnKeyPressed(event -> updateLabelVisibility(textField, label));
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && isFieldEmpty(textField)) {
                label.setVisible(true);
            }
        });
    }

    private void updateLabelVisibility(TextField textField, Label label) {
        if (isFieldEmpty(textField)) {
            label.setVisible(true);
        } else {
            label.setVisible(false);
        }
    }

    public void labelClicked(Label myLabel, TextField myTextField) {
        myLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                myLabel.setVisible(false);
                myTextField.requestFocus();
            }
        });
    }

    public ObservableList<IssueBookDBHistory> getTableBook() {
        String INFOR = "SELECT\n" +
                "    users.id,\n" +
                "    users.name,\n" +
                "    users.class,\n" +
                "    users.school,\n" +
                "    issuebook.ISBN,\n" +
                "    issuebook.due_date,\n" +
                "    issuebook.is_returned,\n" +
                "    issuebook.late_fee\n" +
                "FROM\n" +
                "    users\n" +
                "LEFT JOIN\n" +
                "    issuebook ON users.id = issuebook.student_id\n" +
                "where role = 'student';";
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

    private void addOptionButtonToTable() {
        Callback<TableColumn<IssueBookDBHistory, Void>, TableCell<IssueBookDBHistory, Void>> cellFactory = new Callback<TableColumn<IssueBookDBHistory, Void>, TableCell<IssueBookDBHistory, Void>>() {
            public TableCell<IssueBookDBHistory, Void> call(TableColumn<IssueBookDBHistory, Void> param) {
                return new TableCell<IssueBookDBHistory, Void>() {
                    private final Button optionButton = new Button("Send");

                    {
                        optionButton.setOnAction(event -> {
                            IssueBookDBHistory book = tableBook.getItems().get(getIndex());
                            String title = bookDAO.getBookTitleByIsbn(book.getIsbn());
                            String message = "Sách \"" + title + "\" sắp hết hạn. Vui lòng trả đúng hạn.";
                            sendNotification(book.getStudentId(), message);
                        });
                    }

                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(optionButton);
                        }
                    }
                };
            }
        };
        optionColumn.setCellFactory(cellFactory);
    }

    private void sendNotification(int userId, String message) {
        Notification notification = new Notification(userId, message, LocalDateTime.now());
        notificationDAO.save(notification);
        showAlert("Notification", "Thông báo đã được gửi thành công!");
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
        // Lấy thông tin
        String studentIdText = studentIDField.getText().trim();
        String studentName = studentNameFeild.getText().trim();
        String className = classFeild.getText().trim();
        String schoolName = schoolField.getText().trim();

        // Xác thực input
        if (studentIdText.isEmpty() || studentName.isEmpty() || className.isEmpty() || schoolName.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Kiểm tra msv có phải là một số nguyên hợp lệ hay không.
        int studentId;
        try {
            studentId = Integer.parseInt(studentIdText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Student ID must be a number.");
            return;
        }

        // Kiểm tra xem msv có thay đổi hay không và nếu mã mới đã tồn tại
        IssueBookDBHistory selectedStudent = tableBook.getSelectionModel().getSelectedItem();
        if (selectedStudent != null && selectedStudent.getStudentId() != studentId) {
            if (studentDAO.isStudentIdExists(studentId)) {
                showAlert("Error", "The Student ID already exists. Please choose a unique ID.");
                return;
            }
        }

        // giữ nguyên nếu feild trống.
        String updatedName = studentName.isEmpty() ? selectedStudent.getStudentName() : studentName;
        String updatedClass = className.isEmpty() ? selectedStudent.getClassName() : className;
        String updatedSchool = schoolName.isEmpty() ? selectedStudent.getSchoolName() : schoolName;

        // Tạo một đối tượng Sinh viên với thông tin đã cập nhật.
        Student updatedStudent = new Student(studentId, "", updatedName);
        updatedStudent.setClassroom(updatedClass);
        updatedStudent.setSchool(updatedSchool);

        boolean updateSuccess = studentDAO.update(updatedStudent);
        if (updateSuccess) {
            showAlert("Success", "Student information updated successfully.");
            informations = getTableBook();
            tableBook.setItems(informations);
        } else {
            showAlert("Error", "Failed to update student information.");
        }
    }

    private void deleteStudent() {
        // gọi sinh viên được chọn
        IssueBookDBHistory selectedStudent = tableBook.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            int studentId = selectedStudent.getStudentId();
            Student studentToDelete = new Student(studentId, "", "", "", "", "student");
            boolean deleteSuccess = studentDAO.delete(studentToDelete);
            if (deleteSuccess) {
                showAlert("Success", "Student deleted successfully.");
                informations = getTableBook();
                tableBook.setItems(informations);
            } else {
                showAlert("Error", "Failed to delete student.");
            }
        }
    }

    private void searchStudent() {
        findStudent();
    }

    private void handleBackButton() {
        SceneController.getInstance().switchScene("HomePage");
        SceneManage.removeScene("ManageStudent");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
