package com.example.data

object I18nService {

    // Language keys: "English", "Hindi", "Telugu"
    val languages = listOf("English", "Hindi", "Telugu")

    private val uiTranslations = mapOf(
        "English" to mapOf(
            "app_title" to "Republic Student One",
            "lscep" to "Life Skills & Citizenship Program (LSCEP)",
            "login" to "Login",
            "logout" to "Logout",
            "select_role" to "Select Your Dashboard Portal",
            "student" to "Student",
            "parent" to "Parent",
            "teacher" to "Teacher",
            "school" to "School Management",
            "admin" to "Republic Editor",
            "dashboard" to "Dashboard",
            "lscep_modules" to "LSCEP Modules",
            "assessment" to "AI Psychometric Assessment",
            "newspaper" to "Student Newspaper",
            "podcasts" to "Podcasts & Media",
            "notices" to "School Notices",
            "attendance" to "Attendance",
            "counselling" to "Counselling",
            "reports" to "Academic Reports",
            "class_level" to "Class",
            "submit" to "Submit",
            "approved" to "Approved",
            "pending" to "Pending",
            "rejected" to "Rejected",
            
            // Login & Portal Selection Screen
            "enter_username" to "Enter Username",
            "login_subtitle" to "Empowering India's Next Generation with Civil, Financial, and Digital Literacy",
            "role_selection_header" to "Select Active Portal",
            "active_user" to "Active Student",
            "not_logged_in" to "Please Log In to Access",
            "login_button_text" to "Access Student Portal",

            // Dashboard
            "welcome_back" to "Welcome Back",
            "daily_attendance" to "Daily Attendance Tracker",
            "total_students" to "Total Class Enrollment",
            "present_today" to "Present Today",
            "absent_today" to "Absent Today",
            "submit_attendance" to "Mark Daily Attendance",
            "view_report_card" to "View Report Details",
            "overall_progress" to "Overall Program Progress",
            "completed_lessons" to "Completed Lessons",
            "average_quiz_score" to "Average Quiz Score",
            "academic_year" to "Academic Year 2026",
            "school_notices_board" to "Official Notice Board",

            // LSCEP Screens
            "lesson_materials" to "Interactive Lesson Materials",
            "estimated_duration" to "Duration",
            "min_duration" to "mins",
            "take_quiz" to "Take Practice Lesson Quiz",
            "quiz_title" to "Concept Check Quiz",
            "correct_answer" to "Correct!",
            "incorrect_answer" to "Incorrect, try again!",
            "congratulations" to "Congratulations!",
            "quiz_completed" to "You completed the concept check!",
            "your_score" to "Your Score",
            "close_button" to "Close",
            "submit_score" to "Submit Score",

            // Psychometric Portal
            "assessment_portal" to "Student Aptitude & Psychometric Guidance Portal",
            "assessment_desc" to "This multi-dimensional diagnostic evaluates personality traits, cognitive aptitude, learning styles, values, leadership, and digital behaviour. It suggests secondary streams (PCM, Commerce, Humanities) and career paths.",
            "select_mode" to "Select Assessment Scope",
            "quick_mode" to "Quick Evaluation (9 questions - 1/category)",
            "detailed_mode" to "Detailed Diagnosis (18 questions - 2/category)",
            "comp_mode" to "Comprehensive Audit (27 questions - 3/category)",
            "start_test" to "Begin Aptitude Evaluation",
            "question_progress" to "Question",
            "of" to "of",
            "submitting_assessment" to "Submitting assessment to AI Counseling Engine...",
            "report_card_header" to "OFFICIAL GUIDANCE REPORT CARD",
            "strengths_header" to "Identified Key Strengths",
            "learning_style_header" to "Dominant Learning Style Profile",
            "recommended_stream" to "RECOMMENDED SECONDARY EDUCATION STREAM",
            "career_interests" to "Compatible Career Interest Clusters",
            "behavioral_insights" to "Behavioural Insights & Values Quotient",
            "recommended_skills" to "Recommended Skill Development Pathways",
            "counselor_notes_header" to "Professional School Counselor Recommendations",
            "save_counselor_notes" to "Save Counselor Guidance Details",
            "disclaimer" to "Official Counsel Notice: This psychometric report is compiled dynamically by the Republic Student Publications AI Counseling Engine based on self-reported scientific dimension indicators. It is not a medical or psychological diagnosis and is intended strictly as an educational pathway reference. Academic streams and career paths should be verified via personal consultations with teachers and counselors.",
            "download_report" to "Download Full PDF Report",
            "close_report" to "Close Report Card",
            "historical_eval" to "Historical Assessment Cycles",
            "view_report_button" to "View Analysis",
            "new_test_warning" to "Starting a new test will record a brand new assessment cycle.",

            // Newspaper
            "editorial_features" to "Today's Editorial Features",
            "student_columns" to "Student Columns & Peer Innovations",
            "submit_column" to "Submit Student Column / Poem / Art",
            "title_label" to "Submission Title",
            "content_label" to "Submission Text / Content",
            "category_label" to "Choose Category",
            "submit_to_editor" to "Submit to Republic Editor",
            "pending_submissions" to "Your Pending Submissions",
            "submission_approved" to "Approved by Editorial Board",
            "submission_pending" to "Awaiting Editor Review",
            "submission_rejected" to "Returned for Revision",
            "feedback" to "Feedback",

            // Podcasts
            "media_hub" to "Republic Student Audio & Media Hub",
            "now_streaming" to "Now streaming active audio...",
            "duration_label" to "Duration",
            "speaker_label" to "Speaker",

            // Counselling & Appointments
            "appointment_scheduler" to "Parent-Teacher Counsel Appointments",
            "schedule_header" to "Request Professional Consultation",
            "student_name" to "Student Name",
            "select_date" to "Preferred Date (YYYY-MM-DD)",
            "select_time" to "Preferred Time (e.g. 3:30 PM)",
            "consult_notes" to "Consultation Focus / Parent Concerns",
            "request_appointment" to "Book Counselling Session",
            "your_appointments" to "Scheduled Consultations",
            "counsellor" to "School Counselor",
            "status_scheduled" to "Scheduled",
            "status_completed" to "Completed",
            "status_cancelled" to "Cancelled",
            "otp_header" to "Encrypted OTP Verification",
            "otp_desc" to "A secure 4-digit code has been sent to your registered mobile number ending with •••• 1098.",
            "otp_label" to "Enter 4-Digit OTP",
            "otp_resend_timer" to "Resend OTP in ",
            "otp_resend_btn" to "Resend OTP",
            "otp_verify_btn" to "Verify & Enter",
            "otp_autofill_btn" to "Auto-fill Demo Code (1234)",
            "cancel" to "Cancel"
        ),
        "Hindi" to mapOf(
            "app_title" to "रिपब्लिक स्टूडेंट वन",
            "lscep" to "जीवन कौशल और नागरिकता शिक्षा कार्यक्रम (LSCEP)",
            "login" to "लॉगिन करें",
            "logout" to "लॉगआउट",
            "select_role" to "अपने पोर्टल का चयन करें",
            "student" to "छात्र",
            "parent" to "अभिभावक",
            "teacher" to "शिक्षक",
            "school" to "स्कूल प्रबंधन",
            "admin" to "रिपब्लिक संपादक",
            "dashboard" to "डैशबोर्ड",
            "lscep_modules" to "जीवन कौशल मॉड्यूल",
            "assessment" to "एआई मनोमितीय मूल्यांकन",
            "newspaper" to "छात्र समाचार पत्र",
            "podcasts" to "पॉडकास्ट और मीडिया",
            "notices" to "स्कूल सूचनाएं",
            "attendance" to "उपस्थिति",
            "counselling" to "परामर्श",
            "reports" to "शैक्षणिक रिपोर्ट",
            "class_level" to "कक्षा",
            "submit" to "जमा करें",
            "approved" to "स्वीकृत",
            "pending" to "लंबित",
            "rejected" to "अस्वीकृत",

            // Login & Portal Selection Screen
            "enter_username" to "उपयोगकर्ता नाम दर्ज करें",
            "login_subtitle" to "नागरिक, वित्तीय और डिजिटल साक्षरता के साथ भारत की अगली पीढ़ी को सशक्त बनाना",
            "role_selection_header" to "सक्रिय पोर्टल का चयन करें",
            "active_user" to "सक्रिय छात्र",
            "not_logged_in" to "पहुंचने के लिए कृपया लॉग इन करें",
            "login_button_text" to "छात्र पोर्टल खोलें",

            // Dashboard
            "welcome_back" to "आपका स्वागत है",
            "daily_attendance" to "दैनिक उपस्थिति ट्रैकर",
            "total_students" to "कुल कक्षा नामांकन",
            "present_today" to "आज उपस्थित",
            "absent_today" to "आज अनुपस्थित",
            "submit_attendance" to "दैनिक उपस्थिति दर्ज करें",
            "view_report_card" to "रिपोर्ट विवरण देखें",
            "overall_progress" to "समग्र कार्यक्रम प्रगति",
            "completed_lessons" to "पूरे किए गए पाठ",
            "average_quiz_score" to "औसत प्रश्नोत्तरी स्कोर",
            "academic_year" to "शैक्षणिक वर्ष 2026",
            "school_notices_board" to "आधिकारिक सूचना पट्ट",

            // LSCEP Screens
            "lesson_materials" to "इंटरैक्टिव पाठ सामग्री",
            "estimated_duration" to "अवधि",
            "min_duration" to "मिनट",
            "take_quiz" to "पाठ अभ्यास प्रश्नोत्तरी लें",
            "quiz_title" to "अवधारणा जांच प्रश्नोत्तरी",
            "correct_answer" to "सही उत्तर!",
            "incorrect_answer" to "गलत, फिर से प्रयास करें!",
            "congratulations" to "बधाई हो!",
            "quiz_completed" to "आपने अवधारणा जांच पूरी कर ली है!",
            "your_score" to "आपका स्कोर",
            "close_button" to "बंद करें",
            "submit_score" to "स्कोर जमा करें",

            // Psychometric Portal
            "assessment_portal" to "छात्र योग्यता और मनोमितीय मार्गदर्शन पोर्टल",
            "assessment_desc" to "यह बहु-आयामी निदान व्यक्तित्व लक्षणों, संज्ञानात्मक योग्यता, सीखने की शैली, मूल्यों, नेतृत्व और डिजिटल व्यवहार का मूल्यांकन करता है। यह माध्यमिक धाराओं (PCM, वाणिज्य, मानविकी) और करियर पथों का सुझाव देता है।",
            "select_mode" to "मूल्यांकन का दायरा चुनें",
            "quick_mode" to "त्वरित मूल्यांकन (9 प्रश्न - 1/श्रेणी)",
            "detailed_mode" to "विस्तृत निदान (18 प्रश्न - 2/श्रेणी)",
            "comp_mode" to "व्यापक लेखापरीक्षा (27 प्रश्न - 3/श्रेणी)",
            "start_test" to "योग्यता मूल्यांकन शुरू करें",
            "question_progress" to "प्रश्न",
            "of" to "का",
            "submitting_assessment" to "एआई परामर्श इंजन को मूल्यांकन प्रस्तुत किया जा रहा है...",
            "report_card_header" to "आधिकारिक मार्गदर्शन रिपोर्ट कार्ड",
            "strengths_header" to "पहचाने गए प्रमुख गुण",
            "learning_style_header" to "प्रमुख सीखने की शैली प्रोफ़ाइल",
            "recommended_stream" to "अनुशंसित माध्यमिक शिक्षा स्ट्रीम",
            "career_interests" to "संगत करियर रुचि समूह",
            "behavioral_insights" to "व्यवहारिक अंतर्दृष्टि और मूल्य भागफल",
            "recommended_skills" to "अनुशंसित कौशल विकास पथ",
            "counselor_notes_header" to "पेशेवर स्कूल परामर्शदाता की सिफारिशें",
            "save_counselor_notes" to "परामर्शदाता मार्गदर्शन विवरण सहेजें",
            "disclaimer" to "आधिकारिक परामर्श सूचना: यह मनोमितीय रिपोर्ट स्व-रिपोर्ट किए गए वैज्ञानिक आयाम संकेतकों के आधार पर रिपब्लिक छात्र प्रकाशन एआई परामर्श इंजन द्वारा गतिशील रूप से संकलित की गई है। यह एक चिकित्सा या मनोवैज्ञानिक निदान नहीं है और केवल एक शैक्षिक पथ संदर्भ के रूप में अभिप्रेत है। शैक्षणिक धाराओं और करियर पथों को शिक्षकों और परामर्शदाताओं के साथ व्यक्तिगत परामर्श के माध्यम से सत्यापित किया जाना चाहिए।",
            "download_report" to "पूर्ण पीडीएफ रिपोर्ट डाउनलोड करें",
            "close_report" to "रिपोर्ट कार्ड बंद करें",
            "historical_eval" to "ऐतिहासिक मूल्यांकन चक्र",
            "view_report_button" to "विश्लेषण देखें",
            "new_test_warning" to "एक नया परीक्षण शुरू करने से एक नया मूल्यांकन चक्र दर्ज किया जाएगा।",

            // Newspaper
            "editorial_features" to "आज की संपादकीय विशेषताएं",
            "student_columns" to "छात्र कॉलम और सहकर्मी नवाचार",
            "submit_column" to "छात्र कॉलम / कविता / कला प्रस्तुत करें",
            "title_label" to "प्रस्तुति का शीर्षक",
            "content_label" to "प्रस्तुति पाठ / सामग्री",
            "category_label" to "श्रेणी चुनें",
            "submit_to_editor" to "रिपब्लिक संपादक को भेजें",
            "pending_submissions" to "आपकी लंबित प्रस्तुतियाँ",
            "submission_approved" to "संपादकीय बोर्ड द्वारा स्वीकृत",
            "submission_pending" to "संपादक की समीक्षा की प्रतीक्षा में",
            "submission_rejected" to "संशोधन के लिए लौटाया गया",
            "feedback" to "प्रतिक्रिया",

            // Podcasts
            "media_hub" to "रिपब्लिक स्टूडेंट ऑडियो और मीडिया हब",
            "now_streaming" to "सक्रिय ऑडियो स्ट्रीम हो रहा है...",
            "duration_label" to "अवधि",
            "speaker_label" to "वक्ता",

            // Counselling & Appointments
            "appointment_scheduler" to "अभिभावक-शिक्षक परामर्श नियुक्तियां",
            "schedule_header" to "पेशेवर परामर्श का अनुरोध करें",
            "student_name" to "छात्र का नाम",
            "select_date" to "पसंदीदा तिथि (YYYY-MM-DD)",
            "select_time" to "पसंदीदा समय (जैसे 3:30 PM)",
            "consult_notes" to "परामर्श फोकस / अभिभावक की चिंताएं",
            "request_appointment" to "परामर्श सत्र बुक करें",
            "your_appointments" to "निर्धारित परामर्श",
            "counsellor" to "स्कूल परामर्शदाता",
            "status_scheduled" to "निर्धारित",
            "status_completed" to "पूरा हुआ",
            "status_cancelled" to "रद्द किया गया",
            "otp_header" to "एन्क्रिप्टेड ओटीपी सत्यापन",
            "otp_desc" to "एक सुरक्षित 4-अंकीय कोड आपके पंजीकृत मोबाइल नंबर पर भेजा गया है जिसके अंत में •••• 1098 है।",
            "otp_label" to "4-अंकीय ओटीपी दर्ज करें",
            "otp_resend_timer" to "ओटीपी पुन: भेजें ",
            "otp_resend_btn" to "ओटीपी पुन: भेजें",
            "otp_verify_btn" to "सत्यापित करें और प्रवेश करें",
            "otp_autofill_btn" to "ऑटो-फिल डेमो कोड (1234)",
            "cancel" to "रद्द करें"
        ),
        "Telugu" to mapOf(
            "app_title" to "రిపబ్లిక్ స్టూడెంట్ వన్",
            "lscep" to "జీవిత నైపుణ్యాలు & పౌరసత్వ విద్యా కార్యక్రమం (LSCEP)",
            "login" to "లాగిన్",
            "logout" to "లాగౌట్",
            "select_role" to "మీ డ్యాష్‌బోర్డ్ పోర్టల్‌ని ఎంచుకోండి",
            "student" to "విద్యార్థి",
            "parent" to "తల్లిదండ్రులు",
            "teacher" to "ఉపాధ్యాయుడు",
            "school" to "పాఠశాల యాజమాన్యం",
            "admin" to "రిపబ్లిక్ సంపాదకుడు",
            "dashboard" to "డ్యాష్‌బోర్డ్",
            "lscep_modules" to "జీవిత నైపుణ్యాల మాడ్యూల్స్",
            "assessment" to "AI సైకోమెట్రిక్ అసెస్‌మెంట్",
            "newspaper" to "స్టూడెంట్ న్యూస్‌పేపర్",
            "podcasts" to "పాడ్‌కాస్ట్‌లు & media",
            "notices" to "పాఠశాల నోటీసులు",
            "attendance" to "హాజరు",
            "counselling" to "కౌన్సెలింగ్",
            "reports" to "విద్యా నివేదికలు",
            "class_level" to "తరగతి",
            "submit" to "సమర్పించు",
            "approved" to "ఆమోదించబడింది",
            "pending" to "పెండింగ్",
            "rejected" to "తిరస్కరించబడింది",

            // Login & Portal Selection Screen
            "enter_username" to "వినియోగదారు పేరును నమోదు చేయండి",
            "login_subtitle" to "పౌర, ఆర్థిక మరియు డిజిటల్ అక్షరాస్యతతో భారతదేశ తదుపరి తరాన్ని శక్తివంతం చేయడం",
            "role_selection_header" to "యాక్టివ్ పోర్టల్‌ని ఎంచుకోండి",
            "active_user" to "క్రియాశీల విద్యార్థి",
            "not_logged_in" to "దయచేసి యాక్సెస్ చేయడానికి లాగిన్ అవ్వండి",
            "login_button_text" to "విద్యార్థి పోర్టల్‌ను తెరవండి",

            // Dashboard
            "welcome_back" to "తిరిగి స్వాగతం",
            "daily_attendance" to "రోజువారీ హాజరు ట్రాకర్",
            "total_students" to "మొత్తం తరగతి నమోదు",
            "present_today" to "ఈ రోజు హాజరయ్యారు",
            "absent_today" to "ఈ రోజు హాజరు కాలేదు",
            "submit_attendance" to "రోజువారీ హాజరును గుర్తించండి",
            "view_report_card" to "నివేదిక వివరాలను వీక్షించండి",
            "overall_progress" to "మొత్తం ప్రోగ్రామ్ పురోగతి",
            "completed_lessons" to "పూర్తయిన పాఠాలు",
            "average_quiz_score" to "సగటు క్విజ్ స్కోరు",
            "academic_year" to "విద్యా సంవత్సరం 2026",
            "school_notices_board" to "అధికారిక నోటీసు బోర్డు",

            // LSCEP Screens
            "lesson_materials" to "ఇంటరాక్టివ్ పాఠ్య సామగ్రి",
            "estimated_duration" to "సమయం",
            "min_duration" to "నిమిషాలు",
            "take_quiz" to "పాఠ్య అభ్యాస క్విజ్ తీసుకోండి",
            "quiz_title" to "భావన తనిఖీ క్విజ్",
            "correct_answer" to "సరైన సమాధానం!",
            "incorrect_answer" to "తప్పు, మళ్లీ ప్రయత్నించండి!",
            "congratulations" to "అభినందనలు!",
            "quiz_completed" to "మీరు భావన తనిఖీని విజయవంతంగా పూర్తి చేసారు!",
            "your_score" to "మీ స్కోరు",
            "close_button" to "మూసివేయి",
            "submit_score" to "స్కోరును సమర్పించు",

            // Psychometric Portal
            "assessment_portal" to "విద్యార్థి ఆప్టిట్యూడ్ & సైకోమెట్రిక్ మార్గదర్శక పోర్టల్",
            "assessment_desc" to "ఈ బహుళ-డైమెన్షనల్ డయాగ్నస్టిక్ వ్యక్తిత్వ లక్షణాలు, అభిజ్ఞా ఆప్టిట్యూడ్, అభ్యాస శైలులు, విలువలు, నాయకత్వం మరియు డిజిటల్ ప్రవర్తనను అంచనా వేస్తుంది. ఇది ఉన్నత విద్యకు సరిపోయే కోర్సులు (PCM, కామర్స్, హ్యుమానిటీస్) మరియు కెరీర్ మార్గాలను సూచిస్తుంది.",
            "select_mode" to "అసెస్మెంట్ పరిధిని ఎంచుకోండి",
            "quick_mode" to "త్వరిత మూల్యాంకనం (9 ప్రశ్నలు - 1/కేటగిరీ)",
            "detailed_mode" to "వివరణాత్మక రోగనిర్ధారణ (18 ప్రశ్నలు - 2/కేటగిరీ)",
            "comp_mode" to "సమగ్ర ఆడిట్ (27 ప్రశ్నలు - 3/కేటగిరీ)",
            "start_test" to "ఆప్టిట్యూడ్ మూల్యాంకనాన్ని ప్రారంభించండి",
            "question_progress" to "ప్రశ్న",
            "of" to "యొక్క",
            "submitting_assessment" to "AI కౌన్సెలింగ్ ఇంజిన్‌కు అసెస్‌మెంట్‌ను సమర్పిస్తోంది...",
            "report_card_header" to "అధికారిక మార్గదర్శక నివేదిక కార్డ్",
            "strengths_header" to "గుర్తించబడిన ముఖ్య బలాలు",
            "learning_style_header" to "ప్రధాన అభ్యాస శైలి ప్రొఫైల్",
            "recommended_stream" to "సిఫార్సు చేయబడిన సెకండరీ ఎడ్యుకేషన్ స్ట్రీమ్",
            "career_interests" to "సరిపోయే కెరీర్ ఆసక్తి సమూహాలు",
            "behavioral_insights" to "ప్రవర్తనా అంతర్దృష్టులు & విలువల గుణకం",
            "recommended_skills" to "సిఫార్సు చేయబడిన నైపుణ్య అభివృద్ధి మార్గాలు",
            "counselor_notes_header" to "ప్రొఫెషనల్ స్కూల్ కౌన్సిలర్ సిఫార్సులు",
            "save_counselor_notes" to "కౌన్సిలర్ మార్గదర్శక వివరాలను సేవ్ చేయండి",
            "disclaimer" to "అధికారిక కౌన్సిల్ నోటీసు: ఈ సైకోమెట్రిక్ నివేదిక స్వయంగా నివేదించబడిన శాస్త్రీయ సూచికల ఆధారంగా రిపబ్లిక్ స్టూడెంట్ పబ్లికేషన్స్ AI కౌన్సెలింగ్ ఇంజిన్ ద్వారా డైనమిక్‌గా సంకలనం చేయబడింది. ఇది వైద్య లేదా మానసిక నిర్ధారణ కాదు మరియు కేవలం విద్యా మార్గ సూచనగా మాత్రమే ఉపయోగపడుతుంది. విద్యా విభాగాలు మరియు కెరీర్ మార్గాలను ఉపాధ్యాయులు మరియు కౌన్సిలర్లతో వ్యక్తిగత సంప్రదింపుల ద్వారా ధృవీకరించుకోవాలి.",
            "download_report" to "పూర్తి PDF నివేదికను డౌన్‌లోడ్ చేయండి",
            "close_report" to "రిపోర్ట్ కార్డ్ మూసివేయి",
            "historical_eval" to "చారిత్రక మూల్యాంకన చక్రాలు",
            "view_report_button" to "విశ్లేషణను వీక్షించండి",
            "new_test_warning" to "కొత్త పరీక్షను ప్రారంభించడం వలన సరికొత్త అసెస్‌మెంట్ రికార్డ్ చేయబడుతుంది.",

            // Newspaper
            "editorial_features" to "ఈ రోజు సంపాదకీయ విశేషాలు",
            "student_columns" to "విద్యార్థి కాలమ్‌లు & తోటివారి ఆవిష్కరణలు",
            "submit_column" to "విద్యార్థి కాలమ్ / కవిత / కళను సమర్పించండి",
            "title_label" to "సమర్పణ శీర్షిక",
            "content_label" to "సమర్పణ వచనం / కంటెంట్",
            "category_label" to "వర్గాన్ని ఎంచుకోండి",
            "submit_to_editor" to "రిపబ్లిక్ ఎడిటర్‌కు సమర్పించండి",
            "pending_submissions" to "మీ పెండింగ్ సమర్పణలు",
            "submission_approved" to "ఎడిటోరియల్ బోర్డ్ ఆమోదించింది",
            "submission_pending" to "ఎడిటర్ సమీక్ష కోసం నిరీక్షణ",
            "submission_rejected" to "సవరణ కోసం తిరిగి పంపబడింది",
            "feedback" to "అభిప్రాయం",

            // Podcasts
            "media_hub" to "రిపబ్లిక్ స్టూడెంట్ ఆడియో & మీడియా హబ్",
            "now_streaming" to "ప్రస్తుతం ఆడియో స్ట్రీమ్ అవుతోంది...",
            "duration_label" to "సమయం",
            "speaker_label" to "స్పీకర్",

            // Counselling & Appointments
            "appointment_scheduler" to "తల్లిదండ్రులు-ఉపాధ్యాయుల కౌన్సెలింగ్ నియామకాలు",
            "schedule_header" to "వృత్తిపరమైన సంప్రదింపుల అభ్యర్థన",
            "student_name" to "విద్యార్థి పేరు",
            "select_date" to "ఇష్టపడే తేదీ (YYYY-MM-DD)",
            "select_time" to "ఇష్టపడే సమయం (ఉదా. 3:30 PM)",
            "consult_notes" to "సంప్రదింపుల అంశం / తల్లిదండ్రుల ఆందోళనలు",
            "request_appointment" to "కౌన్సెలింగ్ సెషన్‌ను బుక్ చేయండి",
            "your_appointments" to "షెడ్యూల్ చేయబడిన సంప్రదింపులు",
            "counsellor" to "పాఠశాల కౌన్సిలర్",
            "status_scheduled" to "షెడ్యూల్ చేయబడింది",
            "status_completed" to "పూర్తయింది",
            "status_cancelled" to "రద్దు చేయబడింది",
            "otp_header" to "ఎన్‌క్రిప్టెడ్ OTP వెరిఫికేషన్",
            "otp_desc" to "•••• 1098తో ముగిసే మీ రిజిస్టర్డ్ మొబైల్ నంబర్‌కు సురక్షితమైన 4-అంకెల కోడ్ పంపబడింది.",
            "otp_label" to "4-అంకెల OTPని నమోదు చేయండి",
            "otp_resend_timer" to "మళ్లీ OTP పంపండి ",
            "otp_resend_btn" to "ఓటీపీ మళ్లీ పంపండి",
            "otp_verify_btn" to "ధృవీకరించండి & ప్రవేశించండి",
            "otp_autofill_btn" to "ఆటో-ఫిల్ డెమో కోడ్ (1234)",
            "cancel" to "రద్దు చేయి"
        )
    )

