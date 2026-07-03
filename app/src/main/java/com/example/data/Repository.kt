package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    // --- User Queries ---
    val allUsers: Flow<List<AppUser>> = appDao.getAllUsersFlow()

    suspend fun getUserById(id: String): AppUser? = withContext(Dispatchers.IO) {
        appDao.getUserById(id)
    }

    suspend fun insertUser(user: AppUser) = withContext(Dispatchers.IO) {
        appDao.insertUser(user)
    }

    suspend fun deleteUser(id: String) = withContext(Dispatchers.IO) {
        appDao.deleteUserById(id)
    }

    // --- LSCEP Progress ---
    fun getProgressByStudent(studentId: String): Flow<List<LsceProgress>> {
        return appDao.getProgressByStudentFlow(studentId)
    }

    suspend fun getProgressForModule(studentId: String, classLevel: Int, moduleName: String): List<LsceProgress> = withContext(Dispatchers.IO) {
        appDao.getProgressForModule(studentId, classLevel, moduleName)
    }

    suspend fun insertProgress(progress: LsceProgress) = withContext(Dispatchers.IO) {
        appDao.insertProgress(progress)
    }

    // --- Psychometric Reports ---
    fun getReportsForStudent(studentId: String): Flow<List<PsychometricReport>> {
        return appDao.getReportsForStudentFlow(studentId)
    }

    fun getAllReports(): Flow<List<PsychometricReport>> {
        return appDao.getAllPsychometricReportsFlow()
    }

    suspend fun savePsychometricReport(report: PsychometricReport) = withContext(Dispatchers.IO) {
        appDao.insertPsychometricReport(report)
    }

    suspend fun evaluateAndSaveReport(
        studentId: String,
        studentName: String,
        classLevel: Int,
        answersSummary: String,
        answersJson: String
    ): PsychometricReport = withContext(Dispatchers.IO) {
        // Fetch from Gemini AI
        val aiReport = GeminiClient.getPsychometricReport(studentName, classLevel, answersSummary)
        
        val report = PsychometricReport(
            studentId = studentId,
            timestamp = System.currentTimeMillis(),
            year = 2026,
            answersJson = answersJson,
            strengths = aiReport.strengths,
            learningStyle = aiReport.learningStyle,
            recommendedStream = aiReport.recommendedStream,
            careerClusters = aiReport.careerClusters,
            fullReportJson = aiReport.behaviouralInsights + "||" + aiReport.skillsToDevelop
        )
        
        appDao.insertPsychometricReport(report)
        report
    }

    // --- Article Submissions (Newspaper) ---
    val allArticles: Flow<List<ArticleSubmission>> = appDao.getAllArticlesFlow()

    fun getArticlesByStudent(studentId: String): Flow<List<ArticleSubmission>> {
        return appDao.getArticlesByStudentFlow(studentId)
    }

    suspend fun insertArticle(article: ArticleSubmission) = withContext(Dispatchers.IO) {
        appDao.insertArticle(article)
    }

    suspend fun updateArticleStatus(id: Int, status: String, feedback: String) = withContext(Dispatchers.IO) {
        appDao.updateArticleStatus(id, status, feedback)
    }

    // --- Podcast Bookmarks ---
    fun getBookmarksForStudent(studentId: String): Flow<List<PodcastBookmark>> {
        return appDao.getBookmarksForStudentFlow(studentId)
    }

    suspend fun saveBookmark(bookmark: PodcastBookmark) = withContext(Dispatchers.IO) {
        appDao.insertBookmark(bookmark)
    }

    // --- Appointments ---
    val allAppointments: Flow<List<Appointment>> = appDao.getAllAppointmentsFlow()

    fun getAppointmentsByParent(parentId: String): Flow<List<Appointment>> {
        return appDao.getAppointmentsByParentFlow(parentId)
    }

    suspend fun insertAppointment(appointment: Appointment) = withContext(Dispatchers.IO) {
        appDao.insertAppointment(appointment)
    }

    suspend fun cancelAppointment(id: Int) = withContext(Dispatchers.IO) {
        appDao.updateAppointmentStatus(id, "CANCELLED")
    }

    // --- School Notices ---
    val allNotices: Flow<List<Notice>> = appDao.getAllNoticesFlow()

    suspend fun insertNotice(notice: Notice) = withContext(Dispatchers.IO) {
        appDao.insertNotice(notice)
    }

    // --- Attendance Records ---
    val allAttendance: Flow<List<AttendanceRecord>> = appDao.getAllAttendanceFlow()

    fun getAttendanceForStudent(studentId: String): Flow<List<AttendanceRecord>> {
        return appDao.getAttendanceForStudentFlow(studentId)
    }

    suspend fun insertAttendance(record: AttendanceRecord) = withContext(Dispatchers.IO) {
        appDao.insertAttendance(record)
    }
}
