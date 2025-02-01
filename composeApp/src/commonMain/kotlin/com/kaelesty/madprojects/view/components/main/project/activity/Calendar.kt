package com.kaelesty.madprojects.view.components.main.project.activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.domain.repos.github.BranchCommitView
import com.kaelesty.madprojects.view.ui.experimental.Styled
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

fun monthDays(year: Int, month: Month): Int {
    val start = LocalDate(year, month, 1)
    val end = start.plus(1, DateTimeUnit.MONTH)
    return start.until(end, DateTimeUnit.DAY)
}

fun parseDate(dateString: String): LocalDate {
    val instant = Instant.parse(dateString)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

@Composable
fun CalendarView(
    branchCommits: List<BranchCommitView>,
    year: Int,
    month: ActivityComponent.State.Month
) {
    val commitsByDate = branchCommits
        .filter {
            parseDate(it.date).let {
                it.year == year && ActivityComponent.State.monthFromIndex(it.monthNumber) == month
            }
        }
        .groupBy { parseDate(it.date) }
    val currentDate = parseDate(branchCommits.first().date)
    val year = currentDate.year
    val month = currentDate.month

    val maxCommits = if (commitsByDate.isEmpty()) 0 else {
        commitsByDate.values
            .maxBy { it.size }
            .size
    }

    val firstDayOfMonth = LocalDate(year, month, 1)
    val lastDayOfMonth = LocalDate(year, month, monthDays(year, currentDate.month))

    val daysInMonth = (firstDayOfMonth.dayOfMonth..lastDayOfMonth.dayOfMonth).map { day ->
        val date = firstDayOfMonth.plus(day, DateTimeUnit.DAY)
        date to commitsByDate[date]?.size
    }

    val colors = Styled.uiKit().colors()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .padding(4.dp)
            .height(256.dp)
        ,
        userScrollEnabled = false
    ) {
        itemsIndexed(daysInMonth) { index, (date, commitCount) ->
            DayBox(
                date.dayOfMonth, commitCount ?: 0,
                color = commitCount?.let {
                    if (it == 0) colors.github_0
                    else if (it > maxCommits * 0.90f) colors.github_100
                    else if (it > maxCommits * 0.75f) colors.github_75
                    else if (it > maxCommits * 0.50f) colors.github_50
                    else if (it > maxCommits * 0.25f) colors.github_25
                    else colors.github_0
                } ?: colors.github_0
            )
        }
    }
}

@Composable
fun DayBox(day: Int, commitCount: Int, color: Color) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(2.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(2.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$day",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                )
            )
            Text(
                text = if (commitCount < 10) "$commitCount ком." else if (commitCount < 100) "$commitCount к."
                else "99+ к.",
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}