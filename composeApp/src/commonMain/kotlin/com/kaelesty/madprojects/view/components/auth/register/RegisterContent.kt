package com.kaelesty.madprojects.view.components.auth.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects.domain.UserType
import com.kaelesty.madprojects.domain.toUi
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterComponent
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

	val userTypes = UserType.entries.toTypedArray().toList()

	var secondStep by rememberSaveable { mutableStateOf(false) }

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
			AnimatedVisibility(
				visible = !secondStep,
				enter = slideInHorizontally { -it },
				exit = slideOutHorizontally { -it },
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
						},
						imeAction = ImeAction.Next,
						modifier = Modifier
							.fillMaxWidth(0.8f),
					)
					Spacer(modifier = Modifier.height(12.dp))

					TitledTextField(
						text = state.email,
						title = "Email",
						onValueChange = {
							component.dropError()
							component.setLogin(it)
						},
						imeAction = ImeAction.Next,
						modifier = Modifier
							.fillMaxWidth(0.8f),
					)
					Spacer(modifier = Modifier.height(12.dp))

					TitledTextField(
						text = state.lastName,
						title = "Фамилия",
						onValueChange = {
							component.dropError()
							component.setLastname(it)
						},
						imeAction = ImeAction.Next,
						modifier = Modifier
							.fillMaxWidth(0.8f),
					)
					Spacer(modifier = Modifier.height(12.dp))

					TitledTextField(
						text = state.firstName,
						title = "Имя",
						onValueChange = {
							component.dropError()
							component.setFirstname(it)
						},
						imeAction = ImeAction.Next,
						modifier = Modifier
							.fillMaxWidth(0.8f),
					)
					Spacer(modifier = Modifier.height(12.dp))

					TitledTextField(
						text = state.secondName,
						title = "Отчество",
						onValueChange = {
							component.dropError()
							component.setSecondname(it)
						},
						imeAction = ImeAction.Done,
						modifier = Modifier
							.fillMaxWidth(0.8f),
					)
					Spacer(modifier = Modifier.height(12.dp))

					StyledButton(
						modifier = Modifier
							.fillMaxWidth(0.7f),
						text = "Далее >>>",
						onClick = {
							secondStep = true
						}
					)
				}

			}

			AnimatedVisibility(
				visible = secondStep,
				enter = slideInHorizontally { -it },
				exit = slideOutHorizontally { -it },
			) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier.padding(top = 60.dp)
				) {
					TitledTextField(
						text = state.data,
						title = when (state.userType) {
							UserType.Common -> "Группа"
							UserType.Curator -> "Степень"
						},
						onValueChange = {
							component.dropError()
							component.setData(it)
						},
						imeAction = ImeAction.Next,
						modifier = Modifier
							.fillMaxWidth(0.8f),
					)
					Spacer(modifier = Modifier.height(12.dp))

					TitledTextField(
						text = state.password,
						title = "Пароль",
						onValueChange = {
							component.dropError()
							component.setPassword(it)
						},
						isPassword = true,
						imeAction = ImeAction.Next,
						modifier = Modifier
							.fillMaxWidth(0.8f),
					)
					Spacer(modifier = Modifier.height(12.dp))

					TitledTextField(
						text = state.repeatPassword,
						title = "Подтверждение пароля",
						onValueChange = {
							component.dropError()
							component.setRepeatPassword(it)
						},
						imeAction = ImeAction.Done,
						isPassword = true,
						modifier = Modifier
							.fillMaxWidth(0.8f),
					)
					Spacer(modifier = Modifier.height(12.dp))

					TitledDropdown(
						title = "Тип аккаунта",
						onItemSelection = {
							component.dropError()
							component.setUserType(it)
						},
						values = userTypes,
						selectedItem = state.userType,
						modifier = Modifier
							.fillMaxWidth(0.8f),
						itemTitle = { it.toUi() },
					)
					Spacer(modifier = Modifier.height(4.dp))
					if (state.errorMessage != "") {
						Styled.uiKit().ErrorText(state.errorMessage)
					}
					Spacer(modifier = Modifier.height(48.dp))

					StyledButton(
						modifier = Modifier
							.fillMaxWidth(0.7f),
						text = "<<< Назад",
						onClick = {
							secondStep = false
						}
					)
					Spacer(modifier = Modifier.height(4.dp))
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