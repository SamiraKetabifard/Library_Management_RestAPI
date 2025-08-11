📚 Library Management System API

A secure Spring Boot REST API for managing library operations with JWT authentication, role-based access control, and comprehensive book management features.

🌟 Features

- 🔐 Authentication & Authorization
  - JWT-based secure authentication
  - Role-based access (ADMIN/USER)
  - Password encryption with BCrypt

- 📖 Book Management
  - CRUD operations for books (ADMIN only)
  - Pagination & sorting
  - Search by author
  - Category statistics

- 🔄 Borrowing System
  - Borrow/return books
  - Track borrowing history
  - Prevent duplicate borrows

- ⭐ Rating System
  - Users can rate borrowed books (1-5 stars)
  - View highly-rated books (ADMIN only)
  - 
  - 🛠️ Tech Stack
- **Backend**: Spring Boot, Spring Security, JPA/Hibernate
- **Database**: (Specify: MySQL)
- **Authentication**: JWT tokens
- **Testing**: JUnit 5, Mockito, MockMvc

    Auth Endpoints:

        POST /auth/registeruser - Register a normal user

        POST /auth/login - Login and get JWT token

        POST /admin/registeradmin - Register admin (requires admin role)

    Book Endpoints:

        GET /books/all - Get all books (paginated)

        GET /books/getbookbyid/{id} - Get book by ID

        POST /books/add - Add book (admin only)

        PUT /books/update/{id} - Update book (admin only)

        DELETE /books/deletebook/{id} - Delete book (admin only)

        GET /books/by-author - Get books by author (user role)

        GET /books/highly-rated - Get highly rated books (admin only)

        GET /books/category-stats - Get book count by category

    Rating Endpoints:

        POST /rating - Rate a book (user role)

    Borrow Endpoints:
        POST /borrowRecords/borrowBook/{bookId} - Borrow a book

    POST /borrowRecords/returnBook/{borrowRecordId} - Return a book

    GET /borrowRecords/all - Get all borrow records (admin only)

    GET /borrowRecords/my-borrowed-books - Get user's borrowed books
