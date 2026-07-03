package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- User Queries ---
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): AppUser?

    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<AppUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: AppUser)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: String)

    // --- LSCEP Progress Queries ---
    @Query("SELECT * FROM lsce_progress WHERE studentId = :studentId")
    fun getProgressByStudentFlow(studentId: String): Flow<List<LsceProgress>>

    @Query("SELECT * FROM lsce_progress WHERE studentId = :studentId AND classLevel = :classLevel AND moduleName = :moduleName")
    suspend fun getProgressForModule(studentId: String, classLevel: Int, moduleName: String): List<LsceProgress>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: LsceProgress)

    // --- Psychometric Report Queries ---
    @Query("SELECT * FROM psychometric_reports WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getReportsForStudentFlow(studentId: String): Flow<List<PsychometricReport>>

    @Query("SELECT * FROM psychometric_reports ORDER BY timestamp DESC")
    fun getAllPsychometricReportsFlow(): Flow<List<PsychometricReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPsychometricReport(report: PsychometricReport)

    // --- Article Submission Queries ---
    @Query("SELECT * FROM articles ORDER BY timestamp DESC")
    fun getAllArticlesFlow(): Flow<List<ArticleSubmission>>

    @Query("SELECT * FROM articles WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getArticlesByStudentFlow(studentId: String): Flow<List<ArticleSubmission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleSubmission)

    @Query("UPDATE articles SET status = :status, adminFeedback = :feedback WHERE id = :id")
    suspend fun updateArticleStatus(id: Int, status: String, feedback: String)

    // --- Podcast Bookmark Queries ---
    @Query("SELECT * FROM podcast_bookmarks WHERE studentId = :studentId")
    fun getBookmarksForStudentFlow(studentId: String): Flow<List<PodcastBookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: PodcastBookmark)

    // --- Appointment Queries ---
    @Query("SELECT * FROM appointments ORDER BY id DESC")
    fun getAllAppointmentsFlow(): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE parentId = :parentId ORDER BY id DESC")
    fun getAppointmentsByParentFlow(parentId: String): Flow<List<Appointment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateAppointmentStatus(id: Int, status: String)

    // --- Notice Queries ---
    @Query("SELECT * FROM notices ORDER BY id DESC")
    fun getAllNoticesFlow(): Flow<List<Notice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: Notice)

    // --- Attendance Queries ---
    @Query("SELECT * FROM attendance ORDER BY date DESC")
    fun getAllAttendanceFlow(): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    fun getAttendanceForStudentFlow(studentId: String): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(record: AttendanceRecord)
}

@Database(
    entities = [
        AppUser::class,
        LsceProgress::class,
        PsychometricReport::class,
        ArticleSubmission::class,
        PodcastBookmark::class,
        Appointment::class,
        Notice::class,
        AttendanceRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "republic_student_one_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
