package com.example.muslimvn.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.muslimvn.R
import com.example.muslimvn.domain.models.AppTheme
import com.example.muslimvn.presentation.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToQuranSettings: () -> Unit,
    onNavigateToPrayerNotifications: () -> Unit,
    onNavigateToDownloadedVideos: () -> Unit,
    onNavigateToDownloadedPodcasts: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val appTheme by viewModel.appTheme.collectAsState()
    val useDynamicColor by viewModel.useDynamicColor.collectAsState()
    val supportsDynamicColor = remember { android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    var showThemeDialog by remember { mutableStateOf(false) }
    var showSourcesDialog by remember { mutableStateOf(false) }
    var showDisclaimerDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.nav_settings)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item { PreferenceHeader(title = "Giao diện") }
                item {
                    PreferenceItem(
                        title = "Chế độ tối",
                        subtitle = when (appTheme) {
                            AppTheme.FOLLOW_SYSTEM -> "Theo hệ thống"
                            AppTheme.LIGHT -> "Sáng"
                            AppTheme.DARK -> "Tối"
                        },
                        icon = Icons.Default.DarkMode,
                        onClick = { showThemeDialog = true }
                    )
                }
                item {
                    ListItem(
                        headlineContent = { Text("Màu động (Material You)") },
                        supportingContent = {
                            Text(
                                if (supportsDynamicColor) "Tự động đổi tông màu app theo hình nền thiết bị"
                                else "Chỉ khả dụng trên Android 12 trở lên"
                            )
                        },
                        leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) },
                        trailingContent = {
                            Switch(
                                checked = useDynamicColor && supportsDynamicColor,
                                onCheckedChange = { viewModel.onDynamicColorChanged(it) },
                                enabled = supportsDynamicColor
                            )
                        }
                    )
                }

                item { PreferenceHeader(title = "Nội dung & Thông báo") }
                item {
                    PreferenceItem(
                        title = "Podcast đã tải xuống",
                        subtitle = "Quản lý và giải phóng dung lượng podcast offline",
                        icon = Icons.Default.Podcasts,
                        onClick = onNavigateToDownloadedPodcasts
                    )
                }
                item {
                    PreferenceItem(
                        title = "Nội dung đã tải về",
                        subtitle = "Quản lý và nghe/xem lại video, podcast offline",
                        icon = Icons.Default.Download,
                        onClick = onNavigateToDownloadedVideos
                    )
                }
                item {
                    PreferenceItem(
                        title = "Cài đặt Quran",
                        subtitle = "Font chữ, học giả, chế độ hiển thị",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        onClick = onNavigateToQuranSettings
                    )
                }
                item {
                    PreferenceItem(
                        title = "Thông báo cầu nguyện",
                        subtitle = "Âm thanh Adhan và thông báo",
                        icon = Icons.Default.Notifications,
                        onClick = onNavigateToPrayerNotifications
                    )
                }

                item { PreferenceHeader(title = "Pháp lý & Thông tin") }
                item {
                    PreferenceItem(
                        title = "Chính sách quyền riêng tư",
                        subtitle = "Cam kết bảo mật và xử lý dữ liệu vị trí trên thiết bị",
                        icon = Icons.Default.PrivacyTip,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MuslimVN/MuslimVN/blob/master/PRIVACY.md"))
                            try { context.startActivity(intent) } catch (_: Exception) { }
                        }
                    )
                }
                item {
                    PreferenceItem(
                        title = "Nguồn & Giấy phép",
                        subtitle = "Thông tin bản quyền mã nguồn, phông chữ và dữ liệu",
                        icon = Icons.Default.Gavel,
                        onClick = { showSourcesDialog = true }
                    )
                }
                item {
                    PreferenceItem(
                        title = "Liên hệ & Báo lỗi",
                        subtitle = "Gửi góp ý hoặc báo lỗi qua GitHub Issues",
                        icon = Icons.Default.BugReport,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MuslimVN/MuslimVN/issues"))
                            try { context.startActivity(intent) } catch (_: Exception) { }
                        }
                    )
                }
                item {
                    PreferenceItem(
                        title = "Tuyên bố miễn trừ trách nhiệm",
                        subtitle = "Lưu ý về giờ cầu nguyện, Zakat và nội dung học giả",
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        onClick = { showDisclaimerDialog = true }
                    )
                }

                item { PreferenceHeader(title = "Về ứng dụng") }
                item {
                    PreferenceItem(
                        title = "Đánh giá ứng dụng",
                        icon = Icons.Default.Star,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("market://details?id=${context.packageName}")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                try {
                                    context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                                    })
                                } catch (_: Exception) { }
                            }
                        }
                    )
                }
                item {
                    val packageInfo = remember {
                        try {
                            context.packageManager.getPackageInfo(context.packageName, 0)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    val version = packageInfo?.versionName ?: "1.0.0"
                    PreferenceItem(
                        title = "Phiên bản",
                        subtitle = version,
                        icon = Icons.Default.Info,
                        onClick = { }
                    )
                }
            }
        }
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = appTheme,
            onThemeSelected = {
                viewModel.onThemeSelected(it)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    if (showSourcesDialog) {
        SourcesLicenseDialog(onDismiss = { showSourcesDialog = false })
    }

    if (showDisclaimerDialog) {
        DisclaimerDialog(onDismiss = { showDisclaimerDialog = false })
    }
}

