package com.example.muslimvn.presentation.screens

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentReaderScreen(
    url: String,
    title: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    val isDocumentFile = remember(url) {
        val lowercase = url.lowercase()
        lowercase.endsWith(".pdf") || lowercase.endsWith(".doc") || lowercase.endsWith(".docx")
    }

    fun openInExternalApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) { }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title.ifBlank { "Tài liệu nghiên cứu" }, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { openInExternalApp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = "Mở bằng ứng dụng ngoài"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isDocumentFile) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title.ifBlank { "Tài liệu PDF / Word" },
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { openInExternalApp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mở bằng ứng dụng đọc tài liệu")
                    }
                }
            } else {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                loadWithOverviewMode = true
                                useWideViewPort = true
                                builtInZoomControls = true
                                displayZoomControls = false
                            }
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, pageUrl: String?) {
                                    super.onPageFinished(view, pageUrl)
                                    isLoading = false
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    return false
                                }
                            }
                            loadUrl(url)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                if (isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