    private val schoolContextTranslations = mapOf(
        "Hindi" to mapOf(
            "during a science laboratory experiment" to "विज्ञान प्रयोगशाला प्रयोग के दौरान",
            "in a history group study session" to "इतिहास समूह अध्ययन सत्र में",
            "solving a complex mathematics worksheet" to "एक जटिल गणित कार्यपत्रक को हल करते समय",
            "writing a creative English essay" to "एक रचनात्मक अंग्रेजी निबंध लिखते समय",
            "coding in computer class" to "कंप्यूटर कक्षा में कोडिंग करते समय",
            "learning geography maps" to "भूगोल के नक्शे सीखते समय",
            "in an inter-house sports tournament" to "एक इंटर-हाउस खेल प्रतियोगिता में",
            "during a creative art session" to "एक रचनात्मक कला सत्र के दौरान",
            "working on a high-school physics project" to "हाई-स्कूल भौतिकी परियोजना पर काम करते समय",
            "attending a music workshop" to "एक संगीत कार्यशाला में भाग लेते समय",
            "preparing a biology presentation" to "जीव विज्ञान प्रस्तुति तैयार करते समय",
            "writing a civics assignment" to "नागरिक शास्त्र सत्रीय कार्य लिखते समय",
            "organizing a school science exhibition" to "स्कूल विज्ञान प्रदर्शनी का आयोजन करते समय",
            "participating in a poetry recitation contest" to "कविता पाठ प्रतियोगिता में भाग लेते समय",
            "while helping in the school library" to "स्कूल पुस्तकालय में मदद करते समय",
            "during an inter-school debate session" to "एक अंतर-स्कूली वाद-विवाद सत्र के दौरान",
            "planning a classroom cleanliness drive" to "कक्षा स्वच्छता अभियान की योजना बनाते समय",
            "managing the classroom notice board" to "कक्षा के सूचना पट्ट का प्रबंधन करते समय",
            "setting up a recycling corner" to "एक रीसाइक्लिंग कॉर्नर स्थापित करते समय",
            "collecting funds for a charity drive" to "एक धर्मार्थ अभियान के लिए धन एकत्र करते समय",
            "helping a classmate who missed lessons" to "उस सहपाठी की मदद करते समय जिसने पाठ छोड़ दिए थे",
            "studying in the quiet study hall" to "शांत अध्ययन कक्ष में अध्ययन करते समय",
            "running for class monitor" to "कक्षा मॉनिटर के लिए चुनाव लड़ते समय",
            "attending an online career webinar" to "एक ऑनलाइन करियर वेबिनार में भाग लेते समय",
            "planning a weekend cycle ride with friends" to "दोस्तों के साथ सप्ताहांत चक्र यात्रा की योजना बनाते समय",
            "creating a budget for a school picnic" to "स्कूल पिकनिक के लिए बजट बनाते समय",
            "helping family organize a home event" to "परिवार को एक घरेलू कार्यक्रम आयोजित करने में मदद करते समय",
            "choosing a book at a bookstore" to "किताबों की दुकान पर किताब चुनते समय",
            "learning a new technical skill online" to "ऑनलाइन एक नया तकनीकी कौशल सीखते समय",
            "preparing for a competitive exam" to "एक प्रतियोगी परीक्षा की तैयारी करते समय",
            "doing research on environmental topics" to "पर्यावरण विषयों पर शोध करते समय",
            "assisting an elderly neighbor with tasks" to "एक बुजुर्ग पड़ोसी के कार्यों में सहायता करते समय"
        ),
        "Telugu" to mapOf(
            "during a science laboratory experiment" to "సైన్స్ లాబొరేటరీ ప్రయోగం సమయంలో",
            "in a history group study session" to "ఇతిహాస గ్రూప్ స్టడీ సెషన్‌లో",
            "solving a complex mathematics worksheet" to "ఒక క్లిష్టమైన గణిత వర్క్‌షీట్‌ను పరిష్కరిస్తున్నప్పుడు",
            "writing a creative English essay" to "సృజనాత్మక ఇంగ్లీష్ వ్యాసం రాస్తున్నప్పుడు",
            "coding in computer class" to "కంప్యూటర్ క్లాస్‌లో కోడింగ్ చేస్తున్నప్పుడు",
            "learning geography maps" to "భౌగోళిక పటాలను నేర్చుకుంటున్నప్పుడు",
            "in an inter-house sports tournament" to "ఇంటర్-హౌస్ క్రీడా టోర్నమెంట్‌లో",
            "during a creative art session" to "సృజనాత్మక కళల సెషన్ సమయంలో",
            "working on a high-school physics project" to "హైస్కూల్ ఫిజిక్స్ ప్రాజెక్ట్‌పై పని చేస్తున్నప్పుడు",
            "attending a music workshop" to "సంగీత వర్క్‌షాప్‌కు హాజరవుతున్నప్పుడు",
            "preparing a biology presentation" to "జీవశాస్త్ర ప్రెజెండేషన్‌ను సిద్ధం చేస్తున్నప్పుడు",
            "writing a civics assignment" to "సివిక్స్ అసైన్‌మెంట్ రాస్తున్నప్పుడు",
            "organizing a school science exhibition" to "పాఠశాల సైన్స్ ఎగ్జిబిషన్‌ను నిర్వహిస్తున్నప్పుడు",
            "participating in a poetry recitation contest" to "కవితల పఠన పోటీలో పాల్గొంటున్నప్పుడు",
            "while helping in the school library" to "పాఠశాల లైబ్రరీలో సహాయం చేస్తున్నప్పుడు",
            "during an inter-school debate session" to "అంతర పాఠశాల డిబేట్ సెషన్ సమయంలో",
            "planning a classroom cleanliness drive" to "తరగతి గది పరిశుభ్రత డ్రైవ్‌ను ప్లాన్ చేస్తున్నప్పుడు",
            "managing the classroom notice board" to "తరగతి గది నోటీసు బోర్డును నిర్వహిస్తున్నప్పుడు",
            "setting up a recycling corner" to "రీసైక్లింగ్ కార్నర్‌ను ఏర్పాటు చేస్తున్నప్పుడు",
            "collecting funds for a charity drive" to "ఛారిటీ డ్రైవ్ కోసం నిధులు సేకరిస్తున్నప్పుడు",
            "helping a classmate who missed lessons" to "పాఠాలు కోల్పోయిన తోటి విద్యార్థికి సహాయం చేస్తున్నప్పుడు",
            "studying in the quiet study hall" to "ప్రశాంతమైన స్టడీ హాల్‌లో చదువుతున్నప్పుడు",
            "running for class monitor" to "తరగతి మానిటర్ కోసం పోటీ పడుతున్నప్పుడు",
            "attending an online career webinar" to "ఆన్‌లైన్ కెరీర్ వెబ్‌నార్‌కు హాజరవుతున్నప్పుడు",
            "planning a weekend cycle ride with friends" to "స్నేహితులతో వీకెండ్ సైకిల్ రైడ్‌ను ప్లాన్ చేస్తున్నప్పుడు",
            "creating a budget for a school picnic" to "పాఠశాల విహారయాత్ర కోసం బడ్జెట్‌ను తయారు చేస్తున్నప్పుడు",
            "helping family organize a home event" to "ఇంటి కార్యక్రమాన్ని నిర్వహించడంలో కుటుంబానికి సహాయం చేస్తున్నప్పుడు",
            "choosing a book at a bookstore" to "పుస్తకాల దుకాణంలో పుస్తకాన్ని ఎంచుకుంటున్నప్పుడు",
            "learning a new technical skill online" to "ఆన్‌లైన్‌లో కొత్త సాంకేతిక నైపుణ్యాన్ని నేర్చుకుంటున్నప్పుడు",
            "preparing for a competitive exam" to "పోటీ పరీక్షకు సిద్ధమవుతున్నప్పుడు",
            "doing research on environmental topics" to "పర్యావరణ అంశాలపై పరిశోధన చేస్తున్నప్పుడు",
            "assisting an elderly neighbor with tasks" to "వృద్ధుడైన పొరుగువారికి పనులలో సహాయం చేస్తున్నప్పుడు"
        )
    )

