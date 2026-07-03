package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface UiState {
    object Idle : UiState
    object Loading : UiState
    data class Success(val message: String) : UiState
    data class Error(val error: String) : UiState
}

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = AppRepository(db.appDao())

    // --- State Observables ---
    private val _currentUser = MutableStateFlow<AppUser?>(null)
    val currentUser: StateFlow<AppUser?> = _currentUser.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _currentLanguage = MutableStateFlow("English") // English, Telugu, Hindi
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    // Active screen navigation state for custom screen transitions
    private val _currentScreen = MutableStateFlow("LOGIN") // LOGIN, DASHBOARD, LESSON, ASSESSMENT, NEWSPAPER, PODCAST, SUBMISSIONS, APPOINTMENTS, SCHOOL_REPORTS
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Dynamic Lists Collected Reactively from Room DB
    val allUsers: StateFlow<List<AppUser>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allArticles: StateFlow<List<ArticleSubmission>> = repository.allArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAppointments: StateFlow<List<Appointment>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotices: StateFlow<List<Notice>> = repository.allNotices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAttendance: StateFlow<List<AttendanceRecord>> = repository.allAttendance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered lists depending on active user session
    val studentProgress: StateFlow<List<LsceProgress>> = currentUser
        .flatMapLatest { user ->
            if (user != null && user.role == "STUDENT") {
                repository.getProgressByStudent(user.id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentReports: StateFlow<List<PsychometricReport>> = currentUser
        .flatMapLatest { user ->
            if (user != null) {
                val sId = if (user.role == "PARENT") user.studentEmail else user.id
                repository.getReportsForStudent(sId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReports: StateFlow<List<PsychometricReport>> = repository.getAllReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentArticles: StateFlow<List<ArticleSubmission>> = currentUser
        .flatMapLatest { user ->
            if (user != null && user.role == "STUDENT") {
                repository.getArticlesByStudent(user.id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentBookmarks: StateFlow<List<PodcastBookmark>> = currentUser
        .flatMapLatest { user ->
            if (user != null && user.role == "STUDENT") {
                repository.getBookmarksForStudent(user.id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val parentAppointments: StateFlow<List<Appointment>> = currentUser
        .flatMapLatest { user ->
            if (user != null && user.role == "PARENT") {
                repository.getAppointmentsByParent(user.id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentAttendance: StateFlow<List<AttendanceRecord>> = currentUser
        .flatMapLatest { user ->
            if (user != null) {
                val sId = if (user.role == "PARENT") user.studentEmail else user.id
                repository.getAttendanceForStudent(sId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active LSCEP Lesson detail view
    private val _activeLesson = MutableStateFlow<LsceLesson?>(null)
    val activeLesson: StateFlow<LsceLesson?> = _activeLesson.asStateFlow()

    // Subscriptions payment status
    private val _isSubscribed = MutableStateFlow(true)
    val isSubscribed: StateFlow<Boolean> = _isSubscribed.asStateFlow()

    init {
        // Pre-populate data on first launch
        viewModelScope.launch {
            prepopulateDatabaseIfNeeded()
        }
    }

    private suspend fun prepopulateDatabaseIfNeeded() {
        // Pre-populate users
        val existingAdmin = repository.getUserById("admin_1")
        if (existingAdmin == null) {
            // Admin user
            repository.insertUser(
                AppUser("admin_1", "admin", "ADMIN", "Editorial Director", 0, schoolName = "Republic Publications")
            )
            // School Management
            repository.insertUser(
                AppUser("school_1", "principal", "SCHOOL", "Dr. Rama Rao (Principal)", 0, schoolName = "Republic Public School")
            )
            // Teacher
            repository.insertUser(
                AppUser("teacher_1", "teacher", "TEACHER", "Smt. Latha Madhavan", 0, schoolName = "Republic Public School")
            )
            // Students
            val defaultStudentId = "student_8"
            repository.insertUser(
                AppUser(defaultStudentId, "rahul", "STUDENT", "Rahul Sharma", 8, "parent_rahul@gmail.com", "rahul", "Republic Public School")
            )
            repository.insertUser(
                AppUser("student_5", "priya", "STUDENT", "Priya Reddy", 5, "parent_priya@gmail.com", "priya", "Republic Public School")
            )
            // Parents
            repository.insertUser(
                AppUser("parent_1", "parent", "PARENT", "Mr. Devendra Sharma", 8, "parent_rahul@gmail.com", defaultStudentId, "Republic Public School")
            )

            // Pre-populate notices
            repository.insertNotice(Notice(title = "LSCEP Monthly Syllabus Activated", content = "The Life Skills Education modules for Legal Awareness and Financial Discipline are now active for Classes 3 to 10.", date = "2026-07-02"))
            repository.insertNotice(Notice(title = "Psychometric Assessment Deadline", content = "Students of Classes 6 to 10 are requested to complete their AI Psychometric Assessment to receive their longitudinal career report.", date = "2026-07-01"))

            // Pre-populate attendance records
            repository.insertAttendance(AttendanceRecord("rahul_2026-07-01", defaultStudentId, "Rahul Sharma", "2026-07-01", "PRESENT"))
            repository.insertAttendance(AttendanceRecord("rahul_2026-07-02", defaultStudentId, "Rahul Sharma", "2026-07-02", "PRESENT"))
            repository.insertAttendance(AttendanceRecord("rahul_2026-07-03", defaultStudentId, "Rahul Sharma", "2026-07-03", "PRESENT"))
            repository.insertAttendance(AttendanceRecord("priya_2026-07-03", "student_5", "Priya Reddy", "2026-07-03", "ABSENT"))

            // Pre-populate a historic psychometric report for Rahul to show longitudinal progress
            repository.savePsychometricReport(
                PsychometricReport(
                    studentId = defaultStudentId,
                    timestamp = System.currentTimeMillis() - 31536000000L, // 1 year ago
                    year = 2025,
                    answersJson = "Historical answers",
                    strengths = "1. Artistic Innovation: High appreciation for creative solutions.\n2. Inquisitive Mindset: Quick to adapt to digital tools.",
                    learningStyle = "Visual Learner: Benefited from animations and videos.",
                    recommendedStream = "Humanities or Commerce with Applied Mathematics",
                    careerClusters = "Graphic Design, Digital Journalism, Public Relations",
                    fullReportJson = "Highly positive emotional quotient, showing initial leadership trends in Class 7."
                )
            )

            // Pre-populate progress
            repository.insertProgress(LsceProgress("${defaultStudentId}_8_LEGAL_1", defaultStudentId, "LEGAL", 8, 1, completed = true, quizScore = 100))
            repository.insertProgress(LsceProgress("${defaultStudentId}_8_FINANCIAL_1", defaultStudentId, "FINANCIAL", 8, 1, completed = false))

            // Pre-populate some approved newspaper articles
            repository.insertArticle(
                ArticleSubmission(
                    studentId = defaultStudentId,
                    studentName = "Rahul Sharma",
                    title = "The Power of Savings in Middle School",
                    category = "ESSAY",
                    content = "Savings is not just for adults. When we save Rs 100 every month instead of spending it on fast food, we are building a safety net. If we invest in compound interest models, we can multiply our pocket money safely. I suggest all Class 8 students start their own micro budgets to support their school science projects.",
                    status = "APPROVED"
                )
            )
            repository.insertArticle(
                ArticleSubmission(
                    studentId = "student_5",
                    studentName = "Priya Reddy",
                    title = "My Solar Wind Project Idea",
                    category = "INNOVATION",
                    content = "I want to construct a miniature windmill that generates electricity to charge our school tablets. It uses clean, renewable energy and helps our classrooms go green! I hope my classmates join me in making this a reality.",
                    status = "PENDING"
                )
            )
        }

        // Default login Rahul Sharma on startup so the streaming emulator is pre-populated
        loginUser("rahul", "password")
    }

    // --- Authentication and Role Management ---
    fun loginUser(username: String, roleHint: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            // Simple username matching for mock authentication with DB lookup
            val users = allUsers.value
            val match = users.find { it.username.lowercase() == username.lowercase() }
            if (match != null) {
                _currentUser.value = match
                _currentScreen.value = "DASHBOARD"
                _uiState.value = UiState.Success("Welcome, ${match.fullName}!")
            } else {
                // If user not pre-populated, create a dynamic user profile to handle registration
                val newRole = when (roleHint.uppercase()) {
                    "STUDENT" -> "STUDENT"
                    "PARENT" -> "PARENT"
                    "TEACHER" -> "TEACHER"
                    "SCHOOL" -> "SCHOOL"
                    else -> "ADMIN"
                }
                val newUser = AppUser(
                    id = "dyn_${username.lowercase()}",
                    username = username.lowercase(),
                    role = newRole,
                    fullName = username.replaceFirstChar { it.uppercase() },
                    classLevel = if (newRole == "STUDENT") 8 else 0,
                    parentEmail = if (newRole == "STUDENT") "parent_${username.lowercase()}@gmail.com" else "",
                    studentEmail = if (newRole == "PARENT") "student_${username.lowercase()}" else ""
                )
                repository.insertUser(newUser)
                _currentUser.value = newUser
                _currentScreen.value = "DASHBOARD"
                _uiState.value = UiState.Success("Registered and Logged in as ${newUser.fullName}!")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentScreen.value = "LOGIN"
        _uiState.value = UiState.Idle
    }

    fun setLanguage(language: String) {
        _currentLanguage.value = language
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
        _uiState.value = UiState.Idle
    }

    fun updateStudentClass(classLevel: Int) {
        val user = _currentUser.value ?: return
        if (user.role == "STUDENT") {
            viewModelScope.launch {
                val updated = user.copy(classLevel = classLevel)
                repository.insertUser(updated)
                _currentUser.value = updated
            }
        }
    }

    // --- LSCEP LMS Actions ---
    fun selectLesson(lesson: LsceLesson) {
        _activeLesson.value = lesson
        _currentScreen.value = "LESSON"
    }

    fun completeActiveLesson(quizScore: Int) {
        val user = _currentUser.value ?: return
        val lesson = _activeLesson.value ?: return
        if (user.role == "STUDENT") {
            viewModelScope.launch {
                val progressId = "${user.id}_${user.classLevel}_${lesson.moduleName}_${lesson.id}"
                val progress = LsceProgress(
                    id = progressId,
                    studentId = user.id,
                    moduleName = lesson.moduleName,
                    classLevel = user.classLevel,
                    lessonId = lesson.id,
                    completed = true,
                    quizScore = quizScore
                )
                repository.insertProgress(progress)
                _uiState.value = UiState.Success("Completed lesson and scored $quizScore%!")
            }
        }
    }

    // --- AI Psychometric Assessment Engine ---
    fun savePsychometricReport(report: PsychometricReport) {
        viewModelScope.launch {
            repository.savePsychometricReport(report)
        }
    }

    fun submitPsychometricAssessment(answers: Map<Int, Int>) {
        val user = _currentUser.value ?: return
        val sId = if (user.role == "PARENT") user.studentEmail else user.id
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            // Build summary of answers to pass to Gemini
            val summaryBuilder = StringBuilder()
            answers.forEach { (qId, optionIdx) ->
                val q = DataRepository.psychometricQuestions.find { it.id == qId }
                if (q != null) {
                    val optionText = q.options.getOrNull(optionIdx) ?: "None"
                    summaryBuilder.append("- Q: ${q.question}\n  Answer: $optionText [Dimension: ${q.dimension}]\n")
                }
            }

            try {
                repository.evaluateAndSaveReport(
                    studentId = sId,
                    studentName = if (user.role == "PARENT") "Rahul" else user.fullName,
                    classLevel = if (user.role == "PARENT") 8 else user.classLevel,
                    answersSummary = summaryBuilder.toString(),
                    answersJson = answers.entries.joinToString(",") { "${it.key}:${it.value}" }
                )
                _uiState.value = UiState.Success("Assessment Evaluated by Gemini AI!")
                _currentScreen.value = "DASHBOARD"
            } catch (e: Exception) {
                _uiState.value = UiState.Error("AI Evaluation failed: ${e.message}")
            }
        }
    }

    // --- Newspaper Submissions ---
    fun submitArticle(title: String, category: String, content: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val article = ArticleSubmission(
                studentId = user.id,
                studentName = user.fullName,
                title = title,
                category = category,
                content = content,
                status = "PENDING"
            )
            repository.insertArticle(article)
            _uiState.value = UiState.Success("Article submitted successfully. Sent to Republic Editors for approval!")
            _currentScreen.value = "NEWSPAPER"
        }
    }

    fun approveArticle(id: Int, approve: Boolean, feedback: String) {
        viewModelScope.launch {
            val status = if (approve) "APPROVED" else "REJECTED"
            repository.updateArticleStatus(id, status, feedback)
            _uiState.value = UiState.Success("Article submission marked as $status!")
        }
    }

    // --- Podcast Bookmarking ---
    fun toggleBookmark(podcastId: String) {
        val user = _currentUser.value ?: return
        if (user.role == "STUDENT") {
            viewModelScope.launch {
                val bookmarks = studentBookmarks.value
                val existing = bookmarks.find { it.podcastId == podcastId }
                val newBookmark = PodcastBookmark(
                    id = "${user.id}_$podcastId",
                    studentId = user.id,
                    podcastId = podcastId,
                    bookmarked = !(existing?.bookmarked ?: false)
                )
                repository.saveBookmark(newBookmark)
            }
        }
    }

    // --- Appointment Booking ---
    fun bookCounsellingAppointment(date: String, time: String, notes: String) {
        val user = _currentUser.value ?: return
        if (user.role == "PARENT") {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                val appointment = Appointment(
                    parentId = user.id,
                    parentName = user.fullName,
                    studentName = "Rahul Sharma",
                    date = date,
                    time = time,
                    notes = notes,
                    status = "SCHEDULED"
                )
                repository.insertAppointment(appointment)
                _uiState.value = UiState.Success("Counselling Appointment Booked successfully!")
                _currentScreen.value = "APPOINTMENTS"
            }
        }
    }

    fun cancelCounsellingAppointment(id: Int) {
        viewModelScope.launch {
            repository.cancelAppointment(id)
            _uiState.value = UiState.Success("Appointment cancelled.")
        }
    }

    // --- Notices ---
    fun postNotice(title: String, content: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.insertNotice(
                Notice(
                    schoolName = user.schoolName,
                    title = title,
                    content = content
                )
            )
            _uiState.value = UiState.Success("Announcement published successfully!")
        }
    }

    // --- Attendance Logger ---
    fun logAttendance(studentId: String, studentName: String, date: String, isPresent: Boolean) {
        viewModelScope.launch {
            repository.insertAttendance(
                AttendanceRecord(
                    id = "${studentId}_$date",
                    studentId = studentId,
                    studentName = studentName,
                    date = date,
                    status = if (isPresent) "PRESENT" else "ABSENT"
                )
            )
        }
    }

    // --- Payment Simulation ---
    fun simulatePayment() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            kotlinx.coroutines.delay(1200)
            _isSubscribed.value = true
            _uiState.value = UiState.Success("Subscription Renewed Successfully via Republic UPI Gateway!")
        }
    }
}
