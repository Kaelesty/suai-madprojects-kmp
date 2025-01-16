package com.kaelesty.madprojects.view.components.main

import com.arkivanov.decompose.ComponentContext

interface MainComponent {

    interface Factory {
        fun create(c: ComponentContext): MainComponent
    }

}

class DefaultMainComponent(
    componentContext: ComponentContext
): MainComponent, ComponentContext by componentContext {

    companion object {
        object Factory: MainComponent.Factory {
            override fun create(c: ComponentContext): MainComponent {
                return DefaultMainComponent(c)
            }

        }
    }
}