    private val categoryTranslations = mapOf(
        "Hindi" to mapOf(
            "Personality & Behaviour" to "व्यक्तित्व और व्यवहार",
            "Emotional Intelligence" to "भावनात्मक बुद्धिमत्ता",
            "Responsibility & Values" to "जिम्मेदारी और मूल्य",
            "Learning Style" to "सीखने की शैली",
            "Aptitude & Logical Thinking" to "योग्यता और तार्किक सोच",
            "Leadership & Communication" to "नेतृत्व और संचार",
            "Career Interests" to "करियर रुचियां",
            "Entrepreneurship & Innovation" to "उद्यमिता और नवाचार",
            "Digital Behaviour & Ethics" to "डिजिटल व्यवहार और नैतिकता"
        ),
        "Telugu" to mapOf(
            "Personality & Behaviour" to "వ్యక్తిత్వం & ప్రవర్తన",
            "Emotional Intelligence" to "భావోద్వేగ మేధస్సు",
            "Responsibility & Values" to "బాధ్యత & విలువలు",
            "Learning Style" to "నేర్చుకునే శైలి",
            "Aptitude & Logical Thinking" to "ఆప్టిట్యూడ్ & లాజికల్ థింకింగ్",
            "Leadership & Communication" to "నాయకత్వం & కమ్యూనికేషన్",
            "Career Interests" to "కెరీర్ ఆసక్తులు",
            "Entrepreneurship & Innovation" to "ఆంట్రప్రెన్యూర్‌షిప్ & ఇన్నోవేషన్",
            "Digital Behaviour & Ethics" to "డిజిటల్ ప్రవర్తన & నీతి"
        )
    )

