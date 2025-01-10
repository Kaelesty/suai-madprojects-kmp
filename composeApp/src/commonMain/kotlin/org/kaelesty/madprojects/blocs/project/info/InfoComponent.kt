package com.kaelesty.madprojects_kmp.blocs.project.info

interface InfoComponent {

    sealed interface State {
        data object Loading: State

        data class Main(
            val repos: List<RepoItem>
        )

        data class RepoItem(
            val name: String,
            val url: String,
        )


    }
}