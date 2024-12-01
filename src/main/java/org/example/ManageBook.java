package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ManageBook {

    public Label isbnLabel;
    public Label titleLabel;
    public Label authorLabel;
    public Label languageLabel;
    public Label quantityLabel;

    @FXML
    private TextField searchField;

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
    private Button searchButton;

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
    private TableColumn<Book, String> deleteColumn;

    @FXML
    private ScrollPane suggestionScrollPane;

    @FXML
    private VBox suggestionBox;

    @FXML
    private Button checkButton;

    DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    private final GoogleBooksService googleBooksService = new GoogleBooksService();
    private final BookDAO bookDAO = BookDAO.getInstance();
    private String imageUrl;
    private ObservableList<Book> books;

    public void initialize() {

        labelClicked(isbnLabel, isbnField);
        labelClicked(titleLabel, titleField);
        labelClicked(authorLabel, authorField);
        labelClicked(languageLabel, languageField);
        labelClicked(quantityLabel, quantityField);
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
        });
        //deleteButton.setOnAction(event -> {
       // });
        checkButton.setOnAction(event -> {
            String isbn = isbnField.getText().trim();
            if (isbn.isEmpty()) {
                showAlert("Error", "Hãy nhập ISBN.");
            } else {
                // Gọi hàm kiểm tra sách qua API
                fetchBookDetails(isbn);
            }
        });
        backButton.setOnAction(event -> handleBackButton());
        searchButton.setOnAction(event -> {findBook(); System.out.println(searchField.getText());});
//        searchField.addEventFilter();
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        fetchBookInBackground();
        addDeleteButtonToTable();
    }

    private void fetchBookInBackground() {
        Task<ObservableList<Book>> task = new Task<ObservableList<Book>>() {
            protected ObservableList<Book> call() throws Exception {
                return bookDAO.getObservableList();
            }
        };

        task.setOnSucceeded(event -> {
            books = task.getValue();
            tableBook.setItems(books);
            System.out.println("Book loads successfully");
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            showAlert("Error", "failed to load books.");
            exception.printStackTrace();
        });
        new Thread(task).start();
    }

    private void fetchBookDetails(String isbn) {
        Task<Book> fetchBookTask = new Task<Book>() {
            @Override
            protected Book call() throws Exception {
                return googleBooksService.fetchBookDetails(isbn);
            }
        };

        fetchBookTask.setOnSucceeded(event -> {
            Book book = fetchBookTask.getValue();
            if (book != null) {
                setImageUrl(book.getImageUrl());
                titleField.setText(book.getTitle());
                authorField.setText(book.getAuthor());
                languageField.setText(book.getLanguage());
                if (titleField.getText() != null && !titleField.getText().isEmpty()) {
                    titleLabel.setVisible(false);  // Ẩn label khi có thông tin
                }
                if (authorField.getText() != null && !authorField.getText().isEmpty()) {
                    authorLabel.setVisible(false);  // Ẩn label khi có thông tin
                }
                if (languageField.getText() != null && !languageField.getText().isEmpty()) {
                    languageLabel.setVisible(false);  // Ẩn label khi có thông tin
                }
            } else {
                showAlert("No Data", "No book found with the provided ISBN.");
            }
        });

        fetchBookTask.setOnFailed(event -> {
            Throwable exception = fetchBookTask.getException();
            exception.printStackTrace();
            showAlert("Error", "failed to load book details.");
        });
        new Thread(fetchBookTask).start();
    }

    private void saveBookToDatabase() {
        try {
            String isbn = isbnField.getText().trim();
            if (!isbn.isEmpty()) {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String language = languageField.getText().trim();
                String quantityS = quantityField.getText().trim();
                if (!quantityS.isEmpty()) {
                    int quantity = Integer.parseInt(quantityS);

                    Book book = new Book(isbn, title, author, language, getImageUrl(), quantity);
                    boolean addBook = bookDAO.save(book);

                    if (addBook) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Book saved successfully!");
                        alert.showAndWait();
                        books.add(book);
                    }
                } else {
                    showAlert("Error", "Please enter the quantity.");
                }
            } else {
                showAlert("Error", "Please fill in the ISBN.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid quantity. Please enter a valid number.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred.");
        }
    }


    private void addDeleteButtonToTable() {
        Callback<TableColumn<Book, String>, TableCell<Book, String>> cellFactory = new Callback<TableColumn<Book, String>, TableCell<Book, String>>() {
            public TableCell<Book, String> call(TableColumn<Book, String> param) {
                return new TableCell<Book, String>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Book bookToDelete = getTableView().getItems().get(getIndex());
                            boolean isDeleted = bookDAO.delete(bookToDelete);
                            if (isDeleted) {
                                getTableView().getItems().remove(bookToDelete);  // Remove from table
                                showAlert("Success", "Book deleted successfully.");
                            } else {
                                showAlert("Error", "Failed to delete the book.");
                            }
                        });
                    }

                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        };
        deleteColumn.setCellFactory(cellFactory);
    }

    private void findBook() {
        String keyword = searchField.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            tableBook.setItems(books);
            return;
        }

        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword) || book.getAuthor().toLowerCase().contains(keyword)) {
                filteredBooks.add(book);
            }
        }
        tableBook.setItems(filteredBooks);
    }

    private void handleBackButton() {
        SceneController.getInstance().switchScene("HomePage");
        isbnField.setText("");
        titleField.setText("");
        authorField.setText("");
        languageField.setText("");
        quantityField.setText("");
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}