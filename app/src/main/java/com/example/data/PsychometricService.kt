package com.example.data

import kotlin.random.Random

object PsychometricService {

    val categories = listOf(
        "Personality & Behaviour",
        "Emotional Intelligence",
        "Responsibility & Values",
        "Learning Style",
        "Aptitude & Logical Thinking",
        "Leadership & Communication",
        "Career Interests",
        "Entrepreneurship & Innovation",
        "Digital Behaviour & Ethics"
    )

    // A list of exactly 1,000 generated questions, structured across the nine defined categories
    val questionBank: List<PsychometricQuestion> = generate1000Questions()

    fun getQuestionById(id: Int): PsychometricQuestion? {
        return questionBank.find { it.id == id }
    }

    /**
     * Logic handler to fetch randomized subsets of the 1,000-question bank for adaptive testing.
     * QUICK: 1 question per category (9 total)
     * DETAILED: 2 questions per category (18 total)
     * COMPREHENSIVE: 3 questions per category (27 total)
     */
    fun getAdaptiveQuestions(mode: String): List<PsychometricQuestion> {
        val grouped = questionBank.groupBy { it.dimension }
        val numPerCategory = when (mode.uppercase()) {
            "QUICK" -> 1
            "DETAILED" -> 2
            "COMPREHENSIVE" -> 3
            else -> 1
        }

        // Return balanced randomized subsets representing all 9 defined categories
        return categories.flatMap { category ->
            val list = grouped[category] ?: emptyList()
            // Shuffle with a stable seed per assessment run or standard random shuffling
            list.shuffled().take(numPerCategory)
        }.sortedBy { it.id }
    }

