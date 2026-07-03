package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class AppUser(
    @PrimaryKey val id: String,
    val username: String,
    val role: String, // STUDENT, PARENT, TEACHER, SCHOOL, ADMIN
    val fullName: String,
    val classLevel: Int = 8, // Class 3 to 10
    val parentEmail: String = "",
    val studentEmail: String = "",
    val schoolName: String = "Republic Public School"
)

@Entity(tableName = "lsce_progress")
data class LsceProgress(
    @PrimaryKey val id: String, // studentId_classLevel_module_lessonId
    val studentId: String,
    val moduleName: String, // LEGAL, FINANCIAL, DIGITAL, SOCIAL
    val classLevel: Int,
    val lessonId: Int,
    val completed: Boolean = false,
    val quizScore: Int = -1 // -1 means not taken
)

@Entity(tableName = "psychometric_reports")
data class PsychometricReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val year: Int = 2026,
    val answersJson: String, // Save user responses
    val strengths: String,
    val learningStyle: String,
    val recommendedStream: String,
    val careerClusters: String,
    val fullReportJson: String // Entire response from Gemini AI
)

@Entity(tableName = "articles")
data class ArticleSubmission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,
    val studentName: String,
    val title: String,
    val category: String, // NEWS, SCHOLARSHIP, INNOVATION, POEM, ESSAY, ART
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED
    val adminFeedback: String = ""
)

@Entity(tableName = "podcast_bookmarks")
data class PodcastBookmark(
    @PrimaryKey val id: String, // studentId_podcastId
    val studentId: String,
    val podcastId: String,
    val bookmarked: Boolean = false
)

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val parentId: String,
    val parentName: String,
    val studentName: String,
    val date: String,
    val time: String,
    val notes: String = "",
    val status: String = "SCHEDULED" // SCHEDULED, COMPLETED, CANCELLED
)

@Entity(tableName = "notices")
data class Notice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val schoolName: String = "Republic Public School",
    val title: String,
    val content: String,
    val date: String = "2026-07-03"
)

@Entity(tableName = "attendance")
data class AttendanceRecord(
    @PrimaryKey val id: String, // studentId_date
    val studentId: String,
    val studentName: String,
    val date: String,
    val status: String // PRESENT, ABSENT
)

// UI and Helper Models
data class LsceLesson(
    val id: Int,
    val title: String,
    val moduleName: String,
    val description: String,
    val textContent: String,
    val durationMin: Int = 15,
    val podcastTitle: String = "",
    val podcastDuration: String = "",
    val quizQuestions: List<QuizQuestion> = emptyList()
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)

data class PsychometricQuestion(
    val id: Int,
    val dimension: String, // Personality, Learning Style, Aptitude, Digital Behaviour, Financial Awareness, Legal Awareness, Social Skills
    val question: String,
    val options: List<String>,
    val dimensionScores: List<String> // Maps option selection to dimension attributes
)

// Static data banks
object DataRepository {
    val lsceModules = listOf("LEGAL", "FINANCIAL", "DIGITAL", "SOCIAL")

