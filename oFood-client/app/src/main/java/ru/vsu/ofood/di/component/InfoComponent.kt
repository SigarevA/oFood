package ru.vsu.ofood.di.component

import dagger.Subcomponent
import ru.vsu.ofood.di.modules.InfoModule
import ru.vsu.ofood.di.scopes.InfoTabScope
import ru.vsu.ofood.ui.info.InfoMiddleware

@Subcomponent(modules = [InfoModule::class])
@InfoTabScope
interface InfoComponent {
    fun getInfoMiddleware() : InfoMiddleware
}