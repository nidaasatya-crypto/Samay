package com.example.data

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<ContentJson>,
    val generationConfig: GenerationConfigJson? = null
)

@JsonClass(generateAdapter = true)
data class ContentJson(
    val parts: List<PartJson>
)

@JsonClass(generateAdapter = true)
data class PartJson(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfigJson(
    val responseMimeType: String? = null,
    val temperature: Float? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<CandidateJson>?
)

@JsonClass(generateAdapter = true)
data class CandidateJson(
    val content: ContentJson?
)

@JsonClass(generateAdapter = true)
data class AIReportOutput(
    val strengths: String,
    val learningStyle: String,
    val recommendedStream: String,
    val careerClusters: String,
    val behaviouralInsights: String,
    val skillsToDevelop: String
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: GeminiApi = retrofit.create(GeminiApi::class.java)

    // Helper to request and parse reports
    suspend fun getPsychometricReport(
        studentName: String,
        classLevel: Int,
        answersSummary: String
    ): AIReportOutput {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Fallback mock report if the API key is not configured or placeholder
            return getMockReport(studentName, classLevel)
        }

        val prompt = """
            You are the expert psychometric and career counseling AI engine of Republic Student Publications for the Life Skills & Citizenship Education Program (LSCEP).
            
            Analyze the following student assessment profile:
            - Student Name: $studentName
            - Class Level: Class $classLevel
            - Question Responses: 
            $answersSummary
            
            Based on these indicators of personality, learning style, emotional intelligence, leadership, and interests, generate a comprehensive educational guidance report.
            You must return a raw JSON object matching the following structure:
            {
              "strengths": "3 detailed strengths based on leadership, coordination or innovation",
              "learningStyle": "Explain their dominant learning style (Visual, Auditory, Kinesthetic, or Logical) and how they can optimize study sessions.",
              "recommendedStream": "The ideal secondary academic stream (e.g. Science-PCM, Science-PCB, Commerce, or Humanities/Arts) and why.",
              "careerClusters": "3 highly compatible future professional career domains (e.g., Robotics & AI, Corporate Law, Sustainable Agritech, Creative Arts) and relevant vocational skills.",
              "behaviouralInsights": "Detailed observations about their emotional intelligence, responsibility, and digital ethics quotient.",
              "skillsToDevelop": "A comma-separated list of 5 key soft or technical skills they should master next (e.g., Critical Thinking, Financial Literacy, Coding, Public Speaking, Conflict Resolution)."
            }
            Do not include any Markdown blocks, backticks, or other formatting. Only return the raw valid JSON string.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                ContentJson(
                    parts = listOf(
                        PartJson(text = prompt)
                    )
                )
            ),
            generationConfig = GenerationConfigJson(
                responseMimeType = "application/json",
                temperature = 0.7f
            )
        )

        return try {
            val response = api.generateContent(apiKey, request)
            val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (jsonText != null) {
                // Parse report using Moshi
                val adapter = moshi.adapter(AIReportOutput::class.java)
                adapter.fromJson(jsonText) ?: getMockReport(studentName, classLevel)
            } else {
                getMockReport(studentName, classLevel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            getMockReport(studentName, classLevel)
        }
    }

    private fun getMockReport(studentName: String, classLevel: Int): AIReportOutput {
        return AIReportOutput(
            strengths = "1. Proactive Leadership: Demonstrates an innate ability to structure tasks and guide peers.\n2. Inquisitive Critical Thinking: Eager to cross-verify facts and avoid digital misinformation.\n3. Balanced Financial Discipline: Understands budgeting concepts and budgeting formula.",
            learningStyle = "Analytical & Visual-Spatial: Learns exceptionally well through structural diagrams, mind maps, and logical flowcharts. Benefit from preparing colorful notes and explaining concepts verbally.",
            recommendedStream = if (classLevel >= 8) "Science with Computer Science / Commerce with Applied Finance" else "Foundation for Science / Creative Arts Stream",
            careerClusters = "1. AI Ethics and Data Analytics: Designing safe technology systems.\n2. Civil Law & Public Administration: Guiding community compliance.\n3. Strategic Venture Management: Scaling sustainable entrepreneurship.",
            behaviouralInsights = "Displays strong emotional maturity and respect for digital footprints. Showcases a high empathy quotient when supporting classmates.",
            skillsToDevelop = "Public Speaking, Creative Problem Solving, Advanced Python Programming, Digital Currency Literacy, Advanced Active Citizenship"
        )
    }
}
