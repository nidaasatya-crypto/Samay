package com.example.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

    fun generateReportPdf(
        context: Context,
        report: PsychometricReport,
        studentName: String,
        counselorNotes: String?
    ): ByteArray {
        val pdfDocument = PdfDocument()
        val pageWidth = 595 // A4 width in points
        val pageHeight = 842 // A4 height in points
        
        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var currentPage = pdfDocument.startPage(pageInfo)
        var canvas = currentPage.canvas
        
        // Let's keep track of current y position
        var currentY = 50f
        val marginX = 45f
        val printableWidth = pageWidth - (2 * marginX)
        
        val textPaint = TextPaint().apply {
            isAntiAlias = true
            color = android.graphics.Color.BLACK
        }
        
        val paint = Paint().apply {
            isAntiAlias = true
        }
        
        // Helper to check if elements fit, otherwise create a new page
        fun ensureSpace(neededHeight: Float) {
            if (currentY + neededHeight > pageHeight - 60f) {
                // Finish current page
                pdfDocument.finishPage(currentPage)
                
                // Start new page
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                currentPage = pdfDocument.startPage(pageInfo)
                canvas = currentPage.canvas
                
                // Draw new page header
                drawPageHeader(canvas, pageWidth, pageNumber, studentName)
                currentY = 70f
            }
        }
        
        // Draw the main document header on page 1
        drawMainHeader(canvas, report, studentName, pageWidth)
        currentY = 190f // Below header block
        
        // Section 1: Identified Key Strengths
        ensureSpace(120f)
        currentY = drawSectionCard(
            canvas, 
            title = "IDENTIFIED KEY STRENGTHS", 
            content = report.strengths, 
            x = marginX, 
            y = currentY, 
            width = printableWidth, 
            primaryColor = 0xFF1E8E3E.toInt(), // RepublicTeal
            textPaint = textPaint,
            paint = paint,
            ensureSpace = ::ensureSpace
        )
        
        currentY += 15f
        
        // Section 2: Dominant Learning Style Profile
        ensureSpace(120f)
        currentY = drawSectionCard(
            canvas, 
            title = "DOMINANT LEARNING STYLE PROFILE", 
            content = report.learningStyle, 
            x = marginX, 
            y = currentY, 
            width = printableWidth, 
            primaryColor = 0xFFF27D26.toInt(), // RepublicOrange
            textPaint = textPaint,
            paint = paint,
            ensureSpace = ::ensureSpace
        )
        
        currentY += 15f
        
        // Section 3: Recommended Stream (Highlight banner)
        ensureSpace(110f)
        currentY = drawHighlightBanner(
            canvas,
            title = "RECOMMENDED SECONDARY EDUCATION STREAM",
            value = report.recommendedStream,
            description = "This academic pathway aligns ideally with the student's pattern deduction abilities, analytical logic focus, and career interest indicators.",
            x = marginX,
            y = currentY,
            width = printableWidth,
            primaryColor = 0xFF1A73E8.toInt(), // RepublicBlue
            accentColor = 0xFFF27D26.toInt(), // RepublicOrange
            textPaint = textPaint,
            paint = paint
        )
        
        currentY += 15f
        
        // Section 4: Compatible Career Interest Clusters
        ensureSpace(120f)
        currentY = drawSectionCard(
            canvas, 
            title = "COMPATIBLE CAREER INTEREST CLUSTERS", 
            content = report.careerClusters, 
            x = marginX, 
            y = currentY, 
            width = printableWidth, 
            primaryColor = 0xFF1A73E8.toInt(), // RepublicBlue
            textPaint = textPaint,
            paint = paint,
            ensureSpace = ::ensureSpace
        )
        
        currentY += 15f
        
        // Split fullReportJson for Behavioral Insights and Soft Skills
        val parts = report.fullReportJson.split("||")
        val behaviouralInsights = parts.getOrNull(0) ?: ""
        val skillsToDevelop = parts.getOrNull(1) ?: ""
        
        // Section 5: Behavioural Insights & Values Quotient
        if (behaviouralInsights.isNotEmpty()) {
            ensureSpace(120f)
            currentY = drawSectionCard(
                canvas, 
                title = "BEHAVIOURAL INSIGHTS & VALUES QUOTIENT", 
                content = behaviouralInsights, 
                x = marginX, 
                y = currentY, 
                width = printableWidth, 
                primaryColor = 0xFF1A73E8.toInt(), // RepublicBlue
                textPaint = textPaint,
                paint = paint,
                ensureSpace = ::ensureSpace
            )
            currentY += 15f
        }
        
        // Section 6: Recommended Skill Development Pathways
        if (skillsToDevelop.isNotEmpty()) {
            ensureSpace(100f)
            val cleanSkills = skillsToDevelop.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .joinToString(" • ")
                
            currentY = drawSectionCard(
                canvas, 
                title = "RECOMMENDED SKILL DEVELOPMENT PATHWAYS", 
                content = cleanSkills, 
                x = marginX, 
                y = currentY, 
                width = printableWidth, 
                primaryColor = 0xFF1E8E3E.toInt(), // RepublicTeal
                textPaint = textPaint,
                paint = paint,
                ensureSpace = ::ensureSpace
            )
            currentY += 15f
        }
        
        // Section 7: School Counselor Clinical Notes
        if (!counselorNotes.isNullOrEmpty()) {
            ensureSpace(120f)
            currentY = drawSectionCard(
                canvas, 
                title = "PROFESSIONAL COUNSELOR RECOMMENDATIONS", 
                content = counselorNotes, 
                x = marginX, 
                y = currentY, 
                width = printableWidth, 
                primaryColor = 0xFF1E8E3E.toInt(), // RepublicTeal
                textPaint = textPaint,
                paint = paint,
                ensureSpace = ::ensureSpace
            )
            currentY += 15f
        }
        
        // Section 8: Legal Disclaimer & Official Notice
        ensureSpace(90f)
        currentY = drawDisclaimerCard(
            canvas,
            x = marginX,
            y = currentY,
            width = printableWidth,
            textPaint = textPaint,
            paint = paint
        )
        
        // Draw page numbers and footers on all finished pages
        pdfDocument.finishPage(currentPage)
        
        // Output stream
        val outputStream = ByteArrayOutputStream()
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        
        return outputStream.toByteArray()
    }
    
    private fun drawMainHeader(canvas: Canvas, report: PsychometricReport, studentName: String, pageWidth: Int) {
        val paint = Paint().apply { isAntiAlias = true }
        val textPaint = TextPaint().apply { isAntiAlias = true }
        
        // Draw Header Blue Background Bar
        paint.color = 0xFF1A73E8.toInt() // RepublicBlue
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), 12f, paint)
        
        // Sub-Branding text
        textPaint.color = 0xFFF27D26.toInt() // RepublicOrange
        textPaint.textSize = 10f
        textPaint.isFakeBoldText = true
        canvas.drawText("REPUBLIC STUDENT PUBLICATIONS", 45f, 40f, textPaint)
        
        // Title
        textPaint.color = 0xFF1A73E8.toInt() // RepublicBlue
        textPaint.textSize = 20f
        textPaint.isFakeBoldText = true
        canvas.drawText("STUDENT DEVELOPMENT ASSESSMENT", 45f, 65f, textPaint)
        
        textPaint.color = android.graphics.Color.DKGRAY
        textPaint.textSize = 12f
        textPaint.isFakeBoldText = false
        canvas.drawText("Comprehensive Aptitude & Guidance Report Card", 45f, 82f, textPaint)
        
        // Divider line
        paint.color = 0xFFE0E0E0.toInt()
        canvas.drawLine(45f, 95f, (pageWidth - 45).toFloat(), 95f, paint)
        
        // Student Meta Box
        paint.color = 0xFFF7F9FC.toInt()
        val rect = RectF(45f, 105f, (pageWidth - 45).toFloat(), 175f)
        canvas.drawRoundRect(rect, 8f, 8f, paint)
        
        // Draw Meta fields
        textPaint.color = android.graphics.Color.BLACK
        textPaint.textSize = 11f
        
        // Left Column
        textPaint.isFakeBoldText = true
        canvas.drawText("Student Name:", 60f, 128f, textPaint)
        textPaint.isFakeBoldText = false
        canvas.drawText(studentName, 150f, 128f, textPaint)
        
        textPaint.isFakeBoldText = true
        canvas.drawText("Grade Level:", 60f, 148f, textPaint)
        textPaint.isFakeBoldText = false
        canvas.drawText("Class 8", 150f, 148f, textPaint)
        
        textPaint.isFakeBoldText = true
        canvas.drawText("Academic Cycle:", 60f, 164f, textPaint)
        textPaint.isFakeBoldText = false
        canvas.drawText("${report.year} Calendar", 150f, 164f, textPaint)
        
        // Right Column
        val rightColX = 330f
        val dateStr = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(report.timestamp))
        textPaint.isFakeBoldText = true
        canvas.drawText("Evaluation Date:", rightColX, 128f, textPaint)
        textPaint.isFakeBoldText = false
        canvas.drawText(dateStr, rightColX + 100f, 128f, textPaint)
        
        textPaint.isFakeBoldText = true
        canvas.drawText("Assessment ID:", rightColX, 148f, textPaint)
        textPaint.isFakeBoldText = false
        canvas.drawText("RE-PSA-${report.id.toString().padStart(4, '0')}", rightColX + 100f, 148f, textPaint)
        
        textPaint.isFakeBoldText = true
        canvas.drawText("Status:", rightColX, 164f, textPaint)
        textPaint.color = 0xFF1E8E3E.toInt() // RepublicTeal
        canvas.drawText("OFFICIALLY COMPLETED", rightColX + 100f, 164f, textPaint)
    }
    
    private fun drawPageHeader(canvas: Canvas, pageWidth: Int, pageNumber: Int, studentName: String) {
        val paint = Paint().apply { isAntiAlias = true }
        val textPaint = TextPaint().apply { isAntiAlias = true }
        
        // Small top blue accent
        paint.color = 0xFF1A73E8.toInt()
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), 6f, paint)
        
        textPaint.color = android.graphics.Color.GRAY
        textPaint.textSize = 9f
        textPaint.isFakeBoldText = false
        canvas.drawText("Psychometric Aptitude Report • $studentName", 45f, 30f, textPaint)
        canvas.drawText("Page $pageNumber", (pageWidth - 85).toFloat(), 30f, textPaint)
        
        paint.color = 0xFFEEEEEE.toInt()
        canvas.drawLine(45f, 38f, (pageWidth - 45).toFloat(), 38f, paint)
    }
    
    private fun drawSectionCard(
        canvas: Canvas,
        title: String,
        content: String,
        x: Float,
        y: Float,
        width: Float,
        primaryColor: Int,
        textPaint: TextPaint,
        paint: Paint,
        ensureSpace: (Float) -> Unit
    ): Float {
        // Prepare text paint
        textPaint.color = android.graphics.Color.BLACK
        textPaint.textSize = 10.5f
        textPaint.isFakeBoldText = false
        
        // Calculate static layout to measure height
        val textWidth = (width - 24f).toInt()
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(content, 0, content.length, textPaint, textWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1.2f)
                .setIncludePad(false)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(content, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0f, false)
        }
        
        val headerHeight = 28f
        val padding = 12f
        val totalCardHeight = headerHeight + staticLayout.height + (padding * 2)
        
        // Ensure space is available, if we change pages, this will trigger drawPageHeader and reset Y
        ensureSpace(totalCardHeight)
        
        // Draw background card shadow / border
        paint.color = 0xFFF1F3F4.toInt()
        val cardRect = RectF(x, y, x + width, y + totalCardHeight)
        canvas.drawRoundRect(cardRect, 8f, 8f, paint)
        
        // Draw clean white inner card
        paint.color = android.graphics.Color.WHITE
        val innerRect = RectF(x + 1f, y + 1f, x + width - 1f, y + totalCardHeight - 1f)
        canvas.drawRoundRect(innerRect, 7f, 7f, paint)
        
        // Draw accent top border or banner inside card
        paint.color = primaryColor
        val accentBar = RectF(x + 1f, y + 1f, x + width - 1f, y + headerHeight)
        canvas.drawRoundRect(accentBar, 7f, 7f, paint)
        // clip the rounded corners on the bottom of accentBar by drawing a rectangle over it
        canvas.drawRect(x + 1f, y + headerHeight - 5f, x + width - 1f, y + headerHeight, paint)
        
        // Title text
        textPaint.color = android.graphics.Color.WHITE
        textPaint.textSize = 11f
        textPaint.isFakeBoldText = true
        canvas.drawText(title, x + 12f, y + 18f, textPaint)
        
        // Draw the body content
        textPaint.color = 0xFF202124.toInt()
        textPaint.isFakeBoldText = false
        textPaint.textSize = 10f
        
        canvas.save()
        canvas.translate(x + 12f, y + headerHeight + padding)
        staticLayout.draw(canvas)
        canvas.restore()
        
        return y + totalCardHeight
    }
    
    private fun drawHighlightBanner(
        canvas: Canvas,
        title: String,
        value: String,
        description: String,
        x: Float,
        y: Float,
        width: Float,
        primaryColor: Int,
        accentColor: Int,
        textPaint: TextPaint,
        paint: Paint
    ): Float {
        val totalHeight = 90f
        
        // Draw background
        paint.color = 0xFFF4F8FB.toInt() // light republic blue tint
        val rect = RectF(x, y, x + width, y + totalHeight)
        canvas.drawRoundRect(rect, 8f, 8f, paint)
        
        // Draw border
        paint.color = primaryColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f
        canvas.drawRoundRect(rect, 8f, 8f, paint)
        paint.style = Paint.Style.FILL // reset style
        
        // Draw Title
        textPaint.color = primaryColor
        textPaint.textSize = 9.5f
        textPaint.isFakeBoldText = true
        canvas.drawText(title, x + 16f, y + 22f, textPaint)
        
        // Draw Recommended Value
        textPaint.color = accentColor
        textPaint.textSize = 18f
        textPaint.isFakeBoldText = true
        canvas.drawText(value, x + 16f, y + 46f, textPaint)
        
        // Draw short description
        textPaint.color = android.graphics.Color.DKGRAY
        textPaint.textSize = 9f
        textPaint.isFakeBoldText = false
        
        val textWidth = (width - 32f).toInt()
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(description, 0, description.length, textPaint, textWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1.1f)
                .setIncludePad(false)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(description, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.1f, 0f, false)
        }
        
        canvas.save()
        canvas.translate(x + 16f, y + 54f)
        staticLayout.draw(canvas)
        canvas.restore()
        
        return y + totalHeight
    }
    
    private fun drawDisclaimerCard(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        textPaint: TextPaint,
        paint: Paint
    ): Float {
        val disclaimerText = "Official Counsel Notice: This psychometric report is compiled dynamically by the Republic Student Publications AI Counseling Engine based on self-reported scientific dimension indicators. It is not a medical or psychological diagnosis and is intended strictly as an educational pathway reference. Academic streams and career paths should be verified via personal consultations with teachers and counselors."
        
        textPaint.color = android.graphics.Color.GRAY
        textPaint.textSize = 8f
        textPaint.isFakeBoldText = false
        
        val textWidth = (width - 24f).toInt()
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(disclaimerText, 0, disclaimerText.length, textPaint, textWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1.15f)
                .setIncludePad(false)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(disclaimerText, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.15f, 0f, false)
        }
        
        val totalHeight = staticLayout.height + 24f
        
        // Background soft grey box
        paint.color = 0xFFFAF8F5.toInt() // warm sand tint
        val rect = RectF(x, y, x + width, y + totalHeight)
        canvas.drawRoundRect(rect, 6f, 6f, paint)
        
        canvas.save()
        canvas.translate(x + 12f, y + 12f)
        staticLayout.draw(canvas)
        canvas.restore()
        
        return y + totalHeight
    }

    fun savePdfToDownloads(context: Context, filename: String, pdfBytes: ByteArray): android.net.Uri? {
        var outputStream: java.io.OutputStream? = null
        var uri: android.net.Uri? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = android.content.ContentValues().apply {
                    put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
                }
                val resolver = context.contentResolver
                uri = resolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    outputStream = resolver.openOutputStream(uri)
                }
            } else {
                val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                val file = java.io.File(downloadsDir, filename)
                outputStream = java.io.FileOutputStream(file)
                uri = android.net.Uri.fromFile(file)
            }
            outputStream?.use { it.write(pdfBytes) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return uri
    }
}