    private val dimensionsTranslations = mapOf(
        "Hindi" to mapOf(
            "Extroverted Leadership" to "बहिर्मुखी नेतृत्व",
            "Introverted Structured Planning" to "अंतर्मुखी व्यवस्थित योजना",
            "Adaptive Supportive Behavior" to "अनुकूलनशील सहायक व्यवहार",
            "Spontaneous Creative Action" to "सहज रचनात्मक कार्रवाई",
            "High Self-Awareness & Empathy" to "उच्च आत्म-जागरूकता और सहानुभूति",
            "Cognitive Problem-Solving Focus" to "संज्ञानात्मक समस्या-समाधान फोकस",
            "Emotional Regulation & Control" to "भावनात्मक नियमन और नियंत्रण",
            "High Reactivity (Needs Support)" to "उच्च प्रतिक्रियाशीलता (समर्थन की आवश्यकता)",
            "High Civic Duty & Proactive Integrity" to "उच्च नागरिक कर्तव्य और सक्रिय सत्यनिष्ठा",
            "Collaborative Responsibility & Values" to "सहयोगात्मक जिम्मेदारी और मूल्य",
            "Standard Rule-Compliance" to "मानक नियम-अनुपालन",
            "Passive Individualism" to "निष्क्रिय व्यक्तिवाद",
            "Visual Learner (Spatial)" to "दृश्य शिक्षार्थी (स्थानिक)",
            "Auditory Learner (Verbal)" to "श्रवण शिक्षार्थी (मौखिक)",
            "Read/Write Learner (Textual)" to "पठन/लेखन शिक्षार्थी (पाठ्य)",
            "Kinesthetic Learner (Practical)" to "गतिबोधक शिक्षार्थी (व्यावहारिक)",
            "High Deductive Logic & Analytics" to "उच्च निगमनात्मक तर्क और विश्लेषण",
            "Pattern Recognition & Reference" to "पैटर्न पहचान और संदर्भ",
            "Experimental & Intuitive Logic" to "प्रायोगिक और सहज ज्ञान युक्त तर्क",
            "Collaborative Logic Guidance" to "सहयोगात्मक तर्क मार्गदर्शन",
            "Diplomatic & Collaborative Leadership" to "राजनयिक और सहयोगात्मक नेतृत्व",
            "Transactional Task-Management" to "लेन-देन संबंधी कार्य-प्रबंधन",
            "Charismatic & Visionary Communication" to "करिश्माई और दूरदर्शी संचार",
            "Supportive & Pacesetting Leadership" to "सहायक और गति-निर्धारण नेतृत्व",
            "Investigative (Science, Tech & Research)" to "खोजी (विज्ञान, तकनीक और अनुसंधान)",
            "Artistic (Creative Arts & Media)" to "कलात्मक (रचनात्मक कला और मीडिया)",
            "Social (Education, Counseling & Welfare)" to "सामाजिक (शिक्षा, परामर्श और कल्याण)",
            "Enterprising (Business, Venture & Finance)" to "उद्यमी (व्यवसाय, उद्यम और वित्त)",
            "High Technological Innovation" to "उच्च तकनीकी नवाचार",
            "Strategic Business & Financial Innovation" to "रणनीतिक व्यवसाय और वित्तीय नवाचार",
            "High Risk-Tolerance & Creativity" to "उच्च जोखिम-सहनशीलता और रचनात्मकता",
            "Pragmatic & Incremental Innovation" to "व्यावहारिक और वृद्धिशील नवाचार",
            "High Digital Ethics & Empathy" to "उच्च डिजिटल नैतिकता और सहानुभूति",
            "High Security & Footprint Awareness" to "उच्च सुरक्षा और पदचिह्न जागरूकता",
            "Pragmatic Cyber-Defense Action" to "व्यावहारिक साइबर-सुरक्षा कार्रवाई",
            "Dependent & Consultative Digital Safety" to "निर्भर और संवादात्मक डिजिटल सुरक्षा"
        ),
        "Telugu" to mapOf(
            "Extroverted Leadership" to "బహిర్ముఖ నాయకత్వం",
            "Introverted Structured Planning" to "అంతర్ముఖ క్రమబద్ధమైన ప్రణాళిక",
            "Adaptive Supportive Behavior" to "అనుకూల సహాయక ప్రవర్తన",
            "Spontaneous Creative Action" to "స్వయంసిద్ధ సృజనాత్మక చర్య",
            "High Self-Awareness & Empathy" to "అధిక స్వీయ-అవగాహన & సానుభూతి",
            "Cognitive Problem-Solving Focus" to "అభిజ్ఞా సమస్య-పరిష్కార దృష్టి",
            "Emotional Regulation & Control" to "భావోద్వేగ నియంత్రణ",
            "High Reactivity (Needs Support)" to "అధిక ప్రతిచర్య (మద్దతు అవసరం)",
            "High Civic Duty & Proactive Integrity" to "అధిక పౌర బాధ్యత & సమగ్రత",
            "Collaborative Responsibility & Values" to "సహకార బాధ్యత & విలువలు",
            "Standard Rule-Compliance" to "నియమ నిబంధనల పాటించడం",
            "Passive Individualism" to "నిష్క్రియ వ్యక్తిగతత",
            "Visual Learner (Spatial)" to "దృశ్య అభ్యాసకుడు (ప్రాంతీయ)",
            "Auditory Learner (Verbal)" to "శ్రవణ అభ్యాసకుడు (మౌఖిక)",
            "Read/Write Learner (Textual)" to "పఠన/లేఖన అభ్యాసకుడు (టెక్స్ట్)",
            "Kinesthetic Learner (Practical)" to "స్పర్శ జ్ఞాన అభ్యాసకుడు (ప్రాక్టికల్)",
            "High Deductive Logic & Analytics" to "అధిక డిడక్టివ్ లాజిక్ & అనలిటిక్స్",
            "Pattern Recognition & Reference" to "ప్యాటర్న్ గుర్తింపు & సూచన",
            "Experimental & Intuitive Logic" to "ప్రయోగాత్మక & సహజమైన లాజిక్",
            "Collaborative Logic Guidance" to "సహకార లాజిక్ మార్గదర్శకత్వం",
            "Diplomatic & Collaborative Leadership" to "దౌత్యపరమైన & సహకార నాయకత్వం",
            "Transactional Task-Management" to "లావాదేవీల టాస్క్ మేనేజ్‌మెంట్",
            "Charismatic & Visionary Communication" to "ఆకర్షణీయమైన & దూరదృష్టి కమ్యూనికేషన్",
            "Supportive & Pacesetting Leadership" to "సహాయక & గతినిర్దేశక నాయకత్వం",
            "Investigative (Science, Tech & Research)" to "పరిశోధనాత్మక (సైన్స్, టెక్ & రీసెర్చ్)",
            "Artistic (Creative Arts & Media)" to "కళాత్మక (సృజనాత్మక కళలు & మీడియా)",
            "Social (Education, Counseling & Welfare)" to "సామాజిక (విద్య, కౌన్సెలింగ్ & సంక్షేమం)",
            "Enterprising (Business, Venture & Finance)" to "పారిశ్రామిక (వ్యాపారం, వెంచర్ & ఫైనాన్స్)",
            "High Technological Innovation" to "అధిక సాంకేతిక ఆవిష్కరణలు",
            "Strategic Business & Financial Innovation" to "వ్యూహాత్మక వ్యాపార & ఆర్థిక ఆవిష్కరణ",
            "High Risk-Tolerance & Creativity" to "అధిక ప్రమాద సహనం & సృజనాత్మకత",
            "Pragmatic & Incremental Innovation" to "ఆచరణాత్మక & క్రమమైన ఆవిష్కరణ",
            "High Digital Ethics & Empathy" to "అధిక డిజిటల్ నీతి & సానుభూతి",
            "High Security & Footprint Awareness" to "అధిక భద్రత & ఫుట్‌ప్రింట్ అవగాహన",
            "Pragmatic Cyber-Defense Action" to "ఆచరణాత్మక సైబర్ రక్షణ చర్య",
            "Dependent & Consultative Digital Safety" to "ఆధారిత & సంప్రదింపుల డిజిటల్ భద్రత"
        )
    )

