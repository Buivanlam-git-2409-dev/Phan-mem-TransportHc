# Hệ Thống Quản Lý Vận Tải - TransportHC (Back-End)

Dự án Back-End quản lý vận tải được xây dựng trên nền tảng Spring Boot, hỗ trợ quản lý xe, chi phí, lương nhân viên, lịch trình và các báo cáo liên quan.

## 🛠 Công nghệ sử dụng

- **Ngôn ngữ:** Java 21
- **Framework:** Spring Boot 3.5.4
- **Cơ sở dữ liệu:** MySQL 8.0+
- **ORM:** Spring Data JPA + QueryDSL
- **Bảo mật:** Spring Security + JSON Web Token (JWT)
- **Lập lịch:** Quartz Scheduler
- **Real-time:** WebSockets
- **Xử lý Excel:** Apache POI
- **Công cụ hỗ trợ:** Lombok, Maven

## 📋 Yêu cầu hệ thống

- **Java Development Kit (JDK):** Phiên bản 21
- **Maven:** Phiên bản 3.8 trở lên
- **MySQL Server:** Phiên bản 8.0 trở lên
- **IDE:** IntelliJ IDEA (khuyên dùng) hoặc Eclipse/VS Code

## ⚙️ Hướng dẫn cấu hình

### 1. Cơ sở dữ liệu
- Khởi động MySQL Server.
- Tạo một database mới với tên: `transporthc_db`.
- Cấu hình thông tin kết nối trong file `src/main/resources/application.properties`:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/transporthc_db
  spring.datasource.username=YOUR_USERNAME
  spring.datasource.password=YOUR_PASSWORD
  ```
- Dự án được cấu hình `spring.jpa.hibernate.ddl-auto=update`, do đó các bảng sẽ tự động được tạo khi khởi chạy lần đầu.

### 2. Cài đặt Dependency
Chạy lệnh sau trong thư mục gốc của dự án để tải về các thư viện cần thiết:
```bash
mvn clean install
```
*Lưu ý: QueryDSL yêu cầu quá trình "compile" để tạo các lớp Q-type. Nếu bạn dùng IntelliJ, hãy chạy `mvn compile`.*

## 🚀 Khởi động ứng dụng

### Sử dụng Maven
```bash
mvn spring-boot:run
```

### Sử dụng file JAR
1. Build dự án:
   ```bash
   mvn clean package -DskipTests
   ```
2. Chạy file JAR:
   ```bash
   java -jar target/PrjTranportHC-0.0.1-SNAPSHOT.jar
   ```

Ứng dụng sẽ mặc định chạy tại địa chỉ: `http://localhost:8080`

## 📂 Cấu trúc thư mục chính

- `controller/`: Chứa các API endpoints.
- `service/`: Xử lý logic nghiệp vụ.
- `repository/`: Giao tiếp với cơ sở dữ liệu.
- `entity/`: Các lớp ánh xạ bảng database.
- `dto/`: Đối tượng chuyển đổi dữ liệu.
- `config/`: Cấu hình hệ thống (Security, WebSocket, Quartz).
- `utils/`: Các lớp tiện ích (Excel, File).

## 🛡 Bảo mật & API
- Hệ thống sử dụng JWT để xác thực. Các API (ngoại trừ `/api/auth/**`) yêu cầu Header: `Authorization: Bearer <TOKEN>`.
- Cấu hình Secret Key cho JWT trong `application.properties` (nếu cần thiết).

## 📝 Liên hệ
Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ đội ngũ phát triển.
