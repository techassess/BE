INSERT INTO department (name, deleted)
VALUES ('Phát Triển', false),
       ('Kinh Doanh', false),
       ('Nhân Sự', false),
       ('Tổng Vụ', false);



INSERT INTO department_criterias (department_id, criterias_id, question_id)
VALUES (1, 1, 1),
       (1, 1, 2),
       (2, 2, 2),
       (2, 2, 3),
       (3, 3, 3),
       (3, 3, 4),
       (4, 4, 4),
       (4, 4, 5),
       (5, 5, 5),
       (5, 5, 6);



INSERT INTO criterias (title,  point, visible_for, is_deleted)
VALUES ('Chất lượng công việc (độ chính xác, sáng tạo, tính cẩn thận...)', '0', 'MANAGER', false),
       ('Khả năng tối ưu hóa thời gian và nguồn lực để đạt kết quả tốt nhất', '0', 'SELF', false),
       ('Đóng góp tích cực vào công việc chung của nhóm', '0', 'CROSS', false),
       ('Tinh thần hợp tác, hỗ trợ đồng nghiệp', '0', 'ALL_MEMBER', false);








