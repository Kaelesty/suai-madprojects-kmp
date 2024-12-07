package com.kaelesty.madprojects_kmp.blocs.memberProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.kaelesty.madprojects_kmp.blocs.memberProfile.newProject.NewProjectContent
import com.kaelesty.madprojects_kmp.blocs.memberProfile.profile.ProfileContent
import com.kaelesty.madprojects_kmp.blocs.project.ProjectComponent
import com.kaelesty.madprojects_kmp.blocs.project.bottomBorder
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chat.Avatar
import com.kaelesty.madprojects_kmp.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.uikit.StyledList
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.layout.TopBar
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.right_arrow
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MemberProfileContent(
    component: MemberProfileComponent
) {

    Children(
        stack = component.stack
    ) {
        when (val instance = it.instance) {
            is MemberProfileComponent.Child.NewProject -> NewProjectContent(instance.component)
            is MemberProfileComponent.Child.Profile -> ProfileContent(instance.component)
        }
    }
}
