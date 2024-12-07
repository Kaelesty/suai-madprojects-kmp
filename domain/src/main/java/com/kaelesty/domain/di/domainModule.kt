package com.kaelesty.domain.di

import com.kaelesty.domain.common.JwtTool
import org.koin.dsl.module

val domainModule = module {
	includes(authModule, memberProfileModule)

	single<JwtTool> {
		object : JwtTool {

			private var jwt = ""

			override fun getJwt(): String {
				return jwt
			}

			override fun saveJwt(jwt_: String) {
				jwt = jwt_
			}

			override fun notifyJwtExpired() {
				//
			}
		}
	}
}