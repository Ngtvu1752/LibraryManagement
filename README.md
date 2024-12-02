
# Library Management System - BTL - INT2204 7




## Mô tả



## Sinh viên thực hiện
| Họ Tên | Msv   |
| :-------- | :------- | 
| Nguyễn Trung Kiên | 23021589 |
| Nguyễn Tiến Vũ | 23021752 |
| Trần Thị Phương Thảo | 23020157 |




## Chức năng:

- Quản lý tài liệu (Thêm, Xóa, Sửa, Tìm kiếm, Thông tin sách)
- Quản lý người dùng thư viện (mượn/trả tài liệu, thông
  tin thành viên, Xoá, Sửa, Tìm Kiếm).
- Chức năng tự sáng tạo (đánh giá nhận xét tài liệu).
- Xử lý các trường hợp lỗi.
- Giao diện người dùng.
- Tích hợp API tra cứu thông tin tài liệu.


## Công cụ và Công nghệ

- Java : Ngôn ngữ lập trình.
- JavaFX : Khung xây dựng giao diện người dùng đồ họa (GUI)
- SceneBuilder : Công cụ thiết kế giao diện JavaFX một cách trực quan.
- Google Books API : API được sử dụng để lấy dữ liệu sách.
- MySQL : Cơ sở dữ liệu lưu trữ thông tin.

## Cách sử dụng

Đăng nhập: nhập vào tên và mật khẩu.

![Screenshot_4](https://github.com/user-attachments/assets/c5192e45-259a-4ea6-9881-43c9e0fe78bb)

Đối với người dùng là thủ thư

![Screenshot_5](https://github.com/user-attachments/assets/bdc6edbd-aaf8-4007-94bb-c0b1e47660da)

- Quản lí sinh viên: thủ thư có thể sửa, xoá, tìm kiếm thông tin sinh viên

![Screenshot_2](https://github.com/user-attachments/assets/6835c82e-9a66-4516-989c-f5315263885b)

- Quản lí sách: thủ thư nhập IBSN để check thông tin tài liệu, thủ thư có thể thêm, sửa, tìm kiếm, xoá sách.

![Screenshot_1](https://github.com/user-attachments/assets/d78594f8-500c-4678-9bb7-b3d38d7f3126)

Đối với người dùng là sinh viên

![Screenshot_3](https://github.com/user-attachments/assets/0ab1b714-52f2-4d50-bd01-07e94a47ebde)

- Profile

![Screenshot_6](https://github.com/user-attachments/assets/3a77fc94-ddeb-4642-9bb6-94d3ab1b77d1)

- Quản lí sách: sinh viên có thể tìm kiếm sách, xem thông tin sách, mượn, trả và đánh giá sách.

![Screenshot_7](https://github.com/user-attachments/assets/d37b6116-d1aa-45ee-b531-433c584164cf)

Quên mật khẩu: người dùng có thể lấy lại mật khẩu bằng cách nhập id để tìm kiếm và trả lời đúng security answer.

![Screenshot_9](https://github.com/user-attachments/assets/5166a152-ae06-4bf7-ba10-3a56cd0b07ec)

Đăng kí: Nếu chưa có tài khoản, sinh viên có thể đăng kí tài khoản.

![Screenshot_8](https://github.com/user-attachments/assets/d0dd0d36-75f4-4a27-9c92-166fb95be36a)

## Demo

Insert gif or link to demo


## Hướng dẫn cài đặt

- Để chạy project, mọi người mở phần mềm IntelliJ IDEA, mở thư mục LibraryManagement, sử dụng jdk23.0.1 và thêm thư viện sdk23.0.1, xóa VM option trước khi chạy.
- Nếu chạy bị lỗi, hãy kiểm tra lại các đường dẫn địa chỉ chỉ đến các file txt, fxml đã trùng khớp với code chưa.
- Chạy tệp DictionaryApplication trong package Dictionary để chạy chương trình.
