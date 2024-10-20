-- Insert roles and avoid duplicates
INSERT INTO roles (id, name) VALUES
                                 (4, 'creator'),
                                 (3, 'owner'),
                                 (2, 'moderator'),
                                 (1, 'user')
    ON CONFLICT (id) DO NOTHING;

-- Insert data field types and avoid duplicates
INSERT INTO data_field_type (id, type) VALUES
                                           (1, 'string'),
                                           (2, 'number'),
                                           (3, 'date'),
                                           (4, 'image'),
                                           (5, 'geolocation')
    ON CONFLICT (id) DO NOTHING;