    private val questionTemplatesTranslations = mapOf(
        "Hindi" to mapOf(
            "When starting a new task, how do you prefer to coordinate?" to "जब एक नया कार्य शुरू करते हैं, तो आप कैसे समन्वय करना पसंद करते हैं?",
            "If a new unexpected requirement arises, how do you adjust your pace?" to "यदि कोई नई अप्रत्याशित आवश्यकता उत्पन्न होती है, तो आप अपनी गति को कैसे समायोजित करते हैं?",
            "What environment helps you focus best?" to "कौन सा वातावरण आपको सबसे अच्छा ध्यान केंद्रित करने में मदद करता है?",
            "How do you share feedback with classmates?" to "सहपाठियों के साथ आप अपनी प्रतिक्रिया कैसे साझा करते हैं?",
            "When choosing roles, which do you naturally lean towards?" to "भूमिकाओं का चयन करते समय, आप स्वाभाविक रूप से किस ओर झुकाव रखते हैं?",
            "If you finish your primary task early, what is your next action?" to "यदि आप अपना प्राथमिक कार्य जल्दी पूरा कर लेते हैं, तो आपका अगला कदम क्या होगा?",
            
            "How do you recognize when a peer is feeling stressed or left out?" to "आप कैसे पहचानते हैं कि कोई सहपाठी तनाव महसूस कर रहा है या अलग-थलग महसूस कर रहा है?",
            "If you receive critical constructive feedback on your work, how do you process it?" to "यदि आपको अपने काम पर आलोचनात्मक रचनात्मक प्रतिक्रिया मिलती है, तो आप इसे कैसे संसाधित करते हैं?",
            "When an argument begins between group partners, how do you handle your feelings?" to "जब समूह भागीदारों के बीच कोई बहस शुरू होती है, तो आप अपनी भावनाओं को कैसे संभालते हैं?",
            "If you make a frustrating mistake, what is your immediate response?" to "यदि आप कोई निराशाजनक गलती करते हैं, तो आपकी तत्काल प्रतिक्रिया क्या होती है?",
            "How do you help build a positive, encouraging team atmosphere?" to "आप एक सकारात्मक, उत्साहजनक टीम माहौल बनाने में कैसे मदद करते हैं?",
            "When you feel extremely tired or overwhelmed, how do you restore your focus?" to "जब आप अत्यधिक थका हुआ या अभिभूत महसूस करते हैं, तो आप अपना ध्यान कैसे वापस लाते हैं?",
            
            "If you spot a safety issue or violation of rules, what is your reaction?" to "यदि आप कोई सुरक्षा समस्या या नियमों का उल्लंघन देखते हैं, तो आपकी क्या प्रतिक्रिया होती है?",
            "When you commit to a deadline but face an unexpected delay, how do you act?" to "जब आप किसी समय-सीमा के लिए प्रतिबद्ध होते हैं लेकिन अप्रत्याशित देरी का सामना करते हैं, तो आप कैसे कार्य करते हैं?",
            "What does 'fairness and inclusion' mean to you?" to "आपके लिए 'निष्पक्षता और समावेश' का क्या अर्थ है?",
            "If you notice public or school property being handled carelessly, what do you do?" to "यदि आप देखते हैं कि सार्वजनिक या स्कूल की संपत्ति को लापरवाही से संभाला जा रहा है, तो आप क्या करते हैं?",
            "How do you ensure honesty and integrity are maintained?" to "आप कैसे सुनिश्चित करते हैं कि ईमानदारी और सत्यनिष्ठा बनी रहे?",
            "When representing your school or team, what value do you prioritize most?" to "अपने स्कूल या टीम का प्रतिनिधित्व करते समय, आप किस मूल्य को सबसे अधिक प्राथमिकता देते हैं?",
            
            "When studying a highly complex scientific or logical process, what tool helps you most?" to "जब एक अत्यधिक जटिल वैज्ञानिक या तार्किक प्रक्रिया का अध्ययन कर रहे हों, तो कौन सा उपकरण आपकी सबसे अधिक मदद करता है?",
            "In what type of educational classroom setup are you most active and engaged?" to "आप किस प्रकार के शैक्षिक कक्षा सेटअप में सबसे अधिक सक्रिय और व्यस्त रहते हैं?",
            "If you need to teach a difficult concept to a junior student, how do you explain it?" to "यदि आपको किसी कनिष्ठ छात्र को कोई कठिन अवधारणा सिखानी हो, तो आप उसे कैसे समझाएँगे?",
            "What is your go-to method for reviewing notes and preparing for exams?" to "नोट्स की समीक्षा करने और परीक्षाओं की तैयारी करने का आपका पसंदीदा तरीका क्या है?",
            "When exploring a brand new hobby or elective topic, how do you start?" to "जब किसी बिल्कुल नए शौक या वैकल्पिक विषय की खोज कर रहे हों, तो आप कैसे शुरुआत करते हैं?",
            "Which form of resource material makes you feel most confident?" to "संसाधन सामग्री का कौन सा रूप आपको सबसे अधिक आत्मविश्वासी महसूस कराता है?",
            
            "How do you approach a complex problem with multiple variables?" to "आप कई चरों वाली एक जटिल समस्या को कैसे हल करते हैं?",
            "If a logical pattern or math equation doesn't add up, what is your methodology?" to "यदि कोई तार्किक पैटर्न या गणितीय समीकरण सही नहीं बैठता है, तो आपकी कार्यप्रणाली क्या होती है?",
            "When assembling or analyzing structural details, how do you proceed?" to "संरचनात्मक विवरणों को जोड़ते या उनका विश्लेषण करते समय, आप कैसे आगे बढ़ते हैं?",
            "How do you evaluate arguments and evidence?" to "आप तर्कों और साक्ष्यों का मूल्यांकन कैसे करते हैं?",
            "When faced with riddles, code, or pattern sequences, what is your mindset?" to "जब पहेलियों, कोड या पैटर्न अनुक्रमों का सामना करना पड़ता है, तो आपकी मानसिकता क्या होती है?",
            "If you need to make a fast prediction based on incomplete data, how do you do it?" to "यदि आपको अपूर्ण डेटा के आधार पर त्वरित भविष्यवाणी करनी हो, तो आप इसे कैसे करते हैं?",
            
            "How do you guide a group of students to complete a collaborative task?" to "सहयोगात्मक कार्य को पूरा करने के लिए आप छात्रों के एक समूह का मार्गदर्शन कैसे करते हैं?",
            "When presenting a major topic to a large class audience, what is your primary focus?" to "एक बड़े वर्ग के दर्शकों के सामने एक प्रमुख विषय प्रस्तुत करते समय, आपका प्राथमिक ध्यान किस पर होता है?",
            "How do you handle team disagreements or conflicting priorities?" to "आप टीम के मतभेदों या परस्पर विरोधी प्राथमिकताओं को कैसे संभालते हैं?",
            "When delegating project responsibilities, what strategy do you apply?" to "परियोजना की जिम्मेदारियां सौंपते समय, आप कौन सी रणनीति लागू करते हैं?",
            "How do you motivate a classmate who seems disengaged?" to "आप उस सहपाठी को कैसे प्रेरित करते हैं जो विमुख या थका हुआ लगता है?",
            "In what way do you deliver constructive feedback to group members?" to "आप समूह के सदस्यों को रचनात्मक प्रतिक्रिया किस तरह से देते हैं?",
            
            "Which of the following professional projects would you find most fulfilling?" to "निम्नलिखित में से कौन सी व्यावसायिक परियोजना आपको सबसे अधिक संतोषजनक लगेगी?",
            "If you could shadow a highly accomplished leader for a day, who would you choose?" to "यदि आप एक दिन के लिए किसी अत्यधिक निपुण नेता के साथ काम सीख सकें, तो आप किसे चुनेंगे?",
            "When exploring future industries, which domain excites you most?" to "भविष्य के उद्योगों की खोज करते समय, कौन सा क्षेत्र आपको सबसे अधिक उत्साहित करता है?",
            "What kind of problem do you hope to solve in your future career?" to "आप अपने भविष्य के करियर में किस तरह की समस्या को हल करने की आशा करते हैं?",
            "Which university major or practical vocational stream appeals to you?" to "कौन सा विश्वविद्यालय प्रमुख या व्यावहारिक व्यावसायिक क्षेत्र आपको आकर्षित करता है?",
            "How would you prefer to contribute to a major societal challenge?" to "आप किसी बड़ी सामाजिक चुनौती में किस प्रकार योगदान देना पसंद करेंगे?",
            
            "When you discover an inefficient process, how do you respond?" to "जब आप किसी अक्षम प्रक्रिया की खोज करते हैं, तो आप क्या प्रतिक्रिया देते हैं?",
            "How do you feel when faced with a task that has zero templates or instructions?" to "जब आपके सामने कोई ऐसा कार्य आता है जिसमें कोई निर्देश नहीं होते, तो आप कैसा महसूस करते हैं?",
            "If you were to organize a student-led micro-business or fundraiser, what is your focus?" to "यदि आप छात्रों के नेतृत्व में एक सूक्ष्म-व्यवसाय या धन उगाहने का आयोजन करते हैं, तो आपका ध्यान किस पर होता है?",
            "When a creative idea you suggested fails or is rejected, how do you iterate?" to "जब आपके द्वारा सुझाया गया कोई रचनात्मक विचार विफल हो जाता है या अस्वीकार कर दिया जाता है, तो आप कैसे सुधार करते हैं?",
            "What is your attitude toward taking calculated risks?" to "परिकलित जोखिम लेने के प्रति आपका क्या दृष्टिकोण है?",
            "How do you define a successful innovation?" to "आप एक सफल नवाचार को कैसे परिभाषित करते हैं?",
            
            "You notice a malicious cyber-bullying post on a public forum. What do you do?" to "आप एक सार्वजनिक मंच पर एक दुर्भावनापूर्ण साइबर-धमकी वाली पोस्ट देखते हैं। आप क्या करते हैं?",
            "When sharing files, photos, or comments online, what is your top consideration?" to "ऑनलाइन फाइलें, फोटो या टिप्पणियां साझा करते समय आपकी शीर्ष प्राथमिकता क्या होती है?",
            "If an unfamiliar portal asks for private personal data to download a guide, how do you react?" to "यदि कोई अपरिचित पोर्टल गाइड डाउनलोड करने के लिए निजी व्यक्तिगत डेटा मांगता है, तो आप कैसी प्रतिक्रिया देते हैं?",
            "How do you verify the credibility of research information found online?" to "आप ऑनलाइन मिली शोध जानकारी की विश्वसनीयता कैसे सत्यापित करते हैं?",
            "What is your approach to maintaining a healthy screen-time balance?" to "स्वस्थ स्क्रीन-टाइम संतुलन बनाए रखने के प्रति आपका क्या दृष्टिकोण है?",
            "When collaborating on shared online documents, how do you practice digital respect?" to "साझा ऑनलाइन दस्तावेजों पर सहयोग करते समय, आप डिजिटल सम्मान का अभ्यास कैसे करते हैं?"
        ),
        "Telugu" to mapOf(
            "When starting a new task, how do you prefer to coordinate?" to "కొత్త పనిని ప్రారంభించినప్పుడు, మీరు ఎలా సమన్వయం చేసుకోవడానికి ఇష్టపడతారు?",
            "If a new unexpected requirement arises, how do you adjust your pace?" to "కొత్త ఊహించని అవసరం తలెత్తితే, మీరు మీ వేగాన్ని ఎలా సర్దుబాటు చేసుకుంటారు?",
            "What environment helps you focus best?" to "ఏ వాతావరణం మీ దృష్టిని ఉత్తమంగా కేంద్రీకరించడానికి సహాయపడుతుంది?",
            "How do you share feedback with classmates?" to "తోటి విద్యార్థులతో అభిప్రాయాన్ని మీరు ఎలా పంచుకుంటారు?",
            "When choosing roles, which do you naturally lean towards?" to "పాత్రలను ఎంచుకునేటప్పుడు, మీరు సహజంగా దేని వైపు మొగ్గు చూపుతారు?",
            "If you finish your primary task early, what is your next action?" to "మీరు మీ ప్రాథమిక పనిని త్వరగా పూర్తి చేస్తే, మీ తదుపరి చర్య ఏమిటి?",
            
            "How do you recognize when a peer is feeling stressed or left out?" to "ఒక తోటి విద్యార్థి ఒత్తిడికి గురవుతున్నట్లు లేదా నిర్లక్ష్యం చేయబడినట్లు మీరు ఎలా గుర్తిస్తారు?",
            "If you receive critical constructive feedback on your work, how do you process it?" to "మీ పనిపై విమర్శనాత్మక నిర్మాణాత్మక అభిప్రాయాన్ని పొందితే, మీరు దానిని ఎలా స్వీకరిస్తారు?",
            "When an argument begins between group partners, how do you handle your feelings?" to "సమూహ భాగస్వాముల మధ్య వివాదం ప్రారంభమైనప్పుడు, మీరు మీ భావాలను ఎలా నియంత్రిస్తారు?",
            "If you make a frustrating mistake, what is your immediate response?" to "మీరు ఒక నిరాశపరిచే తప్పు చేస్తే, మీ తక్షణ ప్రతిస్పందన ఏమిటి?",
            "How do you help build a positive, encouraging team atmosphere?" to "సానుకూలమైన, ప్రోత్సాహకరమైన బృంద వాతావరణాన్ని నిర్మించడంలో మీరు ఎలా సహాయపడతారు?",
            "When you feel extremely tired or overwhelmed, how do you restore your focus?" to "మీరు తీవ్ర అలసటగా లేదా ఆందోళనగా భావించినప్పుడు, మీ దృష్టిని ఎలా పునరుద్ధరించుకుంటారు?",
            
            "If you spot a safety issue or violation of rules, what is your reaction?" to "మీరు ఒక భద్రతా సమస్యను లేదా నిబంధనల ఉల్లంఘనను గుర్తిస్తే, మీ ప్రతిచర్య ఏమిటి?",
            "When you commit to a deadline but face an unexpected delay, how do you act?" to "మీరు గడువుకు కట్టుబడి ఉన్నప్పుడు ఊహించని ఆలస్యం ఎదురైతే, మీరు ఎలా ప్రవర్తిస్తారు?",
            "What does 'fairness and inclusion' mean to you?" to "మీ దృష్టిలో 'న్యాయం మరియు అందరినీ కలుపుకుపోవడం' అంటే ఏమిటి?",
            "If you notice public or school property being handled carelessly, what do you do?" to "ప్రభుత్వ లేదా పాఠశాల ఆస్తిని నిర్లక్ష్యంగా నిర్వహించడాన్ని గమనిస్తే, మీరు ఏమి చేస్తారు?",
            "How do you ensure honesty and integrity are maintained?" to "నిజాయితీ మరియు సమగ్రతను కాపాడుకునేలా మీరు ఎలా చూసుకుంటారు?",
            "When representing your school or team, what value do you prioritize most?" to "మీ పాఠశాల లేదా బృందానికి ప్రాతినిధ్యం వహిస్తున్నప్పుడు, మీరు ఏ విలువకు అత్యధిక ప్రాధాన్యత ఇస్తారు?",
            
            "When studying a highly complex scientific or logical process, what tool helps you most?" to "ఒక క్లిష్టమైన శాస్త్రీయ లేదా తార్కిక ప్రక్రియను చదువుతున్నప్పుడు, ఏ సాధనం మీకు బాగా సహాయపడుతుంది?",
            "In what type of educational classroom setup are you most active and engaged?" to "మీరు ఏ రకమైన తరగతి గది అమరికలో అత్యంత చురుకుగా మరియు నిమగ్నమై ఉంటారు?",
            "If you need to teach a difficult concept to a junior student, how do you explain it?" to "ఒక జూనియర్ విద్యార్థికి కష్టమైన భావనను నేర్పించవలసి వస్తే, మీరు దానిని ఎలా వివరిస్తారు?",
            "What is your go-to method for reviewing notes and preparing for exams?" to "నోట్స్ సమీక్షించడానికి మరియు పరీక్షలకు సిద్ధం కావడానికి మీ ప్రధాన పద్ధతి ఏమిటి?",
            "When exploring a brand new hobby or elective topic, how do you start?" to "సరికొత్త అభిరుచి లేదా ఐచ్ఛిక అంశాన్ని అన్వేషించేటప్పుడు, మీరు ఎలా ప్రారంభిస్తారు?",
            "Which form of resource material makes you feel most confident?" to "ఏ రకమైన వనరుల మెటీరియల్ మీకు అత్యంత ఆత్మవిశ్వాసాన్ని ఇస్తుంది?",
            
            "How do you approach a complex problem with multiple variables?" to "బహుళ వేరియబుల్స్ ఉన్న సంక్లిష్ట సమస్యను మీరు ఎలా పరిష్కరిస్తారు?",
            "If a logical pattern or math equation doesn't add up, what is your methodology?" to "ఒక తార్కిక నమూనా లేదా గణిత సమీకరణం సరిపోలకపోతే, మీ పద్ధతి ఏమిటి?",
            "When assembling or analyzing structural details, how do you proceed?" to "నిర్మాణాత్మక వివరాలను సమీకరించేటప్పుడు లేదా విశ్లేషించేటప్పుడు, మీరు ఎలా ముందుకు వెళ్తారు?",
            "How do you evaluate arguments and evidence?" to "మీరు వాదనలు మరియు ఆధారాలను ఎలా అంచనా వేస్తారు?",
            "When faced with riddles, code, or pattern sequences, what is your mindset?" to "పొడుపుకథలు, కోడ్ లేదా ప్యాటర్న్ సీక్వెన్స్‌లను ఎదుర్కొన్నప్పుడు, మీ మనస్తత్వం ఎలా ఉంటుంది?",
            "If you need to make a fast prediction based on incomplete data, how do you do it?" to "అసంపూర్ణ డేటా ఆధారంగా మీరు వేగవంతమైన అంచనా వేయవలసి వస్తే, ఎలా చేస్తారు?",
            
            "How do you guide a group of students to complete a collaborative task?" to "సహకార పనిని పూర్తి చేయడానికి మీరు విద్యార్థుల బృందానికి ఎలా మార్గదర్శకత్వం వహిస్తారు?",
            "When presenting a major topic to a large class audience, what is your primary focus?" to "ఒక పెద్ద తరగతి ప్రేక్షకుల ముందు ఒక ముఖ్యమైన అంశాన్ని ప్రదర్శించేటప్పుడు, మీ ప్రాథమిక దృష్టి దేనిపై ఉంటుంది?",
            "How do you handle team disagreements or conflicting priorities?" to "బృందం విభేదాలు లేదా పరస్పర విరుద్ధమైన ప్రాధాన్యతలను మీరు ఎలా నిర్వహిస్తారు?",
            "When delegating project responsibilities, what strategy do you apply?" to "ప్రాజెక్ట్ బాధ్యతలను అప్పగించేటప్పుడు, మీరు ఏ వ్యూహాన్ని వర్తింపజేస్తారు?",
            "How do you motivate a classmate who seems disengaged?" to "ఆసక్తి లేని లేదా అలసిపోయినట్లు కనిపించే తోటి విద్యార్థిని మీరు ఎలా ప్రేరేపిస్తారు?",
            "In what way do you deliver constructive feedback to group members?" to "బృంద సభ్యులకు మీరు ఏ విధంగా నిర్మాణాత్మక అభిప్రायాన్ని అందజేస్తారు?",
            
            "Which of the following professional projects would you find most fulfilling?" to "కింది ప్రొఫెషనల్ ప్రాజెక్ట్‌లలో మీకు అత్యంత సంతృప్తినిచ్చేది ఏది?",
            "If you could shadow a highly accomplished leader for a day, who would you choose?" to "మీరు ఒక రోజు అత్యంత ప్రతిభావంతుడైన నాయకుడిని పరిశీలించగలిగితే, మీరు ఎవరిని ఎంచుకుంటారు?",
            "When exploring future industries, which domain excites you most?" to "భవిష్యత్ పరిశ్రమలను అన్వేషించేటప్పుడు, ఏ రంగం మిమ్మల్ని ఎక్కువగా ఉత్తేజపరుస్తుంది?",
            "What kind of problem do you hope to solve in your future career?" to "మీ భవిष्यత్ కెరీర్‌లో మీరు ఎలాంటి సమస్యను పరిష్కరించాలని ఆశిస్తున్నారు?",
            "Which university major or practical vocational stream appeals to you?" to "ఏ విశ్వవిద్యాలయ మేజర్ లేదా ప్రాక్టికల్ ఒకేషనల్ స్ట్రీమ్ మిమ్మల్ని ఆకర్షిస్తుంది?",
            "How would you prefer to contribute to a major societal challenge?" to "ఒక ప్రధాన సామాజిక సవాలుకు మీరు ఏ విధంగా సహకారం అందించడానికి ఇష్టపడతారు?",
            
            "When you discover an inefficient process, how do you respond?" to "అసమర్థమైన ప్రక్రియను మీరు కనుగొన్నప్పుడు, మీ ప్రతిస్పందన ఏమిటి?",
            "How do you feel when faced with a task that has zero templates or instructions?" to "ఎటువంటి సూచనలు లేని పనిని ఎదుర్కొన్నప్పుడు మీకు ఎలా అనిపిస్తుంది?",
            "If you were to organize a student-led micro-business or fundraiser, what is your focus?" to "మీరు విద్యార్థుల నేతృత్వంలో చిన్న వ్యాపారం లేదా నిధుల సేకరణను నిర్వహిస్తే, మీ దృష్టి దేనిపై ఉంటుంది?",
            "When a creative idea you suggested fails or is rejected, how do you iterate?" to "మీరు సూచించిన సృజనాత్మక ఆలోచన విఫలమైతే లేదా తిరస్కరించబడితే, మీరు ఎలా మెరుగుపరుస్తారు?",
            "What is your attitude toward taking calculated risks?" to "లెక్కించబడిన నష్టాలను తీసుకోవడంలో మీ వైఖరి ఏమిటి?",
            "How do you define a successful innovation?" to "ఒక విజయవంతమైన ఆవిష్కరణను మీరు ఎలా నిర్వచించారు?"
        )
    )

