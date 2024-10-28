package com.kaelesty.madprojects_kmp.blocs.auth.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.backhandler.BackHandler
import com.kaelesty.madprojects_kmp.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledCard
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField
import com.kaelesty.madprojects_kmp.ui.uikit.text.TypewriterText
import madprojects.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginContent(
	component: LoginComponent
) {

	val state by component.state.collectAsState()

	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		StyledCard(
			modifier = Modifier
				.fillMaxWidth(0.85f)
				.fillMaxHeight(0.5f)
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier.padding(top = 60.dp)
			) {
				TitledTextField(
					state.login,
					title = "Email",
					onValueChange = {
						component.dropError()
						component.setLogin(it)
					},
					modifier = Modifier
						.fillMaxWidth(0.8f)
				)
				Spacer(modifier = Modifier.height(12.dp))
				TitledTextField(
					state.password,
					title = "Пароль",
					isPassword = true,
					onValueChange = {
						component.dropError()
						component.setPassword(it)
					},
					modifier = Modifier
						.fillMaxWidth(0.8f)
				)
				Spacer(modifier = Modifier.height(4.dp))
				if (state.errorMessage != "") {
					Styled.uiKit().ErrorText(state.errorMessage)
				}
				Spacer(modifier = Modifier.height(48.dp))
				StyledButton(
					Modifier.fillMaxWidth(0.7f),
					text = "Вход",
					onClick = {
						component.dropError()
						component.submit()
					}
				)
			}


		}
		TypewriterText(
			text = "madprojects",
			style = MaterialTheme.typography.caption,
			onFinish = { },
			modifier = Modifier.offset(y = (-232).dp),
			playAnimation = false
		)
	}

}