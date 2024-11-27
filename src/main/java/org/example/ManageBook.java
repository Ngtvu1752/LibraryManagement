package org.example;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ManageBook {

    public Label isbnLabel;
    public Label titleLabel;
    public Label authorLabel;
    public Label languageLabel;
    public Label quantityLabel;
    @FXML
    private TextField isbnField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField languageField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

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

    DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    private final GoogleBooksService googleBooksService = new GoogleBooksService();
    private final BookDAO bookDAO = BookDAO.getInstance();

    public void initialize() {
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

//        // Hiện lại Label nếu TextField rỗng
//        isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue.isEmpty()) {
//                isbnLabel.setVisible(true);  // Hiện Label khi TextField trống
//            }
//        });

        // Hiện lại Label nếu TextField trống khi mất tiêu điểm
        isbnField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && isbnField.getText().isEmpty()) {  // Khi mất tiêu điểm và TextField trống
                isbnLabel.setVisible(true);  // Hiện Label khi TextField trống và mất tiêu điểm
            }
        });

        titleField.setOnMouseClicked(event -> {
            if (titleField.getText().isEmpty()) {
                titleLabel.setVisible(false);  // Ẩn Label khi người dùng click vào TextField
            }
        });

        titleField.setOnKeyPressed(event -> {
            if (!titleField.getText().isEmpty()) {
                titleLabel.setVisible(false);  // Ẩn Label khi có văn bản trong TextField
            }
        });

        if (titleField.getText() != null && !titleField.getText().isEmpty()) {
            titleLabel.setVisible(false);  // Ẩn label khi có thông tin
        }

        // Hiện lại Label nếu TextField trống khi mất tiêu điểm
        titleField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && titleField.getText().isEmpty()) {  // Khi mất tiêu điểm và TextField trống
                titleLabel.setVisible(true);  // Hiện Label khi TextField trống và mất tiêu điểm
            }
        });


        authorField.setOnMouseClicked(event -> {
            if (authorField.getText().isEmpty()) {
                authorLabel.setVisible(false);  // Ẩn Label khi người dùng click vào TextField
            }
        });

        authorField.setOnKeyPressed(event -> {
            if (!authorField.getText().isEmpty()) {
                authorLabel.setVisible(false);  // Ẩn Label khi có văn bản trong TextField
            }
        });


        // Hiện lại Label nếu TextField trống khi mất tiêu điểm
        authorField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && authorField.getText().isEmpty()) {  // Khi mất tiêu điểm và TextField trống
                authorLabel.setVisible(true);  // Hiện Label khi TextField trống và mất tiêu điểm
            }
        });

        languageField.setOnMouseClicked(event -> {
            if (languageField.getText().isEmpty()) {
                languageLabel.setVisible(false);  // Ẩn Label khi người dùng click vào TextField
            }
        });

        languageField.setOnKeyPressed(event -> {
            if (!languageField.getText().isEmpty()) {
                languageLabel.setVisible(false);  // Ẩn Label khi có văn bản trong TextField
            }
        });


        // Hiện lại Label nếu TextField trống khi mất tiêu điểm
        languageField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && languageField.getText().isEmpty()) {  // Khi mất tiêu điểm và TextField trống
                languageLabel.setVisible(true);  // Hiện Label khi TextField trống và mất tiêu điểm
            }
        });

        quantityField.setOnMouseClicked(event -> {
            if (quantityField.getText().isEmpty()) {
                quantityLabel.setVisible(false);  // Ẩn Label khi người dùng click vào TextField
            }
        });

        quantityField.setOnKeyPressed(event -> {
            if (!quantityField.getText().isEmpty()) {
                quantityLabel.setVisible(false);  // Ẩn Label khi có văn bản trong TextField
            }
        });


        // Hiện lại Label nếu TextField trống khi mất tiêu điểm
        quantityField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && quantityField.getText().isEmpty()) {  // Khi mất tiêu điểm và TextField trống
                quantityLabel.setVisible(true);  // Hiện Label khi TextField trống và mất tiêu điểm
            }
        });

        // Nhập vào ISBN để ktra xem có sách trên API không
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                isbnField.setOnAction(event -> fetchBookDetails(newValue.trim()));
            }
        });
        addButton.setOnAction(event -> {
            saveBookToDatabase();
            ObservableList<Book> books = bookDAO.getObservableList();
            tableBook.setItems(books);
        });
        deleteButton.setOnAction(event -> {
        });
        backButton.setOnAction(event -> handleBackButton());

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        ObservableList<Book> books = bookDAO.getObservableList();
        tableBook.setItems(books);
    }

    private void fetchBookDetails(String isbn) {
        try {
            Book book = googleBooksService.fetchBookDetails(isbn);
            if (book != null) {
                titleField.setText(book.getTitle());
                if (titleField.getText() != null && !titleField.getText().isEmpty()) {
                    titleLabel.setVisible(false);  // Ẩn label khi có thông tin
                }
                authorField.setText(book.getAuthor());
                if (authorField.getText() != null && !authorField.getText().isEmpty()) {
                    authorLabel.setVisible(false);  // Ẩn label khi có thông tin
                }
                languageField.setText(book.getLanguage());
                if (languageField.getText() != null && !languageField.getText().isEmpty()) {
                    languageLabel.setVisible(false);  // Ẩn label khi có thông tin
                }
            } else {
                showAlert("No Data", "No book found with the provided ISBN.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch book details. Check your internet connection or API key.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    private void saveBookToDatabase() {
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String language = languageField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        Book book = new Book(isbn, title, author, language, quantity);
        boolean addBook = bookDAO.save(book);
        if (addBook) {
            // Hiển thị hộp thoại thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Book saved successfully!");
            alert.showAndWait();
        }
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