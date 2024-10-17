package com.giraffe.quranpage.common.di

import com.giraffe.quranpage.data.repository.RepositoryImp
import com.giraffe.quranpage.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repositoryImp: RepositoryImp): Repository
}