package com.example.localization

enum class AppLanguage {
    ENGLISH, URDU
}

object Loc {
    fun tr(key: String, lang: AppLanguage): String {
        return if (lang == AppLanguage.URDU) {
            urduMap[key] ?: englishMap[key] ?: key
        } else {
            englishMap[key] ?: key
        }
    }

    private val englishMap = mapOf(
        "app_title" to "IMEI & Repair Super Tool Pro",
        "home" to "Home",
        "tools" to "Tools",
        "repairs" to "Repairs",
        "customers" to "Customers",
        "settings" to "Settings",
        
        // Login & Security
        "admin_login" to "Technician Secure Login",
        "admin_desc" to "Enter password to access database & admin dashboard",
        "password" to "Admin Access Password",
        "login" to "Login",
        "admin_bypass" to "System Bypass (Demo Account)",
        "invalid_pass" to "Incorrect credentials. Try 'admin123'.",
        "logout" to "Log Out",
        "logged_in_as" to "Authenticated as Administrator",
        "restricted_access" to "Login Required to access this module",
        
        // Tab Headers
        "dashboard" to "Dashboard Manager",
        "quick_stats" to "Quick Operational Statistics",
        "earnings" to "Total Earnings",
        "active_jobs" to "Active Jobs",
        "registered_clients" to "Registered Clients",
        "view_all" to "View All",

        // IMEI MODULE
        "imei_system_title" to "Advanced IMEI validator & Lookup",
        "imei_input_placeholder" to "Enter 15-Digit IMEI Code",
        "validate_imei" to "Validate IMEI",
        "validation_success" to "Valid IMEI Structure (Luhn Check Passed)",
        "validation_fail" to "Invalid IMEI Structure (Luhn Check Failed)",
        "imei_history" to "Audited IMEI History Ledger",
        "tac_check" to "TAC Allocation Check",
        "brand_model" to "Model/Brand Identity",
        "pta_tax_est" to "Est. PTA Customs Tax",
        "blacklist_status" to "Global Blacklist Status",
        "status_clean" to "Clean (Not Blacklisted / Active)",
        "status_blocked" to "Blacklisted (Reported Stolen or Lost)",
        "copy_success" to "Results copied to clipboard!",
        "share_summary" to "Share Comprehensive Audit",
        "clear_ledger" to "Clear History",
        "checksum_digit" to "Check-Digit",
        "reporting_org" to "Reporting Authority",
        "imei_format_error" to "IMEI must be exactly 15 digits",

        // PTA TAX MODULE
        "pta_tax_checker" to "PTA Tax & Status Checker",
        "tax_status" to "Approval Clearance Level",
        "pta_approved" to "Approved (Customs Tax Settled / Active)",
        "pta_unapproved" to "Unapproved (Tax Due / Demo Model)",
        "pta_blocked" to "Blocked (Substandard / Non-compliant)",
        "tax_table" to "Customs Tax Categories (Pakistan Rules)",
        "passport_registered" to "Passport Registration Discount Applied",
        "cnic_registered" to "CNIC Registration Tax Estimate",
        "payment_api_ready" to "Bank Gateway Integration Active (Sandbox)",
        "device_registration" to "Device Registration Info",

        // DEVICE INFO MODULE
        "device_diagnostics" to "Hardware & Diagnostics Detector",
        "specs_overview" to "Real-Time System Hardware Status",
        "phone_model" to "Device Model",
        "brand" to "Manufacturer",
        "board" to "Motherboard Platform",
        "android_version" to "Android Version OS",
        "ram_status" to "RAM Resources (Available / Total)",
        "storage_status" to "Internal Storage Resources",
        "battery_health" to "Estimated Battery Health",
        "battery_temp" to "Battery Temperature",
        "cpu" to "Central Processing Core",
        "hardware_serial" to "Hardware Board ID",

        // REPAIR SYSTEM
        "repair_invoice" to "Repair Ticketing & Invoicing",
        "fault_type" to "Fault Category Selection",
        "issue_desc" to "Detailed Issue Description",
        "repair_cost" to "Calculated Service & Parts Cost",
        "generate_invoice" to "Generate Official Bill Receipt",
        "invoice_id" to "Invoice Transaction ID",
        "status" to "Job Execution Status",
        "status_pending" to "Pending (Awaiting Parts)",
        "status_progress" to "In Progress",
        "status_completed" to "Completed & Verified",
        "status_delivered" to "Delivered & Paid",
        "whatsapp_share" to "Send Invoice on WhatsApp",
        "export_pdf" to "Export PDF Receipt",
        "add_repair_job" to "Create New Repair Ticket",
        "cost_calculator" to "Parts & Service Pricing",

        // CUSTOMER MANAGEMENT
        "customer_database" to "Client Relationship Directory",
        "add_new_customer" to "Register New Shop Client",
        "search_label" to "Search Customers (Name / Phone / Device)",
        "customer_name" to "Customer Full Name",
        "phone_number" to "Mobile Contact Number",
        "email_address" to "Email Index (Optional)",
        "visit_history" to "Client Chronology & Visit Logs",
        "delete_confirm" to "Are you sure you want to delete this customer record? All associated repair tickets will be marked unlinked.",
        "save_record" to "Save Customer Record",

        // QR CODE & BARCODE MODULE
        "qr_system" to "QR Tagging & Barcode Desk",
        "qr_generator" to "Generate Device QR Tracking Tag",
        "qr_desc" to "Create search tags containing repair bills, IMEI and specs to stick on repair bags.",
        "qr_camera" to "Live Scan Desk Simulator",
        "scan_mock" to "Simulate Scanning Barcode on Device Drawer",
        "scanned_code" to "Detected Metadata",

        // PRICE & MARKET SYSTEM
        "market_index" to "Mobile Market Appraisal Desk",
        "used_calculator" to "Used Handset Value Evaluator",
        "condition_grade" to "Physical Integrity Grade",
        "box_accessories" to "Full Box & Original Charger Added (+15% Value)",
        "pta_bonus" to "Device is PTA Approved (+30% Value)",
        "base_market_price" to "Estimated Brand New Base Price",
        "estimated_value" to "Current Retail Trade Value Value",
        "market_trends" to "Manual Trend Index & Insights",

        // TOOLS SECTION
        "tools_suite" to "Technician Diagnostics Toolbar",
        "flashlight_desc" to "Camera LED Hardware Controller",
        "flashlight_on" to "Toggle Flashlight ON/OFF",
        "network_tester" to "Carrier & Network Status",
        "ping_ms" to "Response Latency Ping",
        "sim_info" to "Subscriber Identity Module (SIM) details",
        "device_hw_test" to "Interactive Component Test Matrix",
        "screen_test" to "RGB Pixels Diagnostics",
        "screen_test_desc" to "Click to flashes full screen Red, Green, Blue to spot dead subpixels",
        "speaker_test" to "Speaker Audio Check",
        "speaker_desc" to "Plays test tone to verify visual audio frequency range",
        "vibe_test" to "Haptic Engine Diagnostic",
        "vibe_desc" to "Flashes vibration sequence on motor",

        // REPORTS SYSTEM
        "operations_report" to "Enterprise Reports & Accounting",
        "report_sheet" to "Full Operations Statement (PDF)",
        "share_report" to "Share Business Statement",
        "print_bill" to "Spool to Thermal Printer",
        "revenue_breakdown" to "Total Business Activity Statements",

        // SETTINGS
        "settings_title" to "Global Configurations",
        "language_choice" to "Primary Application Language",
        "theme_choice" to "High Contrast Obsidian Dark Mode",
        "backup_restore" to "Local Database Administration",
        "backup_btn" to "Secure Export Backup (JSON Format)",
        "restore_btn" to "Secure Import Backup",
        "reset_btn" to "Wipe Database (Factory App Reset)",
        "reset_warning" to "This will erase all customer records, repair history, and database entries permanently. This cannot be undone.",
        "action_not_undone" to "Warning: This action is final.",
        "backup_success" to "Backup written to storage successfully",
        "restore_success" to "Database restored successfully",
        "reset_success" to "Database reset successfully",
        "yes" to "Yes, Proceed",
        "no" to "Cancel"
    )