    // Dynamic LSCEP Lesson content generation depending on Class level
    fun getLessonsForClassAndModule(classLevel: Int, module: String): List<LsceLesson> {
        return when (module) {
            "LEGAL" -> listOf(
                LsceLesson(
                    id = 1,
                    title = if (classLevel < 6) "My Rights and Protection" else "The Constitution & Child Rights",
                    moduleName = "LEGAL",
                    description = "Understanding basic legal rights, children's protection laws, and safety regulations.",
                    textContent = """
                        Welcome to Legal Awareness!
                        At this level, we learn about essential rights guaranteed by our Constitution.
                        1. **Right to Education (RTE)**: Every child between 6 and 14 has the right to free and compulsory education.
                        2. **Protection from Discrimination**: Laws protect every student regardless of background, gender, or religion.
                        3. **Safety and Reporting**: If you ever feel unsafe at home, school, or online, you have a legal right to speak up. National Helplines (like Childline 1098) are available 24/7.
                        
                        Remember, legal literacy empowers citizens to make correct decisions and safeguard their communities!
                    """.trimIndent(),
                    podcastTitle = "Podcast EP 1: Law & Your Daily Life",
                    podcastDuration = "12 mins",
                    quizQuestions = listOf(
                        QuizQuestion(1, "What is the age group covered under the Right to Education (RTE) Act?", listOf("4 to 12 years", "6 to 14 years", "10 to 18 years", "Any age"), 1),
                        QuizQuestion(2, "What is the national child safety helpline number in India?", listOf("100", "101", "1098", "112"), 2)
                    )
                ),
                LsceLesson(
                    id = 2,
                    title = if (classLevel < 6) "Rules at School and Home" else "Contractual Literacy & Civil Laws",
                    moduleName = "LEGAL",
                    description = "Differentiating between rules, policies, and national legislation.",
                    textContent = """
                        Rules govern our behavior to ensure harmony.
                        In public spaces and contracts:
                        1. **Contracts**: An agreement enforceable by law. As students grow, knowing basic terms of usage, privacy terms of apps, and educational agreements is vital.
                        2. **Civil Codes**: Simple regulations about property, civic responsibility, and environmental preservation.
                    """.trimIndent(),
                    podcastTitle = "Podcast EP 2: Understanding Terms & Agreements",
                    podcastDuration = "15 mins",
                    quizQuestions = listOf(
                        QuizQuestion(1, "Which of the following is legally binding if signed by consenting adults?", listOf("A friendly promise", "A valid Contract", "A classroom rule list", "A calendar schedule"), 1)
                    )
                )
            )
            "FINANCIAL" -> listOf(
                LsceLesson(
                    id = 1,
                    title = if (classLevel < 6) "Understanding Money & Saving" else "Budgeting, Savings & Dynamic Interest",
                    moduleName = "FINANCIAL",
                    description = "Mastering financial discipline through intelligent budgeting and saving habits.",
                    textContent = """
                        Financial Discipline is the key to life long stability.
                        1. **Needs vs Wants**: Food, shelter, and basic clothing are needs. Video games, high-end gadgets, and restaurant food are wants.
                        2. **Budgeting Formula**: Income - Savings = Expenses. Always pay yourself first!
                        3. **Compound Interest**: The eighth wonder of the world. Money saved early earns interest, and that interest earns more interest over time!
                    """.trimIndent(),
                    podcastTitle = "Podcast EP 3: Pocket Money Magic",
                    podcastDuration = "10 mins",
                    quizQuestions = listOf(
                        QuizQuestion(1, "What is the primary difference between a 'Need' and a 'Want'?", listOf("Needs are expensive, wants are cheap", "Needs are essential for survival, wants are for luxury", "There is no difference", "Wants are required by law"), 1),
                        QuizQuestion(2, "What does the budgeting rule 'Save First, Spend Later' imply?", listOf("Put aside savings before planning expenses", "Spend everything and save the rest", "Do not spend any money", "Borrow money to save"), 0)
                    )
                ),
                LsceLesson(
                    id = 2,
                    title = if (classLevel < 6) "Banks & Digital Wallets" else "Taxation, Banking & Stock Markets",
                    moduleName = "FINANCIAL",
                    description = "Understanding how financial institutions and modern markets operate.",
                    textContent = """
                        Modern finance goes beyond simple piggy banks:
                        1. **Savings Account**: A secure place to deposit money and earn moderate interest.
                        2. **Inflation**: The rate at which prices rise, decreasing purchasing power over time.
                        3. **Digital Literacy in Payments**: Using UPI, cards, and mobile banking securely.
                    """.trimIndent(),
                    podcastTitle = "Podcast EP 4: Inside a Bank Vault",
                    podcastDuration = "14 mins",
                    quizQuestions = listOf(
                        QuizQuestion(1, "Which of these institutions regulates banking activities in India?", listOf("SEBI", "RBI", "IRDAI", "Supreme Court"), 1)
                    )
                )
            )
            "DIGITAL" -> listOf(
                LsceLesson(
                    id = 1,
                    title = if (classLevel < 6) "Being Safe Online" else "Cyber Security, Phishing & Digital Footprint",
                    moduleName = "DIGITAL",
                    description = "Understanding digital safety, social media hygiene, and security practices.",
                    textContent = """
                        Welcome to Digital Ethics!
                        Every click leaves a permanent digital footprint.
                        1. **Strong Passwords**: Use combinations of uppercase, lowercase, numbers, and symbols. Never share passwords.
                        2. **Phishing Scams**: Suspicious links promising free gifts or prizes are traps to steal data. Always double check URLs.
                        3. **Cyber Bullying**: Respect others online. If you witness online abuse, report and block the user immediately. Do not engage.
                    """.trimIndent(),
                    podcastTitle = "Podcast EP 5: Cyber Safe Shield",
                    podcastDuration = "18 mins",
                    quizQuestions = listOf(
                        QuizQuestion(1, "Which of the following is a strong password?", listOf("123456", "password", "MyP@ssw0rd!2", "admin"), 2),
                        QuizQuestion(2, "What is a 'Digital Footprint'?", listOf("The physical size of your phone", "The permanent trail of data you leave online", "Your screen brightness setting", "A computer virus"), 1)
                    )
                )
            )
            "SOCIAL" -> listOf(
                LsceLesson(
                    id = 1,
                    title = if (classLevel < 6) "Helping My Community" else "Civic Duties, Social Justice & Active Citizenship",
                    moduleName = "SOCIAL",
                    description = "Nurturing empathy, national citizenship responsibilities, and civic contributions.",
                    textContent = """
                        Social Responsibility defines our role in a larger nation.
                        1. **Active Citizenship**: Voting, keeping public spaces clean, and standing up for sustainable environmental practices.
                        2. **Empathy & Inclusion**: Supporting classmates with different physical, linguistic, or socio-economic profiles.
                        3. **Civic Responsibility**: Preserving public property and respecting safety rules (traffic signals, litter control).
                    """.trimIndent(),
                    podcastTitle = "Podcast EP 6: Changing the World Locally",
                    podcastDuration = "11 mins",
                    quizQuestions = listOf(
                        QuizQuestion(1, "Which of these is a form of active citizenship?", listOf("Littering in public parks", "Volunteering for local cleanliness drives", "Avoiding news and current affairs", "Ignoring traffic laws"), 1)
                    )
                )
            )
            else -> emptyList()
        }
    }

