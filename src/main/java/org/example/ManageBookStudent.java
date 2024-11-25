package org.example;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ManageBookStudent {

    public Label isbnLabel;

    @FXML
    private TextField isbnField;

    @FXML
    private Button issueBookButton;
    @FXML
    private Button backButton;

    @FXML
    private TableView<Book> tableBook;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> quantityColumn;

    @FXML
    private TableColumn<Book, String> languageColumn;

    @FXML
    private TableView<IssueBookDBHistory> IssueTableStudent;

    @FXML
    private TableColumn<IssueBookDBHistory, String> issueIDColumn;

    @FXML
    private TableColumn<IssueBookDBHistory, String> issueIsbnColumn;

    @FXML
    private TableColumn<IssueBookDBHistory, String> issueTitleColumn;

    @FXML
    private TableColumn<IssueBookDBHistory, String> issueDueDateColumn;

    @FXML
    private TableColumn<IssueBookDBHistory, String> issueStatusColumn;

    @FXML
    private TableColumn<IssueBookDBHistory, Void> returnColumn;


    private BookDAO bookDAO = new BookDAO();
    private IssueBookDBHistoryDAO issueBookDBHistoryDAO = new IssueBookDBHistoryDAO();
    public void initialize() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            System.err.println("No user is currently logged in.");
            return;
        }
        isbnField.setOnMouseClicked(event -> {
            if (isbnField.getText().isEmpty()) {
                isbnLabel.setVisible(false);  // Ẩn Label khi người dùng click vào TextField
            }
        });

        isbnField.setOnKeyPressed(event -> {
            if (!isbnField.getText().isEmpty()) {
                isbnLabel.setVisible(false);  // Ẩn Label khi có văn bản trong TextField
            }
        });
        isbnField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && isbnField.getText().isEmpty()) {  // Khi mất tiêu điểm và TextField trống
                isbnLabel.setVisible(true);  // Hiện Label khi TextField trống và mất tiêu điểm
            }
        });
        issueBookButton.setOnAction(event -> handleIssueBook());
        backButton.setOnAction(event -> handleBackButton());

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));


        ObservableList<Book> books = bookDAO.getObservableList();
        tableBook.setItems(books);

        issueIDColumn.setCellValueFactory(new PropertyValueFactory<>("issueBookID"));
        issueIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        issueTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        issueDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        issueStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<IssueBookDBHistory> issueBookDBHistories = issueBookDBHistoryDAO.getObservableList();
        IssueTableStudent.setItems(issueBookDBHistories);
        addReturnButtonToTable();
    }

    private void handleIssueBook() {
        User studentId = SessionManager.getCurrentUser();
        int userId = studentId.getId();
        String isbn = isbnField.getText();
        IssueBook issueBook = new IssueBook();
        boolean success = issueBook.borrowBook(userId, isbn);
        if (success) {
            // thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Book borrowed successfully!");
            alert.showAndWait();
        } else {
            // Sách không thể mượn.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Book is not available for borrowing.");
            alert.showAndWait();
        }
    }

    private void addReturnButtonToTable() {
        Callback<TableColumn<IssueBookDBHistory, Void>, TableCell<IssueBookDBHistory, Void>> cellFactory = new Callback<TableColumn<IssueBookDBHistory, Void>, TableCell<IssueBookDBHistory, Void>>() {
            public TableCell<IssueBookDBHistory, Void> call (TableColumn<IssueBookDBHistory, Void> param) {
                return new TableCell<IssueBookDBHistory, Void>() {
                    private final Button returnButton = new Button("Return");
                    {
                        returnButton.setOnAction(event -> {
                            IssueBookDBHistory book = IssueTableStudent.getItems().get(getIndex());
                            System.out.println("return book" + book.getTitle());
                        });
                    }

                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(returnButton);
                        }
                    }
                };
            }
        };
        returnColumn.setCellFactory(cellFactory);
    }

    private void handleBackButton() {
        try {
            // Tải file FXML của cửa sổ
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/StudentHomePage.fxml"));
            Parent studentHomePageRoot = fxmlLoader.load();

            // Lấy Stage hiện tại (MangeBookStudent)
            Stage currentStage = (Stage) backButton.getScene().getWindow();

            // Tạo Scene mới từ giao diện StudentHomePage
            Scene studentHomePageScene = new Scene(studentHomePageRoot);

            // Hiển thị màn hình Login
            currentStage.setScene(studentHomePageScene);
            currentStage.setTitle("StudentHomePage");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
