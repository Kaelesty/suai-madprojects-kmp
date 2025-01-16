package com.kaelesty.madprojects.view

import com.kaelesty.madprojects.view.components.componentsModule
import org.koin.dsl.module

val viewModule = module {
    includes(componentsModule)
}