    // Psychometric adaptive question bank (representing the 1,000 adaptive questions in a scale-down structure)
    val psychometricQuestions = PsychometricService.questionBank

    // Podcasts data
    data class PodcastItem(
        val id: String,
        val title: String,
        val speaker: String,
        val duration: String,
        val category: String,
        val description: String,
        val audioUrl: String = ""
    )

    val podcastsList = listOf(
        PodcastItem("pod_1", "Legal Empowerment for Teenagers", "Adv. Kiran Sharma", "15 mins", "LEGAL", "Understanding basic traffic, civil, and cyber safety regulations in India."),
        PodcastItem("pod_2", "Mastering the Power of Compounding", "Sujatha Sen (Financial Planner)", "18 mins", "FINANCIAL", "A simple, game-changing look into why starting to save at age 12 is a superpower."),
        PodcastItem("pod_3", "Cyber Safety: Guards Against Phishing", "Dr. Amit Verma (Cyber Security Expert)", "12 mins", "DIGITAL", "Real-world stories of digital scams and how simple safety checklists can protect your family."),
        PodcastItem("pod_4", "Active Citizenship: My Civic Impact", "Shekhar Rao (Social Activist)", "14 mins", "SOCIAL", "How school students can drive waste management and cleanliness campaigns locally."),
        PodcastItem("pod_5", "Career Paths in Modern Robotics", "Dr. Rohan Joshi (AI Scientist)", "22 mins", "CAREER", "A fascinating exploration of future engineering, vocational skills, and robotics domains.")
    )

    // Newspaper posts
    data class NewspaperArticle(
        val id: Int,
        val title: String,
        val category: String, // CURRENT_AFFAIRS, SCHOLARSHIP, INNOVATION, STUDENT_COLUMN
        val author: String,
        val date: String,
        val content: String,
        val summary: String,
        val imageResId: String = ""
    )

    val newspaperArticles = listOf(
        NewspaperArticle(
            id = 1,
            title = "National Innovation Award Winners 2026",
            category = "INNOVATION",
            author = "Republic Bureau",
            date = "July 02, 2026",
            content = "Two middle school students from Hyderabad designed an automated crop-watering sensor using recycled parts and low-cost microcontrollers. They were honored by the Science Ministry and awarded a scholarship of INR 2,00,000 for their outstanding contribution to agriculture tech. This shows that innovation is not limited by age or expensive labs; it is driven by solving real community problems.",
            summary = "Hyderabad middle schoolers design automated crop-watering sensors using low-cost recycled electronics, winning INR 2 Lakh scholarship."
        ),
        NewspaperArticle(
            id = 2,
            title = "Top Scholarships for Students in Classes 8-10",
            category = "SCHOLARSHIP",
            author = "Career Cell",
            date = "June 30, 2026",
            content = "The National Talent Search Examination (NTSE) and National Means-cum-Merit Scholarship (NMMS) application dates have been released. These government-sponsored scholarships offer direct monthly financial support for secondary and higher education. Understanding application rules, sample papers, and legal requirements is crucial for all eligible students. Read more to find application forms, links, and study schedules.",
            summary = "A comprehensive list of application dates and guidelines for the NTSE and NMMS government scholarship programs."
        ),
        NewspaperArticle(
            id = 3,
            title = "Understanding Digital Cyber Crime Laws in India",
            category = "CURRENT_AFFAIRS",
            author = "Legal Awareness Team",
            date = "June 28, 2026",
            content = "With the increase of online classrooms, digital safety has become as critical as physical safety. Under India's IT Act 2000, sharing malicious links, cyber-bullying, or impersonating others online are serious legal offenses. Students are advised to immediately report identity theft, inappropriate text messages, or hacking to parent supervisors or register an official complaint at cybercrime.gov.in.",
            summary = "An essential guide to India's IT Act 2000 and cyber crime reporting portals for secondary school students."
        )
    )
}
