-- ============================================================
-- SEED DATABASE: Personal Finance App
-- Chạy file này để xóa sạch + nạp lại toàn bộ dữ liệu mẫu
-- Tương tự: db:seed --fresh
-- ============================================================

USE personal_finance_app;

-- ============================================================
-- BƯỚC 1: XÓA SẠCH DỮ LIỆU (theo thứ tự FK dependency)
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE ai_scan_logs;
TRUNCATE TABLE transaction_images;
TRUNCATE TABLE recurring_transactions;
TRUNCATE TABLE budgets;
TRUNCATE TABLE transactions;
TRUNCATE TABLE accounts;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- BƯỚC 2: TẠO USERS MẪU
-- ============================================================
INSERT INTO users (user_id, firebase_uid, full_name, email, phone, auth_provider) VALUES
(1, 'firebase_uid_demo_001', 'Nguyễn Văn An',  'an.nguyen@gmail.com',  '0901234567', 'firebase'),
(2, 'firebase_uid_demo_002', 'Trần Thị Bình',  'binh.tran@gmail.com',  '0912345678', 'firebase');

-- ============================================================
-- BƯỚC 3: TẠO DANH MỤC MẶC ĐỊNH (is_default = TRUE, user_id = NULL)
-- ============================================================
INSERT INTO categories (category_id, user_id, category_name, category_type, icon, color, is_default) VALUES
-- Chi tiêu (Expense)
(1,  NULL, 'Ăn uống',    'expense', 'ic_food',          '#FF9800', TRUE),
(2,  NULL, 'Di chuyển',  'expense', 'ic_transport',     '#2196F3', TRUE),
(3,  NULL, 'Mua sắm',   'expense', 'ic_shopping',      '#E91E63', TRUE),
(4,  NULL, 'Hóa đơn',   'expense', 'ic_bill',          '#9C27B0', TRUE),
(5,  NULL, 'Giải trí',  'expense', 'ic_entertainment', '#673AB7', TRUE),
(6,  NULL, 'Sức khỏe',  'expense', 'ic_health',        '#4CAF50', TRUE),
(7,  NULL, 'Khác',      'expense', 'ic_other',         '#607D8B', TRUE),
-- Thu nhập (Income)
(8,  NULL, 'Lương',     'income',  'ic_salary',        '#4CAF50', TRUE),
(9,  NULL, 'Thưởng',    'income',  'ic_bonus',         '#FFC107', TRUE),
(10, NULL, 'Đầu tư',   'income',  'ic_investment',    '#009688', TRUE),
(11, NULL, 'Khác',      'income',  'ic_other',         '#607D8B', TRUE);

-- Danh mục riêng do user 1 tự tạo
INSERT INTO categories (category_id, user_id, category_name, category_type, icon, color, is_default) VALUES
(12, 1, 'Học phí',      'expense', 'ic_education',  '#3F51B5', FALSE),
(13, 1, 'Freelance',    'income',  'ic_freelance',  '#00BCD4', FALSE);

-- ============================================================
-- BƯỚC 4: TẠO TÀI KHOẢN / VÍ TIỀN
-- ============================================================
INSERT INTO accounts (account_id, user_id, account_name, account_type, balance, currency) VALUES
-- User 1: Nguyễn Văn An
(1, 1, 'Ví tiền mặt',     'cash',        2500000.00, 'VND'),
(2, 1, 'Techcombank',      'bank',       15800000.00, 'VND'),
(3, 1, 'Thẻ tín dụng VIB', 'credit_card', -3200000.00, 'VND'),
-- User 2: Trần Thị Bình
(4, 2, 'Ví tiền mặt',     'cash',        1200000.00, 'VND'),
(5, 2, 'Vietcombank',      'bank',       22000000.00, 'VND');

