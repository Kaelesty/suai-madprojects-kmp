package com.kaelesty.madprojects_kmp.ui.uikit.dropdowns

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.ui.uikit.text.StyledTextField
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.compose_multiplatform
import madprojects.composeapp.generated.resources.dropdown_arrow
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun StyledDropdown(
	selectedIndex: Int,
	values: List<String>,
	modifier: Modifier = Modifier,
	onItemSelection: (Int) -> Unit,
	closeOnSelect: Boolean = true
) {

	var expanded by remember {
		mutableStateOf(false)
	}

	Card(
		modifier = modifier
			.shadow(
				elevation = 6.dp,
				spotColor = DefaultShadowColor,
				ambientColor = DefaultShadowColor
			)
			.height(50.dp)
			.clickable {
				expanded = !expanded
			}
		,
		shape = MaterialTheme.shapes.medium.copy(
			CornerSize(0), CornerSize(0), CornerSize(0), CornerSize(0),
		),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceAround,
			modifier = Modifier
				.padding(horizontal = 16.dp)
		) {
			Text(
				text = values[selectedIndex],
				style = MaterialTheme.typography.body2,
				modifier = Modifier.weight(1f)
			)
			DropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false },
				modifier = modifier
			) {
				values.forEachIndexed { index, it ->
					DropdownMenuItem(
						onClick = {
							onItemSelection(index)
							if (closeOnSelect) expanded = false
						}
					) {
						Text(
							text = it,
							style = MaterialTheme.typography.body2,
							textDecoration = if (index == selectedIndex) TextDecoration.Underline else null
						)
					}
				}
			}
			Image(
				vectorResource(Res.drawable.dropdown_arrow),
				contentDescription = null,
				modifier = Modifier
					.width(20.dp)
			)
		}
	}

}

@Composable
fun TitledDropdown(
	title: String,
	selectedIndex: Int,
	values: List<String>,
	modifier: Modifier = Modifier,
	onItemSelection: (Int) -> Unit,
) {
	Column {
		Text(
			text = title,
			style = MaterialTheme.typography.body2
		)
		Spacer(modifier = Modifier.height(10.dp))
		StyledDropdown(
			selectedIndex, values, modifier, onItemSelection
		)
	}
}