@Composable
fun SourcesLicenseDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nguồn & Giấy phép") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Mã nguồn ứng dụng:", style = MaterialTheme.typography.titleSmall)
                Text("• Cấp phép mã nguồn mở GPL-3.0-or-later", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Phông chữ (Fonts):", style = MaterialTheme.typography.titleSmall)
                Text("• Amiri & Inter: SIL Open Font License 1.1", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Dữ liệu & Âm thanh:", style = MaterialTheme.typography.titleSmall)
                Text("• Giờ cầu nguyện: Thư viện Adhan (MIT License)", style = MaterialTheme.typography.bodyMedium)
                Text("• Kinh Quran & Bản dịch: Tanzil & Quran.com API", style = MaterialTheme.typography.bodyMedium)
                Text("• Lịch Hijri: Aladhan API & Room local cache", style = MaterialTheme.typography.bodyMedium)
                Text("• Âm thanh Adhan: AlAdhan Project", style = MaterialTheme.typography.bodyMedium)
                Text("• Học giả Việt Nam: Mách Zên & Gosaly Ahmad (Đã cấp phép)", style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

@Composable
fun DisclaimerDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tuyên bố miễn trừ trách nhiệm") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "1. Giờ cầu nguyện, hướng Qibla và tính toán Zakat mang tính tham khảo. Người dùng nên đối chiếu với cộng đồng và giáo sĩ địa phương.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "2. MuslimVN không thuộc, không liên kết chính thức và không được tài trợ bởi các học giả, Qari hay tổ chức bên thứ ba.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đã hiểu")
            }
        }
    )
}

@Composable
fun ThemeSelectionDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn chế độ tối") },
        text = {
            Column {
                ThemeOption(
                    title = "Theo hệ thống",
                    selected = currentTheme == AppTheme.FOLLOW_SYSTEM,
                    onClick = { onThemeSelected(AppTheme.FOLLOW_SYSTEM) }
                )
                ThemeOption(
                    title = "Sáng",
                    selected = currentTheme == AppTheme.LIGHT,
                    onClick = { onThemeSelected(AppTheme.LIGHT) }
                )
                ThemeOption(
                    title = "Tối",
                    selected = currentTheme == AppTheme.DARK,
                    onClick = { onThemeSelected(AppTheme.DARK) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@Composable
fun ThemeOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = androidx.compose.ui.semantics.Role.RadioButton
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun PreferenceHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(top = 8.dp)
    )
}

@Composable
fun PreferenceItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = { Icon(icon, contentDescription = null) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