-- ============================================================
-- BƯỚC 5: TẠO GIAO DỊCH MẪU (Tháng 6/2026)
-- ============================================================
INSERT INTO transactions (transaction_id, user_id, account_id, category_id, title, amount, transaction_type, transaction_date, note, status) VALUES
-- === User 1: Chi tiêu hàng ngày ===
-- Tuần 1 (01-07/06)
(1,  1, 1, 1, 'Phở bò sáng',              35000.00,  'expense', '2026-06-01', 'Quán phở Thìn',           'confirmed'),
(2,  1, 2, 2, 'Grab đi làm',              28000.00,  'expense', '2026-06-01', 'Nhà → Công ty',           'confirmed'),
(3,  1, 1, 1, 'Cơm trưa văn phòng',       45000.00,  'expense', '2026-06-02', NULL,                       'confirmed'),
(4,  1, 2, 3, 'Mua áo thun Uniqlo',       399000.00, 'expense', '2026-06-03', 'Sale 30%',                'confirmed'),
(5,  1, 1, 1, 'Trà sữa Phúc Long',        55000.00,  'expense', '2026-06-04', 'Trà sữa oolong + topping','confirmed'),
(6,  1, 2, 4, 'Tiền điện tháng 5',        850000.00, 'expense', '2026-06-05', 'EVN HCM',                 'confirmed'),
(7,  1, 2, 4, 'Tiền mạng FPT',            220000.00, 'expense', '2026-06-05', 'Gói 220k/tháng',          'confirmed'),

-- Tuần 2 (08-14/06)
(8,  1, 1, 1, 'Bún bò Huế',               40000.00,  'expense', '2026-06-08', NULL,                       'confirmed'),
(9,  1, 2, 5, 'Vé xem phim Avengers',     120000.00, 'expense', '2026-06-09', 'CGV Vincom, 2 vé',        'confirmed'),
(10, 1, 1, 1, 'Cà phê Highlands',         49000.00,  'expense', '2026-06-10', 'Freeze trà xanh',         'confirmed'),
(11, 1, 2, 6, 'Khám bệnh định kỳ',        500000.00, 'expense', '2026-06-11', 'Bệnh viện Hoàn Mỹ',      'confirmed'),
(12, 1, 1, 2, 'Đổ xăng xe máy',           80000.00,  'expense', '2026-06-12', 'Petrolimex Q.1',          'confirmed'),

-- Tuần 3 (15-21/06)
(13, 1, 1, 1, 'Cơm tấm sáng',             30000.00,  'expense', '2026-06-15', NULL,                       'confirmed'),
(14, 1, 2, 3, 'Mua giày Nike',            1890000.00,'expense', '2026-06-16', 'Nike Air Force 1',        'confirmed'),
(15, 1, 1, 7, 'Cắt tóc',                  80000.00,  'expense', '2026-06-17', 'Barber shop',             'confirmed'),
(16, 1, 2, 1, 'Mua đồ siêu thị WinMart', 650000.00, 'expense', '2026-06-18', 'Thịt, rau, gia vị tuần', 'confirmed'),

-- Tuần 4 (22-24/06)
(17, 1, 1, 1, 'Bánh mì sáng',             20000.00,  'expense', '2026-06-22', 'Bánh mì chả lụa',        'confirmed'),
(18, 1, 2, 12,'Đóng học phí IELTS',       3500000.00,'expense', '2026-06-23', 'Khóa 3 tháng',            'confirmed'),

-- === User 1: Thu nhập ===
(19, 1, 2, 8, 'Lương tháng 6',           18000000.00,'income',  '2026-06-10', 'Lương cứng',              'confirmed'),
(20, 1, 2, 9, 'Thưởng KPI Q2',            5000000.00,'income',  '2026-06-15', 'Đạt 120% KPI',            'confirmed'),
(21, 1, 2, 13,'Freelance dự án web',      3000000.00,'income',  '2026-06-20', 'Thiết kế landing page',   'confirmed'),

-- === User 1: Chuyển khoản nội bộ ===
(22, 1, 2, NULL, 'Rút ATM tiền mặt',     2000000.00,'transfer','2026-06-12', 'Techcombank → Tiền mặt',  'confirmed'),

-- === User 2: Giao dịch mẫu ===
(23, 2, 4, 1, 'Cơm trưa',                 40000.00,  'expense', '2026-06-05', NULL,                       'confirmed'),
(24, 2, 5, 2, 'Grab đi chợ',              15000.00,  'expense', '2026-06-06', NULL,                       'confirmed'),
(25, 2, 5, 3, 'Mua mỹ phẩm',             350000.00, 'expense', '2026-06-10', 'Innisfree toner',         'confirmed'),
(26, 2, 5, 8, 'Lương tháng 6',           12000000.00,'income',  '2026-06-10', NULL,                       'confirmed');