    fun get(key: String, lang: String): String {
        return uiTranslations[lang]?.get(key) ?: uiTranslations["English"]?.get(key) ?: key
    }

    fun translateCategory(category: String, lang: String): String {
        return categoryTranslations[lang]?.get(category) ?: category
    }

    fun translateDimension(dimension: String, lang: String): String {
        return dimensionsTranslations[lang]?.get(dimension) ?: dimension
    }

    fun translateQuestionText(englishTemplate: String, context: String, lang: String): String {
        val translatedTemplate = questionTemplatesTranslations[lang]?.get(englishTemplate) ?: englishTemplate
        val translatedContext = schoolContextTranslations[lang]?.get(context) ?: context
        return translatedTemplate.replace("\$context", translatedContext)
    }

    fun translateQuestion(q: PsychometricQuestion, lang: String): PsychometricQuestion {
        if (lang == "English") return q
        
        // Find standard patterns
        var cleanQText = q.question
        // Strip the standard index [i] prefix if present
        val prefixMatch = Regex("^\\[\\d+\\]\\s+").find(cleanQText)
        val prefix = prefixMatch?.value ?: ""
        var actualBody = if (prefix.isNotEmpty()) cleanQText.substring(prefix.length) else cleanQText
        
        // We know actualBody is formed using templates:
        // "When starting a new task $context, how do you prefer to coordinate?" etc.
        // Let's identify the template and context!
        val schoolContexts = listOf(
            "during a science laboratory experiment",
            "in a history group study session",
            "solving a complex mathematics worksheet",
            "writing a creative English essay",
            "coding in computer class",
            "learning geography maps",
            "in an inter-house sports tournament",
            "during a creative art session",
            "working on a high-school physics project",
            "attending a music workshop",
            "preparing a biology presentation",
            "writing a civics assignment",
            "organizing a school science exhibition",
            "participating in a poetry recitation contest",
            "while helping in the school library",
            "during an inter-school debate session",
            "planning a classroom cleanliness drive",
            "managing the classroom notice board",
            "setting up a recycling corner",
            "collecting funds for a charity drive",
            "helping a classmate who missed lessons",
            "studying in the quiet study hall",
            "running for class monitor",
            "attending an online career webinar",
            "planning a weekend cycle ride with friends",
            "creating a budget for a school picnic",
            "helping family organize a home event",
            "choosing a book at a bookstore",
            "learning a new technical skill online",
            "preparing for a competitive exam",
            "doing research on environmental topics",
            "assisting an elderly neighbor with tasks"
        )

        var detectedContext = ""
        var detectedTemplate = ""

        for (ctx in schoolContexts) {
            if (actualBody.contains(ctx)) {
                detectedContext = ctx
                detectedTemplate = actualBody.replace(ctx, "\$context")
                // clean up potential double spaces
                detectedTemplate = detectedTemplate.replace("  ", " ")
                break
            }
        }

        val translatedQuestionText = if (detectedContext.isNotEmpty() && detectedTemplate.isNotEmpty()) {
            val transText = translateQuestionText(detectedTemplate, detectedContext, lang)
            "$prefix$transText"
        } else {
            // Fallback: translate known static questions or return as-is
            val trans = questionTemplatesTranslations[lang]?.get(actualBody)
            if (trans != null) "$prefix$trans" else cleanQText
        }

        // Translate the 4 options based on their category or exact string
        val translatedOptions = q.options.map { option ->
            translateOption(option, q.dimension, lang)
        }

        // Translate the scores/dimensions attributes for display if needed
        val translatedScores = q.dimensionScores.map { score ->
            translateDimension(score, lang)
        }

        return q.copy(
            dimension = translateCategory(q.dimension, lang),
            question = translatedQuestionText,
            options = translatedOptions,
            dimensionScores = translatedScores
        )
    }

