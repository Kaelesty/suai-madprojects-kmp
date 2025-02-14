package com.kaelesty.madprojects_kmp.ui.uikit.dropdowns

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.view.extensions.bottomBorder
import com.kaelesty.madprojects.view.ui.experimental.Styled
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.dropdown_arrow
import org.jetbrains.compose.resources.vectorResource

@Composable
fun <T> StyledDropdown(
	selectedItem: T?,
	values: List<T>,
	modifier: Modifier = Modifier,
	onItemSelection: (T) -> Unit,
	closeOnSelect: Boolean = true,
	fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
	itemTitle: (T) -> String,
	background: Color? = null
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
		backgroundColor = background ?: MaterialTheme.colors.surface
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceAround,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 2.dp)
		) {
			Text(
				text = selectedItem?.let { itemTitle(it) } ?: "Выбрать...",
				style = MaterialTheme.typography.body2.copy(
					fontSize = fontSize,
				),
				modifier = Modifier.weight(1f),
			)
			DropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false },
				modifier = modifier
			) {
				values.forEachIndexed { index, it ->
					DropdownMenuItem(
						onClick = {
							onItemSelection(it)
							if (closeOnSelect) expanded = false
						}
					) {
						Text(
							text = itemTitle(it),
							style = MaterialTheme.typography.body2.copy(
								fontSize = fontSize
							),
							textDecoration = if (it == selectedItem) TextDecoration.Underline else null
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
fun <T> TitledDropdown(
	title: String,
	selectedItem: T?,
	values: List<T>,
	modifier: Modifier = Modifier,
	onItemSelection: (T) -> Unit,
	closeOnSelect: Boolean = true,
	fontSize: TextUnit = 20.sp,
	itemTitle: (T) -> String,
) {
	Column {
		Text(
			text = title,
			style = MaterialTheme.typography.body2.copy(
				fontSize = 22.sp
			)
		)
		Spacer(modifier = Modifier.height(10.dp))
		StyledDropdown(
			selectedItem = selectedItem,
			values = values,
			modifier = modifier,
			onItemSelection = onItemSelection,
			closeOnSelect = closeOnSelect,
			fontSize = fontSize,
			itemTitle = itemTitle
		)
	}
}


@Composable
fun <T> SimplifiedDropdown(
	selectedItem: T?,
	values: List<T>,
	modifier: Modifier = Modifier,
	onItemSelection: (T) -> Unit,
	closeOnSelect: Boolean = true,
	fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
	itemTitle: (T) -> String,
) {

	var expanded by remember {
		mutableStateOf(false)
	}

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceAround,
		modifier = modifier
			.padding(horizontal = 16.dp, vertical = 2.dp)
			.clickable {
				expanded = !expanded
			}
			.bottomBorder(
				brush = Brush.linearGradient(
					Styled.uiKit().colors().gradient,
					tileMode = TileMode.Decal
				),
				height = 2f
			)
	) {
		Text(
			text = selectedItem?.let { itemTitle(it) } ?: "Выбрать...",
			style = MaterialTheme.typography.body2.copy(
				fontSize = fontSize,
			),
			modifier = Modifier.weight(1f)
			,
		)
		DropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false },
			modifier = modifier
		) {
			values.forEachIndexed { index, it ->
				DropdownMenuItem(
					onClick = {
						onItemSelection(it)
						if (closeOnSelect) expanded = false
					}
				) {
					Text(
						text = itemTitle(it),
						style = MaterialTheme.typography.body2.copy(
							fontSize = fontSize
						),
						textDecoration = if (it == selectedItem) TextDecoration.Underline else null
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