-- ============================================================
-- BƯỚC 6: TẠO NGÂN SÁCH / HẠN MỨC CHI TIÊU (Tháng 6/2026)
-- ============================================================
INSERT INTO budgets (budget_id, user_id, category_id, budget_name, amount_limit, daily_amount_limit, spent_amount, start_date, end_date) VALUES
(1, 1, 1, 'Ăn uống tháng 6',     3000000.00, 100000.00, 924000.00,  '2026-06-01', '2026-06-30'),
(2, 1, 2, 'Di chuyển tháng 6',    500000.00,  NULL,      108000.00,  '2026-06-01', '2026-06-30'),
(3, 1, 3, 'Mua sắm tháng 6',    2000000.00,  NULL,      2289000.00, '2026-06-01', '2026-06-30'),
(4, 2, 1, 'Ăn uống tháng 6',    1500000.00, 50000.00,   40000.00,   '2026-06-01', '2026-06-30');

-- ============================================================
-- BƯỚC 7: TẠO GIAO DỊCH ĐỊNH KỲ
-- ============================================================
INSERT INTO recurring_transactions (recurring_id, user_id, account_id, category_id, title, amount, transaction_type, repeat_type, repeat_interval, start_date, end_date, next_run_date, note, is_active) VALUES
(1, 1, 2, 4, 'Tiền nhà tháng',   5000000.00, 'expense', 'monthly', 1, '2026-01-01', NULL,          '2026-07-01', 'Trả tiền phòng trọ',   TRUE),
(2, 1, 2, 4, 'Tiền mạng FPT',     220000.00, 'expense', 'monthly', 1, '2026-01-05', NULL,          '2026-07-05', 'Gói 220k/tháng',       TRUE),
(3, 1, 2, 8, 'Lương hàng tháng',18000000.00, 'income',  'monthly', 1, '2026-01-10', NULL,          '2026-07-10', 'Lương cứng công ty',    TRUE),
(4, 2, 5, 8, 'Lương hàng tháng',12000000.00, 'income',  'monthly', 1, '2026-01-10', NULL,          '2026-07-10', NULL,                     TRUE);

-- ============================================================
-- BƯỚC 8: TẠO AI SCAN LOG MẪU
-- ============================================================
INSERT INTO ai_scan_logs (ai_scan_log_id, user_id, transaction_id, raw_ocr_text, detected_merchant, detected_amount, detected_date, suggested_category_id, actual_category_id, was_corrected, confirmed_at, confidence_score) VALUES
(1, 1, 16, 'WINMART\nĐịa chỉ: 123 Nguyễn Trãi Q1\nThịt heo: 120.000\nRau muống: 15.000\nGia vị: 35.000\nTổng cộng: 650.000\nNgày: 18/06/2026',
   'WinMart', 650000.00, '2026-06-18', 1, 1, FALSE, '2026-06-18 10:30:00', 0.9500),

(2, 1, 5,  'PHUC LONG COFFEE & TEA\nTrà sữa Oolong: 45.000\nTopping trân châu: 10.000\nTổng: 55.000\nNgày: 04/06/2026',
   'Phuc Long', 55000.00, '2026-06-04', 1, 1, FALSE, '2026-06-04 15:20:00', 0.9200),

(3, 1, 6,  'HOA DON TIEN DIEN\nEVN HCM\nKy: 05/2026\nSo tien: 850.000 VND\nNgay: 05/06/2026',
   'EVN HCM', 850000.00, '2026-06-05', 4, 4, FALSE, '2026-06-05 09:00:00', 0.8800);

-- ============================================================
-- BƯỚC 9: KIỂM TRA KẾT QUẢ
-- ============================================================
SELECT '=== SEED HOÀN TẤT ===' AS status;
SELECT 'users' AS tbl, COUNT(*) AS total FROM users
UNION ALL SELECT 'accounts',      COUNT(*) FROM accounts
UNION ALL SELECT 'categories',    COUNT(*) FROM categories
UNION ALL SELECT 'transactions',  COUNT(*) FROM transactions
UNION ALL SELECT 'budgets',       COUNT(*) FROM budgets
UNION ALL SELECT 'recurring',     COUNT(*) FROM recurring_transactions
UNION ALL SELECT 'ai_scan_logs',  COUNT(*) FROM ai_scan_logs;
