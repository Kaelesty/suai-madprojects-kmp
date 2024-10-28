package com.kaelesty.madprojects_kmp.blocs.project.kanban

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.madprojects_kmp.extensions.toReadableTime
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField
import entities.KanbanState
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.circle_add
import madprojects.composeapp.generated.resources.move_dots
import madprojects.composeapp.generated.resources.right_arrow
import org.jetbrains.compose.resources.vectorResource

@Composable
fun KanbanContent(
	component: KanbanComponent
) {
	val state by component.state.collectAsState()
	if (state.kanbanState == null) {
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator(
				modifier = Modifier.size(120.dp)
			)
		}
	}
	else {
		Kanban(state.kanbanState!!, component)
	}
}

@Composable
fun Kanban(
	state: KanbanState,
	component: KanbanComponent
) {

	var showNewKardDialog by remember {
		mutableStateOf(false)
	}

	if (showNewKardDialog) {
		EditDialog(
			title = "Создание карточки",
			initialName = "",
			initialDesc = "",
			onDismiss = { showNewKardDialog = false },
			onSubmit = { name, desc -> /* TODO */ }
		)
	}

	Scaffold(
		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					showNewKardDialog = true
				},
				backgroundColor = MaterialTheme.colors.primary,
				contentColor = Color.White,
				shape = RoundedCornerShape(12.dp)
			) {
				Icon(
					vectorResource(Res.drawable.circle_add),
					null,
					modifier = Modifier
						.size(45.dp)
						.padding(4.dp)
					,
				)
			}
		}
	) {
		var selectedColumnId by remember {
			mutableStateOf(state.columns.first().id)
		}

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(it)
				.padding(4.dp)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp)
			) {
				state.columns.forEach {
					ColumnSnack(
						column = it,
						isSelected = selectedColumnId == it.id,
						onClick = {
							selectedColumnId = it
						}
					)
					Spacer(Modifier.width(8.dp))
				}
			}
			LazyVerticalGrid(
				modifier = Modifier
					.fillMaxWidth(),
				columns = GridCells.Fixed(2),
				contentPadding = PaddingValues(4.dp),
			) {
				items(
					state.columns.first { it.id == selectedColumnId }.kards,
					key = { it.id }
				) {
					Kard(
						it,
						onEdit = { kard, name, desc -> component.updateKard(kard, name, desc) },
						onDelete = { component.deleteKard(it) },
					)
				}
			}
		}
	}
}

@Composable
fun ColumnSnack(
	column: KanbanState.Column,
	isSelected: Boolean,
	onClick: (Int) -> Unit,
) {
	Card(
		backgroundColor = if (isSelected) MaterialTheme.colors.onPrimary
		else MaterialTheme.colors.surface,
		shape = RoundedCornerShape(12.dp),
		modifier = Modifier
			.clickable { onClick(column.id) }
	) {
		Text(
			text = column.name,
			style = MaterialTheme.typography.body2.copy(
				fontSize = 20.sp,
				fontWeight = FontWeight.Light
			),
			modifier = Modifier
				.padding(12.dp)
		)
	}
}

@Composable
fun Kard(
	kard: KanbanState.Kard,
	onEdit: (KanbanState.Kard, String, String) -> Unit,
	onDelete: (Int) -> Unit,
) {

	var showMoreDialog by remember {
		mutableStateOf(false)
	}

	var showEditDialog by remember {
		mutableStateOf(false)
	}

	var showDeleteDialog  by remember {
		mutableStateOf(false)
	}

	if (showMoreDialog) {
		ShowMoreDialog(
			kard = kard,
			onDismiss = {
				showMoreDialog = false
			}
		)
	}

	else if (showEditDialog) {
		EditDialog(
			title = "Редактировать ${kard.title}",
			initialName = kard.title,
			initialDesc = kard.desc,
			onDismiss = {
				showEditDialog = false
			},
			onSubmit = { name, desc ->
				onEdit(kard, name, desc)
			}
		)
	}

	else if (showDeleteDialog) {
		DeleteDialog(
			itemName = "карточку ${kard.title}",
			onDismiss = {
				showDeleteDialog = false
			},
			onSubmit = {
				onDelete(kard.id)
			}
		)
	}

	var menuExpanded by remember {
		mutableStateOf(false)
	}

	StyledRoundedCard {
		Row(
			modifier = Modifier
				.height(160.dp)
				.padding(horizontal = 4.dp, vertical = 6.dp)
		) {
			Column(
				modifier = Modifier
					.padding(vertical = 6.dp, horizontal = 12.dp)
					.weight(0.85f)
				,
			) {
				var lineCount by remember {
					mutableStateOf(1)
				}
				Text(
					text = kard.title,
					style = MaterialTheme.typography.body2.copy(
						fontSize = 28.sp
					),
					onTextLayout = {
						lineCount = it.lineCount
					}
				)
				Spacer(Modifier.weight(1f))
				if (lineCount < 4) {
					Text(
						text = "Автор: ${kard.authorName}",
						style = MaterialTheme.typography.body2.copy(
							fontSize = 18.sp,
							fontWeight = FontWeight.Thin
						),
						modifier = Modifier
							.padding(bottom = 4.dp)
					)
				}
				if (lineCount < 3) {
					Text(
						text = "Создано: ${kard.createdTimeMillis.toReadableTime()}",
						style = MaterialTheme.typography.body2.copy(
							fontSize = 18.sp,
							fontWeight = FontWeight.Thin
						),
						modifier = Modifier
							.padding(bottom = 4.dp)
					)
					Text(
						text = "Изменено: ${kard.updateTimeMillis.toReadableTime()}",
						style = MaterialTheme.typography.body2.copy(
							fontSize = 18.sp,
							fontWeight = FontWeight.Thin
						),
						modifier = Modifier
							.padding(bottom = 4.dp)
					)
				}
			}
			Column(
				horizontalAlignment = Alignment.End,
				modifier = Modifier
					.weight(0.2f)
			) {
				IconButton(
					onClick = { menuExpanded = true }
				) {
					Icon(
						vectorResource(Res.drawable.move_dots),
						modifier = Modifier.size(30.dp),
						contentDescription = null
					)
				}
				DropdownMenu(
					expanded = menuExpanded,
					onDismissRequest = { menuExpanded = false },
					offset = DpOffset(x = 0.dp, y = (-160).dp)
				) {
					DropdownMenuItem(
						onClick = { showEditDialog = true },
					) {
						Text("Редактировать")
					}
					DropdownMenuItem(
						onClick = { showDeleteDialog = true },
					) {
						Text("Удалить")
					}
				}
				Spacer(Modifier.weight(1f))
				IconButton(
					onClick = { showMoreDialog = true }
				) {
					Icon(
						vectorResource(Res.drawable.right_arrow),
						modifier = Modifier.size(40.dp),
						contentDescription = null
					)
				}
			}
		}
	}
}

