-- Delete existing data
DELETE FROM items;

-- Insert test data
INSERT INTO items (name, description, status, location, date_reported, category, contact_name, contact_email, contact_phone, image_url, created_at, updated_at)
VALUES 
    ('iPhone 13', 'Black iPhone 13 with red case', 'FOUND', 'Library - Second Floor', '2025-05-12 14:30:00', 'ELECTRONICS', 'John Doe', 'john.doe@email.com', '123-456-7890', NULL, NOW(), NOW()),
    
    ('Blue Backpack', 'Nike backpack with laptop and books', 'LOST', 'Student Center', '2025-05-13 09:15:00', 'BAG', 'Jane Smith', 'jane.smith@email.com', '234-567-8901', NULL, NOW(), NOW()),
    
    ('Student ID Card', 'University ID card for Sarah Johnson', 'FOUND', 'Cafeteria', '2025-05-12 12:45:00', 'DOCUMENTS', 'Mike Wilson', 'mike.wilson@email.com', '345-678-9012', NULL, NOW(), NOW()),
    
    ('Gold Watch', 'Fossil gold watch with leather strap', 'LOST', 'Gym', '2025-05-11 17:20:00', 'ACCESSORIES', 'Emily Brown', 'emily.brown@email.com', '456-789-0123', NULL, NOW(), NOW()),
    
    ('MacBook Pro', 'Silver MacBook Pro 13-inch', 'FOUND', 'Engineering Building Room 205', '2025-05-13 10:00:00', 'ELECTRONICS', 'David Lee', 'david.lee@email.com', '567-890-1234', NULL, NOW(), NOW()),
    
    ('Car Keys', 'Toyota car keys with black fob', 'CLAIMED', 'Parking Lot B', '2025-05-10 15:30:00', 'KEYS', 'Lisa Chen', 'lisa.chen@email.com', '678-901-2345', NULL, NOW(), NOW()),
    
    ('Textbook', 'Introduction to Computer Science textbook', 'RETURNED', 'Computer Lab', '2025-05-09 13:45:00', 'BOOK', 'Tom Anderson', 'tom.anderson@email.com', '789-012-3456', NULL, NOW(), NOW());
