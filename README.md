# Library Management System - BTL - INT2204 7

## Các thành viên
| Họ Tên | Msv   |
| :-------- | :------- | 
| Nguyễn Trung Kiên | 23021589 |
| Nguyễn Tiến Vũ | 23021752 |
| Trần Thị Phương Thảo | 23020157 |
### Giới thiệu: Phần mềm quản lý thư viện
- [1. Mô tả](#1-mô-tả)
- [2. Chức năng chính](#2-chức-năng-chính)
- [3. Công cụ và công nghệ](#3-công-cụ-và-công-nghệ)
- [4. Cách sử dụng](#4-cách-sử-dụng)
- [5. Hướng dẫn cài đặt](#5-hướng-dẫn-cài-đặt)


### 1. Mô tả
Library Management System là một ứng dụng quản lý thư viện được phát triển bằng Java. Ứng dụng hỗ trợ quản lý tài liệu, người dùng và các chức năng liên quan đến mượn/trả tài liệu.

### 2. Chức năng chính:

- Quản lý tài liệu:
   + Thêm, Xóa, Sửa thông tin tài liệu.
   + Tìm kiếm thông tin tài liệu.
- Quản lý người dùng thư viện:
  + Mượn/trả tài liệu.
  + Thông tin thành viên, Xoá, Sửa, Tìm Kiếm.
- Chức năng tự sáng tạo: Đánh giá nhận xét tài liệu.
- Xử lý các trường hợp lỗi.
- Giao diện người dùng.
- Tích hợp API tra cứu thông tin tài liệu.


### 3. Công cụ và Công nghệ

- Java : Ngôn ngữ lập trình.
- JavaFX : Khung xây dựng giao diện người dùng đồ họa (GUI)
- SceneBuilder : Công cụ thiết kế giao diện JavaFX một cách trực quan.
- Google Books API : API được sử dụng để lấy dữ liệu sách.
- MySQL : Cơ sở dữ liệu lưu trữ thông tin.

### 4. Cách sử dụng

- Đăng kí: Nếu chưa có tài khoản, sinh viên có thể đăng kí tài khoản trong đó:
  + Nhập các trường như Username, Password, Name lần lượt là tên tài khoản, mật khẩu và họ và tên của bạn.
  + Đối với trường security question và answer là bạn sẽ chọn câu hỏi thông tin về bản thân và nhập câu trả lời để khi không nhớ mật khẩu, bạn có thể sử dụng nó để lấy lại mật khẩu.

![Screenshot_8](https://github.com/user-attachments/assets/d0dd0d36-75f4-4a27-9c92-166fb95be36a)

- Đăng nhập: Nhập vào tên và mật khẩu như đã đăng kí ở trên.

![Screenshot_4](https://github.com/user-attachments/assets/c5192e45-259a-4ea6-9881-43c9e0fe78bb)

- Đối với người dùng là thủ thư, sau khi nhấn Sign in giao diện Home sẽ hiện ra như hình trong đó:
   + Nút Manage Book để chuyển tới giao diện dành cho admin quản lý sách.
   + Nút Manage Student để chuyển tới giao diện dành cho admin quản lý người dùng.
   + Nút Log out để đăng xuất ra khỏi tài khoản và trở lại giao diện trang đăng nhập.

![image](https://github.com/user-attachments/assets/1c2f53cc-eb69-4f9d-9d67-0f8998c6a246)


- Quản lí sinh viên: Sau khi nhấn nút Manage Student sẽ chuyển đến giao diện quản lý người dùng như hình. Giao diện gồm các thao tác quản lý như:
  + Sửa, xoá, tìm kiếm thông tin sinh viên.
  + Gửi thông báo khi sinh viên đến hạn trả sách.

![Screenshot_3](https://github.com/user-attachments/assets/f0ce968e-ebb4-424d-a86d-4641895fe0b5)

- Quản lí sách: Sau khi nhấn nút Manage Book sẽ chuyển đến giao diện quản lý sách như hình. Ta có các thao tác chính như: 
  + Thủ thư nhập IBSN để check thông tin tài liệu từ API. Sau khi nhấn check các thông tin của tài liệu sẽ tự nhập vào các trường tương ứng, nếu không tồn tại hệ thống sẽ gửi thông báo đến.
  + Thủ thư có thể thêm, sửa, tìm kiếm, xoá sách.

![Screenshot_1](https://github.com/user-attachments/assets/d78594f8-500c-4678-9bb7-b3d38d7f3126)

- Đối với người dùng là sinh viên: Sau khi nhấn Sign In sẽ chuyển tới giao diện Home như hình.
  + Nhấn biểu tượng chuông để có thể xem các thông báo được gửi từ admin như thông báo trả sách hết hạn, ...
  + Người dùng nhấn Profile để cập nhật các thông tin về bản thân như mã sinh viên, họ và tên, lớp học và trường.
  + Log out để đăng xuất tài khoản chuyển về giao diện đăng nhập.

![Screenshot_2](https://github.com/user-attachments/assets/9cc7e4a0-243d-45f5-97e6-3d3000b277c3)

- Profile: Sau khi ở giao diện Home nhấn nút Profile ta sẽ chuyển đến giao diện profile.
  + Người dùng nhập các thông tin của bản thân theo các trường tương ứng để hệ thống quản lý tốt hơn. Sau khi nhập xong nhấn save để lưu lại các thông tin của bản thân.

![Screenshot_6](https://github.com/user-attachments/assets/3a77fc94-ddeb-4642-9bb6-94d3ab1b77d1)

- Quản lí sách: Sau khi nhấn nút Manage Book sinh viên sẽ chuyển đến giao diện để thao tác với tài liệu. Sinh viên có các thao tác chính sau: 
  + Sinh viên có thể tìm kiếm sách, xem thông tin sách, mượn, trả sách.
  + Khi nhấn vỉew hình ảnh của sách sẽ hiện ra cùng với điểm đánh giá của sách đó và ISBN của sách sẽ tự động nhập vào ô trống ở trên. Người dùng có thể nhấn vào biểu tượng các ngôi sao để chấm điểm cho sách, số ngôi sao tương ứng với số điểm và mỗi người dùng chỉ được đánh giá duy nhất 1 lần. Nếu muốn mượn sách, người dùng nhấn Issue Book để mượn.

![Screenshot_7](https://github.com/user-attachments/assets/d37b6116-d1aa-45ee-b531-433c584164cf)

- Quên mật khẩu: Khi người dùng quên mật khẩu, ở giao diện đăng nhập, nhấn vào nút forgot password sẽ chuyển tới giao diện để lấy lại mật khẩu. Người dùng có thể lấy lại mật khẩu bằng cách nhập mã sinh viên để tìm kiếm thông tin và trả lời đúng security question mật khẩu sẽ hiện ra.

![Screenshot_9](https://github.com/user-attachments/assets/5166a152-ae06-4bf7-ba10-3a56cd0b07ec)



### 5. Hướng dẫn cài đặt

- Để chạy project, mọi người mở phần mềm IntelliJ IDEA, mở thư mục LibraryManagement, sử dụng jdk23.0.1 và thêm thư viện sdk23.0.1, xóa VM option trước khi chạy.
- Nếu chạy bị lỗi, hãy kiểm tra lại các đường dẫn địa chỉ chỉ đến các file txt, fxml đã trùng khớp với code chưa.
- Chạy tệp DictionaryApplication trong package Dictionary để chạy chương trình.