    private fun translateOption(option: String, category: String, lang: String): String {
        val dict = mapOf(
            "Hindi" to mapOf(
                "Take the lead, express thoughts openly, and organize others." to "नेतृत्व संभालें, विचारों को खुलकर व्यक्त करें और दूसरों को व्यवस्थित करें।",
                "Create a quiet, detailed individual plan and stick to it systematically." to "एक शांत, विस्तृत व्यक्तिगत योजना बनाएं और व्यवस्थित रूप से उस पर टिके रहें।",
                "Observe first, adapt to whatever the group needs, and support quietly." to "पहले निरीक्षण करें, समूह की जो भी आवश्यकता हो उसके अनुकूल बनें, और चुपचाप सहायता करें।",
                "Suggest bold, creative ideas and jump straight into action with enthusiasm." to "साहसिक, रचनात्मक विचारों का सुझाव दें और उत्साह के साथ सीधे कार्रवाई में कूदें।",
                
                "Pause, recognize my emotions, and communicate calmly and supportively." to "रुकें, अपनी भावनाओं को पहचानें और शांति व समर्थन के साथ संवाद करें।",
                "Acknowledge the situation objectively and focus on solving the underlying problem." to "स्थिति को निष्पक्ष रूप से स्वीकार करें और अंतर्निहित समस्या को हल करने पर ध्यान केंद्रित करें।",
                "Distance myself slightly from the emotional noise to stay balanced." to "संतुलित रहने के लिए भावनात्मक शोर से खुद को थोड़ा दूर रखें।",
                "Express my frustration or anxiety immediately to clear the air." to "भड़ास निकालने के लिए अपनी निराशा या चिंता को तुरंत व्यक्त करें।",
                
                "Take direct personal responsibility to fix it or report it because values matter." to "इसे ठीक करने या इसकी रिपोर्ट करने की सीधे व्यक्तिगत जिम्मेदारी लें क्योंकि मूल्य मायने रखते हैं।",
                "Discuss it with the group to agree on an ethical solution collectively." to "सामूहिक रूप से एक नैतिक समाधान पर सहमत होने के लिए समूह के साथ इस पर चर्चा करें।",
                "Follow standard protocols and school safety rules strictly." to "मानक प्रोटोकॉल और स्कूल सुरक्षा नियमों का सख्ती से पालन करें।",
                "Ignore it if it doesn't directly affect my personal score or task." to "यदि यह सीधे मेरे व्यक्तिगत स्कोर या कार्य को प्रभावित नहीं करता है, तो इसे अनदेखा करें।"
            ),
            "Telugu" to mapOf(
                "Take the lead, express thoughts openly, and organize others." to "నాయకత్వం వహించండి, ఆలోచనలను బహిరంగంగా వ్యక్తపరచండి మరియు ఇతరులను నిర్వహించండి.",
                "Create a quiet, detailed individual plan and stick to it systematically." to "నిశ్శబ్దమైన, వివరణాత్మక వ్యక్తిగత ప్రణాళికను రూపొందించండి మరియు దానికి క్రమపద్ధతిలో కట్టుబడి ఉండండి.",
                "Observe first, adapt to whatever the group needs, and support quietly." to "మొదట గమనించండి, సమూహానికి అవసరమైన దానికి అనుగుణంగా మారండి మరియు నిశ్శబ్దంగా మద్దతు ఇవ్వండి.",
                "Suggest bold, creative ideas and jump straight into action with enthusiasm." to "ధైర్యమైన, సృజనాత్మక ఆలోచనలను సూచించండి మరియు ఉత్సాహంతో నేరుగా రంగంలోకి దిగండి.",
                
                "Pause, recognize my emotions, and communicate calmly and supportively." to "ఆగండి, నా భావోద్వేగాలను గుర్తించండి మరియు ప్రశాంతంగా, సహాయకారిగా మాట్లాడండి.",
                "Acknowledge the situation objectively and focus on solving the underlying problem." to "పరిస్థితిని నిష్పక్షపాతంగా అంగీకరించి, అంతర్లీన సమస్యను పరిష్కరించడంపై దృష్టి పెట్టండి.",
                "Distance myself slightly from the emotional noise to stay balanced." to "సమతుల్యంగా ఉండటానికి భావోద్వేగ గందరగోళం నుండి నన్ను నేను కొద్దిగా దూరం చేసుకోండి.",
                "Express my frustration or anxiety immediately to clear the air." to "వాతావరణాన్ని క్లియర్ చేయడానికి నా నిరాశను లేదా ఆందోళనను వెంటనే వ్యక్తపరచండి.",
                
                "Take direct personal responsibility to fix it or report it because values matter." to "దాన్ని సరిచేయడానికి లేదా నివేదించడానికి ప్రత్యక్ష వ్యక్తిగత బాధ్యత తీసుకోండి, ఎందుకంటే విలువలు ముఖ్యం.",
                "Discuss it with the group to agree on an ethical solution collectively." to "సామూహికంగా ఒక నైతిక పరిష్కారాన్ని అంగీకరించడానికి బృందంతో చర్చించండి.",
                "Follow standard protocols and school safety rules strictly." to "ప్రామాణిక ప్రోటోకాల్‌లు మరియు పాఠశాల భద్రతా నియమాలను ఖచ్చితంగా పాటించండి.",
                "Ignore it if it doesn't directly affect my personal score or task." to "ఇది నా వ్యక్తిగత స్కోరు లేదా పనిని నేరుగా ప్రయోజనకరంగా మార్చకపోతే విస్మరించండి."
            )
        )

        // For dynamic learning options, etc. We can offer a quick mapper based on Category and prefix words
        val translated = dict[lang]?.get(option)
        if (translated != null) return translated

        // Dynamic/Category-specific fallback option translators
        return when (category) {
            "Learning Style" -> {
                // Learning style options
                if (option.contains("diagrams", ignoreCase = true) || option.contains("flowcharts", ignoreCase = true)) {
                    if (lang == "Hindi") "रंगीन आरेख, तीर और दृश्य फ़्लोचार्ट का अध्ययन करना।" else "రంగురంగుల రేఖాచిత్రాలు, బాణాలు మరియు దృశ్య ఫ్లోచార్ట్‌లను అధ్యయనం చేయడం."
                } else if (option.contains("podcast", ignoreCase = true) || option.contains("audio", ignoreCase = true) || option.contains("lecture", ignoreCase = true)) {
                    if (lang == "Hindi") "विस्तृत पॉडकास्ट, वीडियो या मौखिक व्याख्यान सुनना।" else "వివరణాత్మక పోడ్‌కాస్ట్, వీడియో లేదా మౌఖిక ఉపన్యాసం వినడం."
                } else if (option.contains("textbooks", ignoreCase = true) || option.contains("writing", ignoreCase = true) || option.contains("summaries", ignoreCase = true)) {
                    if (lang == "Hindi") "पाठ्यपुस्तकें पढ़ना, सारांश लिखना और लिखित अभ्यास करना।" else "పాఠ్యపుస్తకాలను చదవడం, సారాంశాలు రాయడం మరియు వ్రాతపూర్వక వ్యాయామాలు చేయడం."
                } else if (option.contains("experiments", ignoreCase = true) || option.contains("tactile", ignoreCase = true) || option.contains("doing", ignoreCase = true)) {
                    if (lang == "Hindi") "त्रिविमीय (3D) मॉडल बनाना, व्यावहारिक प्रयोग करना और करके सीखना।" else "త్రిమితీయ (3D) నమూనాలను రూపొందించడం, ప్రయోగాలు చేయడం మరియు చేయడం ద్వారా నేర్చుకోవడం."
                } else option
            }
            "Aptitude & Logical Thinking" -> {
                if (option.contains("step-by-step", ignoreCase = true) || option.contains("deductive", ignoreCase = true)) {
                    if (lang == "Hindi") "निगमनात्मक तर्क और प्रवाह आरेखों का उपयोग करके समस्या को चरण-दर-चरण विभाजित करें।" else "డిడక్టివ్ లాజిక్ మరియు ఫ్లో చార్ట్‌లను ఉపయోగించి సమస్యను దశలవారీగా పరిష్కరించండి."
                } else if (option.contains("patterns", ignoreCase = true) || option.contains("solved", ignoreCase = true)) {
                    if (lang == "Hindi") "पैटर्न की जांच करें और एक टेम्पलेट खोजने के लिए समान हल किए गए उदाहरणों को देखें।" else "పద్ధతులను పరిశీలించి, ఒక టెంప్లేట్‌ను కనుగొనడానికి సమానమైన పరిష్కరించబడిన ఉదాహరణలను చూడండి."
                } else if (option.contains("experiment", ignoreCase = true) || option.contains("intuitively", ignoreCase = true)) {
                    if (lang == "Hindi") "वास्तविक समय में परिवर्तनों को देखने के लिए सहजता से विभिन्न मानों के साथ प्रयोग करें।" else "మార్పులను గమనించడానికి సహజంగా వివిధ విలువలలో ప్రయోగాలు చేయండి."
                } else if (option.contains("expert", ignoreCase = true) || option.contains("collaborate", ignoreCase = true)) {
                    if (lang == "Hindi") "एक विशेषज्ञ सहपाठी या शिक्षक के साथ मिलकर प्रमाण का विश्लेषण करें।" else "నిరూపణను విశ్లేషించడానికి ఒక నిపుణుడైన తోటి విద్యార్థి లేదా ఉపాధ్యాయుడితో కలిసి పని చేయండి."
                } else option
            }
            "Leadership & Communication" -> {
                if (option.contains("viewpoints", ignoreCase = true) || option.contains("compromise", ignoreCase = true)) {
                    if (lang == "Hindi") "पहले सभी दृष्टिकोणों को सुनें और एक सहयोगी समझौता तैयार करें।" else "మొదట అన్ని దృక్కోణాలను విని, ఒక సహకార రాజీని రూపొందించండి."
                } else if (option.contains("deadlines", ignoreCase = true) || option.contains("checklists", ignoreCase = true)) {
                    if (lang == "Hindi") "स्पष्ट समय-सीमा निर्धारित करें, विस्तृत प्रगति चेकलिस्ट बनाएं और भूमिकाएं सौंपें।" else "ఖచ్చితమైన గడువులను నిర్ణయించండి, పురోగతి చెక్‌లిస్ట్‌లను సృష్టించండి మరియు పాత్రలను అప్పగించండి."
                } else if (option.contains("visionary", ignoreCase = true) || option.contains("energetic", ignoreCase = true)) {
                    if (lang == "Hindi") "कार्रवाई को प्रेरित करने के लिए एक ऊर्जावान, दूरदर्शी प्रस्तुति दें।" else "చర్యను ప్రేరేపించడానికి ఒక శక్తివంతమైన, దూరదృష్టి ప్రెజెంటేషన్ ఇవ్వండి."
                } else if (option.contains("supportive", ignoreCase = true) || option.contains("example", ignoreCase = true)) {
                    if (lang == "Hindi") "सहायक जांच प्रदान करें और कड़ी मेहनत के व्यक्तिगत उदाहरण द्वारा नेतृत्व करें।" else "సహాయక తనిఖీలను అందించండి మరియు కష్టపడి పనిచేసే వ్యక్తిగత ఉదాహరణ ద్వారా నడిపించండి."
                } else option
            }
            "Career Interests" -> {
                if (option.contains("scientific", ignoreCase = true) || option.contains("coding", ignoreCase = true)) {
                    if (lang == "Hindi") "वैज्ञानिक डेटा का विश्लेषण, सॉफ्टवेयर कोडिंग या चिकित्सा इलाज का शोध करना।" else "శాస్త్రీయ డేటాను విశ్లేషించడం, సాఫ్ట్‌వేర్ కోడింగ్ చేయడం లేదా వైద్య చికిత్సలపై పరిశోధన చేయడం."
                } else if (option.contains("creative", ignoreCase = true) || option.contains("music", ignoreCase = true) || option.contains("artistic", ignoreCase = true)) {
                    if (lang == "Hindi") "डिजिटल मीडिया डिजाइन करना, रचनात्मक साहित्य लिखना या संगीत की रचना करना।" else "డిజిటల్ మీడియా రూపకల్పన, సృజనాత్మక సాహిత్యం రాయడం లేదా సంగీతాన్ని సమకూర్చడం."
                } else if (option.contains("counseling", ignoreCase = true) || option.contains("teaching", ignoreCase = true) || option.contains("welfare", ignoreCase = true)) {
                    if (lang == "Hindi") "लोगों को परामर्श देना, छात्रों को पढ़ाना या सामुदायिक कल्याण अभियान चलाना।" else "ప్రజలకు కౌన్సెలింగ్ ఇవ్వడం, విద్యార్థులకు బోధించడం లేదా సామాజిక సంక్షేమ కార్యక్రమాలను నిర్వహించడం."
                } else if (option.contains("startups", ignoreCase = true) || option.contains("finance", ignoreCase = true) || option.contains("marketing", ignoreCase = true)) {
                    if (lang == "Hindi") "वाणिज्यिक स्टार्टअप शुरू करना, उत्पादों का विपणन करना या वित्त पोर्टफोलियो का प्रबंधन करना।" else "వ్యాపార స్టార్టప్‌లను ప్రారంభించడం, ఉత్పత్తులను మార్కెటింగ్ చేయడం లేదా ఫైనాన్స్ పోర్ట్‌ఫోలియోలను నిర్వహించడం."
                } else option
            }
            "Entrepreneurship & Innovation" -> {
                if (option.contains("ordering", ignoreCase = true) || option.contains("workflow", ignoreCase = true)) {
                    if (lang == "Hindi") "एक अभिनव डिजिटल ऑर्डरिंग सिस्टम या स्वचालित वर्कफ़्लो डिज़ाइन करना।" else "ఒక వినూత్న డిజిటల్ ఆర్డరింగ్ సిస్టమ్ లేదా ఆటోమేటెడ్ వర్క్‌ఫ్లోను రూపొందించడం."
                } else if (option.contains("budget", ignoreCase = true) || option.contains("scale", ignoreCase = true)) {
                    if (lang == "Hindi") "इसे बड़े पैमाने पर लागू करने के लिए एक व्यवस्थित वित्तीय बजट और व्यावसायिक योजना तैयार करना।" else "దీనిని విస్తరించడానికి ఒక క్రమబద్ధమైన ఆర్థిక బడ్జెట్ మరియు వ్యాపార ప్రణాళికను రూపొందించడం."
                } else if (option.contains("freedom", ignoreCase = true) || option.contains("creative", ignoreCase = true)) {
                    if (lang == "Hindi") "पूरी तरह से अनिर्देशित, रचनात्मक मार्गों का पता लगाने की स्वतंत्रता का आनंद लेना।" else "ఎటువంటి మార్గదర్శకాలు లేని సృజనాత్మక మార్గాలను అన్వేషించే స్వేచ్ఛను ఆస్వాదించడం."
                } else if (option.contains("reliable", ignoreCase = true) || option.contains("modifications", ignoreCase = true)) {
                    if (lang == "Hindi") "एक विश्वसनीय, मौजूदा समाधान ढूंढना और छोटे संशोधनों के साथ इसे अनुकूलित करना।" else "నమ్మకమైన, ఇప్పటికే ఉన్న పరిష్కారాన్ని కనుగొని, చిన్న మార్పులతో దానిని స్వీకరించడం."
                } else option
            }
            "Digital Behaviour & Ethics" -> {
                if (option.contains("threat", ignoreCase = true) || option.contains("victim", ignoreCase = true) || option.contains("report", ignoreCase = true)) {
                    if (lang == "Hindi") "मॉडरेटर को साइबर खतरे की रिपोर्ट करना और पीड़ित के कल्याण के बारे में निजी तौर पर पूछताछ करना।" else "సైబర్ ముప్పును మోడరేటర్‌లకు నివేదించడం మరియు బాధితుడి క్షేమ సమాచారాన్ని వ్యక్తిగతంగా తెలుసుకోవడం."
                } else if (option.contains("footprint", ignoreCase = true) || option.contains("privacy", ignoreCase = true)) {
                    if (lang == "Hindi") "अपने डिजिटल पदचिह्न का सावधानीपूर्वक मूल्यांकन करना और गोपनीयता मानकों की जांच करना।" else "నా డిజిటల్ ఫుట్‌ప్రింట్‌ను జాగ్రత్తగా అంచనా వేయడం మరియు గోప్యతా పారామితులను తనిఖీ చేయడం."
                } else if (option.contains("fake", ignoreCase = true) || option.contains("identity", ignoreCase = true)) {
                    if (lang == "Hindi") "अपनी वास्तविक पहचान की सुरक्षा के लिए नकली या माध्यमिक विवरण दर्ज करना।" else "నా నిజమైన గుర్తింపును రక్షించుకోవడానికి నకిలీ లేదా ద్వితీయ వివరాలను నమోదు చేయడం."
                } else if (option.contains("supervisor", ignoreCase = true) || option.contains("secure", ignoreCase = true)) {
                    if (lang == "Hindi") "किसी पर्यवेक्षक, अभिभावक या विश्वसनीय वयस्क से पूछना कि क्या प्लेटफ़ॉर्म कानूनी रूप से सुरक्षित है।" else "ప్లాట్‌ఫారమ్ సురక్షితమైనదేనా అని పర్యవేక్షకుడిని, తల్లిదండ్రులను లేదా నమ్మకమైన పెద్దలను అడగడం."
                } else option
            }
            else -> option
        }
    }
}
