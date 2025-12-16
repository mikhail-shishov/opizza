USE opizza;

-- -----------------------------------------------------
-- DEMO DATA: USERS & ADDRESSES
-- -----------------------------------------------------

-- 1. ADRESY (Potrebujeme 4 rôzne, plus jednu pre ADMINA)
INSERT INTO addresses (addresses_id, user_id, city, zip_code, street, house, type, is_default) VALUES
(1, 1, 'Nitra', '94901', 'Hlavná', '12', 'HOME', 1), -- Zákazník 1 (Ján)
(2, 2, 'Nitra', '94901', 'Pivovarská', '5', 'WORK', 1), -- Kuchár 1
(3, 3, 'Nitra', '94901', 'Mostná', '3', 'WORK', 1), -- Kuriér 1
(4, 4, 'Nitra', '94901', 'Štefánikova', '44', 'ADMIN', 1), -- Admin
(5, 1, 'Nitra', '94901', 'Vedľajšia', '5A', 'HOME', 0); -- Zákazník 1 (druhá adresa)

-- 2. POUŽÍVATELIA (Užívateľské roly, ako požaduje zadanie)
-- Heslo pre všetkých (pre demo): 'password' (v reále by bol bcrypt hash)
INSERT INTO users (user_id, email, password, first_name, last_name, phone, role, default_address_id) VALUES
(1, 'zakaznik@opizza.sk', '$2a$10$wI5f2eW.4L9g/0P.H1O0X.0qA1i1R5e1.0p.0a2h2P0j0e1f3Q2d1c1.', 'Ján', 'Hrach', '0901111111', 'ZAKAZNIK', 1),
(2, 'kuchar@opizza.sk', '$2a$10$wI5f2eW.4L9g/0P.H1O0X.0qA1i1R5e1.0p.0a2h2P0j0e1f3Q2d1c1.', 'Peter', 'Kotlík', '0902222222', 'KUCHAR', 2),
(3, 'kurier@opizza.sk', '$2a$10$wI5f2eW.4L9g/0P.H1O0X.0qA1i1R5e1.0p.0a2h2P0j0e1f3Q2d1c1.', 'Marek', 'Rýchly', '0903333333', 'KURIER', 3),
(4, 'admin@opizza.sk', '$2a$10$wI5f2eW.4L9g/0P.H1O0X.0qA1i1R5e1.0p.0a2h2P0j0e1f3Q2d1c1.', 'Eva', 'Majiteľka', '0904444444', 'ADMIN', 4);

-- 3. KOŠÍK (Jeden prázdny košík pre zákazníka)
INSERT INTO carts (carts_id, user_id) VALUES (1, 1);


-- -----------------------------------------------------
-- DEMO DATA: CATALOG
-- -----------------------------------------------------

-- 4. KATEGÓRIE
INSERT INTO categories (category_id, name, description, sort_order) VALUES
(1, 'Klasické pizze', 'Osvedčené recepty', 10),
(2, 'Špeciálne pizze', 'Naše originálne kreácie', 20);

-- 5. VEĽKOSTI (3 veľkosti na pizzu, ako je požadované)
INSERT INTO sizes (size_id, name, weight_grams, sort_order) VALUES
(1, 'Malá (28cm)', 400, 10),
(2, 'Stredná (32cm)', 550, 20),
(3, 'Rodinná (40cm)', 800, 30);

-- 6. INGREDIENCIE (Minimálne 15, ako je požadované)
INSERT INTO ingredients (ingredient_id, name, is_spicy) VALUES
(1, 'Paradajkový základ', 0), (2, 'Mozzarella', 0), (3, 'Šunka', 0), (4, 'Šampiňóny', 0), (5, 'Olivy', 0),
(6, 'Saláma', 0), (7, 'Kukurica', 0), (8, 'Cesnak', 0), (9, 'Feferónky', 1), (10, 'Slanina', 0),
(11, 'Ananás', 0), (12, 'Kuracie mäso', 0), (13, 'Cibuľa', 0), (14, 'Niva', 0), (15, 'Syr Eidam', 0),
(16, 'Bazalka', 0), (17, 'Kapary', 0), (18, 'Vajce', 0); -- Pridané naviac

-- 7. TAGY (Minimálne 5, ako je požadované)
INSERT INTO tags (tag_id, name, color_hex) VALUES
(1, 'Vegetariánska', '#4CAF50'),
(2, 'Pikantná', '#F44336'),
(3, 'Najobľúbenejšia', '#FFC107'),
(4, 'Nová', '#03A9F4'),
(5, 'Bezlepková', '#795548');

