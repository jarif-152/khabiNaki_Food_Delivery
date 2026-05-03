CREATE TABLE customers (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(20),
  address VARCHAR(200) NOT NULL
);

CREATE TABLE restaurant_owners (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(20)
);

CREATE TABLE restaurants (
  id SERIAL PRIMARY KEY,
  owner_id INT REFERENCES restaurant_owners(id),
  name VARCHAR(100) NOT NULL,
  address VARCHAR(200) NOT NULL
);

CREATE TABLE restaurant_schedules (
  id SERIAL PRIMARY KEY,
  restaurant_id INT REFERENCES restaurants(id),
  open_time TIME NOT NULL,
  close_time TIME NOT NULL
);

CREATE TABLE menu_items (
  id SERIAL PRIMARY KEY,
  restaurant_id INT REFERENCES restaurants(id),
  name VARCHAR(100) NOT NULL,
  price NUMERIC(10,2) NOT NULL,
  available BOOLEAN NOT NULL DEFAULT TRUE,
  quantity INT NOT NULL DEFAULT 0,
  addons TEXT
);

CREATE TABLE coupons (
  id SERIAL PRIMARY KEY,
  code VARCHAR(50) UNIQUE NOT NULL,
  discount_percent INT NOT NULL
);

CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  customer_id INT REFERENCES customers(id),
  restaurant_id INT REFERENCES restaurants(id),
  total_price NUMERIC(10,2) NOT NULL,
  status VARCHAR(30) NOT NULL
);

CREATE TABLE order_items (
  id SERIAL PRIMARY KEY,
  order_id INT REFERENCES orders(id),
  menu_item_id INT REFERENCES menu_items(id),
  quantity INT NOT NULL
);

CREATE TABLE payments (
  id SERIAL PRIMARY KEY,
  order_id INT REFERENCES orders(id),
  method VARCHAR(20) NOT NULL,
  status VARCHAR(30) NOT NULL
);