    private fun generate1000Questions(): List<PsychometricQuestion> {
        val questions = mutableListOf<PsychometricQuestion>()
        var globalId = 1

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

        // 1. Personality & Behaviour templates (111 questions)
        for (i in 1..111) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "When starting a new task $context, how do you prefer to coordinate?"
                1 -> "If a new unexpected requirement arises $context, how do you adjust your pace?"
                2 -> "What environment helps you focus best $context?"
                3 -> "How do you share feedback with classmates $context?"
                4 -> "When choosing roles $context, which do you naturally lean towards?"
                else -> "If you finish your primary task early $context, what is your next action?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Personality & Behaviour",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Take the lead, express thoughts openly, and organize others.",
                        "Create a quiet, detailed individual plan and stick to it systematically.",
                        "Observe first, adapt to whatever the group needs, and support quietly.",
                        "Suggest bold, creative ideas and jump straight into action with enthusiasm."
                    ),
                    dimensionScores = listOf(
                        "Extroverted Leadership",
                        "Introverted Structured Planning",
                        "Adaptive Supportive Behavior",
                        "Spontaneous Creative Action"
                    )
                )
            )
        }

        // 2. Emotional Intelligence templates (111 questions)
        for (i in 1..111) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "How do you recognize when a peer is feeling stressed or left out $context?"
                1 -> "If you receive critical constructive feedback on your work $context, how do you process it?"
                2 -> "When an argument begins between group partners $context, how do you handle your feelings?"
                3 -> "If you make a frustrating mistake $context, what is your immediate response?"
                4 -> "How do you help build a positive, encouraging team atmosphere $context?"
                else -> "When you feel extremely tired or overwhelmed $context, how do you restore your focus?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Emotional Intelligence",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Pause, recognize my emotions, and communicate calmly and supportively.",
                        "Acknowledge the situation objectively and focus on solving the underlying problem.",
                        "Distance myself slightly from the emotional noise to stay balanced.",
                        "Express my frustration or anxiety immediately to clear the air."
                    ),
                    dimensionScores = listOf(
                        "High Self-Awareness & Empathy",
                        "Cognitive Problem-Solving Focus",
                        "Emotional Regulation & Control",
                        "High Reactivity (Needs Support)"
                    )
                )
            )
        }

        // 3. Responsibility & Values templates (111 questions)
        for (i in 1..111) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "If you spot a safety issue or violation of rules $context, what is your reaction?"
                1 -> "When you commit to a deadline $context but face an unexpected delay, how do you act?"
                2 -> "What does 'fairness and inclusion' mean to you $context?"
                3 -> "If you notice public or school property being handled carelessly $context, what do you do?"
                4 -> "How do you ensure honesty and integrity are maintained $context?"
                else -> "When representing your school or team $context, what value do you prioritize most?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Responsibility & Values",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Take direct personal responsibility to fix it or report it because values matter.",
                        "Discuss it with the group to agree on an ethical solution collectively.",
                        "Follow standard protocols and school safety rules strictly.",
                        "Ignore it if it doesn't directly affect my personal score or task."
                    ),
                    dimensionScores = listOf(
                        "High Civic Duty & Proactive Integrity",
                        "Collaborative Responsibility & Values",
                        "Standard Rule-Compliance",
                        "Passive Individualism"
                    )
                )
            )
        }

        // 4. Learning Style templates (111 questions)
        for (i in 1..111) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "When studying a highly complex scientific or logical process $context, what tool helps you most?"
                1 -> "In what type of educational classroom setup are you most active and engaged $context?"
                2 -> "If you need to teach a difficult concept to a junior student $context, how do you explain it?"
                3 -> "What is your go-to method for reviewing notes and preparing for exams $context?"
                4 -> "When exploring a brand new hobby or elective topic $context, how do you start?"
                else -> "Which form of resource material makes you feel most confident $context?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Learning Style",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Studying detailed mind maps, visual flowcharts, and colorful diagrams.",
                        "Listening to audio files, lively discussions, and peer explanations.",
                        "Reading printed textbooks, writing summaries, and practicing written exercises.",
                        "Building 3D models, conducting tactile experiments, and learning by doing."
                    ),
                    dimensionScores = listOf(
                        "Visual Learner (Spatial)",
                        "Auditory Learner (Verbal)",
                        "Read/Write Learner (Textual)",
                        "Kinesthetic Learner (Practical)"
                    )
                )
            )
        }

        // 5. Aptitude & Logical Thinking templates (111 questions)
        for (i in 1..111) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "How do you approach a complex problem with multiple variables $context?"
                1 -> "If a logical pattern or math equation doesn't add up $context, what is your methodology?"
                2 -> "When assembling or analyzing structural details $context, how do you proceed?"
                3 -> "How do you evaluate arguments and evidence $context?"
                4 -> "When faced with riddles, code, or pattern sequences $context, what is your mindset?"
                else -> "If you need to make a fast prediction based on incomplete data $context, how do you do it?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Aptitude & Logical Thinking",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Break the problem down step-by-step using deductive logic and flow charts.",
                        "Examine patterns and look up similar solved examples to find a template.",
                        "Experiment with different values intuitively to observe changes in real-time.",
                        "Collaborate with an expert peer or teacher to analyze the proof together."
                    ),
                    dimensionScores = listOf(
                        "High Deductive Logic & Analytics",
                        "Pattern Recognition & Reference",
                        "Experimental & Intuitive Logic",
                        "Collaborative Logic Guidance"
                    )
                )
            )
        }

        // 6. Leadership & Communication templates (111 questions)
        for (i in 1..111) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "How do you guide a group of students to complete a collaborative task $context?"
                1 -> "When presenting a major topic to a large class audience $context, what is your primary focus?"
                2 -> "How do you handle team disagreements or conflicting priorities $context?"
                3 -> "When delegating project responsibilities $context, what strategy do you apply?"
                4 -> "How do you motivate a classmate who seems disengaged $context?"
                else -> "In what way do you deliver constructive feedback to group members $context?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Leadership & Communication",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Listen to all viewpoints first and design a collaborative compromise.",
                        "Set clear deadlines, create detailed progress checklists, and assign roles.",
                        "Deliver an energetic, visionary presentation to inspire action.",
                        "Provide supportive check-ins and lead by hard-working personal example."
                    ),
                    dimensionScores = listOf(
                        "Diplomatic & Collaborative Leadership",
                        "Transactional Task-Management",
                        "Charismatic & Visionary Communication",
                        "Supportive & Pacesetting Leadership"
                    )
                )
            )
        }

        // 7. Career Interests templates (111 questions)
        for (i in 1..111) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "Which of the following professional projects would you find most fulfilling $context?"
                1 -> "If you could shadow a highly accomplished leader for a day $context, who would you choose?"
                2 -> "When exploring future industries $context, which domain excites you most?"
                3 -> "What kind of problem do you hope to solve in your future career $context?"
                4 -> "Which university major or practical vocational stream appeals to you $context?"
                else -> "How would you prefer to contribute to a major societal challenge $context?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Career Interests",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Analyzing scientific data, coding software, or researching medical cures.",
                        "Designing digital media, writing creative literature, or composing music.",
                        "Counseling people, teaching students, or running community welfare campaigns.",
                        "Launching commercial startups, marketing products, or managing finance portfolios."
                    ),
                    dimensionScores = listOf(
                        "Investigative (Science, Tech & Research)",
                        "Artistic (Creative Arts & Media)",
                        "Social (Education, Counseling & Welfare)",
                        "Enterprising (Business, Venture & Finance)"
                    )
                )
            )
        }

        // 8. Entrepreneurship & Innovation templates (111 questions)
        for (i in 1..111) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "When you discover an inefficient process $context, how do you respond?"
                1 -> "How do you feel when faced with a task that has zero templates or instructions $context?"
                2 -> "If you were to organize a student-led micro-business or fundraiser $context, what is your focus?"
                3 -> "When a creative idea you suggested fails or is rejected $context, how do you iterate?"
                4 -> "What is your attitude toward taking calculated risks $context?"
                else -> "How do you define a successful innovation $context?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Entrepreneurship & Innovation",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Design an innovative digital ordering system or automated workflow.",
                        "Formulate a structured financial budget and business plan to scale it.",
                        "Enjoy the freedom to explore completely unguided, creative paths.",
                        "Find a reliable, existing solution and adapt it with minor modifications."
                    ),
                    dimensionScores = listOf(
                        "High Technological Innovation",
                        "Strategic Business & Financial Innovation",
                        "High Risk-Tolerance & Creativity",
                        "Pragmatic & Incremental Innovation"
                    )
                )
            )
        }

        // 9. Digital Behaviour & Ethics templates (112 questions to reach exactly 1,000 total questions)
        for (i in 1..112) {
            val context = schoolContexts[i % schoolContexts.size]
            val questionText = when (i % 6) {
                0 -> "You notice a malicious cyber-bullying post on a public forum $context. What do you do?"
                1 -> "When sharing files, photos, or comments online $context, what is your top consideration?"
                2 -> "If an unfamiliar portal asks for private personal data to download a guide $context, how do you react?"
                3 -> "How do you verify the credibility of research information found online $context?"
                4 -> "What is your approach to maintaining a healthy screen-time balance $context?"
                else -> "When collaborating on shared online documents $context, how do you practice digital respect?"
            }

            questions.add(
                PsychometricQuestion(
                    id = globalId++,
                    dimension = "Digital Behaviour & Ethics",
                    question = "[$i] $questionText",
                    options = listOf(
                        "Report the cyber threat to moderators and check on the victim's welfare privately.",
                        "Carefully evaluate my digital footprint and check privacy parameters.",
                        "Enter fake or secondary details to protect my real-world identity.",
                        "Ask a supervisor, parent, or trusted adult if the platform is legally secure."
                    ),
                    dimensionScores = listOf(
                        "High Digital Ethics & Empathy",
                        "High Security & Footprint Awareness",
                        "Pragmatic Cyber-Defense Action",
                        "Dependent & Consultative Digital Safety"
                    )
                )
            )
        }

        return questions
    }
}