-- 8. PIZZE (Minimálne 10)
INSERT INTO products (product_id, name, description, category_id, is_available, image_url) VALUES
(1, 'Margherita', 'Klasická pizza s paradajkovým základom a mozzarellou.', 1, 1, 'url/margherita.jpg'),
(2, 'Capriciosa', 'Šunka, šampiňóny a olivy.', 1, 1, 'url/capriciosa.jpg'),
(3, 'Diabolo', 'Pikantná saláma, feferónky, cibuľa.', 1, 1, 'url/diabolo.jpg'),
(4, 'Hawai', 'Kontroverzná, ale obľúbená: šunka a ananás.', 1, 1, 'url/hawai.jpg'),
(5, 'Vegetariana', 'S množstvom čerstvej zeleniny.', 1, 1, 'url/vegetariana.jpg'),
(6, 'Quattro Formaggi', 'Štyri druhy syra.', 2, 1, 'url/4formaggi.jpg'),
(7, 'Sedliacka', 'Slanina, klobása, cibuľa a cesnak.', 2, 1, 'url/sedliacka.jpg'),
(8, 'Pizza Kuriér', 'Šunka, kukurica, slanina, volské oko.', 2, 1, 'url/kurier.jpg'),
(9, 'BBQ Kuracie', 'Kuracie mäso, BBQ omáčka, cibuľa.', 2, 1, 'url/bbq.jpg'),
(10, 'Tuniaková', 'Tuniak, cibuľa, olivy.', 2, 1, 'url/tuniakova.jpg');

-- 9. VARIANTY PIZZ (Každá pizza má 3 veľkosti = 30 variantov)
-- Ceny sú len ilustratívne
SET @base_price = 4.50;
INSERT INTO product_variants (product_id, size_id, price)
SELECT p.product_id, s.size_id, 
    @base_price + (p.product_id * 0.2) + (CASE s.size_id WHEN 1 THEN 0.00 WHEN 2 THEN 2.50 WHEN 3 THEN 5.00 END)
FROM products p
JOIN sizes s;

-- 10. PRIDELENIE INGREDIENCIÍ A TAGOV
-- Margherita
INSERT INTO product_ingredients (product_id, ingredient_id) VALUES (1, 1), (1, 2), (1, 16);
INSERT INTO product_tags (product_id, tag_id) VALUES (1, 3), (1, 1);
-- Diabolo
INSERT INTO product_ingredients (product_id, ingredient_id) VALUES (3, 1), (3, 2), (3, 6), (3, 9), (3, 13);
INSERT INTO product_tags (product_id, tag_id) VALUES (3, 2);
-- Vegetariana
INSERT INTO product_ingredients (product_id, ingredient_id) VALUES (5, 1), (5, 2), (5, 4), (5, 5), (5, 7), (5, 13);
INSERT INTO product_tags (product_id, tag_id) VALUES (5, 1);


-- -----------------------------------------------------
-- DEMO DATA: ORDERS (Minimálne 5 objednávok v rôznych stavoch)
-- -----------------------------------------------------

-- Objednávka 1: Pending (Môže byť zrušená)
INSERT INTO orders (order_id, user_id, status, total_price, note, delivery_address_id, delivery_phone) VALUES
(1, 1, 'PENDING', 12.00, 'Poprosím dvojitý syr na Diabolo!', 1, '0901111111');
INSERT INTO order_items (order_id, product_variant_id, quantity, price_at_order, product_name_at_order) VALUES
(1, 4, 1, 9.70, 'Diabolo (Stredná)'),
(1, 10, 1, 2.30, 'Cola (0.33l - ak by bola v katalógu)');

-- Objednávka 2: Preparing (Pracuje Kuchár 1)
INSERT INTO orders (order_id, user_id, status, total_price, note, delivery_address_id, delivery_phone) VALUES
(2, 1, 'PREPARING', 21.00, 'Bez cibule, dakujem.', 5, '0901111111');
INSERT INTO order_items (order_id, product_variant_id, quantity, price_at_order, product_name_at_order) VALUES
(2, 2, 1, 7.20, 'Capriciosa (Malá)'),
(2, 1, 2, 6.70, 'Margherita (Malá)');

-- Objednávka 3: Ready (Čaká na Kuriéra)
INSERT INTO orders (order_id, user_id, status, total_price, note, delivery_address_id, delivery_phone) VALUES
(3, 1, 'READY', 8.50, 'Platba kartou.', 1, '0901111111');
INSERT INTO order_items (order_id, product_variant_id, quantity, price_at_order, product_name_at_order) VALUES
(3, 19, 1, 8.50, 'Sedliacka (Malá)');

-- Objednávka 4: Delivering (Kuriér 1 ju má)
INSERT INTO orders (order_id, user_id, status, total_price, note, delivery_address_id, delivery_phone) VALUES
(4, 1, 'DELIVERING', 35.00, 'Veľká firemná objednávka.', 5, '0901111111');
INSERT INTO order_items (order_id, product_variant_id, quantity, price_at_order, product_name_at_order) VALUES
(4, 3, 2, 9.40, 'Capriciosa (Rodinná)'),
(4, 9, 1, 9.20, 'Hawai (Rodinná)');

-- Objednávka 5: Delivered (História)
INSERT INTO orders (order_id, user_id, status, total_price, note, delivery_address_id, delivery_phone) VALUES
(5, 1, 'DELIVERED', 10.00, 'Bez poznámky.', 1, '0901111111');
INSERT INTO order_items (order_id, product_variant_id, quantity, price_at_order, product_name_at_order) VALUES
(5, 1, 1, 6.70, 'Margherita (Malá)'),
(5, 6, 1, 3.30, 'Voda (0.5l - ak by bola v katalógu)');