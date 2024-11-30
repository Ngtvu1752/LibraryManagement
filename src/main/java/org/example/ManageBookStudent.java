package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.Rating;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ManageBookStudent {

    public Label isbnLabel;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

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

    @FXML
    private ScrollPane suggestionScrollPane;

    @FXML
    private VBox suggestionBox;

    @FXML
    private Rating rating;

    private ObservableList<Book> books;
    private final BookDAO bookDAO = BookDAO.getInstance();
    private final IssueBookDBHistoryDAO issueBookDBHistoryDAO = IssueBookDBHistoryDAO.getInstance();

    public void initialize() {
        labelClicked(isbnLabel, isbnField);
        isbnField.setOnMouseClicked(event -> {
            if (isbnField.getText().isEmpty()) {
                isbnLabel.setVisible(false);
            }
        });

        isbnField.setOnKeyPressed(event -> {
            if (!isbnField.getText().isEmpty()) {
                isbnLabel.setVisible(false);
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


        fetchBookInBackground();

        issueIDColumn.setCellValueFactory(new PropertyValueFactory<>("issueBookID"));
        issueIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        issueTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        issueDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        issueStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<IssueBookDBHistory> issueBookDBHistories = issueBookDBHistoryDAO.getObservableList();
        IssueTableStudent.setItems(issueBookDBHistories);
        addReturnButtonToTable();
        addViewAndRateButtonToTable();
        searchButton.setOnAction(event -> {findBook(); System.out.println(searchField.getText());});
//        searchField.addEventFilter();
        setupAutoCompleteWithListView();
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
            showAlert("Error" , "failed to load books.");
            exception.printStackTrace();
        });
        new Thread(task).start();
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
                            isbnField.setText(book.getIsbn());
                            if (!isbnField.getText().isEmpty()) {
                                isbnLabel.setVisible(false);
                            }
                            String imageUrl = bookDAO.getImageUrl(book.getIsbn());
                            displayBookImageAsync(imageUrl);
                            displayRating(book.getIsbn());
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

    private void setupAutoCompleteWithListView() {
//        suggestionBox = new VBox();
//        suggestionBox.setPadding(new Insets(5));
        suggestionScrollPane.setVisible(false);
        suggestionScrollPane.setManaged(false);
        suggestionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        suggestionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            suggestionBox.getChildren().clear();

            if (!newValue.trim().isEmpty()) {
                Set<String> suggestions = new HashSet<>();

                for (Book book : books) {
                    String lowerCaseInput = newValue.toLowerCase();

                    if (book.getTitle().toLowerCase().contains(lowerCaseInput)) {
                        suggestions.add(book.getTitle());
                    }
                    if (book.getAuthor().toLowerCase().contains(lowerCaseInput)) {
                        suggestions.add(book.getAuthor());
                    }
                }

                List<String> limitedSuggestions = suggestions.stream()
                        .limit(7)
                        .collect(Collectors.toList());

                for (String suggestion : limitedSuggestions) {
                    Label suggestionLabel = new Label(suggestion);
                    suggestionLabel.setStyle("-fx-font-size: 12 ; -fx-padding: 2px; -fx-background-color: white; -fx-cursor: hand;");
//                    suggestionLabel.setMaxWidth(Double.MAX_VALUE);
                    suggestionLabel.setOnMouseClicked(event -> {
                        searchField.setText(suggestionLabel.getText());
                        suggestionScrollPane.setVisible(false);
                        suggestionScrollPane.setManaged(false);
                        searchField.getStyleClass().remove("suggestions-visible");
                    });
                    suggestionBox.getChildren().add(suggestionLabel);
                }

                boolean hasSuggestions = !suggestions.isEmpty();
                if (hasSuggestions) {
                    suggestionScrollPane.setPrefHeight(Math.min(150, limitedSuggestions.size() * 28));
                    if (!searchField.getStyleClass().contains("suggestions-visible")) {
                        searchField.getStyleClass().add("suggestions-visible");
                    }
                } else {
                    searchField.getStyleClass().remove("suggestions-visible");
                }
                suggestionScrollPane.setVisible(hasSuggestions);
                suggestionScrollPane.setManaged(hasSuggestions);
            } else {
                suggestionScrollPane.setVisible(false);
                suggestionScrollPane.setManaged(false);
                searchField.getStyleClass().remove("suggestions-visible");
            }
        });
    }

    private void displayRating(String ISBN) {
        rating.setVisible(true);
        rating.setManaged(true);
        rating.setRating(Math.round((float) bookDAO.getRatingScore(ISBN) / Math.max(bookDAO.getRatingCount(ISBN), 1)));
        rating.setOnMouseClicked(event -> {
            double newRating = rating.getRating();
            bookDAO.incrementRatingCount(ISBN);
            bookDAO.addRatingScore(ISBN, (int) newRating);
        });
    }

    public void displayBookImage(String imageUrl) {
        try {
            if (imageUrl != null) {
                Image image = new Image(imageUrl, true);
                imageView.setImage(image);
                imageView.setFitHeight(177);
                imageView.setFitWidth(140);
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

    public void displayBookImageAsync(String imageUrl) {
        // Hiển thị placeholder trong khi tải ảnh
        imageView.setImage(new Image("/placeholder.jpg"));
        imageView.setFitHeight(177);
        imageView.setFitWidth(140);

        // Tạo Task để tải ảnh trong luồng riêng
        Task<Image> loadImageTask = new Task<Image>() {
            @Override
            protected Image call() {
                return new Image(imageUrl, true);
            }
        };

        // Khi Task thành công, cập nhật ảnh vào ImageView
        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            imageView.setImage(image);
            imageView.setSmooth(true);
            imageView.setFitHeight(177);
            imageView.setFitWidth(140);
        });

        // Khi Task thất bại, hiển thị placeholder
        loadImageTask.setOnFailed(event -> {
            System.out.println("Failed to load img: " + imageUrl);
            imageView.setImage(new Image("/placeholder.jpg"));
            imageView.setFitHeight(177);
            imageView.setFitWidth(140);
        });

        // Chạy Task trong luồng riêng
        new Thread(loadImageTask).start();
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