    private val urduMap = mapOf(
        "app_title" to "آئی ایم ای آئی اور موبائل ریپیئر سپر ٹول پرو",
        "home" to "ہوم",
        "tools" to "ٹولز",
        "repairs" to "ریپیئرنگ",
        "customers" to "گاہک",
        "settings" to "ترتیبات",

        // Login & Security
        "admin_login" to "ٹیکنیشن لاگ ان",
        "admin_desc" to "ڈیٹا بیس اور ایڈمن ڈیش بورڈ تک رسائی کے لیے پاس ورڈ درج کریں",
        "password" to "ایڈمن پاس ورڈ",
        "login" to "لاگ ان کریں",
        "admin_bypass" to "سسٹم ڈیمو اکاؤنٹ",
        "invalid_pass" to "غلط پاس ورڈ! 'admin123' آزمائیں۔",
        "logout" to "لاگ آؤٹ",
        "logged_in_as" to "ایڈمنسٹریٹر لاگ ان ہے",
        "restricted_access" to "اس ماڈیول تک رسائی کے لیے لاگ ان کرنا ضروری ہے",

        // Tab Headers
        "dashboard" to "ڈیش بورڈ مینیجر",
        "quick_stats" to "آپریشنل اعداد و شمار",
        "earnings" to "کل آمدنی",
        "active_jobs" to "جاری کام",
        "registered_clients" to "رجسٹرد گاہک",
        "view_all" to "سب دیکھیں",

        // IMEI MODULE
        "imei_system_title" to "IMEI تصدیق اور تلاش",
        "imei_input_placeholder" to "15 ہندسوں کا IMEI کوڈ درج کریں",
        "validate_imei" to "آئی ایم ای آئی چیک کریں",
        "validation_success" to "درست آئی ایم ای آئی رپورٹ (Luhn فارمولہ پاس)",
        "validation_fail" to "غلط آئی ایم ای آئی رپورٹ (Luhn فارمولہ فیل)",
        "imei_history" to "چیک شدہ آئی ایم ای آئی کی تاریخ",
        "tac_check" to "ٹیک مختص چیک",
        "brand_model" to "برانڈ اور موبائل ماڈل",
        "pta_tax_est" to "اندازاً پی ٹی اے کسٹم ٹیکس",
        "blacklist_status" to "بلیک لسٹ اسٹیٹس",
        "status_clean" to "بالکل صاف (بلیک لسٹ نہیں ہے / ایکٹو)",
        "status_blocked" to "بلیک لسٹڈ (چوری کی رپورٹ شدہ)",
        "copy_success" to "رپورٹ کاپی ہو گئی ہے!",
        "share_summary" to "مکمل آڈٹ شیئر کریں",
        "clear_ledger" to "تاریخ صاف کریں",
        "checksum_digit" to "چیک ہندسہ",
        "reporting_org" to "رپورٹنگ اتھارٹی",
        "imei_format_error" to "آئی ایم ای آئی کو لازمی طور پر 15 ہندسوں کا ہونا چاہیے",

        // PTA TAX MODULE
        "pta_tax_checker" to "پی ٹی اے ٹیکس اور اسٹیٹس چیکر",
        "tax_status" to "پی ٹی اے منظوری کی سطح",
        "pta_approved" to "منظور شدہ (ٹیکس ادا شدہ ہے / رجسٹرڈ)",
        "pta_unapproved" to "غیر منظور شدہ (ٹیکس واجب الادا ہے)",
        "pta_blocked" to "بند ہے (سیکیورٹی / معیار وجوہات پر بلاکڈ)",
        "tax_table" to "موبائل کسٹم ٹیکس کیٹیگریز",
        "passport_registered" to "پاسپورٹ رجسٹریشن رعایت لاگو ہے",
        "cnic_registered" to "شناختی کارڈ پر ٹیکس کا تخمینہ",
        "payment_api_ready" to "بینک گیٹ وے انٹیگریشن ایکٹو (ٹرائل)",
        "device_registration" to "ڈیوائس رجسٹریشن معلومات",

        // DEVICE INFO MODULE
        "device_diagnostics" to "ہارڈ ویئر اور سسٹم کی معلومات",
        "specs_overview" to "سسٹم ہارڈ ویئر مانیٹر",
        "phone_model" to "موبائل ماڈل",
        "brand" to "کمپنی برانڈ",
        "board" to "مدر بورڈ پلیٹ فارم",
        "android_version" to "اینڈرائیڈ او ایس ورژن",
        "ram_status" to "کل ریم میموری (دستیاب / کل)",
        "storage_status" to "اندرونی اسٹوریج میموری",
        "battery_health" to "بیٹری کی متوقع صحت STATUS",
        "battery_temp" to "بیٹری کا درجہ حرارت",
        "cpu" to "سی پی یو پروسیسر کور",
        "hardware_serial" to "بورڈ سیریل نمبر",

        // REPAIR SYSTEM
        "repair_invoice" to "موبائل ریپیئرنگ رسیدیں",
        "fault_type" to "خرابی کی قسم منتخب کریں",
        "issue_desc" to "خرابی کی تفصیل",
        "repair_cost" to "متبادل پرزہ اور سروس لاگت",
        "generate_invoice" to "سرکاری رسید تیار کریں",
        "invoice_id" to "رسید نمبر ID",
        "status" to "مرمت کا اسٹیٹس",
        "status_pending" to "مرمت ہونا باقی (پرزہ دستیاب نہیں)",
        "status_progress" to "مرمت جاری ہے",
        "status_completed" to "مرمت مکمل (پاسڈ)",
        "status_delivered" to "گاہک کو دے دی گئی (ادائیگی مکمل)",
        "whatsapp_share" to "واٹس ایپ پر رسید شیئر کریں",
        "export_pdf" to "رسید پی ڈی ایف رپورٹ بنائیں",
        "add_repair_job" to "نیا ریپیئر ٹکٹ بنائیں",
        "cost_calculator" to "سروس ریٹ لسٹ کیلکولیٹر",

        // CUSTOMER MANAGEMENT
        "customer_database" to "خریدار / گاہک کسٹمر ریکارڈ",
        "add_new_customer" to "نیا گاہک رجسٹر کریں",
        "search_label" to "گاہک تلاش کریں (نام / فون / ماڈل)",
        "customer_name" to "گاہک کا مکمل نام",
        "phone_number" to "رابطہ موبائل نمبر",
        "email_address" to "ای میل پتا (اختیاری)",
        "visit_history" to "گاہک کی خریداری اور آمد کی تاریخ",
        "delete_confirm" to "کیا آپ واقعی کسٹمر ریکارڈ حذف کرنا چاہتے ہیں؟ تمام متعلقہ ٹکٹ ختم ہو جائیں گے۔",
        "save_record" to "ریکارڈ محفوظ کریں",

        // QR CODE & BARCODE MODULE
        "qr_system" to "کیو آر اور بارکوڈ ڈیسک",
        "qr_generator" to "کیو آر موبائل ٹریکنگ اسٹیکر بنائیں",
        "qr_desc" to "ریپیئر بیگ پر لگانے کے لیے کیو آر ٹیگ بنائیں جس میں بل نمبر اور آئی ایم ای آئی شامل ہو۔",
        "qr_camera" to "لائیو کیو آر اسکینر سیمولیٹر",
        "scan_mock" to "فون بیگ بارکوڈ اسکین سیمولیشن چلائیں",
        "scanned_code" to "ٹیگ سے دستیاب ڈیٹا",

        // PRICE & MARKET SYSTEM
        "market_index" to "موبائل مارکیٹ قیمت اور تخمینہ",
        "used_calculator" to "پرانے فون کی قیمت کا کیلکولیٹر",
        "condition_grade" to "موبائل حالت کا گریڈ",
        "box_accessories" to "باکس اور چارجر موجود ہے (+15 فیصد اضافہ)",
        "pta_bonus" to "پی ٹی اے منظور شدہ فون ہے (+30 فیصد اضافہ)",
        "base_market_price" to "نئے موبائل کی متوقع مارکیٹ قیمت",
        "estimated_value" to "موبائل کی موجودہ تجارتی قیمت",
        "market_trends" to "مارکیٹ ریٹس اور اتار چڑھاؤ",

        // TOOLS SECTION
        "tools_suite" to "ٹیکنیشن ڈائگنوسٹک الییکٹرانکس ٹولز",
        "flashlight_desc" to "کیمرہ ایل ای ڈی ٹارچ لائٹ",
        "flashlight_on" to "ٹارچ لائٹ آن یا آف کریں",
        "network_tester" to "موبائل نیٹ ورک اور سگنل رفتار ٹیسٹ",
        "ping_ms" to "لٹینسی پنگ ردعمل کی رفتار",
        "sim_info" to "سیم کارڈ (Subscriber) کی تفصیلات",
        "device_hw_test" to "موبائل ہارڈ ویئر چیک چیکر",
        "screen_test" to "اسکرین کے پکسل چیک کریں",
        "screen_test_desc" to "سکرین پر سرخ، سبز اور نیلا رنگ دکھا کر خراب پکسلز کا معائنہ کریں",
        "speaker_test" to "موبائل اسپیکر ٹیسٹ",
        "speaker_desc" to "اسپیکر کی آواز اور فریکوئنسی چیک کرنے کے لیے ٹون چلائیں",
        "vibe_test" to "وائبریشن موٹر ٹیسٹ",
        "vibe_desc" to "تھرتھراہٹ (Vibration) کی شدت اور موٹر کا معائنہ کریں",

        // REPORTS SYSTEM
        "operations_report" to "بزنس رپورٹس اور مکمل اکاؤنٹنٹ شیٹ",
        "report_sheet" to "آمدنی اور سرگرمی کی تفصیلی پی ڈی ایف شیٹ",
        "share_report" to "مکمل آپریشنل رپورٹ شیئر کریں",
        "print_bill" to "تھرمل رسید پرنٹر",
        "revenue_breakdown" to "دکان کا معاشی کاروباری گوشوارہ",

        // SETTINGS
        "settings_title" to "عام ترتیبیں",
        "language_choice" to "کاروباری پسندیدہ زبان",
        "theme_choice" to "تاریک برائٹ موڈ (Obsidian Theme)",
        "backup_restore" to "لوکل ڈیٹا بیس بیک اپ اور انتظام",
        "backup_btn" to "ڈیٹا کا بیک اپ فائل بنائیں (JSON)",
        "restore_btn" to "بیک اپ فائل ری اسٹور کریں",
        "reset_btn" to "فیکٹری ری سیٹ (تمام ریکارڈ صاف کریں)",
        "reset_warning" to "اس سے کسٹمر کے تمام ریکارڈ، ریپیئرنگ ٹکٹس اور ڈیٹا مستقل طور پر حذف ہو جائیں گے۔ یہ عمل واپس نہیں لیا جا سکتا۔",
        "action_not_undone" to "تنبہ: یہ عمل حتمی ہے اور واپس نہیں لیا جائے گا۔",
        "backup_success" to "سورس ڈیٹا کا بیک اپ کامیابی سے بنا دیا گیا ہے",
        "restore_success" to "ڈیٹا بیس کامیابی سے ری اسٹور ہو گیا ہے",
        "reset_success" to "سارا ڈیٹا کامیابی سے صاف کر دیا گیا ہے",
        "yes" to "ہاں، جاری رکھیں",
        "no" to "کینسل"
    )
}