@Composable
fun EditDialog(
	title: String,
	initialName: String,
	initialDesc: String,
	onDismiss: () -> Unit,
	onSubmit: (String, String) -> Unit,
) {

	var name by rememberSaveable {
		mutableStateOf(initialName)
	}
	var desc by rememberSaveable {
		mutableStateOf(initialDesc)
	}

	Dialog(
		onDismissRequest = onDismiss
	) {
		StyledRoundedCard(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp)
		) {
			Column(
				modifier = Modifier.padding(12.dp)
			) {
				Text(
					text = title,
					style = MaterialTheme.typography.body2.copy(
						fontSize = 28.sp
					),
				)
				Spacer(Modifier.height(4.dp))
				TitledTextField(
					title = "Название",
					text = name,
					onValueChange = {
						name = it
					}
				)
				Spacer(Modifier.height(8.dp))
				TitledTextField(
					title = "Описание",
					text = desc,
					onValueChange = {
						desc = it
					},
					isSingleLine = false
				)
				Spacer(Modifier.height(12.dp))
				StyledButton(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 8.dp),
					text = "Подтвердить",
					onClick = {
						onSubmit(name, desc)
						onDismiss()
					}
				)
			}
		}
	}
}

@Composable
fun DeleteDialog(
	itemName: String,
	onDismiss: () -> Unit,
	onSubmit: () -> Unit,
) {
	Dialog(
		onDismissRequest = onDismiss
	) {
		StyledRoundedCard(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp)
		) {
			Column(
				modifier = Modifier.padding(12.dp)
			) {
				Text(
					text = "Удалить $itemName?",
					style = MaterialTheme.typography.body2.copy(
						fontSize = 28.sp
					),
				)
				Spacer(Modifier.height(4.dp))
				Text(
					text = "Это действие не может быть отменено",
					style = MaterialTheme.typography.body2.copy(
						fontSize = 20.sp,
						color = Color.Red,
					),
				)
				Spacer(Modifier.height(8.dp))
				StyledButton(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 8.dp),
					text = "Удалить",
					onClick = {
						onSubmit()
						onDismiss()
					}
				)
			}
		}
	}
}




@Composable
fun ShowMoreDialog(
	kard: KanbanState.Kard,
	onDismiss: () -> Unit,
) {

	val scrollState = rememberScrollState()

	Dialog(
		onDismissRequest = onDismiss
	) {
		StyledRoundedCard(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(12.dp)
					.heightIn(min=280.dp, max=480.dp)
			) {
				Column(
					modifier = Modifier
						.verticalScroll(scrollState)
				) {
					Text(
						text = kard.title,
						style = MaterialTheme.typography.body2.copy(
							fontSize = 28.sp
						),
						modifier = Modifier

					)
					Spacer(Modifier.height(12.dp))
					Text(
						text = kard.desc,
						style = MaterialTheme.typography.body2.copy(
							fontSize = 24.sp,
							fontWeight = FontWeight.Light
						),
						modifier = Modifier

					)
					Spacer(Modifier.height(12.dp))
					Text(
						text = "Автор: ${kard.authorName}",
						style = MaterialTheme.typography.body2.copy(
							fontSize = 18.sp,
							fontWeight = FontWeight.Thin
						),
						modifier = Modifier
							.padding(bottom = 4.dp)
					)
					Text(
						text = "Создано: ${kard.createdTimeMillis.toReadableTime()}",
						style = MaterialTheme.typography.body2.copy(
							fontSize = 18.sp,
							fontWeight = FontWeight.Thin
						),
						modifier = Modifier
							.padding(bottom = 4.dp)
					)
					Text(
						text = "Изменено: ${kard.updateTimeMillis.toReadableTime()}",
						style = MaterialTheme.typography.body2.copy(
							fontSize = 18.sp,
							fontWeight = FontWeight.Thin
						),
						modifier = Modifier
							.padding(bottom = 4.dp)
					)
				}
			}
		}
	}
}