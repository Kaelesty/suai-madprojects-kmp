package com.kaelesty.madprojects_kmp.ui.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
        ,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}