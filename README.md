# Whiskey Information System (WIS)

## 🎯 Project Overview  
The Whiskey Information System (WIS) is a JavaFX-based desktop application that allows users to browse, search, and manage data about whiskey products. The system supports user authentication, data validation, database queries, and an intuitive GUI for viewing whiskey details.

## 🛠️ Key Features  
- User login and registration  
- Display all whiskey entries from the database  
- Search by whiskey name or type  
- View detailed whiskey data (eg. name, age, type, region, rating)  
- Admin interface to add / edit / delete whiskey records  
- Validation of input data (e.g., no empty fields, valid ranges)  
- Robust exception handling and unit testing for core components

## 🎯 Technologies Used  
- Java 17 (or your chosen Java version)  
- JavaFX for the GUI  
- MVC / layered architecture (e.g., Data → Manager → Controller → View)  
- SQL database (e.g., SQLite / MySQL) for persistence  
- JUnit for unit testing  
- Git & GitHub for version control

## 📁 Project Structure  
/src

/app – Main application launcher

/controller – GUI controllers (JavaFX)

/model – Data model classes (WhiskeyData, UserData, etc)

/manager – Business logic and data access

/validator – Input data validation classes
/utils – Utility/helper classes
/tests – Unit test classes for manager, validator, dataset
/resources – FXML layouts, CSS, icons
