INSERT INTO category (category_id, name, description, display_order, is_active)
VALUES
(1, 'Electronics', 'Phones and gadgets', 1, true),
(2, 'Clothing', 'Apparel and accessories', 2, true);

INSERT INTO brand (brand_id, name, supplier_info)
VALUES
(1, 'TechNova', 'TechNova Distributors'),
(2, 'UrbanWear', 'UrbanWear Apparel');

INSERT INTO product (product_id, name, description, price, stock_quantity,
                     weight, dimensions, color, size, is_available, is_featured,
                     category_id, brand_id, created_at)
VALUES
(1, 'Smartphone X', 'Latest-gen smartphone', 999.99, 25,
 0.3, '15x7x0.8 cm', 'Black', null, true, true, 1, 1, CURRENT_TIMESTAMP),
(2, 'Basic T-Shirt', 'Cotton t-shirt', 19.99, 100,
 null, null, 'White', 'M', true, false, 2, 2, CURRENT_TIMESTAMP);

ALTER TABLE product ALTER COLUMN product_id RESTART WITH 100;
ALTER TABLE category ALTER COLUMN category_id RESTART WITH 100;
ALTER TABLE brand ALTER COLUMN brand_id RESTART WITH 100;