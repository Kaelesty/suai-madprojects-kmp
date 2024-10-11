package com.kaelesty.madprojects_kmp.blocs.auth.register

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
import com.kaelesty.domain.common.UserType
import com.kaelesty.madprojects_kmp.ui.shared_converters.toUi
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledCard
import com.kaelesty.madprojects_kmp.ui.uikit.dropdowns.TitledDropdown
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField
import com.kaelesty.madprojects_kmp.ui.uikit.text.TypewriterText

@Composable
fun RegisterContent(
	component: RegisterComponent
) {

	val state by component.state.collectAsState()

	val userTypes = UserType.entries.toTypedArray()

	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		StyledCard(
			modifier = Modifier
				.fillMaxWidth(0.85f)
				.fillMaxHeight(0.85f)
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier.padding(top = 60.dp)
			) {

				TitledTextField(
					text = state.username,
					title = "Имя пользователя",
					onValueChange = {
						component.dropError()
						component.setUsername(it)
					}
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledTextField(
					text = state.login,
					title = "Email",
					onValueChange = {
						component.dropError()
						component.setLogin(it)
					}
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledTextField(
					text = state.githubLink,
					title = "Ссылка на профиль Github",
					onValueChange = {
						component.dropError()
						component.setGithubLink(it)
					}
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledTextField(
					text = state.password,
					title = "Пароль",
					onValueChange = {
						component.dropError()
						component.setPassword(it)
					},
					isPassword = true
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledTextField(
					text = state.repeatPassword,
					title = "Подтверждение пароля",
					onValueChange = {
						component.dropError()
						component.setRepeatPassword(it)
					}
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledDropdown(
					title = "Тип аккаунта",
					onItemSelection = {
						component.dropError()
						component.setUserType(userTypes[it])
					},
					values = userTypes.map {
						it.toUi()
					}.toList(),
					selectedIndex = userTypes.indexOf(state.userType),
					modifier = Modifier
						.fillMaxWidth(0.8f)
				)
				Spacer(modifier = Modifier.height(4.dp))
				if (state.errorMessage != "") {
					Text(
						text = state.errorMessage,
						style = MaterialTheme.typography.overline
					)
				}
				Spacer(modifier = Modifier.height(48.dp))

				StyledButton(
					modifier = Modifier
						.fillMaxWidth(0.7f),
					text = "Регистрация",
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
			modifier = Modifier.offset(y = (- 385).dp),
			playAnimation = false
		)
	}
}