package com.kaelesty.madprojects.view.components.main.project.activity

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.github.BranchCommits
import com.kaelesty.madprojects.domain.repos.github.GithubRepo
import com.kaelesty.madprojects.domain.repos.github.RepoBranchView
import com.kaelesty.madprojects.domain.repos.github.RepoView
import com.kaelesty.madprojects.domain.repos.sprints.ProfileSprint
import com.kaelesty.madprojects.domain.repos.sprints.SprintsRepo
import com.kaelesty.madprojects.view.components.main.project.ProjectComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface ActivityComponent {

    data class State(
        val sprints: List<ProfileSprint>?,
        val repoBranches: List<RepoBranchView>?,
        val selectedBranch: RepoBranchView? = null,
        val commits: CommitsState = CommitsState.Loading,
        val selectedMonth: Month,
        val selectedYear: Int,
        val years: List<Int>
    ) {

        enum class Month(val string: String) {
            Jan("Январь"), Feb("Февраль"), Mar("Март"), Apr("Апрель"),
            May("Май"), Jun("Июнь"), Jul("Июль"), Aug("Август"), Sep("Сентябрь"),
            Oct("Октябрь"), Nov("Ноябрь"), Dec("Декабрь")
        }

        companion object {
            fun monthFromIndex(index: Int): Month {
                return when (index) {
                    1 -> Month.Jan
                    2 -> Month.Feb
                    3 -> Month.Mar
                    4 -> Month.Apr
                    5 -> Month.May
                    6 -> Month.Jun
                    7 -> Month.Jul
                    8 -> Month.Aug
                    9 -> Month.Sep
                    10 -> Month.Oct
                    11 -> Month.Nov
                    else -> Month.Dec
                }
            }
        }
    }

    interface Navigator {

        fun toSprint(sprintId: String)

        fun toSprintCreation()
    }

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): ActivityComponent
    }

    val state: StateFlow<State?>

    fun toSprint(sprintId: String)

    fun toSprintCreation()

    fun selectBranch(new: RepoBranchView)

    fun setMonth(month: State.Month)

    fun setYear(year: Int)
}

class DefaultActivityComponent(
    private val componentContext: ComponentContext,
    private val navigator: ActivityComponent.Navigator,
    private val projectId: String,
    private val sprintsRepo: SprintsRepo,
    private val githubRepo: GithubRepo,
) : ComponentContext by componentContext, ActivityComponent {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val repos = MutableStateFlow<List<RepoView>>(listOf())

    private val _state = MutableStateFlow<ActivityComponent.State?>(null)
    override val state: StateFlow<ActivityComponent.State?>
        get() = _state.asStateFlow()

    init {
        loadState()
    }

    override fun setMonth(month: ActivityComponent.State.Month) {
        scope.launch {
            _state.emit(
                _state.value?.copy(
                    selectedMonth = month
                )
            )
        }
    }

    override fun setYear(year: Int) {
        scope.launch {
            _state.emit(
                _state.value?.copy(
                    selectedYear = year
                )
            )
        }
    }

    private fun loadState() {
        scope.launch {

            val repoBranches = githubRepo.getProjectRepoBranches(projectId).getOrNull()
                ?.also { repos.emit(it) }
                ?.flatMap { repoView ->
                    repoView.repoBranches.map {
                        it.copy(
                            name = it.name
                        )
                    }
                }
            val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            var years = listOf<Int>()
            _state.emit(
                ActivityComponent.State(
                    sprints = sprintsRepo.getProjectSprints(projectId).getOrNull(),
                    repoBranches = repoBranches,
                    selectedBranch = repoBranches?.firstOrNull(),
                    commits = if (repoBranches.isNullOrEmpty()) CommitsState.Null else {
                        repoBranches.first().let {
                            loadRepoBranchContent(it)?.let {
                                years = it.commits.map {
                                    parseDate(it.date).year
                                }.distinct()
                                CommitsState.Main(it)
                            } ?: CommitsState.Error
                        }
                    },
                    selectedMonth = ActivityComponent.State.monthFromIndex(dateTime.monthNumber),
                    selectedYear = dateTime.year,
                    years = years
                )
            )
        }
    }

    private suspend fun loadRepoBranchContent(repoBranchView: RepoBranchView): BranchCommits? {
        return repos.value.firstOrNull { it.repoBranches.contains(repoBranchView) }?.let {
            githubRepo.getRepoBranchContent(
                sha = repoBranchView.sha,
                repoName = it.name
            ).getOrNull()
        }
    }

    override fun toSprint(sprintId: String) = navigator.toSprint(sprintId)

    override fun toSprintCreation() = navigator.toSprintCreation()

    override fun selectBranch(new: RepoBranchView) {
        scope.launch {
            _state.emit(
                state.value?.copy(
                    selectedBranch = new,
                    commits = CommitsState.Loading
                )
            )
            var years = listOf<Int>()
            _state.emit(
                state.value?.copy(
                    commits = loadRepoBranchContent(new)?.let {
                        years = it.commits.map {
                            parseDate(it.date).year
                        }.distinct()
                        CommitsState.Main(it)
                    } ?: CommitsState.Error,
                    years = years,
                    selectedYear = years.firstOrNull() ?: 2025
                )
            )
        }
    }
}

sealed interface CommitsState {

    class Main(val value: BranchCommits): CommitsState

    data object Error: CommitsState

    data object Loading: CommitsState

    data object Null: CommitsState
}