CREATE TABLE riders (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE deliveries (
  id SERIAL PRIMARY KEY,
  order_id INT REFERENCES orders(id),
  rider_id INT REFERENCES riders(id),
  progress VARCHAR(50) NOT NULL
);


INSERT INTO customers (name, phone, address) VALUES
('Satoru Gojo', '01711-000001', 'Jujutsu High, Tokyo'),
('Naruto Uzumaki', '01811-000002', '123 Mirpur Road, Dhaka'),
('Aashnan', '01911-000003', 'Male Hall, Board Bazar, Gazipur'),
('Safwan Khan', '01722-000004', 'Dhanmondi 27, Dhaka'),
('Samnun', '01833-000005', 'Gulshan 2, Dhaka'),
('Monkey D. Luffy', '01611-000006', 'Sunny Ship, Grand Line'),
('Mikasa Ackerman', '01511-000007', 'Wall Rose, Shiganshina'),
('Alice Smith', '01744-000008', 'Banani, Dhaka'),
('John Doe', '01855-000009', 'Uttara Sector 10, Dhaka'),
('Jane Austen', '01966-000010', 'Baily Road, Dhaka');

INSERT INTO restaurant_owners (name, phone) VALUES
('Gordon Ramsay', '01722-111111'),
('Sanji Vinsmoke', '01822-222222'),
('Teuchi Ichiraku', '01933-333333'),
('Bob Belcher', '01644-444444'),
('Gus Fring', '01555-555555');

INSERT INTO coupons (code, discount_percent) VALUES
('WELCOME10', 10),
('HUNGRY20', 20),
('STUDENT50', 50),
('MIDTERM30', 30),
('FREEDEL', 100);

INSERT INTO riders (name, available) VALUES
('Speedy Gonzales', TRUE),
('Flash Thompson', FALSE),
('Roronoa Zoro', TRUE),
('Yuji Itadori', TRUE),
('Momen Miah', FALSE);

INSERT INTO restaurants (owner_id, name, address) VALUES
(1, 'Hells Kitchen Burgers', 'Gulshan Ave, Dhaka'),  
(2, 'Baratie Seafood', 'Coxs Bazar Beach'),    
(3, 'Ichiraku Ramen', 'Mirpur 10, Dhaka'),         
(4, 'Bobs Burgers', 'Dhanmondi, Dhaka'),
(5, 'Los Pollos Hermanos', 'Uttara, Dhaka'),
(1, 'Ramsay Roasts', 'Banani, Dhaka');

INSERT INTO restaurant_schedules (restaurant_id, open_time, close_time) VALUES
(1, '10:00:00', '22:00:00'),
(2, '11:00:00', '23:30:00'),
(3, '08:00:00', '23:59:00'),
(4, '09:00:00', '21:00:00'),
(5, '10:00:00', '22:00:00'),
(6, '12:00:00', '20:00:00');

INSERT INTO menu_items (restaurant_id, name, price, available, quantity, addons) VALUES
(1, 'Classic Cheeseburger', 450.00, TRUE, 50, 'Extra cheese, Bacon'), 
(1, 'Truffle Fries', 250.00, TRUE, 100, 'Spicy mayo'),               
(2, 'Seafood Risotto', 1200.00, TRUE, 20, NULL),                     
(2, 'Spicy Tuna Roll', 800.00, TRUE, 30, 'Soy sauce, Wasabi'),      
(3, 'Miso Pork Ramen', 650.00, TRUE, 40, 'Extra Chashu, Egg'),
(3, 'Spicy Tonkotsu Ramen', 700.00, TRUE, 30, 'Extra Nori'),
(4, 'Burger of the Day', 500.00, TRUE, 15, 'Onion rings'),
(4, 'Standard Fries', 150.00, TRUE, 200, 'Ketchup'),
(5, 'Fried Chicken Bucket', 1050.00, TRUE, 50, 'Biscuits, Gravy'),
(5, 'Spicy Curly Fries', 200.00, TRUE, 80, NULL),
(6, 'Beef Wellington', 2500.00, FALSE, 0, 'Mashed Potatoes'),
(6, 'Sticky Toffee Pudding', 600.00, TRUE, 25, 'Ice Cream');               

INSERT INTO orders (customer_id, restaurant_id, total_price, status) VALUES
(3, 3, 1300.00, 'Delivered'),   
(4, 5, 1250.00, 'Preparing'),   
(1, 1, 700.00, 'Pending'),
(2, 3, 2050.00, 'Delivered'),
(6, 2, 3200.00, 'Out for Delivery'),
(7, 4, 650.00, 'Delivered'),
(5, 1, 950.00, 'Cancelled'),
(8, 6, 600.00, 'Pending'),
(9, 5, 1050.00, 'Delivered'),
(10, 3, 650.00, 'Preparing');      

INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES
(1, 5, 2),
(2, 9, 1), (2, 10, 1),
(3, 1, 1), (3, 2, 1),
(4, 6, 2), (4, 5, 1),
(5, 3, 2), (5, 4, 1),
(6, 7, 1), (6, 8, 1),
(7, 1, 2),
(8, 12, 1),
(9, 9, 1),
(10, 5, 1);

INSERT INTO payments (order_id, method, status) VALUES
(1, 'bKash', 'Completed'),
(2, 'Credit Card', 'Completed'),
(3, 'Cash on Delivery', 'Pending'),
(4, 'Nagad', 'Completed'),
(5, 'Credit Card', 'Completed'),
(6, 'bKash', 'Completed'),
(7, 'bKash', 'Refunded'),
(8, 'Cash on Delivery', 'Pending'),
(9, 'Credit Card', 'Completed'),
(10, 'Nagad', 'Pending');

INSERT INTO deliveries (order_id, rider_id, progress) VALUES
(1, 1, 'Completed'),
(4, 3, 'Completed'),
(5, 4, 'On the way'),
(6, 1, 'Completed'),
(9, 2, 'Completed');