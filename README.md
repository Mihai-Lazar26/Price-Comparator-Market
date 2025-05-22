# 🛒 Price Comparator – Java Spring Boot App

📍 **Live Demo:** [Watch here](https://youtube.com)  

This project is a price comparison backend built with **Spring Boot**, designed to:

- Load and analyze product price data from multiple stores
- Suggest cheaper alternatives from other stores
- Optimize shopping carts by selecting the cheapest available products
- Track price history and send alerts when prices drop below a threshold
- Display the top available discounts across all stores

---

## ⚙️ Technologies Used

- Java 22
- Spring Boot
- Maven
- OpenCSV
- JUnit 5 & Mockito

---

## 🚀 How to Run

### 1. Clone the repository

```bash
git clone https://github.com/Mihai-Lazar26/Price-Comparator-Market.git
cd price-comparator
```
### 2. Build the project

```bash
mvn clean install
```
### 3. Run the application

```bash
mvn spring-boot:run
```

The app will start at:
📍 `http://localhost:8080`

---

## 📁 File Structure
All CSV files are stored under `src/main/resources/`:

```
resources/
├── discounts/
│   ├── lidl_discounts_2025-05-01.csv
│   ├── kaufland_discounts_2025-05-01.csv
│   ├── profi_discounts_2025-05-01.csv
│   └── ...
├── products-prices/
│   ├── lidl_2025-05-01.csv
│   ├── kaufland_2025-05-01.csv
│   ├── profi_2025-05-01.csv
│   └── ...
```

---

## 🔗 REST API Endpoints

### ✅ Load Products & Discounts

```http
GET /api/products/load?file=products-prices/lidl_2025-05-01.csv&source=Lidl
GET /api/discounts/load?file=discounts/lidl_discounts_2025-05-01.csv&source=Lidl
```

These endpoints allow you to manually load data from specific CSV files.  
📌 **Note:** By default, the application automatically loads the most recent CSV files for each store at startup.  
No manual loading is required to use the main features.

---

### ✅ Get Optimized Cart

```http
POST /api/products/optimize-cart
Content-Type: application/json

[
  {
    "name": "lapte",
    "quantity": 2,
    "unit": "l"
  },
  {
    "name": "cafea",
    "quantity": 1,
    "unit": "kg"
  }
]
```

---

### ✅ Get Product Recommendations

```http
GET /api/products/recommendations?productId=P001
```

---

### ✅ Get Price History

```http
GET /api/products/history?productId=P001&store=Lidl&category=lactate&brand=Zuzu
```
Parameters `productId`, `store`, `category` and `brand` are all optional

---

### ✅ Alerts

```http
POST /api/alerts
{
  "productId": "P001",
  "targetPrice": 8.5
}

DELETE /api/alerts?productId=P001

GET /api/alerts/active
```

---

## 🧪 Running Tests

```bash
mvn test
```

Unit tests are written for:

* ProductService
* DiscountService
* PriceComparatorService
* AlertService

---

## 🧠 Notes

* Data is loaded from static CSVs in the resources folder
* Only products with the same unit and name are compared
* Price history compares all available dates
* Optimized cart compares **same-day** products only

---

## 📅 Author

**Mihai Lazăr**  
Software Engineering Student  
📍 Bucharest, Romania  
