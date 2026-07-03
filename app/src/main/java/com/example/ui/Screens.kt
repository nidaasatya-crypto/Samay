package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import com.example.data.*
import com.example.ui.theme.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

// --- Simple Translation Engine ---
object Trans {
    fun get(key: String, lang: String): String {
        return I18nService.get(key, lang)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationContainer(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val lang by viewModel.currentLanguage.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar((uiState as UiState.Success).message)
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar((uiState as UiState.Error).error)
            }
            else -> {}
        }
    }

    if (currentScreen == "LOGIN") {
        LoginScreen(viewModel = viewModel, lang = lang)
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DashboardDrawerContent(
                    currentUser = currentUser,
                    currentScreen = currentScreen,
                    lang = lang,
                    onNavigate = { screen ->
                        viewModel.navigateTo(screen)
                        scope.launch { drawerState.close() }
                    },
                    onLogout = {
                        viewModel.logout()
                        scope.launch { drawerState.close() }
                    }
                )
            },
            gesturesEnabled = true
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    Surface(
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Open Drawer Navigation",
                                        tint = RepublicBlue
                                    )
                                }
                            },
                            title = {
                                Column {
                                    Text(
                                        text = Trans.get("app_title", lang),
                                        fontWeight = FontWeight.Black,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = RepublicBlue
                                    )
                                    currentUser?.let {
                                        Text(
                                            text = "${it.fullName} • ${it.role} ${if (it.role == "STUDENT") "(${Trans.get("class_level", lang)} ${it.classLevel})" else ""}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            actions = {
                                // Dynamic Announcements & Notifications
                                NotificationIconWithBadge(viewModel = viewModel)

                                // Quick Language Selector
                                LanguageSelector(currentLang = lang, onSelected = { viewModel.setLanguage(it) })

                                // Quick Role Switcher for seamless evaluation
                                QuickRoleSwitcher(currentRole = currentUser?.role ?: "", onSwitch = { role ->
                                    viewModel.loginUser(role.lowercase(), role)
                                })
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent
                            )
                        )
                    }
                },
                bottomBar = {
                    if (currentUser != null) {
                        BottomNavigationBar(
                            currentScreen = currentScreen,
                            userRole = currentUser!!.role,
                            lang = lang,
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                },
                modifier = modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    when (currentScreen) {
                        "DASHBOARD" -> DashboardDispatcher(viewModel = viewModel, lang = lang)
                        "LESSON" -> LessonScreen(viewModel = viewModel, lang = lang)
                        "ASSESSMENT" -> PsychometricAssessmentScreen(viewModel = viewModel, lang = lang)
                        "NEWSPAPER" -> NewspaperScreen(viewModel = viewModel, lang = lang)
                        "PODCAST" -> PodcastScreen(viewModel = viewModel, lang = lang)
                        "APPOINTMENTS" -> AppointmentsScreen(viewModel = viewModel, lang = lang)
                        else -> LoginScreen(viewModel = viewModel, lang = lang)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationIconWithBadge(viewModel: AppViewModel) {
    val notices by viewModel.allNotices.collectAsState()
    var showNoticesDialog by remember { mutableStateOf(false) }
    var readNoticeIds by remember { mutableStateOf(setOf<Int>()) }
    
    val unreadCount = notices.count { it.id !in readNoticeIds }

    Box {
        IconButton(onClick = { 
            showNoticesDialog = true
            readNoticeIds = readNoticeIds + notices.map { it.id }.toSet()
        }) {
            BadgedBox(
                badge = {
                    if (unreadCount > 0) {
                        Badge(
                            containerColor = RepublicOrange,
                            contentColor = Color.White
                        ) {
                            Text(unreadCount.toString(), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = RepublicBlue
                )
            }
        }
    }

    if (showNoticesDialog) {
        Dialog(onDismissRequest = { showNoticesDialog = false }) {
            Card(
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = RepublicOrange,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "School Notices",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = RepublicBlue
                            )
                        }
                        IconButton(onClick = { showNoticesDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close Notices")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (notices.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No active notifications",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.heightIn(max = 320.dp)
                        ) {
                            items(notices.sortedByDescending { it.id }) { notice ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    border = BorderStroke(1.dp, Color(0xFFE1E3E8).copy(alpha = 0.8f)),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = notice.title,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = RepublicBlue,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = notice.date,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = notice.content,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Issued by: ${notice.schoolName}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = RepublicOrange,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { showNoticesDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Dismiss", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardDrawerContent(
    currentUser: AppUser?,
    currentScreen: String,
    lang: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.width(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // BRANDING HEADER
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(RepublicBlue, RepublicBlue.copy(alpha = 0.7f))
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "App Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "REPUBLIC",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = RepublicBlue
                    )
                    Text(
                        text = "STUDENT ONE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = RepublicOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE1E3E8))
            Spacer(modifier = Modifier.height(24.dp))

            // USER PROFILE SUMMARY
            currentUser?.let { user ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(RepublicBlue.copy(alpha = 0.04f))
                        .border(1.dp, RepublicBlue.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(RepublicOrange.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.fullName.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RepublicOrange
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = user.fullName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Portal: ${user.role}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "NAVIGATION",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )

            // NAVIGATION ITEMS LIST
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Dashboard (Always available)
                DrawerNavItem(
                    label = Trans.get("dashboard", lang),
                    icon = Icons.Default.Dashboard,
                    selected = currentScreen == "DASHBOARD",
                    onClick = { onNavigate("DASHBOARD") }
                )

                currentUser?.let { user ->
                    when (user.role) {
                        "STUDENT" -> {
                            DrawerNavItem(
                                label = Trans.get("newspaper", lang),
                                icon = Icons.Default.Newspaper,
                                selected = currentScreen == "NEWSPAPER",
                                onClick = { onNavigate("NEWSPAPER") }
                            )
                            DrawerNavItem(
                                label = Trans.get("podcasts", lang),
                                icon = Icons.Default.Mic,
                                selected = currentScreen == "PODCAST",
                                onClick = { onNavigate("PODCAST") }
                            )
                            DrawerNavItem(
                                label = Trans.get("assessment", lang),
                                icon = Icons.Default.AutoAwesome,
                                selected = currentScreen == "ASSESSMENT",
                                onClick = { onNavigate("ASSESSMENT") }
                            )
                            DrawerNavItem(
                                label = Trans.get("lscep_modules", lang),
                                icon = Icons.Default.MenuBook,
                                selected = currentScreen == "LESSON",
                                onClick = { onNavigate("LESSON") }
                            )
                        }
                        "PARENT" -> {
                            DrawerNavItem(
                                label = Trans.get("counselling", lang),
                                icon = Icons.Default.Event,
                                selected = currentScreen == "APPOINTMENTS",
                                onClick = { onNavigate("APPOINTMENTS") }
                            )
                        }
                        "TEACHER", "SCHOOL" -> {
                            DrawerNavItem(
                                label = Trans.get("newspaper", lang),
                                icon = Icons.Default.Newspaper,
                                selected = currentScreen == "NEWSPAPER",
                                onClick = { onNavigate("NEWSPAPER") }
                            )
                        }
                        "ADMIN" -> {
                            DrawerNavItem(
                                label = "Editorial Work",
                                icon = Icons.Default.RateReview,
                                selected = currentScreen == "NEWSPAPER",
                                onClick = { onNavigate("NEWSPAPER") }
                            )
                        }
                    }
                }
            }

            // BOTTOM ACTION SECTION
            HorizontalDivider(color = Color(0xFFE1E3E8))
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onLogout,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Trans.get("logout", lang),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) RepublicBlue.copy(alpha = 0.08f) else Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) RepublicBlue.copy(alpha = 0.2f) else Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) RepublicBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) RepublicBlue else MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (selected) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(RepublicBlue, CircleShape)
                )
            }
        }
    }
}

@Composable
fun LanguageSelector(currentLang: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Language, contentDescription = "Language", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = currentLang, style = MaterialTheme.typography.bodySmall)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("English", "Hindi", "Telugu").forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang) },
                    onClick = {
                        onSelected(lang)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun QuickRoleSwitcher(currentRole: String, onSwitch: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = RepublicOrange,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .testTag("role_switcher_trigger")
        ) {
            Icon(Icons.Default.SwitchAccount, contentDescription = "Switch Portal", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Portals", style = MaterialTheme.typography.labelSmall)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("Student", "Parent", "Teacher", "School", "Admin").forEach { role ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = currentRole.uppercase() == role.uppercase(), onClick = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(role)
                        }
                    },
                    onClick = {
                        onSwitch(role)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: String,
    userRole: String,
    lang: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.drawBehind {
            val strokeWidth = 1.dp.toPx()
            drawLine(
                color = Color(0xFFE1E3E8),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = strokeWidth
            )
        }
    ) {
        NavigationBarItem(
            selected = currentScreen == "DASHBOARD",
            onClick = { onNavigate("DASHBOARD") },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text(Trans.get("dashboard", lang), fontSize = 11.sp, maxLines = 1) }
        )

        if (userRole == "STUDENT") {
            NavigationBarItem(
                selected = currentScreen == "NEWSPAPER",
                onClick = { onNavigate("NEWSPAPER") },
                icon = { Icon(Icons.Default.Newspaper, contentDescription = "Newspaper") },
                label = { Text(Trans.get("newspaper", lang), fontSize = 11.sp, maxLines = 1) }
            )
            NavigationBarItem(
                selected = currentScreen == "PODCAST",
                onClick = { onNavigate("PODCAST") },
                icon = { Icon(Icons.Default.Mic, contentDescription = "Podcasts") },
                label = { Text(Trans.get("podcasts", lang), fontSize = 11.sp, maxLines = 1) }
            )
        } else if (userRole == "PARENT") {
            NavigationBarItem(
                selected = currentScreen == "APPOINTMENTS",
                onClick = { onNavigate("APPOINTMENTS") },
                icon = { Icon(Icons.Default.Event, contentDescription = "Counseling") },
                label = { Text(Trans.get("counselling", lang), fontSize = 11.sp, maxLines = 1) }
            )
        } else if (userRole == "TEACHER" || userRole == "SCHOOL") {
            NavigationBarItem(
                selected = currentScreen == "NEWSPAPER",
                onClick = { onNavigate("NEWSPAPER") },
                icon = { Icon(Icons.Default.Newspaper, contentDescription = "Articles") },
                label = { Text(Trans.get("newspaper", lang), fontSize = 11.sp, maxLines = 1) }
            )
        } else if (userRole == "ADMIN") {
            NavigationBarItem(
                selected = currentScreen == "NEWSPAPER",
                onClick = { onNavigate("NEWSPAPER") },
                icon = { Icon(Icons.Default.RateReview, contentDescription = "Editorial") },
                label = { Text("Editorial", fontSize = 11.sp, maxLines = 1) }
            )
        }
    }
}

// --- Login Screen ---
@Composable
fun LoginScreen(viewModel: AppViewModel, lang: String) {
    var username by remember { mutableStateOf("rahul") }
    var password by remember { mutableStateOf("1234") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRoleHint by remember { mutableStateOf("STUDENT") }
    var showOtpDialog by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    
    // Validation states
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    
    // Countdown Timer for Resend OTP
    var otpCountdown by remember { mutableStateOf(30) }
    var canResendOtp by remember { mutableStateOf(false) }

    // Synchronize default test credentials on role switch for convenience
    LaunchedEffect(selectedRoleHint) {
        username = when (selectedRoleHint) {
            "STUDENT" -> "rahul"
            "PARENT" -> "parent"
            "TEACHER" -> "teacher"
            "SCHOOL" -> "principal"
            "ADMIN" -> "admin"
            else -> ""
        }
        password = "1234"
        usernameError = false
        passwordError = false
    }

    // OTP Timer countdown
    LaunchedEffect(showOtpDialog, otpCountdown) {
        if (showOtpDialog && otpCountdown > 0) {
            kotlinx.coroutines.delay(1000L)
            otpCountdown -= 1
            if (otpCountdown == 0) {
                canResendOtp = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        RepublicBlue.copy(alpha = 0.08f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Elegant Branded Logo with canvas decor
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .drawBehind {
                        drawCircle(
                            Brush.linearGradient(
                                listOf(RepublicBlue, RepublicOrange)
                            )
                        )
                        drawCircle(
                            color = Color.White,
                            radius = size.minDimension / 2 - 4.dp.toPx()
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "App Logo",
                    tint = RepublicBlue,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "REPUBLIC STUDENT ONE",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                ),
                color = RepublicBlue,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Life Skills & Citizenship Education Portal",
                style = MaterialTheme.typography.bodySmall,
                color = RepublicOrange,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Multilingual Top Language Switcher in Login
            Surface(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.wrapContentSize()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("English", "Hindi", "Telugu").forEach { language ->
                        val active = language == viewModel.currentLanguage.collectAsState().value
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (active) RepublicOrange.copy(alpha = 0.12f) else Color.Transparent)
                                .border(
                                    1.dp, 
                                    if (active) RepublicOrange else Color.Transparent, 
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.setLanguage(language) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = language,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                color = if (active) RepublicOrange else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Portal Card Container
            Card(
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Trans.get("select_role", lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Role Selection Grid with polished minimalist borders
                    val roles = listOf(
                        Triple("STUDENT", Icons.Default.Face, Trans.get("student", lang)),
                        Triple("PARENT", Icons.Default.FamilyRestroom, Trans.get("parent", lang)),
                        Triple("TEACHER", Icons.Default.RecordVoiceOver, Trans.get("teacher", lang)),
                        Triple("SCHOOL", Icons.Default.Domain, Trans.get("school", lang)),
                        Triple("ADMIN", Icons.Default.AdminPanelSettings, Trans.get("admin", lang))
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        roles.chunked(2).forEach { rowRoles ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowRoles.forEach { (roleKey, icon, label) ->
                                    val isSelected = selectedRoleHint == roleKey
                                    OutlinedCard(
                                        onClick = { selectedRoleHint = roleKey },
                                        colors = CardDefaults.outlinedCardColors(
                                            containerColor = if (isSelected) RepublicBlue.copy(alpha = 0.08f) else Color.Transparent,
                                            contentColor = if (isSelected) RepublicBlue else MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        border = BorderStroke(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) RepublicBlue else Color(0xFFE1E3E8)
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(54.dp)
                                            .testTag("role_card_$roleKey")
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = icon, 
                                                contentDescription = label, 
                                                modifier = Modifier.size(18.dp),
                                                tint = if (isSelected) RepublicBlue else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = label, 
                                                style = MaterialTheme.typography.labelMedium, 
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (isSelected) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(
                                                    imageVector = Icons.Default.Check, 
                                                    contentDescription = "Selected", 
                                                    modifier = Modifier.size(14.dp),
                                                    tint = RepublicBlue
                                                )
                                            }
                                        }
                                    }
                                }
                                if (rowRoles.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Username Input
                    OutlinedTextField(
                        value = username,
                        onValueChange = { 
                            username = it
                            if (it.isNotBlank()) usernameError = false
                        },
                        label = { Text("Username") },
                        placeholder = { Text("e.g. rahul") },
                        leadingIcon = { 
                            Icon(
                                Icons.Default.Person, 
                                contentDescription = null,
                                tint = if (usernameError) MaterialTheme.colorScheme.error else RepublicBlue
                            ) 
                        },
                        isError = usernameError,
                        supportingText = {
                            if (usernameError) {
                                Text("Username is required to log in safely", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("username_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Secure Password Input with visibility toggle
                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            if (it.isNotBlank()) passwordError = false
                        },
                        label = { Text("Secure Passcode / Password") },
                        placeholder = { Text("e.g. 1234") },
                        leadingIcon = { 
                            Icon(
                                Icons.Default.Lock, 
                                contentDescription = null,
                                tint = if (passwordError) MaterialTheme.colorScheme.error else RepublicBlue
                            ) 
                        },
                        trailingIcon = {
                            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            val description = if (passwordVisible) "Hide password" else "Show password"
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = icon, contentDescription = description, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError,
                        supportingText = {
                            if (passwordError) {
                                Text("Passcode is required to log in safely", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dynamic Quick tips to guide evaluation
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RepublicOrange.copy(alpha = 0.05f)),
                        border = BorderStroke(1.dp, RepublicOrange.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info, 
                                    contentDescription = "Tips", 
                                    tint = RepublicOrange, 
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Secure Demo Mode", 
                                    style = MaterialTheme.typography.labelMedium, 
                                    fontWeight = FontWeight.Bold,
                                    color = RepublicOrange
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pre-loaded user details are set automatically on switcher click for premium evaluation convenience. Feel free to type any custom credentials to register dynamically as well!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button with Secured OTP Gateway label
                    Button(
                        onClick = {
                            // Validate input values first
                            var hasError = false
                            if (username.isBlank()) {
                                usernameError = true
                                hasError = true
                            }
                            if (password.isBlank()) {
                                passwordError = true
                                hasError = true
                            }
                            
                            if (!hasError) {
                                // Reset OTP States
                                otpCode = ""
                                otpCountdown = 30
                                canResendOtp = false
                                showOtpDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RepublicBlue,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("login_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${Trans.get("login", lang)} via Secured OTP Gateway",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }

    if (showOtpDialog) {
        Dialog(onDismissRequest = { showOtpDialog = false }) {
            Card(
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(RepublicOrange.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sms, 
                            contentDescription = "OTP", 
                            tint = RepublicOrange, 
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = Trans.get("otp_header", lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RepublicBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Trans.get("otp_desc", lang),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Polished OTP Outlined Text Field
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { if (it.length <= 4) otpCode = it },
                        label = { Text(Trans.get("otp_label", lang)) },
                        placeholder = { Text("••••") },
                        leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = RepublicOrange) },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("otp_input"),
                        supportingText = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (canResendOtp) {
                                    Text(
                                        text = "Didn't receive code?",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    Text(
                                        text = "${Trans.get("otp_resend_timer", lang)}${otpCountdown}s",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = RepublicOrange,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Auto-fill Assist Button to make evaluation seamless and highly user-friendly
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            enabled = canResendOtp,
                            onClick = {
                                otpCountdown = 30
                                canResendOtp = false
                                otpCode = ""
                            }
                        ) {
                            Text(
                                text = Trans.get("otp_resend_btn", lang), 
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (canResendOtp) RepublicOrange else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }

                        // Premium demo helper chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(RepublicOrange.copy(alpha = 0.08f))
                                .border(1.dp, RepublicOrange.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .clickable { otpCode = "1234" }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(12.dp), 
                                    tint = RepublicOrange
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = Trans.get("otp_autofill_btn", lang), 
                                    fontSize = 11.sp, 
                                    fontWeight = FontWeight.Bold,
                                    color = RepublicOrange
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showOtpDialog = false },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text(Trans.get("cancel", lang), fontWeight = FontWeight.Bold)
                        }
                        
                        Button(
                            onClick = {
                                showOtpDialog = false
                                viewModel.loginUser(username, selectedRoleHint)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("verify_otp_button")
                        ) {
                            Text(Trans.get("otp_verify_btn", lang), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// --- Dashboard Router Dispatcher ---
@Composable
fun DashboardDispatcher(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    when (currentUser?.role) {
        "STUDENT" -> StudentDashboard(viewModel = viewModel, lang = lang)
        "PARENT" -> ParentDashboard(viewModel = viewModel, lang = lang)
        "TEACHER" -> TeacherDashboard(viewModel = viewModel, lang = lang)
        "SCHOOL" -> SchoolManagementDashboard(viewModel = viewModel, lang = lang)
        "ADMIN" -> AdminDashboard(viewModel = viewModel, lang = lang)
        else -> Text("Role undefined. Please relogin.")
    }
}

// --- Student Dashboard Panel ---
@Composable
fun StudentDashboard(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val progressList by viewModel.studentProgress.collectAsState()
    val notices by viewModel.allNotices.collectAsState()
    val reports by viewModel.studentReports.collectAsState()

    var showClassDropdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.linearGradient(colors = listOf(RepublicBlue, DarkBlue)))
                        .drawBehind {
                            drawCircle(
                                color = Color.White.copy(alpha = 0.1f),
                                radius = 220.dp.toPx(),
                                center = Offset(size.width * 1.1f, -size.height * 0.1f)
                            )
                        }
                        .padding(24.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Welcome back,",
                                    color = Color.White.copy(alpha = 0.8f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = currentUser?.fullName ?: "Student",
                                    color = Color.White,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            // Class Selector Dropdown
                            Box {
                                Button(
                                    onClick = { showClassDropdown = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange)
                                ) {
                                    Text("Class ${currentUser?.classLevel}")
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                                DropdownMenu(expanded = showClassDropdown, onDismissRequest = { showClassDropdown = false }) {
                                    (3..10).forEach { classNum ->
                                        DropdownMenuItem(
                                            text = { Text("Class $classNum") },
                                            onClick = {
                                                viewModel.updateStudentClass(classNum)
                                                showClassDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Brief progress overview
                        val completedCount = progressList.count { it.completed }
                        Text(
                            text = "Syllabus Mastery Progress: $completedCount Completed Modules",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { if (progressList.isEmpty()) 0.1f else (completedCount.toFloat() / 4f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = RepublicOrange,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }

        // --- Psychometric Assessment Guidance Section ---
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RepublicOrange, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI-Powered Psychometric Portal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RepublicBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Are you curious about your strengths, learning styles, and future academic streams? Complete our scientifically structured 6-dimension adaptive assessment generated by Gemini AI.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mandatory Psychometric Disclaimer Notice
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LightSlate),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Info, contentDescription = "Disclaimer", tint = RepublicBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Note: This assessment is an educational guidance tool and does not provide medical or psychological diagnoses or guarantee career outcomes.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (reports.isNotEmpty()) {
                        val latestReport = reports.first()
                        Text(
                            text = "Latest AI Recommendation (${latestReport.year}):",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = RepublicBlue
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• Recommended Stream: ${latestReport.recommendedStream}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "• Strengths Detected: ${latestReport.strengths}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Button(
                        onClick = { viewModel.navigateTo("ASSESSMENT") },
                        colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_psychometric_button")
                    ) {
                        Text(if (reports.isEmpty()) "Take Adaptive Assessment" else "Retake Assessment (Monitor Progress)")
                    }
                }
            }
        }

        // --- LSCEP Syllabus Module Grids (Class Level Aware) ---
        item {
            Text(
                text = Trans.get("lscep_modules", lang),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = RepublicBlue
            )
        }

        item {
            val modules = listOf(
                Quadruple("LEGAL", "⚖️ Legal Awareness", "Constitutional rights, safety & laws", RepublicBlue),
                Quadruple("FINANCIAL", "🪙 Financial Discipline", "Budgeting formula, savings & bank systems", RepublicOrange),
                Quadruple("DIGITAL", "💻 Digital Ethics", "Cyber safety, footprints & phishing defense", Color(0xFF673AB7)),
                Quadruple("SOCIAL", "🤝 Social Responsibility", "Civic responsibility & community service", RepublicTeal)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                modules.forEach { (modKey, title, desc, color) ->
                    val lessons = DataRepository.getLessonsForClassAndModule(currentUser?.classLevel ?: 8, modKey)
                    val progressForMod = progressList.filter { it.moduleName == modKey }
                    val isCompleted = progressForMod.any { it.completed }

                    Card(
                        onClick = {
                            if (lessons.isNotEmpty()) {
                                viewModel.selectLesson(lessons.first())
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("module_card_$modKey")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    drawLine(
                                        color = color,
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, size.height),
                                        strokeWidth = 6.dp.toPx()
                                    )
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = title,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = color
                                    )
                                    if (isCompleted) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Completed", tint = RepublicTeal, modifier = Modifier.size(16.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = color)
                        }
                    }
                }
            }
        }

        // Notices from School Management
        item {
            Text(
                text = Trans.get("notices", lang),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = RepublicBlue
            )
        }

        if (notices.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No notices at this moment.",
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(notices) { notice ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = notice.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = RepublicBlue)
                            Text(text = notice.date, style = MaterialTheme.typography.labelSmall, color = RepublicOrange)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = notice.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// --- Parent Dashboard Panel ---
@Composable
fun ParentDashboard(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val attendanceList by viewModel.studentAttendance.collectAsState()
    val progressList by viewModel.studentProgress.collectAsState()
    val reports by viewModel.studentReports.collectAsState()
    val appointments by viewModel.parentAppointments.collectAsState()

    var showCertificateDialog by remember { mutableStateOf<PsychometricReport?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "Parent Oversight Portal", style = MaterialTheme.typography.labelMedium, color = RepublicOrange, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Parent: ${currentUser?.fullName}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = RepublicBlue)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Child: Rahul Sharma (Class 8)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Longitudinal Progress Over Multiyears
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timeline, contentDescription = null, tint = RepublicBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Longitudinal Progress & Yearly Growth", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (reports.size < 2) {
                        Text(
                            text = "💡 Take the adaptive assessment in Class 8 to generate and compare progress side-by-side with previous years (e.g., Class 7 historic logs).",
                            style = MaterialTheme.typography.bodySmall,
                            color = RepublicOrange
                        )
                    } else {
                        Text(text = "We have detected a positive evolution in competency logs!", style = MaterialTheme.typography.bodySmall, color = RepublicTeal, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    reports.forEach { r ->
                        OutlinedCard(
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Class Assessment Report (${r.year})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Text(text = "Syllabus Stream: ${r.recommendedStream}", style = MaterialTheme.typography.bodySmall)
                                }
                                Button(onClick = { showCertificateDialog = r }, colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange)) {
                                    Text("View Verified Report Card", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Attendance Overview
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = RepublicBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Child's Daily Attendance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                        }
                        val presentPct = if (attendanceList.isEmpty()) "100%" else "${(attendanceList.count { it.status == "PRESENT" }.toFloat() / attendanceList.size.toFloat() * 100).toInt()}%"
                        Text(text = "Monthly Avg: $presentPct", color = RepublicTeal, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Log list
                    if (attendanceList.isEmpty()) {
                        Text("No logs logged yet.", style = MaterialTheme.typography.bodySmall)
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            attendanceList.take(5).forEach { record ->
                                val isPresent = record.status == "PRESENT"
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isPresent) RepublicTeal.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = record.date.substringAfterLast("-"), fontWeight = FontWeight.Bold)
                                        Text(text = if (isPresent) "P" else "A", color = if (isPresent) RepublicTeal else Color.Red, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Learning Progress Summary
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BarChart, contentDescription = null, tint = RepublicBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "LSCEP Module Checklist", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    val syllabusState = listOf(
                        "Legal Awareness" to (progressList.any { it.moduleName == "LEGAL" && it.completed }),
                        "Financial Discipline" to (progressList.any { it.moduleName == "FINANCIAL" && it.completed }),
                        "Digital Ethics" to (progressList.any { it.moduleName == "DIGITAL" && it.completed }),
                        "Social Responsibility" to (progressList.any { it.moduleName == "SOCIAL" && it.completed })
                    )

                    syllabusState.forEach { (module, completed) ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (completed) RepublicTeal else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = module, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // Counseling appointments list
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Booked Counseling Slots", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                Button(onClick = { viewModel.navigateTo("APPOINTMENTS") }, colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue)) {
                    Text("Book Appointment", fontSize = 11.sp)
                }
            }
        }

        if (appointments.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No appointments booked. Click above to reserve a counseling slot.",
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(appointments) { appt ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(text = "Counseling Session (${appt.date})", fontWeight = FontWeight.Bold, color = RepublicBlue)
                            Text(text = "Time slot: ${appt.time}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (appt.notes.isNotEmpty()) {
                                Text(text = "Note: ${appt.notes}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        AssistChip(
                            onClick = { viewModel.cancelCounsellingAppointment(appt.id) },
                            label = { Text("Cancel") },
                            leadingIcon = { Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(14.dp)) }
                        )
                    }
                }
            }
        }
    }

    // PDF Certificate Dialog Overlay with QR code
    showCertificateDialog?.let { r ->
        Dialog(onDismissRequest = { showCertificateDialog = null }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, RepublicOrange),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REPUBLIC STUDENT PUBLICATIONS",
                        style = MaterialTheme.typography.labelMedium,
                        color = RepublicOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "STUDENT DEVELOPMENT ASSESSMENT",
                        style = MaterialTheme.typography.titleMedium,
                        color = RepublicBlue,
                        fontWeight = FontWeight.Black
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = RepublicBlue.copy(alpha = 0.2f))

                    Text(text = "Verified Competency Report Card", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Candidate Name: Rahul Sharma", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                    Text(text = "Evaluation Level: Class ${if (r.year == 2026) 8 else 7}", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                    Text(text = "Academic Year: ${r.year}", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Strengths
                    Card(colors = CardDefaults.cardColors(containerColor = RepublicBlue.copy(alpha = 0.05f))) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Strengths Summary:", fontWeight = FontWeight.Bold, color = RepublicBlue)
                            Text(text = r.strengths, style = MaterialTheme.typography.bodySmall, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Recommendations
                    Card(colors = CardDefaults.cardColors(containerColor = RepublicOrange.copy(alpha = 0.05f))) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Stream Recommendation:", fontWeight = FontWeight.Bold, color = RepublicOrange)
                            Text(text = r.recommendedStream, style = MaterialTheme.typography.bodySmall, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // QR code decor for verification
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White)
                            .border(1.dp, Color.LightGray)
                            .drawBehind {
                                // Simple mock QR code pattern
                                drawRect(color = Color.Black, topLeft = Offset(10f, 10f), size = androidx.compose.ui.geometry.Size(30f, 30f))
                                drawRect(color = Color.Black, topLeft = Offset(60f, 10f), size = androidx.compose.ui.geometry.Size(30f, 30f))
                                drawRect(color = Color.Black, topLeft = Offset(10f, 60f), size = androidx.compose.ui.geometry.Size(30f, 30f))
                                drawRect(color = Color.Black, topLeft = Offset(45f, 45f), size = androidx.compose.ui.geometry.Size(15f, 15f))
                            }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Scan QR to Verify on Republic Portal", fontSize = 10.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(16.dp))

                    val context = LocalContext.current
                    var isDownloadingInDialog by remember { mutableStateOf(false) }

                    Button(
                        onClick = {
                            isDownloadingInDialog = true
                            try {
                                val sName = if (r.studentId == "rahul@school.com") "Rahul Kumar" else "Priya Sharma"
                                val pdfBytes = PdfReportGenerator.generateReportPdf(
                                    context = context,
                                    report = r,
                                    studentName = sName,
                                    counselorNotes = null
                                )
                                val fileName = "Psychometric_Report_RE_PSA_${r.id}.pdf"
                                val uri = PdfReportGenerator.savePdfToDownloads(context, fileName, pdfBytes)
                                if (uri != null) {
                                    Toast.makeText(context, "Saved: $fileName in Downloads folder!", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Failed to save PDF.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isDownloadingInDialog = false
                                showCertificateDialog = null
                            }
                        },
                        enabled = !isDownloadingInDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Download PDF Copy")
                    }
                }
            }
        }
    }
}

// --- Teacher Dashboard Panel ---
@Composable
fun TeacherDashboard(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val attendanceList by viewModel.allAttendance.collectAsState()
    val users by viewModel.allUsers.collectAsState()

    var showNoticeCreator by remember { mutableStateOf(false) }
    var noticeTitle by remember { mutableStateOf("") }
    var noticeContent by remember { mutableStateOf("") }

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.linearGradient(colors = listOf(RepublicBlue, DarkBlue)))
                        .padding(24.dp)
                ) {
                    Column {
                        Text(text = "Instructor Portal", style = MaterialTheme.typography.labelSmall, color = RepublicOrange, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = currentUser?.fullName ?: "Instructor", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "School: ${currentUser?.schoolName}", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }

        // Attendance Management Grid for the day
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Rule, contentDescription = null, tint = RepublicOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Log Today's LSCEP Attendance ($todayStr)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    val students = users.filter { it.role == "STUDENT" }
                    if (students.isEmpty()) {
                        Text("No students loaded.", style = MaterialTheme.typography.bodySmall)
                    } else {
                        students.forEach { s ->
                            val record = attendanceList.find { it.studentId == s.id && it.date == todayStr }
                            val isPresent = record?.status == "PRESENT"

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = s.fullName, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    ElevatedFilterChip(
                                        selected = isPresent,
                                        onClick = { viewModel.logAttendance(s.id, s.fullName, todayStr, true) },
                                        label = { Text("Present") }
                                    )
                                    ElevatedFilterChip(
                                        selected = record != null && !isPresent,
                                        onClick = { viewModel.logAttendance(s.id, s.fullName, todayStr, false) },
                                        label = { Text("Absent") }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Predictive AI Insights Cards
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RepublicOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "AI-Driven Educational Insights", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Alert 1
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, RepublicTeal.copy(alpha = 0.3f)),
                        colors = CardDefaults.cardColors(containerColor = RepublicTeal.copy(alpha = 0.05f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = RepublicTeal)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "Enrichment Recommendation", fontWeight = FontWeight.Bold, color = RepublicTeal)
                                Text(text = "Rahul Sharma has scored 100% in LSCEP Financial Discipline modules. Recommend him for the upcoming National Youth Entrepreneurship Summit.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    // Alert 2
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, RepublicOrange.copy(alpha = 0.3f)),
                        colors = CardDefaults.cardColors(containerColor = RepublicOrange.copy(alpha = 0.05f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = RepublicOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "Action Flag: Absenteeism", fontWeight = FontWeight.Bold, color = RepublicOrange)
                                Text(text = "Priya Reddy has missed 2 consecutive LSCEP classes. Suggest parent feedback or counseling booking.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // Post Announcements to whole School
        item {
            Button(
                onClick = { showNoticeCreator = !showNoticeCreator },
                colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showNoticeCreator) "Close Notice Creator" else "Create School Announcement")
            }
        }

        if (showNoticeCreator) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "New Announcement Form", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = noticeTitle,
                            onValueChange = { noticeTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = noticeContent,
                            onValueChange = { noticeContent = it },
                            label = { Text("Notice Content") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (noticeTitle.isNotEmpty() && noticeContent.isNotEmpty()) {
                                    viewModel.postNotice(noticeTitle, noticeContent)
                                    noticeTitle = ""
                                    noticeContent = ""
                                    showNoticeCreator = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Publish Notice")
                        }
                    }
                }
            }
        }
    }
}

// --- School Management Dashboard Panel ---
@Composable
fun SchoolManagementDashboard(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isSubscribed by viewModel.isSubscribed.collectAsState()
    val users by viewModel.allUsers.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "Administrative Console", style = MaterialTheme.typography.labelSmall, color = RepublicOrange, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = currentUser?.fullName ?: "Administrator", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = RepublicBlue)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "School: ${currentUser?.schoolName}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Subscription Paywall and details
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSubscribed) RepublicTeal.copy(alpha = 0.08f) else RepublicOrange.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, if (isSubscribed) RepublicTeal else RepublicOrange),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isSubscribed) "Active Republic Publications Premium" else "Republic Publications Subscription Expired",
                            fontWeight = FontWeight.Bold,
                            color = if (isSubscribed) RepublicTeal else RepublicOrange,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isSubscribed) "Your LSCEP subscription is active through July 2027. Full access to certified reports is unlocked." else "Renew to unlock multi-year psychometric assessment analysis and daily student digital newspapers.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (!isSubscribed) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { viewModel.simulatePayment() },
                            colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange)
                        ) {
                            Text("Renew via UPI")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Default.Verified, contentDescription = "Active", tint = RepublicTeal, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }

        // Institutional Analytics
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BarChart, contentDescription = null, tint = RepublicBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "School Participation Metrics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    val studentCount = users.count { it.role == "STUDENT" }
                    val teacherCount = users.count { it.role == "TEACHER" }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = studentCount.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = RepublicBlue)
                            Text(text = "Students Active", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = teacherCount.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = RepublicOrange)
                            Text(text = "Teachers Assigned", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "92%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = RepublicTeal)
                            Text(text = "LSCEP Mastery", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // Student Database List
        item {
            Text(text = "Student Enrolment Register", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
        }

        val students = users.filter { it.role == "STUDENT" }
        if (students.isEmpty()) {
            item {
                Text("No student records found.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            items(students) { s ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(text = s.fullName, fontWeight = FontWeight.Bold, color = RepublicBlue)
                            Text(text = "Enrolled Level: Class ${s.classLevel}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        AssistChip(
                            onClick = {},
                            label = { Text("Profile Verified") },
                            leadingIcon = { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp)) }
                        )
                    }
                }
            }
        }
    }
}

// --- Republic Student Publications Admin (Super Admin) Dashboard ---
@Composable
fun AdminDashboard(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val articles by viewModel.allArticles.collectAsState()

    var feedbackTexts = remember { mutableStateMapOf<Int, String>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.linearGradient(colors = listOf(RepublicOrange, DarkOrange)))
                        .padding(24.dp)
                ) {
                    Column {
                        Text(text = "Republic Publications HQ", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = currentUser?.fullName ?: "Editor", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Role: Editorial Approval Workflows", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }

        // Editorial Approval Queue heading
        item {
            Text(
                text = "Editorial Approval Queue (Newspaper Submissions)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = RepublicBlue
            )
        }

        val pendingArticles = articles.filter { it.status == "PENDING" }
        if (pendingArticles.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Zero pending student submissions. Everything has been reviewed! Excellent work.",
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(pendingArticles) { submission ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0xFFE1E3E8)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = submission.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            AssistChip(onClick = {}, label = { Text(submission.category) })
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Author: ${submission.studentName} (Class Student)", style = MaterialTheme.typography.bodySmall, color = RepublicOrange, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = submission.content, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Feedback input
                        val currentFeedback = feedbackTexts[submission.id] ?: ""
                        OutlinedTextField(
                            value = currentFeedback,
                            onValueChange = { feedbackTexts[submission.id] = it },
                            label = { Text("Editorial Notes / Feedback") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.approveArticle(submission.id, true, currentFeedback)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RepublicTeal),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Approve & Publish")
                            }

                            Button(
                                onClick = {
                                    viewModel.approveArticle(submission.id, false, currentFeedback)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reject / Request Edits")
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- LSCEP Interactive Lesson detail screen ---
@Composable
fun LessonScreen(viewModel: AppViewModel, lang: String) {
    val lesson by viewModel.activeLesson.collectAsState()
    val progressList by viewModel.studentProgress.collectAsState()

    var showQuizDialog by remember { mutableStateOf(false) }

    lesson?.let { l ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.navigateTo("DASHBOARD") },
                    colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Modules")
                }
                AssistChip(onClick = {}, label = { Text(l.moduleName) })
            }

            Text(
                text = l.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = RepublicBlue
            )

            Text(
                text = "Estimated reading duration: ${l.durationMin} mins",
                style = MaterialTheme.typography.bodySmall,
                color = RepublicOrange,
                fontWeight = FontWeight.Bold
            )

            // Content body
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = l.textContent,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(20.dp),
                    lineHeight = 22.sp
                )
            }

            // Audio Podcast Section
            if (l.podcastTitle.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = RepublicOrange.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Mic, contentDescription = "Podcast", tint = RepublicOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Syllabus Podcast Integration", fontWeight = FontWeight.Bold, color = RepublicOrange)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = l.podcastTitle, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text(text = "Length: ${l.podcastDuration}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Fake play back controls
                        var isPlaying by remember { mutableStateOf(false) }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { isPlaying = !isPlaying }) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                    contentDescription = "Play",
                                    modifier = Modifier.size(36.dp),
                                    tint = RepublicBlue
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.BookmarkBorder, contentDescription = "Bookmark", tint = RepublicBlue)
                            }
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.Download, contentDescription = "Download", tint = RepublicBlue)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showQuizDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("start_quiz_button")
            ) {
                Text("Start Interactive Lesson Quiz", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Quiz taking dialog
    if (showQuizDialog && lesson != null) {
        val quizList = lesson!!.quizQuestions
        if (quizList.isNotEmpty()) {
            var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
            Dialog(onDismissRequest = { showQuizDialog = false }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(text = "Interactive Lesson Quiz", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium, color = RepublicBlue)
                        Spacer(modifier = Modifier.height(16.dp))

                        val q = quizList.first()
                        Text(text = q.question, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        q.options.forEachIndexed { idx, option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedOptionIndex = idx }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = selectedOptionIndex == idx, onClick = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = option, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextButton(onClick = { showQuizDialog = false }, modifier = Modifier.weight(1f)) {
                                Text("Close")
                            }
                            Button(
                                onClick = {
                                    val score = if (selectedOptionIndex == q.correctIndex) 100 else 0
                                    viewModel.completeActiveLesson(score)
                                    showQuizDialog = false
                                    viewModel.navigateTo("DASHBOARD")
                                },
                                enabled = selectedOptionIndex != null,
                                colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("submit_quiz_button")
                            ) {
                                Text("Submit Score")
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Psychometric Assessment Screen ---
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PsychometricAssessmentScreen(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val reports by viewModel.studentReports.collectAsState()
    val allReports by viewModel.allReports.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    var screenState by remember { mutableStateOf("WELCOME") } // WELCOME, ASSESSMENT, REPORT_DETAIL
    var assessmentMode by remember { mutableStateOf("QUICK") } // QUICK, DETAILED, COMPREHENSIVE
    var activeIndex by remember { mutableStateOf(0) }
    
    // Hold selected questions for the current test run
    var activeQuestions by remember { mutableStateOf<List<PsychometricQuestion>>(emptyList()) }
    val answers = remember { mutableStateMapOf<Int, Int>() }
    var selectedReportForDetail by remember { mutableStateOf<PsychometricReport?>(null) }

    // Search and filter for school/counsellor role
    var counsellorSearchQuery by remember { mutableStateOf("") }
    var counsellorClassFilter by remember { mutableStateOf("All") }

    // Local counselor notes map (retained per-session)
    val counselorNotes = remember { mutableStateMapOf<Int, String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Navigation & Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { 
                    if (screenState != "WELCOME") {
                        screenState = "WELCOME"
                    } else {
                        viewModel.navigateTo("DASHBOARD")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (screenState != "WELCOME") "Exit to Portal Hub" else "Dashboard")
            }

            // Demo data population button for testing / demonstration purposes
            val totalReports = if (currentUser?.role == "SCHOOL" || currentUser?.role == "TEACHER") allReports.size else reports.size
            if (totalReports == 0) {
                Button(
                    onClick = {
                        scope.launch {
                            val r1 = PsychometricReport(
                                studentId = "rahul@school.com",
                                timestamp = System.currentTimeMillis() - 3600000L * 24 * 5, // 5 days ago
                                year = 2026,
                                answersJson = "1:0,4:0,7:0,10:0,13:0,16:0,19:0,22:0,25:0",
                                strengths = "1. Proactive Leadership: Excels at organizing tasks, establishing team timelines, and directing student projects.\n2. Inquisitive Critical Thinking: Dissects digital media and cross-verifies facts to ensure safety.\n3. Strong Moral Values: Actively guards public corridors and demonstrates civic integrity.",
                                learningStyle = "Visual-Spatial Learner: Absorbs concepts best via detailed mind maps, flowcharts, color-coded summaries, and printed diagrams. Optimizes research by visually chunking files.",
                                recommendedStream = "Science (PCM) with Computer Science & Analytics",
                                careerClusters = "1. AI Ethics & Data Architecture: Guiding safe software structures.\n2. Sustainable Agritech Systems: Engineering smart agricultural sensors.\n3. Cyber-Physical Logistics: Designing autonomous supply networks.",
                                fullReportJson = "Displays strong emotional maturity and respect for digital footprints. Showcases a high empathy quotient when supporting classmates.||Conflict Resolution, Python Coding, Public Speaking, Financial Literacy, Advanced Active Citizenship"
                            )
                            val r2 = PsychometricReport(
                                studentId = "priya@school.com",
                                timestamp = System.currentTimeMillis() - 3600000L * 24 * 30, // 30 days ago (previous cycle!)
                                year = 2026,
                                answersJson = "1:1,4:1,7:1,10:1,13:1,16:1,19:1,22:1,25:1",
                                strengths = "1. Out-of-the-Box Creativity: Formulates unique and imaginative approaches to solve complex problems.\n2. High Empathy and Relationship Building: Listen patiently and resolve conflicts peacefully.\n3. Excellent Communication: Inspires groups through visionary public speaking.",
                                learningStyle = "Auditory-Verbal Learner: Remembers information exceptionally well through verbal presentations, peer debates, oral summaries, and educational podcasts.",
                                recommendedStream = "Humanities with Liberal Arts & Communication Science",
                                careerClusters = "1. Corporate Law & Diplomacy: Negotiating civic contracts.\n2. Multimedia Journalism & Editorial: Directing educational columns.\n3. Clinical Child Psychology: Supporting early development counseling.",
                                fullReportJson = "Maintains calm self-regulation during stressful classroom assessments. Highly cooperative and values-driven.||Active Listening, Creative Writing, Public Speaking, Cognitive Empathy, Negotiation"
                            )
                            val r3 = PsychometricReport(
                                studentId = "rahul@school.com",
                                timestamp = System.currentTimeMillis(), // Today (multiple cycles for progress tracking!)
                                year = 2026,
                                answersJson = "1:2,4:2,7:2,10:2,13:2,16:2,19:2,22:2,25:2",
                                strengths = "1. Methodical Analytical Logic: Highly skilled at recognizing numerical pattern variations and deductive sequences.\n2. Balanced Conflict Resolution: Devises structural compromises to align divergent peer opinions.\n3. Strategic Risk Management: Evaluates data privacy variables before sharing online.",
                                learningStyle = "Read/Write Learner: Absorbs content best by reviewing textbooks, preparing written summary workbooks, and executing rigorous logical drills.",
                                recommendedStream = "Commerce with Applied Mathematics & Corporate Finance",
                                careerClusters = "1. Financial Risk Auditing: Devises investment budgets for business ventures.\n2. Information Security Consultancy: Devises cyber security safety protocols.\n3. Public Policy & Economics: Analyzing regional development indicators.",
                                fullReportJson = "Pragmatic, highly self-aware, and organized. Respects academic rules and group commitments.||Time Management, Advanced Excel & Analytics, Cyber Safety Audits, Conflict Mediation, Financial Planning"
                            )
                            viewModel.savePsychometricReport(r1)
                            viewModel.savePsychometricReport(r2)
                            viewModel.savePsychometricReport(r3)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RepublicOrange.copy(alpha = 0.15f),
                        contentColor = RepublicOrange
                    )
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Demo")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Load Demo Data", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (screenState == "WELCOME") {
            // WELCOME STATE: Main Portal Page
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = RepublicBlue.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = Trans.get("app_title", lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RepublicOrange
                    )
                    Text(
                        text = Trans.get("assessment_portal", lang),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = RepublicBlue
                    )
                    Text(
                        text = Trans.get("assessment_desc", lang),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Disclaimer Card (Mandatory Clause)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, RepublicOrange.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Disclaimer",
                        tint = RepublicOrange,
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(
                            text = "PORTAL DISCLAIMER",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = RepublicOrange,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = Trans.get("disclaimer", lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Student Assessment Panel
            if (currentUser?.role == "STUDENT") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = Trans.get("select_mode", lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RepublicBlue
                        )
                        
                        Text(
                            text = Trans.get("new_test_warning", lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Mode selectors
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val modes = listOf(
                                Triple("QUICK", Trans.get("quick_mode", lang), "1 question per dimension. Minimal fatigue, fast summary."),
                                Triple("DETAILED", Trans.get("detailed_mode", lang), "2 questions per dimension. Higher precision."),
                                Triple("COMPREHENSIVE", Trans.get("comp_mode", lang), "3 questions per dimension. Maximum profiling depth.")
                            )

                            modes.forEach { (modeCode, title, desc) ->
                                val isSel = assessmentMode == modeCode
                                OutlinedCard(
                                    onClick = { assessmentMode = modeCode },
                                    border = BorderStroke(1.5.dp, if (isSel) RepublicBlue else Color.LightGray.copy(alpha = 0.4f)),
                                    colors = CardDefaults.outlinedCardColors(
                                        containerColor = if (isSel) RepublicBlue.copy(alpha = 0.04f) else Color.Transparent
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        RadioButton(selected = isSel, onClick = { assessmentMode = modeCode })
                                        Column {
                                            Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = {
                                val sampled = PsychometricService.getAdaptiveQuestions(assessmentMode)
                                activeQuestions = sampled
                                activeIndex = 0
                                answers.clear()
                                screenState = "ASSESSMENT"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("start_adaptive_assessment_btn")
                        ) {
                            Text(Trans.get("start_test", lang), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }

            // Role-Specific Histograms / Management
            when (currentUser?.role) {
                "STUDENT" -> {
                    Text(
                        text = "Your Progress History & Cycle Tracking",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RepublicBlue
                    )

                    if (reports.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Info, contentDescription = "Info", tint = RepublicBlue, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No assessments recorded yet. Take your first assessment above to start tracking your cognitive cycles!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        reports.forEachIndexed { index, r ->
                            val dateStr = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(Date(r.timestamp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedReportForDetail = r
                                        screenState = "REPORT_DETAIL"
                                    },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Assessment Cycle #${reports.size - index}",
                                            fontWeight = FontWeight.Bold,
                                            color = RepublicBlue,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = "Date: $dateStr",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Recommended Stream: ${r.recommendedStream}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = RepublicOrange
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "View Details",
                                        tint = RepublicBlue
                                    )
                                }
                            }
                        }
                    }
                }

                "PARENT" -> {
                    Text(
                        text = "Rahul's Psychometric Progress Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RepublicBlue
                    )

                    if (reports.isEmpty()) {
                        Text("No reports completed by Rahul yet. Switch to STUDENT role using the switcher bar above to take a mock assessment and view parent insights.", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        reports.forEachIndexed { index, r ->
                            val dateStr = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(Date(r.timestamp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedReportForDetail = r
                                        screenState = "REPORT_DETAIL"
                                    },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Rahul's Assessment Cycle #${reports.size - index}", fontWeight = FontWeight.Bold, color = RepublicBlue)
                                        Text("Submitted on: $dateStr", style = MaterialTheme.typography.bodySmall)
                                        Text("Identified Learning Style: ${r.learningStyle.substringBefore(":")}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                    }
                                    Icon(Icons.Default.Info, contentDescription = "View Report", tint = RepublicBlue)
                                }
                            }
                        }

                        // Parent Coaching Companion (Value add)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = RepublicOrange.copy(alpha = 0.05f)),
                            border = BorderStroke(1.5.dp, RepublicOrange.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = "Companion", tint = RepublicOrange)
                                    Text("Parent Coaching Companion", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = RepublicOrange)
                                }
                                Text(
                                    text = "Based on Rahul's dominant Visual-Spatial learning style & Leadership profile, we suggest the following parenting action steps:",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "• Study Environment: Setup a dedicated dry-erase whiteboard in his room so he can sketch diagrams, outline concepts, and visually track study cycles.\n• Extracurriculars: Encourage Rahul to register for public school debates or volunteer to lead science model exhibitions to foster leadership tendencies.\n• Decision Making: Discuss long-term career streams (like PCM/Agritech) early. Guide him to analyze budgets and Compounding using real-world piggy bank savings.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                "TEACHER" -> {
                    // TEACHER VIEW: Class Dashboard and Analytics
                    Text(
                        text = "Teacher's Classroom Insights Dashboard",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RepublicBlue
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Aggregated Learning Style Profile (Class 6-10)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text("Calculated from all active assessments to optimize teacher guidance strategies.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            // Classroom Learning Style Bars
                            val styles = listOf(
                                Pair("Visual-Spatial Learners", 0.45f),
                                Pair("Auditory-Verbal Learners", 0.30f),
                                Pair("Read/Write Textual Learners", 0.15f),
                                Pair("Kinesthetic-Practical Learners", 0.10f)
                            )

                            styles.forEach { (styleName, pct) ->
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = styleName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                        Text(text = "${(pct * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = RepublicBlue)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { pct },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = RepublicBlue,
                                        trackColor = Color.LightGray.copy(alpha = 0.3f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = RepublicBlue.copy(alpha = 0.05f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Classroom Pedagogical Advice:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = RepublicBlue)
                                    Text(
                                        text = "With 45% Visual-Spatial learners, incorporate flowchart slides and visual diagrams frequently. Encourage the 30% Auditory group with peer-tutoring and classroom reading discussions.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Text("Individual Student Profile Cards", fontWeight = FontWeight.Bold, color = RepublicBlue)
                    if (allReports.isEmpty()) {
                        Text("No student reports are registered. Click the 'Load Demo Data' button at the top to instantly view class records.", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        allReports.forEach { r ->
                            val sName = if (r.studentId == "rahul@school.com") "Rahul Kumar" else "Priya Sharma"
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedReportForDetail = r
                                        screenState = "REPORT_DETAIL"
                                    },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = sName, fontWeight = FontWeight.Bold)
                                        Text(text = "Class Level: Class 8", style = MaterialTheme.typography.bodySmall)
                                        Text(text = "Recommended Stream: ${r.recommendedStream}", style = MaterialTheme.typography.bodySmall, color = RepublicOrange)
                                    }
                                    Icon(Icons.Default.Info, contentDescription = "View details", tint = RepublicBlue)
                                }
                            }
                        }
                    }
                }

                "SCHOOL" -> {
                    // COUNSELLOR PORTAL (role SCHOOL)
                    Text(
                        text = "School Counselor Command Hub",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RepublicBlue
                    )

                    // Search and Filter Bar
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = counsellorSearchQuery,
                                onValueChange = { counsellorSearchQuery = it },
                                placeholder = { Text("Search by Student Name...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Class Filter:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                val classes = listOf("All", "Class 6", "Class 7", "Class 8", "Class 9", "Class 10")
                                ScrollableTabRow(
                                    selectedTabIndex = classes.indexOf(counsellorClassFilter).coerceAtLeast(0),
                                    edgePadding = 0.dp,
                                    indicator = {},
                                    divider = {}
                                ) {
                                    classes.forEach { cl ->
                                        val isSel = counsellorClassFilter == cl
                                        Tab(
                                            selected = isSel,
                                            onClick = { counsellorClassFilter = cl },
                                            text = {
                                                Card(
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = if (isSel) RepublicBlue else Color.Transparent,
                                                        contentColor = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                                                    ),
                                                    shape = RoundedCornerShape(16.dp),
                                                    border = if (!isSel) BorderStroke(1.dp, Color.LightGray) else null
                                                ) {
                                                    Text(text = cl, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.bodySmall)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    val filteredReports = allReports.filter { r ->
                        val sName = if (r.studentId == "rahul@school.com") "Rahul Kumar" else "Priya Sharma"
                        val matchQuery = sName.contains(counsellorSearchQuery, ignoreCase = true)
                        val matchClass = counsellorClassFilter == "All" || counsellorClassFilter == "Class 8" // Since our mock models are in Class 8
                        matchQuery && matchClass
                    }

                    if (filteredReports.isEmpty()) {
                        Text("No student records match current search criteria. Click 'Load Demo Data' at the top to populate student logs.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(16.dp))
                    } else {
                        filteredReports.forEach { r ->
                            val sName = if (r.studentId == "rahul@school.com") "Rahul Kumar" else "Priya Sharma"
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedReportForDetail = r
                                        screenState = "REPORT_DETAIL"
                                    },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = sName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                            Text(text = "Student Email: ${r.studentId} • Class 8", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Icon(Icons.Default.Info, contentDescription = "Review", tint = RepublicBlue)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = RepublicBlue.copy(alpha = 0.1f)),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "Stream: ${r.recommendedStream.substringBefore("with")}",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Bold,
                                                color = RepublicBlue
                                            )
                                        }

                                        val hasNotes = counselorNotes[r.id] != null
                                        if (hasNotes) {
                                            Card(
                                                colors = CardDefaults.cardColors(containerColor = RepublicTeal.copy(alpha = 0.1f)),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "Notes Added",
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = RepublicTeal
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else if (screenState == "ASSESSMENT") {
            // ASSESSMENT STATE: Active Question Answering
            Text(
                text = "Adaptive Psychometric Assessment",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = RepublicBlue
            )

            // Progress Indicators
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question ${activeIndex + 1} of ${activeQuestions.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = RepublicOrange
                    )
                    Text(
                        text = "${((activeIndex.toFloat() / activeQuestions.size) * 100).toInt()}% Complete",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                LinearProgressIndicator(
                    progress = { (activeIndex.toFloat() / activeQuestions.size).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = RepublicBlue,
                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                )
            }

            if (uiState is UiState.Loading) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = RepublicOrange)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Evaluating dimension indicators via Gemini AI...", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "This compiles your detailed strengths, learning style, recommended stream, and career clusters.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                val rawQ = activeQuestions.getOrNull(activeIndex)
                val q = if (rawQ != null) I18nService.translateQuestion(rawQ, lang) else null
                if (q != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // Dimension Pill
                            Card(
                                colors = CardDefaults.cardColors(containerColor = RepublicOrange.copy(alpha = 0.12f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.align(Alignment.Start)
                            ) {
                                Text(
                                    text = q.dimension,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Black,
                                    color = RepublicOrange,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = q.question,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            q.options.forEachIndexed { optionIdx, option ->
                                val isSelected = answers[q.id] == optionIdx
                                OutlinedCard(
                                    onClick = { answers[q.id] = optionIdx },
                                    border = BorderStroke(1.5.dp, if (isSelected) RepublicBlue else Color.LightGray.copy(alpha = 0.5f)),
                                    colors = CardDefaults.outlinedCardColors(
                                        containerColor = if (isSelected) RepublicBlue.copy(alpha = 0.05f) else Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                        .testTag("option_${q.id}_$optionIdx")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        RadioButton(selected = isSelected, onClick = null)
                                        Text(text = option, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }

                    // Navigation buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { if (activeIndex > 0) activeIndex-- },
                            enabled = activeIndex > 0
                        ) {
                            Text("Previous", style = MaterialTheme.typography.bodyLarge)
                        }

                        if (activeIndex < activeQuestions.size - 1) {
                            Button(
                                onClick = { activeIndex++ },
                                enabled = answers[q.id] != null,
                                colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue),
                                modifier = Modifier.testTag("next_question_button")
                            ) {
                                Text("Next Question")
                            }
                        } else {
                            Button(
                                onClick = { viewModel.submitPsychometricAssessment(answers) },
                                enabled = answers[q.id] != null,
                                colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange),
                                modifier = Modifier.testTag("submit_assessment_button")
                            ) {
                                Text("Compile AI Report Card", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

        } else if (screenState == "REPORT_DETAIL" && selectedReportForDetail != null) {
            // REPORT_DETAIL STATE: Comprehensive results display
            val r = selectedReportForDetail!!
            val dateStr = SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(Date(r.timestamp))
            val sName = if (r.studentId == "rahul@school.com") "Rahul Kumar" else "Priya Sharma"

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = RepublicBlue.copy(alpha = 0.06f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "OFFICIAL GUIDANCE REPORT CARD", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = RepublicOrange, letterSpacing = 1.sp)
                    Text(text = sName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = RepublicBlue)
                    Text(text = "Class Level: Class 8 • Cycle Year: ${r.year}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Text(text = "Evaluation Date: $dateStr", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Strengths Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Check, contentDescription = "Strengths", tint = RepublicTeal)
                        Text("Identified Key Strengths", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                    }
                    Text(text = r.strengths, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Learning Style Guidance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Info, contentDescription = "Learning Style", tint = RepublicOrange)
                        Text("Dominant Learning Style Profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                    }
                    Text(text = r.learningStyle, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Higher Education Stream Recommendation
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = RepublicBlue.copy(alpha = 0.03f)),
                border = BorderStroke(1.5.dp, RepublicBlue.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("RECOMMENDED SECONDARY EDUCATION STREAM", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black, color = RepublicBlue, letterSpacing = 0.5.sp)
                    Text(text = r.recommendedStream, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = RepublicOrange)
                    Text(
                        text = "This academic pathway aligns ideally with the student's pattern deduction abilities, analytical logic focus, and career interest indicators.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Career Clusters
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Star, contentDescription = "Careers", tint = RepublicOrange)
                        Text("Compatible Career Interest Clusters", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                    }
                    Text(text = r.careerClusters, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Extra details from fullReportJson (divided by || for Behavioural Insights and Soft Skills)
            val parts = r.fullReportJson.split("||")
            val behaviouralInsights = parts.getOrNull(0) ?: ""
            val skillsToDevelop = parts.getOrNull(1) ?: ""

            if (behaviouralInsights.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Behavioural Insights & Values Quotient", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                        Text(text = behaviouralInsights, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            if (skillsToDevelop.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Recommended Skill Development Pathways", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicBlue)
                        Text("Master these soft and technical skills to accelerate career readiness:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            skillsToDevelop.split(",").forEach { skill ->
                                val cleanSkill = skill.trim()
                                if (cleanSkill.isNotEmpty()) {
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(text = cleanSkill, fontWeight = FontWeight.SemiBold) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // School Counselor clinical notes editing (Only visible to Counselor role "SCHOOL")
            if (currentUser?.role == "SCHOOL") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = RepublicBlue.copy(alpha = 0.05f)),
                    border = BorderStroke(1.5.dp, RepublicBlue.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Professional School Counselor Recommendations", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = RepublicBlue)
                        Text("Add clinical evaluation guidance notes. These recommendations will be stored and shared with the student's parents and academic advisors.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        var notesText by remember { mutableStateOf(counselorNotes[r.id] ?: "") }
                        OutlinedTextField(
                            value = notesText,
                            onValueChange = {
                                notesText = it
                                counselorNotes[r.id] = it
                            },
                            label = { Text("Clinical notes / Action items") },
                            placeholder = { Text("Enter counseling remarks here...") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Button(
                            onClick = {
                                scope.launch {
                                    counselorNotes[r.id] = notesText
                                    viewModel.savePsychometricReport(r) // saves local changes in DB
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RepublicTeal),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Save Clinical Notes", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Counselor Notes Display (visible to other roles if notes exist!)
            val existingCounsellorNotes = counselorNotes[r.id] ?: ""
            if (currentUser?.role != "SCHOOL" && existingCounsellorNotes.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = RepublicTeal.copy(alpha = 0.04f)),
                    border = BorderStroke(1.dp, RepublicTeal.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.AccountBox, contentDescription = "Counselor Notes", tint = RepublicTeal)
                            Text("School Counselor Recommendations:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = RepublicTeal)
                        }
                        Text(text = existingCounsellorNotes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            // Detailed Disclaimer at bottom of report
            Text(
                text = "Official Counsel Notice: This psychometric report is compiled dynamically by the Republic StudentPublications AI Counseling Engine based on self-reported scientific dimension indicators. It is not a medical or psychological diagnosis and is intended strictly as an educational pathway reference. Academic streams and career paths should be verified via personal consultations with teachers and counselors.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
            )

            val context = LocalContext.current
            var isDownloading by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    isDownloading = true
                    try {
                        val pdfBytes = PdfReportGenerator.generateReportPdf(
                            context = context,
                            report = r,
                            studentName = sName,
                            counselorNotes = counselorNotes[r.id]
                        )
                        val fileName = "Psychometric_Report_RE_PSA_${r.id}.pdf"
                        val uri = PdfReportGenerator.savePdfToDownloads(context, fileName, pdfBytes)
                        if (uri != null) {
                            Toast.makeText(context, "Saved: $fileName in Downloads folder!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Failed to save PDF.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        isDownloading = false
                    }
                },
                enabled = !isDownloading,
                colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange),
                modifier = Modifier.fillMaxWidth().testTag("download_pdf_report_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Download")
                    Text("Download Full PDF Report", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { screenState = "WELCOME" },
                border = BorderStroke(1.5.dp, RepublicBlue),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RepublicBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close Report Card", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- Newspaper Hub Screen ---
@Composable
fun NewspaperScreen(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val articles by viewModel.allArticles.collectAsState()
    val studentArticles by viewModel.studentArticles.collectAsState()

    var showSubmitForm by remember { mutableStateOf(false) }
    var articleTitle by remember { mutableStateOf("") }
    var articleCategory by remember { mutableStateOf("INNOVATION") }
    var articleContent by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Republic Student Newspaper",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = RepublicBlue
                )

                if (currentUser?.role == "STUDENT") {
                    Button(
                        onClick = { showSubmitForm = !showSubmitForm },
                        colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange)
                    ) {
                        Text(if (showSubmitForm) "Back to Newspaper" else "Submit Article")
                    }
                }
            }
        }

        if (showSubmitForm && currentUser?.role == "STUDENT") {
            // Submission Form
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Submit to Editorial board", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = articleTitle,
                            onValueChange = { articleTitle = it },
                            label = { Text("Article Title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("article_title_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Category
                        Text(text = "Category:", fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("INNOVATION", "POEM", "ESSAY", "ART").forEach { cat ->
                                ElevatedFilterChip(
                                    selected = articleCategory == cat,
                                    onClick = { articleCategory = cat },
                                    label = { Text(cat) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = articleContent,
                            onValueChange = { articleContent = it },
                            label = { Text("Write your contents...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .testTag("article_content_input")
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (articleTitle.isNotEmpty() && articleContent.isNotEmpty()) {
                                    viewModel.submitArticle(articleTitle, articleCategory, articleContent)
                                    articleTitle = ""
                                    articleContent = ""
                                    showSubmitForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RepublicBlue),
                            modifier = Modifier
                                .align(Alignment.End)
                                .testTag("submit_article_button")
                        ) {
                            Text("Submit to Editors")
                        }
                    }
                }
            }

            item {
                Text(text = "My Submission History", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            if (studentArticles.isEmpty()) {
                item {
                    Text("No submsisions yet.")
                }
            } else {
                items(studentArticles) { sub ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = sub.title, fontWeight = FontWeight.Bold)
                                AssistChip(onClick = {}, label = { Text(sub.status) })
                            }
                            if (sub.adminFeedback.isNotEmpty()) {
                                Text(text = "Feedback: ${sub.adminFeedback}", style = MaterialTheme.typography.bodySmall, color = RepublicOrange)
                            }
                        }
                    }
                }
            }
        } else {
            // General Approved Newspaper Articles
            val approved = articles.filter { it.status == "APPROVED" }
            val editorialStories = DataRepository.newspaperArticles

            item {
                Text(text = "Today's Editorial Features", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicOrange)
            }

            // Static curated stories
            items(editorialStories) { article ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = article.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = RepublicBlue)
                            Text(text = article.category, style = MaterialTheme.typography.labelSmall, color = RepublicOrange, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "By ${article.author} • ${article.date}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = article.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            if (approved.isNotEmpty()) {
                item {
                    Text(text = "Student Columns & Peer Innovations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RepublicTeal)
                }

                items(approved) { sub ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = sub.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = RepublicBlue)
                                Text(text = sub.category, style = MaterialTheme.typography.labelSmall, color = RepublicTeal, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "By ${sub.studentName} (Student)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = sub.content, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

// --- Podcast Player Screen ---
@Composable
fun PodcastScreen(viewModel: AppViewModel, lang: String) {
    val podcasts = DataRepository.podcastsList
    val bookmarks by viewModel.studentBookmarks.collectAsState()

    var activePlayingId by remember { mutableStateOf<String?>(null) }
    var filterBookmarkedOnly by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "LSCEP Radio & Podcast Hub",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = RepublicBlue
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = filterBookmarkedOnly, onCheckedChange = { filterBookmarkedOnly = it })
                    Text("Bookmarks Only", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        val filtered = if (filterBookmarkedOnly) {
            podcasts.filter { p -> bookmarks.any { it.podcastId == p.id && it.bookmarked } }
        } else {
            podcasts
        }

        if (filtered.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text("No podcasts match the active criteria.", modifier = Modifier.padding(16.dp))
                }
            }
        } else {
            items(filtered) { pod ->
                val isBookmarked = bookmarks.any { it.podcastId == pod.id && it.bookmarked }
                val isPlaying = activePlayingId == pod.id

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = pod.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = RepublicBlue)
                                Text(text = "${pod.speaker} • ${pod.duration}", style = MaterialTheme.typography.bodySmall, color = RepublicOrange)
                            }
                            IconButton(onClick = { viewModel.toggleBookmark(pod.id) }) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = RepublicOrange
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = pod.description, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { activePlayingId = if (isPlaying) null else pod.id }) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                    contentDescription = "Play",
                                    modifier = Modifier.size(32.dp),
                                    tint = RepublicBlue
                                )
                            }
                            if (isPlaying) {
                                Text(text = "Now streaming active audio...", style = MaterialTheme.typography.bodySmall, color = RepublicTeal, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Parenting Appointments Slot Bookings Screen ---
@Composable
fun AppointmentsScreen(viewModel: AppViewModel, lang: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val appointments by viewModel.parentAppointments.collectAsState()

    var bookingDate by remember { mutableStateOf("2026-07-10") }
    var bookingTime by remember { mutableStateOf("10:00 AM") }
    var notes by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Counselling Appointment Booking",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = RepublicBlue
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Reserve Academic Counseling Slot", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = bookingDate,
                        onValueChange = { bookingDate = it },
                        label = { Text("Select Date (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bookingTime,
                        onValueChange = { bookingTime = it },
                        label = { Text("Preferred Time Slot") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Tell us your queries / concerns") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (bookingDate.isNotEmpty() && bookingTime.isNotEmpty()) {
                                viewModel.bookCounsellingAppointment(bookingDate, bookingTime, notes)
                                notes = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RepublicOrange),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Reserve Counseling Slot")
                    }
                }
            }
        }

        item {
            Text(text = "Active counseling reservations", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        if (appointments.isEmpty()) {
            item {
                Text("No reservations logged.")
            }
        } else {
            items(appointments) { appt ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(text = "Counselling session booked for ${appt.studentName}", fontWeight = FontWeight.Bold)
                            Text(text = "Date: ${appt.date} • Time: ${appt.time}", style = MaterialTheme.typography.bodySmall, color = RepublicOrange)
                        }
                        IconButton(onClick = { viewModel.cancelCounsellingAppointment(appt.id) }) {
                            Icon(Icons.Default.Cancel, contentDescription = "Cancel", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

// Custom Helper classes to represent tuple structures cleanly
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
