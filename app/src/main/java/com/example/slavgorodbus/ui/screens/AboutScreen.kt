package com.example.slavgorodbus.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
// import androidx.compose.material.icons.Icons // Больше не нужен для Icons.AutoMirrored.Filled.ArrowBack
// import androidx.compose.material.icons.automirrored.filled.ArrowBack // Больше не нужен
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.slavgorodbus.R
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    // onNavigateBack: () -> Unit, // <--- 1. УДАЛЯЕМ ПАРАМЕТР onNavigateBack
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Boolean
) {
    val developerName = stringResource(id = R.string.developer_name_value)
    val developerVkUrl = stringResource(id = R.string.developer_vk_url_value)
    val developerGitHubUrl = stringResource(id = R.string.developer_github_url_value)
    val linkTextVk = stringResource(id = R.string.link_text_vk)
    val linkTextGitHub = stringResource(id = R.string.link_text_github)

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.about_screen_title),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            // 2. УДАЛЯЕМ БЛОК navigationIcon
            // navigationIcon = {
            //     IconButton(onClick = onNavigateBack) { // onNavigateBack здесь вызовет ошибку, так как параметр удален
            //         Icon(
            //             imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            //             contentDescription = stringResource(id = R.string.content_description_back)
            //         )
            //     }
            // },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer // Этот параметр можно тоже убрать, если иконки нет
            )
        )
        // Контент экрана
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(id = R.string.developer_section_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = developerName,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                stringResource(id = R.string.links_section_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            ClickableLinkText(
                text = linkTextVk,
                url = developerVkUrl
            )
            Spacer(modifier = Modifier.height(8.dp))

            ClickableLinkText(
                text = linkTextGitHub,
                url = developerGitHubUrl
            )
        }
    }
}

@Composable
private fun ClickableLinkText(
    text: String,
    url: String
) {
    // ... (код ClickableLinkText остается без изменений) ...
    val localContext = LocalContext.current
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
        ),
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            try {
                localContext.startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                android.widget.Toast.makeText(
                    localContext,
                    localContext.getString(R.string.error_no_browser),
                    android.widget.Toast.LENGTH_LONG
                ).show()
            } catch (_: Exception) {
                android.widget.Toast.makeText(
                    localContext,
                    localContext.getString(R.string.error_cant_open_link),
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}