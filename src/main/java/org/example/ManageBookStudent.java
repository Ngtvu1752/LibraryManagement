package org.example;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.IOException;

public class ManageBookStudent {

    public Label isbnLabel;

    @FXML
    private TextField isbnField;

    @FXML
    private ImageView imageView;

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
    private TableColumn<Book, Void> optionColumn;

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


    private final BookDAO bookDAO = BookDAO.getInstance();
    private final IssueBookDBHistoryDAO issueBookDBHistoryDAO = IssueBookDBHistoryDAO.getInstance();

    public void initialize() {
//        User currentUser = SessionManager.getCurrentUser();
//        if (currentUser == null) {
//            System.err.println("No user is currently logged in.");
//            return;
//        }
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
        issueBookButton.setOnAction(event -> {
            handleIssueBook();
            ObservableList<IssueBookDBHistory> issueBookDBHistories = issueBookDBHistoryDAO.getObservableList();
            IssueTableStudent.setItems(issueBookDBHistories);
        });
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
        addViewAndRateButtonToTable();
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

    private void handleReturnBook(String isbn) {
        User studentId = SessionManager.getCurrentUser();
        int userId = studentId.getId();
        IssueBook issueBook = new IssueBook();
        long success = issueBook.returnBook(userId, isbn);
        if (success > 0) {
            showAlert("Returned book successfully", "Bạn đã trả sách muộn: " + (success / 5000) + "ngày\n"
                    + "Số tiền cần nộp phạt: " + success + "VNĐ");
        } else if (success == 0) {
            showAlert("Returned book successfully", "Cảm ơn vì đã mượn sách");
        } else {
            showAlert("Error", "Sách đã được trả");
        }

    }

    private void addReturnButtonToTable() {
        Callback<TableColumn<IssueBookDBHistory, Void>, TableCell<IssueBookDBHistory, Void>> cellFactory = new Callback<TableColumn<IssueBookDBHistory, Void>, TableCell<IssueBookDBHistory, Void>>() {
            public TableCell<IssueBookDBHistory, Void> call(TableColumn<IssueBookDBHistory, Void> param) {
                return new TableCell<IssueBookDBHistory, Void>() {
                    private final Button returnButton = new Button("Return");

                    {
                        returnButton.setOnAction(event -> {
                            IssueBookDBHistory book = IssueTableStudent.getItems().get(getIndex());
                            handleReturnBook(book.getIsbn());
                            ObservableList<IssueBookDBHistory> issueBookDBHistories = issueBookDBHistoryDAO.getObservableList();
                            IssueTableStudent.setItems(issueBookDBHistories);
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

    private void addViewAndRateButtonToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<TableColumn<Book, Void>, TableCell<Book, Void>>() {
            public TableCell<Book, Void> call(TableColumn<Book, Void> param) {
                return new TableCell<Book, Void>() {
                    Image starImage = new Image(getClass().getResource("/star.png").toExternalForm());
                    private final ImageView rateImageView = new ImageView(starImage);

                    private final Button viewButton = new Button("View");

                    private final HBox buttonBox = new HBox(5);

                    {
                        rateImageView.setFitHeight(20);
                        rateImageView.setFitWidth(20);
                        rateImageView.setPreserveRatio(true);

                        rateImageView.setOnMouseClicked(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            System.out.println("Rating book: " + book.getTitle());
                        });

                        viewButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            String imageUrl = bookDAO.getImageUrl(book.getIsbn());
                            displayBookImage(imageUrl);
                            System.out.println("Returning book: " + book.getTitle());
                        });

                        buttonBox.getChildren().addAll(rateImageView, viewButton);
                    }

                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        };
        optionColumn.setCellFactory(cellFactory);
    }

    public void displayBookImage(String imageUrl) {
        try {
            if (imageUrl != null) {
                Image image = new Image(imageUrl, true);

                imageView.setImage(image);
                imageView.setFitHeight(177);
                imageView.setFitWidth(140);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                image.errorProperty().addListener((obs, oldError, newError) -> {
                    if (newError != null) {
                        System.out.println("Failed to load img: " + imageUrl);
                        imageView.setImage(new Image("/placeholder.jpg"));
                    }
                });
            } else {
                System.out.println("Image is null!");
                imageView.setImage(new Image("/placeholder.jpg"));
                imageView.setFitHeight(177);
                imageView.setFitWidth(140);
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImage(new Image("/placeholder.jpg"));
            imageView.setFitHeight(177);
            imageView.setFitWidth(140);
        }
    }

    private void handleBackButton() {
        SceneController.getInstance().switchScene("StudentHomePage");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
