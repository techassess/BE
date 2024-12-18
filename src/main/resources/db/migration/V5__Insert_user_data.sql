INSERT INTO users (create_at, name, file_info_id, email, phone_number, dob, gender, username, password, is_active, is_deleted, rank_id) VALUES
    (NOW(), 'Nguyễn Ngọc Quang', null, 'nguyenvana@example.com', '0987654321', '1990-01-01', 'MALE', 'quangnn', '$2a$12$iJpZGqWVumo708EIzs5TPOUEpaHJXLfmQbJ5LF/2D0ne05r/jw1S.', 1, 0, 25),
    (NOW(), 'Hồ Xuân Đại', null, 'tranthib@example.com', '0976543210', '1992-05-15', 'FEMALE', 'daihx', '$2a$12$ysu.B.LOMnRZTtWU5fAaI.qkIJuCT7w9ypc7NRLVIdsYaa4cvLIdy', 1, 0, 2),
    (NOW(), 'Trịnh Thái Quân', null, 'levanc@example.com', '0965432109', '1988-09-30', 'MALE', 'quantt', '$2a$12$pRdlGKFRK.bgBLVuZUkbZeuQX/vt4LeO.i3tg3NlY6pLYx4bzsjwS', 1, 0, 3),
    (NOW(), 'Hà Cảnh Minh Quang', null, 'daovand@example.com', '0987654321', '1990-01-01', 'MALE', 'quanghcm', '$2a$12$iJpZGqWVumo708EIzs5TPOUEpaHJXLfmQbJ5LF/2D0ne05r/jw1S.', 1, 0, 4),
    (NOW(), 'Nguyễn Văn Linh', null, 'dinhthie@example.com', '0976543210', '1992-05-15', 'FEMALE', 'linhnv', '$2a$12$ysu.B.LOMnRZTtWU5fAaI.qkIJuCT7w9ypc7NRLVIdsYaa4cvLIdy', 1, 0, 4),
    (NOW(), 'Võ Văn Kiệt', null, 'lyvanf@example.com', '0965432109', '1988-09-30', 'MALE', 'kietvv', '$2a$12$pRdlGKFRK.bgBLVuZUkbZeuQX/vt4LeO.i3tg3NlY6pLYx4bzsjwS', 1, 0, 5);
