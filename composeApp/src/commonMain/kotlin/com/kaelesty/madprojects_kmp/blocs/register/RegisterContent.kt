package com.kaelesty.madprojects_kmp.blocs.register

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.domain.common.UserType
import com.kaelesty.madprojects_kmp.ui.converters.toUi
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
					onValueChange = { component.setUsername(it) }
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledTextField(
					text = state.login,
					title = "Email",
					onValueChange = { component.setLogin(it) }
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledTextField(
					text = state.githubLink,
					title = "Ссылка на профиль Github",
					onValueChange = { component.setGithubLink(it) }
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledTextField(
					text = state.password,
					title = "Пароль",
					onValueChange = { component.setPassword(it) },
					isPassword = true
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledTextField(
					text = state.repeatPassword,
					title = "Подтверждение пароля",
					onValueChange = { component.setRepeatPassword(it) }
				)
				Spacer(modifier = Modifier.height(12.dp))

				TitledDropdown(
					title = "Тип аккаунта",
					onItemSelection = {
						component.setUserType(userTypes[it])
					},
					values = userTypes.map {
						it.toUi()
					}.toList(),
					selectedIndex = userTypes.indexOf(state.userType),
					modifier = Modifier
						.fillMaxWidth(0.8f)
				)
				Spacer(modifier = Modifier.height(48.dp))

				StyledButton(
					modifier = Modifier
						.fillMaxWidth(0.7f),
					text = "Регистрация",
					onClick = {
						component.submit()
					}
				)
			}
		}
		TypewriterText(
			text = "madprojects",
			style = MaterialTheme.typography.caption,
			onFinish = { },
			modifier = Modifier.offset(y = (-385).dp),
			playAnimation = false
		)
	}
}